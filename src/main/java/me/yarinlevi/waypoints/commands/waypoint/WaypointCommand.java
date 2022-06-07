package me.yarinlevi.waypoints.commands.waypoint;

import me.yarinlevi.waypoints.Waypoints;
import me.yarinlevi.waypoints.commands.SubCommand;
import me.yarinlevi.waypoints.commands.waypoint.sub.*;
import me.yarinlevi.waypoints.gui.GuiUtils;
import me.yarinlevi.waypoints.utils.MessagesUtils;
import me.yarinlevi.waypoints.waypoint.Waypoint;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author YarinQuapi
 **/
public class WaypointCommand implements CommandExecutor, TabExecutor {
    private final Map<String, SubCommand> commandMap = new HashMap<>();

    public WaypointCommand() {
        commandMap.put("help", new HelpSubCommand());
        commandMap.put("create", new CreateSubCommand());
        commandMap.put("delete", new DeleteSubCommand());
        commandMap.put("check", new CheckSubCommand());
        commandMap.put("distance", new DistanceSubCommand());
        commandMap.put("share", new ShareSubCommand());
        commandMap.put("track", new TrackSubCommand());
        commandMap.put("list", new ListSubCommand());
        commandMap.put("nearest", new NearestSubCommand());
        commandMap.put("spawn", new SpawnSubCommand());
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (args.length == 0 && (sender instanceof Player player)) {
            GuiUtils.openInventory("gui.personal.profile", player);
            return true;
        }

        if (sender instanceof Player player) {
            if (commandMap.containsKey(args[0].toLowerCase())) {
                commandMap.get(args[0].toLowerCase()).run(player, args);
                return true;
            } else {
                commandMap.get("help").run(player, args);
                return false;
            }
        }

        sender.sendMessage(MessagesUtils.getMessage("must_be_player"));
        return false;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        List<String> list = new ArrayList<>();

        if (args.length == 1) {
            list.addAll(List.of(new String[] { "list", "create", "delete", "check", "distance", "track", "nearest", "help", "spawn" } ));
        }

        if (args.length == 2) {
            switch (args[0].toLowerCase()) {
                case "delete", "check", "distance", "track" -> Waypoints.getInstance().getWaypointHandler().getWaypoints((Player) commandSender).stream().forEach(waypoint -> list.add(waypoint.getName()));
            }
        }

        else if (args.length == 3) {
            if ("distance".equalsIgnoreCase(args[0])) {
                List<Waypoint> waypointList = Waypoints.getInstance().getWaypointHandler().getWaypoints((Player) commandSender).stream().toList();
                waypointList.forEach(wp -> list.add(wp.getName()));
            }
        }

        return list;
    }
}
