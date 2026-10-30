package com.aether.client.modules.settings;

/**
 * RGBA Color setting supporting rainbow cycle and alpha opacity.
 */
public class ColorSetting extends Setting<Integer> {
    private boolean rainbow;

    public ColorSetting(String name, String description, int defaultColor, boolean defaultRainbow) {
        super(name, description, defaultColor);
        this.rainbow = defaultRainbow;
    }

    public ColorSetting(String name, String description, int defaultColor) {
        this(name, description, defaultColor, false);
    }

    public boolean isRainbow() {
        return rainbow;
    }

    public void setRainbow(boolean rainbow) {
        this.rainbow = rainbow;
    }

    public int getColor() {
        if (rainbow) {
            float hue = (System.currentTimeMillis() % 4000L) / 4000.0f;
            int rgb = java.awt.Color.HSBtoRGB(hue, 0.8f, 1.0f);
            int alpha = (getValue() >> 24) & 0xFF;
            return (alpha << 24) | (rgb & 0x00FFFFFF);
        }
        return getValue();
    }

    public int getRed() {
        return (getColor() >> 16) & 0xFF;
    }

    public int getGreen() {
        return (getColor() >> 8) & 0xFF;
    }

    public int getBlue() {
        return getColor() & 0xFF;
    }

    public int getAlpha() {
        return (getColor() >> 24) & 0xFF;
    }

    @Override
    public String getValueAsString() {
        return String.format("#%08X", getValue());
    }

    @Override
    public void parseAndSet(String stringValue) {
        try {
            if (stringValue.startsWith("#")) {
                long parsed = Long.parseLong(stringValue.substring(1), 16);
                setValue((int) parsed);
            } else {
                setValue(Integer.parseInt(stringValue));
            }
        } catch (NumberFormatException ignored) {}
    }
}
