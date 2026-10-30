package com.aether.client.events;

/**
 * Dispatched when the 2D HUD or overlay is being rendered.
 * Holds partial ticks for smooth interpolation and screen dimensions.
 */
public class Render2DEvent extends Event {
    private final float partialTicks;
    private final int screenWidth;
    private final int screenHeight;

    public Render2DEvent(float partialTicks, int screenWidth, int screenHeight) {
        this.partialTicks = partialTicks;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
    }

    public float getPartialTicks() {
        return partialTicks;
    }

    public int getScreenWidth() {
        return screenWidth;
    }

    public int getScreenHeight() {
        return screenHeight;
    }
}
