package me.yarinlevi.waypoints.commands.waypoint.sub;

import me.yarinlevi.waypoints.Waypoints;
import me.yarinlevi.waypoints.commands.SubCommand;
import me.yarinlevi.waypoints.utils.Constants;
import me.yarinlevi.waypoints.utils.MessagesUtils;
import me.yarinlevi.waypoints.waypoint.WaypointWorld;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Iterator;

/**
 * @author YarinQuapi
 **/
public class ListSubCommand extends SubCommand {
    @Override
    public void run(Player player, String[] args) {
        if (args.length == 1) {
            if (Waypoints.getInstance().getWaypointHandler().getWaypoints(player).size() > 0) {
                this.list(player, Waypoints.getInstance().getWaypointHandler().getWaypointList(player).iterator());
            } else {
                player.sendMessage(MessagesUtils.getMessage("no_waypoints"));
            }
        } else if (args.length == 2) {
            String world = args[1];

            if (Arrays.stream(WaypointWorld.values()).anyMatch(x -> x.getKeys().contains(world.toLowerCase()))) {
                WaypointWorld waypointWorld = Arrays.stream(WaypointWorld.values()).filter(x -> x.getKeys().contains(world.toLowerCase())).findFirst().get();

                Iterator<String> waypoints = Waypoints.getInstance().getWaypointHandler().getWaypointList(player, waypointWorld).iterator();

                if (waypoints.hasNext()) {
                    this.list(player, waypoints);
                } else {
                    player.sendMessage(MessagesUtils.getMessage("no_waypoint_world", waypointWorld.getName()));
                }
            } else {
                player.sendMessage(MessagesUtils.getMessage("unknown_world"));
            }
        }
    }

    @Override
    public @Nullable String getPermission() {
        return null;
    }

    private void list(Player p, Iterator<String> waypointList) {
        TextComponent message = new TextComponent(ChatColor.translateAlternateColorCodes('&', Constants.PREFIX));
        final TextComponent SPACE = new TextComponent(ChatColor.DARK_GRAY + ", ");
        String waypoint;
        TextComponent waypointText;

        do {
            waypoint = waypointList.next();

            waypointText = new TextComponent(ChatColor.GRAY + waypoint);
            waypointText.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/wp check " + waypoint));
            waypointText.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.GRAY + "Click to check waypoint " + ChatColor.AQUA + waypoint).create()));
            message.addExtra(waypointText);

            if (waypointList.hasNext()) {
                message.addExtra(SPACE);
            }
        } while (waypointList.hasNext());

        p.spigot().sendMessage(message);
    }
}
