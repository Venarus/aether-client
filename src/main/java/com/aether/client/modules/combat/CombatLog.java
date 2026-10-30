package com.aether.client.modules.combat;

import com.aether.client.modules.api.Category;
import com.aether.client.modules.api.CompatibilityStatus;
import com.aether.client.modules.api.Module;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Combat Log module - Logs incoming and outgoing damage events with weapon and timestamp analysis.
 */
public class CombatLog extends Module {
    public static class CombatEntry {
        public final long timestamp;
        public final boolean incoming;
        public final String entityName;
        public final float damage;
        public final String weapon;

        public CombatEntry(boolean incoming, String entityName, float damage, String weapon) {
            this.timestamp = System.currentTimeMillis();
            this.incoming = incoming;
            this.entityName = entityName;
            this.damage = damage;
            this.weapon = weapon;
        }

        public String format() {
            String time = new SimpleDateFormat("HH:mm:ss").format(new Date(timestamp));
            if (incoming) {
                return String.format("[%s] <%s> kaynağından %.1f hasar alındı (%s)", time, entityName, damage, weapon);
            } else {
                return String.format("[%s] <%s> hedefine %.1f hasar vuruldu (%s)", time, entityName, damage, weapon);
            }
        }
    }

    private final List<CombatEntry> entries = new ArrayList<>();

    public CombatLog() {
        super("Combat Log", "Savaş olaylarını, alınan ve verilen hasarları zaman damgalarıyla kaydeder.",
                Category.COMBAT, CompatibilityStatus.SUPPORTED);
    }

    public synchronized void logEvent(boolean incoming, String entity, float damage, String weapon) {
        entries.add(new CombatEntry(incoming, entity, damage, weapon));
        if (entries.size() > 100) {
            entries.remove(0);
        }
    }

    public synchronized List<CombatEntry> getRecentEntries(int count) {
        int from = Math.max(0, entries.size() - count);
        return new ArrayList<>(entries.subList(from, entries.size()));
    }
}
