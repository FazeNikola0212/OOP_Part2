package org.example.DTO;

import lombok.*;
import org.example.model.user.User;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class CreateHotelRequest {
    private String name;
    private String address;
    private String city;
    private User manager;
    private User owner;
}
