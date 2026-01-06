package org.example.integration;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.example.controller.HotelOperationsController;
import org.example.model.hotel.Hotel;
import org.example.model.user.Role;
import org.example.model.user.User;
import org.example.session.SelectedHotelHolder;
import org.example.session.Session;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import static org.junit.Assert.assertTrue;

@ExtendWith(ApplicationExtension.class)
class HotelOperationsControllerIT {

    private HotelOperationsController controller;

    @Start
    private void start(Stage stage) throws Exception {
        Session session = Session.getSession();
        User user = User.builder().username("admin").build();
        user.setRole(Role.ADMIN);
        session.setLoggedUser(user);

        Hotel hotel = Hotel.builder().name("Reina Del Mar").build();

        SelectedHotelHolder.setHotel(hotel);

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/views/hotel-operations.fxml")
        );

        Parent root = loader.load();
        controller = loader.getController();

        stage.setScene(new Scene(root));
        stage.show();
    }

    //FxRobot mocks user who does any operations -> finding, clicking on locations like buttons, label etc.

    @Test
    void hotelLabel_shouldContainHotelName(FxRobot robot) {
        Label label = robot.lookup("#hotelLabel").queryAs(Label.class);
        assertTrue(label.getText().contains( "Reina Del Mar"));
    }

    @Test
    void addReceptionistButton_shouldBeVisible(FxRobot robot) {
        Button btn = robot.lookup("#addReceptionistBtn").queryAs(Button.class);
        assertTrue(btn.isVisible());
    }
}

