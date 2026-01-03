package org.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.factory.ServiceFactory;
import org.example.model.notification.Notification;
import org.example.service.notification.NotificationService;
import org.example.service.reservation.ReservationService;
import org.example.util.ApplicationBootstrap;
import org.example.util.SceneSwitcher;

import java.util.logging.Logger;


public class MainApp extends Application {

    private final Logger logger = Logger.getLogger(MainApp.class.getName());
    private ApplicationBootstrap bootstrap;

    @Override
    public void init() throws Exception {
        ReservationService reservationService = ServiceFactory.getReservationService();
        NotificationService notificationService = ServiceFactory.getNotificationService();
        bootstrap = new ApplicationBootstrap();
        bootstrap.start(reservationService, notificationService);
        logger.info("Successfully started bootstrap");
    }

    public void start(Stage primaryStage) throws Exception {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/login.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(SceneSwitcher.class.getResource("/css/glassmorphism.css").toExternalForm());
            primaryStage.setTitle("Hotel Management System");
            primaryStage.setResizable(false);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stop() throws Exception {
        if (bootstrap != null) {
            bootstrap.stop();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}