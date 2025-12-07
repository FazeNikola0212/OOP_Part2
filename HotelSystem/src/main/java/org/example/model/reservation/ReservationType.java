package org.example.model.reservation;

public enum ReservationType {
    ONLINE("Online"),
    PHONE("Phone"),
    IN_PERSON("In-Person");

    public final String value;

    ReservationType(String value) {
        this.value = value;
    }
    @Override
    public String toString() {
        return value;
    }
}
