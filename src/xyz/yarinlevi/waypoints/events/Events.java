package xyz.yarinlevi.waypoints.events;

import xyz.yarinlevi.waypoints.Waypoints;
import xyz.yarinlevi.waypoints.data.WaypointManager2;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Events implements Listener {

    public Waypoints instance;
    public int min = 1;
    public int max = 100;

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {

    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Random rnd = new Random();
        int random = ThreadLocalRandom.current().nextInt(min, max + 1);
        final Player p = event.getEntity().getPlayer();
        WaypointManager2.addWaypoint(event.getEntity().getPlayer(), "Death-" + random, p.getLocation());
        TextComponent deathPoint = new TextComponent("Death-" + random);
        deathPoint.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Click to check waypoint \"" + ChatColor.AQUA + "Death-" + random + ChatColor.RESET + "\"").create()));
        deathPoint.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/"));
        p.sendMessage(instance.lprefix + ChatColor.YELLOW + "You have died. Waypoint saved as \"" + ChatColor.AQUA + "Death-" + random + ChatColor.YELLOW + "\"");
    }
}
