package com.aether.client;

import com.aether.client.config.ConfigManager;
import com.aether.client.core.AetherClient;
import com.aether.client.modules.visuals.Fullbright;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class ConfigManagerTest {
    @TempDir
    File tempDir;

    private ConfigManager configManager;

    @BeforeEach
    void setUp() {
        AetherClient.getInstance().initialize();
        configManager = new ConfigManager(tempDir);
    }

    @AfterEach
    void tearDown() {
        AetherClient.getInstance().shutdown();
    }

    @Test
    void testSaveAndLoadProfile() {
        Fullbright fullbright = AetherClient.getInstance().getModuleManager().getModule(Fullbright.class);
        assertNotNull(fullbright);
        fullbright.setEnabled(true);

        configManager.saveProfile("test_profile");
        assertTrue(configManager.getAvailableProfiles().contains("test_profile"));

        // Change state
        fullbright.setEnabled(false);
        assertFalse(fullbright.isEnabled());

        // Reload profile
        configManager.loadProfile("test_profile");
        assertTrue(fullbright.isEnabled(), "Module should be re-enabled after profile load");
    }

    @Test
    void testCorruptedProfileSafeRecovery() throws IOException {
        File profilesDir = new File(tempDir, "aether/profiles");
        profilesDir.mkdirs();
        File corruptFile = new File(profilesDir, "corrupted.json");

        try (FileWriter fw = new FileWriter(corruptFile)) {
            fw.write("{ this is completely invalid json ###");
        }

        // Should not throw exception and should safely reset to defaults
        assertDoesNotThrow(() -> configManager.loadProfile("corrupted"));
    }
}
