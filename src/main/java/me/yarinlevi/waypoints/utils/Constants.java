package me.yarinlevi.waypoints.utils;

import lombok.Getter;
import me.yarinlevi.waypoints.Waypoints;
import net.md_5.bungee.api.ChatColor;

/**
 * @author YarinQuapi
 */
public class Constants {
    @Getter static final boolean DEATH_POINTS;
    @Getter static final String PREFIX;

    static {
        DEATH_POINTS = Waypoints.getInstance().getConfig().getBoolean("DeathPoints");
        PREFIX = ChatColor.translateAlternateColorCodes('&', Waypoints.getInstance().getConfig().getString("prefix"));
    }
}
