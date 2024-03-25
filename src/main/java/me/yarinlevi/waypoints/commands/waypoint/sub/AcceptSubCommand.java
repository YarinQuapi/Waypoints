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

    @Override
    public void run(Player player, String[] args) {
        if (!click_sharing) {
            player.sendMessage(MessagesUtils.getMessage("click_sharing_disabled"));
            return;
        }

        if (args.length == 0) {
            player.sendMessage(MessagesUtils.getMessage("not_enough_args"));
        } else if (args.length == 1) {
            UUID uuid = UUID.fromString(args[0]);

            Waypoints.getInstance().getClickSharingHandler().acceptShare(player, uuid);
        } else {
            UUID uuid = UUID.fromString(args[0]);

            Waypoints.getInstance().getClickSharingHandler().acceptShare(player, uuid, args[1]);
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
