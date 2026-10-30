package com.aether.client.modules.movement;

import com.aether.client.modules.api.Category;
import com.aether.client.modules.api.CompatibilityStatus;
import com.aether.client.modules.api.Module;
import com.aether.client.modules.settings.BooleanSetting;

/**
 * Elytra HUD module - Displays flight metrics, speed, glide angle, altitude, and durability.
 */
public class ElytraHUD extends Module {
    private final BooleanSetting showDurability = addSetting(new BooleanSetting("Durability", "Kalan dayanıklılığı göster", true));
    private final BooleanSetting showPitch = addSetting(new BooleanSetting("Pitch/Yaw", "Uçuş açısını göster", true));

    private double flightSpeed = 0.0;
    private double altitude = 0.0;
    private float pitch = 0.0f;
    private int elytraDurability = 432;
    private int maxDurability = 432;

    public ElytraHUD() {
        super("Elytra HUD", "Elytra ile uçuş sırasında hız, irtifa, süzülme açısı ve dayanıklılığı gösterir.",
                Category.MOVEMENT, CompatibilityStatus.SUPPORTED);
    }

    public void updateFlightData(double speed, double y, float p, int durability, int maxD) {
        this.flightSpeed = speed;
        this.altitude = y;
        this.pitch = p;
        this.elytraDurability = durability;
        this.maxDurability = maxD;
    }

    public double getFlightSpeed() { return flightSpeed; }
    public double getAltitude() { return altitude; }
    public float getPitch() { return pitch; }
    public int getElytraDurability() { return elytraDurability; }
    public int getDurabilityPercent() {
        if (maxDurability <= 0) return 100;
        return (int) ((elytraDurability / (float) maxDurability) * 100);
    }
    public boolean isShowDurability() { return showDurability.getValue(); }
    public boolean isShowPitch() { return showPitch.getValue(); }
}
