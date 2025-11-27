package org.example.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.example.util.SceneSwitcher;
import javafx.scene.control.Label;

import java.io.IOException;

public class HotelOperationsController {

    @FXML
    private Button addReceptionistButton;

    @FXML
    private Label hotelLabel;

    @FXML
    public void initialize() {
    }

    @FXML
    private void addReceptionist() throws IOException {
        SceneSwitcher.switchScene((Stage) addReceptionistButton.getScene().getWindow(), "/views/add-receptionist.fxml");
    }

}
