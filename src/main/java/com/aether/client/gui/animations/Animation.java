package com.aether.client.gui.animations;

/**
 * High-precision animation interpolator for UI transitions and smooth state morphing.
 */
public class Animation {
    public enum Easing {
        LINEAR,
        EASE_OUT_CUBIC,
        EASE_IN_OUT_QUAD
    }

    private final long durationMs;
    private final Easing easing;
    private long startTime;
    private double fromValue;
    private double targetValue;
    private double currentValue;

    public Animation(long durationMs, double initialValue, Easing easing) {
        this.durationMs = durationMs;
        this.fromValue = initialValue;
        this.targetValue = initialValue;
        this.currentValue = initialValue;
        this.easing = easing;
        this.startTime = System.currentTimeMillis();
    }

    public Animation(long durationMs, double initialValue) {
        this(durationMs, initialValue, Easing.EASE_OUT_CUBIC);
    }

    public void animateTo(double newTarget) {
        if (Math.abs(this.targetValue - newTarget) < 0.0001) return;
        this.fromValue = this.currentValue;
        this.targetValue = newTarget;
        this.startTime = System.currentTimeMillis();
    }

    public double getValue() {
        long elapsed = System.currentTimeMillis() - startTime;
        if (elapsed >= durationMs) {
            this.currentValue = targetValue;
            return targetValue;
        }

        double progress = (double) elapsed / durationMs;
        double factor = calculateEasing(progress);
        this.currentValue = fromValue + (targetValue - fromValue) * factor;
        return this.currentValue;
    }

    private double calculateEasing(double t) {
        switch (easing) {
            case EASE_OUT_CUBIC:
                return 1.0 - Math.pow(1.0 - t, 3);
            case EASE_IN_OUT_QUAD:
                return t < 0.5 ? 2.0 * t * t : 1.0 - Math.pow(-2.0 * t + 2.0, 2) / 2.0;
            case LINEAR:
            default:
                return t;
        }
    }

    public boolean isFinished() {
        return System.currentTimeMillis() - startTime >= durationMs;
    }

    public double getTargetValue() {
        return targetValue;
    }
}
