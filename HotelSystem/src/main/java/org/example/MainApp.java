package org.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.repository.client.ClientRepositoryImpl;
import org.example.repository.hotel.HotelRepositoryImpl;
import org.example.repository.user.UserRepository;
import org.example.repository.user.UserRepositoryImpl;
import org.example.service.user.UserService;

import java.util.logging.Level;
import java.util.logging.Logger;


public class MainApp extends Application {
    private final UserRepository userRepository = new UserRepositoryImpl();
    private final UserService userService = new UserService(userRepository);
    private final HotelRepositoryImpl hotelRepository = new HotelRepositoryImpl();
    private final ClientRepositoryImpl clientRepository = new ClientRepositoryImpl();

    private final Logger logger = Logger.getLogger(MainApp.class.getName());
    public void start(Stage primaryStage) throws Exception {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/login.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);

            primaryStage.setTitle("Login");
            primaryStage.setResizable(false);
            primaryStage.setScene(scene);
            primaryStage.show();


            logger.log(Level.INFO, "User table created");
            logger.log(Level.INFO, "Hotel table created");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}