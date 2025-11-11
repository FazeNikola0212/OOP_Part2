package org.example.service.user;

import org.example.exceptions.InvalidUserNameException;
import org.example.model.user.User;
import org.example.repository.user.UserRepository;

public class UserService {
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
}
