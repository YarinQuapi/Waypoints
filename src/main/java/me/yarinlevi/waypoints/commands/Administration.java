package me.yarinlevi.waypoints.commands;

import me.yarinlevi.waypoints.gui.GuiUtils;
import me.yarinlevi.waypoints.utils.LocationData;
import me.yarinlevi.waypoints.utils.LocationUtils;
import me.yarinlevi.waypoints.utils.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author YarinQuapi
 */
public class Administration implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        final Player p = (Player) sender;

        if (args.length == 0) {
            p.sendMessage(Utils.newMessage("&eMissing arguments."));
            return false;
        }

        if (args[0].equalsIgnoreCase("chunkscan")) {
            LocationData locDetail = LocationUtils.handleLocation(p.getLocation());
            String msg = Utils.newMessage("&7Advanced chunk scan:\n" +
                    String.format("&a  • &7Coordinates &bX &a%s &bY &a%s &bZ &a%s\n", locDetail.getX(), locDetail.getY(), locDetail.getZ()) +
                    String.format("&a  • &7Is Slime Chunk?: &b%s\n", locDetail.isSlimeChunk()) +
                    String.format("&a  • &7World &b%s", locDetail.getWorld()));
            p.sendMessage(msg);
        } else if (args[0].equalsIgnoreCase("spawn")) {
            LocationData locDetail = LocationUtils.handleLocation(p.getWorld().getSpawnLocation());
            String msg = Utils.newMessage("&7Spawn locator:\n" +
                    String.format("&a  • &7Coordinates &bX& &a%s &bY &a%s &bZ &a%s\n", locDetail.getX(), locDetail.getY(), locDetail.getZ()) +
                    String.format("&a  • &7Distance to coordinates &b%s &7blocks", Utils.calculateDistance(p.getLocation(), p.getWorld().getSpawnLocation())));
            p.sendMessage(msg);
        } else if (args[0].equalsIgnoreCase("gui")) {
            GuiUtils.openInventory(args[1], p);
        } else {
            p.sendMessage(Utils.newMessage("&cInvalid usage!"));
        }

        return true;
    }
}
