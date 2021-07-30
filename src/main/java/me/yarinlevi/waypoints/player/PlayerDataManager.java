package me.yarinlevi.waypoints.player;

import lombok.Getter;
import me.yarinlevi.waypoints.waypoint.Waypoint;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class PlayerDataManager {
    @Getter private final Map<UUID, PlayerData> playerDataMap = new HashMap<>();


    public boolean insertPlayer(UUID uuid, List<Waypoint> waypoints) {
        if (!this.playerDataMap.containsKey(uuid)) {
            this.playerDataMap.put(uuid, new PlayerData(waypoints));
            return true;
        } else {
            return false;
        }
    }

    public boolean removePlayer(UUID uuid) {
        if (this.playerDataMap.containsKey(uuid)) {
            this.playerDataMap.remove(uuid);
            return true;
        } else {
            return false;
        }
    }
}
