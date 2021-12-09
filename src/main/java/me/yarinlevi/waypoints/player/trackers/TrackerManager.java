package me.yarinlevi.waypoints.player.trackers;

import me.yarinlevi.waypoints.Waypoints;
import me.yarinlevi.waypoints.waypoint.Waypoint;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class TrackerManager {
    private final Map<ETracker, Tracker> trackers = new HashMap<>();

    public TrackerManager() {
        if (Waypoints.getInstance().getConfig().getBoolean("trackers.enabled")) {
            if (Waypoints.getInstance().getConfig().getBoolean("trackers.actionbar.enabled")) {
                if (Bukkit.getPluginManager().isPluginEnabled("ProtocolLib")) {
                    trackers.put(ETracker.ActionBar, new ActionBarTracker());
                } else {
                    Waypoints.getInstance().getLogger().severe("ActionBar tracker is enabled but protocol lib was not found!");
                }
            }
            if (Waypoints.getInstance().getConfig().getBoolean("trackers.particle.enabled")) {
                trackers.put(ETracker.Particle, new ParticleTracker());
            }
            if (Waypoints.getInstance().getConfig().getBoolean("trackers.bossbar.enabled")) {
                trackers.put(ETracker.BossBar, new BossBarTracker());
            }
        }

        Bukkit.getScheduler().runTaskTimer(Waypoints.getInstance(), () -> trackers.values().forEach(Tracker::update), 10L, 10L);
    }

    public Tracker getTracker(String key) {
        return trackers.get(ETracker.getTracker(key));
    }

    public boolean track(Player player, Waypoint waypoint, ETracker tracker) {
        return trackers.get(tracker).track(player, waypoint);
    }

    public boolean unTrack(Player player) {
        return trackers.values().stream().anyMatch(x -> x.isTracked(player)) && trackers.values().stream().filter(x -> x.isTracked(player)).findFirst().get().unTrack(player);
    }
}
