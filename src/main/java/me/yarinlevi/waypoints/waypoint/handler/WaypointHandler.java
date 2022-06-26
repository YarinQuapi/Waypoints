package me.yarinlevi.waypoints.waypoint.handler;

import me.yarinlevi.waypoints.Waypoints;
import me.yarinlevi.waypoints.exceptions.PlayerNotLoadedException;
import me.yarinlevi.waypoints.exceptions.WaypointAlreadyExistsException;
import me.yarinlevi.waypoints.exceptions.WaypointDoesNotExistException;
import me.yarinlevi.waypoints.exceptions.WaypointLimitReachedException;
import me.yarinlevi.waypoints.player.PlayerData;
import me.yarinlevi.waypoints.waypoint.Waypoint;
import me.yarinlevi.waypoints.waypoint.WaypointWorld;
import me.yarinlevi.waypoints.waypoint.types.IWaypointHandler;
import org.bukkit.OfflinePlayer;

import java.util.*;

/**
 * @author YarinQuapi
 **/
public class WaypointHandler implements IWaypointHandler {
    private final Map<OfflinePlayer, PlayerData> playerData = new HashMap<>();

    @Override
    public void addPlayer(OfflinePlayer player, PlayerData data) {
        this.playerData.put(player, data);
    }

    @Override
    public void removePlayer(OfflinePlayer player) {
        Waypoints.getInstance().getPlayerSettingsManager().unloadPlayerSettings(player.getUniqueId(), playerData.get(player));
        this.playerData.remove(player);
    }

    @Override
    public PlayerData getPlayer(OfflinePlayer offlinePlayer) {
        return playerData.get(offlinePlayer);
    }

    @Override
    public List<Waypoint> getWaypoints(OfflinePlayer player) {
        return playerData.get(player).getWaypointList();
    }

    @Override
    public List<Waypoint> getWaypoints(OfflinePlayer player, WaypointWorld world) {
        return playerData.get(player).getWaypointList().stream().filter(waypoint -> waypoint.getWorldIdentifier() == world).toList();
    }

    @Override
    public Waypoint getNearestWaypoint(OfflinePlayer player) {
        PlayerData data = playerData.get(player);

        return data.getWaypointList().isEmpty() ? null : playerData.get(player).getWaypointList().stream().sorted(Comparator.comparingInt(Waypoint::getDistance)).findFirst().get();
    }

    @Override
    public List<Waypoint> getPublicWaypoints() {
        return Waypoints.getInstance().getPlayerData().getPublicWaypoints();
    }

    @Override
    public List<Waypoint> getDeathPoints(OfflinePlayer player) {
        PlayerData data = playerData.get(player);

        return data.getWaypointList().isEmpty() ? new ArrayList<>() : data.getWaypointList().stream().filter(waypoint -> waypoint.isDeathpoint()).toList();
    }

    @Override
    public Waypoint getOfflineWaypoint(OfflinePlayer player, String waypointName) {
        return Waypoints.getInstance().getPlayerData().getWaypoint(player.getUniqueId(), waypointName);
    }

    @Override
    public Waypoint getWaypoint(OfflinePlayer player, String waypointName) {
        if (player.isOnline()) {

            PlayerData data = playerData.get(player);

            return data.getWaypointList().isEmpty() ? null : data.getWaypointList().stream()
                    .filter(waypoint -> waypoint.getName().equals(waypointName))
                    .findFirst()
                    .orElse(null);
        } else return this.getOfflineWaypoint(player, waypointName);
    }

    @Override
    public void addWaypoint(OfflinePlayer player, Waypoint waypoint) throws PlayerNotLoadedException, WaypointLimitReachedException, WaypointAlreadyExistsException {
        if (player.isOnline()) {
            PlayerData data = playerData.get(player);

            if (!data.getWaypointList().isEmpty()) {
                if (data.getWaypointList().size() >= data.getWaypointLimit()) {
                    throw new WaypointLimitReachedException();
                }

                else if (data.getWaypointList().stream().anyMatch(waypoint1 -> waypoint1.getName().equals(waypoint.getName()))) {
                    throw new WaypointAlreadyExistsException();
                }
            }

            data.getWaypointList().add(waypoint);
            Waypoints.getInstance().getPlayerData().addWaypoint(player.getUniqueId(), waypoint);

        } else throw new PlayerNotLoadedException();
    }

    @Override
    public void removeWaypoint(OfflinePlayer player, String waypointName) throws PlayerNotLoadedException, WaypointDoesNotExistException {
        if (player.isOnline()) {
            PlayerData data = playerData.get(player);

            if (data.getWaypointList().isEmpty()) {
                throw new WaypointDoesNotExistException();
            }

            Waypoint foundWaypoint = data.getWaypointList().stream()
                    .filter(waypoint -> waypoint.getName().equals(waypointName)).findFirst()
                    .orElseThrow(WaypointDoesNotExistException::new);

            data.getWaypointList().remove(foundWaypoint);
            Waypoints.getInstance().getPlayerData().removeWaypoint(player.getUniqueId(), waypointName);

        } else throw new PlayerNotLoadedException();
    }

    @Override
    public void removeWaypoint(OfflinePlayer player, Waypoint waypoint) throws PlayerNotLoadedException {
        if (player.isOnline()) {
            PlayerData data = playerData.get(player);

            data.getWaypointList().remove(waypoint);
            Waypoints.getInstance().getPlayerData().removeWaypoint(player.getUniqueId(), waypoint.getName());
        } else throw new PlayerNotLoadedException();
    }
}
