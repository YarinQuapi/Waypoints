package me.yarinlevi.waypoints.waypoint.handler;

import me.yarinlevi.waypoints.Waypoints;
import me.yarinlevi.waypoints.exceptions.PlayerNotLoadedException;
import me.yarinlevi.waypoints.exceptions.WaypointAlreadyExistsException;
import me.yarinlevi.waypoints.exceptions.WaypointLimitReachedException;
import me.yarinlevi.waypoints.utils.MessagesUtils;
import me.yarinlevi.waypoints.waypoint.Waypoint;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

public class PlayerTrackingHandler {
    private HashMap<UUID, Player> playerTrackingHandle = new HashMap<>();

    public void addTrack(UUID uuid, Player player) {
        playerTrackingHandle.put(uuid, player);

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                playerTrackingHandle.remove(uuid);
            }
        }, 30000L); // This is in milliseconds
    }

    public boolean acceptTrack(Player player, UUID uuid) {
        if (!playerTrackingHandle.containsKey(uuid)) {
            String message = MessagesUtils.getMessage("track_handle_fail_not_found", uuid);
            player.sendMessage(message);
            return false;
        }

        Player wp = playerTrackingHandle.get(uuid);

        Waypoints.getInstance().getTrackerManager().track(player, wp.getLocation(), Waypoints.getInstance().getWaypointHandler().getPlayer(player).getETracker());
        return true;
    }
}
