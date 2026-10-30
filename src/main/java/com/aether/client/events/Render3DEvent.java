package com.aether.client.events;

/**
 * Dispatched when the 3D in-game world is being rendered.
 * Holds partial ticks and camera coordinates for world-space calculations.
 */
public class Render3DEvent extends Event {
    private final float partialTicks;
    private final double cameraX;
    private final double cameraY;
    private final double cameraZ;

    public Render3DEvent(float partialTicks, double cameraX, double cameraY, double cameraZ) {
        this.partialTicks = partialTicks;
        this.cameraX = cameraX;
        this.cameraY = cameraY;
        this.cameraZ = cameraZ;
    }

    public float getPartialTicks() {
        return partialTicks;
    }

    public double getCameraX() {
        return cameraX;
    }

    public double getCameraY() {
        return cameraY;
    }

    public double getCameraZ() {
        return cameraZ;
    }
}
