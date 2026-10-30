package com.aether.client.modules.movement;

import com.aether.client.core.AetherClient;
import com.aether.client.modules.api.Category;
import com.aether.client.modules.api.CompatibilityStatus;
import com.aether.client.modules.api.Module;
import com.aether.client.modules.settings.ModeSetting;
import com.aether.client.modules.settings.NumberSetting;

/**
 * Fly module - Creative flight controller and local testing flight simulator.
 * Strictly respects server permissions and does not bypass server movement validation.
 */
public class Fly extends Module {
    private final ModeSetting mode = addSetting(new ModeSetting("Mode", "Uçuş modu", "Creative", "Creative", "LocalSimulation"));
    private final NumberSetting speed = addSetting(new NumberSetting("Speed", "Uçuş hız çarpanı", 1.5, 0.5, 5.0, 0.25, "x"));

    private boolean flightActive = false;
    private String permissionWarning = null;

    public Fly() {
        super("Fly", "Creative uçuş kontrolü ve yerel test uçuş simülatörü.",
                Category.MOVEMENT, 0, CompatibilityStatus.REQUIRES_SERVER_PERMISSION, true, false);
    }

    @Override
    protected void onEnable() {
        boolean canFly = false;
        if (AetherClient.getInstance() != null && AetherClient.getInstance().getPermissionDashboard() != null) {
            canFly = AetherClient.getInstance().getPermissionDashboard().isFlightAllowed();
        }

        if (mode.is("Creative") && !canFly) {
            this.permissionWarning = "Sunucuda uçuş izni yok! (Creative modu veya /fly yetkisi gerekli)";
            this.flightActive = false;
        } else {
            this.permissionWarning = null;
            this.flightActive = true;
        }
    }

    @Override
    protected void onDisable() {
        this.flightActive = false;
        this.permissionWarning = null;
    }

    public boolean isFlightActive() {
        return flightActive;
    }

    public String getPermissionWarning() {
        return permissionWarning;
    }

    public double getSpeed() {
        return speed.getValue();
    }

    public ModeSetting getMode() {
        return mode;
    }
}
