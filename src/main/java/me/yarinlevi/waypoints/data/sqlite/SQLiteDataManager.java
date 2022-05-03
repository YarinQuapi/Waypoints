package me.yarinlevi.waypoints.data.sqlite;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import me.yarinlevi.waypoints.Waypoints;
import me.yarinlevi.waypoints.data.IData;
import me.yarinlevi.waypoints.exceptions.PlayerDoesNotExistException;
import me.yarinlevi.waypoints.exceptions.WaypointDoesNotExistException;
import me.yarinlevi.waypoints.player.PlayerData;
import me.yarinlevi.waypoints.utils.LocationData;
import me.yarinlevi.waypoints.waypoint.Waypoint;
import me.yarinlevi.waypoints.waypoint.WaypointState;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginLogger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author YarinQuapi
 **/
public class SQLiteDataManager implements IData {
    private final HikariDataSource dataSource;
    private final Logger logger;
    private Connection connection;
    private List<PreparedStatement> insertQueue = new ArrayList<>();
    private List<PreparedStatement> updateQueue = new ArrayList<>();

    public SQLiteDataManager() {
        //MYSQL 8.x CONNECTOR - com.mysql.cj.jdbc.MysqlDataSource
        //MYSQL 5.x CONNECTOR - com.mysql.jdbc.jdbc2.optional.MysqlDataSource
        //SQLITE - org.sqlite.JDBC

        logger = Logger.getLogger("QWaypoints SQLite");

        HikariConfig config = new HikariConfig();

        config.setDriverClassName("org.sqlite.JDBC");
        config.setJdbcUrl("jdbc:sqlite:" + Waypoints.getInstance().getDataFolder() + "/waypoints.db");

        dataSource = new HikariDataSource(config);


        String waypointTable = "CREATE TABLE `waypoints` (" +
                "`player_uuid` VARCHAR(36) NOT NULL," +
                "`waypoint_name` TEXT NOT NULL," +
                "`location` TEXT NOT NULL," +
                "`item` TEXT NOT NULL DEFAULT \"dirt\"," +
                "`is_deathpoint` BOOLEAN NOT NULL DEFAULT '0'," +
                "`is_public` BOOLEAN NOT NULL DEFAULT '0'" +
                ");";

        try {
            connection = dataSource.getConnection();

            Statement statement = connection.createStatement(); {
                statement.executeUpdate(waypointTable);
                statement.closeOnCompletion();

                logger.log(Level.FINE, "SQLite database established");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            logger.log(Level.SEVERE, "Failed to establish SQLite database");
        }

        Bukkit.getScheduler().runTaskTimerAsynchronously(Waypoints.getInstance(), () -> {
            PreparedStatement insertStatement = insertQueue.get(1);
            PreparedStatement updateStatement = updateQueue.get(1);

            try {
                insertStatement.execute();
                insertStatement.closeOnCompletion();
                insertQueue.remove(1);

                updateStatement.execute();
                updateStatement.closeOnCompletion();
                updateQueue.remove(1);
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }, 0L, 2L);
    }

    @Override
    public List<Waypoint> getPublicWaypoints() {
        try {
            ResultSet rs = connection.prepareStatement("SELECT * FROM `waypoints` WHERE `is_public` = 1").executeQuery();

            List<Waypoint> waypoints = new ArrayList<>();

            while (rs.next()) {
                String[] locations = rs.getString("location").split(",");
                LocationData data = new LocationData(locations[0], locations[1], locations[2], locations[3]);
                Waypoint waypoint = new Waypoint(UUID.fromString(rs.getString("player_uuid")), rs.getString("waypoint_name"), data.getLocation(), new ItemStack(Material.getMaterial(rs.getString("item"))), rs.getBoolean("is_public") ? WaypointState.PUBLIC : WaypointState.PRIVATE, rs.getBoolean("is_deathpoint"));
                waypoints.add(waypoint);
            }

            return waypoints;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void addWaypoint(UUID uuid, Waypoint waypoint) {
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO `waypoints` (`player_uuid`, `waypoint_name`, `location`, `item`, `is_deathpoint`, `is_public`) VALUES (?, ?, ?, ?, ?, ?)"); {
                statement.setString(1, uuid.toString());
                statement.setString(2, waypoint.getName());
                statement.setString(3, waypoint.getLocation().getWorld().getName() + "," + waypoint.getLocation().getX() + "," + waypoint.getLocation().getY() + "," + waypoint.getLocation().getZ());
                statement.setString(4, waypoint.getItem().getType().name());
                statement.setBoolean(5, waypoint.isSystemInduced());
                statement.setBoolean(6, false);
            }

            insertQueue.add(statement);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void removeWaypoint(UUID uuid, String waypoint) {
        try {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM `waypoints` WHERE `player_uuid` = ? AND `waypoint_name` = ?"); {
                statement.setString(1, uuid.toString());
                statement.setString(2, waypoint);
            }

            updateQueue.add(statement);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateWaypointItem(UUID uuid, String waypoint, String item) {
        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE `waypoints` SET `item` = ? WHERE `player_uuid` = ? AND `waypoint_name` = ?"); {
                statement.setString(1, item);
                statement.setString(2, uuid.toString());
                statement.setString(3, waypoint);
            }

            updateQueue.add(statement);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean isWaypoint(UUID uuid, String waypoint) {
        try {
            ResultSet rs = connection.prepareStatement("SELECT * FROM `waypoints` WHERE `player_uuid` = \"" + uuid.toString() + "\" AND `waypoint_name` = \"" + waypoint + "\"").executeQuery();

            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public void renameWaypoint(UUID uuid, String waypoint, String name) throws WaypointDoesNotExistException {
        try {
            ResultSet rs = connection.prepareStatement("SELECT * FROM `waypoints` WHERE `player_uuid` = '" + uuid.toString() + "' AND `waypoint_name` = '" + waypoint + "'").executeQuery();

            if (!rs.next()) {
                throw new WaypointDoesNotExistException("Waypoint does not exist!");
            }

            connection.prepareStatement("UPDATE `waypoints` SET `waypoint_name` = '" + name + "' WHERE `player_uuid` = '" + uuid + "' AND `waypoint_name` = '" + waypoint + "'").executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setWaypointState(UUID uuid, String waypoint, WaypointState state) throws WaypointDoesNotExistException {
        try {
            ResultSet rs = connection.prepareStatement("SELECT * FROM `waypoints` WHERE `player_uuid` = '" + uuid.toString() + "' AND `waypoint_name` = '" + waypoint + "'").executeQuery();

            if (!rs.next()) {
                throw new WaypointDoesNotExistException("Waypoint does not exist");
            }

            connection.prepareStatement("UPDATE `waypoints` SET `is_public` = '" + state.name() + "' WHERE `player_uuid` = '" + uuid + "' AND `waypoint_name` = '" + waypoint + "'").executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void loadPlayer(UUID uuid) {
        try {
            ResultSet rs = connection.prepareStatement("SELECT * FROM `waypoints` WHERE `player_uuid` = '" + uuid.toString() + "'").executeQuery();

            List<Waypoint> waypoints = new ArrayList<>();

            while (rs.next()) {
                String[] locations = rs.getString("location").split(",");
                LocationData data = new LocationData(locations[0], locations[1], locations[2], locations[3]);
                Waypoint waypoint = new Waypoint(uuid, rs.getString("waypoint_name"), data.getLocation(), new ItemStack(Material.getMaterial(rs.getString("item"))), WaypointState.valueOf(rs.getString("is_public")), rs.getBoolean("is_deathpoint"));
                waypoints.add(waypoint);
            }

            Waypoints.getInstance().getPlayerDataManager().insertPlayer(uuid, waypoints);
        } catch (SQLException e) {
            Waypoints.getInstance().getPlayerDataManager().insertPlayer(uuid, new ArrayList<>());
        }
    }

    @Override
    public void unloadPlayer(UUID uuid) {
        Waypoints.getInstance().getPlayerDataManager().removePlayer(uuid);
    }
}
