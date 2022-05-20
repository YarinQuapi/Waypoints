package me.yarinlevi.waypoints.commands.administration.sub;

import me.yarinlevi.waypoints.commands.SubCommand;
import me.yarinlevi.waypoints.utils.LocationData;
import me.yarinlevi.waypoints.utils.LocationUtils;
import me.yarinlevi.waypoints.utils.Utils;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

/**
 * @author YarinQuapi
 **/
public class ChunkScanSubCommand extends SubCommand {
    @Override
    public void run(Player player, String[] args) {
        LocationData locDetail = LocationUtils.handleLocation(player.getLocation());
        String msg = Utils.newMessage("&7Advanced chunk scan:\n" +
                String.format("&a  • &7Coordinates &bX &a%s &bY &a%s &bZ &a%s\n", locDetail.x(), locDetail.y(), locDetail.z()) +
                String.format("&a  • &7Is Slime Chunk?: &b%s\n", locDetail.isSlimeChunk()) +
                String.format("&a  • &7World &b%s", locDetail.world()));
        player.sendMessage(msg);
    }

    @Override
    public @Nullable String getPermission() {
        return null;
    }
}
