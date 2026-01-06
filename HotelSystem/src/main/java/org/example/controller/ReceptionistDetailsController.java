package org.example.controller;

import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.example.DTO.ReceptionistDetailsDTO;
import org.example.factory.ServiceFactory;
import org.example.model.user.User;
import org.example.service.user.ReceptionistService;
import org.example.service.user.UserService;
import org.example.session.SelectedHotelHolder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class ReceptionistDetailsController extends NavigationController {

    private UserService userService = ServiceFactory.getUserService();
    private ReceptionistService receptionistService = ServiceFactory.getReceptionistService();
    private User currentReceptionist;
    private ReceptionistDetailsDTO  dto;

    public void setReceptionist(User receptionist) {
        this.currentReceptionist = receptionist;

        fillData();
    }

    @FXML private Label receptionistName;
    @FXML private TableView<ReceptionistDetailsDTO> queryTable;
    @FXML private TableColumn<ReceptionistDetailsDTO, String> usernameCol;
    @FXML private TableColumn<ReceptionistDetailsDTO, String> fullNameCol;
    @FXML private TableColumn<ReceptionistDetailsDTO, Integer> amenitiesAssignedCol;
    @FXML private TableColumn<ReceptionistDetailsDTO, String> createdByCol;
    @FXML private TableColumn<ReceptionistDetailsDTO, String> hotelNameCol;
    @FXML private TableColumn<ReceptionistDetailsDTO, Integer> amountReservationsCol;
    @FXML private TableColumn<ReceptionistDetailsDTO, BigDecimal> revenuePriceCol;
    @FXML private TableColumn<ReceptionistDetailsDTO, Integer> numberAssignedCol;


    private void fillData() {
        receptionistName.setText(currentReceptionist.getFullName());

        usernameCol.setCellValueFactory(new PropertyValueFactory<>("username"));
        fullNameCol.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        createdByCol.setCellValueFactory(new PropertyValueFactory<>("createdBy"));
        hotelNameCol.setCellValueFactory(new PropertyValueFactory<>("hotelName"));
        amountReservationsCol.setCellValueFactory(new PropertyValueFactory<>("reservationsAssigned"));
        revenuePriceCol.setCellValueFactory(new PropertyValueFactory<>("revenuePrice"));
        numberAssignedCol.setCellValueFactory(new PropertyValueFactory<>("clientsAssigned"));
        amenitiesAssignedCol.setCellValueFactory(new PropertyValueFactory<>("amenitiesAssigned"));

        dto = receptionistService.getReceptionistDetails(currentReceptionist);
        System.out.println(dto.getClientsAssigned());

        ObservableList<ReceptionistDetailsDTO> observableList = FXCollections.observableArrayList();
        List<ReceptionistDetailsDTO> dtos = userService.getAllReceptionistsAndManagersByHotel(SelectedHotelHolder.getHotel())
                .stream().map(receptionistService::getReceptionistDetails).toList();
        observableList.setAll(dtos);
        queryTable.setItems(observableList);

        queryTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        queryTable.setEditable(false);

        styleColumns(queryTable);

    }


    private <S> void styleColumns(TableView<S> tableView) {
        for (TableColumn<S,?> column : tableView.getColumns()) {
            column.setStyle("-fx-alignment: CENTER");
        }
    }

    @Override
    protected Stage getCurrentStage() {
        return (Stage) receptionistName.getScene().getWindow();
    }
}

