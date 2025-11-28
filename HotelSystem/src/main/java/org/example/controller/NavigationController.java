package org.example.controller;

import javafx.fxml.FXML;
import javafx.stage.Stage;
import org.example.command.BackCommand;
import org.example.command.LogoutCommand;

public abstract class NavigationController {

    @FXML
    protected void goBack() {
        Stage stage = getCurrentStage();
        new BackCommand(stage).execute();
    }

    @FXML
    protected void logout() {
        Stage stage = getCurrentStage();
        new LogoutCommand(stage).execute();
    }
    protected abstract Stage getCurrentStage();

}
