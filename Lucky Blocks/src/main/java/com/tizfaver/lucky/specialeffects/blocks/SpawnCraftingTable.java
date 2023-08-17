package com.tizfaver.lucky.specialeffects.blocks;

import com.tizfaver.lucky.utils.SpecialEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;

public class SpawnCraftingTable implements SpecialEffect {

    @Override
    public void runEffect(Player player, Block block, BlockBreakEvent event) {
        event.setCancelled(true);
        if (block.getType() == Material.AIR || block.getType().isTransparent() || block.isLiquid()) {
            return;
        }

        World world = block.getWorld();

        Location location = block.getLocation();
        location.add(0.5, 0, 0.5);

        Block craftingTableBlock = world.getBlockAt(location);
        craftingTableBlock.setType(Material.WORKBENCH);
    }
}
