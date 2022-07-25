package com.tizfaver.whatyouwatchbecomesrandom;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.Random;

public class WatchEvent implements Listener {
    private static final Material[] matlist = Material.values();
    private static Block lastBlock = null;
    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        if(WhatYouWatchBecomesRandom.status == true) {
            Player player = e.getPlayer();
            Block newBlock = player.getTargetBlock(null, 5);
            Location loc = newBlock.getLocation();
            int random = new Random().nextInt(matlist.length);
            Material mat = matlist[random];
            while(!mat.isBlock()){
                random = new Random().nextInt(matlist.length);
                mat = matlist[random];
            }

            if(!newBlock.getType().equals(Material.AIR)) {
               try {
                    if (newBlock.getLocation().getBlockX() != lastBlock.getLocation().getBlockX() || newBlock.getLocation().getBlockY() != lastBlock.getLocation().getBlockY() || newBlock.getLocation().getBlockZ() != lastBlock.getLocation().getBlockZ()) {

                        try {
                            player.getWorld().getBlockAt(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()).setType(mat);
                        } catch (Exception ee) { }
                        Block newBlock2 = player.getTargetBlock(null, 5);
                        lastBlock = newBlock2;
                    } else {
                    }
               } catch (Exception eee){ //happens only first time, when lastBlock is null.
                    Block newBlock2 = player.getTargetBlock(null, 5);
                    lastBlock = newBlock2;
                    lastBlock = newBlock2;
                }
            }
        } else { }
    }
}
