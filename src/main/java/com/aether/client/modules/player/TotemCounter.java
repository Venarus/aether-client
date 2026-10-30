package com.aether.client.modules.player;

import com.aether.client.modules.api.Category;
import com.aether.client.modules.api.CompatibilityStatus;
import com.aether.client.modules.api.Module;
import com.aether.client.modules.settings.BooleanSetting;

/**
 * Totem Counter module - Tracks the total number of Totems of Undying held across all inventory slots.
 */
public class TotemCounter extends Module {
    private final BooleanSetting popAnimation = addSetting(new BooleanSetting("Pop Animation", "Totem patladığında animasyon oynat", true));

    private int totemCount = 0;
    private int totalPops = 0;

    public TotemCounter() {
        super("Totem Counter", "Envanter ve ellerdeki toplam Ölümsüzlük Totemi sayısını ve patlama sayısını sayar.",
                Category.PLAYER, 0, CompatibilityStatus.SUPPORTED, false, true);
    }

    public void updateCount(int count) {
        if (count < this.totemCount && this.totemCount > 0) {
            totalPops++;
        }
        this.totemCount = count;
    }

    public int getTotemCount() { return totemCount; }
    public int getTotalPops() { return totalPops; }
    public boolean isPopAnimation() { return popAnimation.getValue(); }
}
