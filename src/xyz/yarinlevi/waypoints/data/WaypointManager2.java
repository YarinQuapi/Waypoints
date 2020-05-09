package xyz.yarinlevi.waypoints.data;

import xyz.yarinlevi.waypoints.Waypoints;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class WaypointManager2 {
    public static Waypoints waypoints;
    public static File waypointFile;
    public static FileConfiguration playerData;

    public static boolean addWaypoint(final Player p, String name, Location location) {
        if(doesWaypointExist(p, name))
            return false;
        playerData.set(p.getUniqueId() + "." + name, location);
        TextComponent message = new TextComponent(Waypoints.lprefix + "You saved a waypoint named: " + ChatColor.AQUA + name);
        message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/wp check " + name));
        message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Check waypoint.").create()));
        p.spigot().sendMessage(message);
        reloadWaypoints();
        return true;
    }

    public static boolean getWaypoint(final Player target, final Player executor, String name) {
        if(target == executor) {
            if (!doesWaypointExist(target, name)) {
                target.sendMessage(Waypoints.lprefix + "Sorry, You do not have a waypoint with that name.");
                return true;
            }
            Object object = playerData.get(target.getUniqueId() + "." + name);
            Location location = (Location) object;
            String message = String.format(waypoints.lprefix + "Contents of waypoint \"" + name + "\": X: %1$s Y: %2$s Z: %3$s", location.getBlockX(), location.getBlockY(), location.getBlockZ());
            target.sendMessage(message);
            return true;
        } else if (target != executor) {
            if(!doesWaypointExist(target, name)) {
                executor.sendMessage(Waypoints.lprefix + "Sorry, that player does not have a waypoint with that name.");
                return true;
            }
            Object object = playerData.get(target.getUniqueId() + "." + name);
            Location location = (Location) object;
            String message = String.format(waypoints.lprefix + "Contents of waypoint \"" + name + "\": X: %1$s Y: %2$s Z: %3$s", location.getBlockX(), location.getBlockY(), location.getBlockZ());
            executor.sendMessage(message);
        }
        return false;
    }

    public static List<String> listWaypointsTabCompleter(final Player p) {
        ArrayList<String> list = new ArrayList<>();
        if(!playerData.contains(p.getUniqueId().toString())) {
            return list;
        }
        for (String string: playerData.getConfigurationSection(p.getUniqueId().toString()).getKeys(false)
             ) {
            list.add(string);
        }
        return list;
    }

    public static void listWaypoints(final Player target, final Player executor) {
        if(!playerData.contains(target.getUniqueId().toString())) {
            return;
        }

        TextComponent message = new TextComponent(Waypoints.lprefix);
        final TextComponent SPACE = new TextComponent(", ");
        Iterator<String> iterator = playerData.getConfigurationSection(target.getUniqueId().toString()).getKeys(false).iterator();
        while(iterator.hasNext()) {
            String string = iterator.next();
            TextComponent waypointText = new TextComponent(string);
            if(target == executor) {
                waypointText.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/wp check " + string));
            } else if (target != executor) {
                waypointText.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/wpadmin check " + string));
            }
            waypointText.setHoverEvent(new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.RESET + "Click to check waypoint \"" + ChatColor.AQUA + string + ChatColor.RESET + "\"" ).create() ));
            message.addExtra(waypointText);
            if(iterator.hasNext()) {
                message.addExtra(SPACE);
            }
        }
        executor.spigot().sendMessage(message);
    }

    public static boolean deleteWaypoint(final Player p, String name) {
        if(!doesWaypointExist(p,name))
            return false;
        playerData.set(p.getUniqueId()+"."+name, (Object)null);
        reloadWaypoints();
        return true;
    }

    public static boolean doesWaypointExist(final Player p, String waypointName) {
        if(playerData.contains(p.getUniqueId() + "." + waypointName))
            return true;
        return false;
    }

    public static void reloadWaypoints() {
        try {
            WaypointManager2.playerData.save(WaypointManager2.waypointFile);
        }
        catch (Exception e1) {
            e1.printStackTrace();
        }
    }
}