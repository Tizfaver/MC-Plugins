package com.tizfaver.randomteleporteverytotsecond;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.BatToggleSleepEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.Random;

public final class RandomTeleportEveryTotSecond extends JavaPlugin {
    public static boolean status = true;

    @Override
    public void onEnable() {
        getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "[Random TP] started!");
        getCommand("rtp").setExecutor(new CommandListener());

        getConfig().options().copyDefaults(true);
        saveConfig();

        int delay = getConfig().getInt("delayTP");
        getServer().getScheduler().runTaskTimerAsynchronously(this, () -> {
            if(RandomTeleportEveryTotSecond.status == true) {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    p.sendMessage(ChatColor.GREEN + "3");
                    forceDelay(1000);
                    p.sendMessage(ChatColor.YELLOW + "2");
                    forceDelay(1000);
                    p.sendMessage(ChatColor.RED + "1");
                }

                Random rand = new Random();
                //generate random tp
                //check if is not under block
                for (Player p : Bukkit.getOnlinePlayers()) {
                    p.sendMessage();
                    int x = rand.nextInt(10000);
                    int y = 500;
                    int z = rand.nextInt(10000);
                    while(new Location(p.getWorld(), x, y, z).getBlock().getType().name().toLowerCase().contains("air")) {
                        y = y - 1;
                    }
                    y++;
                    String command = "tp " + p.getName() + " " + x + " " + y + " " + z;

                    getServer().getScheduler().runTask(this, () -> {
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
                    });
                }
            } else { }
        }, 1 * 20, delay * 20);

    }

    public void forceDelay(int mil){
        try {
            Thread.sleep(mil);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
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
