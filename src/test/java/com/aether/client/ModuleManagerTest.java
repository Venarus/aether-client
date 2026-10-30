package com.aether.client;

import com.aether.client.modules.api.Category;
import com.aether.client.modules.api.Module;
import com.aether.client.modules.api.ModuleManager;
import com.aether.client.modules.api.ModuleRegistry;
import com.aether.client.modules.visuals.Fullbright;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ModuleManagerTest {
    private ModuleManager moduleManager;

    @BeforeEach
    void setUp() {
        moduleManager = new ModuleManager();
        ModuleRegistry.registerAll(moduleManager);
    }

    @Test
    void testTotalModuleCount() {
        // 36 total modules registered
        assertTrue(moduleManager.getModuleCount() >= 33, "Should have at least 33 modules");
    }

    @Test
    void testCategoryFilter() {
        List<Module> visuals = moduleManager.getModulesByCategory(Category.VISUALS);
        assertEquals(8, visuals.size(), "Visuals category must have 8 modules");

        List<Module> movement = moduleManager.getModulesByCategory(Category.MOVEMENT);
        assertEquals(6, movement.size(), "Movement category must have 6 modules");

        List<Module> combat = moduleManager.getModulesByCategory(Category.COMBAT);
        assertEquals(5, combat.size(), "Combat category must have 5 modules");

        List<Module> world = moduleManager.getModulesByCategory(Category.WORLD);
        assertEquals(5, world.size(), "World category must have 5 modules");

        List<Module> player = moduleManager.getModulesByCategory(Category.PLAYER);
        assertEquals(6, player.size(), "Player category must have 6 modules");

        List<Module> utility = moduleManager.getModulesByCategory(Category.UTILITY);
        assertEquals(5, utility.size(), "Utility category must have 5 modules");
    }

    @Test
    void testSearchModules() {
        List<Module> results = moduleManager.search("Fullbright");
        assertEquals(1, results.size());
        assertEquals("Fullbright", results.get(0).getName());

        List<Module> hudResults = moduleManager.search("HUD");
        assertFalse(hudResults.isEmpty());
    }

    @Test
    void testModuleToggleAndDefaults() {
        Fullbright fullbright = moduleManager.getModule(Fullbright.class);
        assertNotNull(fullbright);
        assertFalse(fullbright.isEnabled());

        fullbright.toggle();
        assertTrue(fullbright.isEnabled());

        fullbright.toggle();
        assertFalse(fullbright.isEnabled());
    }
}
