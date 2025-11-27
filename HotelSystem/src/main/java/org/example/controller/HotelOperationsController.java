package org.example.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import lombok.Getter;
import org.example.command.BackCommand;
import org.example.command.LogoutCommand;
import org.example.session.Session;
import org.example.util.SceneSwitcher;
import javafx.scene.control.Label;

import java.io.IOException;

@Getter
public class HotelOperationsController {

    @FXML
    private Button addReceptionistButton;

    @FXML
    private Label hotelLabel;

    @FXML
    private Button backBtn;

    @FXML
    private Button logoutBtn;

    @FXML
    public void initialize() {
        hotelLabel.setText("Hotel " + Session.getSession().getLoggedUser().getAssignedHotel().getName());
    }

    @FXML
    private void addReceptionist() throws IOException {
        SceneSwitcher.switchScene((Stage) addReceptionistButton.getScene().getWindow(), "/views/add-receptionist.fxml");
    }

    @FXML
    private void goBack(ActionEvent event) {
        Stage  stage = (Stage) backBtn.getScene().getWindow();
        new BackCommand(stage).execute();
    }

    @FXML
    private void logout(ActionEvent event) {
        Stage stage = (Stage) logoutBtn.getScene().getWindow();
        new LogoutCommand(stage).execute();
    }

}
