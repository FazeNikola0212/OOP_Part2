package org.example.strategy;

import org.example.controller.CreateRoomController;
import org.example.controller.CreateUserController;
import org.example.controller.DashboardController;
import org.example.model.user.Role;
import org.example.session.SelectedHotelHolder;

public class OwnerStrategy implements RoleStrategy {

    @Override
    public void applyPermissions(RoleConfigurable controller) {
        if (controller instanceof DashboardController d) {
            d.getBtnHotelOps().setDisable(true);
            d.getBtnHotelOps().setStyle("-fx-background-color: #f0f0f0;");
        }
        if (controller instanceof CreateUserController c) {
            c.getRoleChoiceBox().getItems().add(Role.RECEPTIONIST);
            c.getRoleChoiceBox().getItems().add(Role.MANAGER);
        }
        if (controller instanceof CreateRoomController r) {
            r.getCurrentHotelLabel().setText(SelectedHotelHolder.getHotel().getName());
        }
    }
}
