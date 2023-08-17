package com.tizfaver.lucky.managers;

import com.tizfaver.lucky.utils.Stats;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class PlayerStatsManager {

    private final Map<Player, Stats> stats = new HashMap<>();

    public PlayerStatsManager(){ }

    public void insertStats(Player player, int k, int f, int n){
        Stats s = stats.get(player);
        s.setKills(s.getKills() + k);
        s.setFinals(s.getFinals() + f);
        s.setNexus(s.getNexus() + n);

        this.stats.put(player, s);
    }

    public void createStats(Player player){
        if (!stats.containsKey(player)) {
            stats.put(player, new Stats());
        }
    }

    public void removeStarts(Player player){
        if (stats.containsKey(player)) {
            stats.remove(player);
        }
    }

    public int getStats(Player player, String request){
        Stats value = stats.get(player);

        switch (request){
            case "kills":
                return value.getKills();
            case "finals":
                return value.getFinals();
            case "nexus":
                return value.getNexus();
            default:
                return -1;
        }
    }
}
