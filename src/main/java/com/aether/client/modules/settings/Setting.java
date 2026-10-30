package com.aether.client.modules.settings;

import java.util.Objects;
import java.util.function.BooleanSupplier;

/**
 * Generic setting parameter for client modules.
 */
public abstract class Setting<T> {
    private final String name;
    private final String description;
    private final T defaultValue;
    private T value;
    private BooleanSupplier visibility = () -> true;

    public Setting(String name, String description, T defaultValue) {
        this.name = Objects.requireNonNull(name, "Setting name cannot be null");
        this.description = description != null ? description : "";
        this.defaultValue = defaultValue;
        this.value = defaultValue;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public T getDefaultValue() {
        return defaultValue;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public void reset() {
        this.value = this.defaultValue;
    }

    public boolean isVisible() {
        return visibility == null || visibility.getAsBoolean();
    }

    public Setting<T> setVisibility(BooleanSupplier visibility) {
        this.visibility = visibility;
        return this;
    }

    public abstract String getValueAsString();

    public abstract void parseAndSet(String stringValue);
}
