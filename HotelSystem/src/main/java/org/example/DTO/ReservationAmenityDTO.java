package org.example.DTO;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.example.model.amenity.Amenity;

import java.math.BigDecimal;

@Data
@Getter
@Setter
public class ReservationAmenityDTO {
    private Amenity amenity;
    private String name;
    private int quantity;
    private BigDecimal price;
}
