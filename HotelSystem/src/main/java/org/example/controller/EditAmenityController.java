package org.example.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.DTO.EditAmenityDTO;
import org.example.factory.ServiceFactory;
import org.example.model.amenity.SeasonAmenity;
import org.example.service.amenity.AmenityService;
import org.example.util.AlertMessage;


public class EditAmenityController extends NavigationController {
    private final AmenityService amenityService = ServiceFactory.getAmenityService();
    private EditAmenityDTO dto;

    public void setAmenity(EditAmenityDTO dto) {
        this.dto = dto;
        loadData();
    }
    @FXML private Label welcomeLabel;

    @FXML private TextField nameField;

    @FXML private TextArea descriptionField;

    @FXML private ChoiceBox<SeasonAmenity> seasonAmenityChoiceBox;

    @FXML private CheckBox isEnabledField;


    @FXML
    private void initialize() {
        welcomeLabel.setText("Edit Amenity");
        seasonAmenityChoiceBox.getItems().addAll(SeasonAmenity.values());
    }

    private void loadData() {
        if (dto != null) {
            nameField.setText(dto.getName());
            descriptionField.setText(dto.getDescription());
            seasonAmenityChoiceBox.setValue(dto.getSeasonAmenity());
            isEnabledField.setSelected(dto.isEnabled());
        }
    }

    @FXML
    private void editAmenity() {
        if (dto == null) return;

        dto.setName(nameField.getText());
        dto.setDescription(descriptionField.getText());
        dto.setSeasonAmenity(seasonAmenityChoiceBox.getValue());
        dto.setEnabled(isEnabledField.isSelected());

        amenityService.updateAmenity(dto);
        AlertMessage.showMessage("Edited Amenity", "Successfully edited amenity");
    }

    @Override
    protected Stage getCurrentStage() {
        return (Stage) welcomeLabel.getScene().getWindow();
    }
}
