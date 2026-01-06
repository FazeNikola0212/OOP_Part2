package org.example.unit.service;

import org.example.DTO.CreateRoomDTO;
import org.example.exceptions.ExistingRoomException;
import org.example.model.hotel.Hotel;
import org.example.model.room.Room;
import org.example.model.room.RoomCategory;
import org.example.repository.room.RoomRepository;
import org.example.service.room.RoomService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RoomServiceTest {
    @Mock
    private RoomRepository roomRepository;

    private Hotel hotel;
    private CreateRoomDTO dto;

    @InjectMocks
    private RoomService roomService;


    @BeforeEach
    public void setUp() {
        hotel = Hotel.builder().name("reina del mar")
                .build();

        dto = CreateRoomDTO.builder().number("102")
                .capacity(4)
                .roomCategory(RoomCategory.DELUXE)
                .pricePerNight(BigDecimal.valueOf(250.00))
                .hotel(hotel).build();

    }

    @Test
    void createRoom_shouldCreateRoom() {
        when(roomRepository.existsRoomByNumber("102", hotel)).thenReturn(false);

        Room room = roomService.createRoom(dto);

        assertThat(room.getNumber()).isEqualTo("102");
        assertThat(room.getRoomCategory()).isEqualTo(RoomCategory.DELUXE);
        assertThat(room.getPricePerNight()).isEqualTo(BigDecimal.valueOf(250.00));
        assertThat(room.getHotel()).isEqualTo(hotel);
    }

    @Test
    void createRoom_shouldThrowException() {
        when(roomRepository.existsRoomByNumber("102", hotel)).thenReturn(true);
        assertThrows(ExistingRoomException.class, () -> roomService.createRoom(dto));
    }

}
