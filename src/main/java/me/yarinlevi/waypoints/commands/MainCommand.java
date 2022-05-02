package me.yarinlevi.waypoints.commands;

import me.yarinlevi.waypoints.Waypoints;
import me.yarinlevi.waypoints.exceptions.PlayerDoesNotExistException;
import me.yarinlevi.waypoints.exceptions.PlayerNotLoadedException;
import me.yarinlevi.waypoints.exceptions.WaypointAlreadyExistsException;
import me.yarinlevi.waypoints.exceptions.WaypointDoesNotExistException;
import me.yarinlevi.waypoints.gui.GuiUtils;
import me.yarinlevi.waypoints.player.trackers.ETracker;
import me.yarinlevi.waypoints.utils.LocationData;
import me.yarinlevi.waypoints.utils.LocationUtils;
import me.yarinlevi.waypoints.utils.MessagesUtils;
import me.yarinlevi.waypoints.utils.Utils;
import me.yarinlevi.waypoints.waypoint.Waypoint;
import me.yarinlevi.waypoints.waypoint.WaypointState;
import me.yarinlevi.waypoints.waypoint.WaypointWorld;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.apache.commons.lang.NotImplementedException;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.annotation.command.Commands;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * @author YarinQuapi
 */
@Commands(@org.bukkit.plugin.java.annotation.command.Command(name = "qwaypoint", desc = "Main command", aliases = { "wp", "qwp", "qwaypoints", "waypoints", "waypoint" }))
public class MainCommand implements CommandExecutor, TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!(sender instanceof final Player p)) {
            sender.sendMessage(MessagesUtils.getMessage("must_be_player"));
            return false;
        }

        if (args.length > 3) {
            p.sendMessage(MessagesUtils.getMessage("excessive_arguments"));
            return false;
        }

        if (args.length > 0) {
            switch (args[0].toLowerCase()) {
                case "list" -> {
                    if (args.length == 1) {
                        if (Waypoints.getInstance().getWaypointHandler().getWaypoints(p).size() > 0) {
                            this.list(p, Waypoints.getInstance().getWaypointHandler().getWaypointList(p).iterator());
                        } else {
                            p.sendMessage(MessagesUtils.getMessage("no_waypoints"));
                        }
                    } else if (args.length == 2) {
                        String world = args[1];

                        if (Arrays.stream(WaypointWorld.values()).anyMatch(x -> x.getKeys().contains(world.toLowerCase()))) {
                            WaypointWorld waypointWorld = Arrays.stream(WaypointWorld.values()).filter(x -> x.getKeys().contains(world.toLowerCase())).findFirst().get();

                            Iterator<String> waypoints = Waypoints.getInstance().getWaypointHandler().getWaypointList(p, waypointWorld).iterator();

                            if (waypoints.hasNext()) {
                                this.list(p, waypoints);
                            } else {
                                p.sendMessage(MessagesUtils.getMessage("no_waypoint_world", waypointWorld.getName()));
                            }
                        } else {
                            p.sendMessage(MessagesUtils.getMessage("unknown_world"));
                        }
                    }
                }

                case "public" -> {
                    if (Waypoints.getInstance().getConfig().getBoolean("PublicWaypoints")) {
                        GuiUtils.openInventory("gui.public.browser", p);
                    }
                }

                case "spawn" -> {
                    if (p.hasPermission("qwaypoints.commands.spawn")) {
                        LocationData locDetail = LocationUtils.handleLocation(p.getWorld().getSpawnLocation());
                        String msg = MessagesUtils.getMessageLines("spawn_locate_command", locDetail.x(), locDetail.y(), locDetail.z(), Utils.calculateDistance(p.getLocation(), p.getWorld().getSpawnLocation()));
                        p.sendMessage(msg);
                    }
                }

                case "help" -> {
                    String str = MessagesUtils.getMessageLines("help_command") +
                            "\n §b§lQWaypoints Version§7§l: §a§l" + Waypoints.getInstance().getDescription().getVersion();
                  
                    p.sendMessage(str);
                }

                case "nearest" -> {
                    Waypoint nearest = Waypoints.getInstance().getWaypointHandler().getNearestWaypoint(p);

                    if (nearest != null) {
                        String msg = MessagesUtils.getMessage("closest_waypoint", nearest.getName(), nearest.getDistance(), nearest.getFormattedCoordinates());

                        p.sendMessage(msg);
                    } else {
                        p.sendMessage(MessagesUtils.getMessage("no_waypoint_world", p.getWorld().getEnvironment().name()));
                    }
                }

                case "setting" -> {
                    if (args.length >= 2) {
                        String foundArgument = args[1];

                        switch (foundArgument) {
                            case "deathpoints" -> {
                                boolean deathpoints = !Waypoints.getInstance().getPlayerDataManager().getPlayerDataMap().get(p.getUniqueId()).isPlayerDeathPoints();

                                Waypoints.getInstance().getPlayerDataManager().getPlayerDataMap().get(p.getUniqueId())
                                        .setPlayerDeathPoints(deathpoints);

                                String toggled = deathpoints ? MessagesUtils.getMessage("enabled") : MessagesUtils.getMessage("disabled");

                                p.sendMessage(MessagesUtils.getMessage("setting_toggled", "DeathPoints", toggled));
                                return true;
                            }

                            case "tracker" -> {
                                if (args.length >= 3) {
                                    ETracker tracker = Waypoints.getInstance().getTrackerManager().getTracker(args[2]).getETracker();

                                    Waypoints.getInstance().getPlayerDataManager().getPlayerDataMap().get(p.getUniqueId()).setETracker(tracker);

                                    p.sendMessage(MessagesUtils.getMessage("tracker_changed", tracker.getKey()));
                                    return true;
                                } else {
                                    p.sendMessage(MessagesUtils.getMessage("setting_change_failed_args"));
                                    return false;
                                }
                            }

                            default -> {
                                p.sendMessage(MessagesUtils.getMessage("setting_change_failed_not_found", foundArgument));
                                return false;
                            }
                        }
                    } else {
                        p.sendMessage(MessagesUtils.getMessage("setting_change_failed_args"));
                    }
                }

                case "create" -> {
                    if (args.length >= 2) {
                        String name = args[1].trim();

                        if (Utils.allowedCharacters.matcher(name).matches()) {

                            Waypoint wp = new Waypoint(p.getUniqueId(), name, p.getLocation(), false);

                            try {
                                if (Waypoints.getInstance().getWaypointHandler().addWaypoint(p.getUniqueId(), wp)) {
                                    p.sendMessage(MessagesUtils.getMessage("waypoint_created", name));
                                }
                            } catch (WaypointAlreadyExistsException | PlayerNotLoadedException exception) {
                                p.sendMessage(exception.getMessage());
                            }
                        } else {
                            p.sendMessage(MessagesUtils.getMessage("illegal_characters"));
                        }
                    } else {
                        p.sendMessage(MessagesUtils.getMessage("create_failed_args"));
                    }
                }

                case "share" -> {
                    if (args.length >= 2) {
                        String name = args[1].trim();

                        Waypoint wp = Waypoints.getInstance().getWaypointHandler().getWaypoint(p, name);

                        if (wp != null) {
                            LocationData locationData = wp.getLocationData();

                            String msg = MessagesUtils.getMessage("share_waypoint", p.getName(),
                                    name, locationData.x(), locationData.y(), locationData.z(), WaypointWorld.valueOf(locationData.world()).getName(), Utils.calculateDistance(p.getLocation().toVector(), wp.getVector()));
                            Bukkit.broadcastMessage(msg);
                            return true;
                        } else {
                            p.sendMessage(Utils.newMessage("&cSorry, couldn't find a waypoint with that name."));
                            return false;
                        }
                    } else {
                        p.sendMessage(Utils.newMessage("&cShare failed! &7Not enough arguments!"));
                    }
                }

                case "check" -> {
                    if (args.length >= 2) {
                        String name = args[1];

                        Waypoint wp = Waypoints.getInstance().getWaypointHandler().getWaypoint(p, name);

                        if (wp != null) {
                            LocationData locationData = wp.getLocationData();

                            String msg = MessagesUtils.getMessage("waypoint_check",
                                    name, locationData.x(), locationData.y(), locationData.z(), WaypointWorld.valueOf(locationData.world()).getName(), Utils.calculateDistance(p.getLocation().toVector(), wp.getVector()));
                            p.sendMessage(msg);
                            return true;
                        } else {
                            p.sendMessage(MessagesUtils.getMessage("action_failed_not_found"));
                            return false;
                        }
                    } else {
                        p.sendMessage(MessagesUtils.getMessage("action_failed_args"));
                        return false;
                    }
                }

                case "remove", "delete" -> {
                    if (args.length >= 2) {
                        String name = args[1];

                        try {
                            if (Waypoints.getInstance().getWaypointHandler().removeWaypoint(p, name)) {
                                p.sendMessage(MessagesUtils.getMessage("waypoint_deleted", name));
                            }
                        } catch (PlayerNotLoadedException | WaypointDoesNotExistException exception) {
                            p.sendMessage(exception.getMessage());
                        }
                    } else {
                        p.sendMessage(MessagesUtils.getMessage("action_failed_args"));
                    }
                }

                case "set" -> {
                    if (args.length < 3 || Arrays.stream(WaypointState.values()).noneMatch(x -> x.name().equals(args[2].toUpperCase()))) {
                        p.sendMessage(MessagesUtils.getMessage("state_change_failed_syntax"));
                    } else {
                        String waypoint = args[1];
                        WaypointState state = WaypointState.valueOf(args[2].toUpperCase());

                        switch (state) {
                            case PRIVATE, PUBLIC -> {
                                if (Waypoints.getInstance().getConfig().getBoolean("PublicWaypoints")) {
                                    try {
                                        Waypoints.getInstance().getPlayerData().setWaypointState(p.getUniqueId(), waypoint, state);
                                        Waypoints.getInstance().getWaypointHandler().getWaypoint(p, waypoint).setState(state);

                                        p.sendMessage(MessagesUtils.getMessage("state_change", state.getState()));

                                    } catch (PlayerDoesNotExistException | WaypointDoesNotExistException e) {
                                        p.sendMessage(e.getMessage());
                                    }
                                } else {
                                    p.sendMessage(MessagesUtils.getMessage("state_change_failed_not_allowed"));
                                }
                            }

                            case SERVER -> {
                                if (p.hasPermission("qwaypoints.admin.serverwaypoints")) {
                                    throw new NotImplementedException();
                                } else
                                    p.sendMessage(MessagesUtils.getMessage("no_permission"));
                            }

                        }
                    }
                }

                case "track" -> {
                    if (args.length >= 2) {
                        if (args[1].equalsIgnoreCase("off")) {
                            if (Waypoints.getInstance().getTrackerManager().unTrack(p)) {
                                p.sendMessage(MessagesUtils.getMessage("tracking_disabled"));
                            } else {
                                p.sendMessage(MessagesUtils.getMessage("tracking_off"));
                            }

                        } else {
                            Waypoint wp;
                            if (!args[1].contains(":")) {
                                wp = Waypoints.getInstance().getWaypointHandler().getWaypoint(p, args[1]);
                            } else {
                                String playerName = args[1].split(":")[0];
                                String waypointName = args[1].split(":")[1];

                                wp = Waypoints.getInstance().getWaypointHandler().getAllPublicWaypoints().stream()
                                        .filter(waypoint -> waypoint.getOwner().equals(Bukkit.getOfflinePlayer(playerName).getUniqueId()))
                                        .filter(waypoint -> waypoint.getName().equalsIgnoreCase(waypointName)).findFirst().orElse(null);
                            }

                            if (wp != null) {
                                if (Waypoints.getInstance().getTrackerManager().track(p, wp, Waypoints.getInstance().getPlayerDataManager().getPlayerDataMap().get(p.getUniqueId()).getETracker())) {

                                    p.sendMessage(MessagesUtils.getMessage("tracking", wp.getName()));
                                    p.setCompassTarget(wp.getLocation());
                                } else {
                                    p.sendMessage(MessagesUtils.getMessage("tracking_failed_tracking"));
                                    p.sendMessage(MessagesUtils.getMessage("tracking_failed_tracking_2"));
                                }
                            } else {
                                p.sendMessage(MessagesUtils.getMessage("action_failed_not_found"));
                            }
                        }
                    } else {
                        p.sendMessage(MessagesUtils.getMessage("action_failed_args"));
                    }
                }

                case "distance" -> {
                    if (args.length == 3) {
                        String waypointA = args[1];
                        String waypointB = args[2];

                        Waypoint wpA = Waypoints.getInstance().getWaypointHandler().getWaypoint(p, waypointA);

                        if (wpA != null) {
                            Waypoint wpB = Waypoints.getInstance().getWaypointHandler().getWaypoint(p, waypointB);

                            if (wpB != null) {
                                int distance = Utils.calculateDistance(wpA.getVector(), wpB.getVector());

                                p.sendMessage(MessagesUtils.getMessage("waypoint_distance", wpA.getName(), wpB.getName(), distance));
                            } else {
                                p.sendMessage(MessagesUtils.getMessage("waypoint_distance_not_found", waypointB));
                            }
                        } else {
                            p.sendMessage(MessagesUtils.getMessage("waypoint_distance_not_found", waypointA));
                        }
                    } else {
                        p.sendMessage(MessagesUtils.getMessage("not_enough_args"));
                    }
                }
                default -> p.sendMessage(MessagesUtils.getMessage("command_not_exist"));
            }
        } else {
            GuiUtils.openInventory("gui.personal.profile", p);
        }

        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        List<String> list = new ArrayList<>();

        if (args.length == 1) {
            list.addAll(List.of(new String[] { "list", "create", "remove", "check", "set", "distance", "track", "nearest", "help" } ));

            if (commandSender.hasPermission("qwaypoints.commands.spawn")) {
                list.add("spawn");
            }

            if (Waypoints.getInstance().getConfig().getBoolean("PublicWaypoints")) {
                list.add("public");
            }
        }

        if (args.length == 2) {
            switch (args[0].toLowerCase()) {
                case "remove", "check", "distance", "track", "set" -> list.addAll(Waypoints.getInstance().getWaypointHandler().getWaypointList((Player) commandSender));

                case "setting" -> list.addAll(List.of(new String[] { "deathpoints", "tracker" } ));
            }
        }

        else if (args.length == 3) {
            switch (args[0].toLowerCase()) {
                case "distance" -> {
                    List<String> waypointList = Waypoints.getInstance().getWaypointHandler().getWaypointList((Player) commandSender).stream().toList();
                    list.addAll(waypointList);
                }

                case "set" -> {
                    if (Waypoints.getInstance().getConfig().getBoolean("PublicWaypoints")) {
                        list.add("public");
                    }
                    list.add("private");
                }

                case "setting" -> {
                    if (args[2].equalsIgnoreCase("tracker")) {
                        for (ETracker et : ETracker.values()) {
                            list.add(et.getKey());
                        }
                    }
                }
            }
        }

        return list;
    }

    private void list(Player p, Iterator<String> waypointList) {
        TextComponent message = new TextComponent(ChatColor.translateAlternateColorCodes('&', Waypoints.getInstance().getPrefix()));
        final TextComponent SPACE = new TextComponent(ChatColor.DARK_GRAY + ", ");
        String waypoint;
        TextComponent waypointText;

        do {
            waypoint = waypointList.next();

            waypointText = new TextComponent(ChatColor.GRAY + waypoint);
            waypointText.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/wp check " + waypoint));
            waypointText.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.GRAY + "Click to check waypoint " + ChatColor.AQUA + waypoint).create()));
            message.addExtra(waypointText);

            if (waypointList.hasNext()) {
                message.addExtra(SPACE);
            }
        } while (waypointList.hasNext());

        p.spigot().sendMessage(message);
    }
}
