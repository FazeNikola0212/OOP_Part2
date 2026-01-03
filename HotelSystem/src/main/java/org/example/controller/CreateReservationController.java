package org.example.controller;

import javafx.beans.property.BooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.example.DTO.ReservationCreationDTO;
import org.example.exceptions.ValidationException;
import org.example.factory.ServiceFactory;
import org.example.model.amenity.Amenity;
import org.example.model.client.Client;
import org.example.model.hotel.Hotel;
import org.example.model.reservation.ReservationType;
import org.example.model.room.Room;
import org.example.service.amenity.AmenityService;
import org.example.service.client.ClientService;
import org.example.service.notification.NotificationService;
import org.example.service.reservation.ReservationService;
import org.example.service.room.RoomService;
import org.example.session.SelectedHotelHolder;
import org.example.util.AlertMessage;
import org.example.util.SceneSwitcher;
import org.example.validators.ReservationValidator;

import java.math.BigDecimal;
import java.util.List;

public class CreateReservationController extends NavigationController {
    private final ClientService clientService = ServiceFactory.getClientService();
    private final RoomService roomService = ServiceFactory.getRoomService();
    private final AmenityService amenityService = ServiceFactory.getAmenityService();
    private final ReservationService reservationService = ServiceFactory.getReservationService();
    private final NotificationService notificationService = ServiceFactory.getNotificationService();
    private final Hotel currentHotel = SelectedHotelHolder.getHotel();

    @FXML private Label welcomeLabel;

    @FXML private TableView<Amenity> amenityTable;
    @FXML private TableColumn<Amenity, Long> amenityIdColumn;
    @FXML private TableColumn<Amenity, String> amenityNameColumn;
    @FXML private TableColumn<Amenity, String> amenitySeasonColumn;
    @FXML private TableColumn<Amenity, Boolean> amenityAddColumn;

    @FXML private TableView<Room> roomTable;
    @FXML private TableColumn<Room, String> roomNumberColumn;
    @FXML private TableColumn<Room, String> roomTypeColumn;
    @FXML private TableColumn<Room, BigDecimal> roomPriceColumn;
    @FXML private TableColumn<Room, Integer> roomCapacityColumn;
    @FXML private TableColumn<Room, Boolean> roomAddColumn;

    @FXML private TableView<Client>  clientTable;
    @FXML private TableColumn<Client, Long> idColumn;
    @FXML private TableColumn<Client, String> clientFirstNameColumn;
    @FXML private TableColumn<Client, String> clientLastNameColumn;
    @FXML private TableColumn<Client, Boolean> clientAddColumn;

    private ObservableList<Client> clientData = FXCollections.observableArrayList();
    private ObservableList<Room> roomData = FXCollections.observableArrayList();
    private ObservableList<Amenity> amenityData = FXCollections.observableArrayList();


    @FXML private TextField searchClient;
    @FXML private TextField searchRoom;
    @FXML private TextField searchAmenity;
    @FXML private DatePicker startDatePick;
    @FXML private DatePicker endDatePick;
    @FXML private ChoiceBox<ReservationType> reservationTypeChoice;
    @FXML private TextField reservationNumber;
    @FXML private Button createReservationBtn;

