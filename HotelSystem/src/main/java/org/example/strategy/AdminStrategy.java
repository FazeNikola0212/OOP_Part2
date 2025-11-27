package org.example.strategy;

import org.example.controller.CreateUserController;
import org.example.controller.DashboardController;
import org.example.model.user.Role;

public class AdminStrategy implements RoleStrategy {

    @Override
    public void applyPermissions(RoleConfigurable controller) {
        if (controller instanceof CreateUserController c) {
            c.getRoleChoiceBox().getItems().add(Role.OWNER);
            c.getRoleChoiceBox().getItems().add(Role.ADMIN);
            c.getRoleChoiceBox().getItems().add(Role.MANAGER);
            c.getRoleChoiceBox().getItems().add(Role.RECEPTIONIST);
        }
    }
}
