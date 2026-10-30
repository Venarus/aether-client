package com.aether.client.modules.combat;

import com.aether.client.modules.api.Category;
import com.aether.client.modules.api.CompatibilityStatus;
import com.aether.client.modules.api.Module;
import com.aether.client.modules.settings.BooleanSetting;

import java.util.ArrayList;
import java.util.List;

/**
 * CPS Counter module - High-precision clicks-per-second tracker for left and right mouse buttons.
 */
public class CPSCounter extends Module {
    private final BooleanSetting showRightClick = addSetting(new BooleanSetting("Right Click", "Sağ tık CPS değerini göster", true));

    private final List<Long> leftClicks = new ArrayList<>();
    private final List<Long> rightClicks = new ArrayList<>();
    private int peakLeftCPS = 0;
    private int peakRightCPS = 0;

    public CPSCounter() {
        super("CPS Counter", "Sol ve sağ tıklama hızını (CPS) gerçek zamanlı ölçer.",
                Category.COMBAT, 0, CompatibilityStatus.SUPPORTED, false, true);
    }

    public synchronized void registerLeftClick() {
        leftClicks.add(System.currentTimeMillis());
        int current = getLeftCPS();
        if (current > peakLeftCPS) {
            peakLeftCPS = current;
        }
    }

    public synchronized void registerRightClick() {
        rightClicks.add(System.currentTimeMillis());
        int current = getRightCPS();
        if (current > peakRightCPS) {
            peakRightCPS = current;
        }
    }

    public synchronized int getLeftCPS() {
        long threshold = System.currentTimeMillis() - 1000L;
        leftClicks.removeIf(t -> t < threshold);
        return leftClicks.size();
    }

    public synchronized int getRightCPS() {
        long threshold = System.currentTimeMillis() - 1000L;
        rightClicks.removeIf(t -> t < threshold);
        return rightClicks.size();
    }

    public int getPeakLeftCPS() { return peakLeftCPS; }
    public int getPeakRightCPS() { return peakRightCPS; }
    public boolean isShowRightClick() { return showRightClick.getValue(); }
}
