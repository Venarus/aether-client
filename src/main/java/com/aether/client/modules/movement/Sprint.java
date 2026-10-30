package com.aether.client.modules.movement;

import com.aether.client.modules.api.Category;
import com.aether.client.modules.api.CompatibilityStatus;
import com.aether.client.modules.api.Module;
import com.aether.client.modules.settings.BooleanSetting;
import com.aether.client.modules.settings.ModeSetting;

/**
 * Sprint module - Manages automatic sprinting and toggle sprinting cleanly.
 */
public class Sprint extends Module {
    private final ModeSetting mode = addSetting(new ModeSetting("Mode", "Sprint çalışma modu", "Toggle", "Toggle", "Always"));
    private final BooleanSetting keepSprint = addSetting(new BooleanSetting("Keep In Air", "Havadayken sprinti koru", true));

    public Sprint() {
        super("Sprint", "Karakterin otomatik veya bas-bırak olarak koşmasını sağlar.",
                Category.MOVEMENT, CompatibilityStatus.SUPPORTED);
    }

    public ModeSetting getMode() {
        return mode;
    }

    public boolean isKeepSprint() {
        return keepSprint.getValue();
    }
}
