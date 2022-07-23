package com.tizfaver.hothead;

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
            HotHead.setTrue();
            player.sendMessage(ChatColor.GREEN + "Starting the Spawning lava!");
        }
        else if (args[0].equals("stop")) {
            HotHead.setFalse();
            player.sendMessage(ChatColor.GOLD + "Stopping the Spawning lava!");
        } else {
            player.sendMessage(ChatColor.RED + "You typed the wrong command, usage: /sl <start/stop>");
        }
        return true;
    }
}
