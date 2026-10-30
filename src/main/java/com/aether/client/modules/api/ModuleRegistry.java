package com.aether.client.modules.api;

import com.aether.client.modules.combat.*;
import com.aether.client.modules.movement.*;
import com.aether.client.modules.player.*;
import com.aether.client.modules.world.*;
import com.aether.client.modules.settings.ClientTheme;
import com.aether.client.modules.utility.*;
import com.aether.client.modules.visuals.*;

/**
 * Standard registry declaring all built-in modules for Aether Client.
 */
public final class ModuleRegistry {
    private ModuleRegistry() {}

    public static void registerAll(ModuleManager manager) {
        // Visuals
        manager.register(new EntityESP());
        manager.register(new Tracers());
        manager.register(new XRay());
        manager.register(new StorageESP());
        manager.register(new Fullbright());
        manager.register(new HealthESP());
        manager.register(new NameTags());
        manager.register(new Chams());

        // Movement
        manager.register(new Sprint());
        manager.register(new Speedometer());
        manager.register(new MotionTracker());
        manager.register(new ElytraHUD());
        manager.register(new JumpDiagnostics());
        manager.register(new Fly());

        // Combat
        manager.register(new TargetHUD());
        manager.register(new CPSCounter());
        manager.register(new ReachDisplay());
        manager.register(new AimTrainer());
        manager.register(new CombatLog());

        // World
        manager.register(new ChestFinder());
        manager.register(new SpawnerFinder());
        manager.register(new CaveMapper());
        manager.register(new ChunkVisualizer());
        manager.register(new WaypointManager());

        // Player
        manager.register(new InventoryHUD());
        manager.register(new ArmorStatus());
        manager.register(new TotemCounter());
        manager.register(new PotionEffects());
        manager.register(new Coordinates());
        manager.register(new GamemodeManager());

        // Utility
        manager.register(new AutoReconnect());
        manager.register(new Keystrokes());
        manager.register(new ScreenshotManager());
        manager.register(new InventoryCleaner());
        manager.register(new SessionStatistics());

        // Settings
        manager.register(new ClientTheme());
    }
}
