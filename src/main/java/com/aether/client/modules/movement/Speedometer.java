package com.aether.client.modules.movement;

import com.aether.client.modules.api.Category;
import com.aether.client.modules.api.CompatibilityStatus;
import com.aether.client.modules.api.Module;
import com.aether.client.modules.settings.ModeSetting;

import java.util.LinkedList;

/**
 * Speedometer module - Calculates instantaneous, average, and peak player speed.
 */
public class Speedometer extends Module {
    private final ModeSetting unit = addSetting(new ModeSetting("Unit", "Hız birimi", "m/s", "m/s", "km/h", "bps"));

    private double currentSpeed = 0.0;
    private double peakSpeed = 0.0;
    private final LinkedList<Double> recentSpeeds = new LinkedList<>();

    public Speedometer() {
        super("Speedometer", "Anlık, ortalama ve maksimum hareket hızını gerçek zamanlı hesaplar.",
                Category.MOVEMENT, 0, CompatibilityStatus.SUPPORTED, false, true);
    }

    public void updateSpeed(double deltaX, double deltaZ) {
        // Delta per tick * 20 ticks/sec = blocks per second (m/s)
        double horizontalDist = Math.sqrt(deltaX * deltaX + deltaZ * deltaZ);
        double bps = horizontalDist * 20.0;
        this.currentSpeed = bps;

        if (bps > peakSpeed) {
            peakSpeed = bps;
        }

        recentSpeeds.addLast(bps);
        if (recentSpeeds.size() > 40) { // 2 seconds window
            recentSpeeds.removeFirst();
        }
    }

    public double getAverageSpeed() {
        if (recentSpeeds.isEmpty()) return 0.0;
        double sum = 0.0;
        for (double s : recentSpeeds) sum += s;
        return sum / recentSpeeds.size();
    }

    public double getFormattedSpeed(double rawBps) {
        if (unit.is("km/h")) {
            return rawBps * 3.6;
        }
        return rawBps;
    }

    public String getSpeedDisplay() {
        double val = getFormattedSpeed(currentSpeed);
        return String.format(java.util.Locale.US, "%.2f %s", val, unit.getValue());
    }

    public double getCurrentSpeed() { return currentSpeed; }
    public double getPeakSpeed() { return peakSpeed; }
    public void resetPeak() { this.peakSpeed = 0.0; }
}
