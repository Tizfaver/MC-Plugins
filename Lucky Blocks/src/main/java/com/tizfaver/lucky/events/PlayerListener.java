package com.tizfaver.lucky.events;

import com.tizfaver.lucky.Main;
import com.tizfaver.lucky.enums.GameState;
import com.tizfaver.lucky.enums.GameType;
import com.tizfaver.lucky.managers.TabListManager;
import com.tizfaver.lucky.utils.Game;
import com.tizfaver.lucky.utils.Team;
import com.tizfaver.lucky.utils.Utils;
import fr.mrmicky.fastboard.FastBoard;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.material.MaterialData;
import org.bukkit.material.Wool;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class PlayerListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Main plugin = Main.getInstance();
        Game game = plugin.getGame();
        Player player = event.getPlayer();
        FileConfiguration conf = plugin.getConfig();
        event.setJoinMessage(null);
        Location spawn = new Location(event.getPlayer().getWorld(), conf.getInt("lobby-spawn.x"), conf.getInt("lobby-spawn.y"), conf.getInt("lobby-spawn.z"), conf.getInt("lobby-spawn.yaw"), conf.getInt("lobby-spawn.pitch"));

        if(plugin.getConfig().getBoolean("tab-list.enabled")){
            TabListManager tab = new TabListManager();
            new BukkitRunnable() {
                @Override
                public void run() {
                    tab.tabList(player);
                }
            }.runTaskTimer(plugin, 0L, 20L);
        }

        if(!game.gameState.equals(GameState.PLAYING) && game.getPlayerList().size() < game.max){
            game.addPlayer(player);

            Bukkit.broadcastMessage(ChatColor.GRAY + player.getName() + ChatColor.YELLOW + " joined the game (" + ChatColor.AQUA + game.getPlayerList().size() + ChatColor.YELLOW + "/" + ChatColor.AQUA + plugin.getConfig().getInt("max-players") + ChatColor.YELLOW + ")!");

            player.setGameMode(GameMode.SURVIVAL);
            Utils.setPlayerHealth(player, 20.0);
            event.getPlayer().teleport(spawn);

            if(conf.getBoolean("other.clear-inv-onjoin")){
                player.getInventory().clear();
            }

            if(conf.getBoolean("other.give-back-tool.enabled")){
                ItemStack item = new ItemStack(Material.DARK_OAK_DOOR_ITEM);
                ItemMeta itemMeta = item.getItemMeta();

                itemMeta.setDisplayName(ChatColor.GREEN + "Return to the Lobby " + ChatColor.GRAY + "(click me)");

                List<String> lore = new ArrayList<>();
                lore.add(ChatColor.GRAY + "> Return to the Lobby!");
                itemMeta.setLore(lore);

                item.setItemMeta(itemMeta);
                player.getInventory().setItem(conf.getInt("other.give-back-tool.index"), item);
            }

            if(conf.getBoolean("other.give-team-selector.enabled")){
                ItemStack item = new ItemStack(Material.WOOL);
                ItemMeta itemMeta = item.getItemMeta();

                itemMeta.setDisplayName(ChatColor.GREEN + "Choose Team " + ChatColor.GRAY + "(click me)");

                List<String> lore = new ArrayList<>();
                lore.add(ChatColor.GRAY + "> Choose your team.");
                lore.add(ChatColor.GRAY + "  Or you will get a random one.");
                itemMeta.setLore(lore);

                item.setItemMeta(itemMeta);

                player.getInventory().setItem(conf.getInt("other.give-team-selector.index"), item);
            }

            if(plugin.getConfig().getBoolean("sidebar")){
                FastBoard board = new FastBoard(player);

                new BukkitRunnable() {
                    @Override
                    public void run() {
                    board.updateTitle(Utils.replacePlaceholders(player, plugin.getConfig().getString("custom-sidebar-title")));
                    }
                }.runTaskTimer(plugin, 0L, 20L);

                plugin.boards.put(player.getUniqueId(), board);
            }
        } else {
            player.setGameMode(GameMode.SPECTATOR);
            player.teleport(spawn);
            player.sendTitle(ChatColor.RED + "Spectator", "be quiet please!", 5, 80, 20);
            player.setPlayerListName("SPECTATOR");
        }
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        Main plugin = Main.getInstance();
        Game game = plugin.getGame();
        Player player = event.getPlayer();
        event.setQuitMessage(null);

        Utils.setPlayerHealth(player, 20.0);

        FastBoard board = plugin.boards.remove(player.getUniqueId());
        if (board != null) { board.delete(); }

        if(!player.getGameMode().equals(GameMode.SPECTATOR)){
            Bukkit.broadcastMessage(ChatColor.GRAY + player.getName() + ChatColor.YELLOW + " left the game!");
        } else {
            if (game.getTeamByPlayer(player) != null)
                Bukkit.broadcastMessage(ChatColor.GRAY + player.getName() + ChatColor.YELLOW + " left the game!");
        }

        if(plugin.getConfig().getBoolean("tab-list.enabled")){
            new TabListManager().resetTabList(player);
        }

        if(game.getTeamByPlayer(player) == null && (!game.gameState.equals(GameState.LOBBY) && !game.gameState.equals(GameState.STARTING))){ return; }

        if(game.getTeamByPlayer(player) != null && game.getTeamByPlayer(player).getPlayerList().size() <= 1 && game.gameState.equals(GameState.PLAYING)){
            Team team = game.getTeamByPlayer(player);
            Bukkit.broadcastMessage(" \n" + ChatColor.WHITE + "" + ChatColor.BOLD + "TEAM ELIMINATED > " + ChatColor.RESET + "" + team.getColor() + "Team " + team.getTeamName() + "" +
                    ChatColor.RED + " has been eliminated! \n" + " \n");

            game.getTeamByPlayer(player).setHealth(0);
        }

        game.removePlayer(player);

        if(game.gameState.equals(GameState.PLAYING) || game.gameState.equals(GameState.ENDING)){
            Team temp = null;
            int i = 0;
            for (Team t : game.getTeamList()){
                if(!t.getPlayerList().isEmpty()) {
                    temp = t;
                    i++;
                }
            }

            if(i <= 1 && temp != null){ game.endGame(temp); }

            if(game.getPlayerList().isEmpty()){
                game.gameState = GameState.ENDING;
                Bukkit.broadcastMessage(ChatColor.RED + "Everyone left :( so restarting server immediately.");
                Bukkit.getServer().shutdown();
            }
        }

        if(Main.getInstance().getConfig().getBoolean("name-player-team-color.enabled")) {
            new TabListManager().resetTeamColorPlayer(player);
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        Player player = e.getEntity();
        Utils.setPlayerHealth(player, 20.0);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e){
        Main plugin = Main.getInstance();
        Game game = plugin.getGame();
        Player player = e.getPlayer();
        FileConfiguration conf = plugin.getConfig();

        if(player.getLocation().getY() < plugin.getConfig().getInt("min-y")){
            if(game.gameState.equals(GameState.LOBBY) || game.gameState.equals(GameState.STARTING)) {
                if(!player.getGameMode().equals(GameMode.CREATIVE))
                    player.teleport(new Location(player.getWorld(), plugin.getConfig().getInt("lobby-spawn.x"), plugin.getConfig().getInt("lobby-spawn.y"), plugin.getConfig().getInt("lobby-spawn.z")));
            } else if (game.gameState.equals(GameState.PLAYING)){
                player.damage(20);
            }
        }

        if(!plugin.getConfig().getBoolean("map-margin.enabled")) { return; }

        int playerX = (int) player.getLocation().getX();
        int playerY = (int) player.getLocation().getY();
        int playerZ = (int) player.getLocation().getZ();

        //retrieve the coordinates from config.yml
        int startX = (int) conf.getDouble("map-margin.start-block.x");
        int startY = (int) conf.getDouble("map-margin.start-block.y");
        int startZ = (int) conf.getDouble("map-margin.start-block.z");

        int endX = (int) conf.getDouble("map-margin.end-block.x");
        int endZ = (int) conf.getDouble("map-margin.end-block.z");

        //check if the player is outside the defined square
        if((playerX > startX || playerY > startY || playerZ > startZ) || (playerX < endX || playerZ < endZ)){
            Location temp;
            if(playerX > startX){
                temp = new Location(player.getWorld(), playerX - 2.0, playerY, playerZ);
            } else if (playerY > startY){
                temp = new Location(player.getWorld(), playerX, playerY - 1.0, playerZ);
            } else if (playerZ > startZ){
                temp = new Location(player.getWorld(), playerX, playerY, playerZ - 2.0);
            } else if (playerX < endX){
                temp = new Location(player.getWorld(), playerX + 2.0, playerY, playerZ);
            } else { //(playerZ < endZ)
                temp = new Location(player.getWorld(), playerX, playerY, playerZ + 2.0);
            }

            temp.setPitch(player.getLocation().getPitch());
            temp.setYaw(player.getLocation().getYaw());
            player.teleport(temp);

            //player is outside the defined square
            player.sendMessage(ChatColor.GRAY + "" + ChatColor.BOLD + "Hey!" + ChatColor.RESET + "" + ChatColor.RED + " You cannot go further!");
        }
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent e) {
        if (!(e.getEntity() instanceof Player)) return;

        Main plugin = Main.getInstance();
        Game game = plugin.getGame();
        Player player = (Player) e.getEntity();

        if(!game.gameState.equals(GameState.PLAYING)) {
            e.setCancelled(true);
            return;
        }

        try{
            EntityDamageByEntityEvent entityDamageByEntityEvent = (EntityDamageByEntityEvent) e;
            if (entityDamageByEntityEvent.getDamager() instanceof Player) {
                Player damager = (Player) entityDamageByEntityEvent.getDamager();
                if(game.getTeamByPlayer(player) == game.getTeamByPlayer(damager)){ e.setCancelled(true); return; }
            }
        } catch (Exception err){ }

        if (player.getHealth() - e.getDamage() > 0) return;

        try {
            EntityDamageByEntityEvent entityDamageByEntityEvent = (EntityDamageByEntityEvent) e;
            if (entityDamageByEntityEvent.getDamager() instanceof Player) {
                Player damager = (Player) entityDamageByEntityEvent.getDamager();
                ItemStack item = damager.getInventory().getItemInMainHand();
                if ((item.getType().getMaxDurability() - item.getDurability()) < 0) {
                    if (item != null) {
                        item.setAmount(item.getAmount() - 1);
                        damager.getInventory().setItemInMainHand(item);
                    }
                }
            }
        } catch (Exception err){ }

        Team team = game.getTeamByPlayer(player);

        if (team == null) return;

        try{
            EntityDamageByEntityEvent entityDamageByEntityEvent = (EntityDamageByEntityEvent) e;

            if (entityDamageByEntityEvent.getDamager() instanceof Player) {
                Player damager = (Player) entityDamageByEntityEvent.getDamager();
                Utils.fakeDeath(player, team, team.getHealth() > 0, damager);
            } else if (entityDamageByEntityEvent.getDamager() instanceof Arrow){
                Arrow arrow = (Arrow) entityDamageByEntityEvent.getDamager();
                if (arrow.getShooter() instanceof Player) {
                    Player shooter = (Player) arrow.getShooter();
                    Utils.fakeDeath(player, team, team.getHealth() > 0, shooter);
                } else {
                    Utils.fakeDeath(player, team, team.getHealth() > 0, null);
                }
            } else if (entityDamageByEntityEvent.getDamager() instanceof Fireball){
                Fireball fireball = (Fireball) entityDamageByEntityEvent.getDamager();
                if (fireball.getShooter() instanceof Player) {
                    Player shooter = (Player) fireball.getShooter();
                    Utils.fakeDeath(player, team, team.getHealth() > 0, shooter);
                } else {
                    Utils.fakeDeath(player, team, team.getHealth() > 0, null);
                }
            } else if (entityDamageByEntityEvent.getDamager() instanceof Snowball){
                Snowball snowball = (Snowball) entityDamageByEntityEvent.getDamager();
                if (snowball.getShooter() instanceof Player) {
                    Player shooter = (Player) snowball.getShooter();
                    Utils.fakeDeath(player, team, team.getHealth() > 0, shooter);
                } else {
                    Utils.fakeDeath(player, team, team.getHealth() > 0, null);
                }
            } else if (entityDamageByEntityEvent.getDamager() instanceof Egg){
                Egg egg = (Egg) entityDamageByEntityEvent.getDamager();
                if (egg.getShooter() instanceof Player) {
                    Player shooter = (Player) egg.getShooter();
                    Utils.fakeDeath(player, team, team.getHealth() > 0, shooter);
                } else {
                    Utils.fakeDeath(player, team, team.getHealth() > 0, null);
                }
            } else if (entityDamageByEntityEvent.getDamager() instanceof SplashPotion){
                SplashPotion splashPotion = (SplashPotion) entityDamageByEntityEvent.getDamager();
                if (splashPotion.getShooter() instanceof Player) {
                    Player shooter = (Player) splashPotion.getShooter();
                    Utils.fakeDeath(player, team, team.getHealth() > 0, shooter);
                } else {
                    Utils.fakeDeath(player, team, team.getHealth() > 0, null);
                }
            } else if (entityDamageByEntityEvent.getDamager() instanceof FishHook){
                FishHook fishHook = (FishHook) entityDamageByEntityEvent.getDamager();
                if (fishHook.getShooter() instanceof Player) {
                    Player shooter = (Player) fishHook.getShooter();
                    Utils.fakeDeath(player, team, team.getHealth() > 0, shooter);
                } else {
                    Utils.fakeDeath(player, team, team.getHealth() > 0, null);
                }
            } else if (entityDamageByEntityEvent.getDamager() instanceof FallingBlock){

            } else {
                Utils.fakeDeath(player, team, team.getHealth() > 0, null);
            }
        } catch (Exception ee){ //if goes here is because probably the player get killed by the void
            Utils.fakeDeath(player, team, team.getHealth() > 0, null);
        }

        player.playSound(player.getLocation(), "mob.chicken.say", 1.0f, 1.0f);
        player.playSound(player.getLocation(), "entity.chicken.ambient", 1.0f, 1.0f);

        List<Team> a = new ArrayList<>();
        for (Team t : game.getTeamList()) {
            if (t.getPlayerList().size() > 0) a.add(t);
        }
        if (a.size() == 1) game.endGame(a.get(0));

        e.setCancelled(true);
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        Team team = Main.getInstance().getGame().getTeamByPlayer(player);

        if(player.getGameMode().equals(GameMode.SPECTATOR)){
            event.setCancelled(true);
            return;
        }

        if (!Main.getInstance().getGame().gameState.equals(GameState.PLAYING)) {
            if(team == null){
                event.setFormat(ChatColor.GRAY + "" + player.getName() + " " + ChatColor.DARK_GRAY + "\u1405" + " " + ChatColor.WHITE + "%2$s");
            } else {
                if(Main.getInstance().getConfig().getBoolean("other.show-selected-team")){
                    event.setFormat(ChatColor.GRAY + "[" + team.getColor() + team.getTeamName() + ChatColor.GRAY + "] " + player.getName() + " " + ChatColor.DARK_GRAY + "\u1405" + " " + ChatColor.WHITE + "%2$s");
                } else {
                    event.setFormat(ChatColor.GRAY + "" + player.getName() + " " + ChatColor.DARK_GRAY + "\u1405" + " " + ChatColor.WHITE + "%2$s");
                }
            }
        } else {
            event.setFormat(ChatColor.GRAY + "[" + team.getColor() + team.getTeamName() + ChatColor.GRAY + "] " + player.getName() + " " + ChatColor.DARK_GRAY + "\u1405" + " " + ChatColor.WHITE + "%2$s");
        }
    }

    @EventHandler
    public void onFoodChange(FoodLevelChangeEvent e) {
        if(!(e.getEntity() instanceof Player)) return;

        if (e.getFoodLevel() < 20) {
            e.setFoodLevel(20);
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Main plugin = Main.getInstance();
        Game game = plugin.getGame();

        //check for left-click action with a wood stick and specific display name
        ItemStack item = player.getInventory().getItemInMainHand();
        if (event.getAction().name().contains("LEFT") && item.getType() == Material.GOLD_HOE) {

            FallingBlock cobweb = player.getWorld().spawnFallingBlock(player.getEyeLocation(), new MaterialData(Material.WEB));
            Vector velocity = player.getLocation().getDirection().multiply(2.0); //velocità e range del blocco
            cobweb.setVelocity(velocity);

            item.setDurability((short) (item.getDurability() + 1)); //riduce la durability e in caso rimuove l'oggetto
            if (item.getDurability() >= item.getType().getMaxDurability()) {
                player.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
            } else {
                player.getInventory().setItemInMainHand(item);
            }
        } else if (item.getType() == Material.WOOL && !game.gameState.equals(GameState.PLAYING) && plugin.getConfig().getBoolean("other.give-team-selector.enabled")){
            Inventory gui = Bukkit.createInventory(null, 9, ChatColor.BLACK + "Choose team");

            int i = 0;
            for (Team t : game.getTeamList()) {
                Wool wool = new Wool(new Utils().getDyeColor(t.getColor()));
                String itemName = t.getColor() + "Team " + t.getTeamName();

                ItemStack woolItem = wool.toItemStack(1);

                ItemMeta itemMeta = woolItem.getItemMeta();
                itemMeta.setDisplayName(itemName);

                List<String> lore = new ArrayList<>();

                if(game.gameType.equals(GameType.SOLO)){
                    lore.add(ChatColor.GRAY + "Players: " + t.getPlayerList().size() + "/" + "1");
                } else if(game.gameType.equals(GameType.DUO)){
                    lore.add(ChatColor.GRAY + "Players: " + t.getPlayerList().size() + "/" + "2");
                } else if(game.gameType.equals(GameType.TRIO)){
                    lore.add(ChatColor.GRAY + "Players: " + t.getPlayerList().size() + "/" + "3");
                }

                lore.add(ChatColor.GRAY + "Click to select this team.");
                itemMeta.setLore(lore);

                woolItem.setItemMeta(itemMeta);

                gui.setItem(i, woolItem);
                i++;
            }

            player.openInventory(gui); //open the GUI for the player

        } else if (item.getType() == Material.DARK_OAK_DOOR_ITEM && !plugin.getGame().gameState.equals(GameState.PLAYING) && plugin.getConfig().getBoolean("other.give-back-tool.enabled")){
            plugin.teleport(player, plugin.getConfig().getString("other.give-back-tool.destination"));
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if(!Main.getInstance().getGame().gameState.equals(GameState.PLAYING)){
            event.setCancelled(true);
        }

        Player player = (Player) event.getWhoClicked();
        Game game = Main.getInstance().getGame();

        if (event.getView().getTitle().equals(ChatColor.BLACK + "Choose team")) {
            //prevent the player from taking the item from the GUI
            event.setCancelled(true);

            //check if the clicked item is not air
            ItemStack clickedItem = event.getCurrentItem();
            if (clickedItem != null && clickedItem.getType() != Material.AIR) {
                //get the display name of the clicked item
                ItemMeta itemMeta = clickedItem.getItemMeta();
                String itemName = itemMeta.getDisplayName();

                for(Team t : game.getTeamList()){
                    if (itemName.equals(t.getColor() + "Team " + t.getTeamName())){
                        if(game.gameType.equals(GameType.SOLO)){
                            if (t.getPlayerList().size() == 1){
                                player.sendMessage(ChatColor.RED + "This team if full!");
                                return;
                            } else {
                                if (game.getTeamByPlayer(player) != null){
                                    player.sendMessage(ChatColor.GREEN + "Removed from the team: " + game.getTeamByPlayer(player).getColor() + "" + game.getTeamByPlayer(player).getTeamName());

                                    if (game.getTeamByPlayer(player).isFull()){
                                        game.getTeamByPlayer(player).setFull(false);
                                    }
                                    game.getTeamByPlayer(player).removePlayer(player);
                                }

                                t.getPlayerList().add(player);
                                t.setFull(true);
                            }
                        } else if(game.gameType.equals(GameType.DUO)){
                            if (t.getPlayerList().size() == 1){
                                if (game.getTeamByPlayer(player) != null){
                                    player.sendMessage(ChatColor.GREEN + "Removed from the team: " + game.getTeamByPlayer(player).getColor() + "" + game.getTeamByPlayer(player).getTeamName());

                                    if (game.getTeamByPlayer(player).isFull()){
                                        game.getTeamByPlayer(player).setFull(false);
                                    }
                                    game.getTeamByPlayer(player).removePlayer(player);
                                }

                                t.getPlayerList().add(player);
                                t.setFull(true);
                            } else if (t.getPlayerList().size() >= 2){
                                player.sendMessage(ChatColor.RED + "This team if full!");
                                return;
                            }
                        } else if(game.gameType.equals(GameType.TRIO)){
                            if (t.getPlayerList().size() == 1 || t.getPlayerList().size() == 2){
                                if (game.getTeamByPlayer(player) != null){
                                    player.sendMessage(ChatColor.GREEN + "Removed from the team: " + game.getTeamByPlayer(player).getColor() + "" + game.getTeamByPlayer(player).getTeamName());

                                    if (game.getTeamByPlayer(player).isFull()){
                                        game.getTeamByPlayer(player).setFull(false);
                                    }
                                    game.getTeamByPlayer(player).removePlayer(player);
                                }

                                t.getPlayerList().add(player);
                                t.setFull(true);
                            } else if (t.getPlayerList().size() >= 3){
                                player.sendMessage(ChatColor.RED + "This team if full!");
                                return;
                            }
                        }

                        if(Main.getInstance().getConfig().getBoolean("other.show-selected-team")){
                            ItemStack leatherCap = new ItemStack(Material.LEATHER_HELMET);
                            LeatherArmorMeta leatherMeta = (LeatherArmorMeta) leatherCap.getItemMeta();

                            //set the color of the leather cap
                            leatherMeta.setColor(new Utils().getDyeColor(t.getColor()).getColor());
                            leatherMeta.setDisplayName(t.getColor() + "Team " + t.getTeamName());

                            leatherCap.setItemMeta(leatherMeta);

                            //equip the leather cap on the player's head
                            player.getInventory().setHelmet(leatherCap);

                            if(Main.getInstance().getConfig().getBoolean("name-player-team-color.enabled")) {
                                new TabListManager().addTeamColorToPlayer(player);
                            }
                        }
                    }
                }

                player.sendMessage(ChatColor.GREEN + "Selected team: " + itemName);
                player.playSound(player.getLocation(), "note.pling", 0.5f, 1.0f);
                player.playSound(player.getLocation(), "block.note_block.pling", 0.5f, 1.0f);

                player.closeInventory();
            }
        }
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        if(!Main.getInstance().getGame().gameState.equals(GameState.PLAYING)){
            event.setCancelled(true);
        } else {
            if (event.getItemDrop() != null) {
                event.getItemDrop().setCustomName(event.getItemDrop().getItemStack().getItemMeta().getDisplayName());
                event.getItemDrop().setCustomNameVisible(true);
            }
        }
    }

    @EventHandler
    public void onEnderPearlLaunch(ProjectileLaunchEvent event) {
        if (!(event.getEntity() instanceof EnderPearl)) { return; }

        EnderPearl enderPearl = (EnderPearl) event.getEntity();

        Vector velocity = enderPearl.getVelocity();
        velocity.multiply(1.75);
        enderPearl.setVelocity(velocity);
    }

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent event) {
        if (event.getPlayer().getGameMode() == GameMode.SPECTATOR) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();

        if(!player.getGameMode().equals(GameMode.SPECTATOR)){ return; }
    }
}















