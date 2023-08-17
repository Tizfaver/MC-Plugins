package com.tizfaver.lucky.managers;

import com.tizfaver.lucky.Main;
import com.tizfaver.lucky.utils.ItemWithProbability;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ItemManager {

    private List<ItemWithProbability> itemsWithProbList;
    public List<ItemStack> items = new ArrayList<>();

    public ItemManager() {
        this.loadItemsFromConfig();
    }

    public void loadItemsFromConfig() {
        Main plugin = Main.getInstance();

        this.itemsWithProbList = (List<ItemWithProbability>) plugin.getItemsConfig().getList("customItems");

        for (ItemWithProbability itemWithProbability : itemsWithProbList) {
            for (int i = 0; i < itemWithProbability.getProbability(); i++) {
                items.add(itemWithProbability.getItemStack());
            }
        }
    }

    public ItemStack getRandomItem() {
        return this.items.get(Main.getRandom().nextInt(this.items.size()));
    }

}
