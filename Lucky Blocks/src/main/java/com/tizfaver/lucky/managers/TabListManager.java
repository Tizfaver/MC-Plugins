package com.tizfaver.lucky.managers;

import com.tizfaver.lucky.Main;
import com.tizfaver.lucky.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.List;
import java.lang.reflect.Field;

public class TabListManager {

    public void tabList(Player player){
        String headerLines = "", footerLines = "";
        List<String> header = Main.getInstance().getConfig().getStringList("tab-list.header");
        List<String> footer = Main.getInstance().getConfig().getStringList("tab-list.footer");

        for(String s : header){
            s = Utils.replacePlaceholders(player, s);
            headerLines += s;
        }
        for(String s : footer){
            s = Utils.replacePlaceholders(player, s);
            footerLines += s;
        }

        setTabListHeaderFooter(player, headerLines, footerLines);
    }

    public void resetTabList(Player player){
        setTabListHeaderFooter(player, " ", " ");
    }

    private void setTabListHeaderFooter(Player player, String header, String footer) {
        try {
            Class<?> packetClass = getNMSClass("PacketPlayOutPlayerListHeaderFooter");
            Class<?> componentClass = getNMSClass("IChatBaseComponent");

            Object headerComponent = componentClass.getDeclaredClasses()[0].getMethod("a", String.class).invoke(null, "{\"text\":\"" + header + "\"}");
            Object footerComponent = componentClass.getDeclaredClasses()[0].getMethod("a", String.class).invoke(null, "{\"text\":\"" + footer + "\"}");

            Object packet = packetClass.getConstructor().newInstance();
            Field headerField = packetClass.getDeclaredField("a");
            Field footerField = packetClass.getDeclaredField("b");
            headerField.setAccessible(true);
            footerField.setAccessible(true);
            headerField.set(packet, headerComponent);
            footerField.set(packet, footerComponent);

            sendPacket(player, packet);
        } catch (Exception err) {
            err.printStackTrace();
        }
    }

    //method to send a packet to the player
    private void sendPacket(Player player, Object packet) {
        try {
            Object playerHandle = player.getClass().getMethod("getHandle").invoke(player);
            Object playerConnection = playerHandle.getClass().getField("playerConnection").get(playerHandle);
            playerConnection.getClass().getMethod("sendPacket", getNMSClass("Packet")).invoke(playerConnection, packet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //method to get a class from the net.minecraft.server package
    private Class<?> getNMSClass(String className) throws ClassNotFoundException {
        String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
        String fullName = "net.minecraft.server." + version + "." + className;
        return Class.forName(fullName);
    }

    //method to set the colored name tag to the player
    public void addTeamColorToPlayer(Player player){
        ChatColor color = Main.getInstance().getGame().getTeamByPlayer(player).getColor();

        if(Main.getInstance().getConfig().getBoolean("name-player-team-color.enabled")){
            player.setPlayerListName(color + "" + player.getName());
            setNicknameColor(player, color);
            if(Main.getInstance().getConfig().getBoolean("name-player-team-color.also-add-team-first-char")){
                player.setPlayerListName(color + "" + ChatColor.BOLD + "" + Main.getInstance().getGame().getTeamByPlayer(player).getTeamName().charAt(0) + " " + ChatColor.RESET + "" + color + "" + player.getName());
            }
        }
    }

    private void setNicknameColor(Player player, ChatColor color) {
        try {
            String teamName = player.getName();

            //get the scoreboard manager using reflection
            Class<?> craftServerClass = Class.forName("org.bukkit.craftbukkit." + getVersion() + ".CraftServer");
            Object craftServer = craftServerClass.cast(Bukkit.getServer());
            Object scoreboardManager = craftServerClass.getDeclaredMethod("getScoreboardManager").invoke(craftServer);

            //get the scoreboard from the scoreboard manager
            Class<?> scoreboardManagerClass = Class.forName("org.bukkit.scoreboard.ScoreboardManager");
            Object scoreboard = scoreboardManagerClass.getDeclaredMethod("getMainScoreboard").invoke(scoreboardManager);

            //create a new team or retrieve an existing team with the given name
            Class<?> scoreboardClass = Class.forName("org.bukkit.scoreboard.Scoreboard");
            Class<?> teamClass = Class.forName("org.bukkit.scoreboard.Team");
            Object team;
            if (scoreboardClass.getDeclaredMethod("getTeam", String.class).invoke(scoreboard, teamName) == null) {
                team = scoreboardClass.getDeclaredMethod("registerNewTeam", String.class).invoke(scoreboard, teamName);
            } else {
                team = scoreboardClass.getDeclaredMethod("getTeam", String.class).invoke(scoreboard, teamName);
            }

            //set the team's prefix to the desired color
            if(Main.getInstance().getConfig().getBoolean("name-player-team-color.also-add-team-first-char"))
                teamClass.getDeclaredMethod("setPrefix", String.class).invoke(team, color.toString() + ChatColor.BOLD + "" + Main.getInstance().getGame().getTeamByPlayer(player).getTeamName().charAt(0) + " " + ChatColor.RESET + "" + color + "");
            else
                teamClass.getDeclaredMethod("setPrefix", String.class).invoke(team, color + "");

            //add the player to the team
            teamClass.getDeclaredMethod("addEntry", String.class).invoke(team, player.getName());

            //make the name tag visible above the player's head
            Class<?> playerClass = Class.forName("org.bukkit.entity.Player");
            playerClass.getDeclaredMethod("setScoreboard", scoreboardClass).invoke(player, scoreboard);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getVersion() {
        String packageName = Bukkit.getServer().getClass().getPackage().getName();
        return packageName.substring(packageName.lastIndexOf('.') + 1);
    }

    public void resetTeamColorPlayer(Player player){
        try {
            String teamName = player.getName();

            //get the scoreboard manager using reflection
            Class<?> craftServerClass = Class.forName("org.bukkit.craftbukkit." + getVersion() + ".CraftServer");
            Object craftServer = craftServerClass.cast(Bukkit.getServer());
            Object scoreboardManager = craftServerClass.getDeclaredMethod("getScoreboardManager").invoke(craftServer);

            //get the scoreboard from the scoreboard manager
            Class<?> scoreboardManagerClass = Class.forName("org.bukkit.scoreboard.ScoreboardManager");
            Object scoreboard = scoreboardManagerClass.getDeclaredMethod("getMainScoreboard").invoke(scoreboardManager);

            //get the player's team or create a new one
            Class<?> scoreboardClass = Class.forName("org.bukkit.scoreboard.Scoreboard");
            Class<?> teamClass = Class.forName("org.bukkit.scoreboard.Team");
            Object team;
            if (scoreboardClass.getDeclaredMethod("getTeam", String.class).invoke(scoreboard, teamName) == null) {
                team = scoreboardClass.getDeclaredMethod("registerNewTeam", String.class).invoke(scoreboard, teamName);
            } else {
                team = scoreboardClass.getDeclaredMethod("getTeam", String.class).invoke(scoreboard, teamName);
            }

            //reset the team's prefix to an empty string (removes any prefixes)
            teamClass.getDeclaredMethod("setPrefix", String.class).invoke(team, "");

            //set the team's color to white
            teamClass.getDeclaredMethod("setColor", ChatColor.class).invoke(team, ChatColor.WHITE);

            //add the player to the team
            teamClass.getDeclaredMethod("addEntry", String.class).invoke(team, player.getName());

            //make the name tag visible above the player's head
            Class<?> playerClass = Class.forName("org.bukkit.entity.Player");
            playerClass.getDeclaredMethod("setScoreboard", scoreboardClass).invoke(player, scoreboard);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
