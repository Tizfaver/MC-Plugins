package com.tizfaver.lucky.specialeffects.blocks;

import com.tizfaver.lucky.utils.SpecialEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;

public class SpawnAnvil implements SpecialEffect {

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
        craftingTableBlock.setType(Material.ANVIL);

        player.playSound(player.getLocation(), "random.anvil_use", 1.0f, 1.0f);
        player.playSound(player.getLocation(), "block.anvil.use", 1.0f, 1.0f);
    }
}
