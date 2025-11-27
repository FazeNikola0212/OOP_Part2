package org.example.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import org.example.model.user.User;
import org.example.repository.hotel.HotelRepositoryImpl;
import org.example.repository.user.UserRepository;
import org.example.repository.user.UserRepositoryImpl;
import org.example.service.hotel.HotelService;
import org.example.service.user.UserService;
import javafx.scene.control.Button;
import org.example.session.Session;
import org.example.util.AlertMessage;
import org.example.util.SceneSwitcher;
import javafx.stage.Stage;

import java.io.IOException;

public class AddReceptionistController {

    private final UserRepository userRepository = new UserRepositoryImpl();
    private final UserService userService = new UserService(userRepository);
    private final HotelService hotelService = new HotelService(new HotelRepositoryImpl());

    @FXML
    private Label hotelName;

    @FXML
    private ListView<String> receptionistList;

    @FXML
    private Button backBtn;

    @FXML
    private Button logoutBtn;

    @FXML
    public void initialize() {
        hotelName.setText("Hotel " + Session.getSession().getLoggedUser().getAssignedHotel().getName());
        receptionistList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        receptionistList.getItems().addAll(
                userRepository.findAllReceptionists()
                        .stream()
                        .map(User::getUsername)
                        .toList());
    }

    @FXML
    private void addReceptionist() {
        Long hotelId = Session.getSession().getLoggedUser().getAssignedHotel().getId();
        String receptionistName = receptionistList.
                getSelectionModel().
                getSelectedItem();
        User receptionist = userRepository.findByUsername(receptionistName);
        hotelService.addReceptionist(hotelId, receptionist);
        AlertMessage.showMessage("Adding Receptionist", "Successfully added Receptionist");
    }

    @FXML
    private void goBack() throws IOException {
        SceneSwitcher.goBack((Stage) backBtn.getScene().getWindow());
    }

    @FXML
    private void logout() throws IOException {
        SceneSwitcher.goLogout((Stage) logoutBtn.getScene().getWindow());
    }

}
