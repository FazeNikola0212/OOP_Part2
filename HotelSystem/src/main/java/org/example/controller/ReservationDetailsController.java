package org.example.controller;

import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.converter.BigDecimalStringConverter;
import javafx.util.converter.IntegerStringConverter;
import org.example.DTO.ReservationRowDTO;
import org.example.factory.ServiceFactory;
import org.example.model.amenity.Amenity;
import org.example.model.client.Client;
import org.example.service.amenity.AmenityService;
import org.example.service.reservation.ReservationService;
import org.example.session.SelectedHotelHolder;
import org.example.util.AlertMessage;
import javafx.scene.control.Button;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReservationDetailsController extends NavigationController {
    private final ReservationService  reservationService = ServiceFactory.getReservationService();
    private final AmenityService amenityService = ServiceFactory.getAmenityService();
    private ReservationRowDTO reservation;

    @FXML private Label reservationNumber;
    @FXML private Label roomsNumber;
    @FXML private Label totalPriceRes;
    @FXML private ListView<String> clientsList;
    @FXML private Label status;
    @FXML private ListView<String> amenitiesList;
    @FXML private Label star1;
    @FXML private Label star2;
    @FXML private Label star3;
    @FXML private Label star4;
    @FXML private Label star5;
    private List<Label> stars;
    @FXML private Button addAmenityBtn;
    
    @FXML private TableView<Amenity> amenityTable;
    @FXML private TableColumn<Amenity, String> amenityNameCol;
    @FXML private TableColumn<Amenity, BigDecimal> amenityPriceCol;
    @FXML private TableColumn<Amenity, Integer> amenityQuantityCol;
    private final Map<Amenity, BigDecimal> amenityPrices = new HashMap<>();
    private final Map<Amenity, Integer> amenityQuantity = new HashMap<>();
    private int selectedRating = 0;

    public void setReservation(ReservationRowDTO reservation) {
        this.reservation = reservation;
        loadData();
    }

    public void loadData() {
        if (reservation.getReservationStatus().equals("Expired")) {
            addAmenityBtn.setDisable(true);
            amenityTable.setVisible(false);
        }

        reservationNumber.setText(reservation.getReservationNumber());
        reservationNumber.setTextFill(Color.rgb(240, 97, 86));
        roomsNumber.setText(reservation.getRoomsNumber());
        roomsNumber.setTextFill(Color.rgb(240, 97, 86));
        clientsList.getItems().setAll(
                reservationService.getAllClientsByReservationId(reservation.getId())
                        .stream()
                        .map(Client::getFullName)
                        .toList()
        );
        status.setText(reservation.getReservationStatus());
        status.setTextFill(Color.rgb(240, 97, 86));
        amenitiesList.getItems().setAll(
                reservationService.getAllAmenitiesNamesByReservationId(reservation.getId())
        );
        totalPriceRes.setText(reservation.getTotalPrice().toString() + "€");
        totalPriceRes.setTextFill(Color.rgb(240, 97, 86));

    }

    @FXML
    public void initialize() {
        stars = List.of(star1, star2, star3, star4, star5);
        stars.forEach(s -> s.setTextFill(Color.GOLD));

        amenityNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        amenityPriceCol.setCellValueFactory(c -> {
            Amenity amenity = c.getValue();

            BigDecimal price = amenityPrices.getOrDefault(amenity, BigDecimal.ZERO);
            return new SimpleObjectProperty<>(price);
        });

        amenityPriceCol.setCellFactory(TextFieldTableCell.forTableColumn(new BigDecimalStringConverter()));

        amenityPriceCol.setOnEditCommit(event -> {
            Amenity amenity = event.getRowValue();
            BigDecimal newPrice = event.getNewValue();

            if (newPrice != null && newPrice.compareTo(BigDecimal.ZERO) >= 0) {
                amenityPrices.put(amenity, newPrice);
            } else {
                amenityTable.refresh();
            }
        } );

        amenityQuantityCol.setCellValueFactory(c -> {
            Amenity amenity = c.getValue();

            int quantity = amenityQuantity.getOrDefault(amenity, 0);
            return new SimpleObjectProperty<>(quantity);
        });

        amenityQuantityCol.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));

        amenityQuantityCol.setOnEditCommit(event -> {
            Amenity amenity = event.getRowValue();
            int quantity = event.getNewValue();

            if (quantity > 0) {
                amenityQuantity.put(amenity, quantity);
            } else {
                amenityTable.refresh();
            }
        });

        loadAmenities();

        clientsList.setStyle("-fx-alignment: CENTER;");
        clientsList.setEditable(false);
        amenitiesList.setStyle("-fx-alignment: CENTER;");

        for (int i = 0; i < stars.size(); i++) {
            final int rating = i + 1;
            stars.get(i).setOnMouseEntered(e -> previewRating(rating));
            stars.get(i).setOnMouseExited(e -> setRating(selectedRating));
            stars.get(i).setOnMouseClicked(e -> setRating(rating));
        }



    }

    private void loadAmenities() {
        ObservableList<Amenity> amenities = FXCollections.observableArrayList(amenityService.getAllEnabledAmenitiesByHotel(SelectedHotelHolder.getHotel()));
        amenityTable.setItems(amenities);
        amenityTable.setEditable(true);
        amenityPriceCol.setEditable(true);
        amenityQuantityCol.setEditable(true);
    }

    private void disableStars() {
        stars.forEach(s -> s.setDisable(false));
    }

    private void previewRating(int rating) {
        for (int i = 0; i < stars.size(); i++) {
            stars.get(i).setText(i < rating ? "★" : "☆");
        }
    }

    private void setRating(int rating) {
        selectedRating = rating;

        for (int i = 0; i < stars.size(); i++) {
            stars.get(i).setText(i < rating ? "★" : "☆");
        }
    }

    @FXML
    public void rateClient() {
        if (selectedRating == 0) {
            AlertMessage.showError("Rating Error", "Please select a rating first");
            return;
        }

        reservationService.rateClient(reservation.getId(), selectedRating);

        AlertMessage.showMessage("Rating Success", "Successfully rated Client");
        disableStars();
    }

    @FXML
    public void addAmenity() {
        Amenity selectedAmenity = amenityTable.getSelectionModel().getSelectedItem();

        if (selectedAmenity == null) {
            AlertMessage.showError("Error", "Please select a amenity first");
            return;
        }

        Integer quantity = amenityQuantity.get(selectedAmenity);
        BigDecimal price = amenityPrices.get(selectedAmenity);

        if (quantity == null || quantity <= 0) {
            AlertMessage.showError("Error", "Please select a quantity");
            return;
        }
        if (price == null || price.compareTo(BigDecimal.ZERO) <= 0) {
            AlertMessage.showError("Error", "Please select a price");
            return;
        }

        try {
            reservationService.addReservationAmenity(selectedAmenity, reservation.getId(), quantity, price);
            AlertMessage.showMessage("Success", "Successfully added Reservation");

            amenityTable.refresh();
        } catch (Exception e) {
            AlertMessage.showError("Error", e.getMessage());
        }
    }



    @Override
    protected Stage getCurrentStage() {
        return (Stage) amenityTable.getScene().getWindow();
    }

}
