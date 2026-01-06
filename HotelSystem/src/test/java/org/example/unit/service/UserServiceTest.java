package org.example.unit.service;

import org.example.DTO.RegisterUserRequest;
import org.example.exceptions.InvalidUserNameException;
import org.example.exceptions.PasswordRequiredException;
import org.example.model.user.Role;
import org.example.model.user.User;
import org.example.repository.hotel.HotelRepository;
import org.example.repository.user.UserRepository;
import org.example.service.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mindrot.jbcrypt.BCrypt;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private HotelRepository hotelRepository;

    @InjectMocks
    private UserService userService;

    private RegisterUserRequest  registerUserRequest;
    private User user;

    @BeforeEach
    public void setUp() {
        user = User.builder()
                .username("testuser")
                .password(BCrypt.hashpw("password", BCrypt.gensalt()))
                .isActive(false)
                .build();
    }


    @Test
    void getUserByUsername_success() {
        when(userRepository.findByUsername("testuser")).thenReturn(user);

        User result = userService.getUserByUsername("testuser");

        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
    }

    @Test
    void getUserByUsername_notFound() {
        when(userRepository.findByUsername("missing")).thenReturn(null);

        assertThrows(
                InvalidUserNameException.class,
                () -> userService.getUserByUsername("missing")
        );
    }


    @Test
    void loginUser_success() {
        when(userRepository.findByUsername("testuser")).thenReturn(user);
        when(userRepository.isActive(user)).thenReturn(false);

        User logged = userService.loginUser("testuser", "password");

        assertNotNull(logged);
        assertEquals("testuser", logged.getUsername());
    }

    @Test
    void loginUser_wrongPassword() {
        when(userRepository.findByUsername("testuser")).thenReturn(user);
        when(userRepository.isActive(user)).thenReturn(false);

        assertThrows(
                InvalidUserNameException.class,
                () -> userService.loginUser("testuser", "wrongpass")
        );
    }

    @Test
    void createUser_success() {
        RegisterUserRequest request = new RegisterUserRequest();
        request.setUsername("newuser");
        request.setPassword("123456");
        request.setEmail("test@mail.com");
        request.setRole(Role.ADMIN);
        request.setFullName("Test User");

        when(userRepository.findByUsername("newuser")).thenReturn(null);

        User created = userService.createUser(request);

        assertNotNull(created);
        assertEquals("newuser", created.getUsername());
    }

    @Test
    void createUser_usernameAlreadyExists() {
        RegisterUserRequest request = new RegisterUserRequest();
        request.setUsername("testuser");

        when(userRepository.findByUsername("testuser")).thenReturn(user);

        assertThrows(
                InvalidUserNameException.class,
                () -> userService.createUser(request)
        );

    }

    @Test
    void createUser_passwordMissing() {
        RegisterUserRequest request = new RegisterUserRequest();
        request.setUsername("newuser");
        request.setPassword("");

        when(userRepository.findByUsername("newuser")).thenReturn(null);

        assertThrows(
                PasswordRequiredException.class,
                () -> userService.createUser(request)
        );
    }
}
