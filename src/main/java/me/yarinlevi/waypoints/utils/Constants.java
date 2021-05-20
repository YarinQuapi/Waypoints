package me.yarinlevi.waypoints.utils;

import lombok.Getter;
import me.yarinlevi.waypoints.Waypoints;

/**
 * @author YarinQuapi
 */
public class Constants {
    @Getter static boolean DEATH_POINTS;

    public Constants() {
        DEATH_POINTS = Waypoints.getInstance().getConfig().getBoolean("DeathPoints");
    }
}
