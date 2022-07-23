package com.tizfaver.damagetpyou;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.Random;

public class DamageListener implements Listener {
    @EventHandler
    public void onPlayerDamage(EntityDamageEvent e) {
        if(e.getEntity() instanceof Player){
            if(DamageTPYou.status == true) {
                Random rand = new Random();
                for (Player p : Bukkit.getOnlinePlayers()) {
                    p.sendMessage();
                    int x = rand.nextInt(10000);
                    int y = 500;
                    int z = rand.nextInt(10000);
                    while (new Location(p.getWorld(), x, y, z).getBlock().getType().name().toLowerCase().contains("air")) {
                        y = y - 1;
                    }
                    y++;
                    String command = "tp " + p.getName() + " " + x + " " + y + " " + z;
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
                }
            } else { }
        }
    }
}
