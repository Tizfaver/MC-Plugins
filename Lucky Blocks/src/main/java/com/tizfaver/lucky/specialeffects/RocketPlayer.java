package com.tizfaver.lucky.specialeffects;

import com.tizfaver.lucky.utils.SpecialEffect;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.util.Vector;

public class RocketPlayer implements SpecialEffect {

    @Override
    public void runEffect(Player player, Block block, BlockBreakEvent event) {
        int height = 50;
        launchPlayer(player, height);
    }

    public void launchPlayer(Player player, int height) {
        player.setAllowFlight(false);

        Vector launchVector = new Vector(0, height, 0);

        player.setVelocity(launchVector);

        player.playSound(player.getLocation(), Sound.ENTITY_FIREWORK_LAUNCH, 1.0f, 1.0f);
        player.playSound(player.getLocation(), "fireworks.blast", 1.0f, 1.0f);
        player.playSound(player.getLocation(), "entity.firework_rocket.blast", 1.0f, 1.0f);
    }
}
