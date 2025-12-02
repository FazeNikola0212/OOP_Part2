package org.example.strategy;

import org.example.controller.*;
import org.example.model.user.Role;
import org.example.session.Session;


public class ManagerStrategy implements RoleStrategy {
    @Override
    public void applyPermissions(RoleConfigurable controller) {
        if (controller instanceof DashboardController d) {
            d.getBtnCreateHotel().setDisable(true);
            d.getBtnCreateHotel().setStyle("-fx-background-color: #f0f0f0;");
            d.getSelectHotelBtn().setDisable(true);
            d.getSelectHotelBtn().setStyle("-fx-background-color: #f0f0f0;");
        }
        if (controller instanceof CreateUserController c) {
            c.getRoleChoiceBox().getItems().add(Role.RECEPTIONIST);
        }
        if (controller instanceof CreateRoomController r) {
            r.getCurrentHotelLabel().setText(Session.getSession().getLoggedUser().getAssignedHotel().getName());
        }
        if (controller instanceof CreateAmenityController am) {
            am.setCurrentHotel(Session.getSession().getLoggedUser().getAssignedHotel());
        }
        if (controller instanceof AmenitiesListController al) {
            al.setHotel(Session.getSession().getLoggedUser().getAssignedHotel());
        }

    }
}
