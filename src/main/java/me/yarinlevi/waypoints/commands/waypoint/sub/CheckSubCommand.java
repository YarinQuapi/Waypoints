package me.yarinlevi.waypoints.commands.waypoint.sub;

import me.yarinlevi.waypoints.Waypoints;
import me.yarinlevi.waypoints.commands.shared.SubCommand;
import me.yarinlevi.waypoints.utils.LocationData;
import me.yarinlevi.waypoints.utils.MessagesUtils;
import me.yarinlevi.waypoints.waypoint.Waypoint;
import me.yarinlevi.waypoints.waypoint.WaypointWorld;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * @author YarinQuapi
 **/
public class CheckSubCommand extends SubCommand {
    @Override
    public void run(Player player, String[] args) {
        if (args.length >= 2) {
            String name = args[1];

            Waypoint wp = Waypoints.getInstance().getWaypointHandler().getWaypoint(player, name);

            if (wp != null) {
                LocationData locationData = wp.getLocationData();

                String msg = MessagesUtils.getMessage("waypoint_check",
                        name, locationData.x(), locationData.y(), locationData.z(), WaypointWorld.valueOf(locationData.world()).getName(), locationData.distance(player));
                player.sendMessage(msg);
            } else {
                player.sendMessage(MessagesUtils.getMessage("action_failed_not_found"));
            }
        } else {
            player.sendMessage(MessagesUtils.getMessage("action_failed_args"));
        }
    }

    @Override
    public @Nullable String getPermission() {
        return null;
    }

    @Override
    public @Nullable List<String> getAliases() {
        return null;
    }
}
