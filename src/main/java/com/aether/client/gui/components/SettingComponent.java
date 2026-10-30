package com.aether.client.gui.components;

import com.aether.client.modules.settings.*;

/**
 * Interactive setting row component rendered in the right-side configuration panel.
 */
public class SettingComponent extends GuiComponent {
    private final Setting<?> setting;
    private boolean draggingSlider = false;
    private boolean listeningForKey = false;

    public SettingComponent(double x, double y, double width, double height, Setting<?> setting) {
        super(x, y, width, height);
        this.setting = setting;
    }

    public Setting<?> getSetting() {
        return setting;
    }

    public boolean isListeningForKey() {
        return listeningForKey;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (!isMouseOver(mouseX, mouseY) || !setting.isVisible()) return false;

        if (setting instanceof BooleanSetting) {
            if (button == 0) {
                ((BooleanSetting) setting).toggle();
                return true;
            }
        } else if (setting instanceof NumberSetting) {
            if (button == 0) {
                this.draggingSlider = true;
                updateSliderValue(mouseX);
                return true;
            }
        } else if (setting instanceof ModeSetting) {
            ModeSetting ms = (ModeSetting) setting;
            if (button == 0) {
                ms.cycle();
                return true;
            } else if (button == 1) {
                ms.cycleBack();
                return true;
            }
        } else if (setting instanceof ColorSetting) {
            ColorSetting cs = (ColorSetting) setting;
            if (button == 0) {
                cs.setRainbow(!cs.isRainbow());
                return true;
            }
        } else if (setting instanceof KeybindSetting) {
            if (button == 0) {
                this.listeningForKey = !this.listeningForKey;
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (button == 0 && draggingSlider) {
            this.draggingSlider = false;
            return true;
        }
        return false;
    }

    public void updateSlider(double mouseX) {
        if (draggingSlider && setting instanceof NumberSetting) {
            updateSliderValue(mouseX);
        }
    }

    private void updateSliderValue(double mouseX) {
        NumberSetting ns = (NumberSetting) setting;
        // Slider bar takes the right half of the component
        double sliderStartX = x + width * 0.45;
        double sliderWidth = width * 0.5;

        double fraction = Math.max(0.0, Math.min(1.0, (mouseX - sliderStartX) / sliderWidth));
        double targetVal = ns.getMin() + (ns.getMax() - ns.getMin()) * fraction;
        ns.setValue(targetVal);
    }

    public double getSliderFraction() {
        if (setting instanceof NumberSetting) {
            NumberSetting ns = (NumberSetting) setting;
            double range = ns.getMax() - ns.getMin();
            if (range <= 0.0) return 0.0;
            return (ns.getValue() - ns.getMin()) / range;
        }
        return 0.0;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (listeningForKey && setting instanceof KeybindSetting) {
            if (keyCode == 256 || keyCode == 261 || keyCode == 259) { // Escape / Delete / Backspace
                ((KeybindSetting) setting).setValue(0);
            } else {
                ((KeybindSetting) setting).setValue(keyCode);
            }
            this.listeningForKey = false;
            return true;
        }
        return false;
    }
}
