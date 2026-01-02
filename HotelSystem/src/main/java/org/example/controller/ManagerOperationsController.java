package org.example.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import lombok.Getter;
import org.example.factory.ServiceFactory;
import org.example.model.user.User;
import org.example.service.hotel.HotelService;
import org.example.service.user.UserService;
import org.example.session.SelectedHotelHolder;
import org.example.util.AlertMessage;
import org.example.util.SceneSwitcher;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Getter
public class ManagerOperationsController extends NavigationController {
    private final UserService userService = ServiceFactory.getUserService();
    private final HotelService hotelService = ServiceFactory.getHotelService();

    @FXML private Label currentUsername;
    @FXML private Label currentName;
    @FXML private Label currentEmail;

    @FXML private TableView<User> managersTable;
    @FXML private TableColumn<User, Long> idColumn;
    @FXML private TableColumn<User, String> nameColumn;
    @FXML private TableColumn<User, String> emailColumn;
    @FXML private TableColumn<User, String> usernameColumn;
    @FXML private TableColumn<User, LocalDateTime> updatedAtColumn;
    @FXML private TableColumn<User, Boolean> activeColumn;
    @FXML private TableColumn<User, Void> assignColumn;

    @FXML
    public void initialize() {
        if (!SelectedHotelHolder.getHotel().getManager().equals(SelectedHotelHolder.getHotel().getOwner())) {
            managersTable.setVisible(false);
        }

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        updatedAtColumn.setCellValueFactory(new PropertyValueFactory<>("updatedAt"));
        activeColumn.setCellValueFactory(new PropertyValueFactory<>("active"));

        assignColumn.setCellFactory(col -> new TableCell<>() {
            private final Button assignBtn = new Button("Assign");
            {
                assignBtn.setOnAction(event -> {
                    try {
                        User manager = getTableView().getItems().get(getIndex());
                        hotelService.assignManager(SelectedHotelHolder.getHotel().getId(), manager.getId());
                        AlertMessage.showMessage("Manager", "Successfully assigned manager");
                        SceneSwitcher.goBack((Stage) currentUsername.getScene().getWindow());

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(assignBtn);
                }
            }
        });
        updatedAtColumn.setCellFactory(col -> new TableCell<>() {
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

        loadData();
        styleColumns(managersTable);

    }

    private void loadData() {
        currentUsername.setText(SelectedHotelHolder.getHotel().getManager().getUsername());
        currentName.setText(SelectedHotelHolder.getHotel().getManager().getFullName());
        currentEmail.setText(SelectedHotelHolder.getHotel().getManager().getEmail());

        List<User> notAssignedManagers = userService.getAllNotAssignedManagers();
        managersTable.setItems(FXCollections.observableList(notAssignedManagers));
    }

    @FXML
    private void editManager() {}

    @FXML
    private void removeManager() throws IOException {
        hotelService.removeManager(SelectedHotelHolder.getHotel().getId(), SelectedHotelHolder.getHotel().getManager());
        AlertMessage.showMessage("Manager", "Successfully removed manager");
        SceneSwitcher.goBack((Stage) currentUsername.getScene().getWindow());
    }

    @Override
    protected Stage getCurrentStage() {
        return (Stage) currentUsername.getScene().getWindow();
    }
    private <S> void styleColumns(TableView<S> tableView) {
        for (TableColumn<S,?> column : tableView.getColumns()) {
            column.setStyle("-fx-alignment: CENTER");
        }
    }
}
