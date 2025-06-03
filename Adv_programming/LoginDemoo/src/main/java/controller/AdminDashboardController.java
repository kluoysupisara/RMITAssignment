package controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import model.Event;
import model.Model;
import model.Order;
import util.AlertUtils;
import util.StageUtils;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class AdminDashboardController {
    @FXML
    private TableView<Event> eventTable;
    @FXML private TableColumn<Event, String> eventNameColumn;
    @FXML private TableColumn<Event, String> venueDayColumn;
    @FXML private TableColumn<Event, String> priceColumn;
    @FXML private TableColumn<Event, Void> actionColumn;

    @FXML private TableView<Order> ordersTable;
    @FXML private TableColumn<Order, String> orderNumberColumn;
    @FXML private TableColumn<Order, String> usernameColumn;
    @FXML private TableColumn<Order, String> orderDateColumn;
    @FXML private TableColumn<Order, String> totalColumn;
    @FXML private TableColumn<Order, Void> viewItemsColumn;

    private Stage stage;
    private Stage parentStage;
    private Model model;

    public AdminDashboardController(Stage parentStage, Model model) {
        this.stage = new Stage();
        this.parentStage = parentStage;
        this.model = model;
    }

    @FXML
    public void initialize() {
        setupEventTable();
        setupOrderTable();
    }

    private void setupEventTable() {
        eventNameColumn.setCellValueFactory(data -> javafx.beans.binding.Bindings.createStringBinding(
                () -> data.getValue().getEventName()));
        venueDayColumn.setCellValueFactory(data -> javafx.beans.binding.Bindings.createStringBinding(
                () -> data.getValue().getVenue() + " - " + data.getValue().getDay()));
        priceColumn.setCellValueFactory(data -> javafx.beans.binding.Bindings.createStringBinding(
                () -> String.format("$%.2f", data.getValue().getPrice())));

        try {
            List<Event> events = model.getEventDao().getAllEvents();
            eventTable.setItems(FXCollections.observableArrayList(events));
        } catch (Exception e) {
            AlertUtils.showError("Database Error", "Failed to load events.", stage);
        }
    }

    private void setupOrderTable() {
        orderNumberColumn.setCellValueFactory(data -> javafx.beans.binding.Bindings.createStringBinding(
                data.getValue()::getOrderNumber));
        usernameColumn.setCellValueFactory(data -> javafx.beans.binding.Bindings.createStringBinding(
                data.getValue()::getUsername));
        orderDateColumn.setCellValueFactory(data -> javafx.beans.binding.Bindings.createStringBinding(
                () -> data.getValue().getOrderDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))));
        totalColumn.setCellValueFactory(data -> javafx.beans.binding.Bindings.createStringBinding(
                () -> String.format("$%.2f", data.getValue().getOrderPrice())));

        refreshOrderData();
    }

    private void refreshOrderData() {
        try {
            List<Order> orders = model.getOrderDao().getAllOrders();
            ordersTable.setItems(FXCollections.observableArrayList(orders));
        } catch (Exception e) {
            AlertUtils.showError("Database Error", "Failed to load orders.",stage);
        }
    }

    @FXML
    private void handleAddEvent() {
        AlertUtils.showInfo("Add Event", "This will open a dialog to add a new event.",stage);
    }

    @FXML
    private void handleModifyEvent() {
        AlertUtils.showInfo("Modify Event", "This will open a dialog to modify selected event.",stage);
    }

    @FXML
    private void handleToggleEvent() {
        AlertUtils.showInfo("Enable/Disable Event", "This will toggle event availability.", stage);
    }

    @FXML
    private void handleRefreshOrders() {
        refreshOrderData();
    }

    @FXML
    private void handleExportOrders() {
        AlertUtils.showInfo("Export Orders", "This will export all orders to a file.", stage);
    }

    @FXML
    private void handleLogout() {
        model.logout();
        parentStage.show();
        stage.close();
    }

    public void showStage(Pane root) {
        StageUtils.showStage(stage, root, "Admin Dashboard", 800, 600);
    }
}
