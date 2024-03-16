package me.yarinlevi.waypoints.commands.waypoint.sub;

import me.yarinlevi.waypoints.Waypoints;
import me.yarinlevi.waypoints.commands.shared.SubCommand;
import me.yarinlevi.waypoints.utils.Constants;
import me.yarinlevi.waypoints.utils.MessagesUtils;
import me.yarinlevi.waypoints.waypoint.Waypoint;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.stream.Stream;

/**
 * @author YarinQuapi
 **/
public class TeleportSubCommand extends SubCommand {
    @Override                                       // /wp tp <player>
    public void run(Player player, String[] args) { // /wp tp <"player":waypoint> if public, /wp tp <waypoint> if private.
        if (args.length >= 2) {

            String[] checkArgs;
            OfflinePlayer waypointOwner;
            boolean isPublic;
            boolean playerTP = false;

            if (args[1].contains(":")) {
                checkArgs = args[1].split(":");

                if (checkArgs[0].equalsIgnoreCase("player")) {
                    playerTP = true;
                }

                waypointOwner = Bukkit.getOfflinePlayer(checkArgs[0]); // The player isn't the owner and this is a public waypoint.
                isPublic = true;
            } else {
                checkArgs = new String[] { player.getName(), args[1] };
                waypointOwner = player; // Player executing command is owner.
                isPublic = false;
            }


            if (waypointOwner.hasPlayedBefore()) {
                Stream<Waypoint> waypointStream = isPublic
                        ? Waypoints.getInstance().getWaypointHandler().getPublicWaypoints().stream()
                        : Waypoints.getInstance().getWaypointHandler().getWaypoints(waypointOwner).stream();
                
                waypointStream.filter(x -> x.getName().equalsIgnoreCase(args[1]))
                        .filter(x -> x.getOwner().equals(waypointOwner.getUniqueId()))
                        .findFirst()
                        .ifPresentOrElse(waypoint -> {
                            boolean proceed = true;

                            if (Constants.TELEPROT_ECONOMY_SUPPORT) {
                                Economy economy = Waypoints.getInstance().getEconomyExtension().getEconomy();

                                if (economy.has(player, Constants.WAYPOINT_TELEPORT_COST)) {
                                    economy.withdrawPlayer(player, Constants.WAYPOINT_TELEPORT_COST);
                                } else {
                                    proceed = false;
                                }
                            }

                            if (proceed) {
                                waypoint.teleportToWaypoint(player);
                                player.sendMessage(MessagesUtils.getMessage("teleported_to_waypoint", waypoint.getName()));
                            } else {
                                player.sendMessage(MessagesUtils.getMessage("teleport_failed_no_money"));
                            }
                        }, () -> {
                            player.sendMessage(MessagesUtils.getMessage("action_failed_not_found"));
                        });
            } else {
                player.sendMessage(MessagesUtils.getMessage("never_played", checkArgs[0]));
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