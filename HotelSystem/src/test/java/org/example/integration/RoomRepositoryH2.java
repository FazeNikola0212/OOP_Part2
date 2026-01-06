package org.example.integration;

import org.example.DTO.RoomDetailsDTO;
import org.example.model.hotel.Hotel;
import org.example.model.room.Room;
import org.example.model.room.RoomStatus;
import org.example.repository.room.RoomRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class RoomRepositoryH2 implements RoomRepository {

    private final Connection conn;

    public RoomRepositoryH2(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void save(Room room) {
        try (PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO room (number, capacity, price_per_night, rating, room_category, room_status, hotel_id) VALUES (?, ?, ?, ?, ?, ?, ?)")) {
            ps.setString(1, room.getNumber());
            ps.setInt(2, room.getCapacity());
            ps.setBigDecimal(3, room.getPricePerNight());
            ps.setDouble(4, room.getRating());
            ps.setString(5, room.getRoomCategory().name());
            ps.setString(6, room.getRoomStatus().name());
            ps.setLong(7, room.getHotel().getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Room findById(Long aLong) {
        return null;
    }

    @Override
    public List<Room> findAll() {
        return List.of();
    }

    @Override
    public void deleteById(Long aLong) {

    }

    @Override
    public void update(Room entity) {

    }

    @Override
    public boolean existsRoomByNumber(String number, Hotel hotel) {
        try (PreparedStatement ps = conn.prepareStatement(
                "SELECT COUNT(*) FROM room WHERE number = ? AND hotel_id = ?")) {
            ps.setString(1, number);
            ps.setLong(2, hotel.getId());
            ResultSet rs = ps.executeQuery();
            rs.next();
            return rs.getInt(1) > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Room> findAvailableRooms(LocalDateTime start, LocalDateTime end) {
        return List.of();
    }

    @Override
    public List<Room> findAllRoomsByHotel(Hotel hotel) {
        return List.of();
    }

    @Override
    public void updateRoomStatus(String roomNumber, RoomStatus roomStatus, Hotel hotel) {

    }

    @Override
    public List<RoomDetailsDTO> findAllRoomsDetailsByHotel(Hotel hotel) {
        return List.of();
    }

    @Override
    public void updateRoomStatusMoreThan1Room(List<String> roomNumber, RoomStatus roomStatus) {

    }
}
