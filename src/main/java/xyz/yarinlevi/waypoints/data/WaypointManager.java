package xyz.yarinlevi.waypoints.data;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import xyz.yarinlevi.waypoints.Waypoints;
import xyz.yarinlevi.waypoints.utils.LocationHandler;
import xyz.yarinlevi.waypoints.utils.Utils;
import xyz.yarinlevi.waypoints.waypoint.WaypointWorld;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.stream.Stream;

public class WaypointManager {

    public static ArrayList<String> tabCompleterList(Player p) {
        if(Data.getPlayerData().getConfigurationSection(p.getUniqueId().toString()).getKeys(false).size() != 0) {
            return new ArrayList<>(Data.getPlayerData().getConfigurationSection(p.getUniqueId().toString()).getKeys(false));
        }
        return new ArrayList<>();
    }

    public static ArrayList<String> listWaypointsInWorld(Player p, WaypointWorld world) {
        Iterator<String> waypoints = Data.getPlayerData().getConfigurationSection(p.getUniqueId().toString()).getKeys(false).iterator();
        ArrayList<String> worldRequestedWaypoints = new ArrayList<>();

        String worldType = translateWorldName(world.toString());

        while (waypoints.hasNext()) {
            String wp = waypoints.next();
            ConfigurationSection wpSection = Data.getWaypointData(p, wp);
            HashMap<String, String> locDetail = LocationHandler.handleLocation((Location) wpSection.get("location"));
            if(locDetail.get("world").equals(worldType)) {
                worldRequestedWaypoints.add(wp);
            }
        }

        return worldRequestedWaypoints;
    }


    public static boolean getWaypoint(final Player p, String name) {
        if(Data.isWaypoint(p, name)) {
            ConfigurationSection wp = Data.getWaypointData(p, name);

            Location loc = (Location) wp.get("location");
            HashMap<String, String> locDetail = LocationHandler.handleLocation(loc);

            String msg = Utils.newMessage(String.format("&eWaypoint &f\"&d%s&f\" &eis located at &bX&e: &d%s &bY&e: &d%s &bZ&e: &d%s &ein &bworld&e: &d%s &eYou are &d%s &bblocks &eaway.", name, locDetail.get("x"), locDetail.get("y"), locDetail.get("z"), locDetail.get("world"), Utils.calculateDistance(p.getLocation(), loc)));


            p.sendMessage(msg);
            return true;
        } else {
            String msg = Utils.newMessage(String.format("&cNo waypoint found with name: &f\"&d%s&f\"", name));
            p.sendMessage(msg);
            return false;
        }
    }

    public static boolean addWaypoint(final Player p, String name, boolean systemInduced) {
        if(!Data.isWaypoint(p, name)) {
            Data.addWaypointToData(p, name, systemInduced);
            if(!systemInduced) {
                String msg = Utils.newMessage(String.format("&eCreated new Waypoint: &f\"&d%s&f\"", name));
                p.sendMessage(msg);
            }
            return true;
        } else {
            if(!systemInduced) {
                String msg = Utils.newMessage(String.format("&eWaypoint with name: &f\"&d%s&f\" &ealready exists.", name));
                p.sendMessage(msg);
            }
            return false;
        }
    }

    public static boolean deleteWaypoint(final Player p, String name) {
        if(!Data.isWaypoint(p, name)) {
            String msg = Utils.newMessage(String.format("&cNo waypoint found with name: &f\"&d%s&f\"", name));
            p.sendMessage(msg);
            return false;
        } else {
            Data.deleteWaypoint(p, name);
            String msg = Utils.newMessage(String.format("&eDeleted waypoint: &f\"&d%s&f\"", name));
            p.sendMessage(msg);
            return true;
        }
    }

    public static void listWaypoints(final Player target) {
        if(!Data.getPlayerData().contains(target.getUniqueId().toString())) {
            return;
        }
        
        if(Data.getPlayerData().getConfigurationSection(target.getUniqueId().toString()).getKeys(false).size() == 0) {
            target.sendMessage(Utils.newMessage("&cYou do not have any waypoints."));
        } else {
            TextComponent message = new TextComponent(ChatColor.translateAlternateColorCodes('&', Waypoints.getInstance().getPrefix()));
            final TextComponent SPACE = new TextComponent(ChatColor.YELLOW + ", ");
            Iterator<String> iterator = Data.getPlayerData().getConfigurationSection(target.getUniqueId().toString()).getKeys(false).iterator();
            while (iterator.hasNext()) {
                String string = iterator.next();
                TextComponent waypointText = new TextComponent(ChatColor.LIGHT_PURPLE + string);
                waypointText.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/wp check " + string));
                waypointText.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.RESET + "Click to check waypoint \"" + ChatColor.AQUA + string + ChatColor.RESET + "\"").create()));
                message.addExtra(waypointText);
                if (iterator.hasNext()) {
                    message.addExtra(SPACE);
                }
            }
            target.spigot().sendMessage(message);
        }
    }

    public static boolean deleteAllSystemInduced(Player p) {
        if(Data.deleteAllSystemInducedWaypoints(p)) {
            p.sendMessage(Utils.newMessage("&eSuccessfully deleted all deathpoints."));
            return true;
        } else {
            p.sendMessage(Utils.newMessage("&cYou do not have any death points."));
            return false;
        }
    }

    public static void listWaypointsPerWorld(final Player p, String world) {
        if(!Data.getPlayerData().contains(p.getUniqueId().toString())) {
            return;
        }

        if(Data.getPlayerData().getConfigurationSection(p.getUniqueId().toString()).getKeys(false).size() == 0) {
            p.sendMessage(Utils.newMessage("&cYou do not have any waypoints."));
        } else {
            TextComponent message = new TextComponent(ChatColor.translateAlternateColorCodes('&', Waypoints.getInstance().getPrefix()));
            final TextComponent SPACE = new TextComponent(ChatColor.YELLOW + ", ");

            Iterator<String> waypoints = Data.getPlayerData().getConfigurationSection(p.getUniqueId().toString()).getKeys(false).iterator();
            ArrayList<String> worldRequestedWaypoints = new ArrayList<>();

            while (waypoints.hasNext()) {
                String wp = waypoints.next();
                ConfigurationSection wpSection = Data.getWaypointData(p, wp);
                HashMap<String, String> locDetail = LocationHandler.handleLocation((Location) wpSection.get("location"));
                if(locDetail.get("world").equals(world)) {

                    worldRequestedWaypoints.add(wp);

                }
            }

            if(worldRequestedWaypoints.isEmpty()) {
                p.sendMessage(Utils.newMessage("&cYou do not have any waypoints in that world."));
            } else {
                Iterator<String> worldRequestedWaypointsIterator = worldRequestedWaypoints.iterator();
                while (worldRequestedWaypointsIterator.hasNext()) {
                    String string = worldRequestedWaypointsIterator.next();

                    TextComponent waypointText = new TextComponent(ChatColor.LIGHT_PURPLE + string);
                    waypointText.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/wp check " + string));
                    waypointText.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.RESET + "Click to check waypoint \"" + ChatColor.AQUA + string + ChatColor.RESET + "\"").create()));
                    message.addExtra(waypointText);

                    if (worldRequestedWaypointsIterator.hasNext()) {
                        message.addExtra(SPACE);
                    }
                }
                p.spigot().sendMessage(message);
            }
        }
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
