package com.aether.client.events;

/**
 * Base class for all internal Aether Client events.
 */
public abstract class Event {
    private final long timestamp = System.currentTimeMillis();

    public long getTimestamp() {
        return timestamp;
    }
}
