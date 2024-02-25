package me.fullidle.backtip.backtip;


import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.plugin.EventExecutor;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    public static BaseComponent[] backMsgCom;
    public static Main main;
    public static PlayerListener listener = new PlayerListener();
    @Override
    public void onEnable() {
        main = this;
        reloadConfig();
        //注册指令//事件的监听注册在更具配置文件进行变化的,所以在reloadConfig内
        new CMD(getDescription().getName().toLowerCase());

        getLogger().info("Plugin loaded!");
    }

    @Override
    public void reloadConfig() {
        saveDefaultConfig();
        super.reloadConfig();
        {
            //重载监听器
            for (String clsN : this.getConfig().getStringList("listeners")) {
                try {
                    Class<?> cls = Class.forName(clsN);
                    //判断是否继承了PlayerEvent
                    if (!PlayerEvent.class.isAssignableFrom(cls)) {
                        getLogger().info("§cThe configured §b"+clsN+"§c does not extend Player Event!");
                        continue;
                    }
                    getServer().getPluginManager().registerEvent((Class<? extends PlayerEvent>) cls
                            ,listener
                            , EventPriority.NORMAL
                            ,listener
                            ,this);
                } catch (ClassNotFoundException e) {
                    getLogger().info("§b"+clsN+"§c class not found!(config.yml content error!)");
                }
            }
        }
        {
            //加载信息格式
            String buttonText = getConfigColorMsg("backMsg.button.text");
            boolean hover = main.getConfig().getBoolean("backMsg.button.hover.enable");
            String hoverText = getConfigColorMsg("backMsg.button.hover.text");
            String backMsgText = getConfigColorMsg("backMsg.text");
            //按钮组件
            ComponentBuilder buttonBuilder = new ComponentBuilder(buttonText)
                    .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND,"/back"));
            if (hover){
                BaseComponent[] hoverT = new ComponentBuilder(hoverText).create();
                buttonBuilder.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT,hoverT));
            }
            BaseComponent[] button = buttonBuilder.create();
            //组合完整组件
            String[] split = backMsgText.split("\\{button}");
            System.out.println(split.length);
            ComponentBuilder builder = new ComponentBuilder(split[0]);
            for (int i = 1; i < split.length; i++) {
                builder.append(button).append(new ComponentBuilder(split[i]).create());
            }
            //如果字符串结尾是{button}
            if (backMsgText.endsWith("{button}")){
                builder.append(button);
            }
            backMsgCom = builder.create();
        }
    }

    public static String getColorMsg(String msg){
        return msg.replace("&","§");
    }
    public static String getConfigColorMsg(String path){
        String s = main.getConfig().getString(path);
        if (s==null) {
            return null;
        }
        return getColorMsg(s);
    }

    public static boolean isSubclassOfPlayerEvent(Class<?> cls){
        do {
            if (cls == PlayerEvent.class) {
                return true;
            }
            cls = cls.getSuperclass();
        } while (cls != null && !cls.equals(Object.class));
        return false;
    }
}