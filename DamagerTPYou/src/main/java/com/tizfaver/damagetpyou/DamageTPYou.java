package com.tizfaver.damagetpyou;

import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public final class DamageTPYou extends JavaPlugin {
    public static boolean status = true;
    @Override
    public void onEnable() {
        getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "[Damage TP] started!");
        getCommand("dtp").setExecutor(new CommandListener());
        getServer().getPluginManager().registerEvents(new DamageListener(), this);
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
