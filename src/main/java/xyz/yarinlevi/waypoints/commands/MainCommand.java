package xyz.yarinlevi.waypoints.commands;

import xyz.yarinlevi.waypoints.Waypoints;
import xyz.yarinlevi.waypoints.data.WaypointManager;
import xyz.yarinlevi.waypoints.gui.GuiHandler;
import xyz.yarinlevi.waypoints.utils.LocationHandler;
import xyz.yarinlevi.waypoints.utils.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Stream;

public class MainCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage(Utils.newMessage("&cYou are required to be a Player to use this command."));
            return false;
        }

        final Player p = (Player) sender;

        if(args.length > 2) {
            p.sendMessage(Utils.newMessage("&cExcessive arguments."));
            return false;
        }

        if (args.length == 0) {
            GuiHandler.openInventory("gui.personal.profile", p);
        } else if(args.length == 1 && args[0].equalsIgnoreCase("help")) {
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
            if(args[0].equalsIgnoreCase("create")) {
                if(args.length == 2) {
                    return WaypointManager.addWaypoint(p, args[1], false);
                } else {
                    p.sendMessage(Utils.newMessage("&cInvalid usage! try: &e/wp help"));
                    return false;
                }
            } else if (args[0].equalsIgnoreCase("delete")) {
                if(args.length == 2 && args[1].equalsIgnoreCase("deathpoints")) {
                    return WaypointManager.deleteAllSystemInduced(p);
                }
                if(args.length == 2) {
                    return WaypointManager.deleteWaypoint(p, args[1]);
                } else {
                    p.sendMessage(Utils.newMessage("&cInvalid usage! try: &e/wp help"));
                    return false;
                }
            } else if (args[0].equalsIgnoreCase("list")) {
                if(args.length == 2) {
                    WaypointManager.listWaypointsPerWorld(p, translateWorldName(args[1]));
                    return true;
                }
                if(args.length == 1) {
                    WaypointManager.listWaypoints(p);
                    return true;
                } else {
                    p.sendMessage(Utils.newMessage("&cInvalid usage! try: &e/wp help"));
                    return false;
                }
            } else if (args[0].equalsIgnoreCase("check")) {
                if(args.length == 2) {
                    WaypointManager.getWaypoint(p, args[1]);
                    return true;
                } else {
                    p.sendMessage(Utils.newMessage("&cInvalid usage! try: &e/wp help"));
                    return false;
                }
            } else if (args[0].equalsIgnoreCase("spawn")) {
                HashMap<String, String> locDetail = LocationHandler.handleLocation(p.getWorld().getSpawnLocation());
                String msg = Utils.newMessage("&eSpawn locator:\n" +
                        String.format("&b  • &eCoordinates: &bX&e: &d%s &bY&e: &d%s &bZ&e: &d%s\n", locDetail.get("x"), locDetail.get("y"), locDetail.get("z")) +
                        String.format("&b  • &eDistance to coordinates: &d%s &bblocks", Utils.calculateDistance(p.getLocation(), p.getWorld().getSpawnLocation())));
                p.sendMessage(msg);
            } else {
                String str = Utils.newMessage("&cInvalid usage! try: &e/wp help");
                p.sendMessage(str);
                return false;
            }
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        ArrayList<String> list = new ArrayList<>();
        final Player p = (Player)sender;
        if(args.length==1) {
            Stream.of(
                    "help",
                    "create",
                    "delete",
                    "list",
                    "check",
                    "spawn"
            ).forEach(list::add);
            return list;
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("check")) {
                return WaypointManager.tabCompleterList(p);
            } else if (args[0].equalsIgnoreCase("delete")) {
                list.addAll(WaypointManager.tabCompleterList(p));
                list.add("deathpoints");
                return list;
            } else if (args[0].equalsIgnoreCase("list")) {
                Stream.of(
                        "nether",
                        "end",
                        "overworld"
                ).forEach(list::add);
                return list;
            }
        }
        return list;
    }

    private static String translateWorldName(String input) {
        ArrayList<String> possibleOverworldNames = new ArrayList<>();
        Stream.of(
                "overworld",
                "normal",
                "world"
        ).forEach(possibleOverworldNames::add);
        for (String name: possibleOverworldNames) {
            if(name.equals(input.toLowerCase())) {
                return "NORMAL";
            }
        }

        ArrayList<String> possibleNetherNames = new ArrayList<>();
        Stream.of(
                "nether",
                "the_nether",
                "world_nether",
                "nether_world",
                "netherland",
                "hell"
        ).forEach(possibleNetherNames::add);
        for (String name: possibleNetherNames) {
            if(name.equals(input.toLowerCase())) {
                return "NETHER";
            }
        }
        ArrayList<String> possibleEndNames = new ArrayList<>();
        Stream.of(
                "end",
                "the_end",
                "theend",
                "dragonworld"
        ).forEach(possibleEndNames::add);
        for (String name: possibleEndNames) {
            if(name.equals(input.toLowerCase())) {
                return "THE_END";
            }
        }
        return "NORMAL";
    }
}
