package controller;

import dao.OrderDao;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.CartItems;
import model.Model;
import model.Order;
import util.AlertUtils;
import util.StageUtils;

import java.io.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class OrderHistoryController {
    private Model model;
    private Stage stage;
    private Stage parentStage;

    @FXML private TableView<Order> OrderTable;
    @FXML private TableColumn<Order, String> orderColumn;
    @FXML private TableColumn<Order, String> dateColumn;
    @FXML private TableColumn<Order, String> eventColumn;
    @FXML private TableColumn<Order, String> totalColumn;
    @FXML private Button exportButton;

    public OrderHistoryController(Stage parentStage, Model model) {
        this.parentStage = parentStage;
        this.model = model;
        this.stage = new Stage();
    }
    public void initialize() {
        setupTable();
    }

    private void setupTable() {
        OrderDao orderDao = model.getOrderDao();
        String username = model.getCurrentUser().getUsername();

        List<Order> orders = orderDao.getOrdersByUser(username);
        ObservableList<Order> observableOrders = FXCollections.observableArrayList(orders);

        orderColumn.setCellValueFactory(data -> javafx.beans.binding.Bindings.createStringBinding(data.getValue()::getFormattedOrderId));
        dateColumn.setCellValueFactory(data -> javafx.beans.binding.Bindings.createStringBinding(() ->
                data.getValue().getOrderDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))));
        eventColumn.setCellValueFactory(data -> javafx.beans.binding.Bindings.createStringBinding(data.getValue()::getEventSummary));
        totalColumn.setCellValueFactory(data -> javafx.beans.binding.Bindings.createStringBinding(() ->
                String.format("$%.2f", data.getValue().getOrderPrice())));

        OrderTable.setItems(observableOrders);
    }
    @FXML
    private void handleExport() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Export Orders");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));

        // Suggest a default filename like orders-username.txt
        String username = model.getCurrentUser().getUsername();
        fileChooser.setInitialFileName("orders-" + username + ".txt");

        File selectedFile = fileChooser.showSaveDialog(stage);
        if (selectedFile != null) {
            exportOrdersToFile(selectedFile, username);
        }
    }

    private void exportOrdersToFile(File file, String username) {
        try (PrintWriter writer = new PrintWriter(file)) {
            List<Order> orders = model.getOrderDao().getOrdersByUser(username);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

            for (Order order : orders) {
                writer.println("Order Number: " + order.getFormattedOrderId());
                writer.println("Date: " + order.getOrderDate().format(formatter));
                writer.println("Event x quantity: ");
                for (CartItems item : order.getItems()) {
                    writer.printf("  - %s x%d\n", item.getEvent().getEventName(), item.getQuantity());
                }
                writer.printf("Total Price: $%.2f\n", order.getOrderPrice());
                writer.println("--------------------------------------------------");
            }

            AlertUtils.showInfo("Export Successful", "Orders exported to:\n" + file.getAbsolutePath(), stage);

        } catch (IOException e) {
            AlertUtils.showError("Export Failed", "Could not write to file:\n" + e.getMessage(), stage);
        }
    }

    @FXML
    private void handleBack() {
        stage.close();
        parentStage.show();
    }

    public void showStage(Pane root) {
        StageUtils.showStage(stage, root, "HistoryView", 600, 400);
    }

}
