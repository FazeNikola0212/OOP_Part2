package org.example.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import org.example.authorization.AuthorizationService;
import org.example.model.user.Role;
import org.example.repository.user.UserRepositoryImpl;
import org.example.service.user.UserService;
import org.example.session.Session;
import org.example.util.SceneSwitcher;
import javafx.scene.control.Label;

import javafx.scene.control.Button;

import java.io.IOException;

public class DashboardController {
    private UserService userService = new UserService(new UserRepositoryImpl());

    @FXML
    private Label welcomeLabel;

    @FXML
    private Button btnCreateUser;

    @FXML
    private Button btnCreateHotel;

    @FXML
    private Button logoutBtn;

    @FXML
    public void initialize() {
        welcomeLabel.setText("Welcome " + Session.getSession().getLoggedUser().getUsername());
        welcomeLabel.setFont(Font.font("Arial", FontWeight.BOLD, 30));
        if (AuthorizationService.hasRole(Role.MANAGER, Role.RECEPTIONIST)) {
            btnCreateHotel.setDisable(true);
            if (btnCreateHotel.isDisable()) {
                btnCreateHotel.setStyle("-fx-background-color: #f0f0f0");
            }
        }
        if (AuthorizationService.hasRole(Role.RECEPTIONIST)) {
            btnCreateUser.setDisable(true);
            if (btnCreateUser.isDisable()) {
                btnCreateUser.setStyle("-fx-background-color: #f0f0f0");
            }
        }

    }

    @FXML
    private void createUser() throws IOException {
        SceneSwitcher.switchScene((Stage) btnCreateUser.getScene().getWindow(), "/views/creating-user.fxml");
    }

    @FXML
    private void createHotel() throws IOException {
        SceneSwitcher.switchScene((Stage) btnCreateHotel.getScene().getWindow(), "/views/creating-hotel.fxml");
    }
    @FXML
    private void logout() throws IOException {
        SceneSwitcher.goLogout((Stage) logoutBtn.getScene().getWindow());
    }

}
