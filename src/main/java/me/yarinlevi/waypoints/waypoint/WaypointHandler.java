package me.yarinlevi.waypoints.waypoint;

import me.yarinlevi.waypoints.Waypoints;
import me.yarinlevi.waypoints.exceptions.PlayerNotLoadedException;
import me.yarinlevi.waypoints.exceptions.WaypointAlreadyExistsException;
import me.yarinlevi.waypoints.exceptions.WaypointDoesNotExistException;
import me.yarinlevi.waypoints.player.PlayerData;
import me.yarinlevi.waypoints.utils.MessagesUtils;
import me.yarinlevi.waypoints.utils.Utils;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * @author YarinQuapi
 */
public class WaypointHandler {
    public List<Waypoint> getWaypoints(OfflinePlayer player) {
        return Waypoints.getInstance().getPlayerDataManager().getPlayerDataMap().get(player.getUniqueId()).getWaypointList();
    }

    public PlayerData getPlayerData(UUID uuid) {
        return Waypoints.getInstance().getPlayerDataManager().getPlayerDataMap().get(uuid);
    }

    public Waypoint getNearestWaypoint(Player player) {
        WaypointWorld waypointWorld = WaypointWorld.valueOf(player.getLocation().getWorld().getEnvironment().name());

        if (Waypoints.getInstance().getPlayerDataManager().getPlayerDataMap().get(player.getUniqueId()).getWaypointList().stream().anyMatch(x -> x.getWorld().equals(waypointWorld))) {
            return Waypoints.getInstance().getPlayerDataManager().getPlayerDataMap().get(player.getUniqueId()).getWaypointList().stream()
                    .filter(x -> x.getWorld().equals(waypointWorld))
                    .sorted(Comparator.comparingInt(Waypoint::getDistance)).findFirst().get();
        }

        return null;
    }

    /**
     * Get all public waypoints
     * @return Public waypoint list
     */
    public List<Waypoint> getAllPublicWaypoints() {
        return Waypoints.getInstance().getPlayerData().getPublicWaypoints();
    }

    public void renameWaypoint(OfflinePlayer player, String waypoint, String newWaypointName) {
        if (Waypoints.getInstance().getPlayerDataManager().getPlayerDataMap().get(player.getUniqueId()).getWaypointList().stream().anyMatch(x -> x.getName().equals(waypoint))) {
            Waypoints.getInstance().getPlayerDataManager().getPlayerDataMap().get(player.getUniqueId()).getWaypointList().stream()
                    .filter(x -> x.getName().equals(waypoint))
                    .findFirst().get().setName(newWaypointName);

            try {
                Waypoints.getInstance().getPlayerData().renameWaypoint(player.getUniqueId(), waypoint, newWaypointName);
            } catch (WaypointDoesNotExistException e) {
                e.printStackTrace();
            }
        }
    }

    public Set<String> getWaypointList(OfflinePlayer player) {
        Set<String> waypoints = new HashSet<>();
        Waypoints.getInstance().getPlayerDataManager().getPlayerDataMap().get(player.getUniqueId()).getWaypointList().forEach(x -> waypoints.add(x.getName()));

        return waypoints;
    }

    public Set<String> getWaypointList(OfflinePlayer player, WaypointWorld world) {
        Set<String> list = new HashSet<>();

        Waypoints.getInstance().getPlayerDataManager().getPlayerDataMap().get(player.getUniqueId()).getWaypointList().stream().filter(x -> x.getWorld().equals(world)).forEach(x -> list.add(x.getName()));

        return list;
    }

    public Set<String> getSystemInducedWaypointList(OfflinePlayer player) {
        Set<String> list = new HashSet<>();

        Waypoints.getInstance().getPlayerDataManager().getPlayerDataMap().get(player.getUniqueId()).getWaypointList().stream().filter(Waypoint::isDeathpoints).forEach(x -> list.add(x.getName()));

        return list;
    }


    @Nullable
    public Waypoint getOfflineWaypoint(OfflinePlayer player, String name) {
        return Waypoints.getInstance().getPlayerData().getWaypoint(player.getUniqueId(), name);
    }

    @Nullable
    public Waypoint getWaypoint(OfflinePlayer player, String name) {
        return Waypoints.getInstance().getPlayerDataManager().getPlayerDataMap().containsKey(player.getUniqueId()) && Waypoints.getInstance().getPlayerDataManager().getPlayerDataMap().get(player.getUniqueId()).getWaypointList().stream().anyMatch(x -> x.getName().equalsIgnoreCase(name)) ?
                Waypoints.getInstance().getPlayerDataManager().getPlayerDataMap().get(player.getUniqueId()).getWaypointList().stream().filter(x -> x.getName().equalsIgnoreCase(name)).findAny().get() : null;
    }

    public boolean addWaypoint(UUID player, Waypoint waypoint) throws WaypointAlreadyExistsException, PlayerNotLoadedException {
        if (Waypoints.getInstance().getPlayerDataManager().getPlayerDataMap().containsKey(player)) {
            PlayerData waypointData = Waypoints.getInstance().getPlayerDataManager().getPlayerDataMap().get(player);
            if (waypointData.getWaypointList().stream().anyMatch(x -> x.getName().equalsIgnoreCase(waypoint.getName()))) {
                throw new WaypointAlreadyExistsException(Utils.newMessage(String.format("&7Waypoint with name &b%s &7already exists.", waypoint.getName())));
            } else {
                waypointData.getWaypointList().add(waypoint);
                Waypoints.getInstance().getPlayerData().addWaypoint(player, waypoint);
                return true;
            }
        } else {
            throw new PlayerNotLoadedException("Hey! your account was not loaded correctly, please reconnect.");
        }
    }

    public boolean removeWaypoint(OfflinePlayer player, String waypointName) throws PlayerNotLoadedException, WaypointDoesNotExistException {
        if (Waypoints.getInstance().getPlayerDataManager().getPlayerDataMap().containsKey(player.getUniqueId())) {
            PlayerData waypointData = Waypoints.getInstance().getPlayerDataManager().getPlayerDataMap().get(player.getUniqueId());

            if (waypointData.getWaypointList().stream().anyMatch(x -> x.getName().equalsIgnoreCase(waypointName))) {
                waypointData.getWaypointList().remove(waypointData.getWaypointList().stream().filter(x -> x.getName().equalsIgnoreCase(waypointName)).findAny().get());

                Waypoints.getInstance().getPlayerData().removeWaypoint(player.getUniqueId(), waypointName);
                return true;
            } else {
                throw new WaypointDoesNotExistException(MessagesUtils.getMessage("action_failed_not_found"));
            }

        } else {
            throw new PlayerNotLoadedException("Hey! your account was not loaded correctly, please reconnect.");
        }
    }

    public boolean removeWaypoint(OfflinePlayer player, Waypoint waypoint) {
        PlayerData waypointData = Waypoints.getInstance().getPlayerDataManager().getPlayerDataMap().get(player.getUniqueId());

        waypointData.getWaypointList().remove(waypoint);

        Waypoints.getInstance().getPlayerData().removeWaypoint(player.getUniqueId(), waypoint.getName());
        return true;
    }
}
