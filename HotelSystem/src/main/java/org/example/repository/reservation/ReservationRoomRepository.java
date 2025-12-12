package org.example.repository.reservation;

import org.example.model.reservation.ReservationRoom;
import org.example.model.room.Room;
import org.example.repository.baserepository.CrudRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationRoomRepository extends CrudRepository<ReservationRoom,Long> {
    List<ReservationRoom> findOverlappingReservations(Room room, LocalDateTime startDate, LocalDateTime endDate);
}
