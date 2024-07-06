package me.yarinlevi.waypoints.commands.waypoint.sub;

import me.yarinlevi.waypoints.Waypoints;
import me.yarinlevi.waypoints.commands.shared.SubCommand;
import me.yarinlevi.waypoints.utils.MessagesUtils;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public class AcceptSubCommand extends SubCommand {
    boolean click_sharing = Waypoints.getInstance().getConfig().getBoolean("waypoint_click_sharing", true);
    boolean player_tracking = Waypoints.getInstance().getConfig().getBoolean("waypoint_player_tracking", true);

    @Override
    public void run(Player player, String[] args) {
        if (!click_sharing) {
            player.sendMessage(MessagesUtils.getMessage("click_sharing_disabled"));
            return;
        }

        if (args.length <= 2) {
            player.sendMessage(MessagesUtils.getMessage("not_enough_args"));
        }
        if (args[1].equalsIgnoreCase("share")) {
            if (!click_sharing) {
                player.sendMessage(MessagesUtils.getMessage("click_sharing_disabled"));
                return;
            }

            if (args.length == 3) {
                UUID uuid = UUID.fromString(args[2]);

                Waypoints.getInstance().getClickSharingHandler().acceptShare(player, uuid);
            } else {
                UUID uuid = UUID.fromString(args[2]);

                Waypoints.getInstance().getClickSharingHandler().acceptShare(player, uuid, args[3]);
            }
        } else if (args[1].equalsIgnoreCase("track")) {
            if (!player_tracking) {
                player.sendMessage(MessagesUtils.getMessage("player_tracking_disabled"));
                return;
            }

            UUID uuid = UUID.fromString(args[2]);
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
