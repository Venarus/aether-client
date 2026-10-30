package com.aether.client.events;

/**
 * Event that supports cancellation to prevent subsequent processing.
 */
public abstract class CancellableEvent extends Event {
    private boolean cancelled = false;

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public void cancel() {
        this.cancelled = true;
    }
}
