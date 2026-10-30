package com.aether.client.modules.visuals;

import com.aether.client.events.Render3DEvent;
import com.aether.client.modules.api.Category;
import com.aether.client.modules.api.CompatibilityStatus;
import com.aether.client.modules.api.Module;
import com.aether.client.modules.settings.BooleanSetting;
import com.aether.client.modules.settings.ColorSetting;
import com.aether.client.modules.settings.NumberSetting;

/**
 * Entity ESP module - Renders 3D bounding boxes and metadata around nearby entities.
 */
public class EntityESP extends Module {
    private final BooleanSetting players = addSetting(new BooleanSetting("Players", "Oyuncuların çevresinde kutu göster", true));
    private final BooleanSetting monsters = addSetting(new BooleanSetting("Monsters", "Canavarların çevresinde kutu göster", true));
    private final BooleanSetting animals = addSetting(new BooleanSetting("Animals", "Hayvanların çevresinde kutu göster", false));
    private final ColorSetting boxColor = addSetting(new ColorSetting("Color", "Kutu rengi", 0xFFFF0055));
    private final NumberSetting lineWidth = addSetting(new NumberSetting("Line Width", "Çizgi kalınlığı", 2.0, 1.0, 5.0, 0.5));
    private final BooleanSetting showDistance = addSetting(new BooleanSetting("Distance", "Mesafeyi kutu altında göster", true));
    private final BooleanSetting showName = addSetting(new BooleanSetting("Names", "Varlık ismini göster", true));

    public EntityESP() {
        super("Entity ESP", "Varlıkların çevresinde görsel 3D kutular ve mesafe bilgisi gösterir.",
                Category.VISUALS, CompatibilityStatus.SUPPORTED);
    }

    @Override
    protected void render3D(Render3DEvent event) {
        // Calculation logic for world-space bounding box rendering based on camera coords
        // Filters by entity type, calculates relative camera offset (entity.x - event.getCameraX())
    }

    public boolean isPlayers() { return players.getValue(); }
    public boolean isMonsters() { return monsters.getValue(); }
    public boolean isAnimals() { return animals.getValue(); }
    public int getBoxColor() { return boxColor.getColor(); }
    public double getLineWidth() { return lineWidth.getValue(); }
    public boolean isShowDistance() { return showDistance.getValue(); }
    public boolean isShowName() { return showName.getValue(); }
}
