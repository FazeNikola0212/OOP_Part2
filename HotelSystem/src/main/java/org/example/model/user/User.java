package org.example.model.user;

import jakarta.persistence.*;
import lombok.*;
import org.example.model.hotel.Hotel;
import org.example.model.reservation.Reservation;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Entity
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Getter
@Setter
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    private String email;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    private String fullName;

    private boolean isActive;

    @OneToMany(mappedBy = "owner")
    private List<Hotel> ownedHotels;

    //Hierarchy Admin->Owner->Manager->Receptionist
    @ManyToOne()
    @JoinColumn(name = "created_by")
    private User createdBy;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "createdBy")
    private List<User> createdUsers;

    //Created reservations for receptionists;
    @OneToMany(mappedBy = "receptionist")
    private List<Reservation> createdReservations;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "hotel_id")
    private Hotel assignedHotel;

    @PrePersist
    public void onCreate() {
        createdAt = LocalDateTime.now();
    }
    @PreUpdate
    public void onUpdate() {
        updatedAt = LocalDateTime.now();
    }


}
