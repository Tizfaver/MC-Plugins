package com.tizfaver.watchedblockbecomesair;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class WatchEvent implements Listener {
    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        if(WatchedBlockBecomesAir.status == true) {
            Player player = e.getPlayer();
            Block block = player.getTargetBlock(null, 10);
            Location loc = block.getLocation();
            player.getWorld().getBlockAt(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()).setType(Material.AIR);
        } else { }
    }
}
