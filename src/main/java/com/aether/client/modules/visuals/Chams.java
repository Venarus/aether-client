package com.aether.client.modules.visuals;

import com.aether.client.modules.api.Category;
import com.aether.client.modules.api.CompatibilityStatus;
import com.aether.client.modules.api.Module;
import com.aether.client.modules.settings.BooleanSetting;
import com.aether.client.modules.settings.ColorSetting;
import com.aether.client.modules.settings.NumberSetting;

/**
 * Chams module - Renders custom silhouette and depth-layer effects on entity models.
 */
public class Chams extends Module {
    private final ColorSetting chamsColor = addSetting(new ColorSetting("Color", "Model silüet rengi", 0x99FF007F));
    private final NumberSetting opacity = addSetting(new NumberSetting("Opacity", "Saydamlık", 0.6, 0.1, 1.0, 0.1));
    private final BooleanSetting renderThroughWalls = addSetting(new BooleanSetting("Through Walls", "Duvar arkasında göster", true));
    private final BooleanSetting pulse = addSetting(new BooleanSetting("Pulse", "Nefes alma animasyonu", false));

    public Chams() {
        super("Chams", "İzinli test ortamında varlık modellerini silüet ve saydamlık renkleriyle çizer.",
                Category.VISUALS, CompatibilityStatus.SUPPORTED);
    }

    public int getEffectiveColor() {
        int color = chamsColor.getColor();
        float alpha = opacity.getValueFloat();
        if (pulse.getValue()) {
            float wave = (float) ((Math.sin(System.currentTimeMillis() / 300.0) + 1.0) / 2.0);
            alpha = 0.2f + wave * 0.6f;
        }
        int a = (int) (alpha * 255);
        return (color & 0x00FFFFFF) | (a << 24);
    }

    public boolean isRenderThroughWalls() { return renderThroughWalls.getValue(); }
}
