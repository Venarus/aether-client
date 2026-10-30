package com.aether.client.modules.visuals;

import com.aether.client.modules.api.Category;
import com.aether.client.modules.api.CompatibilityStatus;
import com.aether.client.modules.api.Module;
import com.aether.client.modules.settings.ModeSetting;
import com.aether.client.modules.settings.NumberSetting;

/**
 * Fullbright module - Overrides client-side gamma / ambient lighting for clear visibility in darkness.
 */
public class Fullbright extends Module {
    private final ModeSetting mode = addSetting(new ModeSetting("Mode", "Aydınlatma modu", "Gamma", "Gamma", "NightVision"));
    private final NumberSetting gammaMultiplier = addSetting(new NumberSetting("Intensity", "Parlaklık çarpanı", 10.0, 1.0, 16.0, 1.0));

    public Fullbright() {
        super("Fullbright", "Karanlık ortamlarda ve mağaralarda tam parlaklık sağlar.",
                Category.VISUALS, CompatibilityStatus.SUPPORTED);
    }

    public ModeSetting getMode() { return mode; }
    public double getIntensity() { return gammaMultiplier.getValue(); }
}
