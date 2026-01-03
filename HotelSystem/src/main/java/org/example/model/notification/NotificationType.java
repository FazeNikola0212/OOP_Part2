package org.example.model.notification;

public enum NotificationType {
    RISK("Risk"), INFO("Info"), WARNING("Warning")

    ;
    private String value;
    private NotificationType(String value) {
        this.value = value;
    }
    public String toString() {
        return this.value;
    }
}
