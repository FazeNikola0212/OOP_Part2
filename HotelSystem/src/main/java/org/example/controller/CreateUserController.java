package org.example.controller;

import javafx.fxml.FXML;

import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.stage.Stage;
import lombok.Getter;
import org.example.DTO.RegisterUserRequest;
import org.example.factory.ServiceFactory;
import org.example.model.user.Role;
import org.example.service.user.UserService;
import org.example.session.Session;
import org.example.strategy.RoleConfigurable;
import org.example.strategy.RoleStrategy;
import org.example.strategy.RoleStrategyFactory;
import org.example.util.AlertMessage;
import org.example.util.SceneSwitcher;

import java.io.IOException;

@Getter
public class CreateUserController extends NavigationController implements RoleConfigurable {
    private final UserService userService = ServiceFactory.getUserService();

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
    private Label headingLabel;

    @FXML
    public void initialize() {
        RoleStrategy strategy = RoleStrategyFactory.getStrategy(Session.getSession().getLoggedUser().getRole());

        strategy.applyPermissions(this);
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
        SceneSwitcher.goBack((Stage) headingLabel.getScene().getWindow());
    }

    @Override
    protected Stage getCurrentStage() {
        return (Stage) headingLabel.getScene().getWindow();
    }
}
