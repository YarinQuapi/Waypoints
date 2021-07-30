package me.yarinlevi.waypoints.player.trackers;

import me.yarinlevi.waypoints.Waypoints;
import me.yarinlevi.waypoints.utils.Utils;
import me.yarinlevi.waypoints.waypoint.Waypoint;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author YarinQuapi
 */
public class ActionBarTracker {

    // System variables
    private static final int interval = 20;
    private final ConcurrentHashMap<Player, Location> players = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Player, String> bars = new ConcurrentHashMap<>();

    // Display settings
    private static final int range = 70;
    private static final String leftArrow = "<-";
    private static final String rightArrow = "->";
    private static final String section = "⬛";
    private static final int sectionCount = 35;

    public ActionBarTracker() {
        Bukkit.getScheduler().runTaskTimerAsynchronously(Waypoints.getInstance(), () -> {
            List<Player> notFound = new ArrayList<>();
            bars.forEach((player, str) -> {
                if (!players.containsKey(player))
                    notFound.add(player);
            });
            notFound.forEach(bars::remove);
            players.forEach((player, location) -> {
                if (player.getWorld().equals(location.getWorld())) {
                    bars.put(player, generateDirectionIndicator(deltaAngleToTarget(player.getLocation(), location)));
                } else {
                    bars.remove(player);
                }
            });
        }, interval, interval);
    }

    public void update() {
        bars.forEach((x, y) -> {
            if (x.getLocation().distance(players.get(x)) <= 10) {
                players.remove(x);
                x.sendMessage(Utils.newMessage("&7You have reached your destination!"));
            } else {
                x.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(bars.get(x)));
            }
        });
    }

    public boolean track(Player player, Waypoint waypoint) {
        if (players.containsKey(player)) {
            return false;
        } else {
            players.put(player, waypoint.getLocation());
            return true;
        }
    }

    public boolean unTrack(Player player) {
        if (players.containsKey(player)) {
            players.remove(player);
            return true;
        } else return false;
    }

    private String generateDirectionIndicator(double angle) {
        if (angle > range) {
            return "§b" + leftArrow + "§7" + Utils
                    .repeat(section, sectionCount)
                    + rightArrow;
        }
        if (-angle > range) {
            return "§7" + leftArrow + Utils
                    .repeat(section,
                            sectionCount) + "§b"
                    + rightArrow;
        }
        double percent = -(angle / range);
        int nthSection = (int) Math.round(((double) (sectionCount - 1) / 2) * percent);
        nthSection += Math.round((double) sectionCount / 2);
        return "§7" + leftArrow + Utils.repeat(section, nthSection - 1)
                + "§b" + section
                + "§7" + Utils.repeat(section,
                sectionCount - nthSection) + rightArrow;
    }


    /**
     * @param location The location to calculate the angle from
     * @param target   The target when looked at the angle is 0
     * @return The delta angle
     */
    private double deltaAngleToTarget(Location location, Location target) {
        double playerAngle = location.getYaw() + 90;
        while (playerAngle < 0)
            playerAngle += 360;
        double angle = playerAngle - Math.toDegrees(Math.atan2(location.getZ() - target.getZ(), location.getX() - target.getX())) + 180;
        while (angle > 360)
            angle -= 360;
        if (angle > 180)
            angle = -(360 - angle);
        return angle;
    }
}
