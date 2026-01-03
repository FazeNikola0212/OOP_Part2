package org.example.model.notification;

import jakarta.persistence.*;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import lombok.*;
import org.example.model.hotel.Hotel;
import org.example.model.user.User;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Table(name = "notifications")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Hotel hotel;

    private boolean global;

    @Enumerated(EnumType.STRING)
    private NotificationType type;

    private String message;

    private LocalDateTime createdAt;

    private boolean isRead;

    @Transient
    private BooleanProperty readProperty = new SimpleBooleanProperty();

    @PostLoad
    private void syncReadProperty() {
        readProperty.set(isRead);
    }

    public BooleanProperty readProperty() {
        return readProperty;
    }

    public boolean isRead() {
        return readProperty.get();
    }

    public void setRead(boolean read) {
        this.isRead = read;
        this.readProperty.set(read);
    }

}
