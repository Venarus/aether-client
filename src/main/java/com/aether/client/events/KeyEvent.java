package com.aether.client.events;

/**
 * Dispatched on GLFW keyboard input.
 */
public class KeyEvent extends CancellableEvent {
    public static final int ACTION_RELEASE = 0;
    public static final int ACTION_PRESS = 1;
    public static final int ACTION_REPEAT = 2;

    private final int key;
    private final int scanCode;
    private final int action;
    private final int modifiers;

    public KeyEvent(int key, int scanCode, int action, int modifiers) {
        this.key = key;
        this.scanCode = scanCode;
        this.action = action;
        this.modifiers = modifiers;
    }

    public int getKey() {
        return key;
    }

    public int getScanCode() {
        return scanCode;
    }

    public int getAction() {
        return action;
    }

    public int getModifiers() {
        return modifiers;
    }

    public boolean isPress() {
        return action == ACTION_PRESS;
    }

    public boolean isRelease() {
        return action == ACTION_RELEASE;
    }

    public boolean isRepeat() {
        return action == ACTION_REPEAT;
    }
}
