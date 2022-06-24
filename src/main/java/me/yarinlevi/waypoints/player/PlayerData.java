package me.yarinlevi.waypoints.player;

import lombok.Getter;
import lombok.Setter;
import me.yarinlevi.waypoints.Waypoints;
import me.yarinlevi.waypoints.exceptions.PlayerNotLoadedException;
import me.yarinlevi.waypoints.player.trackers.ETracker;
import me.yarinlevi.waypoints.waypoint.Waypoint;
import me.yarinlevi.waypoints.waypoint.types.StateIdentifier;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
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
    @Getter @Setter private ETracker eTracker = ETracker.ActionBar;

    @Getter private int limit = 0;

    public PlayerData(OfflinePlayer player, List<Waypoint> waypoints) {
        this.player = player;
        this.waypointList.addAll(waypoints);

        if (player.isOnline()) {
            try {
                this.loadLimit();
            } catch (PlayerNotLoadedException ignored) {
                // this lied. I'm hurt.
            }
        }

        Waypoints.getInstance().getPlayerSettingsManager().loadPlayerSettings(this.player.getUniqueId(), this);
    }

    public void loadLimit() throws PlayerNotLoadedException {
        Player _player = Bukkit.getPlayer(player.getUniqueId());

        if (_player == null) {
            throw new PlayerNotLoadedException("Player not online.");
        }

        FileConfiguration config = Waypoints.getInstance().getConfig();

        for (String perm : config.getStringList("total_limits")) {
            if (_player.hasPermission(perm)) {
                if (limit <= config.getInt("total_limits." + perm)) {
                    limit = config.getInt("total_limits." + perm);
                }
            }
        }
    }

    @Deprecated(forRemoval = true)
    public PlayerData(List<Waypoint> waypoints) {
        this.waypointList.addAll(waypoints);
        this.player = null;
    }

    /**
     * Player must be online!
     * @return total waypoint limit
     */
    public int getWaypointLimit() {
        return this.limit;
    }
}
