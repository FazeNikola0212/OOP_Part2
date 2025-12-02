package org.example.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import lombok.Getter;
import org.example.command.Command;
import org.example.command.SwitchSceneCommand;
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
    private Command removeReceptionistCommand;
    private Command createClientCommand;
    private Command createAmenityCommand;

    @FXML
    private Button addReceptionistBtn;

    @FXML
    private Button removeReceptionistBtn;

    @FXML
    private Button createRoomBtn;

    @FXML
    private Label hotelLabel;

    @FXML
    public void initialize() {
        Platform.runLater(() -> {
            RoleStrategy strategy = RoleStrategyFactory.getStrategy(Session.getSession().getLoggedUser().getRole());
            strategy.applyPermissions(this);

            hotelLabel.setText("Hotel " + Session.getSession().getLoggedUser().getAssignedHotel().getName());
            Stage stage =  (Stage) hotelLabel.getScene().getWindow();

            addReceptionistCommand = new SwitchSceneCommand(stage, "/views/add-receptionist.fxml");
            removeReceptionistCommand = new SwitchSceneCommand(stage, "/views/remove-receptionist.fxml");
            createRoomCommand = new SwitchSceneCommand(stage, "/views/creating-room.fxml");
            createClientCommand = new SwitchSceneCommand(stage, "/views/creating-client.fxml");
            createAmenityCommand = new SwitchSceneCommand(stage, "/views/amenities-list.fxml");

        });
    }

    @FXML
    private void addReceptionist() throws Exception {
        addReceptionistCommand.execute();
    }

    @FXML
    private void removeReceptionist() throws Exception {
        removeReceptionistCommand.execute();
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
        createAmenityCommand.execute();
    }

    @Override
    protected Stage getCurrentStage() {
        return (Stage) hotelLabel.getScene().getWindow();
    }
}
