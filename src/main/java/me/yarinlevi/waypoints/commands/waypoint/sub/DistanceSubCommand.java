package me.yarinlevi.waypoints.commands.waypoint.sub;

import me.yarinlevi.waypoints.Waypoints;
import me.yarinlevi.waypoints.commands.SubCommand;
import me.yarinlevi.waypoints.utils.MessagesUtils;
import me.yarinlevi.waypoints.utils.Utils;
import me.yarinlevi.waypoints.waypoint.Waypoint;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

/**
 * @author YarinQuapi
 **/
public class DistanceSubCommand extends SubCommand {
    @Override
    public void run(Player player, String[] args) {
        if (args.length == 3) {
            String waypointA = args[1];
            String waypointB = args[2];

            Waypoint wpA = Waypoints.getInstance().getWaypointHandler().getWaypoint(player, waypointA);

            if (wpA != null) {
                Waypoint wpB = Waypoints.getInstance().getWaypointHandler().getWaypoint(player, waypointB);

                if (wpB != null) {
                    int distance = Utils.calculateDistance(wpA.getVector(), wpB.getVector());

                    player.sendMessage(MessagesUtils.getMessage("waypoint_distance", wpA.getName(), wpB.getName(), distance));
                } else {
                    player.sendMessage(MessagesUtils.getMessage("waypoint_distance_not_found", waypointB));
                }
            } else {
                player.sendMessage(MessagesUtils.getMessage("waypoint_distance_not_found", waypointA));
            }
        } else {
            player.sendMessage(MessagesUtils.getMessage("not_enough_args"));
        }
    }

    @Override
    public @Nullable String getPermission() {
        return null;
    }
}
