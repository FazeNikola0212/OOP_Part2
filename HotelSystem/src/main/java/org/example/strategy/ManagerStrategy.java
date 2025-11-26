package org.example.strategy;

import org.example.controller.CreateUserController;
import org.example.controller.DashboardController;
import org.example.model.user.Role;

public class ManagerStrategy implements RoleStrategy {
    @Override
    public void applyPermissions(RoleConfigurable controller) {
        if (controller instanceof DashboardController d) {
            d.getBtnCreateHotel().setDisable(true);
            d.getBtnCreateHotel().setStyle("-fx-background-color: #f0f0f0;");
        }
        if (controller instanceof CreateUserController c) {
            c.getRoleChoiceBox().getItems().add(Role.RECEPTIONIST);
        }

    }
}
