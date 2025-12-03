package org.example.controller;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lombok.Getter;
import org.example.DTO.CreateRoomDTO;
import org.example.factory.ServiceFactory;
import org.example.model.hotel.Hotel;
import org.example.model.room.RoomCategory;
import org.example.model.room.RoomStatus;
import org.example.service.hotel.HotelService;
import org.example.service.room.RoomService;
import org.example.session.SelectedHotelHolder;
import org.example.session.Session;
import org.example.strategy.RoleConfigurable;
import org.example.strategy.RoleStrategy;
import org.example.strategy.RoleStrategyFactory;
import org.example.util.AlertMessage;

import java.math.BigDecimal;

@Getter
public class CreateRoomController extends NavigationController implements RoleConfigurable {

    private final HotelService hotelService = ServiceFactory.getHotelService();
    private final RoomService roomService = ServiceFactory.getRoomService();

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private Label welcomeLabel;

    @FXML
    private TextField numberField;

    @FXML
    private TextField priceField;

    @FXML
    private ChoiceBox<RoomCategory> categoryChoiceBox;

    @FXML
    private ChoiceBox<RoomStatus> statusChoiceBox;

    @FXML
    private ChoiceBox<Integer> capacityChoiceBox;

    @FXML
    private Label currentHotelLabel;

    @FXML
    private Button createRoomBtn;

    @FXML
    public void initialize() {
        RoleStrategy roleStrategy = RoleStrategyFactory.getStrategy(Session.getSession().getLoggedUser().getRole());
        roleStrategy.applyPermissions(this);
        categoryChoiceBox.getItems().addAll(RoomCategory.values());
        statusChoiceBox.getItems().addAll(RoomStatus.values());
        capacityChoiceBox.getItems().addAll(0, 1, 2, 3, 4, 5, 6);

        priceField.setTextFormatter(new TextFormatter<>(change ->
            {
            if (change.getControlNewText().matches("\\d*(\\.\\d{0,2})?")) {
                return change;
            }
            return null;
        }));
    }

    @FXML
    private void createRoom() {
        BigDecimal price = new BigDecimal(priceField.getText());

        CreateRoomDTO request = new CreateRoomDTO();
        request.setNumber(numberField.getText());
        request.setCapacity(capacityChoiceBox.getValue());
        request.setRoomStatus(statusChoiceBox.getValue());
        request.setRoomCategory(categoryChoiceBox.getValue());
        request.setPricePerNight(price);
        request.setHotel(SelectedHotelHolder.getHotel());

        roomService.createRoom(request);
        AlertMessage.showMessage("Room Creation", "Successfully Created Room");
        clearTextFields(anchorPane);
    }

    @Override
    protected Stage getCurrentStage() {
        return (Stage) categoryChoiceBox.getScene().getWindow();
    }

    private void clearTextFields(AnchorPane anchorPane) {
        for (Node node : anchorPane.getChildren()) {
            if (node instanceof TextField) {
                ( (TextField) node ).clear();
            } else if (node instanceof ChoiceBox) {
                ( (ChoiceBox) node ).getSelectionModel().clearSelection();
            }
            else if (node instanceof AnchorPane) {
                clearTextFields((AnchorPane) node);
            }
        }
    }
}
