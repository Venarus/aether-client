package com.aether.client.keybinds;

import com.aether.client.core.AetherClient;
import com.aether.client.events.KeyEvent;
import com.aether.client.events.Subscribe;
import com.aether.client.modules.api.Module;

import java.util.*;

/**
 * Keybind management system handling GLFW key mappings, client hotkeys, and conflict detection.
 */
public class KeybindManager {
    // Default GLFW key codes
    public static final int KEY_RIGHT_SHIFT = 344;
    public static final int KEY_F7 = 296;
    public static final int KEY_F8 = 297;
    public static final int KEY_ESCAPE = 256;

    private int clickGuiKey = KEY_RIGHT_SHIFT;
    private final Map<Integer, String> keyNames = new HashMap<>();

    public KeybindManager() {
        initKeyNames();
    }

    private void initKeyNames() {
        keyNames.put(0, "NONE");
        keyNames.put(KEY_ESCAPE, "ESCAPE");
        keyNames.put(257, "ENTER");
        keyNames.put(258, "TAB");
        keyNames.put(259, "BACKSPACE");
        keyNames.put(KEY_RIGHT_SHIFT, "RSHIFT");
        keyNames.put(340, "LSHIFT");
        keyNames.put(341, "LCTRL");
        keyNames.put(345, "RCTRL");
        keyNames.put(342, "LALT");
        keyNames.put(346, "RALT");
        keyNames.put(32, "SPACE");
        keyNames.put(KEY_F7, "F7");
        keyNames.put(KEY_F8, "F8");

        // F1 - F12
        for (int i = 1; i <= 12; i++) {
            keyNames.put(289 + i, "F" + i);
        }

        // Standard letters A-Z
        for (int c = 'A'; c <= 'Z'; c++) {
            keyNames.put(c, String.valueOf((char) c));
        }

        // Numbers 0-9
        for (int n = '0'; n <= '9'; n++) {
            keyNames.put(n, String.valueOf((char) n));
        }
    }

    public String getKeyName(int keyCode) {
        if (keyCode <= 0) return "NONE";
        return keyNames.getOrDefault(keyCode, "KEY_" + keyCode);
    }

    public int getClickGuiKey() {
        return clickGuiKey;
    }

    public void setClickGuiKey(int clickGuiKey) {
        this.clickGuiKey = clickGuiKey;
    }

    /**
     * Detects key conflicts among enabled modules.
     * Returns a map of KeyCode -> List of module names sharing that key.
     */
    public Map<Integer, List<String>> detectConflicts() {
        Map<Integer, List<String>> boundMap = new HashMap<>();
        if (AetherClient.getInstance() == null || AetherClient.getInstance().getModuleManager() == null) {
            return boundMap;
        }

        for (Module m : AetherClient.getInstance().getModuleManager().getModules()) {
            int key = m.getKey();
            if (key > 0) {
                boundMap.computeIfAbsent(key, k -> new ArrayList<>()).add(m.getName());
            }
        }

        // Add Click GUI keybind
        if (clickGuiKey > 0) {
            boundMap.computeIfAbsent(clickGuiKey, k -> new ArrayList<>()).add("Click GUI");
        }

        Map<Integer, List<String>> conflicts = new HashMap<>();
        for (Map.Entry<Integer, List<String>> entry : boundMap.entrySet()) {
            if (entry.getValue().size() > 1) {
                conflicts.put(entry.getKey(), entry.getValue());
            }
        }
        return conflicts;
    }

    @Subscribe
    public void onKey(KeyEvent event) {
        if (!event.isPress()) return;

        int key = event.getKey();
        if (key <= 0) return;

        // Click GUI toggle
        if (key == clickGuiKey) {
            if (AetherClient.getInstance() != null) {
                AetherClient.getInstance().toggleClickGui();
                event.cancel();
                return;
            }
        }

        // Dispatch to modules
        if (AetherClient.getInstance() != null && AetherClient.getInstance().getModuleManager() != null) {
            AetherClient.getInstance().getModuleManager().onKey(key);
        }
    }
}
