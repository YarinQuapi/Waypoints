package xyz.yarinlevi.waypoints.commands;

import xyz.yarinlevi.waypoints.Waypoints;
import xyz.yarinlevi.waypoints.data.WaypointManager2;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Commands2 implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("I think you're mistaken. You are required to be a player to execute this command.");
            return false;
        }

        final Player p = (Player)sender;
        if(args.length==0) {
            p.sendMessage("Help command not yet implemented.");
            return true;
        }
        if(args[0].equalsIgnoreCase("create")) {
            if(args.length == 2) {
                WaypointManager2.addWaypoint(p, args[1], p.getLocation());
                p.sendMessage(Waypoints.lprefix + "Successfully created waypoint: \"" + ChatColor.AQUA + args[1] + ChatColor.RESET + "\"");
                return true;
            } else
            return false;
        } else if (args[0].equalsIgnoreCase("check")) {
            if(args.length == 2) {
                WaypointManager2.getWaypoint(p, p, args[1]);
                return true;
            } else return false;
        } else if (args[0].equalsIgnoreCase("list")) {
            WaypointManager2.listWaypoints(p, p);
            return true;
        } else if (args[0].equalsIgnoreCase("delete")) {
            if(args.length ==2) {
                WaypointManager2.deleteWaypoint(p, args[1]);
                p.sendMessage(Waypoints.lprefix + "Successfully deleted waypoint.");
                return true;
            }
        } else if(WaypointManager2.doesWaypointExist(p, args[0])) {
            if(args.length !=1) {
                p.sendMessage(Waypoints.lprefix + "Excessive arguments.");
            }
            WaypointManager2.getWaypoint(p, p, args[0]);
            return true;
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String s, String[] args) {
        ArrayList<String> list = new ArrayList<>();
        final Player p = (Player)sender;
        if(args.length==1) {
            list.add("create");
            list.add("check");
            list.add("list");
            list.add("delete");
            return list;
        }
        if (args.length == 2) {
            if(args[0].equalsIgnoreCase("list")) {
                if(args.length >= 1) {
                    return list;
                }
            } else if(args.length >= 2) {
                if(args[0].equalsIgnoreCase("check")) {
                    if(args.length != 2) return list;
                    return WaypointManager2.listWaypointsTabCompleter(p);
                } else if(args[0].equalsIgnoreCase("delete")) {
                    if(args.length != 2) return list;
                    return WaypointManager2.listWaypointsTabCompleter(p);
                } else if(args[0].equalsIgnoreCase("create")) {
                    if(args.length != 2) return list;
                    return list;
                }
            }
        }
        return list;
    }

    enum SELECTION {
    }
}
