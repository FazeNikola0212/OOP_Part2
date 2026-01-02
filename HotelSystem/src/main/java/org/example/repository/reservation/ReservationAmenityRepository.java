package org.example.repository.reservation;

import org.example.model.reservation.ReservationAmenity;
import org.example.repository.baserepository.CrudRepository;

import java.util.List;

public interface ReservationAmenityRepository extends CrudRepository<ReservationAmenity,Long> {
    List<String> findAmenityNamesByReservationId(Long reservationId);
}
