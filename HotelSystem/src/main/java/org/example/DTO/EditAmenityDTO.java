package org.example.DTO;

import lombok.*;
import org.example.model.amenity.SeasonAmenity;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EditAmenityDTO {
    private Long id;
    private String name;
    private String description;
    private SeasonAmenity seasonAmenity;
    private boolean enabled;
}
