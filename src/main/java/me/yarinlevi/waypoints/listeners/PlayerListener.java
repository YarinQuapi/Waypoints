package me.yarinlevi.waypoints.listeners;

import me.yarinlevi.waypoints.Waypoints;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * @author YarinQuapi
 */
public class PlayerListener implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Bukkit.getScheduler().runTask(Waypoints.getInstance(), () -> Waypoints.getInstance().getPlayerData().loadPlayer(event.getPlayer()));
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        Bukkit.getScheduler().runTask(Waypoints.getInstance(), () -> Waypoints.getInstance().getPlayerData().unloadPlayer(event.getPlayer()));
    }
}