package org.example.model.reservation;

public enum ReservationStatus {
    ACTIVE("Active"), CHECKED_IN("Checked-In"), EXPIRED("Expired");

    public final String value;

    ReservationStatus(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
