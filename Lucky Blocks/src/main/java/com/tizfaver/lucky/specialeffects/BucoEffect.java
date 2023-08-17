package com.tizfaver.lucky.specialeffects;

import com.tizfaver.lucky.utils.SpecialEffect;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;

public class BucoEffect implements SpecialEffect {

    @Override
    public void runEffect(Player player, Block block, BlockBreakEvent event) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.RED + "watch out the hole!"));
        Location loc = block.getLocation();
        World world = loc.getWorld();
        int startX = loc.getBlockX() - 1;
        int startY = loc.getBlockY() - 100;
        int startZ = loc.getBlockZ() - 1;

        for (int x = startX; x <= startX + 2; x++) {
            for (int y = startY; y <= loc.getBlockY() + 5; y++) {
                for (int z = startZ; z <= startZ + 2; z++) {
                    Block bloc = world.getBlockAt(x, y, z);
                    bloc.setType(Material.AIR);
                }
            }
        }

        player.playSound(player.getLocation(), "mob.endermen.portal", 0.5f, 1.0f);
        player.playSound(player.getLocation(), "entity.enderman.teleport", 0.5f, 1.0f);
    }

}
