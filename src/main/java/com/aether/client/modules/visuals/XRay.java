package com.aether.client.modules.visuals;

import com.aether.client.events.Render3DEvent;
import com.aether.client.modules.api.Category;
import com.aether.client.modules.api.CompatibilityStatus;
import com.aether.client.modules.api.Module;
import com.aether.client.modules.settings.BooleanSetting;
import com.aether.client.modules.settings.NumberSetting;

/**
 * X-Ray module - Highlights loaded ore blocks in local test environments.
 * Only visualizes blocks already received and loaded by the client.
 */
public class XRay extends Module {
    private final BooleanSetting diamond = addSetting(new BooleanSetting("Diamond", "Elmas cevherlerini göster", true));
    private final BooleanSetting netherite = addSetting(new BooleanSetting("Netherite", "Antik Kalıntıları göster", true));
    private final BooleanSetting gold = addSetting(new BooleanSetting("Gold", "Altın cevherlerini göster", true));
    private final BooleanSetting iron = addSetting(new BooleanSetting("Iron", "Demir cevherlerini göster", true));
    private final BooleanSetting coal = addSetting(new BooleanSetting("Coal", "Kömür cevherlerini göster", false));
    private final NumberSetting range = addSetting(new NumberSetting("Range", "Tarama yarıçapı", 24.0, 8.0, 48.0, 4.0, "m"));
    private final NumberSetting opacity = addSetting(new NumberSetting("Opacity", "Opaklık derecesi", 0.7, 0.1, 1.0, 0.1));

    public XRay() {
        super("X-Ray", "İzinli test dünyasında yüklenmiş cevher bloklarını görsel olarak vurgular.",
                Category.VISUALS, CompatibilityStatus.SUPPORTED);
    }

    @Override
    protected void render3D(Render3DEvent event) {
        // Renders outlines around loaded matching blocks within range
    }

    public boolean isDiamond() { return diamond.getValue(); }
    public boolean isNetherite() { return netherite.getValue(); }
    public boolean isGold() { return gold.getValue(); }
    public boolean isIron() { return iron.getValue(); }
    public boolean isCoal() { return coal.getValue(); }
    public double getRange() { return range.getValue(); }
    public double getOpacity() { return opacity.getValue(); }
}
