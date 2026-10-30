package com.aether.client.modules.player;

import com.aether.client.keybinds.KeybindManager;
import com.aether.client.modules.api.Category;
import com.aether.client.modules.api.CompatibilityStatus;
import com.aether.client.modules.api.Module;
import com.aether.client.modules.settings.BooleanSetting;

/**
 * Coordinates module - Displays detailed player coordinates, biome, facing direction, and Nether conversion.
 */
public class Coordinates extends Module {
    private final BooleanSetting showNether = addSetting(new BooleanSetting("Nether Coords", "Nether karşılığını göster", true));
    private final BooleanSetting showBiome = addSetting(new BooleanSetting("Biome", "Biyom bilgisini göster", true));
    private final BooleanSetting showFacing = addSetting(new BooleanSetting("Facing", "Baktığı yönü göster", true));

    private double x, y, z;
    private String biome = "Plains";
    private String facing = "North";
    private float yaw = 0.0f;
    private float pitch = 0.0f;
    private String dimension = "minecraft:overworld";

    public Coordinates() {
        super("Coordinates", "XYZ koordinatlarını, biyomu, bakılan yönü ve Nether karşılığını gösterir.",
                Category.PLAYER, KeybindManager.KEY_F8, CompatibilityStatus.SUPPORTED, false, true);
    }

    public void update(double x, double y, double z, String biome, float yaw, float pitch, String dimension) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.biome = biome != null ? biome : "Unknown";
        this.yaw = yaw;
        this.pitch = pitch;
        this.dimension = dimension != null ? dimension : "minecraft:overworld";
        this.facing = calculateFacing(yaw);
    }

    public static String calculateFacing(float yaw) {
        float normalized = (yaw % 360 + 360) % 360;
        if (normalized >= 315 || normalized < 45) {
            return "South (+Z)";
        } else if (normalized >= 45 && normalized < 135) {
            return "West (-X)";
        } else if (normalized >= 135 && normalized < 225) {
            return "North (-Z)";
        } else {
            return "East (+X)";
        }
    }

    public String getFormattedOverworld() {
        return String.format(java.util.Locale.US, "XYZ: %.1f / %.1f / %.1f", x, y, z);
    }

    public String getFormattedNether() {
        if (dimension.contains("nether")) {
            return String.format(java.util.Locale.US, "OW: %.1f / %.1f / %.1f", x * 8.0, y, z * 8.0);
        } else {
            return String.format(java.util.Locale.US, "Nether: %.1f / %.1f / %.1f", x / 8.0, y, z / 8.0);
        }
    }

    public String getCopyString() {
        return String.format(java.util.Locale.US, "%.1f, %.1f, %.1f", x, y, z);
    }

    public double getX() { return x; }
    public double getY() { return y; }
    public double getZ() { return z; }
    public String getBiome() { return biome; }
    public String getFacing() { return facing; }
    public boolean isShowNether() { return showNether.getValue(); }
    public boolean isShowBiome() { return showBiome.getValue(); }
    public boolean isShowFacing() { return showFacing.getValue(); }
}
