package com.aether.client.modules.world;

import com.aether.client.modules.api.Category;
import com.aether.client.modules.api.CompatibilityStatus;
import com.aether.client.modules.api.Module;
import com.aether.client.modules.settings.NumberSetting;

import java.util.ArrayList;
import java.util.List;

/**
 * Cave Mapper module - Tracks player trail and breadcrumbs through underground cavities.
 */
public class CaveMapper extends Module {
    public static class Breadcrumb {
        public final double x, y, z;
        public final long timestamp;

        public Breadcrumb(double x, double y, double z) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.timestamp = System.currentTimeMillis();
        }
    }

    private final NumberSetting maxTrailPoints = addSetting(new NumberSetting("Max Points", "Maksimum iz noktası", 250, 50, 1000, 50));
    private final List<Breadcrumb> trail = new ArrayList<>();
    private double lastRecordedX, lastRecordedY, lastRecordedZ;

    public CaveMapper() {
        super("Cave Mapper", "Keşfedilen mağara koridorlarını ayak izi breadcrumb sistemiyle haritalar.",
                Category.WORLD, CompatibilityStatus.SUPPORTED);
    }

    public synchronized void recordPosition(double x, double y, double z) {
        double distSq = (x - lastRecordedX) * (x - lastRecordedX) + (y - lastRecordedY) * (y - lastRecordedY) + (z - lastRecordedZ) * (z - lastRecordedZ);
        if (distSq >= 4.0) { // Every 2 blocks of movement
            trail.add(new Breadcrumb(x, y, z));
            lastRecordedX = x;
            lastRecordedY = y;
            lastRecordedZ = z;

            int max = maxTrailPoints.getValueInt();
            while (trail.size() > max) {
                trail.remove(0);
            }
        }
    }

    public synchronized List<Breadcrumb> getTrail() {
        return new ArrayList<>(trail);
    }

    public synchronized void clearTrail() {
        trail.clear();
    }
}
