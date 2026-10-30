package com.aether.client.gui;

import com.aether.client.core.AetherClient;
import com.aether.client.core.AetherConstants;
import com.aether.client.modules.combat.CPSCounter;
import com.aether.client.modules.combat.TargetHUD;
import com.aether.client.modules.movement.Speedometer;
import com.aether.client.modules.player.ArmorStatus;
import com.aether.client.modules.player.Coordinates;
import com.aether.client.modules.player.PotionEffects;
import com.aether.client.modules.utility.Keystrokes;
import com.aether.client.modules.utility.SessionStatistics;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;

import java.util.HashMap;
import java.util.Map;

/**
 * On-screen HUD Manager handling real-time rendering, positions, and layout of active visual modules.
 */
public class HUDManager {
    public static class HUDPosition {
        public double x, y;
        public boolean visible;

        public HUDPosition(double x, double y) {
            this.x = x;
            this.y = y;
            this.visible = true;
        }
    }

    private final Map<String, HUDPosition> elementPositions = new HashMap<>();

    public HUDManager() {
        // Default positions
        elementPositions.put("Coordinates", new HUDPosition(10, 24));
        elementPositions.put("Speedometer", new HUDPosition(10, 36));
        elementPositions.put("CPSCounter", new HUDPosition(10, 48));
        elementPositions.put("Keystrokes", new HUDPosition(10, 105));
        elementPositions.put("TargetHUD", new HUDPosition(320, 240));
        elementPositions.put("ArmorStatus", new HUDPosition(550, 400));
        elementPositions.put("PotionEffects", new HUDPosition(700, 10));
        elementPositions.put("SessionStatistics", new HUDPosition(10, 60));
    }

    public HUDPosition getPosition(String elementName) {
        return elementPositions.computeIfAbsent(elementName, k -> new HUDPosition(20, 20));
    }

    public void setPosition(String elementName, double x, double y) {
        HUDPosition pos = getPosition(elementName);
        pos.x = x;
        pos.y = y;
    }

    public void render(GuiGraphics graphics) {
        if (AetherClient.getInstance() == null || AetherClient.getInstance().getModuleManager() == null) return;
        Minecraft mc = Minecraft.getInstance();
        if (mc == null) return;
        Font font = mc.font;
        if (font == null) return;

        // Watermark: AETHER CLIENT v1.0.0
        graphics.drawString(font, AetherConstants.CLIENT_NAME.toUpperCase(), 10, 10, 0xFFE53935, true);
        graphics.drawString(font, "v" + AetherConstants.CLIENT_VERSION, 15 + font.width(AetherConstants.CLIENT_NAME.toUpperCase()), 10, 0xFFAAAAAA, true);

        int currentY = 24;

        // Coordinates HUD
        Coordinates coords = AetherClient.getInstance().getModuleManager().getModule(Coordinates.class);
        if (coords != null && coords.isEnabled()) {
            graphics.drawString(font, coords.getFormattedOverworld(), 10, currentY, 0xFFFFFFFF, true);
            currentY += 12;
            graphics.drawString(font, coords.getFormattedNether(), 10, currentY, 0xFF888888, true);
            currentY += 12;
        }

        // Speedometer HUD
        Speedometer speed = AetherClient.getInstance().getModuleManager().getModule(Speedometer.class);
        if (speed != null && speed.isEnabled()) {
            graphics.drawString(font, "Hız: " + speed.getSpeedDisplay(), 10, currentY, 0xFF00E676, true);
            currentY += 12;
        }

        // CPS Counter HUD
        CPSCounter cps = AetherClient.getInstance().getModuleManager().getModule(CPSCounter.class);
        if (cps != null && cps.isEnabled()) {
            String cpsStr = "CPS: " + cps.getLeftCPS() + (cps.isShowRightClick() ? " | " + cps.getRightCPS() : "");
            graphics.drawString(font, cpsStr, 10, currentY, 0xFF64B5F6, true);
            currentY += 12;
        }

        // Session Statistics
        SessionStatistics stats = AetherClient.getInstance().getModuleManager().getModule(SessionStatistics.class);
        if (stats != null && stats.isEnabled()) {
            String statStr = "Süre: " + stats.getFormattedSessionTime() + " | Ölümler: " + stats.getDeaths();
            graphics.drawString(font, statStr, 10, currentY, 0xFFFFB74D, true);
            currentY += 12;
        }

        // Target HUD
        TargetHUD target = AetherClient.getInstance().getModuleManager().getModule(TargetHUD.class);
        if (target != null && target.isEnabled() && target.hasTarget()) {
            HUDPosition tPos = getPosition("TargetHUD");
            int tx = (int) tPos.x;
            int ty = (int) tPos.y;
            graphics.fill(tx, ty, tx + 140, ty + 38, 0xD0101218);
            graphics.fill(tx, ty, tx + 140, ty + 1, 0xFFE53935);
            graphics.drawString(font, target.getTargetName(), tx + 8, ty + 6, 0xFFFFFFFF, false);

            double hp = target.getTargetHealth();
            double maxHp = target.getTargetMaxHealth();
            double percent = Math.max(0.0, Math.min(1.0, hp / maxHp));
            graphics.fill(tx + 8, ty + 20, tx + 132, ty + 28, 0xFF2A2D36);
            graphics.fill(tx + 8, ty + 20, tx + 8 + (int)(124 * percent), ty + 28, 0xFF00E676);
            graphics.drawString(font, String.format(java.util.Locale.US, "%.1f / %.1f", hp, maxHp), tx + 45, ty + 21, 0xFFFFFFFF, false);
        }
    }
}
