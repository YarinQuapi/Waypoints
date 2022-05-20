package me.yarinlevi.waypoints.commands.waypoint.sub;

import me.yarinlevi.waypoints.Waypoints;
import me.yarinlevi.waypoints.commands.SubCommand;
import me.yarinlevi.waypoints.utils.MessagesUtils;
import me.yarinlevi.waypoints.waypoint.Waypoint;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

/**
 * @author YarinQuapi
 **/
public class TrackSubCommand extends SubCommand {

    @Override
    public void run(Player player, String[] args) {
        if (args.length >= 2) {
            if (args[1].equalsIgnoreCase("off")) {
                if (Waypoints.getInstance().getTrackerManager().unTrack(player)) {
                    player.sendMessage(MessagesUtils.getMessage("tracking_disabled"));
                } else {
                    player.sendMessage(MessagesUtils.getMessage("tracking_off"));
                }

            } else {
                Waypoint wp;
                if (!args[1].contains(":")) {
                    wp = Waypoints.getInstance().getWaypointHandler().getWaypoint(player, args[1]);
                } else {
                    String playerName = args[1].split(":")[0];
                    String waypointName = args[1].split(":")[1];

                    wp = Waypoints.getInstance().getWaypointHandler().getAllPublicWaypoints().stream()
                            .filter(waypoint -> waypoint.getOwner().equals(Bukkit.getOfflinePlayer(playerName).getUniqueId()))
                            .filter(waypoint -> waypoint.getName().equalsIgnoreCase(waypointName)).findFirst().orElse(null);
                }

                if (wp != null) {
                    if (Waypoints.getInstance().getTrackerManager().track(player, wp, Waypoints.getInstance().getPlayerDataManager().getPlayerDataMap().get(player.getUniqueId()).getETracker())) {

                        player.sendMessage(MessagesUtils.getMessage("tracking", wp.getName()));
                        player.setCompassTarget(wp.getLocation());
                    } else {
                        player.sendMessage(MessagesUtils.getMessage("tracking_failed_tracking"));
                        player.sendMessage(MessagesUtils.getMessage("tracking_failed_tracking_2"));
                    }
                } else {
                    player.sendMessage(MessagesUtils.getMessage("action_failed_not_found"));
                }
            }
        } else {
            player.sendMessage(MessagesUtils.getMessage("action_failed_args"));
        }
    }

    @Override
    public @Nullable String getPermission() {
        return null;
    }
}
