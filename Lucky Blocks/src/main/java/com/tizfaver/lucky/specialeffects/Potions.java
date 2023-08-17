package com.tizfaver.lucky.specialeffects;

import com.tizfaver.lucky.Main;
import com.tizfaver.lucky.utils.Utils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Potions implements Listener {

    @EventHandler
    public void onPlayerItemConsume(PlayerItemConsumeEvent event) {
        Main plugin = Main.getInstance();
        Player player = event.getPlayer();

        if (event.getItem().getType() == Material.POTION
                && event.getItem().hasItemMeta()
                && event.getItem().getItemMeta().hasDisplayName()
                && event.getItem().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.GOLD + "" + ChatColor.BOLD + "" +
                ChatColor.ITALIC + "Lucky " + ChatColor.YELLOW + "" + ChatColor.ITALIC + "Potion")) {

            int chance = Main.getRandom().nextInt(100) + 1; // Generate a random number from 1 to 100

            if (chance <= 80) { //80% chance of good effects
                Utils.setPlayerHealth(player, 20.0);
                player.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 1200, 4));
                player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 1200, 4));
            } else { //20% chance of bad effects
                player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 1200, 3));
                player.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 300, 3));
                player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 1200, 3));
                player.setHealth(2);
            }
        } else if (event.getItem().getType() == Material.POTION
                && event.getItem().hasItemMeta()
                && event.getItem().getItemMeta().hasDisplayName()
                && event.getItem().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.RED + "" + ChatColor.BOLD + "" +
                ChatColor.ITALIC + "Unlucky " + ChatColor.YELLOW + "" + ChatColor.ITALIC + "Potion")) {

            int chance = Main.getRandom().nextInt(100) + 1; // Generate a random number from 1 to 100

            if (chance <= 80) { //80% chance of bad effects
                player.setHealth(player.getMaxHealth()); // Set player's health to maximum
                player.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 1200, 2));
                player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 1200, 4));
                player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 1200, 2));
            } else { //20% chance of really good effects
                player.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 1200, 6));
                player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 1200, 6));
                player.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 1200, 4));
                player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 1200, 4));
                player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 1200, 4));
                Utils.setPlayerHealth(player, 20.0);
            }

        }
    }
}
