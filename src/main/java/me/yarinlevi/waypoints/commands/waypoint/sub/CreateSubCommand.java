package me.yarinlevi.waypoints.commands.waypoint.sub;

import me.yarinlevi.waypoints.Waypoints;
import me.yarinlevi.waypoints.commands.SubCommand;
import me.yarinlevi.waypoints.exceptions.PlayerNotLoadedException;
import me.yarinlevi.waypoints.exceptions.WaypointAlreadyExistsException;
import me.yarinlevi.waypoints.exceptions.WaypointLimitReachedException;
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
public class CreateSubCommand extends SubCommand {
    @Override
    public void run(Player player, String[] args) {
        if (args.length >= 2) {
            String name = args[1].trim();

            if (!Utils.disallowedCharacters.matcher(name).matches()) {

                Waypoint wp = new Waypoint(player.getUniqueId(), name, player.getLocation(), StateIdentifier.PRIVATE, false);

                try {
                    Waypoints.getInstance().getWaypointHandler().addWaypoint(player, wp);

                    player.sendMessage(MessagesUtils.getMessage("waypoint_created", name));
                } catch (WaypointAlreadyExistsException | PlayerNotLoadedException |
                         WaypointLimitReachedException exception) {
                    player.sendMessage(exception.getMessage());
                }
            } else {
                player.sendMessage(MessagesUtils.getMessage("illegal_characters"));
            }
        } else {
            player.sendMessage(MessagesUtils.getMessage("create_failed_args"));
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
