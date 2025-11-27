package org.example.model.amenity;

public enum SeasonAmenity {
    ALL_SEASONS("All-seasons"),
    SUMMER("Summer"),
    AUTUMN("Autumn"),
    SPRING("Spring"),
    WINTER("Winter");

    private String displayName;
    SeasonAmenity(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}

