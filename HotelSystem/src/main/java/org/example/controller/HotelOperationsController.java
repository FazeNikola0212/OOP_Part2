package org.example.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import lombok.Getter;
import org.example.command.Command;
import org.example.command.SwitchSceneCommand;
import org.example.session.SelectedHotelHolder;
import org.example.session.Session;
import org.example.strategy.RoleConfigurable;
import org.example.strategy.RoleStrategy;
import org.example.strategy.RoleStrategyFactory;
import org.example.util.SceneSwitcher;
import javafx.scene.control.Label;

import java.io.IOException;

@Getter
public class HotelOperationsController extends NavigationController implements RoleConfigurable {

    private Command createRoomCommand;
    private Command addReceptionistCommand;
    private Command createClientCommand;
    private Command listAmenitiesCommand;
    private Command createAmenityCommand;
    private Command listReceptionistsCommand;
    private Command createReservationCommand;
    private Command listReservationsCommand;
    private Command notificationList;

    @FXML private Button addReceptionistBtn;

    @FXML private Button createRoomBtn;

    @FXML private Button createClientBtn;

    @FXML private Button listAmenitiesBtn;

    @FXML private Button createAmenityBtn;

    @FXML private Button listReceptionistsBtn;

    @FXML private Button listReservationsBtn;

    @FXML private Label hotelLabel;

    @FXML
    public void initialize() {
        Platform.runLater(() -> {
            RoleStrategy strategy = RoleStrategyFactory.getStrategy(Session.getSession().getLoggedUser().getRole());
            strategy.applyPermissions(this);

            hotelLabel.setText("Hotel " + SelectedHotelHolder.getHotel().getName());
            Stage stage =  (Stage) hotelLabel.getScene().getWindow();

            addReceptionistCommand = new SwitchSceneCommand(stage, "/views/add-receptionist.fxml");
            createRoomCommand = new SwitchSceneCommand(stage, "/views/creating-room.fxml");
            createClientCommand = new SwitchSceneCommand(stage, "/views/creating-client.fxml");
            listAmenitiesCommand = new SwitchSceneCommand(stage, "/views/amenities-list.fxml");
            createAmenityCommand = new SwitchSceneCommand(stage, "/views/creating-amenity.fxml");
            listReceptionistsCommand = new SwitchSceneCommand(stage, "/views/receptionists-list.fxml");
            createReservationCommand = new SwitchSceneCommand(stage, "/views/creating-reservation.fxml");
            listReservationsCommand = new SwitchSceneCommand(stage, "/views/reservations-list.fxml");
            notificationList = new SwitchSceneCommand(stage, "/views/notifications-list.fxml");

        });
    }

    @FXML
    private void addReceptionist() throws Exception {
        addReceptionistCommand.execute();
    }

    @FXML
    private void listNotifications() throws Exception {
        notificationList.execute();
    }

    @FXML
    private void createRoom() throws Exception {
        createRoomCommand.execute();
    }

    @FXML
    private void createClient() throws Exception {
        createClientCommand.execute();
    }

    @FXML
    private void listAmenity() throws Exception {
        listAmenitiesCommand.execute();
    }

    @FXML
    private void createAmenity() throws Exception {
        createAmenityCommand.execute();
    }

    @FXML
    private void listReceptionists() throws Exception {
        listReceptionistsCommand.execute();
    }

    @FXML
    private void createReservation() throws Exception {
        createReservationCommand.execute();
    }

    @FXML
    private void listReservations() throws Exception {
        listReservationsCommand.execute();
    }

    @Override
    protected Stage getCurrentStage() {
        return (Stage) hotelLabel.getScene().getWindow();
    }
}
