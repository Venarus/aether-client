package com.aether.client.core;

import com.aether.client.modules.combat.*;
import com.aether.client.modules.movement.*;
import com.aether.client.modules.player.*;
import com.aether.client.modules.utility.*;
import com.aether.client.modules.visuals.*;
import net.minecraft.client.Minecraft;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;

/**
 * Coordinates real-time Minecraft game logic and execution for all active Aether Client modules.
 * Executes on every client tick to ensure all modules perform real, tangible in-game actions.
 */
public class AetherClientTicker {
    private static double lastPlayerX = 0, lastPlayerY = 0, lastPlayerZ = 0;
    private static boolean lastFullbright = false;
    private static boolean lastFly = false;

    public static void onClientTick() {
        Minecraft mc = Minecraft.getInstance();
        if (mc == null || mc.player == null || mc.level == null) return;

        // 1. Fullbright (Night Vision / Brightness)
        Fullbright fullbright = AetherClient.getInstance().getModuleManager().getModule(Fullbright.class);
        if (fullbright != null && fullbright.isEnabled()) {
            lastFullbright = true;
            // Apply ambient night vision with zero particle swirls
            mc.player.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 600, 0, false, false, false));
        } else if (lastFullbright) {
            lastFullbright = false;
            mc.player.removeEffect(MobEffects.NIGHT_VISION);
        }

        // 2. Sprint (Auto-Sprint)
        Sprint sprint = AetherClient.getInstance().getModuleManager().getModule(Sprint.class);
        if (sprint != null && sprint.isEnabled()) {
            boolean movingForward = mc.options.keyUp.isDown() || (mc.player.input != null && mc.player.input.hasForwardImpulse());
            if (movingForward && !mc.player.isShiftKeyDown() && !mc.player.horizontalCollision) {
                mc.player.setSprinting(true);
            }
        }

        // 3. Fly (Creative / Simulation Flight)
        Fly fly = AetherClient.getInstance().getModuleManager().getModule(Fly.class);
        if (fly != null && fly.isEnabled()) {
            lastFly = true;
            mc.player.getAbilities().mayfly = true;
            mc.player.getAbilities().flying = true;
            mc.player.getAbilities().setFlyingSpeed((float) (0.05f * fly.getSpeed()));
        } else if (lastFly) {
            lastFly = false;
            if (!mc.player.isCreative() && !mc.player.isSpectator()) {
                mc.player.getAbilities().flying = false;
                mc.player.getAbilities().mayfly = false;
            }
            mc.player.getAbilities().setFlyingSpeed(0.05f);
        }

        // 4. Entity ESP / Glowing
        EntityESP esp = AetherClient.getInstance().getModuleManager().getModule(EntityESP.class);
        boolean espOn = esp != null && esp.isEnabled();
        for (Entity e : mc.level.entitiesForRendering()) {
            if (e != mc.player && e instanceof LivingEntity) {
                if (espOn) {
                    if (!e.hasGlowingTag()) e.setGlowingTag(true);
                } else {
                    if (e.hasGlowingTag()) e.setGlowingTag(false);
                }
            }
        }

        // 5. Speedometer & Motion Tracker
        Speedometer speed = AetherClient.getInstance().getModuleManager().getModule(Speedometer.class);
        Vec3 v = mc.player.getDeltaMovement();
        if (speed != null) {
            speed.updateSpeed(v.x, v.z);
        }
        MotionTracker tracker = AetherClient.getInstance().getModuleManager().getModule(MotionTracker.class);
        if (tracker != null) {
            tracker.updateMotion(v.x, v.y, v.z);
        }

        // 6. Coordinates HUD
        Coordinates coords = AetherClient.getInstance().getModuleManager().getModule(Coordinates.class);
        if (coords != null) {
            String biome = mc.level.getBiome(mc.player.blockPosition()).unwrapKey()
                    .map(k -> k.location().getPath()).orElse("Unknown");
            coords.update(
                    mc.player.getX(), mc.player.getY(), mc.player.getZ(),
                    biome, mc.player.getYRot(), mc.player.getXRot(),
                    mc.level.dimension().location().toString()
            );
        }

        // 7. Keystrokes
        Keystrokes keys = AetherClient.getInstance().getModuleManager().getModule(Keystrokes.class);
        if (keys != null && mc.options != null && mc.mouseHandler != null) {
            keys.updateKeys(
                    mc.options.keyUp.isDown(),
                    mc.options.keyLeft.isDown(),
                    mc.options.keyDown.isDown(),
                    mc.options.keyRight.isDown(),
                    mc.options.keyJump.isDown(),
                    mc.options.keyShift.isDown(),
                    mc.mouseHandler.isLeftPressed(),
                    mc.mouseHandler.isRightPressed()
            );
        }

        // 8. Target HUD & Reach Display
        TargetHUD targetHud = AetherClient.getInstance().getModuleManager().getModule(TargetHUD.class);
        ReachDisplay reachDisplay = AetherClient.getInstance().getModuleManager().getModule(ReachDisplay.class);
        Entity crosshair = mc.crosshairPickEntity;
        if (crosshair instanceof LivingEntity living) {
            double dist = mc.player.distanceTo(living);
            if (targetHud != null) {
                targetHud.updateTarget(living.getName().getString(), living.getHealth(), living.getMaxHealth(), dist);
            }
            if (reachDisplay != null) {
                reachDisplay.registerAttackReach(dist);
            }
        } else if (targetHud != null) {
            targetHud.clearTarget();
        }

        // 9. Totem Counter
        TotemCounter totems = AetherClient.getInstance().getModuleManager().getModule(TotemCounter.class);
        if (totems != null) {
            int count = 0;
            for (int i = 0; i < mc.player.getInventory().getContainerSize(); i++) {
                ItemStack item = mc.player.getInventory().getItem(i);
                if (!item.isEmpty() && item.is(Items.TOTEM_OF_UNDYING)) {
                    count += item.getCount();
                }
            }
            totems.updateCount(count);
        }

        // 10. Jump Diagnostics
        JumpDiagnostics jumpDiag = AetherClient.getInstance().getModuleManager().getModule(JumpDiagnostics.class);
        if (jumpDiag != null) {
            jumpDiag.onPlayerTick(mc.player.getY(), mc.player.onGround(), (float) mc.player.fallDistance);
        }

        // 11. Session Statistics (Distance, FPS)
        SessionStatistics stats = AetherClient.getInstance().getModuleManager().getModule(SessionStatistics.class);
        if (stats != null) {
            double dx = mc.player.getX() - lastPlayerX;
            double dy = mc.player.getY() - lastPlayerY;
            double dz = mc.player.getZ() - lastPlayerZ;
            double dist = Math.sqrt(dx * dx + dy * dy + dz * dz);
            if (lastPlayerX != 0 && dist < 10.0) { // filter teleports
                stats.addDistance(dist);
            }
            stats.updateFps(mc.getFps());
        }

        lastPlayerX = mc.player.getX();
        lastPlayerY = mc.player.getY();
        lastPlayerZ = mc.player.getZ();
    }
}
