package com.aether.client.gui.screens;

import com.aether.client.core.AetherClient;
import com.aether.client.core.AetherConstants;
import com.aether.client.modules.api.Category;
import com.aether.client.modules.api.Module;
import com.aether.client.modules.settings.*;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.CharacterEvent;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Modern, adaptive, high-performance Click GUI screen for Aether Client.
 * Automatically adapts to any Minecraft resolution and GUI scale, providing
 * intuitive category filtering, live search, module toggling, keybinding,
 * and interactive settings manipulation (sliders, checkboxes, mode cycles).
 */
public class AetherForgeScreen extends Screen {
    private Category selectedCategory = Category.MOVEMENT;
    private Module selectedModule = null;

    private String searchText = "";
    private boolean searchFocused = false;
    private boolean listeningForKey = false;

    private int middleScroll = 0;
    private int rightScroll = 0;

    private NumberSetting draggingSlider = null;
    private int sliderTrackX = 0, sliderTrackW = 100;

    public AetherForgeScreen() {
        super(Component.literal("Aether Client"));
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    protected void init() {
        super.init();
        AetherClient.getInstance().setClickGuiOpen(true);
        // Default select first module of selected category
        selectFirstModule();
    }

    private void selectFirstModule() {
        List<Module> mods = getFilteredModules();
        if (!mods.isEmpty()) {
            if (selectedModule == null || !mods.contains(selectedModule)) {
                selectedModule = mods.get(0);
            }
        } else {
            selectedModule = null;
        }
    }

    @Override
    public void removed() {
        super.removed();
        AetherClient.getInstance().setClickGuiOpen(false);
        this.listeningForKey = false;
        this.searchFocused = false;
        this.draggingSlider = null;
    }

    private List<Module> getFilteredModules() {
        if (AetherClient.getInstance() == null || AetherClient.getInstance().getModuleManager() == null) {
            return new ArrayList<>();
        }
        String q = searchText.trim().toLowerCase();
        if (!q.isEmpty()) {
            return AetherClient.getInstance().getModuleManager().search(q);
        }
        return AetherClient.getInstance().getModuleManager().getModulesByCategory(selectedCategory);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        // 1. In-game background darkening
        graphics.fill(0, 0, this.width, this.height, 0x8A000000);

        // 2. Responsive Geometry (strictly scaled and centered)
        int ww = Math.min(this.width - 20, 480);
        int wh = Math.min(this.height - 20, 270);
        int wx = (this.width - ww) / 2;
        int wy = (this.height - wh) / 2;

        int accent = AetherClient.getInstance().getAccentColor();

        // Window background (Dark Anthracite #0D0E12)
        graphics.fill(wx, wy, wx + ww, wy + wh, 0xF50D0E12);

        // Window borders
        graphics.fill(wx, wy, wx + ww, wy + 2, accent | 0xFF000000); // Crimson accent top
        graphics.fill(wx, wy + 2, wx + 1, wy + wh, 0x25FFFFFF);
        graphics.fill(wx + ww - 1, wy + 2, wx + ww, wy + wh, 0x25FFFFFF);
        graphics.fill(wx, wy + wh - 1, wx + ww, wy + wh, 0x25FFFFFF);

        // Title Header (Height 24px)
        graphics.drawString(this.font, "§lAETHER §fCLIENT", wx + 10, wy + 8, accent | 0xFF000000, true);
        graphics.drawString(this.font, "§7v" + AetherConstants.CLIENT_VERSION, wx + 108, wy + 8, 0xFF888888, false);

        // Close Button [X]
        int closeX = wx + ww - 18;
        int closeY = wy + 7;
        boolean closeHovered = mouseX >= closeX && mouseX <= closeX + 12 && mouseY >= closeY && mouseY <= closeY + 12;
        graphics.drawString(this.font, "✕", closeX, closeY, closeHovered ? 0xFFFF4444 : 0xFF888888, false);

        // Header separator
        graphics.fill(wx, wy + 24, wx + ww, wy + 25, 0x20FFFFFF);

        // Layout Columns:
        // Col 1: Categories (width 96px)
        // Col 2: Modules (width 174px)
        // Col 3: Settings (remaining width)
        int catW = 96;
        int catX = wx;
        int catY = wy + 25;
        int catH = wh - 25;

        int modW = 174;
        int modX = catX + catW + 1;
        int modY = wy + 25;
        int modH = wh - 25;

        int setX = modX + modW + 1;
        int setY = wy + 25;
        int setW = ww - (setX - wx);
        int setH = wh - 25;

        // Dividers
        graphics.fill(catX + catW, catY, catX + catW + 1, catY + catH, 0x1AFFFFFF);
        graphics.fill(modX + modW, modY, modX + modW + 1, modY + modH, 0x1AFFFFFF);

        // === 1. Render Categories ===
        Category[] categories = Category.values();
        int catBtnH = 24;
        int currCatY = catY + 6;
        for (Category cat : categories) {
            boolean isSelected = (cat == selectedCategory && searchText.isEmpty());
            boolean isHovered = mouseX >= catX && mouseX < catX + catW && mouseY >= currCatY && mouseY < currCatY + catBtnH;

            if (isSelected) {
                graphics.fill(catX, currCatY, catX + catW, currCatY + catBtnH, 0x25FFFFFF);
                graphics.fill(catX, currCatY, catX + 3, currCatY + catBtnH, accent | 0xFF000000);
            } else if (isHovered) {
                graphics.fill(catX, currCatY, catX + catW, currCatY + catBtnH, 0x10FFFFFF);
            }

            int catColor = isSelected ? (accent | 0xFF000000) : (isHovered ? 0xFFFFFFFF : 0xFFAAAAAA);
            graphics.drawString(this.font, cat.getDisplayName(), catX + 10, currCatY + 7, catColor, false);
            currCatY += catBtnH + 2;
        }

        // === 2. Render Middle Column (Search + Modules) ===
        // Search Bar
        int sbX = modX + 6;
        int sbY = modY + 6;
        int sbW = modW - 12;
        int sbH = 18;
        graphics.fill(sbX, sbY, sbX + sbW, sbY + sbH, searchFocused ? 0xFF1C1E26 : 0xFF12141A);
        graphics.fill(sbX, sbY + sbH - 1, sbX + sbW, sbY + sbH, searchFocused ? (accent | 0xFF000000) : 0x30FFFFFF);

        if (searchText.isEmpty() && !searchFocused) {
            graphics.drawString(this.font, "§7Filtrele...", sbX + 6, sbY + 5, 0xFF666666, false);
        } else {
            String cursor = (searchFocused && (System.currentTimeMillis() / 450 % 2 == 0)) ? "§f|" : "";
            graphics.drawString(this.font, searchText + cursor, sbX + 6, sbY + 5, 0xFFFFFFFF, false);
        }

        // Module List (Scrollable Area)
        int listY = sbY + sbH + 6;
        int listH = modH - (listY - modY) - 6;
        List<Module> modules = getFilteredModules();

        // Enable scissor
        safeScissor(graphics, modX + 2, listY, modX + modW - 2, listY + listH);

        int cardH = 26;
        int cardY = listY + middleScroll;

        for (Module m : modules) {
            if (cardY + cardH >= listY && cardY <= listY + listH) {
                boolean isSelected = (m == selectedModule);
                boolean isHovered = mouseX >= modX + 4 && mouseX <= modX + modW - 4 && mouseY >= cardY && mouseY <= cardY + cardH;

                int cardBg = isSelected ? 0xFF242734 : (isHovered ? 0xFF1B1D26 : 0xFF13151D);
                graphics.fill(modX + 4, cardY, modX + modW - 4, cardY + cardH, cardBg);
                if (isSelected) {
                    graphics.fill(modX + 4, cardY, modX + 6, cardY + cardH, accent | 0xFF000000);
                }

                // Module Name
                int nameColor = m.isEnabled() ? 0xFFFFFFFF : 0xFF888888;
                graphics.drawString(this.font, m.getName(), modX + 10, cardY + 8, nameColor, false);

                // Toggle pill [ON / OFF]
                int pillW = 28;
                int pillH = 14;
                int pillX = modX + modW - 4 - pillW - 6;
                int pillY = cardY + 6;
                int pillBg = m.isEnabled() ? 0xFF00E676 : 0xFF353842;
                graphics.fill(pillX, pillY, pillX + pillW, pillY + pillH, pillBg);

                String pText = m.isEnabled() ? "ON" : "OFF";
                int ptw = this.font.width(pText);
                graphics.drawString(this.font, pText, pillX + (pillW - ptw) / 2, pillY + 3, m.isEnabled() ? 0xFF000000 : 0xFFFFFFFF, false);

                // Keybind badge
                if (m.getKey() > 0) {
                    String kStr = "[" + AetherClient.getInstance().getKeybindManager().getKeyName(m.getKey()) + "]";
                    int kw = this.font.width(kStr);
                    graphics.drawString(this.font, kStr, pillX - kw - 4, cardY + 8, 0xFF666666, false);
                }
            }
            cardY += cardH + 3;
        }
        graphics.disableScissor();

        // === 3. Render Right Column (Selected Module & Settings) ===
        if (selectedModule != null) {
            int cx = setX + 10;
            int cy = setY + 8;
            int cw = setW - 20;

            // Module Header
            graphics.drawString(this.font, "§l" + selectedModule.getName().toUpperCase(), cx, cy, accent | 0xFF000000, true);
            graphics.drawString(this.font, "§7" + selectedModule.getCategory().getDisplayName(), cx, cy + 13, 0xFF888888, false);

            // Description
            String desc = selectedModule.getDescription();
            if (this.font.width(desc) > cw) {
                desc = desc.substring(0, Math.min(desc.length(), 35)) + "...";
            }
            graphics.drawString(this.font, "§8" + desc, cx, cy + 26, 0xFF777777, false);

            // Big Toggle Button
            int btnToggleY = cy + 42;
            int btnToggleH = 20;
            boolean btnHovered = mouseX >= cx && mouseX <= cx + cw && mouseY >= btnToggleY && mouseY <= btnToggleY + btnToggleH;
            int btnBg = selectedModule.isEnabled() ? 0xFFD32F2F : 0xFF2E7D32;
            if (btnHovered) btnBg = selectedModule.isEnabled() ? 0xFFE53935 : 0xFF388E3C;
            graphics.fill(cx, btnToggleY, cx + cw, btnToggleY + btnToggleH, btnBg);

            String btnText = selectedModule.isEnabled() ? "DEVRE DIŞI BIRAK" : "ETKİNLEŞTİR";
            int btw = this.font.width(btnText);
            graphics.drawString(this.font, "§l" + btnText, cx + (cw - btw) / 2, btnToggleY + 6, 0xFFFFFFFF, true);

            // Keybind Button
            int btnKeyY = btnToggleY + btnToggleH + 5;
            int btnKeyH = 18;
            boolean keyHovered = mouseX >= cx && mouseX <= cx + cw && mouseY >= btnKeyY && mouseY <= btnKeyY + btnKeyH;
            graphics.fill(cx, btnKeyY, cx + cw, btnKeyY + btnKeyH, listeningForKey ? 0xFF4527A0 : (keyHovered ? 0xFF252934 : 0xFF181A22));

            String keyLabel = listeningForKey ? "§e[ Bir tuşa basınız... ]" : "§7Kısayol: §f" + AetherClient.getInstance().getKeybindManager().getKeyName(selectedModule.getKey());
            int klw = this.font.width(keyLabel);
            graphics.drawString(this.font, keyLabel, cx + (cw - klw) / 2, btnKeyY + 5, 0xFFFFFFFF, false);

            // Settings Header
            int sHeaderY = btnKeyY + btnKeyH + 8;
            graphics.fill(cx, sHeaderY, cx + cw, sHeaderY + 1, 0x20FFFFFF);
            graphics.drawString(this.font, "§7AYARLAR", cx, sHeaderY + 4, 0xFF666666, false);

            // Settings Scrollable Area
            int sAreaY = sHeaderY + 15;
            int sAreaH = setH - (sAreaY - setY) - 6;

            safeScissor(graphics, cx - 2, sAreaY, cx + cw + 2, sAreaY + sAreaH);
            int sy = sAreaY + rightScroll;

            for (Setting<?> s : selectedModule.getSettings()) {
                if (!s.isVisible()) continue;

                if (s instanceof BooleanSetting bs) {
                    int rH = 20;
                    boolean h = mouseX >= cx && mouseX <= cx + cw && mouseY >= sy && mouseY <= sy + rH;
                    graphics.fill(cx, sy, cx + cw, sy + rH, h ? 0xFF1C1E26 : 0xFF14161C);
                    graphics.drawString(this.font, bs.getName(), cx + 6, sy + 6, 0xFFCCCCCC, false);

                    // Checkbox
                    int boxX = cx + cw - 18;
                    int boxY = sy + 4;
                    graphics.fill(boxX, boxY, boxX + 12, boxY + 12, bs.getValue() ? (accent | 0xFF000000) : 0xFF353842);
                    if (bs.getValue()) {
                        graphics.drawString(this.font, "✓", boxX + 2, boxY + 2, 0xFFFFFFFF, false);
                    }
                    sy += rH + 4;
                } else if (s instanceof NumberSetting ns) {
                    int rH = 28;
                    boolean h = mouseX >= cx && mouseX <= cx + cw && mouseY >= sy && mouseY <= sy + rH;
                    graphics.fill(cx, sy, cx + cw, sy + rH, h ? 0xFF1C1E26 : 0xFF14161C);

                    String valStr = String.format(java.util.Locale.US, "%.2f", ns.getValue());
                    graphics.drawString(this.font, ns.getName() + ": §f" + valStr, cx + 6, sy + 4, 0xFFCCCCCC, false);

                    // Slider Track
                    int tX = cx + 6;
                    int tY = sy + 18;
                    int tW = cw - 12;
                    graphics.fill(tX, tY, tX + tW, tY + 4, 0xFF2A2D38);

                    double frac = Math.max(0.0, Math.min(1.0, (ns.getValue() - ns.getMin()) / (ns.getMax() - ns.getMin())));
                    int fW = (int) (frac * tW);
                    graphics.fill(tX, tY, tX + fW, tY + 4, accent | 0xFF000000);
                    graphics.fill(tX + fW - 2, tY - 2, tX + fW + 4, tY + 6, 0xFFFFFFFF);

                    if (draggingSlider == ns) {
                        this.sliderTrackX = tX;
                        this.sliderTrackW = tW;
                    }
                    sy += rH + 4;
                } else if (s instanceof ModeSetting ms) {
                    int rH = 22;
                    boolean h = mouseX >= cx && mouseX <= cx + cw && mouseY >= sy && mouseY <= sy + rH;
                    graphics.fill(cx, sy, cx + cw, sy + rH, h ? 0xFF1C1E26 : 0xFF14161C);
                    graphics.drawString(this.font, ms.getName(), cx + 6, sy + 7, 0xFFCCCCCC, false);

                    String pill = "< " + ms.getValue() + " >";
                    int pw = this.font.width(pill);
                    graphics.drawString(this.font, pill, cx + cw - pw - 6, sy + 7, accent | 0xFF000000, false);
                    sy += rH + 4;
                }
            }
            graphics.disableScissor();
        } else {
            graphics.drawString(this.font, "§7Bir modül seçiniz", setX + 20, setY + 50, 0xFF555555, false);
        }

        super.render(graphics, mouseX, mouseY, partialTicks);
    }

    private void safeScissor(GuiGraphics graphics, int minX, int minY, int maxX, int maxY) {
        int cMinX = Math.max(0, Math.min(this.width, minX));
        int cMinY = Math.max(0, Math.min(this.height, minY));
        int cMaxX = Math.max(cMinX + 1, Math.min(this.width, maxX));
        int cMaxY = Math.max(cMinY + 1, Math.min(this.height, maxY));
        graphics.enableScissor(cMinX, cMinY, cMaxX, cMaxY);
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
        double mx = event.x();
        double my = event.y();
        int btn = event.button();

        int ww = Math.min(this.width - 20, 480);
        int wh = Math.min(this.height - 20, 270);
        int wx = (this.width - ww) / 2;
        int wy = (this.height - wh) / 2;

        // 1. Close Button
        int closeX = wx + ww - 18;
        int closeY = wy + 7;
        if (mx >= closeX && mx <= closeX + 12 && my >= closeY && my <= closeY + 12) {
            this.onClose();
            return true;
        }

        // Layout dimensions
        int catW = 96;
        int catX = wx;
        int catY = wy + 25;

        int modW = 174;
        int modX = catX + catW + 1;
        int modY = wy + 25;
        int modH = wh - 25;

        int setX = modX + modW + 1;
        int setY = wy + 25;
        int setW = ww - (setX - wx);

        // 2. Category selection
        Category[] categories = Category.values();
        int catBtnH = 24;
        int currCatY = catY + 6;
        for (Category cat : categories) {
            if (mx >= catX && mx < catX + catW && my >= currCatY && my < currCatY + catBtnH) {
                this.selectedCategory = cat;
                this.searchText = "";
                this.searchFocused = false;
                this.middleScroll = 0;
                selectFirstModule();
                return true;
            }
            currCatY += catBtnH + 2;
        }

        // 3. Search Bar
        int sbX = modX + 6;
        int sbY = modY + 6;
        int sbW = modW - 12;
        int sbH = 18;
        if (mx >= sbX && mx <= sbX + sbW && my >= sbY && my <= sbY + sbH) {
            this.searchFocused = true;
            return true;
        } else {
            this.searchFocused = false;
        }

        // 4. Module List
        int listY = sbY + sbH + 6;
        int listH = modH - (listY - modY) - 6;
        if (mx >= modX && mx <= modX + modW && my >= listY && my <= listY + listH) {
            List<Module> modules = getFilteredModules();
            int cardH = 26;
            int cardY = listY + middleScroll;

            for (Module m : modules) {
                if (mx >= modX + 4 && mx <= modX + modW - 4 && my >= cardY && my <= cardY + cardH) {
                    this.selectedModule = m;
                    this.listeningForKey = false;

                    // If clicked on toggle pill (right side) OR left click: toggle module!
                    int pillW = 28;
                    int pillX = modX + modW - 4 - pillW - 6;
                    if (mx >= pillX && mx <= pillX + pillW || btn == 0) {
                        m.toggle();
                    }
                    return true;
                }
                cardY += cardH + 3;
            }
        }

        // 5. Right Column (Toggle, Keybind, Settings)
        if (selectedModule != null) {
            int cx = setX + 10;
            int cy = setY + 8;
            int cw = setW - 20;

            // Big Toggle Button
            int btnToggleY = cy + 42;
            int btnToggleH = 20;
            if (mx >= cx && mx <= cx + cw && my >= btnToggleY && my <= btnToggleY + btnToggleH) {
                selectedModule.toggle();
                return true;
            }

            // Keybind Button
            int btnKeyY = btnToggleY + btnToggleH + 5;
            int btnKeyH = 18;
            if (mx >= cx && mx <= cx + cw && my >= btnKeyY && my <= btnKeyY + btnKeyH) {
                this.listeningForKey = !this.listeningForKey;
                return true;
            }

            // Settings click handling
            int sHeaderY = btnKeyY + btnKeyH + 8;
            int sAreaY = sHeaderY + 15;
            int sy = sAreaY + rightScroll;

            for (Setting<?> s : selectedModule.getSettings()) {
                if (!s.isVisible()) continue;

                if (s instanceof BooleanSetting bs) {
                    int rH = 20;
                    if (mx >= cx && mx <= cx + cw && my >= sy && my <= sy + rH) {
                        bs.setValue(!bs.getValue());
                        return true;
                    }
                    sy += rH + 4;
                } else if (s instanceof NumberSetting ns) {
                    int rH = 28;
                    if (mx >= cx && mx <= cx + cw && my >= sy && my <= sy + rH) {
                        this.draggingSlider = ns;
                        this.sliderTrackX = cx + 6;
                        this.sliderTrackW = cw - 12;
                        updateSliderValue(mx);
                        return true;
                    }
                    sy += rH + 4;
                } else if (s instanceof ModeSetting ms) {
                    int rH = 22;
                    if (mx >= cx && mx <= cx + cw && my >= sy && my <= sy + rH) {
                        ms.cycle();
                        return true;
                    }
                    sy += rH + 4;
                }
            }
        }

        return super.mouseClicked(event, doubleClick);
    }

    private void updateSliderValue(double mouseX) {
        if (draggingSlider == null || sliderTrackW <= 0) return;
        double fraction = Math.max(0.0, Math.min(1.0, (mouseX - sliderTrackX) / sliderTrackW));
        double range = draggingSlider.getMax() - draggingSlider.getMin();
        double target = draggingSlider.getMin() + fraction * range;
        draggingSlider.setValue(target);
    }

    @Override
    public boolean mouseReleased(MouseButtonEvent event) {
        this.draggingSlider = null;
        return super.mouseReleased(event);
    }

    @Override
    public boolean mouseDragged(MouseButtonEvent event, double dragX, double dragY) {
        if (draggingSlider != null) {
            updateSliderValue(event.x());
            return true;
        }
        return super.mouseDragged(event, dragX, dragY);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        int ww = Math.min(this.width - 20, 480);
        int wx = (this.width - ww) / 2;
        int catW = 96;
        int modW = 174;
        int modX = wx + catW + 1;
        int setX = modX + modW + 1;

        if (mouseX >= modX && mouseX <= modX + modW) {
            middleScroll += (int) (scrollY * 16);
            if (middleScroll > 0) middleScroll = 0;
            return true;
        } else if (mouseX >= setX && mouseX <= wx + ww) {
            rightScroll += (int) (scrollY * 16);
            if (rightScroll > 0) rightScroll = 0;
            return true;
        }
        return super.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
    }

    @Override
    public boolean keyPressed(KeyEvent event) {
        // 1. Assigning keybind to selected module
        if (listeningForKey && selectedModule != null) {
            if (event.key() == 256 || event.key() == 259) { // ESC or Backspace: unbind
                selectedModule.setKey(0);
            } else {
                selectedModule.setKey(event.key());
            }
            this.listeningForKey = false;
            return true;
        }

        // 2. Typing into Search Bar
        if (searchFocused) {
            if (event.key() == 256) { // ESC: exit search focus
                this.searchFocused = false;
                return true;
            } else if (event.key() == 259 && !searchText.isEmpty()) { // Backspace
                searchText = searchText.substring(0, searchText.length() - 1);
                selectFirstModule();
                return true;
            }
        }

        // 3. Close GUI with ESC or ClickGui Key (Default: Right Shift)
        if (event.key() == 256 || event.key() == AetherClient.getInstance().getKeybindManager().getClickGuiKey()) {
            this.onClose();
            return true;
        }

        return super.keyPressed(event);
    }

    @Override
    public boolean charTyped(CharacterEvent event) {
        if (searchFocused && event.isAllowedChatCharacter()) {
            searchText += (char) event.codepoint();
            selectFirstModule();
            return true;
        }
        return super.charTyped(event);
    }

    @Override
    public void onClose() {
        AetherClient.getInstance().setClickGuiOpen(false);
        super.onClose();
    }
}
