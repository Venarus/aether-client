package com.aether.client.events;

/**
 * Dispatched on every client game tick (20 ticks/sec).
 */
public class ClientTickEvent extends Event {
    public enum Phase {
        START,
        END
    }

    private final Phase phase;
    private final int tickCount;

    public ClientTickEvent(Phase phase, int tickCount) {
        this.phase = phase;
        this.tickCount = tickCount;
    }

    public Phase getPhase() {
        return phase;
    }

    public int getTickCount() {
        return tickCount;
    }

    public boolean isStart() {
        return phase == Phase.START;
    }

    public boolean isEnd() {
        return phase == Phase.END;
    }
}
