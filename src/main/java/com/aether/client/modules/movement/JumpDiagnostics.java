package com.aether.client.modules.movement;

import com.aether.client.modules.api.Category;
import com.aether.client.modules.api.CompatibilityStatus;
import com.aether.client.modules.api.Module;

/**
 * Jump Diagnostics module - Measures jump apex, hang time, fall distance, and jump mechanics.
 */
public class JumpDiagnostics extends Module {
    private double startY = 0.0;
    private double apexY = 0.0;
    private double lastJumpHeight = 0.0;
    private long jumpStartTime = 0;
    private long lastAirTimeMs = 0;
    private double currentFallDistance = 0.0;
    private int totalJumps = 0;
    private boolean inAir = false;

    public JumpDiagnostics() {
        super("Jump Diagnostics", "Zıplama yüksekliği, havada kalma süresi ve düşüş mesafesini analiz eder.",
                Category.MOVEMENT, CompatibilityStatus.SUPPORTED);
    }

    public void onPlayerJump(double y) {
        this.startY = y;
        this.apexY = y;
        this.jumpStartTime = System.currentTimeMillis();
        this.inAir = true;
        this.totalJumps++;
    }

    public void onPlayerTick(double currentY, boolean onGround, float fallDist) {
        this.currentFallDistance = fallDist;
        if (inAir) {
            if (currentY > apexY) {
                apexY = currentY;
            }
            if (onGround) {
                this.inAir = false;
                this.lastJumpHeight = Math.max(0.0, apexY - startY);
                this.lastAirTimeMs = System.currentTimeMillis() - jumpStartTime;
            }
        }
    }

    public double getLastJumpHeight() { return lastJumpHeight; }
    public long getLastAirTimeMs() { return lastAirTimeMs; }
    public double getCurrentFallDistance() { return currentFallDistance; }
    public int getTotalJumps() { return totalJumps; }
    public boolean isInAir() { return inAir; }
}
