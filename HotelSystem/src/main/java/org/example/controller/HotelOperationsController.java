package org.example.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import lombok.Getter;
import org.example.session.Session;
import org.example.strategy.RoleConfigurable;
import org.example.strategy.RoleStrategy;
import org.example.strategy.RoleStrategyFactory;
import org.example.util.SceneSwitcher;
import javafx.scene.control.Label;

import java.io.IOException;

@Getter
public class HotelOperationsController extends NavigationController implements RoleConfigurable {

    @FXML
    private Button addReceptionistBtn;

    @FXML
    private Button removeReceptionistBtn;

    @FXML
    private Label hotelLabel;

    @FXML
    public void initialize() {
        hotelLabel.setText("Hotel " + Session.getSession().getLoggedUser().getAssignedHotel().getName());

        RoleStrategy strategy = RoleStrategyFactory.getStrategy(Session.getSession().getLoggedUser().getRole());
        strategy.applyPermissions(this);
    }

    @FXML
    private void addReceptionist() throws IOException {
        SceneSwitcher.switchScene((Stage) addReceptionistBtn.getScene().getWindow(), "/views/add-receptionist.fxml");
    }

    @FXML
    private void removeReceptionist() throws IOException {
        SceneSwitcher.switchScene((Stage) removeReceptionistBtn.getScene().getWindow(), "/views/remove-receptionist.fxml");
    }

    @Override
    protected Stage getCurrentStage() {
        return (Stage) hotelLabel.getScene().getWindow();
    }
}
