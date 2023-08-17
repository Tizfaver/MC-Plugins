package com.tizfaver.lucky.specialeffects;

import com.tizfaver.lucky.Main;
import com.tizfaver.lucky.utils.SpecialEffect;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class FlyEffect implements SpecialEffect {

    @Override
    public void runEffect(Player player, Block block, BlockBreakEvent event) {
        player.setAllowFlight(true);
        player.setFlying(true);
        int seconds = Main.getInstance().getTrapsConfig().getInt("fly-effect.seconds");
        player.sendMessage(ChatColor.GREEN + "You can now fly for " + seconds + " seconds! fly little bird!");

        player.playSound(player.getLocation(), "mob.bat.takeoff", 0.5f, 1.0f);
        player.playSound(player.getLocation(), "entity.bat.takeoff", 0.5f, 1.0f);

        new BukkitRunnable() {
            @Override
            public void run() {
                if(!player.getGameMode().equals(GameMode.CREATIVE)){
                    player.setFlying(false);
                    player.setAllowFlight(false);
                }
            }
        }.runTaskLater(Main.getInstance(), seconds * 20L);
    }
}
