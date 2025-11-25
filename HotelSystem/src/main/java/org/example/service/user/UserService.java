package org.example.service.user;

import jakarta.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.DTO.RegisterUserRequest;
import org.example.exceptions.InvalidEmailException;
import org.example.exceptions.InvalidUserNameException;
import org.example.exceptions.PasswordRequiredException;
import org.example.model.user.User;
import org.example.repository.user.UserRepository;
import org.example.session.Session;
import org.example.util.AlertMessage;
import org.mindrot.jbcrypt.BCrypt;

import java.time.LocalDateTime;
import java.util.List;

public class UserService {
    private static final Logger log = LogManager.getLogger(UserService.class);
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUserByUsername(String username) {
        if (userRepository.findByUsername(username) == null) {
            throw new InvalidUserNameException(username);
        }
        return userRepository.findByUsername(username);
    }

    public List<User> getAllManagers() {
        return userRepository.findAllManagers();
    }

    public User loginUser(String username, String password) {
        if (userRepository.findByUsername(username) == null) {
            log.error("Incorrect username or password");
            throw new InvalidUserNameException(username);
        }
        if (BCrypt.checkpw(password, userRepository.findByUsername(username).getPassword())) {
            log.info("Successfully logged in USER : " + username);
            return userRepository.findByUsername(username);
        } else {
            log.error("Incorrect username or password");
            throw new InvalidUserNameException(username);
        }
    }

    @Transactional
    public User createUser(RegisterUserRequest request) {

        if (userRepository.findByUsername(request.getUsername()) != null) {
            log.error("Username already exists");
            throw new InvalidUserNameException("The username is already taken " + request.getUsername());
        }
        if (request.getUsername() == null || request.getUsername().isEmpty()) {
            log.error("Username field is empty");
            throw new InvalidUserNameException("The username is required");
        }
        if (request.getPassword() == null || request.getPassword().isEmpty()) {
            log.error("Password field is empty");
            throw new PasswordRequiredException("The password is required");
        }
        if (request.getEmail() == null || !request.getEmail().contains("@")) {
            log.error("Email field is empty or the email is not valid");
            throw new InvalidEmailException("Required email address or is not valid");
        }

        User user = User.builder()
                .username(request.getUsername())
                .password(BCrypt.hashpw(request.getPassword(), BCrypt.gensalt()))
                .email(request.getEmail())
                .role(request.getRole())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .fullName(request.getFullName())
                .isActive(true)
                .createdBy(Session.getSession().getLoggedUser())
                .build();
        userRepository.save(user);
        log.info("USER " + user.getUsername() + " HAS BEEN CREATED BY " + Session.getSession().getLoggedUser());

        return user;
    }
}
