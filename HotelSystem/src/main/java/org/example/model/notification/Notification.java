package org.example.model.notification;

import jakarta.persistence.*;
import lombok.*;
import org.example.model.hotel.Hotel;
import org.example.model.user.User;

import java.time.LocalDateTime;

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
}
