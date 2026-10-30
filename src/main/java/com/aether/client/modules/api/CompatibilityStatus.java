package com.aether.client.modules.api;

/**
 * Server and environment compatibility classification for modules.
 */
public enum CompatibilityStatus {
    SUPPORTED("Supported", 0xFF4CAF50, "Tüm sunucularda ve yerel oyun ortamında sorunsuz çalışır."),
    LOCAL_ONLY("Local Only", 0xFF2196F3, "Yalnızca tek oyunculu veya yerel test ortamında çalışır."),
    REQUIRES_SERVER_PERMISSION("Requires Server Permission", 0xFFFF9800, "Sunucudan yetki (örn. OP, izin belgesi) gerektirir."),
    REQUIRES_SERVER_COMPONENT("Requires Server Component", 0xFF9C27B0, "Sunucuda özel yardımcı eklenti veya mod gerektirir."),
    UNSUPPORTED("Unsupported", 0xFFF44336, "Mevcut sunucu ortamında kullanılamaz.");

    private final String label;
    private final int color;
    private final String tooltip;

    CompatibilityStatus(String label, int color, String tooltip) {
        this.label = label;
        this.color = color;
        this.tooltip = tooltip;
    }

    public String getLabel() {
        return label;
    }

    public int getColor() {
        return color;
    }

    public String getTooltip() {
        return tooltip;
    }
}
