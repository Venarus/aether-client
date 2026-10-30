package com.aether.client.modules.utility;

import com.aether.client.modules.api.Category;
import com.aether.client.modules.api.CompatibilityStatus;
import com.aether.client.modules.api.Module;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Screenshot Manager module - Handles custom naming, historical gallery tracking, and quick folder access.
 */
public class ScreenshotManager extends Module {
    public static class ScreenshotEntry {
        public final String fileName;
        public final String timestamp;
        public final long sizeBytes;

        public ScreenshotEntry(String fileName, String timestamp, long sizeBytes) {
            this.fileName = fileName;
            this.timestamp = timestamp;
            this.sizeBytes = sizeBytes;
        }
    }

    private final List<ScreenshotEntry> history = new ArrayList<>();

    public ScreenshotManager() {
        super("Screenshot Manager", "Ekran görüntülerini özel isim formatıyla kaydeder ve geçmişi listeler.",
                Category.UTILITY, CompatibilityStatus.SUPPORTED);
    }

    public String generateNextFilename() {
        String time = new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss").format(new Date());
        return "Aether_" + time + ".png";
    }

    public synchronized void registerScreenshot(String fileName, long sizeBytes) {
        String time = new SimpleDateFormat("HH:mm:ss").format(new Date());
        history.add(new ScreenshotEntry(fileName, time, sizeBytes));
    }

    public synchronized List<ScreenshotEntry> getHistory() {
        return Collections.unmodifiableList(new ArrayList<>(history));
    }
}
