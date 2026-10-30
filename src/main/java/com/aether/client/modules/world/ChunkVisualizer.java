package com.aether.client.modules.world;

import com.aether.client.modules.api.Category;
import com.aether.client.modules.api.CompatibilityStatus;
import com.aether.client.modules.api.Module;
import com.aether.client.modules.settings.BooleanSetting;
import com.aether.client.modules.settings.ColorSetting;

/**
 * Chunk Visualizer module - Renders real-time chunk borders, 16x16 sub-chunk grids, and center lines.
 */
public class ChunkVisualizer extends Module {
    private final BooleanSetting showSubChunks = addSetting(new BooleanSetting("Sub-chunks", "16x16x16 alt bölümleri göster", true));
    private final ColorSetting borderColor = addSetting(new ColorSetting("Border Color", "Chunk sınır rengi", 0xFFFF0055));
    private final ColorSetting subChunkColor = addSetting(new ColorSetting("Grid Color", "Alt ızgara rengi", 0xFF00E5FF));

    public ChunkVisualizer() {
        super("Chunk Visualizer", "Chunk sınırlarını, alt ızgaraları ve merkez koordinatlarını 3B olarak çizer.",
                Category.WORLD, CompatibilityStatus.SUPPORTED);
    }

    public boolean isShowSubChunks() { return showSubChunks.getValue(); }
    public int getBorderColor() { return borderColor.getColor(); }
    public int getSubChunkColor() { return subChunkColor.getColor(); }
}
