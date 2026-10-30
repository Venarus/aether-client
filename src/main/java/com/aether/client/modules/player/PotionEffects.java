package com.aether.client.modules.player;

import com.aether.client.modules.api.Category;
import com.aether.client.modules.api.CompatibilityStatus;
import com.aether.client.modules.api.Module;
import com.aether.client.modules.settings.BooleanSetting;

import java.util.ArrayList;
import java.util.List;

/**
 * Potion Effects module - Displays active status effects, tiers, and duration countdowns.
 */
public class PotionEffects extends Module {
    public static class ActiveEffect {
        public final String name;
        public final int amplifier;
        public final int durationSeconds;
        public final boolean beneficial;

        public ActiveEffect(String name, int amplifier, int durationSeconds, boolean beneficial) {
            this.name = name;
            this.amplifier = amplifier;
            this.durationSeconds = durationSeconds;
            this.beneficial = beneficial;
        }

        public String getFormattedDuration() {
            int minutes = durationSeconds / 60;
            int seconds = durationSeconds % 60;
            return String.format("%02d:%02d", minutes, seconds);
        }

        public String getDisplayName() {
            String tier = amplifier > 0 ? " " + (amplifier + 1) : "";
            return name + tier;
        }
    }

    private final BooleanSetting compact = addSetting(new BooleanSetting("Compact", "Kompakt görünüm modu", false));
    private final List<ActiveEffect> effects = new ArrayList<>();

    public PotionEffects() {
        super("Potion Effects", "Aktif iksir ve durum efektlerini kalan süreleriyle ekranda gösterir.",
                Category.PLAYER, 0, CompatibilityStatus.SUPPORTED, false, true);
    }

    public synchronized void setEffects(List<ActiveEffect> newEffects) {
        this.effects.clear();
        if (newEffects != null) {
            this.effects.addAll(newEffects);
        }
    }

    public synchronized List<ActiveEffect> getEffects() {
        return new ArrayList<>(effects);
    }

    public boolean isCompact() { return compact.getValue(); }
}
