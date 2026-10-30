package com.aether.client.modules.player;

import com.aether.client.core.AetherClient;
import com.aether.client.modules.api.Category;
import com.aether.client.modules.api.CompatibilityStatus;
import com.aether.client.modules.api.Module;
import com.aether.client.networking.PermissionDashboard;

/**
 * Gamemode Manager module - Quick gamemode selector with operator permission verification.
 */
public class GamemodeManager extends Module {
    private String lastExecutionResult = null;

    public GamemodeManager() {
        super("Gamemode Manager", "Yetkili oyuncular için hızlı oyun modu değiştirme arayüzü.",
                Category.PLAYER, 0, CompatibilityStatus.REQUIRES_SERVER_PERMISSION, true, false);
    }

    public boolean requestGamemodeChange(PermissionDashboard.Gamemode targetMode) {
        boolean isOp = false;
        if (AetherClient.getInstance() != null && AetherClient.getInstance().getPermissionDashboard() != null) {
            isOp = AetherClient.getInstance().getPermissionDashboard().isOperatorStatus();
        }

        if (!isOp) {
            this.lastExecutionResult = "Yetki Reddedildi: Bu komut için sunucu yöneticisi (OP) yetkisi gereklidir.";
            return false;
        }

        this.lastExecutionResult = "Oyun modu değiştirme isteği gönderildi: /gamemode " + targetMode.getLabel().toLowerCase();
        return true;
    }

    public String getLastExecutionResult() {
        return lastExecutionResult;
    }
}
