package org.example.service.room;

import jakarta.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.DTO.CreateRoomDTO;
import org.example.exceptions.ExistingRoomException;
import org.example.model.hotel.Hotel;
import org.example.model.room.Room;
import org.example.repository.room.RoomRepository;

import java.time.LocalDateTime;
import java.util.List;


public class RoomService {
    private final RoomRepository roomRepository;
    private static final Logger log = LogManager.getLogger(RoomService.class);

    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public List<Room> getAllRoomsByHotel(Hotel hotel) {
        return roomRepository.findAllRoomsByHotel(hotel);
    }

    public List<Room> getAvailableRooms(LocalDateTime start, LocalDateTime end) {
        return roomRepository.findAvailableRooms(start, end);
    }

    @Transactional
    public Room createRoom(CreateRoomDTO request) {
        if (roomRepository.existsRoomByNumber(request.getNumber(), request.getHotel())) {
            log.error("Room already exists in: " + request.getHotel().getName());
            throw new ExistingRoomException("Room with this number already exists");
        }

        Room room = Room.builder()
                .number(request.getNumber())
                .capacity(request.getCapacity())
                .pricePerNight(request.getPricePerNight())
                .roomCategory(request.getRoomCategory())
                .roomStatus(request.getRoomStatus())
                .hotel(request.getHotel())
                .rating(0.0)
                .build();

        roomRepository.save(room);
        log.info("Successfully created room with number: " + room.getNumber());
        return room;
    }

}
