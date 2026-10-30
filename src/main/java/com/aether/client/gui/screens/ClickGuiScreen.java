package com.aether.client.gui.screens;

import com.aether.client.core.AetherClient;
import com.aether.client.core.AetherConstants;
import com.aether.client.gui.animations.Animation;
import com.aether.client.gui.components.*;
import com.aether.client.modules.api.Category;
import com.aether.client.modules.api.Module;
import com.aether.client.modules.settings.ClientTheme;
import com.aether.client.modules.settings.Setting;

import java.util.ArrayList;
import java.util.List;

/**
 * Modern, high-performance Click GUI screen for Aether Client.
 * Features an anthracite dark theme, crimson/purple accents, 3-column layout,
 * live search, smooth animations, scrolling, and interactive settings editing.
 */
public class ClickGuiScreen {
    private double windowX = 100;
    private double windowY = 60;
    private double windowWidth = 820;
    private double windowHeight = 520;

    private boolean draggingWindow = false;
    private double dragOffsetX, dragOffsetY;

    private Category selectedCategory = Category.VISUALS;
    private Module selectedModule = null;

    private final SearchBar searchBar;
    private final List<CategoryButton> categoryButtons = new ArrayList<>();
    private final List<ModuleCard> moduleCards = new ArrayList<>();
    private final List<SettingComponent> settingComponents = new ArrayList<>();

    private double middleScroll = 0;
    private double rightScroll = 0;

    private final Animation openAnimation = new Animation(220, 0.0);

    public ClickGuiScreen() {
        this.searchBar = new SearchBar(windowX + 220, windowY + 20, 280, 32, "Modülleri filtrele...");
        initCategories();
        rebuildModuleCards();
        openAnimation.animateTo(1.0);
    }

    private void initCategories() {
        categoryButtons.clear();
        double startY = windowY + 70;
        double btnHeight = 36;
        for (Category cat : Category.values()) {
            CategoryButton btn = new CategoryButton(windowX + 16, startY, 180, btnHeight, cat);
            if (cat == selectedCategory) {
                btn.setSelected(true);
            }
            categoryButtons.add(btn);
            startY += btnHeight + 6;
        }
    }

    public void rebuildModuleCards() {
        moduleCards.clear();
        if (AetherClient.getInstance() == null || AetherClient.getInstance().getModuleManager() == null) {
            return;
        }

        String search = searchBar.getText().trim();
        List<Module> list = search.isEmpty()
                ? AetherClient.getInstance().getModuleManager().getModulesByCategory(selectedCategory)
                : AetherClient.getInstance().getModuleManager().search(search);

        double cardY = windowY + 68 + middleScroll;
        double cardW = 280;
        double cardH = 48;

        for (Module m : list) {
            ModuleCard card = new ModuleCard(windowX + 220, cardY, cardW, cardH, m);
            if (m == selectedModule) {
                card.setSelected(true);
            }
            moduleCards.add(card);
            cardY += cardH + 8;
        }

        if (selectedModule == null && !list.isEmpty()) {
            selectModule(list.get(0));
        } else if (selectedModule != null) {
            rebuildSettingComponents();
        }
    }

    public void selectModule(Module module) {
        this.selectedModule = module;
        for (ModuleCard card : moduleCards) {
            card.setSelected(card.getModule() == module);
        }
        rebuildSettingComponents();
    }

    public void rebuildSettingComponents() {
        settingComponents.clear();
        if (selectedModule == null) return;

        double setY = windowY + 90 + rightScroll;
        double setW = 280;
        double setH = 34;

        for (Setting<?> s : selectedModule.getSettings()) {
            if (s.isVisible()) {
                settingComponents.add(new SettingComponent(windowX + 520, setY, setW, setH, s));
                setY += setH + 6;
            }
        }
    }

