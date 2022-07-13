package me.yarinlevi.waypoints.commands.waypoint.sub;

import me.yarinlevi.waypoints.commands.shared.SubCommand;
import me.yarinlevi.waypoints.gui.GuiUtils;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * @author YarinQuapi
 **/
public class PublicSubCommand extends SubCommand {
    @Override
    public void run(Player player, String[] args) {
        GuiUtils.openInventory("gui.public.browser", player);
    }

    @Override
    public @Nullable String getPermission() {
        return null;
    }

    @Override
    public @Nullable List<String> getAliases() {
        return List.of("browser");
    }
}
