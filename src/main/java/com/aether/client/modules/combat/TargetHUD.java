package com.aether.client.modules.combat;

import com.aether.client.events.Render2DEvent;
import com.aether.client.gui.animations.Animation;
import com.aether.client.modules.api.Category;
import com.aether.client.modules.api.CompatibilityStatus;
import com.aether.client.modules.api.Module;
import com.aether.client.modules.settings.BooleanSetting;

/**
 * Target HUD module - Displays health, armor, and distance of the targeted entity.
 */
public class TargetHUD extends Module {
    private final BooleanSetting showArmor = addSetting(new BooleanSetting("Show Armor", "Hedefin zırhlarını göster", true));
    private final BooleanSetting showDistance = addSetting(new BooleanSetting("Distance", "Hedef mesafesini göster", true));

    private String targetName = "None";
    private float targetHealth = 20.0f;
    private float targetMaxHealth = 20.0f;
    private double targetDistance = 0.0;
    private final Animation healthAnimation = new Animation(250, 20.0);

    public TargetHUD() {
        super("Target HUD", "Hedeflenen varlığın canını, zırhını ve mesafesini şık bir HUD kartında gösterir.",
                Category.COMBAT, 0, CompatibilityStatus.SUPPORTED, false, true);
    }

    public void updateTarget(String name, float health, float maxHealth, double distance) {
        this.targetName = name != null ? name : "None";
        this.targetHealth = Math.max(0.0f, health);
        this.targetMaxHealth = Math.max(1.0f, maxHealth);
        this.targetDistance = distance;
        this.healthAnimation.animateTo(this.targetHealth);
    }

    public void clearTarget() {
        this.targetName = "None";
        this.targetHealth = 0.0f;
        this.healthAnimation.animateTo(0.0);
    }

    public String getTargetName() { return targetName; }
    public float getTargetHealth() { return targetHealth; }
    public float getTargetMaxHealth() { return targetMaxHealth; }
    public double getTargetDistance() { return targetDistance; }
    public double getAnimatedHealth() { return healthAnimation.getValue(); }
    public boolean hasTarget() { return !targetName.equals("None"); }
    public boolean isShowArmor() { return showArmor.getValue(); }
    public boolean isShowDistance() { return showDistance.getValue(); }
}
