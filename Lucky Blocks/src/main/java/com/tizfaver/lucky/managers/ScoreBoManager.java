package com.tizfaver.lucky.managers;

import com.tizfaver.lucky.Main;
import com.tizfaver.lucky.enums.GameState;
import com.tizfaver.lucky.utils.Game;
import com.tizfaver.lucky.utils.Team;
import com.tizfaver.lucky.utils.Utils;
import fr.mrmicky.fastboard.FastBoard;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

public class ScoreBoManager {

    private static Utils utils = new Utils();

    public ScoreBoManager(){ }

    public static void updateBoard(FastBoard board) {
        Main plugin = Main.getInstance();
        Game game = plugin.getGame();

        if(game.gameState.equals(GameState.LOBBY) || game.gameState.equals(GameState.STARTING)){
            String tempTeam, status;
            if(plugin.getGame().getTeamByPlayer(board.getPlayer()) != null){
                tempTeam = game.getTeamByPlayer(board.getPlayer()).getColor() + "" + game.getTeamByPlayer(board.getPlayer()).getTeamName();
            } else { tempTeam = "N/D"; }
            if(game.gameState.equals(GameState.LOBBY)){ status = "In Lobby"; } else { status = "Starting"; }

            board.updateLines(
                    "",
                    ChatColor.WHITE + " Map: " + ChatColor.GOLD + plugin.getConfig().getString("map-name"),
                    "",
                    ChatColor.WHITE + " Team: " + tempTeam,
                    ChatColor.WHITE + " Players: " + ChatColor.AQUA + Bukkit.getOnlinePlayers().size() + "/" + game.max,
                    "",
                    " " + ChatColor.GREEN + status + ChatColor.WHITE + ".",
                    "",
                    " " + plugin.getConfig().getString("your-custom-server-ip"),
                    ""
            );
        } else if (game.gameState.equals(GameState.PLAYING)) {
            List<String> lines = new ArrayList<>();
            lines.add(" ");
            for (Team t : game.getTeamList()) {
                if (t.getPlayerList().contains(board.getPlayer())) {
                    if(t.getHealth() == 0){
                        lines.add(ChatColor.RED+ "" + t.getHealth() + "" + "\u2764" + "  " + t.getColor() + "\u00BB" + " " + ChatColor.STRIKETHROUGH + t.getTeamName() + " " + ChatColor.RESET + ChatColor.GRAY + "(You)");
                    } else {
                        if(t.getHealth() < 10)
                            lines.add(ChatColor.RED+ "" + t.getHealth() + "\u2764" + "  " + t.getColor() + "\u00BB" + " " + t.getTeamName() + "" + ChatColor.GRAY + " (You)");
                        else
                            lines.add(ChatColor.RED+ "" + t.getHealth() + "\u2764" + " " + t.getColor() + "\u00BB" + " " + t.getTeamName() + " " + ChatColor.GRAY + "(You)");
                    }
                } else {
                    if(t.getHealth() == 0){
                        lines.add(ChatColor.RED+ "" + t.getHealth() + "" + "\u2764" + "  " + t.getColor() + "\u00BB" + " " + ChatColor.STRIKETHROUGH + t.getTeamName());
                    } else {
                        if(t.getHealth() < 10)
                            lines.add(ChatColor.RED+ "" + t.getHealth() + "" + "\u2764" + "  " + t.getColor() + "\u00BB" + " " + t.getTeamName());
                        else
                            lines.add(ChatColor.RED+ "" + t.getHealth() + "" + "\u2764" + " " + t.getColor() + "\u00BB" + " " + t.getTeamName());
                    }
                }
            }
            lines.add(" ");
            lines.add(ChatColor.WHITE + " Kills: " + ChatColor.AQUA + "" + plugin.getStats().getStats(board.getPlayer(), "kills") + "" + ChatColor.WHITE + ".");
            lines.add(ChatColor.WHITE + " Finals: " + ChatColor.AQUA + "" + plugin.getStats().getStats(board.getPlayer(), "finals") + "" + ChatColor.WHITE + ".");
            lines.add(ChatColor.WHITE + " Destr. Nexus: " + ChatColor.AQUA + "" + plugin.getStats().getStats(board.getPlayer(), "nexus") + "" + ChatColor.WHITE + ".");
            lines.add(" ");
            lines.add(ChatColor.WHITE + " Time: " + ChatColor.GREEN + utils.formatTime(game.maxTime));
            lines.add(" ");
            lines.add(" " + Utils.replacePlaceholders(board.getPlayer(), plugin.getConfig().getString("your-custom-server-ip")));
            lines.add(" ");

            board.updateLines(lines.toArray(new String[0]));
        } else if (game.gameState.equals(GameState.ENDING)) {
            List<String> lines = new ArrayList<>();
            lines.add(" ");
            lines.add(ChatColor.WHITE + " " + ChatColor.BOLD + padString("GAME OVER", 20));
            lines.add("");
            lines.add(game.teamWinner.getColor() + "" + padString("Team " + game.teamWinner.getTeamName() + ChatColor.WHITE + ChatColor.BOLD + " WON!", 20));
            lines.add("");
            lines.add(ChatColor.WHITE + "Kills: " + ChatColor.AQUA + "" + plugin.getStats().getStats(board.getPlayer(), "kills") + "" + ChatColor.WHITE + ".");
            lines.add(ChatColor.WHITE + "Finals: " + ChatColor.AQUA + "" + plugin.getStats().getStats(board.getPlayer(), "finals") + "" + ChatColor.WHITE + ".");
            lines.add(ChatColor.WHITE + "Destr. Nexus: " + ChatColor.AQUA + "" + plugin.getStats().getStats(board.getPlayer(), "nexus") + "" + ChatColor.WHITE + ".");
            lines.add("");
            lines.add(Utils.replacePlaceholders(board.getPlayer(), plugin.getConfig().getString("your-custom-server-ip")));

            board.updateLines(lines.toArray(new String[0]));
        }

    }

    public static String padString(String input, int targetLength) {
        int currentLength = input.length();
        if (currentLength >= targetLength) {
            //if the input string is already longer than or equal to the target length, return it as is.
            return input;
        } else {
            int spacesToAdd = targetLength - currentLength;
            int spacesToAddFront = spacesToAdd / 2;
            int spacesToAddBack = spacesToAdd - spacesToAddFront;

            for (int i = 0; i < input.length(); i++){
                if(input.charAt(i) == '§'){
                    spacesToAddFront += 2;
                }
            }

            StringBuilder paddedString = new StringBuilder();

            //adding spaces in front
            for (int i = 0; i < spacesToAddFront; i++) {
                paddedString.append(" ");
            }

            paddedString.append(input);

            //adding spaces at the back
            for (int i = 0; i < spacesToAddBack; i++) {
                paddedString.append(" ");
            }

            return paddedString.toString();
        }
    }

}
