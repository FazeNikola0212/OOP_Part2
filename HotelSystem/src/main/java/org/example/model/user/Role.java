package org.example.model.user;

public enum Role {
    ADMIN("Admin"),
    OWNER("Owner"),
    MANAGER("Manager"),
    RECEPTIONIST("Receptionist");

    private String displayName;

    Role(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
