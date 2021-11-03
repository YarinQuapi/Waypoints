package me.yarinlevi.waypoints.player.trackers;

import me.yarinlevi.waypoints.Waypoints;
import me.yarinlevi.waypoints.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class BossBarTracker extends Tracker {
    private final ConcurrentMap<Player, BossBar> bossBarConcurrentHashMap = new ConcurrentHashMap<>();
    private final BarColor barColor = Arrays.stream(BarColor.values()).filter(x -> x.name().equalsIgnoreCase(Waypoints.getInstance().getConfig().getString("trackers.bossbar.barcolor"))).findFirst().get();

    @Override
    public void update() {
        trackedPlayers.forEach((player, waypoint) -> {
            if (player.getWorld() == waypoint.getLocation().getWorld()) {
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
                    bossBar.setVisible(true);
                }

                bossBar.setTitle(Utils.newMessageNoPrefix("&7• Tracking - &b" + waypoint.getName() + " &7distance - &b" + waypoint.getDistance(player) + " &7•"));


                if (waypoint.getDistance(player) <= 10) {
                    this.unTrack(player);
                }
            }
        });
    }

    @Override
    public ETracker getETracker() {
        return ETracker.BossBar;
    }

    @Override
    public boolean unTrack(Player player) {
        if (trackedPlayers.containsKey(player)) {
            trackedPlayers.remove(player);

            bossBarConcurrentHashMap.get(player).removePlayer(player);
            bossBarConcurrentHashMap.remove(player);

            return true;
        } else return false;
    }
}
