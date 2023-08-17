package com.tizfaver.lucky.utils;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter
public class Team {

    private Location nexusLocation;
    private Location spawnLocation;

    private int health = 20;

    private String teamName;

    private boolean isEliminated = false;

    private ChatColor color;

    private boolean isFull = false;

    private final List<Player> playerList = new ArrayList<>();

    public Team(Location nexusLocation, Location spawnLocation, String teamName, ChatColor color) {
        this.nexusLocation = nexusLocation;
        this.spawnLocation = spawnLocation;
        this.teamName = teamName;
        this.color = color;
    }

    public void addPlayer(Player player) {
        if (this.playerList.contains(player)) return;

        this.playerList.add(player);
    }

    public void removeHealth(int value){
        health -= value;
    }

    public void removePlayer(Player player) {
        this.playerList.remove(player);
    }

}
