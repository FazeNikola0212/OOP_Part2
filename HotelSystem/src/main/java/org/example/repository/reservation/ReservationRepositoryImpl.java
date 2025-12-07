package org.example.repository.reservation;

import javafx.scene.control.Tab;
import org.example.model.reservation.Reservation;
import org.example.repository.baserepository.GenericRepositoryImpl;

public class ReservationRepositoryImpl extends GenericRepositoryImpl<Reservation, Long> implements ReservationRepository {

    public ReservationRepositoryImpl() {
        super(Reservation.class);
    }
}
