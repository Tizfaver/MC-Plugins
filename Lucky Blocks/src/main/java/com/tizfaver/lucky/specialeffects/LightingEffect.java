package com.tizfaver.lucky.specialeffects;

import com.tizfaver.lucky.Main;
import com.tizfaver.lucky.utils.SpecialEffect;
import org.bukkit.GameMode;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class LightingEffect implements SpecialEffect {

    @Override
    public void runEffect(Player player, Block block, BlockBreakEvent event) {
        Main plugin = Main.getInstance();

        final int[] i = {0};
        new BukkitRunnable() {
            @Override
            public void run() {
                if(i[0] == 3)
                    cancel();
                if(!player.getGameMode().equals(GameMode.SPECTATOR)){ player.getWorld().strikeLightning(player.getLocation()); }
                i[0]++;
            }
        }.runTaskLater(plugin, 15L);

    }
}
