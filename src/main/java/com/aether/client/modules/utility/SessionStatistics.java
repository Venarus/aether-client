package com.aether.client.modules.utility;

import com.aether.client.modules.api.Category;
import com.aether.client.modules.api.CompatibilityStatus;
import com.aether.client.modules.api.Module;

/**
 * Session Statistics module - Tracks session playtime, distance traveled, jumps, blocks mined/placed, and deaths.
 */
public class SessionStatistics extends Module {
    private final long sessionStartTime = System.currentTimeMillis();
    private double totalDistanceMeters = 0.0;
    private int totalJumps = 0;
    private int blocksBroken = 0;
    private int blocksPlaced = 0;
    private int deaths = 0;
    private int currentFps = 60;

    public SessionStatistics() {
        super("Session Statistics", "Oturum süresi, yürünen mesafe, kırılan bloklar ve ölüm sayısını hesaplar.",
                Category.UTILITY, 0, CompatibilityStatus.SUPPORTED, false, true);
    }

    public void addDistance(double deltaMeters) {
        if (deltaMeters > 0) {
            this.totalDistanceMeters += deltaMeters;
        }
    }

    public void incrementJumps() { this.totalJumps++; }
    public void incrementBlocksBroken() { this.blocksBroken++; }
    public void incrementBlocksPlaced() { this.blocksPlaced++; }
    public void incrementDeaths() { this.deaths++; }
    public void updateFps(int fps) { this.currentFps = fps; }

    public long getSessionDurationSeconds() {
        return (System.currentTimeMillis() - sessionStartTime) / 1000L;
    }

    public String getFormattedSessionTime() {
        long sec = getSessionDurationSeconds();
        long hours = sec / 3600;
        long minutes = (sec % 3600) / 60;
        long seconds = sec % 60;
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    public double getTotalDistanceMeters() { return totalDistanceMeters; }
    public int getTotalJumps() { return totalJumps; }
    public int getBlocksBroken() { return blocksBroken; }
    public int getBlocksPlaced() { return blocksPlaced; }
    public int getDeaths() { return deaths; }
    public int getCurrentFps() { return currentFps; }
}
