package com.aether.client.rendering;

import java.awt.Color;

/**
 * Utility functions for color manipulation, alpha blending, and gradients.
 */
public final class ColorUtils {
    private ColorUtils() {}

    public static int rgba(int r, int g, int b, int a) {
        return ((a & 0xFF) << 24) | ((r & 0xFF) << 16) | ((g & 0xFF) << 8) | (b & 0xFF);
    }

    public static int rgb(int r, int g, int b) {
        return rgba(r, g, b, 255);
    }

    public static int withAlpha(int color, int alpha) {
        return ((alpha & 0xFF) << 24) | (color & 0x00FFFFFF);
    }

    public static int interpolate(int startColor, int endColor, float fraction) {
        float f = Math.max(0.0f, Math.min(1.0f, fraction));

        int sa = (startColor >> 24) & 0xFF;
        int sr = (startColor >> 16) & 0xFF;
        int sg = (startColor >> 8) & 0xFF;
        int sb = startColor & 0xFF;

        int ea = (endColor >> 24) & 0xFF;
        int er = (endColor >> 16) & 0xFF;
        int eg = (endColor >> 8) & 0xFF;
        int eb = endColor & 0xFF;

        int a = (int) (sa + (ea - sa) * f);
        int r = (int) (sr + (er - sr) * f);
        int g = (int) (sg + (eg - sg) * f);
        int b = (int) (sb + (eb - sb) * f);

        return rgba(r, g, b, a);
    }

    public static int getRainbow(float seconds, float saturation, float brightness, long index) {
        float hue = ((System.currentTimeMillis() + index) % (int) (seconds * 1000)) / (seconds * 1000);
        return Color.HSBtoRGB(hue, saturation, brightness);
    }

    public static int getHealthColor(float currentHealth, float maxHealth) {
        float ratio = Math.max(0.0f, Math.min(1.0f, currentHealth / maxHealth));
        if (ratio > 0.5f) {
            // Green to Yellow
            float f = (ratio - 0.5f) * 2.0f;
            return interpolate(0xFFFFEB3B, 0xFF4CAF50, f);
        } else {
            // Yellow to Red
            float f = ratio * 2.0f;
            return interpolate(0xFFF44336, 0xFFFFEB3B, f);
        }
    }
}
