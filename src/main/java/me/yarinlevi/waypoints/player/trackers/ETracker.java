package me.yarinlevi.waypoints.player.trackers;

import lombok.Getter;

public enum ETracker {
    ActionBar("actionbar"),
    Particle("particle"),
    BossBar("bossbar");

    @Getter String key;

    ETracker(String key) {
        this.key = key;
    }
}
