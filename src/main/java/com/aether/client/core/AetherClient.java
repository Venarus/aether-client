package com.aether.client.core;

import com.aether.client.config.ConfigManager;
import com.aether.client.events.EventBus;
import com.aether.client.gui.HUDManager;
import com.aether.client.gui.screens.ClickGuiScreen;
import com.aether.client.keybinds.KeybindManager;
import com.aether.client.modules.api.ModuleManager;
import com.aether.client.networking.PermissionDashboard;

import java.io.File;

/**
 * Main singleton controller and lifecycle coordinator for Aether Client.
 */
public class AetherClient {
    private static AetherClient instance;

    private final EventBus eventBus;
    private final ModuleManager moduleManager;
    private final KeybindManager keybindManager;
    private final ConfigManager configManager;
    private final PermissionDashboard permissionDashboard;
    private final HUDManager hudManager;
    private ClickGuiScreen clickGuiScreen;

    private boolean initialized = false;
    private boolean clickGuiOpen = false;

    private AetherClient() {
        this.eventBus = new EventBus();
        this.moduleManager = new ModuleManager();
        this.keybindManager = new KeybindManager();
        this.permissionDashboard = new PermissionDashboard();
        this.hudManager = new HUDManager();

        File gameDir = new File(System.getProperty("user.dir"));
        this.configManager = new ConfigManager(gameDir);
    }

    public static synchronized AetherClient getInstance() {
        if (instance == null) {
            instance = new AetherClient();
        }
        return instance;
    }

    public synchronized void initialize() {
        if (initialized) return;

        System.out.printf("[Aether Client] Initializing %s v%s for Minecraft %s...%n",
                AetherConstants.CLIENT_NAME, AetherConstants.CLIENT_VERSION, AetherConstants.TARGET_MINECRAFT);

        // Register core event listeners
        this.eventBus.register(this.keybindManager);

        // Register all modular client modules
        com.aether.client.modules.api.ModuleRegistry.registerAll(this.moduleManager);
        this.moduleManager.enableDefaults();

        // Load active configuration
        this.configManager.loadCurrentProfile();

        this.initialized = true;
        System.out.printf("[Aether Client] Successfully initialized %d modules in %s.%n",
                this.moduleManager.getModuleCount(), AetherConstants.CLIENT_NAME);
    }

    public synchronized void shutdown() {
        if (!initialized) return;

        System.out.println("[Aether Client] Shutting down client subsystems and saving active profile...");
        this.configManager.saveCurrentProfile();
        this.moduleManager.disableAll();
        this.eventBus.clear();
        this.initialized = false;
    }

    public void toggleClickGui() {
        this.clickGuiOpen = !this.clickGuiOpen;
        System.out.printf("[Aether Client] Click GUI is now %s.%n", this.clickGuiOpen ? "OPEN" : "CLOSED");
    }

    public boolean isClickGuiOpen() {
        return clickGuiOpen;
    }

    public void setClickGuiOpen(boolean clickGuiOpen) {
        this.clickGuiOpen = clickGuiOpen;
    }

    public EventBus getEventBus() {
        return eventBus;
    }

    public ModuleManager getModuleManager() {
        return moduleManager;
    }

    public KeybindManager getKeybindManager() {
        return keybindManager;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public synchronized ClickGuiScreen getClickGuiScreen() {
        if (this.clickGuiScreen == null) {
            this.clickGuiScreen = new ClickGuiScreen();
        }
        return this.clickGuiScreen;
    }

    public HUDManager getHudManager() {
        return this.hudManager;
    }

    public PermissionDashboard getPermissionDashboard() {
        return permissionDashboard;
    }

    public int getAccentColor() {
        if (moduleManager != null) {
            com.aether.client.modules.settings.ClientTheme theme = moduleManager.getModule(com.aether.client.modules.settings.ClientTheme.class);
            if (theme != null) {
                return theme.getAccentColor();
            }
        }
        return AetherConstants.COLOR_ACCENT_CRIMSON;
    }

    public boolean isInitialized() {
        return initialized;
    }
}
