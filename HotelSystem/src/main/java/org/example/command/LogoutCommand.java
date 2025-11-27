package org.example.command;

import javafx.stage.Stage;
import org.example.session.Session;

public class LogoutCommand implements Command{
    private final Stage stage;

    public LogoutCommand(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void execute() {
        try {
            Session.getSession().clearSession();
            new SwitchSceneCommand(stage, "/views/login.fxml").execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
