package com.tizfaver.whatyouwatchbecomesrandom;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandListener implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String [] args) {
        if(!(sender instanceof Player)){
            sender.sendMessage(ChatColor.RED + "Only player can get this Item!");
            return false;
        }
        Player player = (Player) sender;
        if(args[0].equals("start")) {
            WhatYouWatchBecomesRandom.setTrue();
            player.sendMessage(ChatColor.GREEN + "Starting the Replacing with random!");
        }
        else if (args[0].equals("stop")) {
            WhatYouWatchBecomesRandom.setFalse();
            player.sendMessage(ChatColor.GOLD + "Stopping the Replacing with random!");
        } else {
            player.sendMessage(ChatColor.RED + "You typed the wrong command, usage: /rblock <start/stop>");
        }
        return true;
    }

}
