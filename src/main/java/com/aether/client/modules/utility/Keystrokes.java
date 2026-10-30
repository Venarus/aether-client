package com.aether.client.modules.utility;

import com.aether.client.modules.api.Category;
import com.aether.client.modules.api.CompatibilityStatus;
import com.aether.client.modules.api.Module;
import com.aether.client.modules.settings.BooleanSetting;
import com.aether.client.modules.settings.ColorSetting;

/**
 * Keystrokes module - Displays on-screen key indicators (W, A, S, D, Space, Shift, Mouse clicks).
 */
public class Keystrokes extends Module {
    private final BooleanSetting showMouse = addSetting(new BooleanSetting("Mouse Buttons", "Fare tuşlarını göster", true));
    private final BooleanSetting showSpace = addSetting(new BooleanSetting("Spacebar", "Boşluk tuşunu göster", true));
    private final ColorSetting activeColor = addSetting(new ColorSetting("Pressed Color", "Basılı tuş rengi", 0xFFE53935));

    private boolean keyW, keyA, keyS, keyD, keySpace, keyShift, mouseLeft, mouseRight;

    public Keystrokes() {
        super("Keystrokes", "W, A, S, D, boşluk ve fare vuruşlarını ekranda canlı kutucuklarla gösterir.",
                Category.UTILITY, 0, CompatibilityStatus.SUPPORTED, false, true);
    }

    public void updateKeys(boolean w, boolean a, boolean s, boolean d, boolean space, boolean shift, boolean lmb, boolean rmb) {
        this.keyW = w;
        this.keyA = a;
        this.keyS = s;
        this.keyD = d;
        this.keySpace = space;
        this.keyShift = shift;
        this.mouseLeft = lmb;
        this.mouseRight = rmb;
    }

    public boolean isKeyW() { return keyW; }
    public boolean isKeyA() { return keyA; }
    public boolean isKeyS() { return keyS; }
    public boolean isKeyD() { return keyD; }
    public boolean isKeySpace() { return keySpace; }
    public boolean isKeyShift() { return keyShift; }
    public boolean isMouseLeft() { return mouseLeft; }
    public boolean isMouseRight() { return mouseRight; }
    public boolean isShowMouse() { return showMouse.getValue(); }
    public boolean isShowSpace() { return showSpace.getValue(); }
    public int getActiveColor() { return activeColor.getColor(); }
}
