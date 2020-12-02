package xyz.yarinlevi.waypoints.events;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import xyz.yarinlevi.waypoints.data.Data;
import xyz.yarinlevi.waypoints.data.WaypointManager;
import xyz.yarinlevi.waypoints.utils.Utils;

public class Events implements Listener {

    private int min = 1;
    private int max = 100;

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        final Player p = e.getPlayer();

        if(!Data.getPlayerData().contains(p.getUniqueId().toString())) {
            Data.getPlayerData().createSection(p.getUniqueId().toString());
            Data.saveData();
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        Player p = e.getEntity();

        TextComponent msg = new TextComponent(Utils.newMessage("&eYou died. Created waypoint: "));
        TextComponent delete = new TextComponent(Utils.newMessageNoPrefix("&cDELETE"));

        int deathCount = p.getStatistic(Statistic.DEATHS);

        if (WaypointManager.addWaypoint(p, "Death-" + deathCount, true)) {
            TextComponent deathPoint = new TextComponent(Utils.newMessageNoPrefix("&f\"&dDeath-" + deathCount + "&f\" "));

            deathPoint.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(Utils.newMessageNoPrefix(String.format("&eClick to check waypoint &f\"&dDeath-%s&f\"", deathCount))).create()));
            deathPoint.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/wp check Death-" + deathCount));

            delete.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(Utils.newMessageNoPrefix(String.format("&eClick to delete waypoint &f\"&dDeath-%s&f\"", deathCount))).create()));
            delete.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/wp delete Death-" + deathCount));

            msg.addExtra(deathPoint);

        }
        msg.addExtra(delete);
        p.spigot().sendMessage(msg);
    }

    

    /*
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {

        Random rnd = new Random();
        int random = ThreadLocalRandom.current().nextInt(min, max + 1);
        final Player p = event.getEntity().getPlayer();
        WaypointManager2.addWaypoint(event.getEntity().getPlayer(), "Death-" + random, p.getLocation());
        TextComponent deathPoint = new TextComponent("Death-" + random);
        deathPoint.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Click to check waypoint \"" + ChatColor.AQUA + "Death-" + random + ChatColor.RESET + "\"").create()));
        deathPoint.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/"));
        p.sendMessage(Waypoints.getInstance().getPrefix() + ChatColor.YELLOW + "You have died. Waypoint saved as \"" + ChatColor.AQUA + "Death-" + random + ChatColor.YELLOW + "\"");
    }
     */
}
