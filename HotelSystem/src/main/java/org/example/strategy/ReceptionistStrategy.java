package org.example.strategy;

import org.example.controller.*;

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
            d.getSelectHotelBtn().setDisable(true);
            d.getSelectHotelBtn().setStyle("-fx-background-color: #f0f0f0;");
        }
        if (controller instanceof HotelOperationsController h) {
            h.getAddReceptionistBtn().setDisable(true);
            h.getAddReceptionistBtn().setStyle("-fx-background-color: #f0f0f0;");
            h.getRemoveReceptionistBtn().setDisable(true);
            h.getRemoveReceptionistBtn().setStyle("-fx-background-color: #f0f0f0;");
            h.getCreateRoomBtn().setDisable(true);
            h.getCreateRoomBtn().setStyle("-fx-background-color: #f0f0f0;");
        }

    }
}
