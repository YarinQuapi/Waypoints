package me.yarinlevi.waypoints.commands;

import me.yarinlevi.waypoints.Waypoints;
import me.yarinlevi.waypoints.gui.GuiUtils;
import me.yarinlevi.waypoints.utils.LocationData;
import me.yarinlevi.waypoints.utils.LocationUtils;
import me.yarinlevi.waypoints.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.annotation.command.Commands;
import org.jetbrains.annotations.NotNull;

/**
 * @author YarinQuapi
 */
@Commands(@org.bukkit.plugin.java.annotation.command.Command(name = "qwaypointsadmin", permission = "qwaypoints.admin", aliases = { "qwpa", "qwpadmin", "wpadmin", "wpa" }))
public class Administration implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, String s, String[] args) {
        if (!(sender instanceof final Player p)) {
            sender.sendMessage(Utils.newMessage("&cYou are required to be a Player to use this command."));
            return false;
        }

        if (args.length == 0) {
            p.sendMessage(Utils.newMessage("&cMissing arguments."));
            return false;
        }

        if (args[0].equalsIgnoreCase("chunkscan")) {
            LocationData locDetail = LocationUtils.handleLocation(p.getLocation());
            String msg = Utils.newMessage("&7Advanced chunk scan:\n" +
                    String.format("&a  • &7Coordinates &bX &a%s &bY &a%s &bZ &a%s\n", locDetail.x(), locDetail.y(), locDetail.z()) +
                    String.format("&a  • &7Is Slime Chunk?: &b%s\n", locDetail.slimeChunk()) +
                    String.format("&a  • &7World &b%s", locDetail.world()));
            p.sendMessage(msg);
        } else if (args[0].equalsIgnoreCase("spawn")) {
            LocationData locDetail = LocationUtils.handleLocation(p.getWorld().getSpawnLocation());
            String msg = Utils.newMessage("&7Spawn locator:\n" +
                    String.format("&a  • &7Coordinates &bX &a%s &bY &a%s &bZ &a%s\n", locDetail.x(), locDetail.y(), locDetail.z()) +
                    String.format("&a  • &7Distance to coordinates &b%s &7blocks", Utils.calculateDistance(p.getLocation(), p.getWorld().getSpawnLocation())));
            p.sendMessage(msg);
        } else if (args[0].equalsIgnoreCase("gui")) {
            GuiUtils.openInventory(args[1], p);

        } else if (args[0].equalsIgnoreCase("load")) {
            OfflinePlayer offlinePlayer;
            if ((offlinePlayer = Bukkit.getOfflinePlayer(args[0])) != null) {
                Waypoints.getInstance().getPlayerListener().loadPlayer(offlinePlayer.getUniqueId());
                p.sendMessage(Utils.newMessage("&7Successfully loaded player " + offlinePlayer.getPlayer()));
            }
        } else if (args[0].equalsIgnoreCase("unload")) {
            OfflinePlayer offlinePlayer;
            if ((offlinePlayer = Bukkit.getOfflinePlayer(args[0])) != null) {
                Waypoints.getInstance().getPlayerListener().unloadPlayer(offlinePlayer.getUniqueId());
                p.sendMessage(Utils.newMessage("&7Successfully unloaded player " + offlinePlayer.getPlayer()));
            }
        } else {
            p.sendMessage(Utils.newMessage("&cInvalid usage!"));
        }

        return true;
    }
}
