package org.example.controller;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.example.DTO.CreateClientRequest;
import org.example.repository.client.ClientRepositoryImpl;
import org.example.service.client.ClientService;
import org.example.util.AlertMessage;
import org.example.util.SceneSwitcher;

import javax.swing.*;
import java.io.IOException;

public class CreateClientController {

    private final ClientService clientService = new ClientService(new ClientRepositoryImpl());

    @FXML
    private TextField firstNameField;
    @FXML
    private TextField lastNameField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField phoneNumberField;
    @FXML
    private Button createBtn;
    @FXML
    private Button backBtn;
    @FXML
    private Button logoutBtn;
    @FXML
    private AnchorPane anchorPane;

    @FXML
    public void initialize() {}

    @FXML
    private void createClient(ActionEvent event) throws IOException {
        CreateClientRequest request = CreateClientRequest
                .builder()
                .firstName(firstNameField.getText())
                .lastName(lastNameField.getText())
                .phoneNumber(phoneNumberField.getText())
                .email(emailField.getText())
                .build();
        clientService.createClient(request);
        AlertMessage.showMessage("Client creation", "Client " + request.getFirstName() + " " + request.getLastName() + " has been created");
        clearTextFields(anchorPane);
    }

    @FXML
    private void goBack(ActionEvent event) throws IOException {
        SceneSwitcher.goBack((Stage) backBtn.getScene().getWindow());
    }

    @FXML
    private void logout(ActionEvent event) throws IOException {
        SceneSwitcher.goLogout((Stage) logoutBtn.getScene().getWindow());
    }

    private void clearTextFields(AnchorPane anchorPane) {
        for (Node node : anchorPane.getChildren()) {
            if (node instanceof TextField) {
                ( (TextField) node ).clear();
            } else if (node instanceof AnchorPane) {
                clearTextFields((AnchorPane) node);
            }
        }
    }
}
