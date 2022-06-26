package me.yarinlevi.waypoints.player;

import lombok.Getter;
import lombok.Setter;
import me.yarinlevi.waypoints.Waypoints;
import me.yarinlevi.waypoints.exceptions.PlayerNotLoadedException;
import me.yarinlevi.waypoints.player.trackers.ETracker;
import me.yarinlevi.waypoints.waypoint.Waypoint;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * @author YarinQuapi
 */
public class PlayerData {
    @Getter private final List<Waypoint> waypointList = new ArrayList<>();
    @Getter private final OfflinePlayer player;

    @Getter @Setter private boolean playerDeathPoints = true;
    @Getter @Setter private ETracker eTracker = Waypoints.getInstance().getTrackerManager().getDefaultTracker();

    @Getter private int limit = 0;

    public PlayerData(OfflinePlayer player, List<Waypoint> waypoints) {
        this.player = player;
        this.waypointList.addAll(waypoints);

        try {
            this.loadLimit();
        } catch (PlayerNotLoadedException ignored) {
            // no need to load limit as the player can't create waypoints if he is not online.
        }

        Waypoints.getInstance().getPlayerSettingsManager().loadPlayerSettings(this.player.getUniqueId(), this);
    }

    /**
     * Loads the waypoint limit for the player that connects. Please note, if granted a new permission, the player needs to reconnect to update his limit.
     * @throws PlayerNotLoadedException if the player is offline.
     */
    public void loadLimit() throws PlayerNotLoadedException {
        Player _player = Bukkit.getPlayer(player.getUniqueId());

        if (_player == null) {
            throw new PlayerNotLoadedException("Player is not online.");
        }

        FileConfiguration config = Waypoints.getInstance().getConfig();

        for (String perm : config.getConfigurationSection("total_limits").getKeys(false)) {
            if (_player.hasPermission(perm.replaceAll("-", "."))) {
                if (limit <= config.getInt("total_limits." + perm)) {
                    limit = config.getInt("total_limits." + perm);
                }
            }
        }
    }

    /**
     * Player must be online!
     * @return total waypoint limit
     */
    public int getWaypointLimit() {
        return this.limit;
    }
}
