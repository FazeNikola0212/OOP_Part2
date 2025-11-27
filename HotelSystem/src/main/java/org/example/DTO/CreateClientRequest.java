package org.example.DTO;

import lombok.*;
import org.example.model.hotel.Hotel;
import org.example.model.user.User;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CreateClientRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
}
