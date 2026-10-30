package com.aether.client.modules.api;

/**
 * Standard categories for Aether Client modules.
 */
public enum Category {
    VISUALS("Visuals", "Görsel ve HUD göstergeleri, ESP ve aydınlatma"),
    MOVEMENT("Movement", "Hareket analizi, hız göstergeleri ve asistanlar"),
    COMBAT("Combat", "İzinli savaş analitiği, CPS sayacı ve hedef göstergeleri"),
    WORLD("World", "Dünya verileri, chunk sınırları ve waypoint yöneticisi"),
    PLAYER("Player", "Oyuncu istatistikleri, envanter HUD ve zırh durumu"),
    UTILITY("Utility", "Tuş vuruşları, oturum analitiği ve yardımcı araçlar"),
    SETTINGS("Settings", "İstemci teması, GUI ayarları ve yetki paneli");

    private final String displayName;
    private final String description;

    Category(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }
}
