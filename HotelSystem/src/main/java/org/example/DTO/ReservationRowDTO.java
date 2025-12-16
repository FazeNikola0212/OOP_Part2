package org.example.DTO;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReservationRowDTO {
    private String clientFullName;
    private String reservationNumber;
    private String terminationType;
    private String type;
    private LocalDateTime createdAt;
    private String roomsNumber;
    private Integer guestsNumber;
    private String receptionistName;
    private String reservationStatus;
    private boolean isChecked;
}
