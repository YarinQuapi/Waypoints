package me.yarinlevi.waypoints.waypoint;

import lombok.Getter;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * @author YarinQuapi
 */
public enum WaypointWorld {
    ALL("All", List.of()),
    NORMAL("Overworld", List.of("overworld", "normal")),
    NETHER("The Nether", List.of("nether", "thenether")),
    THE_END("The End", List.of("end", "theend"));

    @Getter String name;
    @Nullable @Getter List<String> keys;

    WaypointWorld(String name, @Nullable List<String> keys) {
        this.name = name;
        this.keys = keys;
    }
}
