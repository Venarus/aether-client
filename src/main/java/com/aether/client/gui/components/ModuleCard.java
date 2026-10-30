package com.aether.client.gui.components;

import com.aether.client.gui.animations.Animation;
import com.aether.client.modules.api.Module;

/**
 * Interactive card displaying a module in the central list.
 * Supports toggling, keybinding, selection for settings inspection, and smooth switch animations.
 */
public class ModuleCard extends GuiComponent {
    private final Module module;
    private boolean selected;
    private boolean listeningForKey = false;
    private final Animation toggleAnimation = new Animation(200, 0.0);

    public ModuleCard(double x, double y, double width, double height, Module module) {
        super(x, y, width, height);
        this.module = module;
        this.toggleAnimation.animateTo(module.isEnabled() ? 1.0 : 0.0);
    }

    public Module getModule() {
        return module;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isListeningForKey() {
        return listeningForKey;
    }

    public void setListeningForKey(boolean listening) {
        this.listeningForKey = listening;
    }

    public double getToggleProgress() {
        // Sync animation with module state
        if (module.isEnabled() && toggleAnimation.getTargetValue() < 0.5) {
            toggleAnimation.animateTo(1.0);
        } else if (!module.isEnabled() && toggleAnimation.getTargetValue() > 0.5) {
            toggleAnimation.animateTo(0.0);
        }
        return toggleAnimation.getValue();
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (!isMouseOver(mouseX, mouseY)) return false;

        if (button == 0) { // Left click: Toggle module
            module.toggle();
            toggleAnimation.animateTo(module.isEnabled() ? 1.0 : 0.0);
            return true;
        } else if (button == 1) { // Right click: Select for settings
            setSelected(true);
            return true;
        } else if (button == 2) { // Middle click: Start keybind listening
            setListeningForKey(!isListeningForKey());
            return true;
        }
        return false;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (listeningForKey) {
            // GLFW_KEY_ESCAPE = 256, GLFW_KEY_DELETE = 261, GLFW_KEY_BACKSPACE = 259 -> clear keybind
            if (keyCode == 256 || keyCode == 261 || keyCode == 259) {
                module.setKey(0);
            } else {
                module.setKey(keyCode);
            }
            listeningForKey = false;
            return true;
        }
        return false;
    }
}
