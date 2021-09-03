package me.yarinlevi.waypoints.commands;

import me.yarinlevi.waypoints.Waypoints;
import me.yarinlevi.waypoints.exceptions.PlayerDoesNotExistException;
import me.yarinlevi.waypoints.exceptions.PlayerNotLoadedException;
import me.yarinlevi.waypoints.exceptions.WaypointAlreadyExistsException;
import me.yarinlevi.waypoints.exceptions.WaypointDoesNotExistException;
import me.yarinlevi.waypoints.gui.GuiUtils;
import me.yarinlevi.waypoints.utils.LocationData;
import me.yarinlevi.waypoints.utils.LocationUtils;
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
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Iterator;

/**
 * @author YarinQuapi
 */
public class MainCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!(sender instanceof final Player p)) {
            sender.sendMessage(Utils.newMessage("&cYou are required to be a Player to use this command."));
            return false;
        }

        if (args.length > 3) {
            p.sendMessage(Utils.newMessage("&cExcessive arguments."));
            return false;
        }

        if (args.length > 0) {
            switch (args[0].toLowerCase()) {
                case "list" -> {
                    if (args.length == 1) {
                        if (Waypoints.getInstance().getWaypointHandler().getWaypoints(p).size() > 0) {
                            this.list(p, Waypoints.getInstance().getWaypointHandler().getWaypointList(p).iterator());
                        } else {
                            p.sendMessage(Utils.newMessage("&cYou do not have any waypoints."));
                        }
                    } else if (args.length == 2) {
                        String world = args[1];

                        if (Arrays.stream(WaypointWorld.values()).anyMatch(x -> x.getKeys().contains(world.toLowerCase()))) {
                            WaypointWorld waypointWorld = Arrays.stream(WaypointWorld.values()).filter(x -> x.getKeys().contains(world.toLowerCase())).findFirst().get();

                            Iterator<String> waypoints = Waypoints.getInstance().getWaypointHandler().getWaypointList(p, waypointWorld).iterator();

                            if (waypoints.hasNext()) {
                                this.list(p, waypoints);
                            } else {
                                p.sendMessage(Utils.newMessage("&cNo waypoints detected in world &4" + waypointWorld.getName()));
                            }
                        } else {
                            p.sendMessage(Utils.newMessage("&cUnknown world type. "));
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
                        String msg = Utils.newMessage("&7Spawn locator:\n" +
                                String.format("&a  • &7Coordinates &bX &a%s &bY &a%s &bZ &a%s\n", locDetail.x(), locDetail.y(), locDetail.z()) +
                                String.format("&a  • &7Distance to coordinates &b%s &7blocks", Utils.calculateDistance(p.getLocation(), p.getWorld().getSpawnLocation())));
                        p.sendMessage(msg);
                    }
                }

                case "help" -> {
                    String str = Utils.newMessage("&7Commands:\n" +
                            "&a  • &b/wp help &f- &7Show this help menu\n" +
                            "&a  • &b/wp check <&awaypoint&b> &f- &7Check a certain waypoint.\n" +
                            "&a  • &b/wp create <&aname&b> &f- &7Create a new waypoint\n" +
                            "&a  • &b/wp list [&aworld&b] &f- &7List all (or some) your waypoints\n" +
                            "&a  • &b/wp delete <&awaypoint&b | &adeathpoints&b> &f- &7Delete a waypoint\n" +
                            "&a  • &b/wp spawn &f- &7Locates the spawn of the world\n" +
                            "&a  • &b/wp nearest &f- &7Locates the nearest waypoint\n" +
                            "&a  • &b/wp distance <&awaypointA&b> <&awaypointB&b> &f- &7Calculates the distance between two waypoints\n" +
                            "&a  • &b/wp set <&awaypoint&b> <&astate&b> &f- &7Changes the state of the waypoint\n" +
                            "&a  • &b/wp track <&awaypoint&b> &f- &7Tracks a waypoints" +
                            "\n &b&lQWaypoints Version&7&l: &a&l" + Waypoints.getInstance().getDescription().getVersion());
                    p.sendMessage(str);
                }

                case "nearest" -> {
                    Waypoint nearest = Waypoints.getInstance().getWaypointHandler().getNearestWaypoint(p);

                    if (nearest != null) {
                        String msg = Utils.newMessage(String.format("&7The closest waypoint to you is &b%s &7at &b%s &7blocks away from you, located at %s", nearest.getName(), nearest.getDistance(), nearest.getFormattedCoordinates()));

                        p.sendMessage(msg);
                    } else {
                        p.sendMessage(Utils.newMessage("&cYou do not have any waypoints in this world."));
                    }
                }

                case "toggle" -> {
                    if (args.length >= 2) {
                        String foundArgument = args[1];

                        switch (foundArgument) {
                            case "deathpoints":
                                Waypoints.getInstance().getPlayerDataManager().getPlayerDataMap().get(p.getUniqueId())
                                        .setPlayerDeathPoints(!Waypoints.getInstance()
                                                .getPlayerDataManager()
                                                .getPlayerDataMap().get(p.getUniqueId())
                                                .isPlayerDeathPoints());

                                boolean deathpoints = Waypoints.getInstance().getPlayerDataManager().getPlayerDataMap().get(p.getUniqueId()).isPlayerDeathPoints();
                                String toggled = deathpoints ? "&aenabled" : "&cdisabled";

                                p.sendMessage(Utils.newMessage("&7Setting '&eDeathPoints&7' was toggled " + toggled + "&7!"));
                                return true;
                            default:
                                p.sendMessage(Utils.newMessage("&cToggle failed! &7No setting '&c" + foundArgument + "&7'."));
                                return false;
                        }
                    } else {
                        p.sendMessage(Utils.newMessage("&cSetting change failed! &7Not enough arguments!"));
                    }
                }

                case "create" -> {
                    if (args.length >= 2) {
                        String name = args[1].trim();

                        if (Utils.allowedCharacters.matcher(name).matches()) {

                            Waypoint wp = new Waypoint(p.getUniqueId(), name, p.getLocation(), false);

                            try {
                                if (Waypoints.getInstance().getWaypointHandler().addWaypoint(p.getUniqueId(), wp)) {
                                    p.sendMessage(Utils.newMessage(String.format("&7Created new waypoint: &b%s", name)));
                                }
                            } catch (WaypointAlreadyExistsException | PlayerNotLoadedException exception) {
                                p.sendMessage(exception.getMessage());
                            }
                        } else {
                            p.sendMessage(Utils.newMessage("&cIllegal characters found (Allowed: A-z,0-9)"));
                        }
                    } else {
                        p.sendMessage(Utils.newMessage("&cCreate failed! &7Not enough arguments!"));
                    }
                }

                case "check" -> {
                    if (args.length >= 2) {
                        String name = args[1];

                        Waypoint wp = Waypoints.getInstance().getWaypointHandler().getWaypoint(p, name);

                        LocationData locationData = wp.getLocationData();

                        String msg = Utils.newMessage(String.format("&7Waypoint &b%s &7is located at &bX &a%s &bY &a%s &bZ &a%s &7in world &b%s &7You are &b%s &7blocks away.",
                                name, locationData.x(), locationData.y(), locationData.z(), WaypointWorld.valueOf(locationData.world()).getName(), Utils.calculateDistance(p.getLocation().toVector(), wp.getVector())));
                        p.sendMessage(msg);
                    } else {
                        p.sendMessage(Utils.newMessage("&cCheck failed! &7Not enough arguments!"));
                    }
                }

                case "delete" -> {
                    if (args.length >= 2) {
                        String name = args[1];

                        try {
                            if (Waypoints.getInstance().getWaypointHandler().removeWaypoint(p, name)) {
                                p.sendMessage(Utils.newMessage(String.format("&7Deleted waypoint &b%s", name)));
                            }
                        } catch (PlayerNotLoadedException | WaypointDoesNotExistException exception) {
                            p.sendMessage(exception.getMessage());
                        }
                    } else {
                        p.sendMessage(Utils.newMessage("&cDelete failed! &7Not enough arguments!"));
                    }
                }

                case "set" -> {
                    if (args.length < 3 || Arrays.stream(WaypointState.values()).noneMatch(x -> x.name().equals(args[2].toUpperCase()))) {
                        p.sendMessage(Utils.newMessage("&cState changed failed! &7Incorrect syntax! please try &e/wp set <waypoint> <state>"));
                    } else {
                        String waypoint = args[1];
                        WaypointState state = WaypointState.valueOf(args[2].toUpperCase());

                        switch (state) {
                            case PRIVATE, PUBLIC -> {
                                if (Waypoints.getInstance().getConfig().getBoolean("PublicWaypoints")) {
                                    try {
                                        Waypoints.getInstance().getPlayerListener().setWaypointState(p.getUniqueId(), waypoint, state);
                                        Waypoints.getInstance().getWaypointHandler().getWaypoint(p, waypoint).setState(state);

                                        p.sendMessage(Utils.newMessage("&7Successfully set waypoint state to " + state.getState()));

                                    } catch (PlayerDoesNotExistException | WaypointDoesNotExistException e) {
                                        p.sendMessage(e.getMessage());
                                    }
                                } else {
                                    p.sendMessage(Utils.newMessage("&cSorry, changing waypoint state is not allowed."));
                                }
                            }

                            case SERVER -> {
                                if (p.hasPermission("qwaypoints.admin.serverwaypoints")) {
                                    throw new NotImplementedException();
                                } else
                                    p.sendMessage(Utils.newMessage("&cSorry, you do not have the required permission."));
                            }

                        }
                    }
                }

                case "track" -> {
                    if (args.length >= 2) {
                        if (args[1].equalsIgnoreCase("off")) {
                            if (Waypoints.getInstance().getTrackerManager().unTrack(p)) {
                                p.sendMessage(Utils.newMessage("&cNo longer tracking."));
                            } else {
                                p.sendMessage(Utils.newMessage("&cYou are not tracking any waypoints."));
                            }

                        } else {
                            Waypoint wp = Waypoints.getInstance().getWaypointHandler().getWaypoint(p, args[1]);

                            if (wp != null) {
                                if (Waypoints.getInstance().getTrackerManager().track(p, wp, "actionbar")) {

                                    p.sendMessage(Utils.newMessage("&7Tracking - " + wp.getName()));
                                    p.setCompassTarget(wp.getLocation());
                                } else {
                                    p.sendMessage(Utils.newMessage("&cTracking failed! &7You are already tracking a waypoint!"));
                                    p.sendMessage(Utils.newMessage("&7Use &e/wp track off &7in order to stop tracking."));
                                }
                            } else {
                                p.sendMessage(Utils.newMessage("&cTrack failed! &7Waypoint doesn't exist!"));
                            }
                        }
                    } else {
                        p.sendMessage(Utils.newMessage("&cTrack failed! &7Not enough arguments!"));
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

                                p.sendMessage(Utils.newMessage(String.format("&7The distance between waypoint &b%s &7and &b%s &7is &b%s &7blocks", wpA.getName(), wpB.getName(), distance)));
                            } else {
                                p.sendMessage(Utils.newMessage(String.format("&cWaypoint %s doesn't exist!", waypointB)));
                            }
                        } else {
                            p.sendMessage(Utils.newMessage(String.format("&cWaypoint %s doesn't exist!", waypointA)));
                        }
                    } else {
                        p.sendMessage(Utils.newMessage("&cWrong argument syntax!"));
                    }
                }
                default -> p.sendMessage(Utils.newMessage("&cCommand failed! &7command doesn't exist!"));
            }
        } else {
            GuiUtils.openInventory("gui.personal.profile", p);
        }

        return true;
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
