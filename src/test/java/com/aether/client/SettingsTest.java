package com.aether.client;

import com.aether.client.modules.settings.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SettingsTest {
    @Test
    void testBooleanSetting() {
        BooleanSetting setting = new BooleanSetting("TestBool", "Description", false);
        assertFalse(setting.getValue());

        setting.toggle();
        assertTrue(setting.getValue());

        setting.parseAndSet("false");
        assertFalse(setting.getValue());

        setting.reset();
        assertFalse(setting.getValue());
    }

    @Test
    void testNumberSettingClampingAndStepping() {
        NumberSetting setting = new NumberSetting("Speed", "Test speed", 5.0, 1.0, 10.0, 0.5, "m/s");
        assertEquals(5.0, setting.getValue());

        // Value beyond max should clamp to max
        setting.setValue(25.0);
        assertEquals(10.0, setting.getValue());

        // Value below min should clamp to min
        setting.setValue(-5.0);
        assertEquals(1.0, setting.getValue());

        // Value stepping: 3.23 with step 0.5 should round to 3.0 or 3.5
        setting.setValue(3.23);
        assertEquals(3.0, setting.getValue());

        setting.setValue(3.35);
        assertEquals(3.5, setting.getValue());

        assertEquals("3.50m/s", setting.getValueAsString());
    }

    @Test
    void testModeSettingCycle() {
        ModeSetting mode = new ModeSetting("Mode", "Test mode", "Gamma", "Gamma", "NightVision", "Lightmap");
        assertEquals("Gamma", mode.getValue());
        assertTrue(mode.is("gamma"));

        mode.cycle();
        assertEquals("NightVision", mode.getValue());

        mode.cycle();
        assertEquals("Lightmap", mode.getValue());

        mode.cycle();
        assertEquals("Gamma", mode.getValue(), "Cycling should wrap around to first mode");

        mode.cycleBack();
        assertEquals("Lightmap", mode.getValue());
    }

    @Test
    void testColorSetting() {
        ColorSetting color = new ColorSetting("ThemeColor", "Accent", 0xFFE53935);
        assertEquals(0xFFE53935, color.getColor());
        assertEquals(255, color.getAlpha());
        assertEquals(229, color.getRed());
        assertEquals(57, color.getGreen());
        assertEquals(53, color.getBlue());

        color.parseAndSet("#00E5FF");
        assertEquals(0x00E5FF, color.getColor() & 0x00FFFFFF);
    }
}
