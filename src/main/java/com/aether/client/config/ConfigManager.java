package com.aether.client.config;

import com.aether.client.core.AetherClient;
import com.aether.client.modules.api.Module;
import com.aether.client.modules.settings.*;
import com.google.gson.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

/**
 * Robust JSON-based Configuration and Profile Manager for Aether Client.
 * Handles atomic saving, profile switching, and safe corruption recovery.
 */
public class ConfigManager {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private final File configDirectory;
    private final File profilesDirectory;
    private String currentProfile = "default";

    public ConfigManager(File baseDirectory) {
        this.configDirectory = new File(baseDirectory, "aether");
        this.profilesDirectory = new File(this.configDirectory, "profiles");
        ensureDirectories();
    }

    private void ensureDirectories() {
        if (!profilesDirectory.exists()) {
            profilesDirectory.mkdirs();
        }
    }

    public String getCurrentProfile() {
        return currentProfile;
    }

    public List<String> getAvailableProfiles() {
        ensureDirectories();
        List<String> list = new ArrayList<>();
        File[] files = profilesDirectory.listFiles((dir, name) -> name.endsWith(".json"));
        if (files != null) {
            for (File f : files) {
                String name = f.getName().replace(".json", "");
                list.add(name);
            }
        }
        if (list.isEmpty()) {
            list.add("default");
        }
        return list;
    }

    public void setProfile(String profileName) {
        if (profileName == null || profileName.isBlank()) return;
        saveProfile(this.currentProfile);
        this.currentProfile = profileName.trim().toLowerCase();
        loadProfile(this.currentProfile);
    }

    public void saveCurrentProfile() {
        saveProfile(this.currentProfile);
    }

    public void loadCurrentProfile() {
        loadProfile(this.currentProfile);
    }

