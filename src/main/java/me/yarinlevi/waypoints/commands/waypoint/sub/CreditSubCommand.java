package me.yarinlevi.waypoints.commands.waypoint.sub;

import me.yarinlevi.waypoints.commands.shared.SubCommand;
import me.yarinlevi.waypoints.utils.Constants;
import me.yarinlevi.waypoints.utils.MessagesUtils;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CreditSubCommand extends SubCommand {
    @Override
    public void run(Player player, String[] args) {
        String message = MessagesUtils.getMessageLines("credit", Constants.PLUGIN_NAME, Constants.PLUGIN_VERSION);

        player.sendMessage(message);
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
