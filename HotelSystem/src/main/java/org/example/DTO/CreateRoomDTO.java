package org.example.DTO;

import lombok.*;
import org.example.model.hotel.Hotel;
import org.example.model.room.RoomCategory;
import org.example.model.room.RoomStatus;

import java.math.BigDecimal;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateRoomDTO {
    private String number;
    private int capacity;
    private BigDecimal pricePerNight;
    private RoomCategory roomCategory;
    private RoomStatus roomStatus;
    private Hotel hotel;
}
