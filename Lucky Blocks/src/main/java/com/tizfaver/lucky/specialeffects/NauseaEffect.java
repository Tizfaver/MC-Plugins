package com.tizfaver.lucky.specialeffects;

import com.tizfaver.lucky.utils.SpecialEffect;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class NauseaEffect implements SpecialEffect {

    @Override
    public void runEffect(Player player, Block block, BlockBreakEvent event) {
        player.sendMessage(ChatColor.RED + "Omg Taco Bell, uh ah...");
        player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 12 * 20, 1));
    }

}
