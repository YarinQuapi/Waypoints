package me.yarinlevi.waypoints.commands.waypoint.sub;

import me.yarinlevi.waypoints.Waypoints;
import me.yarinlevi.waypoints.commands.shared.SubCommand;
import me.yarinlevi.waypoints.utils.LocationData;
import me.yarinlevi.waypoints.utils.MessagesUtils;
import me.yarinlevi.waypoints.utils.Utils;
import me.yarinlevi.waypoints.waypoint.Waypoint;
import me.yarinlevi.waypoints.waypoint.WaypointWorld;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * @author YarinQuapi
 **/
public class ShareSubCommand extends SubCommand {
    boolean click_sharing = Waypoints.getInstance().getConfig().getBoolean("waypoint_click_sharing", true);


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

                UUID uuid = UUID.randomUUID();

                Waypoints.getInstance().getClickSharingHandler().addShare(uuid, wp);

                TextComponent message = new TextComponent(msg);
                message.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/wp accept " + uuid));

                if (click_sharing) {
                    if (target != null) {
                        target.spigot().sendMessage(message);
                        player.spigot().sendMessage(message);
                    } else {
                        Bukkit.getOnlinePlayers().forEach(
                                onlinePlayer -> onlinePlayer.spigot().sendMessage(message)
                        );
                    }
                } else {
                    if (target != null) {
                        target.sendMessage(msg);
                        player.sendMessage(msg);
                    } else {
                        Bukkit.broadcastMessage(msg);
                    }
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

    @Override
    public @Nullable List<String> getAliases() {
        return null;
    }
}
