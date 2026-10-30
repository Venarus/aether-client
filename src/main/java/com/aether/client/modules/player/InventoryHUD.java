package com.aether.client.modules.player;

import com.aether.client.modules.api.Category;
import com.aether.client.modules.api.CompatibilityStatus;
import com.aether.client.modules.api.Module;
import com.aether.client.modules.settings.BooleanSetting;

/**
 * Inventory HUD module - Renders an on-screen preview of the player's 27-slot inventory and hotbar.
 */
public class InventoryHUD extends Module {
    private final BooleanSetting showDurability = addSetting(new BooleanSetting("Durability", "Dayanıklılık çubuklarını göster", true));
    private final BooleanSetting showCounts = addSetting(new BooleanSetting("Counts", "Eşya adetlerini göster", true));

    public InventoryHUD() {
        super("Inventory HUD", "Oyuncu çantasındaki eşyaları ve hotbarı ekranda canlı olarak gösterir.",
                Category.PLAYER, 0, CompatibilityStatus.SUPPORTED, false, true);
    }

    public boolean isShowDurability() { return showDurability.getValue(); }
    public boolean isShowCounts() { return showCounts.getValue(); }
}
