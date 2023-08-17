package com.tizfaver.lucky.managers;

import com.tizfaver.lucky.Main;
import com.tizfaver.lucky.utils.Game;
import org.bukkit.Bukkit;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLiteManager implements Runnable {

    private Connection connection;
    private final String url;
    private final int id;
    private final String table_name;
    private Statement statement;
    private ResultSet resultSet;

    public SQLiteManager(int id, String table_name, String url){
        this.id = id;
        this.table_name = table_name;
        this.url = url;
    }

    private void update(){
        Main plugin = Main.getInstance();
        Game game = plugin.getGame();

        if (connection == null) { //check if not connected, then try to connect
            try {
                connection = DriverManager.getConnection(url);
                plugin.getLogger().info("Connected to the database.");
            } catch (SQLException err) {
                plugin.getLogger().warning("Failed to connect to the database.");
                err.printStackTrace();
                return;
            }
        }

        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT COUNT(*) AS count FROM '" + table_name + "' WHERE id_map = '" + id + "'");

            int count = resultSet.getInt("count");

            if (count == 0) {
                //row doesn't exist: perform insert
                statement.executeUpdate("INSERT INTO '" + table_name + "' (id_map, map_name, current_players, max_players, game_status, game_type, bungee_name) " +
                        "VALUES ('" + id + "', '" + plugin.getConfig().getString("map-name") + "', '" + Bukkit.getOnlinePlayers().size() + "', " +
                        "'" + plugin.getConfig().getInt("max-players") + "', '" + game.gameState + "', " +
                        "'" + game.gameType + "', '" + plugin.getConfig().getString("this-bungeecord-name") + "')");
                plugin.getLogger().info("Row inserted.");
            } else {
                //row exists: perform update
                statement.executeUpdate("UPDATE '" + table_name + "' SET map_name = '" + plugin.getConfig().getString("map-name") + "', " +
                        "current_players = '" + Bukkit.getOnlinePlayers().size() + "', " +
                        "max_players = '" + plugin.getConfig().getInt("max-players") + "', " +
                        "game_status = '" + game.gameState + "', " +
                        "game_type = '" + game.gameType + "', " +
                        "bungee_name = '" + plugin.getConfig().getString("this-bungeecord-name") + "' " +
                        "WHERE id_map = '" + id + "'");
            }

            resultSet.close();
            statement.close();
        } catch (SQLException err) {
            plugin.getLogger().warning("Failed to execute the query.");
            err.printStackTrace();
        }
    }

    @Override
    public void run() {
        update();
    }
}
