package com.aether.client.modules.settings;

/**
 * Numeric slider setting supporting bounds, steps, and formatting.
 */
public class NumberSetting extends Setting<Double> {
    private final double min;
    private final double max;
    private final double step;
    private final String suffix;

    public NumberSetting(String name, String description, double defaultValue, double min, double max, double step, String suffix) {
        super(name, description, clamp(defaultValue, min, max));
        this.min = min;
        this.max = max;
        this.step = step;
        this.suffix = suffix != null ? suffix : "";
    }

    public NumberSetting(String name, String description, double defaultValue, double min, double max, double step) {
        this(name, description, defaultValue, min, max, step, "");
    }

    private static double clamp(double val, double min, double max) {
        return Math.max(min, Math.min(max, val));
    }

    @Override
    public void setValue(Double value) {
        if (value == null) return;
        double stepped = Math.round(value / step) * step;
        super.setValue(clamp(stepped, min, max));
    }

    public double getMin() {
        return min;
    }

    public double getMax() {
        return max;
    }

    public double getStep() {
        return step;
    }

    public String getSuffix() {
        return suffix;
    }

    public int getValueInt() {
        return getValue().intValue();
    }

    public float getValueFloat() {
        return getValue().floatValue();
    }

    @Override
    public String getValueAsString() {
        if (step >= 1.0) {
            return String.valueOf(getValueInt()) + suffix;
        }
        return String.format(java.util.Locale.US, "%.2f%s", getValue(), suffix);
    }

    @Override
    public void parseAndSet(String stringValue) {
        try {
            String sanitized = stringValue.replace(suffix, "").trim();
            setValue(Double.parseDouble(sanitized));
        } catch (NumberFormatException ignored) {}
    }
}
