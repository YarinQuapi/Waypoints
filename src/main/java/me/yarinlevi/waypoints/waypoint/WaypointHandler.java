package me.yarinlevi.waypoints.waypoint;

import me.yarinlevi.waypoints.exceptions.PlayerNotLoadedException;
import me.yarinlevi.waypoints.exceptions.WaypointAlreadyExistsException;
import me.yarinlevi.waypoints.exceptions.WaypointDoesNotExistException;
import me.yarinlevi.waypoints.utils.Utils;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class WaypointHandler {
    private final HashMap<Player, HashMap<String, Waypoint>> playerWaypoints = new HashMap<>();

    public HashMap<String, Waypoint> getWaypoints(Player player) {
        return playerWaypoints.getOrDefault(player, new HashMap<>());
    }

    public Set<String> getWaypointList(Player player) {
        return playerWaypoints.containsKey(player) ? playerWaypoints.get(player).keySet() : new HashSet<>();
    }

    public Set<String> getWaypointList(Player player, WaypointWorld world) {
        Set<String> list = new HashSet<>();

        if (playerWaypoints.containsKey(player)) {
            HashMap<String, Waypoint> waypoints = playerWaypoints.get(player);

            for (Waypoint wp : waypoints.values()) {
                if (wp.getWorld().equals(world)) {
                    list.add(wp.getName());
                }
            }
        }
        return list;
    }

    public Waypoint getWaypoint(Player player, String name) throws WaypointDoesNotExistException {
        HashMap<String, Waypoint> waypoints = playerWaypoints.get(player);
        if (waypoints.containsKey(name)) {
            return waypoints.get(name);
        } else {
            throw new WaypointDoesNotExistException(Utils.newMessage(String.format("&cNo waypoint found with name: &f\"&d%s&f\"", name)));
        }
    }

    public boolean addWaypoint(Player player, Waypoint waypoint) throws WaypointAlreadyExistsException, PlayerNotLoadedException {
        if (this.playerWaypoints.containsKey(player)) {
            HashMap<String, Waypoint> waypoints = playerWaypoints.get(player);
            if (waypoints.containsKey(waypoint.getName())) {
                throw new WaypointAlreadyExistsException(Utils.newMessage(String.format("&eWaypoint with name: &f\"&d%s&f\" &ealready exists.", waypoint)));
            } else {
                waypoints.put(waypoint.getName(), waypoint);
                return true;
            }
        } else {
            throw new PlayerNotLoadedException("Hey! your account was not loaded correctly, please reconnect.");
        }
    }

    public boolean removeWaypoint(Player player, String waypointName) throws PlayerNotLoadedException, WaypointDoesNotExistException {
        if (this.playerWaypoints.containsKey(player)) {
            HashMap<String, Waypoint> waypoints = playerWaypoints.get(player);
            if (waypoints.containsKey(waypointName)) {
                waypoints.remove(waypointName);
                return true;
            } else {
                throw new WaypointDoesNotExistException(Utils.newMessage(String.format("&cNo waypoint found with name: &f\"&d%s&f\"", waypointName)));
            }
        } else {
            throw new PlayerNotLoadedException("Hey! your account was not loaded correctly, please reconnect.");
        }
    }

    public boolean insertPlayer(Player player, HashMap<String, Waypoint> waypoints) {
        if (!this.playerWaypoints.containsKey(player)) {
            this.playerWaypoints.put(player, waypoints);
            return true;
        } else {
            return false;
        }
    }

    public boolean removePlayer(Player player) {
        if (this.playerWaypoints.containsKey(player)) {
            this.playerWaypoints.remove(player);
            return true;
        } else {
            return false;
        }
    }

    public FileConfiguration dumpToFile(FileConfiguration data) {
        for (Player player : playerWaypoints.keySet()) {
            HashMap<String, Waypoint> waypoints = playerWaypoints.get(player);
            ConfigurationSection playerSection = data.getConfigurationSection(player.getUniqueId().toString());
            ConfigurationSection waypointSection = playerSection.getConfigurationSection("waypoints");

            for (Waypoint waypoint : waypoints.values()) {
                waypointSection.set(waypoint.getName() + ".location", waypoint.getLocation());
                waypointSection.set(waypoint.getName() + ".systemInduced", waypoint.isSystemInduced());
                waypointSection.set(waypoint.getName() + ".item", waypoint.getItem().getType().name());
            }
        }

        return data;
    }
}
