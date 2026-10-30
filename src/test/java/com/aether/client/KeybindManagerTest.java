package com.aether.client;

import com.aether.client.core.AetherClient;
import com.aether.client.keybinds.KeybindManager;
import com.aether.client.modules.player.Coordinates;
import com.aether.client.modules.visuals.Fullbright;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class KeybindManagerTest {
    private KeybindManager keybindManager;

    @BeforeEach
    void setUp() {
        AetherClient.getInstance().initialize();
        keybindManager = AetherClient.getInstance().getKeybindManager();
    }

    @AfterEach
    void tearDown() {
        AetherClient.getInstance().shutdown();
    }

    @Test
    void testDefaultKeybinds() {
        assertEquals(KeybindManager.KEY_RIGHT_SHIFT, keybindManager.getClickGuiKey());
        assertEquals("RSHIFT", keybindManager.getKeyName(KeybindManager.KEY_RIGHT_SHIFT));
        assertEquals("F7", keybindManager.getKeyName(KeybindManager.KEY_F7));
        assertEquals("F8", keybindManager.getKeyName(KeybindManager.KEY_F8));
    }

    @Test
    void testKeyConflictDetection() {
        Fullbright fullbright = AetherClient.getInstance().getModuleManager().getModule(Fullbright.class);
        Coordinates coordinates = AetherClient.getInstance().getModuleManager().getModule(Coordinates.class);

        // Bind both to F8
        fullbright.setKey(KeybindManager.KEY_F8);
        coordinates.setKey(KeybindManager.KEY_F8);

        Map<Integer, List<String>> conflicts = keybindManager.detectConflicts();
        assertTrue(conflicts.containsKey(KeybindManager.KEY_F8), "Conflict on F8 should be detected");
        assertTrue(conflicts.get(KeybindManager.KEY_F8).contains("Fullbright"));
        assertTrue(conflicts.get(KeybindManager.KEY_F8).contains("Coordinates"));
    }
}
