package org.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.model.hotel.Hotel;
import org.example.model.user.User;
import org.example.repository.hotel.HotelRepositoryImpl;
import org.example.repository.user.UserRepositoryImpl;


public class MainApp extends Application {
    private final UserRepositoryImpl userRepository = new UserRepositoryImpl();
    private final HotelRepositoryImpl hotelRepository = new HotelRepositoryImpl();
    public void start(Stage primaryStage) throws Exception {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/login.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);

            primaryStage.setTitle("Login");
            primaryStage.setResizable(false);
            primaryStage.setScene(scene);
            primaryStage.show();
            userRepository.save(new User());
            hotelRepository.save(new Hotel());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}