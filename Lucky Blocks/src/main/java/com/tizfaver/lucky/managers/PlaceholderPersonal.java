package com.tizfaver.lucky.managers;

import com.tizfaver.lucky.Main;
import com.tizfaver.lucky.enums.GameState;
import com.tizfaver.lucky.utils.Game;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;

public class PlaceholderPersonal extends PlaceholderExpansion {
    @Override
    public String getIdentifier() {
        //Return your placeholder identifier here
        return "luckyblock";
    }

    @Override
    public String getAuthor() {
        //Return your name or the plugin author's name here
        return "@tizfaver";
    }

    @Override
    public String getVersion() {
        //Return your plugin version here
        return "1.0";
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onPlaceholderRequest(Player player, String identifier) {
        if (player == null) {
            return "";
        }

        Main plugin = Main.getInstance();
        Game game = plugin.getGame();

        if(identifier.equals("kills")){
            return String.valueOf(plugin.getStats().getStats(player, "kills"));
        } else if (identifier.equals("finals")) {
            return String.valueOf(plugin.getStats().getStats(player, "finals"));
        } else if (identifier.equals("destroyed_nexus")){
            return String.valueOf(plugin.getStats().getStats(player, "nexus"));
        } else if (identifier.equals("health_nexus")) {
            if (game.getTeamByPlayer(player) != null){
                return String.valueOf(game.getTeamByPlayer(player).getHealth());
            } else {
                return "N/D";
            }
        } else if (identifier.equals("team_name")) {
            if (game.getTeamByPlayer(player) != null){
                return game.getTeamByPlayer(player).getTeamName();
            } else {
                return "N/D";
            }
        } else if (identifier.equals("get_color")) {
            if (game.getTeamByPlayer(player) != null){
                return String.valueOf(game.getTeamByPlayer(player).getColor());
            } else {
                return "N/D";
            }
        } else if (identifier.equals("winnerTeam")) {
            if(game.gameState.equals(GameState.ENDING)){
                return game.teamWinner.getTeamName();
            } else {
                return "N/D";
            }
        }

        return "err";
    }
}
