package com.tizfaver.randomteleporteverytotsecond;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandListener implements CommandExecutor {
    //private Plugin plugin = RandomTeleportEveryTotSecond.getPlugin(RandomTeleportEveryTotSecond.class);
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String [] args) {
        if(!(sender instanceof Player)) return false;

        Player player = (Player) sender;
        if(args[0].equals("start")) {
            RandomTeleportEveryTotSecond.setTrue();
            player.sendMessage(ChatColor.GREEN + "Starting the random TP!");
        }
        else if (args[0].equals("stop")) {
            RandomTeleportEveryTotSecond.setFalse();
            player.sendMessage(ChatColor.GOLD + "Stopping the random TP!");
        } else {
            player.sendMessage(ChatColor.RED + "You typed the wrong command, usage: /rtp <start/stop>");
        }
        return true;
    }
}