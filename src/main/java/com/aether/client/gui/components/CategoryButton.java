package com.aether.client.gui.components;

import com.aether.client.gui.animations.Animation;
import com.aether.client.modules.api.Category;

/**
 * Sidebar button representing a module category.
 */
public class CategoryButton extends GuiComponent {
    private final Category category;
    private boolean selected;
    private final Animation selectAnimation = new Animation(180, 0.0);

    public CategoryButton(double x, double y, double width, double height, Category category) {
        super(x, y, width, height);
        this.category = category;
    }

    public Category getCategory() {
        return category;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
        this.selectAnimation.animateTo(selected ? 1.0 : 0.0);
    }

    public double getAnimationProgress() {
        return selectAnimation.getValue();
    }
}
