package me.yarinlevi.waypoints.utils;

import lombok.Getter;
import me.yarinlevi.waypoints.Waypoints;

/**
 * @author YarinQuapi
 */
public class Constants {
    @Getter static final boolean DEATH_POINTS;

    static {
        DEATH_POINTS = Waypoints.getInstance().getConfig().getBoolean("DeathPoints");
    }
}
