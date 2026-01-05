package org.example.unit.service;

import org.example.model.hotel.Hotel;
import org.example.model.room.Room;
import org.example.repository.room.RoomRepository;
import org.example.service.room.RoomService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class RoomServiceTest {

    @Mock
    RoomRepository roomRepository;

    @InjectMocks
    RoomService roomService;

    @BeforeEach
    void setup() {
        roomService = new RoomService(roomRepository);

    }

    @Test
    void getAllRoomsByHotel_shouldReturnRooms() {
        Hotel hotel = new Hotel();

        when(roomRepository.findAllRoomsByHotel(hotel)).thenReturn(List.of(new Room()));

        List<Room> rooms = roomService.getAllRoomsByHotel(hotel);

        assertEquals(1, rooms.size());
    }
}
