package com.aether.client.modules.api;

import com.aether.client.core.AetherClient;
import com.aether.client.events.Render2DEvent;
import com.aether.client.events.Render3DEvent;
import com.aether.client.modules.settings.KeybindSetting;
import com.aether.client.modules.settings.Setting;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Base abstract class for all Aether Client modules.
 * Encapsulates lifecycle, settings, keybinding, compatibility metadata, and isolated error handling.
 */
public abstract class Module {
    private final String name;
    private final String description;
    private final Category category;
    private final CompatibilityStatus compatibilityStatus;
    private final boolean permissionRequired;
    private final boolean defaultEnabled;

    private boolean enabled;
    private final KeybindSetting keybind;
    private final List<Setting<?>> settings = new ArrayList<>();
    private String lastErrorMessage = null;

    public Module(String name, String description, Category category, int defaultKey, CompatibilityStatus compatibilityStatus, boolean permissionRequired, boolean defaultEnabled) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.compatibilityStatus = compatibilityStatus;
        this.permissionRequired = permissionRequired;
        this.defaultEnabled = defaultEnabled;
        this.keybind = new KeybindSetting("Keybind", "Modülü açıp kapatan klavye tuşu", defaultKey);
        this.settings.add(this.keybind);
        this.enabled = false;
    }

    public Module(String name, String description, Category category, int defaultKey, CompatibilityStatus compatibilityStatus) {
        this(name, description, category, defaultKey, compatibilityStatus, false, false);
    }

    public Module(String name, String description, Category category, CompatibilityStatus compatibilityStatus) {
        this(name, description, category, KeybindSetting.KEY_NONE, compatibilityStatus, false, false);
    }

    public Module(String name, String description, Category category) {
        this(name, description, category, KeybindSetting.KEY_NONE, CompatibilityStatus.SUPPORTED, false, false);
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Category getCategory() {
        return category;
    }

    public CompatibilityStatus getCompatibilityStatus() {
        return compatibilityStatus;
    }

    public boolean isPermissionRequired() {
        return permissionRequired;
    }

    public boolean isDefaultEnabled() {
        return defaultEnabled;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public int getKey() {
        return keybind.getValue();
    }

    public void setKey(int key) {
        keybind.setValue(key);
    }

    public KeybindSetting getKeybindSetting() {
        return keybind;
    }

    public List<Setting<?>> getSettings() {
        return Collections.unmodifiableList(settings);
    }

    protected <T extends Setting<?>> T addSetting(T setting) {
        if (setting != null && !settings.contains(setting)) {
            settings.add(setting);
        }
        return setting;
    }

    public Setting<?> getSettingByName(String name) {
        for (Setting<?> s : settings) {
            if (s.getName().equalsIgnoreCase(name)) {
                return s;
            }
        }
        return null;
    }

    public void setEnabled(boolean state) {
        if (this.enabled == state) return;

        this.enabled = state;
        try {
            if (this.enabled) {
                if (AetherClient.getInstance() != null && AetherClient.getInstance().getEventBus() != null) {
                    AetherClient.getInstance().getEventBus().register(this);
                }
                onEnable();
            } else {
                if (AetherClient.getInstance() != null && AetherClient.getInstance().getEventBus() != null) {
                    AetherClient.getInstance().getEventBus().unregister(this);
                }
                onDisable();
            }
            this.lastErrorMessage = null;
        } catch (Throwable t) {
            this.lastErrorMessage = t.getMessage();
            System.err.printf("[Aether Module] Error in %s (%s): %s%n",
                    name, state ? "onEnable" : "onDisable", t.getMessage());
        }
    }

    public void toggle() {
        setEnabled(!this.enabled);
    }

    // Lifecycle hooks for subclasses
    protected void onEnable() {}
    protected void onDisable() {}

    public void onTick() {
        if (!enabled) return;
        try {
            update();
        } catch (Throwable t) {
            this.lastErrorMessage = t.getMessage();
        }
    }

    protected void update() {}

    public void onRender2D(Render2DEvent event) {
        if (!enabled) return;
        try {
            render2D(event);
        } catch (Throwable t) {
            this.lastErrorMessage = t.getMessage();
        }
    }

    protected void render2D(Render2DEvent event) {}

    public void onRender3D(Render3DEvent event) {
        if (!enabled) return;
        try {
            render3D(event);
        } catch (Throwable t) {
            this.lastErrorMessage = t.getMessage();
        }
    }

    protected void render3D(Render3DEvent event) {}

    public String getLastErrorMessage() {
        return lastErrorMessage;
    }

    public boolean hasError() {
        return lastErrorMessage != null;
    }

    public void clearError() {
        this.lastErrorMessage = null;
    }
}
