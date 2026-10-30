package com.aether.client.networking;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Real-time security, permission, and server environment inspector.
 * Accurately tracks server software, flight allowances, and operator status without spoofing.
 */
public class PermissionDashboard {
    public enum Gamemode {
        SURVIVAL("Survival"),
        CREATIVE("Creative"),
        ADVENTURE("Adventure"),
        SPECTATOR("Spectator"),
        UNKNOWN("Unknown");

        private final String label;
        Gamemode(String label) { this.label = label; }
        public String getLabel() { return label; }
    }

    private boolean isConnected = false;
    private String serverAddress = "Local Singleplayer";
    private String serverBrand = "Vanilla / Local";
    private int pingMs = 0;

    private boolean operatorStatus = false;
    private boolean serverVerifiedOp = false;
    private Gamemode currentGamemode = Gamemode.SURVIVAL;
    private boolean flightAllowed = false;
    private final List<String> authorizedFeatures = new ArrayList<>();

    public PermissionDashboard() {
        refreshAuthorizedFeatures();
    }

    public void updateServerState(boolean connected, String address, String brand, int ping) {
        this.isConnected = connected;
        this.serverAddress = address != null ? address : "Singleplayer";
        this.serverBrand = brand != null ? brand : "Vanilla";
        this.pingMs = ping;
        refreshAuthorizedFeatures();
    }

    public void updatePlayerState(boolean op, boolean serverVerified, Gamemode gamemode, boolean canFly) {
        this.operatorStatus = op;
        this.serverVerifiedOp = serverVerified;
        this.currentGamemode = gamemode != null ? gamemode : Gamemode.UNKNOWN;
        this.flightAllowed = canFly;
        refreshAuthorizedFeatures();
    }

    private void refreshAuthorizedFeatures() {
        authorizedFeatures.clear();
        authorizedFeatures.add("Visuals: HUD & Overlays");
        authorizedFeatures.add("Utility: Diagnostics & Session Analytics");
        authorizedFeatures.add("World: Waypoints & Chunk Visualizer");
        authorizedFeatures.add("Combat: CPS Counter & Target HUD");

        if (flightAllowed || currentGamemode == Gamemode.CREATIVE) {
            authorizedFeatures.add("Movement: Authorized Flight Mode");
        }
        if (operatorStatus && serverVerifiedOp) {
            authorizedFeatures.add("Player: Gamemode Switcher Command Interface");
        }
    }

    public boolean isConnected() {
        return isConnected;
    }

    public String getServerAddress() {
        return serverAddress;
    }

    public String getServerBrand() {
        return serverBrand;
    }

    public int getPingMs() {
        return pingMs;
    }

    public boolean isOperatorStatus() {
        return operatorStatus;
    }

    public boolean isServerVerifiedOp() {
        return serverVerifiedOp;
    }

    public Gamemode getCurrentGamemode() {
        return currentGamemode;
    }

    public boolean isFlightAllowed() {
        return flightAllowed;
    }

    public List<String> getAuthorizedFeatures() {
        return Collections.unmodifiableList(authorizedFeatures);
    }
}
