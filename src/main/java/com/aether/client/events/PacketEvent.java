package com.aether.client.events;

/**
 * Dispatched on inbound or outbound network packets for protocol telemetry and permission handshake.
 */
public class PacketEvent extends CancellableEvent {
    public enum Direction {
        INBOUND,
        OUTBOUND
    }

    private final Direction direction;
    private final String packetType;
    private final Object payload;

    public PacketEvent(Direction direction, String packetType, Object payload) {
        this.direction = direction;
        this.packetType = packetType;
        this.payload = payload;
    }

    public Direction getDirection() {
        return direction;
    }

    public String getPacketType() {
        return packetType;
    }

    public Object getPayload() {
        return payload;
    }

    public boolean isInbound() {
        return direction == Direction.INBOUND;
    }

    public boolean isOutbound() {
        return direction == Direction.OUTBOUND;
    }
}
