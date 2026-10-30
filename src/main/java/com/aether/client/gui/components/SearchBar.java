package com.aether.client.gui.components;

/**
 * Interactive search bar component for live module filtering.
 */
public class SearchBar extends GuiComponent {
    private String text = "";
    private boolean focused = false;
    private final String placeholder;

    public SearchBar(double x, double y, double width, double height, String placeholder) {
        super(x, y, width, height);
        this.placeholder = placeholder != null ? placeholder : "Modülleri ara...";
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0) {
            this.focused = isMouseOver(mouseX, mouseY);
            return this.focused;
        }
        return false;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (!focused) return false;

        // GLFW_KEY_BACKSPACE = 259
        if (keyCode == 259 && !text.isEmpty()) {
            text = text.substring(0, text.length() - 1);
            return true;
        }
        // GLFW_KEY_ESCAPE = 256
        if (keyCode == 256) {
            focused = false;
            return true;
        }
        return false;
    }

    @Override
    public boolean charTyped(char codePoint, int modifiers) {
        if (!focused) return false;
        if (Character.isLetterOrDigit(codePoint) || Character.isWhitespace(codePoint) || codePoint == '-' || codePoint == '_') {
            text += codePoint;
            return true;
        }
        return false;
    }

    public String getText() { return text; }
    public void setText(String text) { this.text = text != null ? text : ""; }
    public boolean isFocused() { return focused; }
    public void setFocused(boolean focused) { this.focused = focused; }
    public String getPlaceholder() { return placeholder; }
    public void clear() { this.text = ""; }
}
