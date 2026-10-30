package com.aether.client.modules.settings;

import com.aether.client.modules.api.Category;
import com.aether.client.modules.api.CompatibilityStatus;
import com.aether.client.modules.api.Module;

/**
 * Client Theme module - Customizes Click GUI visual themes, accent colors, animations, and sound effects.
 */
public class ClientTheme extends Module {
    private final ModeSetting accent = addSetting(new ModeSetting("Accent Color", "Vurgu rengi", "Crimson", "Crimson", "Purple", "Cyan"));
    private final ModeSetting animationSpeed = addSetting(new ModeSetting("Animations", "Arayüz animasyon hızı", "Smooth", "Smooth", "Instant", "Bouncy"));
    private final BooleanSetting blurBackground = addSetting(new BooleanSetting("Blur Background", "Arka plan bulanıklaştırması", true));
    private final BooleanSetting uiSounds = addSetting(new BooleanSetting("UI Sounds", "Tıklama ses efektleri", true));

    public ClientTheme() {
        super("Client Theme", "İstemci ve Click GUI görünümünü, vurgu renklerini ve animasyonlarını özelleştirir.",
                Category.SETTINGS, 0, CompatibilityStatus.SUPPORTED, false, true);
    }

    public int getAccentColor() {
        if (accent.is("Purple")) {
            return 0xFF8E24AA;
        } else if (accent.is("Cyan")) {
            return 0xFF00E5FF;
        }
        return 0xFFE53935; // Crimson default
    }

    public ModeSetting getAccent() { return accent; }
    public ModeSetting getAnimationSpeed() { return animationSpeed; }
    public boolean isBlurBackground() { return blurBackground.getValue(); }
    public boolean isUiSounds() { return uiSounds.getValue(); }
}
