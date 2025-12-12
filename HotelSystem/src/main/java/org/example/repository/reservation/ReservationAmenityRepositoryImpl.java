package org.example.repository.reservation;

import org.example.model.reservation.ReservationAmenity;
import org.example.repository.baserepository.CrudRepository;
import org.example.repository.baserepository.GenericRepositoryImpl;

public class ReservationAmenityRepositoryImpl extends GenericRepositoryImpl<ReservationAmenity, Long> implements ReservationAmenityRepository {
    public ReservationAmenityRepositoryImpl() {
        super(ReservationAmenity.class);
    }
}
