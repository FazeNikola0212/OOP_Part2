package org.example.controller;


import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import javafx.event.ActionEvent;
import org.example.factory.ServiceFactory;
import org.example.model.user.User;
import org.example.service.user.UserService;
import org.example.session.Session;
import org.example.util.SceneSwitcher;

public class LoginController {

    private final UserService userService = ServiceFactory.getUserService();

    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;

    @FXML
    private void onLogin(ActionEvent event) {
        String username =  usernameField.getText();
        String pass = passwordField.getText();

        try {
            User user = userService.loginUser(username, pass);
            Session.getSession().setLoggedUser(user);

            SceneSwitcher.switchScene((Stage) usernameField.getScene().getWindow(), "/views/dashboard.fxml");
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
