package com.aether.client.modules.visuals;

import com.aether.client.events.Render3DEvent;
import com.aether.client.modules.api.Category;
import com.aether.client.modules.api.CompatibilityStatus;
import com.aether.client.modules.api.Module;
import com.aether.client.modules.settings.BooleanSetting;
import com.aether.client.modules.settings.NumberSetting;

/**
 * NameTags module - Renders enhanced nametags with distance, health, and equipment preview.
 */
public class NameTags extends Module {
    private final NumberSetting scale = addSetting(new NumberSetting("Scale", "Yazı boyutu ölçeği", 1.0, 0.5, 2.0, 0.1));
    private final BooleanSetting showHealth = addSetting(new BooleanSetting("Health", "Can miktarını göster", true));
    private final BooleanSetting showDistance = addSetting(new BooleanSetting("Distance", "Mesafeyi göster", true));
    private final BooleanSetting showArmor = addSetting(new BooleanSetting("Armor", "Zırh ve el eşyalarını göster", true));

    public NameTags() {
        super("Name Tags", "Varlıkların isimlerini, canını ve mesafesini duvardan net okunabilir şekilde çizer.",
                Category.VISUALS, CompatibilityStatus.SUPPORTED);
    }

    public double getScale() { return scale.getValue(); }
    public boolean isShowHealth() { return showHealth.getValue(); }
    public boolean isShowDistance() { return showDistance.getValue(); }
    public boolean isShowArmor() { return showArmor.getValue(); }
}