    public void mouseClicked(double mouseX, double mouseY, int button) {
        // Window header dragging
        if (button == 0 && mouseX >= windowX && mouseX <= windowX + windowWidth && mouseY >= windowY && mouseY <= windowY + 50) {
            if (!searchBar.isMouseOver(mouseX, mouseY)) {
                this.draggingWindow = true;
                this.dragOffsetX = mouseX - windowX;
                this.dragOffsetY = mouseY - windowY;
                return;
            }
        }

        // Search bar
        if (searchBar.mouseClicked(mouseX, mouseY, button)) {
            rebuildModuleCards();
            return;
        }

        // Categories
        for (CategoryButton btn : categoryButtons) {
            if (btn.isMouseOver(mouseX, mouseY) && button == 0) {
                for (CategoryButton b : categoryButtons) b.setSelected(false);
                btn.setSelected(true);
                this.selectedCategory = btn.getCategory();
                searchBar.clear();
                this.middleScroll = 0;
                rebuildModuleCards();
                return;
            }
        }

        // Module cards
        for (ModuleCard card : moduleCards) {
            if (card.mouseClicked(mouseX, mouseY, button)) {
                if (button == 1) { // Right click selects module for settings
                    selectModule(card.getModule());
                }
                return;
            }
        }

        // Setting components
        for (SettingComponent sc : settingComponents) {
            if (sc.mouseClicked(mouseX, mouseY, button)) {
                return;
            }
        }
    }

    public void mouseReleased(double mouseX, double mouseY, int button) {
        if (button == 0) {
            this.draggingWindow = false;
        }
        for (SettingComponent sc : settingComponents) {
            sc.mouseReleased(mouseX, mouseY, button);
        }
    }

    public void mouseMoved(double mouseX, double mouseY) {
        if (draggingWindow) {
            this.windowX = mouseX - dragOffsetX;
            this.windowY = mouseY - dragOffsetY;
            updateLayoutPositions();
            return;
        }

        searchBar.updateHover(mouseX, mouseY);
        for (CategoryButton btn : categoryButtons) btn.updateHover(mouseX, mouseY);
        for (ModuleCard card : moduleCards) card.updateHover(mouseX, mouseY);
        for (SettingComponent sc : settingComponents) {
            sc.updateHover(mouseX, mouseY);
            sc.updateSlider(mouseX);
        }
    }

    public void mouseScrolled(double mouseX, double mouseY, double amount) {
        if (mouseX >= windowX + 210 && mouseX <= windowX + 510) {
            middleScroll += amount * 20.0;
            if (middleScroll > 0) middleScroll = 0;
            rebuildModuleCards();
        } else if (mouseX >= windowX + 515 && mouseX <= windowX + windowWidth) {
            rightScroll += amount * 20.0;
            if (rightScroll > 0) rightScroll = 0;
            rebuildSettingComponents();
        }
    }

    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        // ESC = 256
        if (keyCode == 256) {
            if (searchBar.isFocused()) {
                searchBar.setFocused(false);
                return true;
            }
            AetherClient.getInstance().setClickGuiOpen(false);
            return true;
        }

        // Forward to search bar
        if (searchBar.keyPressed(keyCode, scanCode, modifiers)) {
            rebuildModuleCards();
            return true;
        }

        // Forward to module cards (for keybinding)
        for (ModuleCard card : moduleCards) {
            if (card.keyPressed(keyCode, scanCode, modifiers)) {
                return true;
            }
        }

        // Forward to setting components
        for (SettingComponent sc : settingComponents) {
            if (sc.keyPressed(keyCode, scanCode, modifiers)) {
                return true;
            }
        }

        return false;
    }

    public boolean charTyped(char codePoint, int modifiers) {
        if (searchBar.charTyped(codePoint, modifiers)) {
            rebuildModuleCards();
            return true;
        }
        return false;
    }

    private void updateLayoutPositions() {
        searchBar.setX(windowX + 220);
        searchBar.setY(windowY + 20);

        double startY = windowY + 70;
        for (CategoryButton btn : categoryButtons) {
            btn.setX(windowX + 16);
            btn.setY(startY);
            startY += btn.getHeight() + 6;
        }
        rebuildModuleCards();
    }

    public int getAccentColor() {
        if (AetherClient.getInstance() != null && AetherClient.getInstance().getModuleManager() != null) {
            ClientTheme theme = AetherClient.getInstance().getModuleManager().getModule(ClientTheme.class);
            if (theme != null) {
                return theme.getAccentColor();
            }
        }
        return AetherConstants.COLOR_ACCENT_CRIMSON;
    }

    public double getWindowX() { return windowX; }
    public double getWindowY() { return windowY; }
    public double getWindowWidth() { return windowWidth; }
    public double getWindowHeight() { return windowHeight; }
    public Category getSelectedCategory() { return selectedCategory; }
    public Module getSelectedModule() { return selectedModule; }
    public SearchBar getSearchBar() { return searchBar; }
    public List<CategoryButton> getCategoryButtons() { return categoryButtons; }
    public List<ModuleCard> getModuleCards() { return moduleCards; }
    public List<SettingComponent> getSettingComponents() { return settingComponents; }
}
