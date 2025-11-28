package org.example.controller;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.example.DTO.CreateClientRequest;
import org.example.factory.ServiceFactory;
import org.example.service.client.ClientService;
import org.example.util.AlertMessage;

public class CreateClientController extends NavigationController {

    private final ClientService clientService = ServiceFactory.getClientService();

    @FXML
    private TextField firstNameField;
    @FXML
    private TextField lastNameField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField phoneNumberField;
    @FXML
    private AnchorPane anchorPane;

    @FXML
    public void initialize() {}

    @FXML
    private void createClient(ActionEvent event) {
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

    @Override
    protected Stage getCurrentStage() {
        return (Stage) firstNameField.getScene().getWindow();
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
