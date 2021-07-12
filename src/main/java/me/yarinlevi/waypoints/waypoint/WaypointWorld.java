package me.yarinlevi.waypoints.waypoint;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;

/**
 * @author YarinQuapi
 */
public enum WaypointWorld {
    NORMAL("Overworld", Arrays.asList("overworld", "normal")),
    NETHER("The Nether", Arrays.asList("nether", "thenether")),
    THE_END("The End", Arrays.asList("end", "theend"));

    @Getter String name;
    @Getter List<String> keys;

    WaypointWorld(String name, List<String> keys) {
        this.name = name;
        this.keys = keys;
    }
}
