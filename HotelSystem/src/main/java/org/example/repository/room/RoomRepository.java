package org.example.repository.room;

import org.example.model.hotel.Hotel;
import org.example.model.room.Room;
import org.example.repository.baserepository.CrudRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface RoomRepository extends CrudRepository<Room, Long> {
    boolean existsRoomByNumber(String number, Hotel hotel);
    List<Room> findAvailableRooms(LocalDateTime start, LocalDateTime end);
    List<Room> findAllRoomsByHotel(Hotel hotel);

}
