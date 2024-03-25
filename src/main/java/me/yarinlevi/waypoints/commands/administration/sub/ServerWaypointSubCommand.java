package me.yarinlevi.waypoints.commands.administration.sub;

import me.yarinlevi.waypoints.commands.shared.SubCommand;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ServerWaypointSubCommand extends SubCommand {
    @Override
    public void run(Player player, String[] args) {

    }

    @Override
    public @Nullable String getPermission() {
        return "qwaypoints.command.serverwaypoint";
    }

    @Override
    public @Nullable List<String> getAliases() {
        return List.of("swp", "serverwp", "swaypoint");
    }
}
