package org.example.DTO;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReservationRowDTO {
    private Long id;
    private String clientFullName;
    private String reservationNumber;
    private String terminationType;
    private BigDecimal totalPrice;
    private String type;
    private LocalDateTime createdAt;
    private String roomsNumber;
    private Integer guestsNumber;
    private String receptionistName;
    private String reservationStatus;
    private boolean isChecked;
}
