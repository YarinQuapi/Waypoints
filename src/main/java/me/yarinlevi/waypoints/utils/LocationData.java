package me.yarinlevi.waypoints.utils;

import lombok.Getter;

/**
 * @author YarinQuapi
 */
public class LocationData {

    @Getter private final String x;
    @Getter private final String y;
    @Getter private final String z;
    @Getter private final String world;
    @Getter private final boolean slimeChunk;

    public LocationData(String x, String y, String z, String world, boolean slimeChunk) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.world = world;

        this.slimeChunk = slimeChunk;
    }
}
