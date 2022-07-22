package com.tizfaver.raining_tnt;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class CommandListener implements CommandExecutor {
    private Plugin plugin = Raining_TNT.getPlugin(Raining_TNT.class);
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String [] args) {
        if(!(sender instanceof Player)) return false;

        Player player = (Player) sender;
        if(args[0].equals("start")) {
            Raining_TNT.setTrue();
            player.sendMessage(ChatColor.GREEN + "Starting the TNT rain!");
        }
        else if (args[0].equals("stop")) {
            Raining_TNT.setFalse();
            player.sendMessage(ChatColor.GOLD + "Stopping the TNT rain!");
        } else {
            player.sendMessage(ChatColor.RED + "You typed the wrong command, usage: /RT <start/stop>");
        }
        return true;
    }
}