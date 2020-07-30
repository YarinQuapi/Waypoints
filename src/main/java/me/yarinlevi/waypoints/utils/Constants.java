package me.yarinlevi.waypoints.utils;

import lombok.Getter;
import me.yarinlevi.waypoints.Waypoints;

public class Constants {
    @Getter static boolean DEATH_POINTS;

    public Constants() {
        DEATH_POINTS = Waypoints.getInstance().getConfig().getBoolean("DeathPoints");
    }
}
