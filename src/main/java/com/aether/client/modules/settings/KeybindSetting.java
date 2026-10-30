package com.aether.client.modules.settings;

/**
 * Keybind setting holding a GLFW key code.
 */
public class KeybindSetting extends Setting<Integer> {
    public static final int KEY_NONE = 0;

    public KeybindSetting(String name, String description, int defaultKey) {
        super(name, description, defaultKey);
    }

    public boolean isBound() {
        return getValue() != null && getValue() != KEY_NONE;
    }

    @Override
    public String getValueAsString() {
        return String.valueOf(getValue());
    }

    @Override
    public void parseAndSet(String stringValue) {
        try {
            setValue(Integer.parseInt(stringValue));
        } catch (NumberFormatException ignored) {}
    }
}
