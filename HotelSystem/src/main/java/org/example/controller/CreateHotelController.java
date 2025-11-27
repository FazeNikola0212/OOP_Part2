package org.example.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import org.example.DTO.CreateHotelRequest;
import org.example.command.BackCommand;
import org.example.command.LogoutCommand;
import org.example.model.user.User;
import org.example.repository.hotel.HotelRepositoryImpl;
import org.example.repository.user.UserRepositoryImpl;
import org.example.service.hotel.HotelService;
import org.example.service.user.UserService;
import org.example.session.Session;
import org.example.util.AlertMessage;
import org.example.util.SceneSwitcher;
import javafx.stage.Stage;


import java.io.IOException;

public class CreateHotelController {

    private final UserService userService = new UserService(new UserRepositoryImpl());
    private final HotelService hotelService = new HotelService(new HotelRepositoryImpl());

    @FXML
    private TextField hotelNameField;

    @FXML
    private TextField cityField;

    @FXML
    private TextField addressField;

    @FXML
    private ChoiceBox<String> managerChoiceBox;

    @FXML
    private Button backBtn;

    @FXML
    private Button logoutBtn;

    @FXML
    private Button createBtn;

    @FXML
    public void initialize() {
        managerChoiceBox.getItems()
                .addAll(userService.getAllManagers()
                        .stream()
                        .map(User::getUsername)
                        .toList());
    }

    @FXML
    private void createHotel(ActionEvent event) throws IOException {
        CreateHotelRequest createHotelRequest = new CreateHotelRequest();
        createHotelRequest.setName(hotelNameField.getText());
        createHotelRequest.setAddress(addressField.getText());
        createHotelRequest.setCity(cityField.getText());
        createHotelRequest.setManager(userService.getUserByUsername(managerChoiceBox.getValue()));
        createHotelRequest.setOwner(Session.getSession().getLoggedUser());

        hotelService.createHotel(createHotelRequest);
        AlertMessage.showMessage("Hotel Creation", "Hotel with name " + createHotelRequest.getName() + " was successfully created.");
        SceneSwitcher.goBack((Stage) backBtn.getScene().getWindow());
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
