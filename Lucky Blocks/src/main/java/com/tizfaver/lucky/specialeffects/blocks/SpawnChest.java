package com.tizfaver.lucky.specialeffects.blocks;

import com.tizfaver.lucky.Main;
import com.tizfaver.lucky.utils.SpecialEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class SpawnChest implements SpecialEffect {

    @Override
    public void runEffect(Player player, Block block, BlockBreakEvent event) {
        event.setCancelled(true);
        World world = block.getWorld();
        Location temp = block.getLocation();

        Location location = temp.getBlock().getLocation();

        Block chestBlock = world.getBlockAt(location);

        int chance = Main.getRandom().nextInt(6) + 1;

        if (chance <= 2) {
            ItemStack customDirt = createNamedItem(Material.DIRT, "LOL");
            chestBlock.setType(Material.CHEST);
            Chest chest = (Chest) chestBlock.getState();
            Inventory chestInventory = chest.getInventory();

            chestInventory.setItem(0, new ItemStack(customDirt));
            chestInventory.setItem(3, new ItemStack(customDirt));
            chestInventory.setItem(4, new ItemStack(customDirt));
            chestInventory.setItem(5, new ItemStack(customDirt));
            chestInventory.setItem(7, new ItemStack(customDirt));
            chestInventory.setItem(9, new ItemStack(customDirt));
            chestInventory.setItem(12, new ItemStack(customDirt));
            chestInventory.setItem(14, new ItemStack(customDirt));
            chestInventory.setItem(16, new ItemStack(customDirt));
            chestInventory.setItem(18, new ItemStack(customDirt));
            chestInventory.setItem(19, new ItemStack(customDirt));
            chestInventory.setItem(21, new ItemStack(customDirt));
            chestInventory.setItem(22, new ItemStack(customDirt));
            chestInventory.setItem(23, new ItemStack(customDirt));
            chestInventory.setItem(25, new ItemStack(customDirt));
            chestInventory.setItem(26, new ItemStack(customDirt));

        } else if (chance <= 4) {
            chestBlock.setType(Material.TRAPPED_CHEST);
            Chest chest = (Chest) chestBlock.getState();
            Inventory chestInventory = chest.getInventory();

            chestInventory.setItem(13, new ItemStack(Material.GOLDEN_APPLE));
            Block tntBlock = chestBlock.getRelative(0, -1, 0);
            tntBlock.setType(Material.TNT);
        } else {
            chestBlock.setType(Material.CHEST);
            Chest chest = (Chest) chestBlock.getState();
            Inventory chestInventory = chest.getInventory();

            chestInventory.setItem(13, new ItemStack(Material.GOLDEN_APPLE));
            chestInventory.setItem(22, new ItemStack(Material.OBSIDIAN));
        }

        player.playSound(player.getLocation(), "mob.horse.armor", 0.5f, 1.0f);
        player.playSound(player.getLocation(), "entity.horse.armor", 0.5f, 1.0f);
    }

    private ItemStack createNamedItem(Material material, String name) {
        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(name);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
}
