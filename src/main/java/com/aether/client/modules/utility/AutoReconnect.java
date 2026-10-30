package com.aether.client.modules.utility;

import com.aether.client.modules.api.Category;
import com.aether.client.modules.api.CompatibilityStatus;
import com.aether.client.modules.api.Module;
import com.aether.client.modules.settings.NumberSetting;

/**
 * Auto Reconnect module - Reconnects cleanly to the previous server after disconnect with a countdown.
 */
public class AutoReconnect extends Module {
    private final NumberSetting delaySeconds = addSetting(new NumberSetting("Delay", "Yeniden bağlanma bekleme süresi", 5.0, 2.0, 30.0, 1.0, "s"));
    private final NumberSetting maxAttempts = addSetting(new NumberSetting("Max Attempts", "Maksimum deneme sayısı", 5, 1, 20, 1));

    private int currentAttempt = 0;
    private long reconnectTimestamp = 0;
    private boolean waitingToReconnect = false;

    public AutoReconnect() {
        super("Auto Reconnect", "Bağlantı koptuğunda geri sayımlı olarak güvenli şekilde sunucuya tekrar bağlanır.",
                Category.UTILITY, CompatibilityStatus.SUPPORTED);
    }

    public void triggerDisconnect() {
        if (!isEnabled()) return;
        if (currentAttempt < maxAttempts.getValueInt()) {
            this.waitingToReconnect = true;
            this.reconnectTimestamp = System.currentTimeMillis() + (long) (delaySeconds.getValue() * 1000L);
            this.currentAttempt++;
        } else {
            cancel();
        }
    }

    public void cancel() {
        this.waitingToReconnect = false;
        this.currentAttempt = 0;
    }

    public boolean isWaitingToReconnect() {
        return waitingToReconnect;
    }

    public int getRemainingSeconds() {
        if (!waitingToReconnect) return 0;
        long diff = reconnectTimestamp - System.currentTimeMillis();
        return Math.max(0, (int) Math.ceil(diff / 1000.0));
    }

    public int getCurrentAttempt() { return currentAttempt; }
}
