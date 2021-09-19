package me.yarinlevi.waypoints.player.trackers;

import me.yarinlevi.waypoints.Waypoints;
import me.yarinlevi.waypoints.waypoint.Waypoint;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class TrackerManager {
    private final Map<ETracker, Tracker> trackers = new HashMap<>();

    public TrackerManager() {
        if (Waypoints.getInstance().getConfig().getBoolean("trackers.enabled")) {
            if (Waypoints.getInstance().getConfig().getBoolean("trackers.actionbar.enabled")) {
                trackers.put(ETracker.ActionBar, new ActionBarTracker());
            }
        }

        Bukkit.getScheduler().runTaskTimer(Waypoints.getInstance(), () -> trackers.values().forEach(Tracker::update), 10L, 10L);
    }

    public Tracker getTracker(String key) {
        return trackers.get(Arrays.stream(ETracker.values()).filter(x-> x.getKey().equals(key)).findAny().orElse(ETracker.ActionBar));
    }

    public boolean track(Player player, Waypoint waypoint, String tracker) {
        return getTracker(tracker).track(player, waypoint);
    }

    public boolean unTrack(Player player) {
        return trackers.values().stream().filter(x -> x.isTracked(player)).findAny().get().unTrack(player);
    }
}
