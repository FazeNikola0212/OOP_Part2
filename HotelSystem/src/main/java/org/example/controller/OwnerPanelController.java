package org.example.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.example.command.Command;
import org.example.command.SwitchSceneCommand;
import org.example.session.SelectedHotelHolder;

public class OwnerPanelController extends NavigationController {

    private Command listReceptionistsCommand;
    private Command managerOpsCommand;
    private Command listReservationsCommand;
    private Command showAmenitiesCommand;
    private Command listRoomInfoCommand;

    @FXML
    private Label currentHotelLabel;


    @FXML
    public void initialize() {

        Platform.runLater(() -> {
            currentHotelLabel.setText("Current hotel: " + SelectedHotelHolder.getHotel().getName());
            currentHotelLabel.setStyle("-fx-text-fill: lightgray;");

            Stage stage = (Stage) currentHotelLabel.getScene().getWindow();
            listReceptionistsCommand = new SwitchSceneCommand(stage, "/views/list-receptionists.fxml");
            managerOpsCommand = new SwitchSceneCommand(stage, "/views/manager-ops.fxml");
            listReservationsCommand = new SwitchSceneCommand(stage, "/views/list-reservations.fxml");
            showAmenitiesCommand = new SwitchSceneCommand(stage, "/views/show-amenities.fxml");
            listRoomInfoCommand = new SwitchSceneCommand(stage, "/views/list-room-info.fxml");
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

    @Override
    protected Stage getCurrentStage() {
        SelectedHotelHolder.clear();
        return (Stage) currentHotelLabel.getScene().getWindow();
    }
}
