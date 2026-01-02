package org.example.service.notification;

import lombok.*;
import org.example.repository.reservation.ReservationRepository;

@Getter
@Setter
@NoArgsConstructor
@Builder
public class NotificationService {
    private ReservationRepository reservationRepository;

    public NotificationService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }



}
