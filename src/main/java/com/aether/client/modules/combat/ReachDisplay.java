package com.aether.client.modules.combat;

import com.aether.client.modules.api.Category;
import com.aether.client.modules.api.CompatibilityStatus;
import com.aether.client.modules.api.Module;

/**
 * Reach Display module - Measures and displays exact distance from player eye to attack target.
 */
public class ReachDisplay extends Module {
    private double lastReach = 0.0;
    private double maxReach = 0.0;
    private int attackCount = 0;
    private double totalReach = 0.0;

    public ReachDisplay() {
        super("Reach Display", "Vuruş anında hedef varlıkla olan gerçek mesafeyi ölçer.",
                Category.COMBAT, CompatibilityStatus.SUPPORTED);
    }

    public void registerAttackReach(double distance) {
        this.lastReach = distance;
        if (distance > maxReach) {
            maxReach = distance;
        }
        totalReach += distance;
        attackCount++;
    }

    public double getLastReach() { return lastReach; }
    public double getMaxReach() { return maxReach; }
    public double getAverageReach() {
        return attackCount > 0 ? totalReach / attackCount : 0.0;
    }
}
