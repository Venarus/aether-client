package com.aether.client.modules.visuals;

import com.aether.client.events.Render3DEvent;
import com.aether.client.modules.api.Category;
import com.aether.client.modules.api.CompatibilityStatus;
import com.aether.client.modules.api.Module;
import com.aether.client.modules.settings.BooleanSetting;
import com.aether.client.modules.settings.ColorSetting;
import com.aether.client.modules.settings.NumberSetting;

/**
 * Tracers module - Draws lines from camera center to nearby entities.
 */
public class Tracers extends Module {
    private final BooleanSetting targetPlayers = addSetting(new BooleanSetting("Players", "Oyunculara çizgi çek", true));
    private final BooleanSetting targetMobs = addSetting(new BooleanSetting("Mobs", "Canavarlara çizgi çek", false));
    private final NumberSetting maxDistance = addSetting(new NumberSetting("Max Distance", "Maksimum çizgi mesafesi", 64.0, 10.0, 128.0, 5.0, "m"));
    private final ColorSetting tracerColor = addSetting(new ColorSetting("Color", "Çizgi rengi", 0xFF00E5FF));
    private final NumberSetting thickness = addSetting(new NumberSetting("Thickness", "Çizgi kalınlığı", 1.5, 1.0, 4.0, 0.5));

    public Tracers() {
        super("Tracers", "Kamera merkezinden hedef varlıklara kılavuz çizgiler çizer.",
                Category.VISUALS, CompatibilityStatus.SUPPORTED);
    }

    @Override
    protected void render3D(Render3DEvent event) {
        // Perspective camera ray tracing to entity centroids
    }

    public boolean isTargetPlayers() { return targetPlayers.getValue(); }
    public boolean isTargetMobs() { return targetMobs.getValue(); }
    public double getMaxDistance() { return maxDistance.getValue(); }
    public int getTracerColor() { return tracerColor.getColor(); }
    public double getThickness() { return thickness.getValue(); }
}
