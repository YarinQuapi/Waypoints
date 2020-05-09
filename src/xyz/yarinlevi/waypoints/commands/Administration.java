package xyz.yarinlevi.waypoints.commands;

import xyz.yarinlevi.waypoints.Waypoints;
import xyz.yarinlevi.waypoints.data.WaypointManager2;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.List;

public class Administration implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        final Player p = (Player)sender;

        if(args[0].equalsIgnoreCase("list")) {
            final Player target = Bukkit.getPlayer(args[1]);
            WaypointManager2.listWaypoints(target,p);
            return true;
        } else if (args[0].equalsIgnoreCase("check")) {
            if(args.length == 3) {
                final Player target = Bukkit.getPlayer(args[1]);
                WaypointManager2.getWaypoint(target, p, args[2]);
                return true;
            }
            else if(args.length == 2) {
                p.sendMessage(Waypoints.ladminprefix + "Too little arguments.");
                return true;
            }
            else if(args.length > 3) {
                p.sendMessage(Waypoints.ladminprefix + "Excessive arguments.");
                return true;
            }
        } else if (args[0].equalsIgnoreCase("tp")) {
            /*
            1. Come up with a way to have other players (offline & online) and same player.
            2. Code it effectively.
             */
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String s, String[] args) {
        return null;
    }
}
