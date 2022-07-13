package me.yarinlevi.waypoints.commands.waypoint.sub;

import me.yarinlevi.waypoints.Waypoints;
import me.yarinlevi.waypoints.commands.shared.SubCommand;
import me.yarinlevi.waypoints.exceptions.PlayerNotLoadedException;
import me.yarinlevi.waypoints.exceptions.WaypointDoesNotExistException;
import me.yarinlevi.waypoints.utils.MessagesUtils;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * @author YarinQuapi
 **/
public class DeleteSubCommand extends SubCommand {
    @Override
    public void run(Player player, String[] args) {
        if (args.length >= 2) {
            String name = args[1];

            try {
                Waypoints.getInstance().getWaypointHandler().removeWaypoint(player, name);
                player.sendMessage(MessagesUtils.getMessage("waypoint_deleted", name));
            } catch (PlayerNotLoadedException | WaypointDoesNotExistException exception) {
                player.sendMessage(exception.getMessage());
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
        return List.of("remove");
    }
}
