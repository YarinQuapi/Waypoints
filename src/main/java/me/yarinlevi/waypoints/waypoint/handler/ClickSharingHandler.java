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

public class ClickSharingHandler {
    private HashMap<UUID, Waypoint> clickSharingHandle = new HashMap<>();

    public void addShare(UUID uuid, Waypoint waypoint) {
        clickSharingHandle.put(uuid, waypoint);

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                clickSharingHandle.remove(uuid);
            }
        }, 30000L); // This is in milliseconds
    }

    public boolean acceptShare(Player player, UUID uuid) {
        if (!clickSharingHandle.containsKey(uuid)) {
            String message = MessagesUtils.getMessage("share_handle_fail_not_found", uuid);
            player.sendMessage(message);
            return false;
        }

        String name = clickSharingHandle.get(uuid).getName();

        return acceptShare(player, uuid, name);
    }

    public boolean acceptShare(Player player, UUID uuid, String name) {
        if (!clickSharingHandle.containsKey(uuid)) {
            String message = MessagesUtils.getMessage("share_handle_fail_not_found", uuid);
            player.sendMessage(message);
            return false;
        }

        Waypoint wp = clickSharingHandle.get(uuid);

        wp.setName(name);
        wp.setOwner(player.getUniqueId());

        try {
            Waypoints.getInstance().getWaypointHandler().addWaypoint(player, wp);
            return true;

        } catch (PlayerNotLoadedException e) {
            player.sendMessage(e.getMessage()); // yea that can throw at this point
        } catch (WaypointAlreadyExistsException e) {
            player.sendMessage(MessagesUtils.getMessage("share_create_failed_exists", name));
        } catch (WaypointLimitReachedException e) {
            player.sendMessage(e.getMessage());
        }

        return false;
    }
}
