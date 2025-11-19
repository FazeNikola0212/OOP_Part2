package org.example.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.authorization.AuthorizationService;
import org.example.model.user.Role;
import org.example.repository.user.UserRepositoryImpl;
import org.example.service.user.UserService;
import org.example.util.SceneSwitcher;
import javafx.scene.control.Button;

import java.io.IOException;

public class DashboardController {
    private UserService userService = new UserService(new UserRepositoryImpl());

    public DashboardController() {}

    @FXML
    private Button btnCreateUser;

    @FXML
    private Button btnCreateHotel;

    @FXML
    private Button btnLogout;

    @FXML
    public void initialize() {
        if (AuthorizationService.hasRole(Role.MANAGER, Role.RECEPTIONIST)) {
            btnCreateHotel.setDisable(true);
            if (btnCreateHotel.isDisable()) {
                btnCreateHotel.setStyle("-fx-background-color: #f0f0f0");
            }
        }
    }

    @FXML
    public void createUser() throws IOException {
        SceneSwitcher.switchScene((Stage) btnCreateUser.getScene().getWindow(), "/views/creating-user.fxml");
    }

    public void createHotel() throws IOException {
        SceneSwitcher.switchScene((Stage) btnCreateHotel.getScene().getWindow(), "/views/creating-hotel.fxml");
    }

}
