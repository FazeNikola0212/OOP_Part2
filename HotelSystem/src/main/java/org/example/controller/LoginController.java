package org.example.controller;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import javafx.event.ActionEvent;
public class LoginController {

    @FXML
    private TextField username;
    @FXML
    private PasswordField password;

    @FXML
    private void onLogin(ActionEvent event) {
        String user =  username.getText();
        String pass = password.getText();

        if ("admin".equals(user) && "admin".equals(pass)) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/dashboard.fxml"));
                Parent root = loader.load();
                Stage stage = (Stage) username.getScene().getWindow();
                stage.getScene().setRoot(root);
                stage.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Wrong username or password");
        }

    }
}
