package me.yarinlevi.waypoints.waypoint;

import me.yarinlevi.waypoints.exceptions.PlayerNotLoadedException;
import me.yarinlevi.waypoints.exceptions.WaypointAlreadyExistsException;
import me.yarinlevi.waypoints.exceptions.WaypointDoesNotExistException;
import me.yarinlevi.waypoints.player.PlayerData;
import me.yarinlevi.waypoints.utils.Utils;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * @author YarinQuapi
 */
public class WaypointHandler {
    private final Map<UUID, PlayerData> playerDataMap = new HashMap<>();

    public List<Waypoint> getWaypoints(OfflinePlayer player) {
        return playerDataMap.get(player.getUniqueId()).getWaypointList();
    }

    public PlayerData getPlayerData(UUID uuid) {
        return playerDataMap.get(uuid);
    }

    public Waypoint getNearestWaypoint(Player player) {
        WaypointWorld waypointWorld = WaypointWorld.valueOf(player.getLocation().getWorld().getEnvironment().name());

        if (playerDataMap.get(player.getUniqueId()).getWaypointList().stream().anyMatch(x -> x.getWorld().equals(waypointWorld))) {
            return playerDataMap.get(player.getUniqueId()).getWaypointList().stream()
                    .filter(x -> x.getWorld().equals(waypointWorld))
                    .sorted(Comparator.comparingInt(Waypoint::getDistance)).findFirst().get();
        }

        return null;
    }

    public void renameWaypoint(OfflinePlayer player, String waypoint, String newWaypointName) {
        if (playerDataMap.get(player.getUniqueId()).getWaypointList().stream().anyMatch(x -> x.getName().equals(waypoint))) {
            playerDataMap.get(player.getUniqueId()).getWaypointList().stream()
                    .filter(x -> x.getName().equals(waypoint))
                    .findFirst().get().setName(newWaypointName);
        }
    }

    public Set<String> getWaypointList(OfflinePlayer player) {
        Set<String> waypoints = new HashSet<>();
        playerDataMap.get(player.getUniqueId()).getWaypointList().stream().filter(x -> !x.isSystemInduced()).forEach(x -> waypoints.add(x.getName()));

        return waypoints;
    }

    public Set<String> getWaypointList(OfflinePlayer player, WaypointWorld world) {
        Set<String> list = new HashSet<>();

        playerDataMap.get(player.getUniqueId()).getWaypointList().stream().filter(x -> x.getWorld().equals(world)).filter(x -> !x.isSystemInduced()).forEach(x -> list.add(x.getName()));

        return list;
    }

    public Set<String> getSystemInducedWaypointList(OfflinePlayer player) {
        Set<String> list = new HashSet<>();

        playerDataMap.get(player.getUniqueId()).getWaypointList().stream().filter(Waypoint::isSystemInduced).forEach(x -> list.add(x.getName()));

        return list;
    }


    @Nullable
    public Waypoint getWaypoint(OfflinePlayer player, String name) {
        return playerDataMap.containsKey(player.getUniqueId()) && playerDataMap.get(player.getUniqueId()).getWaypointList().stream().anyMatch(x -> x.getName().equalsIgnoreCase(name)) ?
                playerDataMap.get(player.getUniqueId()).getWaypointList().stream().filter(x -> x.getName().equalsIgnoreCase(name)).findAny().get() : null;
    }

    public boolean addWaypoint(UUID player, Waypoint waypoint) throws WaypointAlreadyExistsException, PlayerNotLoadedException {
        if (this.playerDataMap.containsKey(player)) {
            PlayerData data = playerDataMap.get(player);
            if (data.getWaypointList().stream().anyMatch(x -> x.getName().equalsIgnoreCase(waypoint.getName()))) {
                throw new WaypointAlreadyExistsException(Utils.newMessage(String.format("&7Waypoint with name &b%s &7already exists.", waypoint.getName())));
            } else {
                data.getWaypointList().add(waypoint);
                return true;
            }
        } else {
            throw new PlayerNotLoadedException("Hey! your account was not loaded correctly, please reconnect.");
        }
    }

    public boolean removeWaypoint(OfflinePlayer player, String waypointName) throws PlayerNotLoadedException, WaypointDoesNotExistException {
        if (this.playerDataMap.containsKey(player.getUniqueId())) {
            PlayerData data = playerDataMap.get(player.getUniqueId());
            if (data.getWaypointList().stream().anyMatch(x -> x.getName().equalsIgnoreCase(waypointName))) {
                data.getWaypointList().remove(data.getWaypointList().stream().filter(x -> x.getName().equalsIgnoreCase(waypointName)).findAny().get());
                return true;
            } else {
                throw new WaypointDoesNotExistException(Utils.newMessage(String.format("&cNo waypoint found with name: &f\"&d%s&f\"", waypointName)));
            }
        } else {
            throw new PlayerNotLoadedException("Hey! your account was not loaded correctly, please reconnect.");
        }
    }

    public boolean insertPlayer(UUID uuid, List<Waypoint> waypoints) {
        if (!this.playerDataMap.containsKey(uuid)) {
            this.playerDataMap.put(uuid, new PlayerData(waypoints));
            return true;
        } else {
            return false;
        }
    }

    public boolean removePlayer(UUID uuid) {
        if (this.playerDataMap.containsKey(uuid)) {
            this.playerDataMap.remove(uuid);
            return true;
        } else {
            return false;
        }
    }
}
