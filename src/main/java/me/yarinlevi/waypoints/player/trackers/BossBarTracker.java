package me.yarinlevi.waypoints.player.trackers;

import me.yarinlevi.waypoints.Waypoints;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BossBarTracker implements Tracker {
    private final Map<Player, BossBar> bossBarConcurrentHashMap = new ConcurrentHashMap<>();
    private final BarColor barColor = Arrays.stream(BarColor.values()).filter(x -> x.name().equalsIgnoreCase(Waypoints.getInstance().getConfig().getString("trackers.bossbar.barcolor"))).findFirst().get();

    @Override
    public void update() {
        trackedPlayers.forEach((player, waypoint) -> {
            if (!player.isOnline()) {
                trackedPlayers.remove(player);
            }

            BossBar bossBar;

            if (bossBarConcurrentHashMap.containsKey(player)) {
                bossBar = bossBarConcurrentHashMap.get(player);
            } else {
                bossBar = Bukkit.createBossBar(waypoint.getName(), barColor, BarStyle.SOLID);
                bossBarConcurrentHashMap.put(player, bossBar);
                bossBar.addPlayer(player);
            }

            bossBar.setTitle("&7• Tracking - &b" + waypoint.getName() + " &7distance - &b" + waypoint.getDistance(player) + " &7•");

            bossBar.setVisible(true);
        });
    }

    @Override
    public ETracker getETracker() {
        return ETracker.BossBar;
    }
}
