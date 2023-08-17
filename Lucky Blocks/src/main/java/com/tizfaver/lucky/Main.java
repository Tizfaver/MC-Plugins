package com.tizfaver.lucky;

import com.tizfaver.lucky.commands.CommandListener;
import com.tizfaver.lucky.events.PlayerListener;
import com.tizfaver.lucky.events.WorldListener;
import com.tizfaver.lucky.managers.*;
import com.tizfaver.lucky.specialeffects.Potions;
import com.tizfaver.lucky.utils.Game;
import com.tizfaver.lucky.utils.GetDeathMessage;
import com.tizfaver.lucky.utils.ItemWithProbability;
import fr.mrmicky.fastboard.FastBoard;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public final class Main extends JavaPlugin {

    private static Main instance;

    private ItemManager itemManager;
    private SpecialEffectsManager specialEffectsManager;

    private Game game;
    private PlayerStatsManager stats;
    private GetDeathMessage msg = null;
    private ScoreBoManager scoreboardManager;

    private File itemsFile;
    private File trapsFile;
    private FileConfiguration itemsConfig;
    private FileConfiguration trapsConfig;

    private static final Random random = new Random();

    public final Map<UUID, FastBoard> boards = new HashMap<>();

    @Override
    public void onLoad() {
        Main.instance = this;
    }

    @Override
    public void onEnable() {
        ConfigurationSerialization.registerClass(ItemWithProbability.class);

        this.saveDefaultConfig();
        this.loadConfigFiles();

        this.registerManagers();
        this.registerEvents();
        this.registerCommands();

        this.game = new Game();
        this.stats = new PlayerStatsManager();
        this.scoreboardManager = new ScoreBoManager();
        this.startConfigs();

        this.getServer().getScheduler().runTaskTimer(this, this.game, 0L, 1L);

        getServer().getConsoleSender().sendMessage("§e------------------------------");
        getServer().getConsoleSender().sendMessage("§e|§6  Cubo Lucky block loaded!  §e|");
        getServer().getConsoleSender().sendMessage("§e|§6 By @Tizfaver and @Pingu-io §e|");
        getServer().getConsoleSender().sendMessage("§e------------------------------");
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll(this);
        this.getServer().getScheduler().cancelAllTasks();
    }

    private void registerEvents() {
        this.getServer().getPluginManager().registerEvents(new PlayerListener(), this);
        this.getServer().getPluginManager().registerEvents(new WorldListener(), this);
        this.getServer().getPluginManager().registerEvents(new Potions(), this);
    }

    private void registerCommands(){
        getCommand("lucky").setExecutor(new CommandListener());
        getCommand("lucky").setTabCompleter(new CommandListener());
    }

    private void registerManagers() {
        this.itemManager = new ItemManager();
        this.specialEffectsManager = new SpecialEffectsManager();
        new PlaceholderPersonal().register();
    }

    private void startConfigs(){
        if(getConfig().getBoolean("death-messages.enabled")){

            this.msg = new GetDeathMessage();
        }

        SQLiteManager sqlite = new SQLiteManager(getConfig().getInt("sqlite.chosen-id"), getConfig().getString("sqlite.table-name"), getConfig().getString("sqlite.db-path"));
        if(getConfig().getBoolean("sqlite.enabled")){
            this.getServer().getScheduler().runTaskTimer(this, sqlite, 0L, 3 * 20L);
        }

        if(getConfig().getBoolean("sidebar")){
            getServer().getScheduler().runTaskTimer(this, () -> {
                for (FastBoard board : this.boards.values()) {
                    ScoreBoManager.updateBoard(board);
                }
            }, 0, 20);
        }

        if (getConfig().getBoolean("other.fixed-day.enabled")){
            getServer().getScheduler().runTaskTimer(this, () -> {
                for (World world : Bukkit.getWorlds()) {
                    world.setTime(getConfig().getInt("other.fixed-day.specific-tick"));
                }
            }, 0, 5);
        }
    }

    private void loadConfigFiles(){
        itemsFile = new File(getDataFolder(), "items.yml");
        trapsFile = new File(getDataFolder(), "traps.yml");

        if(!itemsFile.exists()) {
            itemsFile.getParentFile().mkdirs();
            saveResource("items.yml", false);
        }
        if(!trapsFile.exists()) {
            trapsFile.getParentFile().mkdirs();
            saveResource("traps.yml", false);
        }

        itemsConfig = new YamlConfiguration();
        trapsConfig = new YamlConfiguration();

        try{
            itemsConfig.load(itemsFile);
            trapsConfig.load(trapsFile);
        } catch (IOException | InvalidConfigurationException err){
            err.printStackTrace();
        }
    }

    public ItemManager getItemManager() { return this.itemManager; }
    public SpecialEffectsManager getSpecialEffectsManager() { return this.specialEffectsManager; }
    public ScoreBoManager getScoreboard() { return this.scoreboardManager; }
    public Game getGame() { return this.game; }
    public PlayerStatsManager getStats() { return this.stats; }
    public GetDeathMessage getDeathMessage() { return this.msg; }
    public FileConfiguration getItemsConfig() { return this.itemsConfig; }
    public File getItemsFile() { return this.itemsFile; }
    public File getTrapsFile() { return this.trapsFile; }
    public FileConfiguration getTrapsConfig() { return this.trapsConfig; }

    public static Main getInstance() { return Main.instance; }
    public static Random getRandom() { return Main.random; }

    public void teleport(Player player, String destination){
        Bukkit.getServer().getMessenger().registerOutgoingPluginChannel((Plugin) Main.getInstance(), "BungeeCord");

        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        final DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);

        try {
            dataOutputStream.writeUTF("Connect");
            dataOutputStream.writeUTF(destination);
        } catch (Exception err) {
            err.printStackTrace();
        }

        player.sendPluginMessage((Plugin) Main.getInstance(), "BungeeCord", byteArrayOutputStream.toByteArray());
        try {
            dataOutputStream.close();
            byteArrayOutputStream.close();
        } catch (IOException err) {
            err.printStackTrace();
        }
    }

}
