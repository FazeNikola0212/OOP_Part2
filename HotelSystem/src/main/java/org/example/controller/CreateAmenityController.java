package org.example.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;
import org.example.factory.ServiceFactory;
import org.example.model.amenity.SeasonAmenity;
import org.example.model.hotel.Hotel;
import org.example.model.user.Role;
import org.example.service.amenity.AmenityService;
import org.example.session.Session;
import org.example.strategy.RoleConfigurable;
import javafx.scene.control.Button;
import org.example.strategy.RoleStrategy;
import org.example.strategy.RoleStrategyFactory;
import org.example.util.AlertMessage;


@Getter
@Setter
public class CreateAmenityController extends NavigationController implements RoleConfigurable {

    private final AmenityService amenityService = ServiceFactory.getAmenityService();
    private Hotel currentHotel;

    @FXML
    private TextField nameField;
    @FXML
    private ChoiceBox<SeasonAmenity> seasonChoiceBox;
    @FXML
    private TextArea descriptionArea;
    @FXML
    private Button createBtn;

    @FXML
    public void initialize() {
        RoleStrategy roleStrategy = RoleStrategyFactory.getStrategy(Session.getSession().getLoggedUser().getRole());
        seasonChoiceBox.getItems().addAll(SeasonAmenity.values());
        roleStrategy.applyPermissions(this);
        System.out.println(currentHotel.getName());
    }

    @FXML
    private void createAmenity(ActionEvent event) {
        amenityService.createAmenity(nameField.getText(), descriptionArea.getText(), seasonChoiceBox.getValue(), currentHotel);
        AlertMessage.showMessage("Amenity Creation", "Amenity with " +  nameField.getText() + " was successfully created.");
    }

    @Override
    protected Stage getCurrentStage() {
        return (Stage)  nameField.getScene().getWindow();
    }
}
