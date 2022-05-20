package me.yarinlevi.waypoints.commands.waypoint.sub;

import me.yarinlevi.waypoints.Waypoints;
import me.yarinlevi.waypoints.commands.SubCommand;
import me.yarinlevi.waypoints.utils.LocationData;
import me.yarinlevi.waypoints.utils.MessagesUtils;
import me.yarinlevi.waypoints.utils.Utils;
import me.yarinlevi.waypoints.waypoint.Waypoint;
import me.yarinlevi.waypoints.waypoint.WaypointWorld;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

/**
 * @author YarinQuapi
 **/
public class ShareSubCommand extends SubCommand {
    @Override
    public void run(Player player, String[] args) {
        if (args.length >= 2) {
            String name = args[1].trim();

            Waypoint wp = Waypoints.getInstance().getWaypointHandler().getWaypoint(player, name);

            Player target = null;

            if (args.length >= 3) {
                target = Bukkit.getPlayer(args[2]);
            }

            if (wp != null) {
                LocationData locationData = wp.getLocationData();

                String msg = MessagesUtils.getMessage("share_waypoint", player.getName(),
                        name, locationData.x(), locationData.y(), locationData.z(), WaypointWorld.valueOf(locationData.world()).getName());

                if (target != null) {
                    target.sendMessage(msg);
                } else {
                    Bukkit.broadcastMessage(msg);
                }
            } else {
                player.sendMessage(Utils.newMessage("&cSorry, couldn't find a waypoint with that name."));
            }
        } else {
            player.sendMessage(Utils.newMessage("&cShare failed! &7Not enough arguments!"));
        }
    }

    @Override
    public @Nullable String getPermission() {
        return null;
    }
}
