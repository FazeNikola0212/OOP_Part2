package org.example.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.factory.ServiceFactory;
import org.example.model.hotel.Hotel;
import org.example.service.hotel.HotelService;
import org.example.session.SelectedHotelHolder;
import org.example.session.Session;
import org.example.util.SceneSwitcher;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;


public class ListHotelsController extends NavigationController {

    private final HotelService hotelService = ServiceFactory.getHotelService();

    @FXML
    private Label welcomeLabel;

    @FXML
    private TableView<Hotel>  hotelTable;

    @FXML
    private TableColumn<Hotel, Long> hotelIdColumn;

    @FXML
    private TableColumn<Hotel, String> hotelNameColumn;

    @FXML
    private TableColumn<Hotel, String> hotelAddressColumn;

    @FXML
    private TableColumn<Hotel, String> hotelCityColumn;

    @FXML
    private TableColumn<Hotel, String> hotelManagerColumn;

    @FXML
    private TableColumn<Hotel, Void> hotelOperationsBtn;


    @FXML
    public void initialize() {
        hotelIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        hotelNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        hotelAddressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        hotelCityColumn.setCellValueFactory(new PropertyValueFactory<>("city"));
        hotelManagerColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getManager().getFullName()));

        hotelOperationsBtn.setCellFactory(col -> new TableCell<>() {
            private final Button btn = new Button("Operations");

            {
                btn.setOnAction(event -> {
                    Hotel hotel = getTableView().getItems().get(getIndex());
                    try {
                        openOperationsForHotel(hotel);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(btn);
                }
            }
        });
        loadHotels();
        styleColumns(this.hotelTable);
    }

    @Override
    protected Stage getCurrentStage() {
        return (Stage) welcomeLabel.getScene().getWindow();
    }

    private void openOperationsForHotel(Hotel hotel) throws IOException {
        SelectedHotelHolder.setHotel(hotel);
        SceneSwitcher.switchScene((Stage) welcomeLabel.getScene().getWindow(), "/views/owner-panel.fxml");
    }

    private void loadHotels() {
        List<Hotel> allHotels = hotelService.getAllHotels(Session.getSession().getLoggedUser());
        hotelTable.setItems(FXCollections.observableArrayList(allHotels));
    }

    private <S> void styleColumns(TableView<S> tableView) {
        for (TableColumn<S,?> column : tableView.getColumns()) {
            column.setStyle("-fx-alignment: CENTER");
        }
    }




}
