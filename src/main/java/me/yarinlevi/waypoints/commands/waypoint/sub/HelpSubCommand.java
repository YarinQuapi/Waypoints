package me.yarinlevi.waypoints.commands.waypoint.sub;

import me.yarinlevi.waypoints.commands.SubCommand;
import me.yarinlevi.waypoints.utils.MessagesUtils;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * @author YarinQuapi
 **/
public class HelpSubCommand extends SubCommand {
    @Override
    public void run(Player player, String[] args) {
        player.sendMessage(MessagesUtils.getMessageLines("help_command"));
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
