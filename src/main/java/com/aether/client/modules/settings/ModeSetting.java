package com.aether.client.modules.settings;

import java.util.Arrays;
import java.util.List;

/**
 * Dropdown/Cycle mode selector setting.
 */
public class ModeSetting extends Setting<String> {
    private final List<String> modes;

    public ModeSetting(String name, String description, String defaultMode, String... modes) {
        super(name, description, defaultMode);
        if (modes.length == 0) {
            this.modes = List.of(defaultMode);
        } else {
            this.modes = Arrays.asList(modes);
        }
        if (!this.modes.contains(defaultMode)) {
            setValue(this.modes.get(0));
        }
    }

    public List<String> getModes() {
        return modes;
    }

    public int getIndex() {
        return modes.indexOf(getValue());
    }

    public void cycle() {
        int next = (getIndex() + 1) % modes.size();
        setValue(modes.get(next));
    }

    public void cycleBack() {
        int prev = (getIndex() - 1 + modes.size()) % modes.size();
        setValue(modes.get(prev));
    }

    public boolean is(String mode) {
        return getValue() != null && getValue().equalsIgnoreCase(mode);
    }

    @Override
    public void setValue(String value) {
        for (String m : modes) {
            if (m.equalsIgnoreCase(value)) {
                super.setValue(m);
                return;
            }
        }
    }

    @Override
    public String getValueAsString() {
        return getValue();
    }

    @Override
    public void parseAndSet(String stringValue) {
        setValue(stringValue);
    }
}