    public synchronized void saveProfile(String profileName) {
        ensureDirectories();
        File targetFile = new File(profilesDirectory, profileName + ".json");
        File tempFile = new File(profilesDirectory, profileName + ".json.tmp");

        JsonObject root = new JsonObject();
        root.addProperty("profileName", profileName);
        root.addProperty("savedAt", System.currentTimeMillis());

        // Save Click GUI Key
        if (AetherClient.getInstance() != null && AetherClient.getInstance().getKeybindManager() != null) {
            root.addProperty("clickGuiKey", AetherClient.getInstance().getKeybindManager().getClickGuiKey());
        }

        // Save Modules
        JsonObject modulesObj = new JsonObject();
        if (AetherClient.getInstance() != null && AetherClient.getInstance().getModuleManager() != null) {
            for (Module m : AetherClient.getInstance().getModuleManager().getModules()) {
                JsonObject mObj = new JsonObject();
                mObj.addProperty("enabled", m.isEnabled());
                mObj.addProperty("keybind", m.getKey());

                JsonObject settingsObj = new JsonObject();
                for (Setting<?> s : m.getSettings()) {
                    if (s instanceof BooleanSetting) {
                        settingsObj.addProperty(s.getName(), ((BooleanSetting) s).getValue());
                    } else if (s instanceof NumberSetting) {
                        settingsObj.addProperty(s.getName(), ((NumberSetting) s).getValue());
                    } else if (s instanceof ModeSetting) {
                        settingsObj.addProperty(s.getName(), ((ModeSetting) s).getValue());
                    } else if (s instanceof ColorSetting) {
                        ColorSetting cs = (ColorSetting) s;
                        JsonObject colorObj = new JsonObject();
                        colorObj.addProperty("hex", cs.getValueAsString());
                        colorObj.addProperty("rainbow", cs.isRainbow());
                        settingsObj.add(s.getName(), colorObj);
                    } else {
                        settingsObj.addProperty(s.getName(), s.getValueAsString());
                    }
                }
                mObj.add("settings", settingsObj);
                modulesObj.add(m.getName(), mObj);
            }
        }
        root.add("modules", modulesObj);

        // Atomic write via temp file
        try {
            try (Writer writer = new OutputStreamWriter(new FileOutputStream(tempFile), StandardCharsets.UTF_8)) {
                GSON.toJson(root, writer);
                writer.flush();
            }
            try {
                Files.move(tempFile.toPath(), targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
            } catch (java.nio.file.AtomicMoveNotSupportedException ignored) {
                Files.move(tempFile.toPath(), targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException e) {
            System.err.printf("[Aether Config] Failed to save profile '%s': %s%n", profileName, e.getMessage());
        }
    }

    public synchronized void loadProfile(String profileName) {
        ensureDirectories();
        File targetFile = new File(profilesDirectory, profileName + ".json");
        if (!targetFile.exists()) {
            // First time load: save defaults as initial profile
            saveProfile(profileName);
            return;
        }

        try (Reader reader = new InputStreamReader(new FileInputStream(targetFile), StandardCharsets.UTF_8)) {
            JsonObject root = JsonParser.parseReader(reader).getAsJsonObject();

            // Load Click GUI Key
            if (root.has("clickGuiKey") && AetherClient.getInstance() != null && AetherClient.getInstance().getKeybindManager() != null) {
                AetherClient.getInstance().getKeybindManager().setClickGuiKey(root.get("clickGuiKey").getAsInt());
            }

            // Load Modules
            if (root.has("modules") && AetherClient.getInstance() != null && AetherClient.getInstance().getModuleManager() != null) {
                JsonObject modulesObj = root.getAsJsonObject("modules");
                for (Module m : AetherClient.getInstance().getModuleManager().getModules()) {
                    if (modulesObj.has(m.getName())) {
                        JsonObject mObj = modulesObj.getAsJsonObject(m.getName());
                        if (mObj.has("enabled")) {
                            m.setEnabled(mObj.get("enabled").getAsBoolean());
                        }
                        if (mObj.has("keybind")) {
                            m.setKey(mObj.get("keybind").getAsInt());
                        }
                        if (mObj.has("settings")) {
                            JsonObject sObj = mObj.getAsJsonObject("settings");
                            for (Setting<?> s : m.getSettings()) {
                                if (sObj.has(s.getName())) {
                                    JsonElement elem = sObj.get(s.getName());
                                    if (s instanceof BooleanSetting && elem.isJsonPrimitive()) {
                                        ((BooleanSetting) s).setValue(elem.getAsBoolean());
                                    } else if (s instanceof NumberSetting && elem.isJsonPrimitive()) {
                                        ((NumberSetting) s).setValue(elem.getAsDouble());
                                    } else if (s instanceof ModeSetting && elem.isJsonPrimitive()) {
                                        ((ModeSetting) s).setValue(elem.getAsString());
                                    } else if (s instanceof ColorSetting) {
                                        if (elem.isJsonObject()) {
                                            JsonObject cObj = elem.getAsJsonObject();
                                            if (cObj.has("hex")) {
                                                s.parseAndSet(cObj.get("hex").getAsString());
                                            }
                                            if (cObj.has("rainbow")) {
                                                ((ColorSetting) s).setRainbow(cObj.get("rainbow").getAsBoolean());
                                            }
                                        } else {
                                            s.parseAndSet(elem.getAsString());
                                        }
                                    } else {
                                        s.parseAndSet(elem.getAsString());
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Throwable t) {
            System.err.printf("[Aether Config] Corrupted profile '%s' detected! Backing up and resetting to defaults: %s%n",
                    profileName, t.getMessage());
            backupCorruptedProfile(targetFile);
            saveProfile(profileName);
        }
    }

    private void backupCorruptedProfile(File file) {
        try {
            File backup = new File(file.getParentFile(), file.getName() + ".corrupt_" + System.currentTimeMillis() + ".bak");
            Files.copy(file.toPath(), backup.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ignored) {}
    }

    public File getConfigDirectory() {
        return configDirectory;
    }
}
