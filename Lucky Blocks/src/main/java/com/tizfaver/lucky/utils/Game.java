package com.tizfaver.lucky.utils;

import com.tizfaver.lucky.Main;
import com.tizfaver.lucky.enums.GameState;
import com.tizfaver.lucky.enums.GameType;
import com.tizfaver.lucky.managers.TabListManager;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import java.util.ArrayList;
import java.util.List;

public class Game implements Runnable {

    private final List<Player> playerList = new ArrayList<>();
    private final List<Team> teamList = new ArrayList<>();

    public GameType gameType;
    public GameState gameState;

    private int timeToStart;
    private String returnLobby;

    private int min;
    public int max;
    public int maxTime;
    public Team teamWinner = null;

    private final Main plugin = Main.getInstance();

    public Game() {
        this.gameState = GameState.LOBBY;

        this.timeToStart = plugin.getConfig().getInt("time-to-start");
        this.min = plugin.getConfig().getInt("min-players");
        this.max = plugin.getConfig().getInt("max-players");
        this.returnLobby = plugin.getConfig().getString("return-lobby");
        this.maxTime = plugin.getConfig().getInt("max-time");

        //Get the game type.
        this.gameType = Utils.deserializeEnum(GameType.class, plugin.getConfig().getString("gameType"));

        ConfigurationSection teamsSection = plugin.getConfig().getConfigurationSection("teams");
        for (String key : teamsSection.getKeys(false)) {
            ConfigurationSection teamSection = teamsSection.getConfigurationSection(key);

            World spawnWorld = plugin.getServer().getWorld(teamSection.getString("spawnLocation.world"));
            Location spawnLocation = new Location( //Get the world and coordinates of the Team.
                    spawnWorld,
                    teamSection.getDouble("spawnLocation.x"),
                    teamSection.getDouble("spawnLocation.y"),
                    teamSection.getDouble("spawnLocation.z"),
                    teamSection.getInt("spawnLocation.yaw"),
                    teamSection.getInt("spawnLocation.pitch")

            );

            World nexusWorld = plugin.getServer().getWorld(teamSection.getString("nexusLocation.world"));
            Location nexusLocation = new Location( //Get the world and coordinates of the Team's nexus.
                    nexusWorld,
                    teamSection.getDouble("nexusLocation.x"),
                    teamSection.getDouble("nexusLocation.y"),
                    teamSection.getDouble("nexusLocation.z")
            );

            ChatColor color = ChatColor.valueOf(teamSection.getString("teamColor").toUpperCase());

            //Create the Team and add it to the List of Teams.
            Team team = new Team(nexusLocation, spawnLocation, teamSection.getString("teamName"), color);
            this.teamList.add(team);
        }
    }

