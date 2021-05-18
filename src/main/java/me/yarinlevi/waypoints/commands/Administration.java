package me.yarinlevi.waypoints.commands;

import me.yarinlevi.waypoints.gui.GuiUtils;
import me.yarinlevi.waypoints.utils.LocationData;
import me.yarinlevi.waypoints.utils.LocationUtils;
import me.yarinlevi.waypoints.utils.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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
            String msg = Utils.newMessage("&eAdvanced chunk scan:\n" +
                    String.format("&b  • &eCoordinates: &bX&e: &d%s &bY&e: &d%s &bZ&e: &d%s\n", locDetail.getX(), locDetail.getY(), locDetail.getZ()) +
                    String.format("&b  • &eIs Slime Chunk?: &d%s\n", locDetail.isSlimeChunk()) +
                    String.format("&b  • &eWorld: &d%s", locDetail.getWorld()));
            p.sendMessage(msg);
        } else if (args[0].equalsIgnoreCase("spawn")) {
            LocationData locDetail = LocationUtils.handleLocation(p.getWorld().getSpawnLocation());
            String msg = Utils.newMessage("&eSpawn locator:\n" +
                    String.format("&b  • &eCoordinates: &bX&e: &d%s &bY&e: &d%s &bZ&e: &d%s\n", locDetail.getX(), locDetail.getY(), locDetail.getZ()) +
                    String.format("&b  • &eDistance to coordinates: &d%s &bblocks", Utils.calculateDistance(p.getLocation(), p.getWorld().getSpawnLocation())));
            p.sendMessage(msg);
        } else if (args[0].equalsIgnoreCase("gui")) {
            GuiUtils.openInventory(args[1], p);
        } else {
            p.sendMessage(Utils.newMessage("&cInvalid usage!"));
        }

        return true;
    }
}
