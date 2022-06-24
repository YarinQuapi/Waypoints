package me.yarinlevi.waypoints.commands;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * @author YarinQuapi
 **/
public abstract class SubCommand {
    public abstract void run(Player player, String[] args);

    @Nullable
    public abstract String getPermission();

    @Nullable
    public abstract List<String> getAliases();
}
