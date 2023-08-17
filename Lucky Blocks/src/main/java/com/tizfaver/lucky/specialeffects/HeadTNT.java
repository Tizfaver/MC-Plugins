package com.tizfaver.lucky.specialeffects;

import com.tizfaver.lucky.utils.SpecialEffect;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.block.BlockBreakEvent;

public class HeadTNT implements SpecialEffect {

    @Override
    public void runEffect(Player player, Block block, BlockBreakEvent event) {
        int yAbovePlayer = 5;
        TNTPrimed tnt = (TNTPrimed) player.getWorld().spawnEntity(player.getLocation().add(0, yAbovePlayer, 0), EntityType.PRIMED_TNT);
        tnt.setFuseTicks(3 * 20);
    }
}
