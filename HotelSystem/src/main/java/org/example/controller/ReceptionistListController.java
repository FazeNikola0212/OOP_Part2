package org.example.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;
import org.example.factory.ServiceFactory;
import org.example.model.hotel.Hotel;
import org.example.model.user.User;
import javafx.scene.control.*;
import org.example.service.hotel.HotelService;
import org.example.service.user.UserService;
import org.example.session.SelectedHotelHolder;
import org.example.session.Session;
import org.example.strategy.RoleConfigurable;
import org.example.strategy.RoleStrategy;
import org.example.strategy.RoleStrategyFactory;
import org.example.util.AlertMessage;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class ReceptionistListController extends NavigationController implements RoleConfigurable {

    private final UserService userService = ServiceFactory.getUserService();
    private final HotelService hotelService = ServiceFactory.getHotelService();

    @FXML private Label welcomeLabel;

    @FXML private TableView<User> receptionistTable;

    @FXML private TableColumn<User, Integer> idColumn;

    @FXML private TableColumn<User, String> nameColumn;

    @FXML private TableColumn<User, String> usernameColumn;

    @FXML private TableColumn<User, String> emailColumn;

    @FXML private TableColumn<User, String> hotelNameColumn;

    @FXML private TableColumn<User, LocalDateTime> updateAtColumn;

    @FXML private TableColumn<User, Void> inquiryColumn;

    @FXML private TableColumn<User, Void> removeColumn;

    @FXML
    private void initialize() {
        RoleStrategy roleStrategy = RoleStrategyFactory.getStrategy(Session.getSession().getLoggedUser().getRole());
        roleStrategy.applyPermissions(this);

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        updateAtColumn.setCellValueFactory(new PropertyValueFactory<>("updatedAt"));

        inquiryColumn.setCellFactory(col -> new TableCell<>() {
            private final Button queriesBtn = new Button("Inquiries");
            {
                queriesBtn.setOnAction(event -> {
                    try {
                        User receptionist = getTableView().getItems().get(getIndex());


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }
            @Override
            public void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(queriesBtn);
                }
            }
        });
        removeColumn.setCellFactory(col -> new TableCell<>() {
            private final Button removeBtn = new Button("Remove");
            {
                removeBtn.setOnAction(event -> {
                    try {
                        User receptionist = getTableView().getItems().get(getIndex());
                        hotelService.removeReceptionist(SelectedHotelHolder.getHotel().getId(), receptionist);
                        AlertMessage.showMessage("Removing receptionist", "Successfully removed receptionist");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }
            @Override
            public void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(removeBtn);
                }
            }
        });
        updateAtColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(LocalDateTime item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.format(java.time.format.DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")));
                }
            }
        });


        loadReceptionist();
        styleColumns(receptionistTable);

    }

    private void loadReceptionist() {
        List<User> allReceptionists = userService.getReceptionistsByHotelId(SelectedHotelHolder.getHotel().getId());
        receptionistTable.setItems(FXCollections.observableList(allReceptionists));
    }

    @Override
    protected Stage getCurrentStage() {
        return (Stage) welcomeLabel.getScene().getWindow();
    }
    private <S> void styleColumns(TableView<S> tableView) {
        for (TableColumn<S,?> column : tableView.getColumns()) {
            column.setStyle("-fx-alignment: CENTER");
        }
    }
}
