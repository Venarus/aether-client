package com.aether.client.modules.combat;

import com.aether.client.modules.api.Category;
import com.aether.client.modules.api.CompatibilityStatus;
import com.aether.client.modules.api.Module;
import com.aether.client.modules.settings.NumberSetting;

/**
 * Aim Trainer module - Client-side target practice for reaction time and tracking precision.
 */
public class AimTrainer extends Module {
    private final NumberSetting targetDuration = addSetting(new NumberSetting("Target Lifetime", "Hedefin ekranda kalma süresi", 1.5, 0.5, 4.0, 0.25, "s"));

    private int hits = 0;
    private int misses = 0;
    private long lastSpawnTime = 0;
    private double currentTargetX = 0.5;
    private double currentTargetY = 0.5;
    private long totalReactionTimeMs = 0;

    public AimTrainer() {
        super("Aim Trainer", "Yerel hedef antrenmanı ile refleks ve nişan alma hassasiyetini ölçer.",
                Category.COMBAT, CompatibilityStatus.LOCAL_ONLY);
    }

    @Override
    protected void onEnable() {
        resetStats();
        spawnTarget();
    }

    public void resetStats() {
        this.hits = 0;
        this.misses = 0;
        this.totalReactionTimeMs = 0;
    }

    public void spawnTarget() {
        this.currentTargetX = 0.2 + Math.random() * 0.6;
        this.currentTargetY = 0.2 + Math.random() * 0.6;
        this.lastSpawnTime = System.currentTimeMillis();
    }

    public void registerClick(double mouseX, double mouseY, double targetRadius) {
        double distSq = (mouseX - currentTargetX) * (mouseX - currentTargetX) + (mouseY - currentTargetY) * (mouseY - currentTargetY);
        if (distSq <= targetRadius * targetRadius) {
            hits++;
            totalReactionTimeMs += (System.currentTimeMillis() - lastSpawnTime);
            spawnTarget();
        } else {
            misses++;
        }
    }

    public double getAccuracy() {
        int total = hits + misses;
        return total > 0 ? ((double) hits / total) * 100.0 : 100.0;
    }

    public double getAverageReactionTimeMs() {
        return hits > 0 ? (double) totalReactionTimeMs / hits : 0.0;
    }

    public int getHits() { return hits; }
    public int getMisses() { return misses; }
}
