package org.example.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import lombok.Getter;
import org.example.factory.ServiceFactory;
import org.example.model.user.Role;
import org.example.service.user.UserService;
import org.example.session.Session;
import org.example.command.Command;
import org.example.strategy.RoleConfigurable;
import org.example.strategy.RoleStrategy;
import org.example.strategy.RoleStrategyFactory;
import javafx.scene.control.Label;

import javafx.scene.control.Button;
import org.example.command.SwitchSceneCommand;


@Getter
public class DashboardController extends NavigationController implements RoleConfigurable {
    private final UserService userService = ServiceFactory.getUserService();

    private Command createUserCommand;
    private Command createHotelCommand;
    private Command redirectHotelOpsCommand;
    private Command selectOwnedHotelsCommand;

    @FXML
    private Label welcomeLabel;

    @FXML
    private Button btnCreateUser;

    @FXML
    private Button btnCreateHotel;


    @FXML
    private Button btnCreateAmenity;

    @FXML
    private Button btnHotelOps;

    @FXML
    private Button selectHotelBtn;


    @FXML
    public void initialize() {
        Platform.runLater(() -> {
            Stage stage = (Stage) welcomeLabel.getScene().getWindow();
            createUserCommand = new SwitchSceneCommand(stage, "/views/creating-user.fxml");
            createHotelCommand = new SwitchSceneCommand(stage, "/views/creating-hotel.fxml");
            redirectHotelOpsCommand = new SwitchSceneCommand(stage, "/views/hotel-operations.fxml");
            selectOwnedHotelsCommand = new SwitchSceneCommand(stage, "/views/owned-hotels.fxml");


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
    private void hotelOperations() throws Exception {
        redirectHotelOpsCommand.execute();
    }

    @FXML
    private void selectHotel() throws Exception {
        selectOwnedHotelsCommand.execute();
    }

    protected Stage getCurrentStage() {
        return (Stage) welcomeLabel.getScene().getWindow();
    }

}
