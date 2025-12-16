package org.example.DTO;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.example.model.room.Room;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Getter
@Setter
public class ReservationRoomDTO {
    private Room room;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private BigDecimal price;
}
