package org.example.model.reservation;

import jakarta.persistence.*;
import lombok.*;
import org.example.model.client.Client;
import org.example.model.room.Room;
import org.example.model.user.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "reservations")
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String reservationNumber;

    @ManyToOne(fetch = FetchType.EAGER)
    private Client reservor;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "reservation_guests",
            joinColumns = @JoinColumn(name = "reservation_id"),
            inverseJoinColumns = @JoinColumn(name = "client_id"))
    private List<Client> guests;

    @ManyToOne(fetch = FetchType.EAGER)
    private User receptionist;

    @OneToMany(fetch = FetchType.EAGER)
    private Set<Room> room;

    @Column(nullable = false)
    private LocalDateTime checkIn;

    @Column(nullable = false)
    private LocalDateTime checkOut;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReservationStatus status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReservationType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TerminationType terminationType;

    private LocalDateTime createdAt;

}
