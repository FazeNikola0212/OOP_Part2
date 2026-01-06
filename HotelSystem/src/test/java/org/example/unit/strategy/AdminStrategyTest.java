package org.example.unit.strategy;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import org.example.controller.CreateUserController;
import org.example.controller.DashboardController;
import org.example.model.user.Role;
import org.example.strategy.AdminStrategy;
import org.h2.command.ddl.CreateUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
    import org.testfx.framework.junit5.ApplicationExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(ApplicationExtension.class)
public class AdminStrategyTest {

    @Test
    void disablesButton() {
        Button btn = new Button();
        DashboardController controller = mock(DashboardController.class);

        when(controller.getBtnHotelOps()).thenReturn(btn);

        new AdminStrategy().applyPermissions(controller);

        assertTrue(btn.isDisabled());
    }

    @Test
    void applyPermissions_addsRolesToChoiceBox() {
        ChoiceBox<Role> choiceBox = new ChoiceBox<>();
        CreateUserController controller = mock(CreateUserController.class);

        when(controller.getRoleChoiceBox()).thenReturn(choiceBox);

        new AdminStrategy().applyPermissions(controller);

        assertTrue(choiceBox.getItems().contains(Role.OWNER));
        assertTrue(choiceBox.getItems().contains(Role.ADMIN));
        assertTrue(choiceBox.getItems().contains(Role.MANAGER));
        assertTrue(choiceBox.getItems().contains(Role.RECEPTIONIST));

        assertEquals(4, choiceBox.getItems().size());
    }


}
