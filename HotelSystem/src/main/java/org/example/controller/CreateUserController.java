package org.example.controller;

import javafx.fxml.FXML;

import javafx.event.ActionEvent;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import org.example.DTO.RegisterUserRequest;
import org.example.authorization.AuthorizationService;
import org.example.model.user.Role;
import org.example.repository.user.UserRepository;
import org.example.repository.user.UserRepositoryImpl;
import org.example.service.user.UserService;
import org.example.util.AlertMessage;
import org.example.util.SceneSwitcher;

import java.io.IOException;



public class CreateUserController {
    private final UserRepository userRepository = new UserRepositoryImpl();
    private final UserService userService = new UserService(userRepository);

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField emailField;

    @FXML
    private ChoiceBox<Role> roleChoiceBox;

    @FXML
    private TextField fullnameField;

    @FXML
    private Button backBtn;

    @FXML
    private Button logoutBtn;

    @FXML
    public void initialize() {
        if (AuthorizationService.hasRole(Role.ADMIN)) {
            roleChoiceBox.getItems().add(Role.ADMIN);
            roleChoiceBox.getItems().add(Role.OWNER);
        } else if  (AuthorizationService.hasRole(Role.OWNER)) {
            roleChoiceBox.getItems().add(Role.OWNER);
            roleChoiceBox.getItems().add(Role.MANAGER);
        } else if (AuthorizationService.hasRole(Role.MANAGER)) {
            roleChoiceBox.getItems().add(Role.RECEPTIONIST);
        }
    }

    @FXML
    private void createUser(ActionEvent event) throws  IOException {
        RegisterUserRequest request = new RegisterUserRequest();
        request.setUsername(usernameField.getText());
        request.setPassword(passwordField.getText());
        request.setEmail(emailField.getText());
        request.setRole(roleChoiceBox.getValue());
        request.setFullName(fullnameField.getText());

        userService.createUser(request);
        AlertMessage.showMessage("User Creation", "User " +  request.getUsername() + " has been created successfully");
        SceneSwitcher.goBack((Stage) backBtn.getScene().getWindow());
    }

    @FXML
    private void goBack(ActionEvent event) throws IOException {
        SceneSwitcher.goBack((Stage) backBtn.getScene().getWindow());
    }

    @FXML
    private void logout(ActionEvent event) throws IOException {
        SceneSwitcher.goLogout((Stage) logoutBtn.getScene().getWindow());
    }
}
