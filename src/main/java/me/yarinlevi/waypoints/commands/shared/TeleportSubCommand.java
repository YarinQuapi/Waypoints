package me.yarinlevi.waypoints.commands.shared;

import me.yarinlevi.waypoints.Waypoints;
import me.yarinlevi.waypoints.utils.MessagesUtils;
import me.yarinlevi.waypoints.waypoint.Waypoint;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * @author YarinQuapi
 **/
public class TeleportSubCommand extends SubCommand {
    @Override
    public void run(Player player, String[] args) {
        if (args.length >= 2) {
            String[] checkArgs = args[1].split(":");

            if (checkArgs.length == 2 && Bukkit.getOfflinePlayer(checkArgs[0]).hasPlayedBefore()) {
                Waypoint wp = Waypoints.getInstance().getWaypointHandler().getOfflineWaypoint(Bukkit.getOfflinePlayer(checkArgs[0]), checkArgs[1]);

                if (wp != null) {

                    wp.teleportToWaypoint(player);
                    player.sendMessage(MessagesUtils.getMessage("teleported_to_waypoint", wp.getName()));

                } else {
                    player.sendMessage(MessagesUtils.getMessage("action_failed_not_found"));
                }
            } else {
                player.sendMessage("§cAn argument was either missing or player was not found.");
            }
        } else {
            player.sendMessage(MessagesUtils.getMessage("action_failed_args"));
        }
    }

    @Override
    public @Nullable String getPermission() {
        return "qwaypoints.command.teleport";
    }

    @Override
    public @Nullable List<String> getAliases() {
        return List.of("waypointteleport", "wpteleport", "wptp", "tp");
    }
}
