package me.fullidle.backtip.backtip;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.plugin.EventExecutor;

import static me.fullidle.backtip.backtip.Main.backMsgCom;

public class PlayerListener implements Listener, EventExecutor {
    @Override
    public void execute(Listener listener, Event event) {
        PlayerEvent e = (PlayerEvent) event;
        Player player = e.getPlayer();
        player.spigot().sendMessage(backMsgCom);
    }
}
