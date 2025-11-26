package org.example.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import lombok.Getter;
import org.example.model.user.Role;
import org.example.repository.user.UserRepositoryImpl;
import org.example.service.user.UserService;
import org.example.session.Session;
import org.example.command.Command;
import org.example.strategy.RoleConfigurable;
import org.example.strategy.RoleStrategy;
import org.example.strategy.RoleStrategyFactory;
import org.example.util.SceneSwitcher;
import javafx.scene.control.Label;

import javafx.scene.control.Button;
import org.example.command.SwitchSceneCommand;

import java.io.IOException;

@Getter
public class DashboardController implements RoleConfigurable {
    private UserService userService = new UserService(new UserRepositoryImpl());

    private Command createUserCommand;
    private Command createHotelCommand;
    private Command createClientCommand;
    private Command createAmenityCommand;
    private Command redirectHotelOps;

    @FXML
    private Label welcomeLabel;

    @FXML
    private Button btnCreateUser;

    @FXML
    private Button btnCreateHotel;

    @FXML
    private Button btnCreateClient;

    @FXML
    private Button btnCreateAmenity;

    @FXML
    private Button btnHotelOps;

    @FXML
    private Button logoutBtn;

    @FXML
    public void initialize() {
        Platform.runLater(() -> {
            Stage stage = (Stage) welcomeLabel.getScene().getWindow();
            createUserCommand = new SwitchSceneCommand(stage, "/views/creating-user.fxml");
            createHotelCommand = new SwitchSceneCommand(stage, "/views/creating-hotel.fxml");
            createClientCommand = new SwitchSceneCommand(stage, "/views/creating-client.fxml");
            createAmenityCommand = new SwitchSceneCommand(stage, "/views/creating-amenity.fxml");
            redirectHotelOps = new SwitchSceneCommand(stage, "/views/hotel-operations.fxml");


            welcomeLabel.setText("Welcome " + Session.getSession().getLoggedUser().getUsername()
                    + " - " + Session.getSession().getLoggedUser().getRole().name().toLowerCase());
            welcomeLabel.setFont(Font.font("Arial", FontWeight.BOLD, 30));
            welcomeLabel.setPrefWidth(635);
            Role userRole = Session.getSession().getLoggedUser().getRole();

            RoleStrategy strategy = RoleStrategyFactory.getStrategy(userRole);

            strategy.applyPermissions(this);
        });
    }

    @FXML
    private void createUser() throws Exception {
        createUserCommand.execute();
    }

    @FXML
    private void createHotel() throws Exception {
        createHotelCommand.execute();
    }

    @FXML
    private void createClient() throws Exception {
        createClientCommand.execute();
    }

    @FXML
    private void createAmenity() throws Exception {
        createAmenityCommand.execute();
    }

    @FXML
    private void hotelOperations() throws Exception {
        redirectHotelOps.execute();
    }

    @FXML
    private void logout() throws IOException {
        SceneSwitcher.goLogout((Stage) logoutBtn.getScene().getWindow());
    }

}
