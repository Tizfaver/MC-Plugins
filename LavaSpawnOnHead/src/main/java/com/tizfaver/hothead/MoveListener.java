package com.tizfaver.hothead;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class MoveListener implements Listener {
    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        if(HotHead.status == true) {
            Player player = e.getPlayer();
            Location from = e.getFrom();
            Location to = e.getTo();
            Location pLoc = player.getLocation();
            if(from.getX() != to.getX() || from.getZ() != to.getZ() || from.getY() != to.getY()) {
                player.getWorld().getBlockAt((int) pLoc.getX(), (int) (pLoc.getY() + 10), (int) pLoc.getZ()).setType(Material.LAVA);
            }
        } else { }
    }
}
