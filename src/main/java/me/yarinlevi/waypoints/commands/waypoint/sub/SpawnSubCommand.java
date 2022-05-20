package me.yarinlevi.waypoints.commands.waypoint.sub;

import me.yarinlevi.waypoints.commands.SubCommand;
import me.yarinlevi.waypoints.utils.LocationData;
import me.yarinlevi.waypoints.utils.LocationUtils;
import me.yarinlevi.waypoints.utils.MessagesUtils;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

/**
 * @author YarinQuapi
 **/
public class SpawnSubCommand extends SubCommand {
    @Override
    public void run(Player player, String[] args) {
        LocationData locDetail = LocationUtils.handleLocation(player.getWorld().getSpawnLocation());
        String msg = MessagesUtils.getMessageLines("spawn_locate_command", locDetail.x(), locDetail.y(), locDetail.z(), locDetail.distance(player));
        player.sendMessage(msg);
    }

    @Override
    public @Nullable String getPermission() {
        return "qwaypoints.command.spawn";
    }
}
