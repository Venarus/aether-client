package com.aether.client;

import com.aether.client.core.AetherClient;
import com.aether.client.core.ForgeClientEvents;
import net.minecraftforge.fml.common.Mod;

/**
 * Main Forge Mod Entrypoint for Aether Client.
 * Annotated with @Mod to allow FMLJavaModLanguageProvider to detect and construct the mod container.
 */
@Mod(AetherMod.MOD_ID)
public class AetherMod {
    public static final String MOD_ID = "aetherclient";

    public AetherMod() {
        System.out.println("[Aether Mod] Loading Aether Client mod instance...");
        AetherClient.getInstance().initialize();

        // Register native Forge client events (Input, Render, Tick)
        try {
            ForgeClientEvents.register();
            System.out.println("[Aether Mod] Forge client events registered successfully.");
        } catch (Throwable t) {
            System.err.println("[Aether Mod] Warning: Could not register Forge client events: " + t.getMessage());
        }

        // Register shutdown hook
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("[Aether Mod] JVM shutdown hook triggered.");
            AetherClient.getInstance().shutdown();
        }, "AetherClient-Shutdown"));
    }
}
