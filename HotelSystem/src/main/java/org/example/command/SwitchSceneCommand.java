package org.example.command;

import javafx.stage.Stage;
import org.example.util.SceneSwitcher;

/* Through Command pattern we are accessing and switching easily
 between different scenes and following the SOLID principles */

public class SwitchSceneCommand implements Command {
    private final Stage stage;
    private final String fxmlPath;

    public SwitchSceneCommand(Stage stage, String fxmlPath) {
        this.stage = stage;
        this.fxmlPath = fxmlPath;
    }

    @Override
    public void execute() throws Exception {
        SceneSwitcher.switchScene(stage, fxmlPath);
    }
}
