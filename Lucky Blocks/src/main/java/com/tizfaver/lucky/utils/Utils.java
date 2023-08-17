package com.tizfaver.lucky.utils;

import com.tizfaver.lucky.Main;
import com.tizfaver.lucky.enums.GameState;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;

public class Utils {

    public static <T extends Enum<T>> String serializeEnum(T element) {
        return element.name();
    }

    public static <T extends Enum<T>> T deserializeEnum(Class<T> klass, String value) {
        return Enum.valueOf(klass, value);
    }

    public Utils(){ /* document why this constructor is empty */ }

    public static void fakeDeath(Player player, Team team, boolean respawn, Player damager) {
        Main plugin = Main.getInstance();
        FileConfiguration conf = plugin.getConfig();

        player.setGameMode(GameMode.SPECTATOR);

        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_PLING, 1f, 1f);

        player.teleport(new Location(player.getWorld(), conf.getInt("lobby-spawn.x"), conf.getInt("lobby-spawn.y"), conf.getInt("lobby-spawn.z")));

        if (!respawn) {
            player.sendMessage(ChatColor.RED + "You have been eliminated!");

            if(team.getPlayerList().isEmpty()){
                Bukkit.broadcastMessage(" \n" + ChatColor.WHITE + "" + ChatColor.BOLD + "TEAM ELIMINATED > " + ChatColor.RESET + "" + team.getColor() + "Team " + team.getTeamName() + "" +
                        ChatColor.RED + " has been eliminated! \n" + " \n");
            }

            if (damager != null){
                plugin.getStats().insertStats(damager, 0, 1, 0);
            }

            for(Player p : plugin.getGame().getPlayerList()){
                p.playSound(p.getLocation(), "ambient.weather.thunder", 0.5f, 1.0f);
                p.playSound(p.getLocation(), "entity.lightning_bolt.thunder", 0.5f, 1.0f);
            }

            sendDeathMessage(player, damager, "final");

            PlayerDeathEvent playerDeathEvent = new PlayerDeathEvent(player, null, 0, "");
            plugin.getServer().getPluginManager().callEvent(playerDeathEvent);

            team.removePlayer(player);
            return;
        } else {
            if (damager != null){
                plugin.getStats().insertStats(damager, 1, 0, 0);
            }
        }

        sendDeathMessage(player, damager, "normal");

        PlayerDeathEvent playerDeathEvent = new PlayerDeathEvent(player, null, 0, "");
        plugin.getServer().getPluginManager().callEvent(playerDeathEvent);

        final int[] i = {1};
        new BukkitRunnable() {
            @Override
            public void run() {
                if (i[0] == 5){
                    cancel();
                }
                if(!plugin.getGame().gameState.equals(GameState.ENDING)){
                    player.sendTitle(ChatColor.RED + "You Died", ChatColor.WHITE + "Respawn in " + ChatColor.GOLD + (6 - i[0]) + ChatColor.YELLOW + " seconds!", 0, 40, 0);
                }

                i[0]++;
            }
        }.runTaskTimer(plugin, 0L, 20L);

        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            if (!player.isOnline()) return;

            player.setGameMode(GameMode.SURVIVAL);
            Utils.setPlayerHealth(player, 20.0);
            for (PotionEffect effect : player.getActivePotionEffects())
                player.removePotionEffect(effect.getType());
            player.teleport(team.getSpawnLocation());
            player.sendMessage(ChatColor.GREEN + "You respawned");
            player.setInvulnerable(true);
            plugin.getServer().getScheduler().runTaskLater(plugin, () -> player.setInvulnerable(false), 3 * 20L);
        }, 20L * 5L);
    }

    private static void sendDeathMessage(Player victim, Player killer, String type){
        String deathMessage = "";
        if(Main.getInstance().getDeathMessage() != null){
            if (killer == null){
                deathMessage = Main.getInstance().getDeathMessage().getRandomAccident(type, victim);
            } else {
                deathMessage = Main.getInstance().getDeathMessage().getRandomNormal(type, victim, killer);
            }
        }

        Bukkit.broadcastMessage(deathMessage);
    }

    public static String formatTime(int seconds) {
        int hours = seconds / 3600;
        int minutes = (seconds % 3600) / 60;
        int remainingSeconds = seconds % 60;

        return String.format("%02d:%02d:%02d", hours, minutes, remainingSeconds);
    }

    public static String replacePlaceholders(Player player, String input) {
        if (input == null || input.isEmpty() || player == null) {
            return input; //nothing to replace, return the input as it is.
        }

        //replace the placeholders using PlaceholderAPI
        return PlaceholderAPI.setPlaceholders(player, input);
    }

    private static final Map<ChatColor, DyeColor> colorMapping = new HashMap<>();

    public static void setPlayerHealth(Player player, double maxHealth){
        player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(maxHealth);
        player.setHealth(maxHealth);
    }

    static {
        colorMapping.put(ChatColor.BLACK, DyeColor.BLACK);
        colorMapping.put(ChatColor.DARK_BLUE, DyeColor.BLUE);
        colorMapping.put(ChatColor.DARK_GREEN, DyeColor.GREEN);
        colorMapping.put(ChatColor.DARK_AQUA, DyeColor.CYAN);
        colorMapping.put(ChatColor.DARK_RED, DyeColor.RED);
        colorMapping.put(ChatColor.DARK_PURPLE, DyeColor.PURPLE);
        colorMapping.put(ChatColor.GOLD, DyeColor.ORANGE);
        colorMapping.put(ChatColor.GRAY, DyeColor.SILVER);
        colorMapping.put(ChatColor.DARK_GRAY, DyeColor.GRAY);
        colorMapping.put(ChatColor.BLUE, DyeColor.LIGHT_BLUE);
        colorMapping.put(ChatColor.GREEN, DyeColor.LIME);
        colorMapping.put(ChatColor.AQUA, DyeColor.LIGHT_BLUE);
        colorMapping.put(ChatColor.RED, DyeColor.RED);
        colorMapping.put(ChatColor.LIGHT_PURPLE, DyeColor.MAGENTA);
        colorMapping.put(ChatColor.YELLOW, DyeColor.YELLOW);
        colorMapping.put(ChatColor.WHITE, DyeColor.WHITE);
    }

    public DyeColor getDyeColor(ChatColor color){
        return colorMapping.getOrDefault(color, DyeColor.WHITE);
    }

}
