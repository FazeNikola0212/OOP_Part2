package org.example.repository.room;

import org.example.model.hotel.Hotel;
import org.example.model.room.Room;
import org.example.repository.baserepository.CrudRepository;

public interface RoomRepository extends CrudRepository<Room, Long> {
    boolean existsRoomByNumber(String number, Hotel hotel);
}
