package org.example.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;
import org.example.command.Command;
import org.example.command.SwitchSceneCommand;
import org.example.session.SelectedHotelHolder;

@Getter
@Setter
public class OwnerPanelController extends NavigationController {

    private Command listReceptionistsCommand;
    private Command managerOpsCommand;
    private Command listReservationsCommand;
    private Command showAmenitiesCommand;
    private Command listRoomInfoCommand;
    private Command createRoomCommand;
    private Command createAmenityCommand;
    private Command addReceptionistCommand;

    @FXML
    private Label currentHotelLabel;


    @FXML
    public void initialize() {

        Platform.runLater(() -> {

            Stage stage = (Stage) currentHotelLabel.getScene().getWindow();
            listReceptionistsCommand = new SwitchSceneCommand(stage, "/views/receptionists-list.fxml");
            managerOpsCommand = new SwitchSceneCommand(stage, "/views/manager-operations.fxml");
            listReservationsCommand = new SwitchSceneCommand(stage, "/views/reservations-list.fxml");
            showAmenitiesCommand = new SwitchSceneCommand(stage, "/views/amenities-list.fxml");
            listRoomInfoCommand = new SwitchSceneCommand(stage, "/views/list-room-info.fxml");
            createRoomCommand = new SwitchSceneCommand(stage, "/views/creating-room.fxml");
            createAmenityCommand = new SwitchSceneCommand(stage, "/views/creating-amenity.fxml");
            addReceptionistCommand = new SwitchSceneCommand(stage, "/views/add-receptionist.fxml");
        });

    }

    @FXML
    private void listReceptionists() throws Exception {
        listReceptionistsCommand.execute();
    }

    @FXML
    private void listReservations() throws Exception {
        listReservationsCommand.execute();
    }

    @FXML
    private void showManager() throws Exception {
        managerOpsCommand.execute();
    }

    @FXML
    private void showAmenities() throws Exception {
        showAmenitiesCommand.execute();
    }

    @FXML
    private void listRoomInfo() throws Exception {
        listRoomInfoCommand.execute();
    }

    @FXML
    private void createRoom() throws Exception {
        createRoomCommand.execute();
    }

    @FXML
    private void createAmenity() throws Exception {
        createAmenityCommand.execute();
    }

    @FXML
    private void addReceptionist() throws Exception {
        addReceptionistCommand.execute();
    }


    @Override
    protected Stage getCurrentStage() {
        SelectedHotelHolder.clear();
        return (Stage) currentHotelLabel.getScene().getWindow();
    }
}
