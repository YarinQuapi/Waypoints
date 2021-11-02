package me.yarinlevi.waypoints.player.trackers;

import me.yarinlevi.waypoints.Waypoints;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.util.Vector;

import java.util.Arrays;

/**
 * @author YarinQuapi
 */
public class ParticleTracker implements Tracker {
    private final int length = Waypoints.getInstance().getConfig().getInt("trackers.particle.length");
    private final int amount = Waypoints.getInstance().getConfig().getInt("trackers.particle.amount");
    private final int heightOffset = Waypoints.getInstance().getConfig().getInt("trackers.particle.heightOffset");
    private final boolean verticalDirection = Waypoints.getInstance().getConfig().getBoolean("trackers.particle.verticalDirection");
    private final Particle particle = Arrays.stream(Particle.values()).filter(x -> x.name().equalsIgnoreCase(Waypoints.getInstance().getConfig().getString("trackers.particle.particle"))).findAny().get();

    @Override
    public void update() {
        trackedPlayers.forEach((player, location) -> {
            if (player.getWorld() == location.getWorld()) {
                Location playerLocation = player.getLocation();
                Vector dir = location.toVector().subtract(playerLocation.toVector()).normalize().multiply(length);

                dir.divide(new Vector(amount, 1, amount));

                for (int i = 0; i < amount; i++) {
                    int y = heightOffset;

                    if (verticalDirection) {
                        y += dir.getY() * i;
                    }

                    player.spawnParticle(
                            particle,
                            playerLocation.getX() + (dir.getX() * i),
                            playerLocation.getY() + y,
                            playerLocation.getZ() + (dir.getZ() * i),
                            1, 0.0, 0.0, 0.0, 0.0);
                }
            }
        });
    }

    @Override
    public ETracker getETracker() {
        return ETracker.Particle;
    }
}
