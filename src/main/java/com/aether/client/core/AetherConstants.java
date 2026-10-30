package com.aether.client.core;

/**
 * Global constants and branding parameters for Aether Client.
 */
public final class AetherConstants {
    private AetherConstants() {}

    public static final String CLIENT_NAME = "Aether Client";
    public static final String CLIENT_VERSION = "1.0.0-PRO";
    public static final String TARGET_MINECRAFT = "1.21.10";
    public static final String TARGET_FORGE = "1.21.10-60.1.18";

    // Theme Colors (ARGB 32-bit packed ints)
    public static final int COLOR_BG_DARKEST = 0xFF0B0C0E;
    public static final int COLOR_BG_PRIMARY = 0xFF121316;
    public static final int COLOR_BG_SECONDARY = 0xFF1A1C23;
    public static final int COLOR_BG_CARD = 0xFF21242D;
    public static final int COLOR_BG_CARD_HOVER = 0xFF292D38;
    public static final int COLOR_BORDER = 0xFF2E3340;
    public static final int COLOR_BORDER_FOCUSED = 0xFF434A5C;

    // Accent Colors
    public static final int COLOR_ACCENT_CRIMSON = 0xFFE53935;
    public static final int COLOR_ACCENT_PURPLE = 0xFF8E24AA;
    public static final int COLOR_ACCENT_CYAN = 0xFF00E5FF;

    // Text & Status
    public static final int COLOR_TEXT_PRIMARY = 0xFFF5F5F7;
    public static final int COLOR_TEXT_SECONDARY = 0xFF9E9E9E;
    public static final int COLOR_TEXT_MUTED = 0xFF616161;
    public static final int COLOR_SUCCESS = 0xFF4CAF50;
    public static final int COLOR_WARNING = 0xFFFFC107;
    public static final int COLOR_ERROR = 0xFFE53935;

    // Directories
    public static final String CONFIG_DIR_NAME = "aether";
    public static final String PROFILES_DIR_NAME = "profiles";
    public static final String WAYPOINTS_DIR_NAME = "waypoints";
}
