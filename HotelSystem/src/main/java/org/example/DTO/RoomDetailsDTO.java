package org.example.DTO;

import lombok.*;
import org.example.model.room.RoomCategory;
import org.example.model.room.RoomStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RoomDetailsDTO {
    private String number;
    private RoomCategory category;
    private BigDecimal price;
    private RoomStatus status;
    private Double rating;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

}
