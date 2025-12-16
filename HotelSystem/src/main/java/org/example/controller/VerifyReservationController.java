package org.example.controller;

import javafx.beans.property.*;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.converter.BigDecimalStringConverter;
import javafx.util.converter.IntegerStringConverter;
import org.example.DTO.PersistReservationDTO;
import org.example.DTO.ReservationAmenityDTO;
import org.example.DTO.ReservationCreationDTO;
import org.example.DTO.ReservationRoomDTO;
import org.example.factory.ServiceFactory;
import org.example.model.amenity.Amenity;
import org.example.model.client.Client;
import org.example.model.room.Room;
import org.example.service.reservation.ReservationService;
import org.example.session.Session;
import org.example.util.AlertMessage;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class VerifyReservationController extends NavigationController {
    private ReservationCreationDTO dto;
    private final ReservationService reservationService = ServiceFactory.getReservationService();
    private final Map<Room, LocalDateTime[]> roomDateMap = new HashMap<>();
    private final Map<Amenity, BigDecimal> amenityPrices = new HashMap<>();
    private final Map<Amenity, Integer> amenityQuantities = new HashMap<>();
    private final DateTimeFormatter dateFormatter =
            DateTimeFormatter.ofPattern("dd-MMM-yyyy").withLocale(Locale.ENGLISH);


    private Client mainClient = null;

    @FXML private Label reservationNumber;
    @FXML private Label reservationType;

    @FXML private TableView<Client> clientTable;
    @FXML private TableColumn<Client, Long> clientIdColumn;
    @FXML private TableColumn<Client, String> clientFirstNameColumn;
    @FXML private TableColumn<Client, String> clientLastNameColumn;
    @FXML private TableColumn<Client, Boolean> assignMainColumn;

    @FXML private TableView<Room> roomTable;
    @FXML private TableColumn<Room, String> roomNumberColumn;
    @FXML private TableColumn<Room, String> roomTypeColumn;
    @FXML private TableColumn<Room, LocalDateTime> startDateColumn;
    @FXML private TableColumn<Room, LocalDateTime> endDateColumn;
    @FXML private TableColumn<Room, BigDecimal> totalPriceColumn;

    @FXML private TableView<Amenity> amenityTable;
    @FXML private TableColumn<Amenity, String> amenityNameColumn;
    @FXML private TableColumn<Amenity, BigDecimal> amenityPriceColumn;
    @FXML private TableColumn<Amenity, Integer> amenityQuantityColumn;

    @FXML private Button verifyBtn;
    @FXML private Label totalPrice;

    public void initData(ReservationCreationDTO dto) {
        this.dto = dto;

        fillData();
    }

    private void fillData() {
        reservationNumber.setText(dto.getReservationNumber());
        reservationType.setText(dto.getReservationType().toString());
        verifyBtn.setAlignment(Pos.CENTER);

        clientIdColumn.setCellValueFactory(c -> new ReadOnlyObjectWrapper<>(c.getValue().getId()));
        clientFirstNameColumn.setCellValueFactory(c -> new ReadOnlyObjectWrapper<>(c.getValue().getFirstName()));
        clientLastNameColumn.setCellValueFactory(c -> new ReadOnlyObjectWrapper<>(c.getValue().getLastName()));
        clientTable.getItems().addAll(dto.getSelectedClients());

        clientTable.setEditable(true);
        assignMainColumn.setCellValueFactory(c -> {
            Client client = c.getValue();

            BooleanProperty selected = new SimpleBooleanProperty(client.equals(mainClient));

            selected.addListener((observable, oldValue, newValue) -> {
                if (newValue) {
                    mainClient = client;

                } else if (mainClient == client ) {
                    mainClient = null;
                }
                clientTable.refresh();
            });
            return selected;
        });

        assignMainColumn.setCellFactory(CheckBoxTableCell.forTableColumn(assignMainColumn));

        roomNumberColumn.setCellValueFactory(c -> new ReadOnlyObjectWrapper<>(c.getValue().getNumber()));
        roomTypeColumn.setCellValueFactory(c -> new ReadOnlyObjectWrapper<>(c.getValue().getRoomCategory().toString()));

        startDateColumn.setCellValueFactory(c -> {
            LocalDateTime[] dates = roomDateMap.get(c.getValue());
            return new ReadOnlyObjectWrapper<>(dates != null ? dates[0] : null);

        });

        endDateColumn.setCellValueFactory(c -> {
            LocalDateTime[] dates = roomDateMap.get(c.getValue());
            return new ReadOnlyObjectWrapper<>(dates != null ? dates[1] : null);
        });

        formatDateSelect();

        totalPriceColumn.setCellValueFactory(c -> {
            Room room = c.getValue();
            LocalDateTime[] dates = roomDateMap.get(room);

            if (dates == null || dates[0] == null || dates[1] == null)
                return new ReadOnlyObjectWrapper<>(BigDecimal.ZERO);

            BigDecimal total = reservationService.totalRoomPrice(room, dates[0], dates[1]);
            updateTotalPrice();
            return new ReadOnlyObjectWrapper<>(total);
        });

        roomTable.getItems().addAll(dto.getSelectedRooms());
        setupRoomRowDoubleClick();

        amenityNameColumn.setCellValueFactory(c -> new ReadOnlyObjectWrapper<>(c.getValue().getName()));

        amenityPriceColumn.setCellValueFactory(c -> {
            Amenity amenity = c.getValue();

            BigDecimal price = amenityPrices.getOrDefault(amenity, BigDecimal.ZERO);
            return new SimpleObjectProperty<>(price);
        });

        amenityPriceColumn.setCellFactory(TextFieldTableCell.forTableColumn(new BigDecimalStringConverter()));

        amenityPriceColumn.setOnEditCommit(event -> {
            Amenity amenity = event.getRowValue();
            BigDecimal newPrice = event.getNewValue();

            if (newPrice != null && newPrice.compareTo(BigDecimal.ZERO) >= 0) {
                amenityPrices.put(amenity, newPrice);
                updateTotalPrice();
            } else {
                amenityTable.refresh();
            }
        } );

        amenityQuantityColumn.setCellValueFactory(c -> {
            Amenity amenity = c.getValue();

            int quantity = amenityQuantities.getOrDefault(amenity, 0);
            return new SimpleObjectProperty<>(quantity);
        });

        amenityQuantityColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));

        amenityQuantityColumn.setOnEditCommit(event -> {
            Amenity amenity = event.getRowValue();
            int newQuantity = event.getNewValue();

            if (newQuantity != 0) {
                amenityQuantities.put(amenity, newQuantity);
                updateTotalPrice();
            } else {
                amenityTable.refresh();
            }

        });

        for (Room room : dto.getSelectedRooms()) {
            LocalDateTime start = dto.getStartDate().atStartOfDay();
            LocalDateTime end = dto.getEndDate().atTime(LocalTime.MAX);

            roomDateMap.put(room, new LocalDateTime[]{start, end});
        }

        amenityTable.setEditable(true);
        amenityTable.getItems().addAll(dto.getSelectedAmenities());

        styleColumns(clientTable);
        styleColumns(amenityTable);
        styleColumns(roomTable);
    }

    @FXML private void verifyReservation() {
        PersistReservationDTO reservationDTO = getPersistReservationDTO();
        if (reservationDTO == null) return;

        reservationService.createReservation(reservationDTO);
        AlertMessage.showMessage("Reservation", "Reservation created successfully");

    }

    private void updateTotalPrice() {

        totalPrice.setText("The total price for the Reservation is: " +
                reservationService.totalReservationPrice(roomDateMap, amenityQuantities, amenityPrices).toString() + "€");

        totalPrice.setAlignment(Pos.CENTER);
        totalPrice.setStyle("-fx-text-fill: darkred; -fx-font-size: 16px");
    }
    private void formatDateSelect() {
        startDateColumn.setCellFactory(col -> new TableCell<Room, LocalDateTime>() {
            @Override
            protected void updateItem(LocalDateTime item, boolean empty) {
                super.updateItem(item, empty);

                if (item == null) {
                    setText("");
                } else {
                    setText(item.format(dateFormatter));
                }
            }
        });


        endDateColumn.setCellFactory(column -> new TableCell<Room, LocalDateTime>() {
            @Override
            protected void updateItem(LocalDateTime item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText("");
                } else {
                    setText(item.format(dateFormatter));
                }
            }
        });
    }
    private void disablePastDatesAndHighlightToday(DatePicker datePicker, DatePicker startPicker) {
        LocalDate today = LocalDate.now();

        datePicker.setDayCellFactory(dp -> new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) return;

                if (item.isBefore(today)) {
                    setDisable(true);
                    setStyle("-fx-background-color: #ffc0cb;");
                }

                if (startPicker != null && startPicker.getValue() != null && item.isBefore(startPicker.getValue())) {
                    setDisable(true);
                    setStyle("-fx-background-color: #ffc0cb;");
                }

                if (item.equals(today)) {
                    setStyle("-fx-background-color: #ffc0cb;");
                }
            }
        });
    }
    private void setupRoomRowDoubleClick() {
        roomTable.setRowFactory(tv -> {
            TableRow<Room> row = new TableRow<>();

            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getClickCount() == 2) {
                    openDateSelectionPopup(row.getItem());
                }
            });

            return row;
        });
    }
    private void openDateSelectionPopup(Room room) {

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Select Dates for Room " + room.getNumber());

        DatePicker startPicker = new DatePicker();
        DatePicker endPicker = new DatePicker();



        LocalDateTime[] existing = roomDateMap.get(room);
        if (existing != null) {
            if (existing[0] != null) startPicker.setValue(existing[0].toLocalDate());
            if (existing[1] != null) endPicker.setValue(existing[1].toLocalDate());
        }

        disablePastDatesAndHighlightToday(startPicker, null);
        disablePastDatesAndHighlightToday(endPicker, startPicker);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.add(new Label("Start Date:"), 0, 0);
        grid.add(startPicker, 1, 0);
        grid.add(new Label("End Date:"), 0, 1);
        grid.add(endPicker, 1, 1);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(button -> {
            if (button == ButtonType.OK) {
                LocalDate start = startPicker.getValue();
                LocalDate end = endPicker.getValue();

                if (start != null && end != null) {
                    roomDateMap.put(room, new LocalDateTime[]{
                            start.atStartOfDay(),
                            end.atTime(23, 59, 59)
                    });
                    roomTable.refresh();
                }
            }
            return null;
        });

        dialog.showAndWait();
    }
    private PersistReservationDTO getPersistReservationDTO() {
        List<ReservationRoomDTO> roomDTO = new ArrayList<>();
        for (Room room : roomTable.getItems()) {
            LocalDateTime[] dates = roomDateMap.get(room);

            if (dates == null || dates[0] == null || dates[1] == null) {
                AlertMessage.showError("Select Date", "Room " + room.getNumber() + " has no selected dates");
                return null;
            }

            BigDecimal totalRoomPrice = reservationService.totalRoomPrice(room,  dates[0], dates[1]);

            ReservationRoomDTO tempDto = new ReservationRoomDTO();
            tempDto.setRoom(room);
            tempDto.setPrice(totalRoomPrice);
            tempDto.setStartDate(dates[0]);
            tempDto.setEndDate(dates[1]);

            roomDTO.add(tempDto);
        }

        List<ReservationAmenityDTO> amenityDTO = new ArrayList<>();
        for (Amenity amenity : amenityTable.getItems()) {
            BigDecimal price = amenityPrices.getOrDefault(amenity, BigDecimal.ZERO);
            int qty = amenityQuantities.getOrDefault(amenity, 0);

            if (qty <= 0) {
                AlertMessage.showError("Invalid quantity", "Select more than 1 quantity");
                return null;
            }

            ReservationAmenityDTO tempDto = new ReservationAmenityDTO();
            tempDto.setAmenity(amenity);
            tempDto.setName(amenity.getName());
            tempDto.setPrice(price);
            tempDto.setQuantity(qty);

            amenityDTO.add(tempDto);
        }


        return getPersistReservationDTO(roomDTO, amenityDTO);
    }

    private PersistReservationDTO getPersistReservationDTO(List<ReservationRoomDTO> roomDTO, List<ReservationAmenityDTO> amenityDTO) {
        PersistReservationDTO reservationDTO = new PersistReservationDTO();
        reservationDTO.setReservationNumber(reservationNumber.getText());
        reservationDTO.setMainClient(mainClient);
        reservationDTO.setReceptionist(Session.getSession().getLoggedUser());
        reservationDTO.setType(dto.getReservationType());
        reservationDTO.setTotalPrice(reservationService.totalReservationPrice(roomDateMap, amenityQuantities, amenityPrices));
        reservationDTO.setGuests(clientTable.getItems().stream().toList());
        reservationDTO.setRooms(roomDTO);
        reservationDTO.setAmenities(amenityDTO);
        return reservationDTO;
    }
    private <S> void styleColumns(TableView<S> tableView) {
        for (TableColumn<S,?> column : tableView.getColumns()) {
            column.setStyle("-fx-alignment: CENTER");
        }
    }

    @Override
    protected Stage getCurrentStage() {
        return (Stage) reservationNumber.getScene().getWindow();
    }
}
