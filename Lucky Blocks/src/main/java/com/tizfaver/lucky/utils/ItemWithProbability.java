package com.tizfaver.lucky.utils;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

@Getter @Setter
public class ItemWithProbability implements ConfigurationSerializable {

    private ItemStack itemStack;
    private int probability;

    public ItemWithProbability(ItemStack itemStack, int probability) {
        this.itemStack = itemStack;
        this.probability = probability;
    }

    public ItemWithProbability(Map<String, Object> map) {
        this.itemStack = (ItemStack) map.get("itemStack");
        this.probability = (int) map.get("probability");
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("itemStack", this.itemStack);
        map.put("probability", this.probability);
        return map;
    }
}
