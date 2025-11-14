package org.example.controller;

import javafx.fxml.FXML;

import javafx.event.ActionEvent;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import org.example.DTO.RegisterUserRequest;
import org.example.model.user.Role;
import org.example.repository.user.UserRepository;
import org.example.repository.user.UserRepositoryImpl;
import org.example.service.user.UserService;


public class RegisterController {
    private final UserRepository userRepository = new UserRepositoryImpl();
    private final UserService userService = new UserService(userRepository);

    @FXML
    private TextField usernameField;

    @FXML
    private TextField passwordField;

    @FXML
    private TextField emailField;

    @FXML
    private ChoiceBox<Role> roleChoiceBox;

    @FXML
    private TextField fullnameField;

    @FXML
    public void initialize() {
        roleChoiceBox.getItems().addAll(Role.values());
    }

    @FXML
    private void createUser(ActionEvent event) {
        RegisterUserRequest request = new RegisterUserRequest();
        request.setUsername(usernameField.getText());
        request.setPassword(passwordField.getText());
        request.setEmail(emailField.getText());
        request.setRole(roleChoiceBox.getValue());
        request.setFullName(fullnameField.getText());

        userService.createUser(request);
    }
}
