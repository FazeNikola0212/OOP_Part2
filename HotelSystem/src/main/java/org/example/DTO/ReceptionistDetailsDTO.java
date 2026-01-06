package org.example.DTO;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Data
@Getter
@Setter
@Builder
public class ReceptionistDetailsDTO {
    private String username;
    private String fullName;
    private String hotelName;
    private String createdBy;
    private Integer amenitiesAssigned;
    private Integer reservationsAssigned;
    private BigDecimal revenuePrice;
    private Integer clientsAssigned;
}
