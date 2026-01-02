package org.example.repository.reservation;

import org.example.model.client.Client;
import org.example.model.hotel.Hotel;
import org.example.model.reservation.Reservation;
import org.example.model.reservation.ReservationStatus;
import org.example.repository.baserepository.CrudRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationRepository extends CrudRepository<Reservation, Long> {
    List<Reservation> findAllByReservationStatus(ReservationStatus reservationStatus);
    int expireNoShowReservations(LocalDateTime threshold);
    List<Reservation> findAllByHotel(Hotel hotel);
    List<Client> findClientsWithExpiredNoShows(LocalDateTime threshold);
    List<Client> findAllClientsByReservationId(Long reservationId);
    Reservation findByIdWithClient(Long reservationId);
}
