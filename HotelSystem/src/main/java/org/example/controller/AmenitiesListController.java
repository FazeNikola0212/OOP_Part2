package org.example.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import lombok.Getter;
import lombok.Setter;
import org.example.DTO.EditAmenityDTO;
import org.example.factory.ServiceFactory;
import org.example.model.amenity.Amenity;
import org.example.model.amenity.SeasonAmenity;
import org.example.model.hotel.Hotel;
import org.example.service.amenity.AmenityService;
import org.example.session.Session;
import org.example.strategy.RoleConfigurable;
import org.example.strategy.RoleStrategy;
import org.example.strategy.RoleStrategyFactory;
import org.example.util.SceneSwitcher;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

@Getter
@Setter
public class AmenitiesListController extends NavigationController implements RoleConfigurable {

    private final AmenityService amenityService = ServiceFactory.getAmenityService();
    private Hotel hotel;

    @FXML
    private Label currentHotel;

    @FXML
    private TableView<Amenity> amenitiesTable;

    @FXML
    private TableColumn<Amenity, Long> idColumn;

    @FXML
    private TableColumn<Amenity, String> nameColumn;

    @FXML
    private TableColumn<Amenity, String> descriptionColumn;

    @FXML
    private TableColumn<Amenity, SeasonAmenity> seasonColumn;

    @FXML
    private TableColumn<Amenity, Integer> usageColumn;

    @FXML
    private TableColumn<Amenity, Void> editColumn;

    @FXML
    private TableColumn<Amenity, Boolean> enabledColumn;

    @FXML
    public void initialize() {
        RoleStrategy strategy = RoleStrategyFactory.getStrategy(Session.getSession().getLoggedUser().getRole());
        strategy.applyPermissions(this);

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        seasonColumn.setCellValueFactory(new PropertyValueFactory<>("season"));
        usageColumn.setCellValueFactory(new PropertyValueFactory<>("usageCount"));
        enabledColumn.setCellValueFactory(new PropertyValueFactory<>("enabled"));

        editColumn.setCellFactory(col -> new TableCell<>() {
            private final Button editBtn = new Button("Edit");

            {
                editBtn.setOnAction(event -> {
                    try {
                        Amenity amenity = getTableView().getItems().get(getIndex());

                        EditAmenityDTO dto =  new EditAmenityDTO();
                        dto.setId(amenity.getId());
                        dto.setName(amenity.getName());
                        dto.setDescription(amenity.getDescription());
                        dto.setSeasonAmenity(amenity.getSeason());
                        dto.setEnabled(amenity.isEnabled());

                        FXMLLoader fxmlLoader = SceneSwitcher.switchSceneWithLoader((Stage) amenitiesTable.getScene().getWindow(), "/views/edit-amenity.fxml");
                        EditAmenityController controller = fxmlLoader.getController();
                        controller.setAmenity(dto);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
            @Override
            public void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(editBtn);
                }
            }
        });
        loadAmenities();
        styleColumns(amenitiesTable);
    }


    private void loadAmenities() {
        List<Amenity> amenityList = amenityService.getAllAmenitiesByHotel(hotel);
        amenitiesTable.setItems(FXCollections.observableArrayList(amenityList));
    }

    @Override
    protected Stage getCurrentStage() {
        return (Stage) amenitiesTable.getScene().getWindow();
    }

    private <S> void styleColumns(TableView<S> tableView) {
        for (TableColumn<S,?> column : tableView.getColumns()) {
            column.setStyle("-fx-alignment: CENTER");
        }
    }
}
