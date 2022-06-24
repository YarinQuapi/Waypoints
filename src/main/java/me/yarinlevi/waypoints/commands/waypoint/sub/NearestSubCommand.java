package me.yarinlevi.waypoints.commands.waypoint.sub;

import me.yarinlevi.waypoints.Waypoints;
import me.yarinlevi.waypoints.commands.SubCommand;
import me.yarinlevi.waypoints.utils.MessagesUtils;
import me.yarinlevi.waypoints.waypoint.Waypoint;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * @author YarinQuapi
 **/
public class NearestSubCommand extends SubCommand {
    @Override
    public void run(Player player, String[] args) {
        Waypoint nearest = Waypoints.getInstance().getWaypointHandler().getNearestWaypoint(player);

        if (nearest != null) {
            String msg = MessagesUtils.getMessage("closest_waypoint", nearest.getName(), nearest.getDistance(), nearest.getFormattedCoordinates());

            player.sendMessage(msg);
        } else {
            player.sendMessage(MessagesUtils.getMessage("no_waypoint_world", player.getWorld().getEnvironment().name()));
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
