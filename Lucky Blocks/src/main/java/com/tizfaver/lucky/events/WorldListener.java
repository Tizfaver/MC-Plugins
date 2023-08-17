package com.tizfaver.lucky.events;

import com.tizfaver.lucky.Main;
import com.tizfaver.lucky.enums.GameState;
import com.tizfaver.lucky.managers.ItemManager;
import com.tizfaver.lucky.managers.SpecialEffectsManager;
import com.tizfaver.lucky.utils.Game;
import com.tizfaver.lucky.utils.Team;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.inventory.ItemStack;

public class WorldListener implements Listener {

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Block block = event.getBlock();

        if(!Main.getInstance().getGame().gameState.equals(GameState.PLAYING)){
            event.setCancelled(true);
            return;
        }

        if (!block.getType().equals(Material.SPONGE)) return;

        Main plugin = Main.getInstance();
        ItemManager itemManager = plugin.getItemManager();
        SpecialEffectsManager specialEffectsManager = plugin.getSpecialEffectsManager();

        int r = Main.getRandom().nextInt(15) + 1;
        if (r <= 11){
            try{
                block.getWorld().dropItem(block.getLocation(), itemManager.getRandomItem());
            } catch (IllegalArgumentException out){
                event.getPlayer().sendMessage(ChatColor.RED + "No items registered in the lucky blocks.");
            } catch (Exception err){ }
        } else {
            specialEffectsManager.getRandomSpecialEffect().runEffect(event.getPlayer(), event.getBlock(), event);
        }

        event.setDropItems(false);
    }

    @EventHandler
    public void onNexusBreak(BlockBreakEvent event) {
        Main plugin = Main.getInstance();
        Game game = plugin.getGame();
        Location loc = new Location(event.getBlock().getWorld(), (int) event.getBlock().getLocation().getX(), (int) event.getBlock().getLocation().getY(), (int) event.getBlock().getLocation().getZ());
        Team team = game.getTeamByNexusLocation(loc);

        if(!Main.getInstance().getGame().gameState.equals(GameState.PLAYING)){
            event.setCancelled(true);
            return;
        }

        if (team == null) return;

        if(team.getHealth() < 1){ return; }

        if(!event.getBlock().getType().equals(Material.getMaterial(plugin.getConfig().getString("other.nexus-block")))) return;

        Player player = event.getPlayer();

        if(team.getPlayerList().contains(player)) {
            player.sendMessage(ChatColor.DARK_RED + "You can't destroy your nexus!");
            player.playSound(player.getLocation(), "entity.villager.no", 1.0f, 1.0f);
            event.setCancelled(true);
            return;
        }

        //since it's cancelled, it's manually called the usage of the item.
        ItemStack item = event.getPlayer().getInventory().getItemInMainHand();
        PlayerItemDamageEvent itemUsed = new PlayerItemDamageEvent(player, item, 1);
        plugin.getServer().getPluginManager().callEvent(itemUsed);

        //if the item has below 0 of durability, it will get destroyed since just removing, like before, will not work (going negative)
        if ((item.getType().getMaxDurability() - item.getDurability()) < 0) {
            item.setAmount(item.getAmount() - 1);
            player.getInventory().setItemInMainHand(item);
        }

        team.removeHealth(1);

        if (team.getHealth() == 0) {
            player.sendMessage(ChatColor.GREEN + "You destroyed " + team.getColor() + "" + team.getTeamName() + ChatColor.GREEN + "'s nexus!");
            plugin.getStats().insertStats(player, 0, 0, 1);
            Bukkit.broadcastMessage(" \n" + ChatColor.WHITE + "" + ChatColor.BOLD + "NEXUS DESTRUCTION > " + ChatColor.RESET + "" + team.getColor() + "" + team.getTeamName() + "" + ChatColor.GRAY + "'s " +
                    "Nexus was destroyed by " + game.getTeamByPlayer(player).getColor() + "" + player.getName() + ChatColor.GRAY + "! \n" + " \n");
        } else{
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.YELLOW + "This nexus has " + ChatColor.GREEN + team.getHealth() + ChatColor.YELLOW + " of health" + ChatColor.WHITE + "."));
            player.playSound(player.getLocation(), "block.lava.pop", 1.0f, 1.0f);
        }

        for(Player playerTeam : team.getPlayerList()){
            playerTeam.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.RED + "Your nexus is under attack! health remaining: " + ChatColor.GREEN + team.getHealth() + ChatColor.WHITE + "."));
            //alert the players of the team with a sound
            playerTeam.playSound(playerTeam.getLocation(), "note.pling", 0.5f, 1.0f);
            playerTeam.playSound(playerTeam.getLocation(), "block.note_block.pling", 0.5f, 1.0f);

            if (team.getHealth() == 0){
                playerTeam.sendTitle(ChatColor.RED + "NEXUS DESTROYED!", ChatColor.WHITE + "You will no longer respawn!", 5, 80, 20);
                //sound ender dragon for every player:
                for(Player p : Bukkit.getOnlinePlayers()){
                    Location playerLocation = playerTeam.getLocation();
                    double distance = event.getBlock().getLocation().distance(playerLocation);
                    float maxVolume = 1.0f;
                    int maxDistance = 500;

                    float volume = (float) (maxVolume * (1.0 - (distance / maxDistance)));

                    p.playSound(playerLocation, "entity.enderdragon.growl", volume, 1.0f);
                    p.playSound(playerLocation, "mob.enderdragon.growl", volume, 1.0f);
                }
                event.setCancelled(false);
                return;
            }
        }

        event.setCancelled(true);
    }

    @EventHandler
    public void onPlaceBlock(BlockPlaceEvent event){
        if(!Main.getInstance().getGame().gameState.equals(GameState.PLAYING)){
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockPhysics(BlockPhysicsEvent event) {
        Block block = event.getBlock();

        //disable the absorption of water from the sponge
        if (block.getType() == Material.SPONGE) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onItemSpawn(ItemSpawnEvent event) {
        try {
            if (event.getEntity() != null) {
                event.getEntity().setCustomName(event.getEntity().getItemStack().getItemMeta().getDisplayName());
                event.getEntity().setCustomNameVisible(true);
            }
        } catch (Exception err){ }

        if (event.getEntity() instanceof EnderPearl) {
            World world = event.getLocation().getWorld();
            world.playSound(event.getLocation(), "mob.endermen.portal", 0.5f, 1.0f);
            world.playSound(event.getLocation(), "entity.enderman.teleport", 0.5f, 1.0f);
        }
    }

}
