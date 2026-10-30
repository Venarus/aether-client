package com.aether.client.modules.visuals;

import com.aether.client.events.Render3DEvent;
import com.aether.client.modules.api.Category;
import com.aether.client.modules.api.CompatibilityStatus;
import com.aether.client.modules.api.Module;
import com.aether.client.modules.settings.BooleanSetting;
import com.aether.client.modules.settings.ColorSetting;
import com.aether.client.modules.settings.NumberSetting;

/**
 * Storage ESP module - Highlights chests, ender chests, shulker boxes, and barrels.
 */
public class StorageESP extends Module {
    private final BooleanSetting chests = addSetting(new BooleanSetting("Chests", "Normal sandıkları vurgula", true));
    private final BooleanSetting enderChests = addSetting(new BooleanSetting("Ender Chests", "Ender sandıklarını vurgula", true));
    private final BooleanSetting shulkers = addSetting(new BooleanSetting("Shulkers", "Shulker kutularını vurgula", true));
    private final BooleanSetting barrels = addSetting(new BooleanSetting("Barrels", "Fıçıları vurgula", true));
    private final NumberSetting maxDistance = addSetting(new NumberSetting("Distance", "Maksimum arama mesafesi", 32.0, 8.0, 64.0, 4.0, "m"));
    private final ColorSetting chestColor = addSetting(new ColorSetting("Chest Color", "Sandık kutu rengi", 0xFFFF9800));
    private final ColorSetting enderColor = addSetting(new ColorSetting("Ender Color", "Ender sandık rengi", 0xFF9C27B0));

    public StorageESP() {
        super("Storage ESP", "Sandık ve depolama bloklarını renkli 3D kutularla işaretler.",
                Category.VISUALS, CompatibilityStatus.SUPPORTED);
    }

    @Override
    protected void render3D(Render3DEvent event) {
        // Renders bounding highlights on loaded container block entities
    }

    public boolean isChests() { return chests.getValue(); }
    public boolean isEnderChests() { return enderChests.getValue(); }
    public boolean isShulkers() { return shulkers.getValue(); }
    public boolean isBarrels() { return barrels.getValue(); }
    public double getMaxDistance() { return maxDistance.getValue(); }
    public int getChestColor() { return chestColor.getColor(); }
    public int getEnderColor() { return enderColor.getColor(); }
}
