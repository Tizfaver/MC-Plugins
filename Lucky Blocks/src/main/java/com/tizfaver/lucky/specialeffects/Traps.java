package com.tizfaver.lucky.specialeffects;

import com.tizfaver.lucky.Main;
import com.tizfaver.lucky.utils.SpecialEffect;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.material.MaterialData;


public class Traps implements SpecialEffect {
    @Override
    public void runEffect(Player player, Block b, BlockBreakEvent event) {
        player.sendMessage(ChatColor.RED + "Trapped!");
        World world = player.getWorld();
        Location location = player.getLocation();

        int x = location.getBlockX();
        int y = location.getBlockY();
        int z = location.getBlockZ();

        int chance = Main.getRandom().nextInt(10) + 1;

        if(chance <= 5){
            Location initial = player.getLocation();
            Block block = world.getBlockAt(new Location(world, initial.getX(), initial.getY()-1, initial.getZ()));
            block.setType(Material.OBSIDIAN);

            block = world.getBlockAt(new Location(world, initial.getX()+1, initial.getY()-1, initial.getZ())); block.setType(Material.OBSIDIAN);
            block = world.getBlockAt(new Location(world, initial.getX()-1, initial.getY()-1, initial.getZ())); block.setType(Material.OBSIDIAN);
            block = world.getBlockAt(new Location(world, initial.getX(), initial.getY()-1, initial.getZ()-1)); block.setType(Material.OBSIDIAN);
            block = world.getBlockAt(new Location(world, initial.getX(), initial.getY()-1, initial.getZ()+1)); block.setType(Material.OBSIDIAN);
            block = world.getBlockAt(new Location(world, initial.getX()+1, initial.getY()-1, initial.getZ()+1)); block.setType(Material.OBSIDIAN);
            block = world.getBlockAt(new Location(world, initial.getX()+1, initial.getY()-1, initial.getZ()-1)); block.setType(Material.OBSIDIAN);
            block = world.getBlockAt(new Location(world, initial.getX()-1, initial.getY()-1, initial.getZ()+1)); block.setType(Material.OBSIDIAN);
            block = world.getBlockAt(new Location(world, initial.getX()-1, initial.getY()-1, initial.getZ()-1)); block.setType(Material.OBSIDIAN);

            block = world.getBlockAt(new Location(world, initial.getX()+1, initial.getY(), initial.getZ())); block.setType(Material.OBSIDIAN);
            block = world.getBlockAt(new Location(world, initial.getX()+1, initial.getY(), initial.getZ()-1)); block.setType(Material.OBSIDIAN);
            block = world.getBlockAt(new Location(world, initial.getX()+1, initial.getY(), initial.getZ()+1)); block.setType(Material.OBSIDIAN);
            block = world.getBlockAt(new Location(world, initial.getX(), initial.getY(), initial.getZ()+1)); block.setType(Material.OBSIDIAN);
            block = world.getBlockAt(new Location(world, initial.getX(), initial.getY(), initial.getZ()-1)); block.setType(Material.OBSIDIAN);
            block = world.getBlockAt(new Location(world, initial.getX()-1, initial.getY(), initial.getZ()+1)); block.setType(Material.OBSIDIAN);
            block = world.getBlockAt(new Location(world, initial.getX()-1, initial.getY(), initial.getZ()-1)); block.setType(Material.OBSIDIAN);
            block = world.getBlockAt(new Location(world, initial.getX()-1, initial.getY(), initial.getZ())); block.setType(Material.OBSIDIAN);

            block = world.getBlockAt(new Location(world, initial.getX()+1, initial.getY()+1, initial.getZ())); block.setType(Material.STAINED_GLASS_PANE);
            block = world.getBlockAt(new Location(world, initial.getX()+1, initial.getY()+1, initial.getZ()-1)); block.setType(Material.OBSIDIAN);
            block = world.getBlockAt(new Location(world, initial.getX()+1, initial.getY()+1, initial.getZ()+1)); block.setType(Material.OBSIDIAN);
            block = world.getBlockAt(new Location(world, initial.getX(), initial.getY()+1, initial.getZ()+1)); block.setType(Material.STAINED_GLASS_PANE);
            block = world.getBlockAt(new Location(world, initial.getX(), initial.getY()+1, initial.getZ()-1)); block.setType(Material.STAINED_GLASS_PANE);
            block = world.getBlockAt(new Location(world, initial.getX()-1, initial.getY()+1, initial.getZ()+1)); block.setType(Material.OBSIDIAN);
            block = world.getBlockAt(new Location(world, initial.getX()-1, initial.getY()+1, initial.getZ()-1)); block.setType(Material.OBSIDIAN);
            block = world.getBlockAt(new Location(world, initial.getX()-1, initial.getY()+1, initial.getZ())); block.setType(Material.STAINED_GLASS_PANE);

            block = world.getBlockAt(new Location(world, initial.getX()+1, initial.getY()+2, initial.getZ())); block.setType(Material.OBSIDIAN);
            block = world.getBlockAt(new Location(world, initial.getX()-1, initial.getY()+2, initial.getZ())); block.setType(Material.OBSIDIAN);
            block = world.getBlockAt(new Location(world, initial.getX(), initial.getY()+2, initial.getZ()-1)); block.setType(Material.OBSIDIAN);
            block = world.getBlockAt(new Location(world, initial.getX(), initial.getY()+2, initial.getZ()+1)); block.setType(Material.OBSIDIAN);
            block = world.getBlockAt(new Location(world, initial.getX()+1, initial.getY()+2, initial.getZ()+1)); block.setType(Material.OBSIDIAN);
            block = world.getBlockAt(new Location(world, initial.getX()+1, initial.getY()+2, initial.getZ()-1)); block.setType(Material.OBSIDIAN);
            block = world.getBlockAt(new Location(world, initial.getX()-1, initial.getY()+2, initial.getZ()+1)); block.setType(Material.OBSIDIAN);
            block = world.getBlockAt(new Location(world, initial.getX()-1, initial.getY()+2, initial.getZ()-1)); block.setType(Material.OBSIDIAN);
            block = world.getBlockAt(new Location(world, initial.getX(), initial.getY()+2, initial.getZ())); block.setType(Material.OBSIDIAN);

            block = world.getBlockAt(new Location(world, initial.getX(), initial.getY()+1, initial.getZ())); block.setType(Material.STATIONARY_WATER);

        } else {
            int height = 3;
            int width = 3;
            int depth = 2;

            for (int offsetX = -width / 2; offsetX <= width / 2; offsetX++) {
                for (int offsetY = 0; offsetY <= height; offsetY++) {
                    for (int offsetZ = -depth / 2; offsetZ <= depth / 2; offsetZ++) {
                        Block block = world.getBlockAt(x + offsetX, y + offsetY, z + offsetZ);
                        if (offsetX == -width / 2 || offsetX == width / 2 || offsetZ == -depth / 2 || offsetZ == depth / 2) {
                            block.setType(Material.IRON_FENCE);
                        }
                    }
                }
            }

            for (int i = 0; i < 2; i++){
                Location anvilLocation = player.getLocation().add(0, 20 + i, 0);

                Block anvilBlock = anvilLocation.getBlock();
                anvilBlock.setType(Material.ANVIL);

                FallingBlock fallingBlock = world.spawnFallingBlock(anvilLocation, new MaterialData(Material.ANVIL));
                fallingBlock.setDropItem(false);

                ArmorStand armorStand = (ArmorStand) world.spawnEntity(anvilLocation, EntityType.ARMOR_STAND);
                armorStand.setVisible(false);
                armorStand.setBasePlate(false);
                armorStand.setGravity(false);
                armorStand.setCustomNameVisible(false);
                armorStand.setMarker(true);
                armorStand.setInvulnerable(true);

                fallingBlock.addPassenger(armorStand);
            }

        }

    }
}
