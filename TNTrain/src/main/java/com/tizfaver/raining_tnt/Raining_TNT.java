package com.tizfaver.raining_tnt;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public final class Raining_TNT extends JavaPlugin {
    public static boolean status = true;

    @Override
    public void onEnable() {
        getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "[TNT rain] started!");
        getCommand("rt").setExecutor(new CommandListener());

        getConfig().options().copyDefaults(true);
        saveConfig();

        int delay = getConfig().getInt("delayTNT");
        getServer().getScheduler().runTaskTimerAsynchronously(this, () -> {
            if(Raining_TNT.status == true) {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    String command = "summon minecraft:tnt " + p.getLocation().getX() + " " + (p.getLocation().getY() + 5) + " " + p.getLocation().getZ() + " {Fuse:50}";

                    getServer().getScheduler().runTask(this, () -> {
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
                    });
                }
            } else { }
        }, 1 * 20, delay * 20);

    }

    public static void setTrue(){
        status = true;
    }
    public static void setFalse(){
        status = false;
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}