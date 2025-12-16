package org.example.strategy;

import javafx.scene.control.TableView;
import org.example.controller.*;
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
        if (controller instanceof ListHotelsController li) {
            li.getHotelTable().setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        }

        if (controller instanceof OwnerPanelController ow) {
            ow.getCurrentHotelLabel().setText("Current hotel: " + SelectedHotelHolder.getHotel().getName());
            ow.getCurrentHotelLabel().setStyle("-fx-background-color: #f0f0f0;");
        }

        if (controller instanceof ReceptionistListController re) {
            re.getWelcomeLabel().setText("Current hotel: " + SelectedHotelHolder.getHotel().getName());
        }
    }
}
