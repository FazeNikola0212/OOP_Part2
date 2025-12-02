package org.example.model.room;

public enum RoomStatus {
    AVAILABLE("Available"),
    MAINTENANCE("Maintenance"),
    OCCUPIED("Occupied");

    public final String value;
    RoomStatus(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
