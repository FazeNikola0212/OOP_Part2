package org.example.strategy;

import org.example.controller.CreateAmenityController;
import org.example.controller.CreateUserController;
import org.example.controller.DashboardController;

public class ReceptionistStrategy implements RoleStrategy {

    @Override
    public void applyPermissions(RoleConfigurable controller) {
        if (controller instanceof DashboardController d ) {
            d.getBtnCreateHotel().setDisable(true);
            d.getBtnCreateHotel().setStyle("-fx-background-color: #f0f0f0;");
            d.getBtnCreateUser().setDisable(true);
            d.getBtnCreateUser().setStyle("-fx-background-color: #f0f0f0;");
            d.getBtnCreateAmenity().setDisable(true);
            d.getBtnCreateAmenity().setStyle("-fx-background-color: #f0f0f0;");
        }
    }
}
