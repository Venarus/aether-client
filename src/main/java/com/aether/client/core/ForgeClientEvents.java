package com.aether.client.core;

import com.aether.client.events.KeyEvent;
import com.aether.client.gui.screens.AetherForgeScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.AddGuiOverlayLayersEvent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.event.TickEvent;

/**
 * Bridges native Minecraft Forge client events with the Aether Client subsystem.
 * Listens for key inputs, ticks, and registers in-game HUD render layers.
 */
public class ForgeClientEvents {

    public static void register() {
        System.out.println("[Aether Client] Registering Forge client event listeners...");

        // 1. Input Event: Listen for GLFW key presses
        InputEvent.Key.BUS.addListener(event -> {
            if (event.getAction() == 1) { // 1 = GLFW_PRESS
                int key = event.getKey();
                int clickGuiKey = AetherClient.getInstance().getKeybindManager().getClickGuiKey();

                if (key == clickGuiKey) {
                    Minecraft mc = Minecraft.getInstance();
                    mc.execute(() -> {
                        if (mc.screen == null) {
                            mc.setScreen(new AetherForgeScreen());
                        } else if (mc.screen instanceof AetherForgeScreen) {
                            mc.setScreen(null);
                        }
                    });
                } else {
                    // Dispatch to Aether Client event bus
                    AetherClient.getInstance().getEventBus().post(
                            new KeyEvent(key, event.getScanCode(), event.getAction(), event.getModifiers())
                    );
                }
            }
        });

        // 2. Client Tick Event
        TickEvent.ClientTickEvent.Post.BUS.addListener(event -> {
            AetherClientTicker.onClientTick();
            AetherClient.getInstance().getEventBus().post(
                    new com.aether.client.events.ClientTickEvent(com.aether.client.events.ClientTickEvent.Phase.END, 0)
            );
        });

        // 3. In-Game HUD Overlay Layer
        AddGuiOverlayLayersEvent.BUS.addListener(event -> {
            event.getLayeredDraw().add(
                    ResourceLocation.fromNamespaceAndPath("aetherclient", "hud_overlay"),
                    (graphics, delta) -> {
                        Minecraft mc = Minecraft.getInstance();
                        if (mc == null || mc.options == null || mc.options.hideGui) return;
                        if (mc.screen instanceof AetherForgeScreen) return;

                        AetherClient.getInstance().getHudManager().render(graphics);
                    }
            );
        });
    }
}
