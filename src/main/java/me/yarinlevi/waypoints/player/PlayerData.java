package me.yarinlevi.waypoints.player;

import lombok.Getter;
import lombok.Setter;
import me.yarinlevi.waypoints.Waypoints;
import me.yarinlevi.waypoints.player.trackers.ETracker;
import me.yarinlevi.waypoints.waypoint.Waypoint;
import me.yarinlevi.waypoints.waypoint.types.StateIdentifier;
import org.bukkit.OfflinePlayer;

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

    public PlayerData(OfflinePlayer player, List<Waypoint> waypoints) {
        this.player = player;
        this.waypointList.addAll(waypoints);

        Waypoints.getInstance().getPlayerSettingsManager().loadPlayerSettings(this.player.getUniqueId(), this);
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
        return this.getWaypointLimit(StateIdentifier.ALL);
    }

    /**
     * Player must be online for it to return a value!
     * @param state what state limit would you like to check?
     * @return limit for the specified state
     */
    public int getWaypointLimit(StateIdentifier state) {
        switch (state) {
        }

        return 0;
    }
}
