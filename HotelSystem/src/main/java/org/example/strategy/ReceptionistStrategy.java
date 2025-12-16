package org.example.strategy;

import javafx.scene.control.TableView;
import org.example.controller.*;
import org.example.session.SelectedHotelHolder;
import org.example.session.Session;

public class ReceptionistStrategy implements RoleStrategy {

    @Override
    public void applyPermissions(RoleConfigurable controller) {
        if (controller instanceof DashboardController d ) {
            d.getBtnCreateHotel().setDisable(true);
            d.getBtnCreateHotel().setStyle("-fx-background-color: #f0f0f0;");
            d.getBtnCreateUser().setDisable(true);
            d.getBtnCreateUser().setStyle("-fx-background-color: #f0f0f0;");
            d.getSelectHotelBtn().setDisable(true);
            d.getSelectHotelBtn().setStyle("-fx-background-color: #f0f0f0;");
            SelectedHotelHolder.setHotel(Session.getSession().getLoggedUser().getAssignedHotel());
        }
        if (controller instanceof HotelOperationsController h) {
            h.getAddReceptionistBtn().setDisable(true);
            h.getAddReceptionistBtn().setStyle("-fx-background-color: #f0f0f0;");
            h.getCreateRoomBtn().setDisable(true);
            h.getCreateRoomBtn().setStyle("-fx-background-color: #f0f0f0;");
            h.getCreateAmenityBtn().setDisable(true);
            h.getCreateAmenityBtn().setStyle("-fx-background-color: #f0f0f0;");
            h.getListReceptionistsBtn().setDisable(true);
            h.getListReceptionistsBtn().setStyle("-fx-background-color: #f0f0f0;");
        }
        if (controller instanceof AmenitiesListController al) {
            al.getEditColumn().setVisible(false);
            al.getAmenitiesTable().setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        }




    }
}