    @Override
    public void run() {
        if (!this.shouldStart()) return; //shouldStart will be true if the num of player is more than the minimum player required.

        if(this.gameState != GameState.LOBBY) return;

        new BukkitRunnable() {
            @Override
            public void run() {
                gameState = GameState.STARTING;
                for(Player player : playerList){
                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.YELLOW + "Game will start in " + ChatColor.GREEN + (timeToStart) + " seconds" + ChatColor.WHITE + "."));
                }

                if(!shouldStart()){
                    gameState = GameState.LOBBY;
                    timeToStart = plugin.getConfig().getInt("time-to-start");
                    cancel();
                }

                if(timeToStart == 0){
                    if (gameState == GameState.PLAYING || gameState == GameState.STARTING) initGame();
                    gameState = GameState.PLAYING;
                    cancel();
                    return;
                }

                timeToStart--;
            }
        }.runTaskTimer(plugin, 0L, 20L);
    }

    private void initGame() {
        //Adding new player to a Team.
        //if (gameType.equals(GameType.SOLO)){ //Game type SOLO.
        for (Player player : this.playerList) {
            boolean joined = false;
            for(Team t : this.teamList){
                if (getTeamByPlayer(player) == t){
                    joined = true;
                    if(plugin.getConfig().getBoolean("name-player-team-color.enabled")){
                        new TabListManager().addTeamColorToPlayer(player);
                    }
                }
            }

            if (!joined){
                for(Team t : this.teamList){
                    if (!t.isFull()){
                        t.addPlayer(player);
                        if(plugin.getConfig().getBoolean("name-player-team-color.enabled")){
                            new TabListManager().addTeamColorToPlayer(player);
                        }

                        if(gameType.equals(GameType.SOLO)){
                            t.setFull(true);
                        } else if (gameType.equals(GameType.DUO)){
                            if(t.getPlayerList().size() == 2){
                                t.setFull(true);
                            }
                        } else if (gameType.equals(GameType.TRIO)){
                            if(t.getPlayerList().size() == 3){
                                t.setFull(true);
                            }
                        }
                        break;
                    }
                }
            }
        }
        //} else { //Game type DUO.
            //...
        //}

        for (Team t : teamList){
            if (t.getPlayerList().isEmpty()){
                t.setHealth(0);
            }
        }

        deleteArea();

        Bukkit.broadcastMessage(ChatColor.GREEN + "\u2580\u2580\u2580\u2580\u2580\u2580\u2580\u2580\u2580\u2580\u2580\u2580\u2580\u2580\u2580\u2580\u2580\u2580\u2580\u2580\u2580\u2580\u2580\u2580\u2580\u2580\u2580\u2580\u2580\u2580\u2580\u2580" +
                " \n " + " \n " +
                ChatColor.LIGHT_PURPLE + "Cubo Lucky Block \n \n" +
                "\n" +
                ChatColor.YELLOW + " Win eliminating all the enemies!\n" +
                " To eliminate an enemy destroy his Nexus, so \n" +
                " the " + ChatColor.AQUA + "DIAMOND BLOCK \n" +
                " \n" +
                ChatColor.GREEN + "\u2580\u2580\u2580\u2580\u2580\u2580\u2580\u2580\u2580\u2580\u2580\u2580\u2580\u2580\u2580\u2580\u2580\u2580\u2580\u2580\u2580\u2580\u2580\u2580\u2580\u2580\u2580\u2580\u2580\u2580\u2580\u2580");

        //Teleport player to the spawn location.
        for(Team team : teamList){
            for(Player player : team.getPlayerList()){
                player.teleport(team.getSpawnLocation());
                player.getInventory().clear();
            }
        }

        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            player.sendTitle(ChatColor.GREEN + "GAME STARTED", ChatColor.WHITE + "good luck!", 5, 80, 20);
            player.playSound(player.getLocation(), "mob.enderdragon.wings", 1.0f, 1.0f);
            player.playSound(player.getLocation(), "entity.ender_dragon.flap", 1.0f, 1.0f);
        }
        this.gameState = GameState.PLAYING;

        new BukkitRunnable() {
            @Override
            public void run() {
            maxTime--;
                if(maxTime <= 0){
                maxTime = 0; //per sicurezza
                List<Team> lastTeams = new ArrayList<>();
                for(Team team : teamList){
                    if (team.getHealth() > 0){
                        lastTeams.add(team);
                    } else { /* already eliminated teams */ }
                }

                if(lastTeams.size() == 1){ //If already there is only one team with more than 0 health
                    endGame(lastTeams.get(0));
                    cancel();
                } else {
                    int highestHealth = -1;
                    List<Team> highestTeams = new ArrayList<>();

                    for (Team team : lastTeams) {
                        int teamHealth = team.getHealth();
                        if (teamHealth > highestHealth) {
                            highestHealth = teamHealth;
                            highestTeams.clear();
                            highestTeams.add(team);
                        } else if (teamHealth == highestHealth) {
                            highestTeams.add(team);
                        } else {
                            //you got eliminated because too low point!
                            for(Player player : team.getPlayerList()){
                                //player.sendTitle(ChatColor.RED + "NOT ENOUGH!", ChatColor.WHITE + "points to survive!", 5, 80, 20);
                                player.sendMessage(ChatColor.RED + "NOT ENOUGH health point to survive!");
                            }
                        }
                    }

                    if (highestTeams.size() == 1) {
                        endGame(highestTeams.get(0)); //l'ultimo team con la vita maggiore ha vinto.
                        cancel();
                    } else {
                        int randomIndex = Main.getRandom().nextInt(highestTeams.size()); //random
                        endGame(highestTeams.get(randomIndex));
                        cancel();
                    }
                }
            }
            }
        }.runTaskTimer(plugin, 0L, 20L);

        new BukkitRunnable(){
            @Override
            public void run() {
            if(!playerList.isEmpty()){
                for (Team t : teamList){
                    if(t.getHealth() > 0 && !t.getPlayerList().isEmpty()){
                        Location nexus = t.getNexusLocation();
                        if (!nexus.getBlock().getType().equals(Material.getMaterial(plugin.getConfig().getString("other.nexus-block")))){
                            nexus.getBlock().setType(Material.getMaterial(plugin.getConfig().getString("other.nexus-block")));
                        }
                    }

                    Location spawn = new Location(playerList.get(0).getWorld(), t.getSpawnLocation().getX(), t.getSpawnLocation().getY()-1, t.getSpawnLocation().getZ());
                    if (!spawn.getBlock().getType().equals(Material.getMaterial(plugin.getConfig().getString("other.spawn-block")))){
                        spawn.getBlock().setType(Material.getMaterial(plugin.getConfig().getString("other.spawn-block")));
                    }
                }
            }
            }
        }.runTaskTimer(plugin, 0, 10L);
    }

    public void endGame(Team winnerTeam) {
        teamWinner = winnerTeam;
        gameState = GameState.ENDING;
        Main plugin = Main.getInstance();

        Bukkit.broadcastMessage(ChatColor.GREEN + "Team " + winnerTeam.getColor() + "" + winnerTeam.getTeamName() + ChatColor.GREEN + " won!");
        for (Player player : playerList) {
            player.setGameMode(GameMode.SURVIVAL);
            player.getInventory().clear();

            player.sendTitle(ChatColor.GREEN + "GAME ENDED", ChatColor.GREEN + "Team " + winnerTeam.getColor() + "" + winnerTeam.getTeamName() + ChatColor.GREEN + " won!", 5, 80, 20);
            player.sendMessage(ChatColor.GREEN + "Returning to the lobby");
            player.playSound(player.getLocation(), "fireworks.twinkle", 1.0f, 1.0f);
            player.playSound(player.getLocation(), "entity.firework_rocket.twinkle", 1.0f, 1.0f);

            if(!player.getAllowFlight()){
                player.setAllowFlight(true);
                player.setFlying(true);
            }

            if(plugin.getConfig().getBoolean("other.give-back-tool.enabled")){
                ItemStack item = new ItemStack(Material.DARK_OAK_DOOR_ITEM);
                ItemMeta itemMeta = item.getItemMeta();

                itemMeta.setDisplayName(ChatColor.GREEN + "Return to the Lobby " + ChatColor.GRAY + "(click me)");

                List<String> lore = new ArrayList<>();
                lore.add(ChatColor.GRAY + "Return to the Lobby!");
                itemMeta.setLore(lore);

                item.setItemMeta(itemMeta);
                player.getInventory().setItem(plugin.getConfig().getInt("other.give-back-tool.index"), item);
            }
        }

        new BukkitRunnable(){
            @Override
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    Main.getInstance().teleport(player, Main.getInstance().getConfig().getString("return-lobby"));
                }
                cancel();
            }
        }.runTaskTimer(plugin, plugin.getConfig().getInt("time-after-end") * 20L, 5 * 20L);

        plugin.getServer().getScheduler().runTaskLater(plugin, () -> Bukkit.getServer().shutdown(), plugin.getConfig().getInt("time-after-end") * 22L);
    }

    public void deleteArea(){
        if(!plugin.getConfig().getBoolean("remover.enabled")) { return; }

        int x1 = plugin.getConfig().getInt("remover.start-block.x");
        int y1 = plugin.getConfig().getInt("remover.start-block.y");
        int z1 = plugin.getConfig().getInt("remover.start-block.z");
        int x2 = plugin.getConfig().getInt("remover.end-block.x");
        int y2 = plugin.getConfig().getInt("remover.end-block.y");
        int z2 = plugin.getConfig().getInt("remover.end-block.z");

        int minX = Math.min(x1, x2);
        int minY = Math.min(y1, y2);
        int minZ = Math.min(z1, z2);
        int maxX = Math.max(x1, x2);
        int maxY = Math.max(y1, y2);
        int maxZ = Math.max(z1, z2);

        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    Block block = plugin.getServer().getWorld(plugin.getConfig().getString("lobby-spawn.world")).getBlockAt(x, y, z);
                    block.setType(Material.AIR);
                }
            }
        }
    }

    public void addPlayer(Player player) {
        if (this.playerList.size() == max) return; //Max lobby player.
        if (this.playerList.contains(player)) return; //If the player is already registered, don't register again.

        plugin.getStats().createStats(player);
        this.playerList.add(player);
    }

    public void removePlayer(Player player) {
        if(getTeamByPlayer(player) != null){
            getTeamByPlayer(player).removePlayer(player);
        }

        plugin.getStats().removeStarts(player);
        this.playerList.remove(player);
    }

    public boolean shouldStart() {
        return this.playerList.size() >= min;
    }

    public Team getTeamByNexusLocation(Location location) { //Get the Team by the Nexus location.
        for (Team team : this.teamList) {
            if (!team.getNexusLocation().equals(location.getBlock().getLocation())) continue;
            return team;
        }

        return null;
    }

    public Team getTeamByPlayer(Player player){
        for (Team team : this.teamList) {
            if (!team.getPlayerList().contains(player)) continue;
            return team;
        }

        return null;
    }

    public List<Team> getTeamList(){
        return this.teamList;
    }

    public List<Player> getPlayerList(){
        return this.playerList;
    }

}
