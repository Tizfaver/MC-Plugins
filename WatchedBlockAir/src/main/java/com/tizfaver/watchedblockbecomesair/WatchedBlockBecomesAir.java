package com.tizfaver.watchedblockbecomesair;

import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public final class WatchedBlockBecomesAir extends JavaPlugin {
    public static boolean status = true;
    @Override
    public void onEnable() {
        getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "[Replace with Air] started!");
        getCommand("ra").setExecutor(new CommandListener());
        getServer().getPluginManager().registerEvents(new WatchEvent(), this);
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
