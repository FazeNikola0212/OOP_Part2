package org.example.strategy;

import org.example.controller.DashboardController;

public class OwnerStrategy implements RoleStrategy {

    @Override
    public void applyPermissions(DashboardController controller) {
        controller.getBtnCreateClient().setDisable(true);
        controller.getBtnCreateClient().setStyle("-fx-background-color: #f0f0f0;");

    }
}
