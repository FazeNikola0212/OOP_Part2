package org.example.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lombok.Getter;
import org.example.model.amenity.SeasonAmenity;
import org.example.repository.amenity.AmenityRepositoryImpl;
import org.example.service.amenity.AmenityService;
import org.example.strategy.RoleConfigurable;
import javafx.scene.control.Button;
import org.example.util.AlertMessage;
import org.example.util.SceneSwitcher;

import java.io.IOException;

@Getter
public class CreateAmenityController implements RoleConfigurable {

    private final AmenityService amenityService = new AmenityService(new AmenityRepositoryImpl());

    @FXML
    private TextField nameField;
    @FXML
    private ChoiceBox<SeasonAmenity> seasonChoiceBox;
    @FXML
    private TextArea descriptionArea;
    @FXML
    private Button createBtn;
    @FXML
    private Button backBtn;
    @FXML
    private Button logoutBtn;

    @FXML
    public void initialize() {
        seasonChoiceBox.getItems().addAll(SeasonAmenity.values());
    }

    @FXML
    private void createAmenity(ActionEvent event) {
        amenityService.createAmenity(nameField.getText(), descriptionArea.getText(), seasonChoiceBox.getValue());
        AlertMessage.showMessage("Amenity Creation", "Amenity with " +  nameField.getText() + " was successfully created.");
    }

    @FXML
    private void goBack(ActionEvent event) throws IOException {
        SceneSwitcher.goBack((Stage) createBtn.getScene().getWindow());
    }

    @FXML
    private void logout(ActionEvent event) throws IOException {
        SceneSwitcher.goLogout((Stage) createBtn.getScene().getWindow());
    }
}
