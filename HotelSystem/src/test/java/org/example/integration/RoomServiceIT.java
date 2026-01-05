package org.example.integration;

import org.example.DTO.CreateRoomDTO;
import org.example.exceptions.ExistingRoomException;
import org.example.model.hotel.Hotel;
import org.example.model.room.Room;
import org.example.model.room.RoomCategory;
import org.example.model.room.RoomStatus;
import org.example.repository.room.RoomRepository;
import org.example.service.room.RoomService;
import org.junit.After;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

public class RoomServiceIT {
    private Connection connection;
    private RoomService roomService;
    private Hotel hotel;



    @BeforeEach
    void setUp() throws Exception {
        connection = H2TestDatabase.createConnection();

        SimpleScriptRunner.run(
                connection,
                getClass().getResourceAsStream("/schema-room.sql")
        );

        PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO hotel (name) VALUES (?)",
                Statement.RETURN_GENERATED_KEYS
        );
        ps.setString(1, "Test Hotel");
        ps.executeUpdate();

        ResultSet rs = ps.getGeneratedKeys();
        rs.next();

        hotel = new Hotel();
        hotel.setId(rs.getLong(1));
        hotel.setName("Test Hotel");

        RoomRepository roomRepository =
                new RoomRepositoryH2(connection);

        roomService = new RoomService(roomRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
        connection.prepareStatement("DROP TABLE hotel").execute();
        connection.prepareStatement("DROP TABLE room").execute();
    }

    @Test
    void createRoom_shouldPersistRoom() {
        CreateRoomDTO dto = new CreateRoomDTO();
        dto.setNumber("101");
        dto.setCapacity(2);
        dto.setPricePerNight(BigDecimal.valueOf(120));
        dto.setRoomCategory(RoomCategory.DOUBLE);
        dto.setRoomStatus(RoomStatus.AVAILABLE);
        dto.setHotel(hotel);

        Room room = roomService.createRoom(dto);

        assertNotNull(room);
        assertEquals("101", room.getNumber());
    }

    @Test
    void createRoom_shouldThrowExceptionWhenRoomExists() {
        CreateRoomDTO dto = new CreateRoomDTO();
        dto.setNumber("101");
        dto.setCapacity(2);
        dto.setPricePerNight(BigDecimal.valueOf(120));
        dto.setRoomCategory(RoomCategory.DELUXE);
        dto.setRoomStatus(RoomStatus.AVAILABLE);
        dto.setHotel(hotel);

        roomService.createRoom(dto);

        assertThrows(
                ExistingRoomException.class,
                () -> roomService.createRoom(dto)
        );
    }

}