    @FXML private void initialize() {
        

        reservationTypeChoice.getItems().addAll(ReservationType.values());
        reservationNumber.setEditable(false);
        reservationNumber.setText(reservationService.generateReservationNumber(currentHotel));

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        clientFirstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        clientLastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));

        roomNumberColumn.setCellValueFactory(new PropertyValueFactory<>("number"));
        roomTypeColumn.setCellValueFactory(new PropertyValueFactory<>("roomCategory"));
        roomPriceColumn.setCellValueFactory(new PropertyValueFactory<>("pricePerNight"));
        roomCapacityColumn.setCellValueFactory(new PropertyValueFactory<>("capacity"));

        amenityIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        amenityNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        amenitySeasonColumn.setCellValueFactory(new PropertyValueFactory<>("season"));

        setCheckboxClientColumn();

        clientTable.setEditable(true);

        clientData.addAll(clientService.getAllClientsByHotel(currentHotel));
        roomData.addAll(roomService.getAllRoomsByHotel(currentHotel));
        amenityData.addAll(amenityService.getAllEnabledAmenitiesByHotel(currentHotel));


        FilteredList<Client> filteredClientData = new FilteredList<>(clientData, p -> true);

        //Runtime client search by first and last name
        searchClient.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredClientData.setPredicate(client -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String filter = newValue.toLowerCase();

                if (client.getFirstName().toLowerCase().contains(filter) || client.getLastName().toLowerCase().contains(filter)) {
                    return true;
                } else {
                    return false;
                }
            });
        });

        SortedList<Client> sortedClientData = new SortedList<>(filteredClientData);

        sortedClientData.comparatorProperty().bind(clientTable.comparatorProperty());
        clientTable.setItems(sortedClientData);


        //Runtime search by Start-End date of room occupancy / room number / type / capacity's room
        FilteredList<Room> filteredRoomData = new FilteredList<>(roomData, p -> true);

        searchRoom.textProperty().addListener((observable, oldValue, newValue) -> filterRooms(filteredRoomData));
        startDatePick.valueProperty().addListener((observable, oldValue, newValue) -> filterRooms(filteredRoomData));
        endDatePick.valueProperty().addListener((observable, oldValue, newValue) -> filterRooms(filteredRoomData));

        SortedList<Room> sortedRoomData = new SortedList<>(filteredRoomData);
        sortedRoomData.comparatorProperty().bind(roomTable.comparatorProperty());
        roomTable.setItems(sortedRoomData);

        setCheckboxRoomColumn();


        FilteredList<Amenity> filteredAmenityData = new FilteredList<>(amenityData, p -> true);

        searchAmenity.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredAmenityData.setPredicate(amenity -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String filter = newValue.toLowerCase();

                if (amenity.getName().toLowerCase().contains(filter) || amenity.getSeason().toString().toLowerCase().contains(filter)) {
                    return true;
                } else {
                    return false;
                }
            });
        });

        SortedList<Amenity> sortedAmenityData = new SortedList<>(filteredAmenityData);
        sortedAmenityData.comparatorProperty().bind(amenityTable.comparatorProperty());
        amenityTable.setItems(sortedAmenityData);

        amenityAddColumn.setCellValueFactory(cellData -> cellData.getValue().getSelected());
        amenityAddColumn.setCellFactory(CheckBoxTableCell.forTableColumn(amenityAddColumn));

        amenityTable.setRowFactory(aw -> {
            TableRow<Amenity> row = new TableRow<>();

            row.setOnMouseClicked(event -> {
                if (!row.isEmpty()) {
                    Amenity amenity =  row.getItem();

                    amenity.setSelected(!amenity.isSelected());

                    amenityTable.refresh();
                }
            });
            return row;
        });

        pickerRestrictions(startDatePick, endDatePick);

        styleColumns(roomTable);
        styleColumns(clientTable);
        styleColumns(amenityTable);
    }

    private void setCheckboxRoomColumn() {
        roomAddColumn.setCellValueFactory(cellData -> cellData.getValue().getSelected());
        roomAddColumn.setCellFactory(CheckBoxTableCell.forTableColumn(roomAddColumn));

        roomTable.setRowFactory(rw -> {
            TableRow<Room> row = new TableRow<>();

            row.setOnMouseClicked((event) -> {
                if (!row.isEmpty()) {
                    Room room = row.getItem();

                    room.setSelected(!room.isSelected());
                    roomTable.refresh();
                }
            });
            return row;
        });
    }

    private void pickerRestrictions(DatePicker startDate, DatePicker endDate) {

        startDatePick.setValue(java.time.LocalDate.now());
        endDatePick.setValue(java.time.LocalDate.now().plusDays(1));

        startDatePick.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(java.time.LocalDate date, boolean empty) {
                super.updateItem(date, empty);

                if (date.isBefore(java.time.LocalDate.now())) {
                    setDisable(true);
                    setStyle("-fx-background-color: #ffc0cb;");
                }
            }
        });

        endDatePick.setDayCellFactory(picker -> new DateCell() {

            @Override
            public void updateItem(java.time.LocalDate date, boolean empty) {
                super.updateItem(date, empty);

                java.time.LocalDate start = startDatePick.getValue();

                if (start != null && date.isBefore(start)) {
                    setDisable(true);
                    setStyle("-fx-background-color: #ffc0cb;");
                }
            }
        });


        startDatePick.valueProperty().addListener((obs, oldDate, newDate) -> {
            endDatePick.setValue(null);
            endDatePick.setDayCellFactory(picker -> new DateCell() {
                @Override
                public void updateItem(java.time.LocalDate date, boolean empty) {
                    super.updateItem(date, empty);

                    if (date.isBefore(newDate)) {
                        setDisable(true);
                        setStyle("-fx-background-color: #ffc0cb;");
                    }
                }
            });
        });

    }

    private void setCheckboxClientColumn() {
        clientAddColumn.setCellValueFactory(cellData -> cellData.getValue().getSelected());
        clientAddColumn.setCellFactory(CheckBoxTableCell.forTableColumn(clientAddColumn));

        clientTable.setRowFactory(cw -> {
            TableRow<Client> row = new TableRow<>();

            row.setOnMouseClicked(event -> {
                if (!row.isEmpty()) {
                    Client client = row.getItem();

                    client.setSelected(!client.isSelected());

                    clientTable.refresh();
                }
            });
            return row;
        });
    }

    @FXML
    private void createReservation() {
        ReservationCreationDTO dto = new ReservationCreationDTO();

        List<Client> selectedClients = clientData.stream().filter(Client ::isSelected).toList();

        notificationService.riskClientNotification(selectedClients);

        for (int i = 0; i < selectedClients.size(); i++) {
            Client client = selectedClients.get(i);

            if (client.isRisk()) {
                AlertMessage.showError("Risk Client", "There are clients with risk!");
                break;
            }
        }

        dto.setSelectedClients(selectedClients);
        List<Room> selectedRooms = roomData.stream().filter(Room::isSelected).toList();
        dto.setSelectedRooms(selectedRooms);
        List<Amenity> selectedAmenities = amenityData.stream().filter(Amenity::isSelected).toList();
        dto.setSelectedAmenities(selectedAmenities);
        dto.setReservationType(reservationTypeChoice.getValue());
        dto.setReservationNumber(reservationNumber.getText());
        dto.setStartDate(startDatePick.getValue());
        dto.setEndDate(endDatePick.getValue());


        try {
            ReservationValidator.validate(dto);
        }
        catch (ValidationException ex) {
            AlertMessage.showError("Invalid Reservation", ex.getMessage());
            ex.printStackTrace();
            return;
        }

        passData(dto);

    }

    private void passData(ReservationCreationDTO dto) {
        try {
            FXMLLoader fxmlLoader =
                    SceneSwitcher.switchSceneWithLoader((Stage) welcomeLabel.getScene().getWindow(),
                            "/views/verify-reservation.fxml");

            VerifyReservationController controller = fxmlLoader.getController();
            controller.initData(dto);
        }
        catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void filterRooms(FilteredList<Room> filteredRoomData) {
        filteredRoomData.setPredicate(room -> {
            String filter = searchRoom.getText();
            if (filter != null && !filter.trim().isEmpty()) {
                String lower = filter.toLowerCase();

                if (!(room.getNumber().toLowerCase().contains(lower) ||
                        room.getRoomCategory().toString().toLowerCase().contains(lower) ||
                        String.valueOf(room.getCapacity()).contains(lower))) {

                    return false;
                }
            }

            if (startDatePick.getValue() != null && endDatePick.getValue() != null) {

                var start = startDatePick.getValue().atStartOfDay();
                var end = endDatePick.getValue().atTime(23, 59, 59);

                var availableRooms = roomService.getAvailableRooms(start, end);

                return availableRooms.stream()
                        .anyMatch(r -> r.getId().equals(room.getId()));
            }

            return true;
        });
    }

    private <S> void styleColumns(TableView<S> tableView) {
        for (TableColumn<S,?> column : tableView.getColumns()) {
            column.setStyle("-fx-alignment: CENTER");
        }
    }

    @Override
    protected Stage getCurrentStage() {
        return (Stage) welcomeLabel.getScene().getWindow();
    }
}
