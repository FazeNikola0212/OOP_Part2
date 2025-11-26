package org.example.model.reservation;

import jakarta.persistence.*;
import lombok.*;
import org.example.model.amenity.Amenity;

import java.math.BigDecimal;

@Entity
@Table(name = "reservation_services")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ReservationAmenity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne()
    @JoinColumn(name = "reservation_id")
    private Reservation Reservation;

    @ManyToOne()
    @JoinColumn(name = "amenity_id")
    private Amenity amenity;

    private int quantity;
    private BigDecimal price;
}
