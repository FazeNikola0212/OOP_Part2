package org.example.model.reservation;

import jakarta.persistence.*;
import lombok.*;
import org.example.model.client.Client;
import org.example.model.hotel.Hotel;
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

    @ManyToOne(fetch = FetchType.LAZY)
    private Client mainClient;

    @ManyToMany
    @JoinTable(name = "reservation_guests",
            joinColumns = @JoinColumn(name = "reservation_id"),
            inverseJoinColumns = @JoinColumn(name = "client_id"))
    private List<Client> guests;

    @ManyToOne(fetch = FetchType.LAZY)
    private User receptionist;

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

    @ManyToOne(fetch = FetchType.LAZY)
    private Hotel hotel;
    


}
