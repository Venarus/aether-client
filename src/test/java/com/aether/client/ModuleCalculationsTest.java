package com.aether.client;

import com.aether.client.modules.combat.CPSCounter;
import com.aether.client.modules.movement.JumpDiagnostics;
import com.aether.client.modules.movement.Speedometer;
import com.aether.client.modules.player.Coordinates;
import com.aether.client.modules.utility.InventoryCleaner;
import com.aether.client.modules.world.WaypointManager;
import com.aether.client.networking.PermissionDashboard;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ModuleCalculationsTest {
    @Test
    void testSpeedometerCalculations() {
        Speedometer speedometer = new Speedometer();
        // Moving 0.2 blocks in X and 0.0 in Z per tick -> 0.2 * 20 = 4.0 blocks/sec (m/s)
        speedometer.updateSpeed(0.2, 0.0);
        assertEquals(4.0, speedometer.getCurrentSpeed(), 0.001);
        assertEquals(4.0, speedometer.getPeakSpeed(), 0.001);

        // Check km/h conversion: 4.0 m/s * 3.6 = 14.4 km/h
        speedometer.getSettingByName("Unit").parseAndSet("km/h");
        assertEquals(14.4, speedometer.getFormattedSpeed(4.0), 0.001);
    }

    @Test
    void testJumpDiagnostics() {
        JumpDiagnostics jd = new JumpDiagnostics();
        jd.onPlayerJump(64.0);
        assertTrue(jd.isInAir());
        assertEquals(1, jd.getTotalJumps());

        // In air apex at 65.25 blocks
        jd.onPlayerTick(65.25, false, 0.0f);
        assertTrue(jd.isInAir());

        // Land on ground at 64.0
        jd.onPlayerTick(64.0, true, 1.25f);
        assertFalse(jd.isInAir());
        assertEquals(1.25, jd.getLastJumpHeight(), 0.001);
    }

    @Test
    void testCPSCounter() {
        CPSCounter cps = new CPSCounter();
        assertEquals(0, cps.getLeftCPS());
        assertEquals(0, cps.getRightCPS());

        cps.registerLeftClick();
        cps.registerLeftClick();
        cps.registerLeftClick();
        assertEquals(3, cps.getLeftCPS());

        cps.registerRightClick();
        assertEquals(1, cps.getRightCPS());
    }

    @Test
    void testCoordinatesFacingCalculation() {
        // Yaw 0 -> South (+Z)
        assertEquals("South (+Z)", Coordinates.calculateFacing(0.0f));
        // Yaw 90 -> West (-X)
        assertEquals("West (-X)", Coordinates.calculateFacing(90.0f));
        // Yaw 180 -> North (-Z)
        assertEquals("North (-Z)", Coordinates.calculateFacing(180.0f));
        // Yaw 270 -> East (+X)
        assertEquals("East (+X)", Coordinates.calculateFacing(270.0f));
    }

    @Test
    void testInventoryCleanerSafety() {
        InventoryCleaner cleaner = new InventoryCleaner();
        // Valuables, Weapons, Armor should NOT be safe to auto-drop
        assertFalse(cleaner.isSafeToDrop("minecraft:diamond_sword"));
        assertFalse(cleaner.isSafeToDrop("minecraft:netherite_chestplate"));
        assertFalse(cleaner.isSafeToDrop("minecraft:diamond"));

        // Junk and common blocks SHOULD be safe to drop
        assertTrue(cleaner.isSafeToDrop("minecraft:rotten_flesh"));
        assertTrue(cleaner.isSafeToDrop("minecraft:cobblestone"));
    }

    @Test
    void testWaypointDistance() {
        WaypointManager.Waypoint wp = new WaypointManager.Waypoint("Base", 100, 64, 100, "overworld", 0xFF00FF);
        double dist = wp.distanceTo(100, 64, 150);
        assertEquals(50.0, dist, 0.001);
    }

    @Test
    void testPermissionDashboard() {
        PermissionDashboard dashboard = new PermissionDashboard();
        dashboard.updateServerState(true, "mc.aternos.org", "Paper (MC: 1.21.10)", 35);
        dashboard.updatePlayerState(false, false, PermissionDashboard.Gamemode.SURVIVAL, false);

        assertFalse(dashboard.isOperatorStatus());
        assertFalse(dashboard.isFlightAllowed());
        assertEquals("Paper (MC: 1.21.10)", dashboard.getServerBrand());
        assertEquals(35, dashboard.getPingMs());

        // Flight not allowed in survival without flight permission
        assertFalse(dashboard.getAuthorizedFeatures().contains("Movement: Authorized Flight Mode"));

        // Now simulate granting flight
        dashboard.updatePlayerState(false, false, PermissionDashboard.Gamemode.SURVIVAL, true);
        assertTrue(dashboard.getAuthorizedFeatures().contains("Movement: Authorized Flight Mode"));
    }
}
