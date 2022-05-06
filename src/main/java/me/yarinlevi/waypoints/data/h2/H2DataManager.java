package me.yarinlevi.waypoints.data.h2;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import me.yarinlevi.waypoints.Waypoints;
import me.yarinlevi.waypoints.data.IData;
import me.yarinlevi.waypoints.exceptions.WaypointDoesNotExistException;
import me.yarinlevi.waypoints.utils.LocationData;
import me.yarinlevi.waypoints.waypoint.Waypoint;
import me.yarinlevi.waypoints.waypoint.WaypointState;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author YarinQuapi
 **/
public class H2DataManager implements IData {
    private final HikariDataSource dataSource;
    private final Logger logger;
    private Connection connection;

    public H2DataManager() {
        //MYSQL 8.x CONNECTOR - com.mysql.cj.jdbc.MysqlDataSource
        //MYSQL 5.x CONNECTOR - com.mysql.jdbc.jdbc2.optional.MysqlDataSource
        //SQLITE - org.sqlite.JDBC
        //H2 - org.h2.Driver

        logger = Logger.getLogger("QWaypoints H2");

        HikariConfig config = new HikariConfig();

        config.setDriverClassName("org.h2.Driver");
        config.setJdbcUrl("jdbc:h2:file:./" + Waypoints.getInstance().getDataFolder() + "/waypoints");

        dataSource = new HikariDataSource(config);


        String waypointTable = "CREATE TABLE IF NOT EXISTS `waypoints` (" +
                "`player_uuid` VARCHAR(36) NOT NULL," +
                "`waypoint_name` TEXT NOT NULL," +
                "`location` TEXT NOT NULL," +
                "`item` TEXT NOT NULL," +
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
    }

    @Override
    public void closeDatabase() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Waypoint> getPublicWaypoints() {
        try {
            ResultSet rs = this.get("SELECT * FROM `waypoints` WHERE `is_public` = 'true'");

            List<Waypoint> waypoints = new ArrayList<>();

            if (rs != null) {
                while (rs.next()) {
                    String[] locations = rs.getString("location").split(",");
                    LocationData data = new LocationData(locations[0], locations[1], locations[2], locations[3]);
                    Waypoint waypoint = new Waypoint(UUID.fromString(rs.getString("uuid")), rs.getString("waypoint_name"), data.getLocation(), new ItemStack(Material.getMaterial(rs.getString("item"))), WaypointState.valueOf(rs.getString("is_public")), rs.getBoolean("is_deathpoint"));waypoints.add(waypoint);
                    waypoints.add(waypoint);
                }

                return waypoints;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void addWaypoint(UUID uuid, Waypoint waypoint) {
        String loc = waypoint.getLocation().getX() + "," + waypoint.getLocation().getY() + "," + waypoint.getLocation().getZ() + ',' + waypoint.getLocation().getWorld().getName();

        String statement = "INSERT INTO `waypoints` (`player_uuid`, `waypoint_name`, `location`, `item`, `is_deathpoint`, `is_public`) VALUES ('%s', '%s', '%s', '%s', '%s', '%s');"
                .formatted(uuid.toString(), waypoint.getName(), loc, waypoint.getItem().getType().name(), waypoint.isSystemInduced(), false);

        this.insert(statement);
    }

    @Override
    public void removeWaypoint(UUID uuid, String waypoint) {
        String statement = "DELETE FROM `waypoints` WHERE `player_uuid` = \"%s\" AND `waypoint_name` = \"%s\""
                .formatted(uuid.toString(), waypoint);

        this.update(statement);
    }

    @Override
    public void updateWaypointItem(UUID uuid, String waypoint, String item) {
        String statement = "UPDATE `waypoints` SET `item` = \"%s\" WHERE `player_uuid` = \"%s\" AND `waypoint_name` = \"%s\""
                .formatted(item, uuid.toString(), waypoint);

        this.update(statement);
    }

    @Override
    public boolean isWaypoint(UUID uuid, String waypoint) {
        try {
            ResultSet rs = this.get("SELECT * FROM `waypoints` WHERE `player_uuid` = \"" + uuid.toString() + "\" AND `waypoint_name` = \"" + waypoint + "\"");

            return rs != null && rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public void renameWaypoint(UUID uuid, String waypoint, String name) throws WaypointDoesNotExistException {
        try {
            ResultSet rs = this.get("SELECT * FROM `waypoints` WHERE `player_uuid` = '" + uuid.toString() + "' AND `waypoint_name` = '" + waypoint + "'");

            if (rs != null && !rs.next()) {
                throw new WaypointDoesNotExistException("Waypoint does not exist!");
            }

            this.update("UPDATE `waypoints` SET `waypoint_name` = '" + name + "' WHERE `player_uuid` = '" + uuid + "' AND `waypoint_name` = '" + waypoint + "'");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setWaypointState(UUID uuid, String waypoint, WaypointState state) throws WaypointDoesNotExistException {
        try {
            ResultSet rs = this.get("SELECT * FROM `waypoints` WHERE `player_uuid` = '" + uuid.toString() + "' AND `waypoint_name` = '" + waypoint + "'");

            if (rs != null && !rs.next()) {
                throw new WaypointDoesNotExistException("Waypoint does not exist");
            }

            this.update("UPDATE `waypoints` SET `is_public` = '" + state.name() + "' WHERE `player_uuid` = '" + uuid + "' AND `waypoint_name` = '" + waypoint + "'");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void loadPlayer(UUID uuid) {
        Bukkit.broadcastMessage("called");

        try {
            ResultSet rs = this.get("SELECT * FROM `waypoints` WHERE `player_uuid` = '" + uuid.toString() + "'");


            List<Waypoint> waypoints = new ArrayList<>();

            if (rs != null) {
                while (rs.next()) {
                    String[] locations = rs.getString("location").split(",");
                    LocationData data = new LocationData(locations[0], locations[1], locations[2], locations[3]);
                    Waypoint waypoint = new Waypoint(uuid, rs.getString("waypoint_name"), data.getLocation(), new ItemStack(Material.getMaterial(rs.getString("item"))), rs.getString("is_public") == "false" ? WaypointState.PRIVATE : WaypointState.PUBLIC, rs.getBoolean("is_deathpoint"));
                    waypoints.add(waypoint);
                }
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

    @Nullable
    public ResultSet get(String query) {
        Bukkit.broadcastMessage(query);

        try {
            return connection.createStatement().executeQuery(query);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    public int update(String query) {
        //Bukkit.getScheduler().runTaskAsynchronously(Waypoints.getInstance(), () -> {
            try {
                connection.createStatement().executeUpdate(query);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        //});

        return 0;
    }

    public boolean insert(String query) {

        //Bukkit.getScheduler().runTaskAsynchronously(Waypoints.getInstance(), () -> {
            try {
                connection.createStatement().execute(query);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        //});

        return false;
    }

    public boolean insertLarge(List<String> list) {
        try {
            Statement statement = connection.createStatement();

            for (String s : list) {
                statement.addBatch(s);
            }

            statement.executeBatch();
            statement.closeOnCompletion();
            return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }
}
