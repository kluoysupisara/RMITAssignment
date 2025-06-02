package controller;

import dao.Database;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.CartItems;
import model.Event;
import model.Model;
import model.ShoppingCart;
import util.AlertUtils;
import util.StageUtils;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class ShoppingCartController {
    private Stage stage;
    private Model model;
    @FXML
    private Button backButton;
    @FXML
    private Label cartLabel;
    @FXML
    private TableView<CartItems> cartTable;
    @FXML
    private TableColumn<CartItems, String> eventColumn;
    @FXML
    private TableColumn<CartItems, String> venueColumn;
    @FXML
    private TableColumn<CartItems, String> dateColumn;
    @FXML
    private TableColumn<CartItems, Double> priceColumn;
    @FXML
    private TableColumn<CartItems, Integer> quantityColumn;
    @FXML
    private TableColumn<CartItems, Void> removeColumn;
    @FXML
    private Button checkoutButton;
    @FXML
    private Label totalLabel;
    private Stage parentStage;
    private HomeController homeController;  // direct reference
    private Double totalPrice;

    public ShoppingCartController(Stage parentstage, Model model, HomeController homeController) {
        this.stage = new Stage();
        this.model = model;
        this.parentStage = parentstage;
        this.homeController = homeController;
    }

    @FXML
    public void initialize() {
        System.out.println("Initializing ShoppingCartController");
        setupColumns();
        setupQuantityColumn();
        setupRemoveButton();

        // Set table data
        cartTable.setItems(model.getShoppingCart().getItems());
        updateTotal();
        if(model.getShoppingCart() == null || model.getShoppingCart().getItems().isEmpty()) {
            checkoutButton.setDisable(true);
        }
    }
    public void refreshCart() {
        System.out.println("Refreshing ShoppingCartController");
        cartTable.setItems(model.getShoppingCart().getItems()); // Rebind just in case
        updateTotal(); // Recalculate total
    }


    private void setupColumns() {
        eventColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getEvent().getEventName()));
        venueColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getEvent().getVenue()));
        dateColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getEvent().getDay()));
        priceColumn.setCellValueFactory(cellData ->
                new SimpleDoubleProperty(cellData.getValue().getEvent().getPrice()).asObject());
    }

    private void setupQuantityColumn() {
        quantityColumn.setCellFactory(col -> new TableCell<>() {
            private final Spinner<Integer> spinner = new Spinner<>();

            {
                spinner.setEditable(true);
                spinner.valueProperty().addListener((obs, oldVal, newVal) -> {
                    CartItems item = getTableView().getItems().get(getIndex());
                    int available = item.getEvent().getAvailableTickets();

                    if (newVal > available) {
                        // Show Alert
                        AlertUtils.showWarning("Ticket Limit Exceeded", "Only " + available + " tickets available for this event.", stage);
                        // Reset spinner value
                        spinner.getValueFactory().setValue(oldVal);
                    } else {
                        // Update quantity in the model
                        item.setQuantity(newVal);
                        updateTotal();
                    }
                });
            }

            @Override
            protected void updateItem(Integer quantity, boolean empty) {
                super.updateItem(quantity, empty);
                if (empty || getIndex() >= getTableView().getItems().size()) {
                    setGraphic(null);
                } else {
                    CartItems item = getTableView().getItems().get(getIndex());
                    // get the max value of each event
                    int max = item.getEvent().getAvailableTickets();
                    spinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, max, item.getQuantity()));
                    setGraphic(spinner);
                }
            }
        });
    }

    private void setupRemoveButton() {
        removeColumn.setCellFactory(col -> new TableCell<>() {
            private final Button removeBtn = new Button("Remove");

            {
                removeBtn.setOnAction(e -> {
                    CartItems item = getTableView().getItems().get(getIndex());
                    model.getShoppingCart().removeItem(item.getEvent());
                    cartTable.getItems().remove(item);
                    updateTotal();
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : removeBtn);
            }
        });
    }


    private double updateTotal() {
        double totalPrice = model.getShoppingCart().getItems().stream()
                .mapToDouble(item -> item.getEvent().getPrice() * item.getQuantity())
                .sum();
        totalLabel.setText("$" + String.format("%.2f",totalPrice));
        return totalPrice;
    }

//
//    @FXML
//    private void handleBack() {
//        System.out.println("Back ShoppingCartController");
//
//        try {
//            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/HomeView.fxml"));
//            HomeController homeController = new HomeController(, model);
//            loader.setController(homeController);
//
//            Pane root = loader.load();
//            homeController.showStage(root);
//            stage.close();
//        } catch (IOException e) {
//            System.out.println("Failed to reload HomeView: " + e.getMessage());
//        }
//    }

    public void showStage(Pane root) {
        StageUtils.showStage(stage, root, "Shopping Cart", 600, 400);
    }

    @FXML
    private void handleCheckout() {
//        double total = model.getShoppingCart().getItems().stream()
//                .mapToDouble(item -> item.getEvent().getPrice() * item.getQuantity())
//                .sum();

        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Checkout");
        confirmAlert.setHeaderText("Total Price: $" + String.format("%.2f", updateTotal()));
        confirmAlert.setContentText("Do you want to proceed to payment?");

        // Proceed if user clicks OK
        confirmAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                // Show payment dialog
                //showPaymentDialog(total);
                openPaymentWindow();
            }
        });
    }

    private void openPaymentWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Payment.fxml"));

            Stage paymentStage = new Stage();
            // Set modal behavior and owner to the home stage
            paymentStage.initModality(Modality.WINDOW_MODAL);
            paymentStage.initOwner(stage); // 'stage' is HomeController stage

            PaymentController controller = new PaymentController(paymentStage, model, updateTotal(), this);
            loader.setController(controller);

            AnchorPane root = loader.load();
            controller.showStage(root);
            //StageUtils.showStage(paymentStage, root, "Confirm Payment", 300, 200);
        } catch (IOException e) {
            System.out.println("Failed to open payment window: " + e.getMessage());
        }
    }
//    private void processPayment() {
//        // Update sold tickets in DB
//        for (CartItems item : model.getShoppingCart().getItems()) {
//            Event event = item.getEvent();
//            int newSold = event.getSoldTickets() + item.getQuantity();
//            event.setSoldTickets(newSold);
//
//            model.getEventDao().updateSoldTickets(event.getId(), newSold);
//        }
//
//        // Clear cart from DB and memory
//        model.getShoppingCart().clear();
//        model.getshoppingCartDao().clearCart(model.getCurrentUser().getUsername());
//
//        AlertUtils.showInfo("Payment Successful", "Your order has been confirmed.", stage);
//    }
    @FXML
    private void handleBack() {
        System.out.println("Back ShoppingCartController");
        if (stage != null) {
            stage.close();// Close the cart stage only
            homeController.setupDataRow();
            parentStage.show();
        }
    }
}

