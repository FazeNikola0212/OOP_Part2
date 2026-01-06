package org.example.repository.room;

import org.example.DTO.RoomDetailsDTO;
import org.example.model.hotel.Hotel;
import org.example.model.room.Room;
import org.example.model.room.RoomStatus;
import org.example.repository.baserepository.CrudRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface RoomRepository extends CrudRepository<Room, Long> {
    boolean existsRoomByNumber(String number, Hotel hotel);
    List<Room> findAvailableRooms(LocalDateTime start, LocalDateTime end);
    List<Room> findAllRoomsByHotel(Hotel hotel);
    List<RoomDetailsDTO> findAllRoomsDetailsByHotel(Hotel hotel);
    void updateRoomStatus(String roomNumber, RoomStatus roomStatus);
    void updateRoomStatusMoreThan1Room(List<String> roomNumber, RoomStatus roomStatus);
}
