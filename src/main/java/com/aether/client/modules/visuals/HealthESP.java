package com.aether.client.modules.visuals;

import com.aether.client.events.Render3DEvent;
import com.aether.client.modules.api.Category;
import com.aether.client.modules.api.CompatibilityStatus;
import com.aether.client.modules.api.Module;
import com.aether.client.modules.settings.BooleanSetting;
import com.aether.client.modules.settings.ModeSetting;
import com.aether.client.rendering.ColorUtils;

/**
 * Health ESP module - Displays health bars and exact health numbers above entities.
 */
public class HealthESP extends Module {
    private final BooleanSetting showBar = addSetting(new BooleanSetting("Health Bar", "Can çubuğunu göster", true));
    private final BooleanSetting showNumber = addSetting(new BooleanSetting("Health Number", "Sayısal can miktarını göster", true));
    private final ModeSetting colorMode = addSetting(new ModeSetting("Color Mode", "Renk geçişi", "Dynamic", "Dynamic", "Static"));

    public HealthESP() {
        super("Health ESP", "Hedef varlıkların anlık can miktarını ve can çubuğunu gösterir.",
                Category.VISUALS, CompatibilityStatus.SUPPORTED);
    }

    public int calculateBarColor(float currentHealth, float maxHealth) {
        if (colorMode.is("Dynamic")) {
            return ColorUtils.getHealthColor(currentHealth, maxHealth);
        }
        return 0xFF4CAF50;
    }

    public boolean isShowBar() { return showBar.getValue(); }
    public boolean isShowNumber() { return showNumber.getValue(); }
    public ModeSetting getColorMode() { return colorMode; }
}
