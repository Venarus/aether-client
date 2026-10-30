package com.aether.client.modules.movement;

import com.aether.client.modules.api.Category;
import com.aether.client.modules.api.CompatibilityStatus;
import com.aether.client.modules.api.Module;
import com.aether.client.modules.settings.BooleanSetting;

/**
 * Motion Tracker module - Tracks 3D velocity vectors (X, Y, Z) and motion dynamics.
 */
public class MotionTracker extends Module {
    private final BooleanSetting showGraph = addSetting(new BooleanSetting("Show Graph", "Hız değişim grafiği göster", true));

    private double motionX;
    private double motionY;
    private double motionZ;
    private double acceleration;
    private double previousSpeed;

    public MotionTracker() {
        super("Motion Tracker", "3B hareket vektörlerini (X/Y/Z) ve ivmelenme durumunu izler.",
                Category.MOVEMENT, CompatibilityStatus.SUPPORTED);
    }

    public void updateMotion(double mx, double my, double mz) {
        this.motionX = mx;
        this.motionY = my;
        this.motionZ = mz;

        double currentSpeed = Math.sqrt(mx * mx + my * my + mz * mz);
        this.acceleration = (currentSpeed - previousSpeed) * 20.0;
        this.previousSpeed = currentSpeed;
    }

    public double getMotionX() { return motionX; }
    public double getMotionY() { return motionY; }
    public double getMotionZ() { return motionZ; }
    public double getAcceleration() { return acceleration; }
    public boolean isShowGraph() { return showGraph.getValue(); }
}
