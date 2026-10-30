package com.aether.client;

import com.aether.client.core.AetherClient;
import com.aether.client.gui.components.CategoryButton;
import com.aether.client.gui.components.ModuleCard;
import com.aether.client.gui.components.SettingComponent;
import com.aether.client.gui.screens.ClickGuiScreen;
import com.aether.client.modules.api.Category;
import com.aether.client.modules.api.Module;
import com.aether.client.modules.settings.NumberSetting;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ClickGuiTest {
    private ClickGuiScreen gui;

    @BeforeEach
    void setUp() {
        AetherClient.getInstance().initialize();
        gui = new ClickGuiScreen();
    }

    @AfterEach
    void tearDown() {
        AetherClient.getInstance().shutdown();
    }

    @Test
    void testClickGuiInitialization() {
        assertNotNull(gui);
        assertEquals(Category.VISUALS, gui.getSelectedCategory());
        assertFalse(gui.getCategoryButtons().isEmpty());
        assertFalse(gui.getModuleCards().isEmpty());
    }

    @Test
    void testSearchLiveFilter() {
        // Search for "Fullbright"
        gui.getSearchBar().setText("Fullbright");
        gui.rebuildModuleCards();

        assertEquals(1, gui.getModuleCards().size());
        assertEquals("Fullbright", gui.getModuleCards().get(0).getModule().getName());

        // Clear search
        gui.getSearchBar().clear();
        gui.rebuildModuleCards();
        assertEquals(8, gui.getModuleCards().size(), "Clearing search should restore Visuals category modules (8)");
    }

    @Test
    void testModuleCardToggle() {
        ModuleCard card = gui.getModuleCards().get(0);
        Module module = card.getModule();
        boolean initial = module.isEnabled();

        // Simulate left click (button 0) on the card
        card.mouseClicked(card.getX() + 10, card.getY() + 10, 0);
        assertEquals(!initial, module.isEnabled(), "Left click should toggle module state");
    }

    @Test
    void testSliderDragFractionCalculation() {
        NumberSetting setting = new NumberSetting("Intensity", "Test", 5.0, 0.0, 10.0, 1.0);
        SettingComponent component = new SettingComponent(100, 100, 200, 30, setting);

        // Click at middle of slider bar
        double sliderStart = 100 + 200 * 0.45; // 190
        double sliderW = 200 * 0.5; // 100
        double midX = sliderStart + sliderW * 0.5; // 240

        component.mouseClicked(midX, 115, 0);
        assertEquals(5.0, setting.getValue(), 0.001);
        assertEquals(0.5, component.getSliderFraction(), 0.001);
    }

    @Test
    void testEscKeyClosesGui() {
        AetherClient.getInstance().setClickGuiOpen(true);
        assertTrue(AetherClient.getInstance().isClickGuiOpen());

        // Key code 256 is GLFW_KEY_ESCAPE
        boolean handled = gui.keyPressed(256, 1, 0);
        assertTrue(handled);
        assertFalse(AetherClient.getInstance().isClickGuiOpen(), "ESC should close Click GUI");
    }
}
