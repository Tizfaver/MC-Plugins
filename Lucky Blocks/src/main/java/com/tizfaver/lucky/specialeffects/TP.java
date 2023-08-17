package com.tizfaver.lucky.specialeffects;

import com.tizfaver.lucky.Main;
import com.tizfaver.lucky.utils.SpecialEffect;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.ArrayList;
import java.util.List;

public class TP implements SpecialEffect {
    @Override
    public void runEffect(Player player, Block block, BlockBreakEvent event) {
        List<Player> tippablePlayers = new ArrayList<>();

        for(Player p : Bukkit.getOnlinePlayers()){
            if(!p.getGameMode().equals(GameMode.SPECTATOR))
                tippablePlayers.add(p);
        }

        tippablePlayers.remove(player);

        if (!tippablePlayers.isEmpty()) {
            Player randomPlayer = tippablePlayers.get(Main.getRandom().nextInt(tippablePlayers.size()));

            player.teleport(randomPlayer.getLocation());
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.RED + " I brought you to a friend!"));
        } else {
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.RED + " Nobody to TP!"));
        }

        player.playSound(player.getLocation(), "mob.endermen.idle", 0.5f, 1.0f);
        player.playSound(player.getLocation(), "entity.enderman.ambient", 0.5f, 1.0f);
    }
}
