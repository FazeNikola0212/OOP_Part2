package org.example.model.reservation;

import jakarta.persistence.*;
import lombok.*;
import org.example.model.additional_services.AdditionalService;

import java.math.BigDecimal;

@Entity
@Table(name = "reservation_services")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ReservationService {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne()
    @JoinColumn(name = "reservation_id")
    private Reservation Reservation;

    @ManyToOne()
    @JoinColumn(name = "service_id")
    private AdditionalService additionalService;

    private int quantity;
    private BigDecimal price;
}
