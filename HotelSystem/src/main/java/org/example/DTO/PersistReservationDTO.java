package org.example.DTO;

import lombok.*;
import org.example.model.amenity.Amenity;
import org.example.model.client.Client;
import org.example.model.hotel.Hotel;
import org.example.model.reservation.*;
import org.example.model.room.Room;
import org.example.model.user.User;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PersistReservationDTO {
    private String reservationNumber;
    private Client mainClient;
    private List<Client> guests;
    private User receptionist;
    private ReservationStatus status;
    private ReservationType type;
    private TerminationType terminationType;
    private LocalDateTime createdAt;
    private Hotel hotel;
    private BigDecimal totalPrice;
    private List<ReservationRoomDTO> rooms;
    private List<ReservationAmenityDTO> amenities;
}
