package org.example.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import org.example.factory.ServiceFactory;
import org.example.model.user.User;
import org.example.service.hotel.HotelService;
import org.example.service.user.UserService;
import org.example.session.Session;
import org.example.util.AlertMessage;

public class RemoveReceptionistController extends NavigationController {

    private final HotelService hotelService = ServiceFactory.getHotelService();
    private final UserService userService = ServiceFactory.getUserService();

    @FXML
    private Label headingLabel;

    @FXML
    private ListView<String> receptionistList;

    @FXML
    private Button removeReceptionistBtn;

    @FXML
    private Label hotelLabel;

    @FXML
    public void initialize() {
        hotelLabel.setText(Session.getSession().getLoggedUser().getAssignedHotel().getName());
        receptionistList.getItems()
                .addAll(userService.getReceptionistsByHotelId(Session.getSession().getLoggedUser().getAssignedHotel().getId())
                        .stream()
                        .map(User :: getUsername)
                        .toList());

    }

    @FXML
    private void removeReceptionist() {
        Long hotelId = Session.getSession().getLoggedUser().getAssignedHotel().getId();
        String receptionistName = receptionistList.getSelectionModel().getSelectedItem();
        User receptionist = userService.getUserByUsername(receptionistName);
        hotelService.removeReceptionist(hotelId, receptionist);
        AlertMessage.showMessage("Removing receptionist", "Successfully removed receptionist");
        getCurrentStage();
    }

    @Override
    protected Stage getCurrentStage() {
        return (Stage) headingLabel.getScene().getWindow();
    }
}
