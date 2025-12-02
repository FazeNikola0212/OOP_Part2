package org.example.model.room;

public enum RoomCategory {
    DELUXE("Deluxe"),
    SINGLE("Single"),
    DOUBLE("Double"),
    SUITE("Suite"),
    PRESIDENTIAL("Presidential"),
    TRIPLE("Triple"),
    PENTHOUSE("Penthouse");

    public final String value;
    RoomCategory(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
