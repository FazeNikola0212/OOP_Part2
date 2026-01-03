package org.example.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import org.example.factory.ServiceFactory;
import org.example.model.notification.Notification;
import org.example.service.notification.NotificationService;
import org.example.session.Session;

import java.awt.dnd.MouseDragGestureRecognizer;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class NotificationController extends NavigationController {
    private final NotificationService notificationService = ServiceFactory.getNotificationService();

    @FXML private TableView<Notification> notificationTable;
    @FXML private TableColumn<Notification, Long> idCol;
    @FXML private TableColumn<Notification, String> messageCol;
    @FXML private TableColumn<Notification, Boolean> globalCol;
    @FXML private TableColumn<Notification, Boolean> readCol;
    @FXML private TableColumn<Notification, LocalDateTime> createdAtCol;
    @FXML private TableColumn<Notification, String> typeCol;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public void initialize() {
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        messageCol.setCellValueFactory(new PropertyValueFactory<>("message"));
        globalCol.setCellValueFactory(new PropertyValueFactory<>("global"));
        readCol.setCellValueFactory(cell -> cell.getValue().readProperty());
        createdAtCol.setCellValueFactory(new PropertyValueFactory<>("createdAt"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));

        createdAtCol.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(LocalDateTime item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.format(formatter));
                }
            }
        });

        notificationTable.setRowFactory(tv -> {


            TableRow<Notification> row = new TableRow<>();

            row.setOnMouseClicked(event -> {

                if (!row.isEmpty()
                        && event.getClickCount() == 2
                        && event.getButton() == MouseButton.PRIMARY) {

                    Notification notification = row.getItem();

                    if (!notification.isRead()) {
                        markAsRead(notification);
                    }
                }
            });

            return row;
        });

        loadData();
        styleColumns(notificationTable);
    }

    private void markAsRead(Notification notification) {
        notificationService.markAsRead(notification.getId());
        loadData();
    }

    private void loadData() {
        notificationTable
                .setItems(FXCollections.observableList(
                        notificationService.getAllNotificationsByUserId(
                                Session.getSession().getLoggedUser().getId())));

    }

    @Override
    protected Stage getCurrentStage() {
        return (Stage) notificationTable.getScene().getWindow();
    }

    private <S> void styleColumns(TableView<S> tableView) {
        for (TableColumn<S,?> column : tableView.getColumns()) {
            column.setStyle("-fx-alignment: CENTER");
        }
    }
}
