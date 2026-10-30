package com.aether.client.modules.utility;

import com.aether.client.modules.api.Category;
import com.aether.client.modules.api.CompatibilityStatus;
import com.aether.client.modules.api.Module;
import com.aether.client.modules.settings.BooleanSetting;

import java.util.ArrayList;
import java.util.List;

/**
 * Inventory Cleaner module - Analyzes inventory slots, suggests categorical sorting, and prevents accidental loss.
 */
public class InventoryCleaner extends Module {
    public enum ItemCategory {
        WEAPON,
        ARMOR,
        TOOL,
        FOOD,
        RESOURCE,
        BLOCK,
        JUNK,
        VALUABLE
    }

    public static class SortingAdvice {
        public final int slot;
        public final String itemName;
        public final ItemCategory category;
        public final String suggestion;

        public SortingAdvice(int slot, String itemName, ItemCategory category, String suggestion) {
            this.slot = slot;
            this.itemName = itemName;
            this.category = category;
            this.suggestion = suggestion;
        }
    }

    private final BooleanSetting protectValuables = addSetting(new BooleanSetting("Protect Valuables", "Değerli eşyaları koru", true));

    public InventoryCleaner() {
        super("Inventory Cleaner", "Çanta düzenleme tavsiyeleri verir ve değerli eşyaların yanlışlıkla atılmasını önler.",
                Category.UTILITY, CompatibilityStatus.SUPPORTED);
    }

    public ItemCategory categorizeItem(String itemId) {
        String lower = itemId.toLowerCase();
        if (lower.contains("sword") || lower.contains("bow") || lower.contains("crossbow") || lower.contains("trident")) {
            return ItemCategory.WEAPON;
        } else if (lower.contains("helmet") || lower.contains("chestplate") || lower.contains("leggings") || lower.contains("boots") || lower.contains("elytra")) {
            return ItemCategory.ARMOR;
        } else if (lower.contains("pickaxe") || lower.contains("axe") || lower.contains("shovel") || lower.contains("hoe")) {
            return ItemCategory.TOOL;
        } else if (lower.contains("diamond") || lower.contains("netherite") || lower.contains("emerald") || lower.contains("totem")) {
            return ItemCategory.VALUABLE;
        } else if (lower.contains("beef") || lower.contains("bread") || lower.contains("apple") || lower.contains("carrot") || lower.contains("porkchop")) {
            return ItemCategory.FOOD;
        } else if (lower.contains("dirt") || lower.contains("cobblestone") || lower.contains("gravel") || lower.contains("andesite")) {
            return ItemCategory.BLOCK;
        } else if (lower.contains("rotten_flesh") || lower.contains("poisonous_potato")) {
            return ItemCategory.JUNK;
        }
        return ItemCategory.RESOURCE;
    }

    public boolean isSafeToDrop(String itemId) {
        if (!protectValuables.getValue()) return true;
        ItemCategory cat = categorizeItem(itemId);
        return cat != ItemCategory.VALUABLE && cat != ItemCategory.WEAPON && cat != ItemCategory.ARMOR;
    }
}
