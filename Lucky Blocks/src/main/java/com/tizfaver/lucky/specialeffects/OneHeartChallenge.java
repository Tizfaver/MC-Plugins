package com.tizfaver.lucky.specialeffects;

import com.tizfaver.lucky.utils.SpecialEffect;
import com.tizfaver.lucky.utils.Utils;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;

public class OneHeartChallenge implements SpecialEffect {

    @Override
    public void runEffect(Player player, Block block, BlockBreakEvent event) {
        player.sendMessage(ChatColor.RED + "One heart Challenge!");
        Utils.setPlayerHealth(player, 2.0);
    }
}
