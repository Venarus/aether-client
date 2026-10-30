package com.aether.client.modules.world;

import com.aether.client.modules.api.Category;
import com.aether.client.modules.api.CompatibilityStatus;
import com.aether.client.modules.api.Module;
import com.aether.client.modules.settings.BooleanSetting;
import com.aether.client.modules.settings.NumberSetting;

/**
 * Spawner Finder module - Locates mob spawners in loaded chunks and alerts the player.
 */
public class SpawnerFinder extends Module {
    private final NumberSetting searchRadius = addSetting(new NumberSetting("Radius", "Arama yarıçapı", 48.0, 16.0, 64.0, 8.0, "m"));
    private final BooleanSetting soundAlert = addSetting(new BooleanSetting("Sound Alert", "Bulunduğunda sesli uyarı çal", true));

    public SpawnerFinder() {
        super("Spawner Finder", "Yüklenmiş chunk'larda bulunan mob spawner bloklarını tespit edip uyarır.",
                Category.WORLD, CompatibilityStatus.SUPPORTED);
    }

    public double getSearchRadius() { return searchRadius.getValue(); }
    public boolean isSoundAlert() { return soundAlert.getValue(); }
}
