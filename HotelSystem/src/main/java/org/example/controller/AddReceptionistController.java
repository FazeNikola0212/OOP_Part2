package org.example.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import org.example.factory.ServiceFactory;
import org.example.model.user.User;
import org.example.service.hotel.HotelService;
import org.example.service.user.UserService;
import org.example.session.SelectedHotelHolder;
import org.example.session.Session;
import org.example.util.AlertMessage;
import javafx.stage.Stage;


public class AddReceptionistController extends NavigationController {

    private final HotelService hotelService = ServiceFactory.getHotelService();
    private final UserService userService = ServiceFactory.getUserService();

    @FXML
    private Label hotelName;

    @FXML
    private ListView<String> receptionistList;

    @FXML
    public void initialize() {
        hotelName.setText("Hotel " + SelectedHotelHolder.getHotel().getName());
        receptionistList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        receptionistList.getItems().addAll(
                userService.getAllNotAssignedReceptionists()
                        .stream()
                        .map(User::getUsername)
                        .toList());
    }

    @FXML
    private void addReceptionist() {
        Long hotelId = SelectedHotelHolder.getHotel().getId();
        String receptionistName = receptionistList.
                getSelectionModel().
                getSelectedItem();
        User receptionist = userService.getUserByUsername(receptionistName);
        hotelService.addReceptionist(hotelId, receptionist);
        AlertMessage.showMessage("Adding Receptionist", "Successfully added Receptionist");
    }

    @Override
    protected Stage getCurrentStage() {
        return (Stage) hotelName.getScene().getWindow();
    }
}
