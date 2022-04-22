package me.yarinlevi.waypoints.player.trackers;

import me.yarinlevi.waypoints.Waypoints;
import me.yarinlevi.waypoints.utils.Utils;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author YarinQuapi
 */
public class ActionBarTracker extends Tracker {

    // System settings
    private static final int interval = Waypoints.getInstance().getConfig().getInt("trackers.actionbar.interval");
    private final ConcurrentMap<Player, String> bars = new ConcurrentHashMap<>();

    // Display settings
    private static final int range = 70;
    private static final String leftArrow = Waypoints.getInstance().getConfig().getString("trackers.actionbar.leftarrow");
    private static final String rightArrow = Waypoints.getInstance().getConfig().getString("trackers.actionbar.rightarrow");
    private static final String block = "⬛";
    private static final int blockCount = 25;
    private static final String indicatorColor = Waypoints.getInstance().getConfig().getString("trackers.actionbar.indicatorcolor");

    public ActionBarTracker() {
        Bukkit.getScheduler().runTaskTimerAsynchronously(Waypoints.getInstance(), () -> {
            List<Player> notFound = new ArrayList<>();
            bars.forEach((player, str) -> {
                if (!trackedPlayers.containsKey(player))
                    notFound.add(player);
            });

            notFound.forEach(bars::remove);

            trackedPlayers.forEach((player, location) -> {
                if (player.getWorld().equals(location.getLocation().getWorld())) {
                    bars.put(player, generateDirectionIndicator(deltaAngleToTarget(player.getLocation(), location.getLocation())));
                } else {
                    bars.remove(player);
                }
            });
        }, interval, interval);
    }

    @Override
    protected void update() {
        bars.forEach((x, y) -> {
            if (trackedPlayers.get(x).getDistance(x) <= 10) {
                trackedPlayers.remove(x);
                x.sendMessage(Utils.newMessage("&7You have reached your destination!"));
            } else {
                x.spigot().sendMessage(ChatMessageType.ACTION_BAR, null, new TextComponent(bars.get(x)));
            }
        });
    }

    @Override
    public ETracker getETracker() {
        return ETracker.ActionBar;
    }

    private @NotNull String generateDirectionIndicator(double angle) {
        if (angle > range) {
            return indicatorColor + leftArrow + indicatorColor + Utils.repeat(block, blockCount) + rightArrow;
        }

        if (-angle > range) {
            return indicatorColor + leftArrow + Utils.repeat(block, blockCount) + indicatorColor + rightArrow;
        }

        double percent = -(angle / range);
        int nthSection = (int) Math.round(((double) (blockCount - 1) / 2) * percent);
        nthSection += Math.round((double) blockCount / 2);

        return indicatorColor + leftArrow +
                Utils.repeat(block, nthSection - 1)
                + indicatorColor + block + indicatorColor
                + Utils.repeat(block, blockCount - nthSection) + rightArrow;
    }

    
    private double deltaAngleToTarget(@NotNull Location location, Location target) {
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
