package com.tizfaver.lucky.utils;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;

@FunctionalInterface
public interface SpecialEffect {
    void runEffect(Player player, Block block, BlockBreakEvent event);
}
