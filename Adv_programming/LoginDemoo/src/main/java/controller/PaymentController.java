package controller;

import dao.OrderDao;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import model.CartItems;
import model.Event;
import model.Model;
import model.Order;
import util.AlertUtils;
import util.StageUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class PaymentController {
    @FXML private TextField codeField;
    @FXML private Label message;

    private Stage stage;
    private Model model;
    private Runnable onPaymentSuccess;
    //private Stage parentStage;
    private Double totalPrice;
    private ShoppingCartController shoppingCartController;

    public PaymentController(Stage stage, Model model, Double totalPrice, ShoppingCartController shoppingCartController) {
        this.stage = stage;
        this.model = model;
        //this.parentStage = parentStage;
        this.totalPrice = totalPrice;
        this.shoppingCartController = shoppingCartController;
    }

    @FXML
    private void handleConfirm() {
        String code = codeField.getText().trim();
        if (!code.matches("\\d{6}")) {
            message.setText("Please enter a valid 6-digit code.");
            message.setTextFill(Color.RED);
            return;
        }

        // ✅ Payment successful
        stage.close();
        processPayment();

    }
    private void processPayment() {
        AlertUtils.showInfo("Payment Successful", "Your order has been confirmed.", stage);

        //insert to OrderDao DB
        processOrder();
        // Update sold tickets in DB
        for (CartItems item : model.getShoppingCart().getItems()) {
            Event event = item.getEvent();
            int newSold = event.getSoldTickets() + item.getQuantity();
            event.setSoldTickets(newSold);

            model.getEventDao().updateSoldTickets(event.getId(), newSold);
        }

        // Clear cart from DB and memory
        model.getShoppingCart().clear();
        shoppingCartController.refreshCart();
        //model.getShoppingCart().clear();
        //model.getshoppingCartDao().clearCart(model.getCurrentUser().getUsername());

    }
    private void processOrder() {
        String username = model.getCurrentUser().getUsername(); // get username
        OrderDao  orderDao = model.getOrderDao(); // get orderDao
        LocalDateTime orderDate = LocalDateTime.now();
        // Generate 4- digit order number for this order
        String orderNumber = orderDao.generateNextOrderNumberForUser(username);
        // ✅ Clone items to avoid being cleared later
        List<CartItems> cartItems = new ArrayList<>(model.getShoppingCart().getItems());



        // Save Order into OrderDB
        int orderId = orderDao.saveOrder(orderNumber, username, orderDate, totalPrice);
        if (orderId == -1) {
            message.setText("Failed to place order.");
            return;
        }

        //create Order object
        Order order = new Order(orderId, orderNumber, username, orderDate, cartItems, totalPrice);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); // More readable
        String formattedDate = orderDate.format(formatter);

        //save orderItems DB
        orderDao.saveOrderItems(order);
        // ✅ Print confirmation to terminal
        System.out.println("\n✅ New Order Saved:");
        System.out.println("Order ID: " + orderId);
        System.out.println("Order Number: " + orderNumber);
        System.out.println("Username: " + username);
        System.out.println("Order Date: " + formattedDate);
        System.out.println("Total Price: $" + totalPrice);
        System.out.println("Ordered Items:");
        for (CartItems item : cartItems) {
            System.out.printf("  - %s x%d ($%.2f)\n",
                    item.getEvent().getEventName(),
                    item.getQuantity(),
                    item.getEvent().getPrice() * item.getQuantity());
        }

    }

    @FXML
    private void handleCancel() {
        stage.close(); // Close without saving
    }
    public void showStage(Pane root) {
        StageUtils.showStage(stage, root, "Payment", 300, 200);
    }
}