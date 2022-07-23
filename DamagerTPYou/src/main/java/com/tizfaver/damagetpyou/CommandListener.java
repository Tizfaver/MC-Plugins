package com.tizfaver.damagetpyou;

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
            DamageTPYou.setTrue();
            player.sendMessage(ChatColor.GREEN + "Starting the Random TP when Damage!");
        }
        else if (args[0].equals("stop")) {
            DamageTPYou.setFalse();
            player.sendMessage(ChatColor.GOLD + "Stopping the Random TP when Damage!");
        } else {
            player.sendMessage(ChatColor.RED + "You typed the wrong command, usage: /dtp <start/stop>");
        }
        return true;
    }
}
