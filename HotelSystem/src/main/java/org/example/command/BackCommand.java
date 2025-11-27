package org.example.command;

import javafx.stage.Stage;
import org.example.util.SceneSwitcher;

public class BackCommand implements Command {
    private final Stage stage;
    public BackCommand(Stage stage) {
        this.stage = stage;
    }
    @Override
    public void execute() {
        try {
            SceneSwitcher.goBack(stage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
