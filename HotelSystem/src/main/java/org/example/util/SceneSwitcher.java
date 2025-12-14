package org.example.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.session.SelectedHotelHolder;
import org.example.session.Session;

import java.io.IOException;
import java.util.Stack;

/* Utility Class which helps to navigate
easier between scenes mainly to get back
to previous scene and logging out */

public class SceneSwitcher {
    private static final Stack<String> history = new Stack<>();
    private static final String GLOBAL_CSS = "/css/glassmorphism.css";

    public static void switchScene(Stage stage, String fxml) throws IOException {
        if (stage.getScene() == null) {
            history.push(stage.getScene().getRoot().getId());
        }

        FXMLLoader loader = new FXMLLoader(SceneSwitcher.class.getResource(fxml));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        scene.getStylesheets().add(SceneSwitcher.class.getResource(GLOBAL_CSS).toExternalForm());
        stage.setScene(scene);
        stage.show();

        history.push(fxml);
    }

    public static FXMLLoader switchSceneWithLoader(Stage stage, String fxml) throws IOException {
        if (stage.getScene() != null) {
            history.push(stage.getScene().getRoot().getId());
        }

        FXMLLoader loader = new FXMLLoader(SceneSwitcher.class.getResource(fxml));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        scene.getStylesheets().add(SceneSwitcher.class.getResource(GLOBAL_CSS).toExternalForm());
        stage.setScene(scene);
        stage.show();

        history.push(fxml);
        return loader;
    }

    public static void goBack(Stage stage)  throws IOException {
        if (history.size() <= 1) {
            return;
        }
        history.pop();

        String previousFXML = history.pop();

        FXMLLoader loader = new FXMLLoader(SceneSwitcher.class.getResource(previousFXML));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        scene.getStylesheets().add(SceneSwitcher.class.getResource(GLOBAL_CSS).toExternalForm());
        stage.setScene(scene);
        stage.show();

        history.push(previousFXML);
    }

    public static void goLogout(Stage stage) throws IOException {
        history.clear();

        FXMLLoader loader = new FXMLLoader(SceneSwitcher.class.getResource("/views/login.fxml"));
        Parent root = loader.load();
        stage.setScene(new Scene(root));
        stage.show();

        Session.getSession().clearSession();
        SelectedHotelHolder.clear();
    }
}
