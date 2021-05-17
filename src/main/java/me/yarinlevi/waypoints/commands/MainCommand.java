package me.yarinlevi.waypoints.commands;

import me.yarinlevi.waypoints.Waypoints;
import me.yarinlevi.waypoints.exceptions.PlayerNotLoadedException;
import me.yarinlevi.waypoints.exceptions.WaypointAlreadyExistsException;
import me.yarinlevi.waypoints.exceptions.WaypointDoesNotExistException;
import me.yarinlevi.waypoints.gui.GuiHandler;
import me.yarinlevi.waypoints.utils.LocationData;
import me.yarinlevi.waypoints.utils.Utils;
import me.yarinlevi.waypoints.waypoint.Waypoint;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Iterator;

public class MainCommand implements CommandExecutor {

    @SuppressWarnings("deprecation")
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Utils.newMessage("&cYou are required to be a Player to use this command."));
            return false;
        }

        final Player p = (Player) sender;

        if (args.length > 2) {
            p.sendMessage(Utils.newMessage("&cExcessive arguments."));
            return false;
        }

        if (args.length == 0) {
            GuiHandler.openInventory("gui.personal.profile", p);
            return true;
        } else if (args.length == 1) {
            if (args[0].equalsIgnoreCase("list")) {
                Iterator<String> waypointList = Waypoints.getInstance().getWaypointHandler().getWaypointList(p).iterator();

                if (waypointList.hasNext()) {
                    TextComponent message = new TextComponent(ChatColor.translateAlternateColorCodes('&', Waypoints.getInstance().getPrefix()));
                    final TextComponent SPACE = new TextComponent(ChatColor.YELLOW + ", ");
                    String waypoint;
                    TextComponent waypointText;
                    while (waypointList.hasNext()) {
                        waypoint = waypointList.next();
                        waypointText = new TextComponent(ChatColor.LIGHT_PURPLE + waypoint);
                        waypointText.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/wp check " + waypoint));
                        waypointText.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.RESET + "Click to check waypoint \"" + ChatColor.AQUA + waypoint + ChatColor.RESET + "\"").create()));
                        message.addExtra(waypointText);
                        if (waypointList.hasNext()) {
                            message.addExtra(SPACE);
                        }
                    }
                    p.spigot().sendMessage(message);

                } else {
                    p.sendMessage(Utils.newMessage("&cYou do not have any waypoints."));
                }
                return true;
            } else if (args[0].equalsIgnoreCase("help")) {
                String str = Utils.newMessage("&eCommands:\n" +
                        "&b  • &d/wp help &f- &eShow this command\n" +
                        "&b  • &d/wp check <&awaypoint&d> &f- &eCheck a certain waypoint.\n" +
                        "&b  • &d/wp create <&aname&d> &f- &eCreate a new waypoint\n" +
                        "&b  • &d/wp list [&aworld&d] &f- &eList all (or some) your waypoints\n" +
                        "&b  • &d/wp delete <&awaypoint&d | &adeathpoints&d> &f- &eDelete a waypoint\n" +
                        "&b  • &d/wp spawn &f- &eLocates the spawn of the world.\n" +
                        "\n &b&lQWaypoints Version&e&l: &a&l" + Waypoints.getInstance().getDescription().getVersion());
                p.sendMessage(str);
                return true;
            } else {
                p.sendMessage(Utils.newMessage("&cSub command: \"" + args[0] + "\" was not found"));
                return false;
            }
        } else {
            if (args[0].equalsIgnoreCase("create")) {
                String name = args[1];

                Waypoint wp = new Waypoint(name, p.getLocation(), false);

                try {
                    if (Waypoints.getInstance().getWaypointHandler().addWaypoint(p, wp)) {
                        p.sendMessage(Utils.newMessage(String.format("&eCreated new Waypoint: &f\"&d%s&f\"", name)));
                        return true;
                    }
                    return false;
                } catch (WaypointAlreadyExistsException | PlayerNotLoadedException exception) {
                    p.sendMessage(exception.getMessage());
                    return false;
                }
            } else if (args[0].equalsIgnoreCase("check")) {
                String name = args[1];

                Waypoint wp = Waypoints.getInstance().getWaypointHandler().getWaypoint(p, name);

                LocationData locationData = wp.getLocationData();

                String msg = Utils.newMessage(String.format("&eWaypoint &f\"&d%s&f\" &eis located at &bX&e: &d%s &bY&e: &d%s &bZ&e: &d%s &ein &bworld&e: &d%s &eYou are &d%s &bblocks &eaway.",
                        name, locationData.getX(), locationData.getY(), locationData.getZ(), locationData.getWorld(), Utils.calculateDistance(p.getLocation().toVector(), wp.getVector())));
                p.sendMessage(msg);

                return true;
            } else if (args[0].equalsIgnoreCase("delete")) {
                String name = args[1];

                try {
                    if (Waypoints.getInstance().getWaypointHandler().removeWaypoint(p, name)) {
                        p.sendMessage(Utils.newMessage(String.format("&eDeleted waypoint: &f\"&d%s&f\"", name)));
                        return true;
                    }
                    return false;
                } catch (PlayerNotLoadedException | WaypointDoesNotExistException exception) {
                    p.sendMessage(exception.getMessage());
                    return false;
                }
            } else if (args[0].equalsIgnoreCase("setcompass")) {
                String name = args[1];

                Waypoint wp = Waypoints.getInstance().getWaypointHandler().getWaypoint(p, name);

                p.setCompassTarget(wp.getLocation());

                p.sendMessage(Utils.newMessage(String.format("&eSet compass to point to waypoint: %s", name)));
                return true;

            } else {
                p.sendMessage(Utils.newMessage("&cSub command: \"" + args[0] + "\" was not found"));
                return false;
            }
        }
    }
}
