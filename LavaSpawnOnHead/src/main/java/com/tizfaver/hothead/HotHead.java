package com.tizfaver.hothead;

import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public final class HotHead extends JavaPlugin {
    public static boolean status = true;
    @Override
    public void onEnable() {
        getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "[Lava Spawn] started!");
        getCommand("sl").setExecutor(new CommandListener());
        getServer().getPluginManager().registerEvents(new MoveListener(), this);
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
