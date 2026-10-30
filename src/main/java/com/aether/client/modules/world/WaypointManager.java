package com.aether.client.modules.world;

import com.aether.client.modules.api.Category;
import com.aether.client.modules.api.CompatibilityStatus;
import com.aether.client.modules.api.Module;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Waypoint Manager module - Complete 3D waypoint creation, color tagging, distance calculation, and JSON persistence.
 */
public class WaypointManager extends Module {
    public static class Waypoint {
        public String name;
        public double x, y, z;
        public String dimension;
        public int color;
        public boolean visible;

        public Waypoint(String name, double x, double y, double z, String dimension, int color) {
            this.name = name;
            this.x = x;
            this.y = y;
            this.z = z;
            this.dimension = dimension != null ? dimension : "overworld";
            this.color = color;
            this.visible = true;
        }

        public double distanceTo(double px, double py, double pz) {
            double dx = x - px;
            double dy = y - py;
            double dz = z - pz;
            return Math.sqrt(dx * dx + dy * dy + dz * dz);
        }
    }

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private final List<Waypoint> waypoints = new ArrayList<>();

    public WaypointManager() {
        super("Waypoint Manager", "Özel 3B dünya işaretleri oluşturur, düzenler ve mesafelerini gösterir.",
                Category.WORLD, 0, CompatibilityStatus.SUPPORTED, false, true);
    }

    public synchronized void addWaypoint(String name, double x, double y, double z, String dimension, int color) {
        waypoints.add(new Waypoint(name, x, y, z, dimension, color));
    }

    public synchronized boolean removeWaypoint(String name) {
        return waypoints.removeIf(w -> w.name.equalsIgnoreCase(name));
    }

    public synchronized List<Waypoint> getWaypoints() {
        return Collections.unmodifiableList(new ArrayList<>(waypoints));
    }

    public synchronized List<Waypoint> getWaypointsForDimension(String dimension) {
        List<Waypoint> filtered = new ArrayList<>();
        for (Waypoint w : waypoints) {
            if (w.visible && (w.dimension.equalsIgnoreCase(dimension) || dimension == null)) {
                filtered.add(w);
            }
        }
        return filtered;
    }

    public synchronized void exportToJson(File file) {
        try (FileWriter writer = new FileWriter(file, StandardCharsets.UTF_8)) {
            GSON.toJson(waypoints, writer);
        } catch (Exception e) {
            System.err.println("[Aether Waypoints] Export error: " + e.getMessage());
        }
    }

    public synchronized void importFromJson(File file) {
        if (!file.exists()) return;
        try (FileReader reader = new FileReader(file, StandardCharsets.UTF_8)) {
            List<Waypoint> loaded = GSON.fromJson(reader, new TypeToken<List<Waypoint>>(){}.getType());
            if (loaded != null) {
                waypoints.clear();
                waypoints.addAll(loaded);
            }
        } catch (Exception e) {
            System.err.println("[Aether Waypoints] Import error: " + e.getMessage());
        }
    }
}
