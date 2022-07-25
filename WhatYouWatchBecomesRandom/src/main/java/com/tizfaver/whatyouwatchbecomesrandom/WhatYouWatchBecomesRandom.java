package com.tizfaver.whatyouwatchbecomesrandom;

import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public final class WhatYouWatchBecomesRandom extends JavaPlugin {
    public static boolean status = true;

    @Override
    public void onEnable() {
        getServer().getConsoleSender().sendMessage(ChatColor.GOLD + "[Watch is random] started!");
        getCommand("rblock").setExecutor(new CommandListener());
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
