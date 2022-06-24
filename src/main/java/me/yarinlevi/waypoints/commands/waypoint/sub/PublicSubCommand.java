package me.yarinlevi.waypoints.commands.waypoint.sub;

import me.yarinlevi.waypoints.commands.SubCommand;
import me.yarinlevi.waypoints.gui.GuiUtils;
import me.yarinlevi.waypoints.utils.Utils;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

/**
 * @author YarinQuapi
 **/
public class PublicSubCommand extends SubCommand {
    @Override
    public void run(Player player, String[] args) {
        player.sendMessage(new Utils.RGBController(15,20,30 ).toColor() + "hello");

        // &<15,15,15>hello

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
