package me.yarinlevi.waypoints.commands;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

/**
 * @author YarinQuapi
 **/
public abstract class SubCommand {
    public abstract void run(Player player, String[] args);

    @Nullable
    public abstract String getPermission();
}
