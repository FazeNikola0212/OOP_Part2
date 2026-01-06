package org.example.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.example.DTO.RoomDetailsDTO;
import org.example.factory.ServiceFactory;
import org.example.model.room.Room;
import org.example.model.room.RoomStatus;
import org.example.service.room.RoomService;
import org.example.session.SelectedHotelHolder;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.Provider;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class RoomListController extends NavigationController {

    private final RoomService roomService = ServiceFactory.getRoomService();

    @FXML private Label currentHotel;

    @FXML private TableView<RoomDetailsDTO> roomsTable;
    @FXML private TableColumn<RoomDetailsDTO, String> numberCol;
    @FXML private TableColumn<RoomDetailsDTO, String> categoryCol;
    @FXML private TableColumn<RoomDetailsDTO, BigDecimal> priceCol;
    @FXML private TableColumn<RoomDetailsDTO, String> statusCol;
    @FXML private TableColumn<RoomDetailsDTO, Double> ratingCol;
    @FXML private TableColumn<RoomDetailsDTO, LocalDateTime> startDateCol;
    @FXML private TableColumn<RoomDetailsDTO, LocalDateTime> endDateCol;

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @FXML
    public void initialize() {
        currentHotel.setText(SelectedHotelHolder.getHotel().getName());
        currentHotel.setAlignment(Pos.CENTER);

        numberCol.setCellValueFactory(new PropertyValueFactory<>("number"));
        categoryCol.setCellValueFactory(new PropertyValueFactory<>("category"));
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        ratingCol.setCellValueFactory(new PropertyValueFactory<>("rating"));
        startDateCol.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        endDateCol.setCellValueFactory(new PropertyValueFactory<>("endDate"));

        roomsTable.setRowFactory(tv -> {
            TableRow<RoomDetailsDTO> row = new TableRow<>();

            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    RoomDetailsDTO room = row.getItem();
                    toggleRoomStatus(room);
                }
            });

            return row;
        });

        formatDateColumn(startDateCol);
        formatDateColumn(endDateCol);


        styleColumns(roomsTable);
        roomsTable.setItems(FXCollections.observableList(roomService.getRoomsDetails(SelectedHotelHolder.getHotel())));
    }

    private void toggleRoomStatus(RoomDetailsDTO room) {

        String currentStatus = room.getStatus().toString();

        RoomStatus newStatus;
        if ("AVAILABLE".equalsIgnoreCase(currentStatus)) {
            newStatus = RoomStatus.MAINTENANCE;
        } else if ("MAINTENANCE".equalsIgnoreCase(currentStatus)) {
            newStatus = RoomStatus.AVAILABLE;
        } else {
            return;
        }

        roomService.updateRoomStatus(room.getNumber(), newStatus);

        room.setStatus(newStatus);

        roomsTable.refresh();
    }


    private void formatDateColumn(TableColumn<RoomDetailsDTO, LocalDateTime> column) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        column.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(LocalDateTime item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : item.format(formatter));
            }
        });
    }

    @Override
    protected Stage getCurrentStage() {
        return (Stage) roomsTable.getScene().getWindow();
    }

    private <S> void styleColumns(TableView<S> tableView) {
        for (TableColumn<S,?> column : tableView.getColumns()) {
            column.setStyle("-fx-alignment: CENTER");
        }
    }
}
