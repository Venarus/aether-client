package com.aether.client.modules.settings;

/**
 * Boolean toggle setting for modules.
 */
public class BooleanSetting extends Setting<Boolean> {
    public BooleanSetting(String name, String description, boolean defaultValue) {
        super(name, description, defaultValue);
    }

    public void toggle() {
        setValue(!getValue());
    }

    @Override
    public String getValueAsString() {
        return String.valueOf(getValue());
    }

    @Override
    public void parseAndSet(String stringValue) {
        setValue(Boolean.parseBoolean(stringValue));
    }
}
