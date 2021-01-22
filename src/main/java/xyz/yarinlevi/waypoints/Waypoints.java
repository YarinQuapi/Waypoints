package xyz.yarinlevi.waypoints;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.yarinlevi.waypoints.commands.Administration;
import xyz.yarinlevi.waypoints.commands.MainCommand;
import xyz.yarinlevi.waypoints.gui.GuiHandler;
import xyz.yarinlevi.waypoints.waypoint.PlayerListener;
import xyz.yarinlevi.waypoints.waypoint.WaypointHandler;

public class Waypoints extends JavaPlugin {
    @Getter private static Waypoints instance;
    @Getter private String prefix;
    @Getter private String adminPrefix;
    @Getter private WaypointHandler waypointHandler;

    @Override
    public void onEnable() {
        instance = this;

        this.saveDefaultConfig();
        registerConfigData();

        //New data methods

        Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);
        waypointHandler = new WaypointHandler();


        this.getCommand("wpadmin").setExecutor(new Administration());
        this.getCommand("waypoint").setExecutor(new MainCommand());


        //


        /*
        Data.setWaypointFile(new File(this.getDataFolder(), "Waypoints.yml"));
        Data.setPlayerData((FileConfiguration) YamlConfiguration.loadConfiguration(Data.getWaypointFile()));
        if (!Data.getWaypointFile().exists()) {
            Data.getWaypointFile().getParentFile().mkdirs();
        } else {
            try {
                Data.getPlayerData().load(Data.getWaypointFile());
            }
            catch (InvalidConfigurationException | IOException e) {
                e.printStackTrace();
            }
        }
        Data.saveData();

        this.getCommand("waypoint").setExecutor((CommandExecutor)new MainCommand());
        this.getCommand("wpadmin").setExecutor((CommandExecutor)new Administration());
        Bukkit.getPluginManager().registerEvents(new Events(), this);
        */

        GuiHandler.registerGui().forEach((key, value) -> {
           Bukkit.getPluginManager().registerEvents(value, this);
        });
    }

    @Override
    public void onDisable() {
    }

    public void registerConfigData() {
       prefix = getConfig().getString("Prefix");
       adminPrefix = getConfig().getString("AdminPrefix");
    }
}