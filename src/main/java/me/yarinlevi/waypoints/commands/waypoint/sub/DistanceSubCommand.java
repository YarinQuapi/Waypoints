package me.yarinlevi.waypoints.commands.waypoint.sub;

import me.yarinlevi.waypoints.Waypoints;
import me.yarinlevi.waypoints.commands.shared.SubCommand;
import me.yarinlevi.waypoints.utils.MessagesUtils;
import me.yarinlevi.waypoints.utils.Utils;
import me.yarinlevi.waypoints.waypoint.Waypoint;
import me.yarinlevi.waypoints.waypoint.types.StateIdentifier;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * @author YarinQuapi
 **/
public class DistanceSubCommand extends SubCommand {
    @Override
    public void run(Player player, String[] args) {
        if (args.length >= 2) {
            String waypointA = args[1];

            Waypoint wpA;
            Waypoint wpB;

            if (args.length >= 3) {
                wpB = Waypoints.getInstance().getWaypointHandler().getWaypoint(player, args[2]);
            } else {
                wpB = new Waypoint(player.getUniqueId(), "player-location", player.getLocation(), StateIdentifier.PRIVATE, false);
            }

            wpA = Waypoints.getInstance().getWaypointHandler().getWaypoint(player, waypointA);

            if (wpA != null) {

                if (wpB != null) {
                    int distance = Utils.calculateDistance(wpA.getVector(), wpB.getVector());

                    player.sendMessage(MessagesUtils.getMessage("waypoint_distance", wpA.getName(), wpB.getName(), distance));
                } else {
                    player.sendMessage(MessagesUtils.getMessage("waypoint_distance_not_found", args[2]));
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

    @Override
    public @Nullable List<String> getAliases() {
        return null;
    }
}
