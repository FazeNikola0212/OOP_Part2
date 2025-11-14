package org.example.DTO;

import lombok.*;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.NotBlank;
import org.example.model.user.Role;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RegisterUserRequest {
    private String username;
    private String password;
    private String email;
    private Role role;
    private String fullName;
}
