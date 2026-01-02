package org.example.controller;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.DTO.ReservationRowDTO;
import org.example.factory.ServiceFactory;
import org.example.model.reservation.ReservationStatus;
import org.example.service.reservation.ReservationService;
import org.example.session.SelectedHotelHolder;
import org.example.util.SceneSwitcher;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;


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

    private ObservableList<ReservationRowDTO> mainData;
    private FilteredList<ReservationRowDTO> filteredData;
    private SortedList<ReservationRowDTO> sortedData;



    @FXML private TextField searchClientName;
    @FXML private DatePicker searchDateCreation;

    @FXML
    public void initialize() throws IOException {
        currentHotel.setText(SelectedHotelHolder.getHotel().getName());

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
        disableFutureDates(searchDateCreation);
        lifeSearch();
    }

    private void addDetailsButton() throws IOException {
        details.setCellFactory(col -> new TableCell<>() {
            private final Button button = new Button("Details");

            {
                button.setOnAction(event -> {
                    ReservationRowDTO dto = getTableView().getItems().get(getIndex());
                    try {
                        System.out.println(dto.getRoomsNumber());
                        openDetails(dto);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : button);
            }
        });
    }

    private void openDetails(ReservationRowDTO dto) throws IOException {
        FXMLLoader loader = SceneSwitcher.switchSceneWithLoader((Stage) reservationsTable.getScene().getWindow(), "/views/reservation-details.fxml");
        ReservationDetailsController controller = loader.getController();
        controller.setReservation(dto);
    }

    private void addChangeStatusBtn() {
        changeStatus.setCellFactory(col -> new TableCell<>() {

            private final Button button = new Button();

            {
                button.setOnAction(event -> {
                    ReservationRowDTO dto = getTableView().getItems().get(getIndex());
                    reservationService.changeReservationStatus(dto.getId());
                    dto.setChecked(!dto.isChecked());
                    updateButtonText(dto);
                    loadReservations();
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
                if (dto.getReservationStatus().equals(ReservationStatus.EXPIRED.toString())) {
                    button.setText("Expired");
                    button.setStyle("-fx-background-color: #ff0000");
                    button.setDisable(true);
                }
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

    private void lifeSearch() {
        searchClientName.textProperty().addListener((observable, oldValue, newValue) -> {
            applyFilters();
        });

        searchDateCreation.valueProperty().addListener((observable, oldValue, newValue) -> {
            applyFilters();
        });
    }

    private void applyFilters() {
            String nameFilter = searchClientName.getText();
            LocalDate dateFilter = searchDateCreation.getValue();


            filteredData.setPredicate(dto -> {

                boolean matchesName = true;
                if (nameFilter != null && !nameFilter.isBlank()) {
                    String fullName = dto.getClientFullName();
                    if (fullName == null) {
                        matchesName = false;
                    } else {
                        matchesName = fullName
                                .toLowerCase()
                                .contains(nameFilter.trim().toLowerCase());
                    }
                }

                boolean matchesDate = true;
                if (dateFilter != null) {
                    if (dto.getCreatedAt() == null) {
                        matchesDate = false;
                    } else {
                        matchesDate = dto.getCreatedAt()
                                .toLocalDate()
                                .equals(dateFilter);
                    }
                }

                return matchesName && matchesDate;
            });
    }

    private void disableFutureDates(DatePicker datePicker) {
        datePicker.setDayCellFactory(dp -> new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);

                if (item.isAfter(LocalDate.now())) {
                    setDisable(true);
                    setStyle("-fx-background-color: #ffc0cb");
                }

            }
        });
    }

    private void loadReservations() {
        List<ReservationRowDTO> reservations = reservationService.getReservationRows(SelectedHotelHolder.getHotel());
        mainData = FXCollections.observableList(reservations);
        filteredData = new FilteredList<>(mainData, p -> true);
        sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(reservationsTable.comparatorProperty());

        reservationsTable.setItems(sortedData);
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
