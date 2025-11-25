package org.example.strategy;

import org.example.controller.DashboardController;

public class ReceptionistStrategy implements RoleStrategy {

    @Override
    public void applyPermissions(DashboardController controller) {
        controller.getBtnCreateHotel().setDisable(true);
        controller.getBtnCreateHotel().setStyle("-fx-background-color: #f0f0f0;");
        controller.getBtnCreateUser().setDisable(true);
        controller.getBtnCreateUser().setStyle("-fx-background-color: #f0f0f0;");
    }
}
