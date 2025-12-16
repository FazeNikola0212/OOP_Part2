package org.example.controller;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.stage.Stage.*;
import javafx.scene.Scene.*;
import org.example.DTO.ReservationRowDTO;
import org.example.factory.ServiceFactory;
import org.example.model.client.Client;
import org.example.model.reservation.Reservation;
import org.example.model.reservation.ReservationRoom;
import org.example.service.reservation.ReservationService;
import org.example.session.SelectedHotelHolder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;


public class ReservationListController extends NavigationController {
    private final ReservationService reservationService = ServiceFactory.getReservationService();
    private final DateTimeFormatter dateFormatter =
            DateTimeFormatter.ofPattern("dd-MMM-yyyy").withLocale(Locale.ENGLISH);

    @FXML private Label currentHotel;
    @FXML private TableView<ReservationRowDTO> reservationsTable;
    @FXML private TableColumn<ReservationRowDTO, String> reservationNumberCol;
    @FXML private TableColumn<ReservationRowDTO, String> clientCol;
    @FXML private TableColumn<ReservationRowDTO, String> reservationTypeCol;
    @FXML private TableColumn<ReservationRowDTO, String> terminationTypeCol;
    @FXML private TableColumn<ReservationRowDTO, String>  createdAtCol;
    @FXML private TableColumn<ReservationRowDTO, String> roomsCol;
    @FXML private TableColumn<ReservationRowDTO, Integer> guestAmountCol;
    @FXML private TableColumn<ReservationRowDTO, String> receptionistCol;
    @FXML private TableColumn<ReservationRowDTO, String> reservationStatusCol;
    @FXML private TableColumn<ReservationRowDTO, Void> changeStatus;
    @FXML private TableColumn<ReservationRowDTO, Void> details;


    @FXML private TextField searchClientName;
    @FXML private DatePicker searchDateCreation;

    @FXML
    public void initialize() {

        reservationNumberCol.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getReservationNumber()));

        clientCol.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getClientFullName()));

        reservationTypeCol.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getType()));

        terminationTypeCol.setCellValueFactory(data ->
                new SimpleStringProperty(
                        data.getValue().getTerminationType()));

        createdAtCol.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getCreatedAt().format(dateFormatter)));


        roomsCol.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getRoomsNumber()));

        guestAmountCol.setCellValueFactory(data ->
                new SimpleIntegerProperty(data.getValue().getGuestsNumber()).asObject());

        receptionistCol.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getReceptionistName()));

        reservationStatusCol.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getReservationStatus()));

        addChangeStatusBtn();
        addDetailsButton();

        styleColumns(reservationsTable);
        loadReservations();
    }

    private void addDetailsButton() {
        details.setCellFactory(col -> new TableCell<>() {
            private final Button button = new Button("Details");

            {
                button.setOnAction(event -> {
                    openDetails();
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : button);
            }
        });
    }

    private void openDetails() {

    }

    private void addChangeStatusBtn() {
        changeStatus.setCellFactory(col -> new TableCell<>() {

            private final Button button = new Button();

            {
                button.setOnAction(event -> {
                    ReservationRowDTO dto = getTableView().getItems().get(getIndex());

                    dto.setChecked(!dto.isChecked());
                    updateButtonText(dto);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setGraphic(null);
                } else {
                    ReservationRowDTO dto = getTableView().getItems().get(getIndex());
                    updateButtonText(dto);
                    setGraphic(button);
                }
            }

            private void updateButtonText(ReservationRowDTO dto) {
                if (dto.isChecked()) {
                    button.setText("Checked In");
                    button.setStyle("-fx-background-color: green;");
                } else {
                    button.setText("Pending");
                    button.setStyle("-fx-background-color: red;");
                }

            }
        });
    }

    private void loadReservations() {
        List<ReservationRowDTO> reservations = reservationService.getReservationRows(SelectedHotelHolder.getHotel());
        reservationsTable.setItems(FXCollections.observableList(reservations));
    }

    @Override
    protected Stage getCurrentStage() {
        return (Stage) currentHotel.getScene().getWindow();
    }
    private <S> void styleColumns(TableView<S> tableView) {
        for (TableColumn<S,?> column : tableView.getColumns()) {
            column.setStyle("-fx-alignment: CENTER");
        }
    }
}
