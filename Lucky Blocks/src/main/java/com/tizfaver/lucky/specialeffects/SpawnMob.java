package com.tizfaver.lucky.specialeffects;

import com.tizfaver.lucky.Main;
import com.tizfaver.lucky.utils.SpecialEffect;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class SpawnMob implements SpecialEffect {

    @Override
    public void runEffect(Player player, Block block, BlockBreakEvent event) {

        int r = Main.getRandom().nextInt(4) + 1;
        Location location = block.getLocation();

        if(r == 1){
            spawnDiamondZombie(location);
        } else if(r == 2){
            spawnGhast(location);
        } else if(r == 3){
            spawnExplodingVillager(location, player);
        } else if(r == 4){
            spawnTerroristDog(location, player);
        }
    }

    private void spawnTerroristDog(Location location, Player player){
        World world = player.getWorld();
        location.add(0, 1, 0);
        Wolf dog = (Wolf) world.spawnEntity(location, EntityType.WOLF);

        dog.setCustomName(ChatColor.RED + "" + ChatColor.BOLD +  "WOOF");
        dog.setCustomNameVisible(true);
        dog.setAngry(true);
        dog.setTarget(player);

        new BukkitRunnable() {
            @Override
            public void run() {
                if (!dog.isDead() && !player.isDead()) {
                    dog.setTarget(player);
                } else {
                    dog.remove();
                    cancel();
                }
            }
        }.runTaskTimer(Main.getInstance(), 0L, 20L); // Run every second (20 ticks)

        new BukkitRunnable() {
            int secondsPassed = 0;

            @Override
            public void run() {
                if (secondsPassed < 3) {
                    if (secondsPassed == 0) {
                        player.sendMessage(ChatColor.RED + "WOOOFFF " + ChatColor.YELLOW + "3 " + ChatColor.RED + "SECONDS!!!");
                    } else if (secondsPassed == 1) {
                        player.sendMessage(ChatColor.RED + "WOOOFFF " + ChatColor.YELLOW + "2 " + ChatColor.RED + "SECONDS!!!");
                    } else if (secondsPassed == 2) {
                        player.sendMessage(ChatColor.RED + "WOOOFFF " + ChatColor.YELLOW + "1 " + ChatColor.RED + "SECONDS!!!");
                    }
                    secondsPassed++;
                } else {
                    world.createExplosion(dog.getLocation(), 4f);
                    dog.remove();
                    cancel();
                }
            }
        }.runTaskTimer(Main.getInstance(), 0, 20L);
    }

    private void spawnExplodingVillager(Location location, Player player) {
        World world = location.getWorld();
        location.add(0, 1, 0);
        Villager villager = (Villager) world.spawnEntity(location, EntityType.VILLAGER);
        villager.setAI(false);
        villager.setInvulnerable(true);
        villager.setCustomName(ChatColor.RED + "" + ChatColor.BOLD +  "TERRORIST");
        villager.setCustomNameVisible(true);

        new BukkitRunnable() {
            int secondsPassed = 0;

            @Override
            public void run() {
                if (secondsPassed < 3) {
                    if (secondsPassed == 0) {
                        player.sendMessage(ChatColor.RED + "I'M GONNA EXPLODE IN " + ChatColor.YELLOW + "3 " + ChatColor.RED + "SECONDS!!!");
                    } else if (secondsPassed == 1) {
                        player.sendMessage(ChatColor.RED + "I'M GONNA EXPLODE IN " + ChatColor.YELLOW + "2 " + ChatColor.RED + "SECONDS!!!");
                    } else if (secondsPassed == 2) {
                        player.sendMessage(ChatColor.RED + "I'M GONNA EXPLODE IN " + ChatColor.YELLOW + "1 " + ChatColor.RED + "SECONDS!!!");
                    }
                    secondsPassed++;
                } else {
                    world.createExplosion(location, 4f);
                    villager.remove();
                    cancel();
                }
            }
        }.runTaskTimer(Main.getInstance(), 0, 20L);
    }

    private void spawnGhast(Location location){
        World world = location.getWorld();
        location.add(0, 10, 0);
        Ghast ghast = (Ghast) world.spawnEntity(location, EntityType.GHAST);
        ghast.setAI(true);
        ghast.setInvulnerable(false);

        new BukkitRunnable() {
            @Override
            public void run() {
                ghast.remove();
                cancel();
            }
        }.runTaskTimer(Main.getInstance(), 60 * 20L, 20L);
    }

    private void spawnDiamondZombie(Location location) {
        Zombie zombie = (Zombie) location.getWorld().spawnEntity(location, EntityType.ZOMBIE);

        //give the zombie full diamond armor
        ItemStack diamondHelmet = new ItemStack(Material.DIAMOND_HELMET);
        ItemStack diamondChestplate = new ItemStack(Material.DIAMOND_CHESTPLATE);
        ItemStack diamondLeggings = new ItemStack(Material.DIAMOND_LEGGINGS);
        ItemStack diamondBoots = new ItemStack(Material.DIAMOND_BOOTS);

        zombie.getEquipment().setHelmet(diamondHelmet);
        zombie.getEquipment().setChestplate(diamondChestplate);
        zombie.getEquipment().setLeggings(diamondLeggings);
        zombie.getEquipment().setBoots(diamondBoots);

        //give the zombie a diamond sword
        ItemStack diamondSword = new ItemStack(Material.DIAMOND_SWORD);
        zombie.getEquipment().setItemInMainHand(diamondSword);

        //make the zombie wear the armor and hold the sword
        zombie.setCanPickupItems(false);
        zombie.setRemoveWhenFarAway(false);
        zombie.setAI(true);

        zombie.setCustomName("§9§lB§3§lO§bB§l§f§lY");
        zombie.setCustomNameVisible(true);
    }
}
