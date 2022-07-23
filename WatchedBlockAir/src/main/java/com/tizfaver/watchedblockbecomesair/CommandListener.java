package com.tizfaver.watchedblockbecomesair;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandListener implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String [] args) {
        if(!(sender instanceof Player)) return false;

        Player player = (Player) sender;
        if(args[0].equals("start")) {
            WatchedBlockBecomesAir.setTrue();
            player.sendMessage(ChatColor.GREEN + "Starting the Replacing!");
        }
        else if (args[0].equals("stop")) {
            WatchedBlockBecomesAir.setFalse();
            player.sendMessage(ChatColor.GOLD + "Stopping the Replacing!");
        } else {
            player.sendMessage(ChatColor.RED + "You typed the wrong command, usage: /ra <start/stop>");
        }
        return true;
    }
}
