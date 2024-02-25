package me.fullidle.backtip.backtip;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import static me.fullidle.backtip.backtip.Main.main;

public class CMD implements CommandExecutor {
    public CMD(String cmd){
        main.getCommand(cmd).setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.isOp()){
            sender.sendMessage("§cYou do not have permission!");
            return false;
        }
        main.reloadConfig();
        sender.sendMessage("§aReload completed!");
        return false;
    }
}
