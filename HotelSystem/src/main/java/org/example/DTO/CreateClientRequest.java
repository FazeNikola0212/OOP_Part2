package org.example.DTO;

import lombok.*;

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
