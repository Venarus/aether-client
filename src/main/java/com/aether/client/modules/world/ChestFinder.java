package com.aether.client.modules.world;

import com.aether.client.modules.api.Category;
import com.aether.client.modules.api.CompatibilityStatus;
import com.aether.client.modules.api.Module;
import com.aether.client.modules.settings.NumberSetting;

/**
 * Chest Finder module - Scans loaded chunks for accessible container blocks and calculates distances.
 */
public class ChestFinder extends Module {
    private final NumberSetting searchRadius = addSetting(new NumberSetting("Radius", "Arama yarıçapı", 32.0, 8.0, 64.0, 4.0, "m"));

    public ChestFinder() {
        super("Chest Finder", "Yüklenmiş chunk'lardaki sandıkların konumunu ve mesafesini listeler.",
                Category.WORLD, CompatibilityStatus.SUPPORTED);
    }

    public double getSearchRadius() { return searchRadius.getValue(); }
}
