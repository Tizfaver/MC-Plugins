package com.tizfaver.lucky.managers;

import com.tizfaver.lucky.Main;
import com.tizfaver.lucky.specialeffects.*;
import com.tizfaver.lucky.specialeffects.blocks.SpawnCraftingTable;
import com.tizfaver.lucky.specialeffects.blocks.SpawnAnvil;
import com.tizfaver.lucky.specialeffects.blocks.SpawnChest;
import com.tizfaver.lucky.specialeffects.blocks.SpawnEnchantingTable;
import com.tizfaver.lucky.utils.SpecialEffect;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;

public class SpecialEffectsManager {

    private final List<SpecialEffect> specialEffectList = new ArrayList<>();

    public SpecialEffectsManager() {
        FileConfiguration conf = Main.getInstance().getTrapsConfig();

        for(int i = 0; i < conf.getInt("hole-trap"); i++)
            this.specialEffectList.add(new BucoEffect());
        for(int i = 0; i < conf.getInt("nausea-effect"); i++)
            this.specialEffectList.add(new NauseaEffect());
        for(int i = 0; i < conf.getInt("fly-effect.probability"); i++)
            this.specialEffectList.add(new FlyEffect());
        for(int i = 0; i < conf.getInt("lighting-trap"); i++)
            this.specialEffectList.add(new LightingEffect());
        for(int i = 0; i < conf.getInt("one-heart-challenge"); i++)
            this.specialEffectList.add(new OneHeartChallenge());
        for(int i = 0; i < conf.getInt("head-tnt-trap"); i++)
            this.specialEffectList.add(new HeadTNT());
        for(int i = 0; i < conf.getInt("spawn-mobs"); i++)
            this.specialEffectList.add(new SpawnMob());
        for(int i = 0; i < conf.getInt("tp-to-near-player"); i++)
            this.specialEffectList.add(new TP());
        for(int i = 0; i < conf.getInt("spawn-crafting-table"); i++)
            this.specialEffectList.add(new SpawnCraftingTable());
        for(int i = 0; i < conf.getInt("spawn-anvil"); i++)
            this.specialEffectList.add(new SpawnAnvil());
        for(int i = 0; i < conf.getInt("spawn-enchanting-table"); i++)
            this.specialEffectList.add(new SpawnEnchantingTable());
        for(int i = 0; i < conf.getInt("spawn-chests"); i++)
            this.specialEffectList.add(new SpawnChest());
        for(int i = 0; i < conf.getInt("obsidian-anvil-traps"); i++)
            this.specialEffectList.add(new Traps());
        for(int i = 0; i < conf.getInt("rocket-trap"); i++)
            this.specialEffectList.add(new RocketPlayer());

        if(conf.getBoolean("custom-command-executor.enabled")){
            this.specialEffectList.add(new CustomCommand((List<String>) conf.getList("custom-command-executor.commands")));
        }
    }

    public SpecialEffect getRandomSpecialEffect() {
        return this.specialEffectList.get(Main.getRandom().nextInt(this.specialEffectList.size()));
    }

}
