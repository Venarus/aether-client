package com.aether.client.modules.player;

import com.aether.client.modules.api.Category;
import com.aether.client.modules.api.CompatibilityStatus;
import com.aether.client.modules.api.Module;
import com.aether.client.modules.settings.BooleanSetting;

/**
 * Armor Status module - Displays equipped armor pieces, held items, remaining durability, and low-durability warnings.
 */
public class ArmorStatus extends Module {
    public static class ArmorPiece {
        public final String name;
        public final int durability;
        public final int maxDurability;

        public ArmorPiece(String name, int durability, int maxDurability) {
            this.name = name;
            this.durability = durability;
            this.maxDurability = maxDurability;
        }

        public int getPercentage() {
            if (maxDurability <= 0) return 100;
            return (int) ((durability / (float) maxDurability) * 100);
        }

        public boolean isLow() {
            return getPercentage() < 20;
        }
    }

    private final BooleanSetting showPercentage = addSetting(new BooleanSetting("Percentage", "Yüzde olarak göster", true));
    private final BooleanSetting showWarning = addSetting(new BooleanSetting("Warning", "Kırılma eşiğinde uyarı ver", true));

    public ArmorStatus() {
        super("Armor Status", "Kuşanılmış zırhların dayanıklılık oranlarını ve kırılma uyarılarını gösterir.",
                Category.PLAYER, 0, CompatibilityStatus.SUPPORTED, false, true);
    }

    public boolean isShowPercentage() { return showPercentage.getValue(); }
    public boolean isShowWarning() { return showWarning.getValue(); }
}
