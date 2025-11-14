package org.example.controller;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import javafx.event.ActionEvent;
import org.example.repository.user.UserRepositoryImpl;
import org.example.service.user.UserService;

public class LoginController {

    private final UserService userService = new UserService(new UserRepositoryImpl());

    @FXML
    private TextField username;
    @FXML
    private PasswordField password;

    @FXML
    private void onLogin(ActionEvent event) {
        String user =  username.getText();
        String pass = password.getText();

        if (userService.loginUser(user, pass)) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/creating-user.fxml"));
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
