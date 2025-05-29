package controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import model.CartItems;
import model.Event;
import model.Model; // optional if you use shared model for cart
import model.ShoppingCart;
import util.StageUtils;

public class EventPopupController {

    @FXML private Label eventNameLabel;
    @FXML private Label venueLabel;
    @FXML private Label dayLabel;
    @FXML private Label priceLabel;
    @FXML private Label availableTicketsLabel;
    @FXML private Spinner<Integer> quantitySpinner;
    @FXML private Button addToCartButton;
    @FXML private Button cancelButton;
    @FXML private Label message;

    private Event event;
    private Stage stage;
    private Model model; // optional if using shared cart

    public EventPopupController(Stage stage, Event event, Model model) {
        this.stage = stage;
        this.model = model;
        this.event = event;
    }

    @FXML
    private void initialize() {
        System.out.println("EventPopupController initialized");
        setEvent(event);
        addToCartButton.setOnAction(e -> handleAddToCart());
        cancelButton.setOnAction(e -> {handleCancel();});
    }

    public void setEvent(Event event) {
        eventNameLabel.setText(event.getEventName());
        venueLabel.setText(event.getVenue());
        dayLabel.setText(event.getDay());
        priceLabel.setText(String.format("$%.2f", event.getPrice()));
        //availableTicketsLabel.setText(String.valueOf(event.getAvailableTickets()));
        int max = event.getAvailableTickets();
        System.out.println("max: " + max);
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, Integer.MAX_VALUE, 1);
        quantitySpinner.setValueFactory(valueFactory);
    }

    @FXML
    public void handleAddToCart() {
        int quantity = quantitySpinner.getValue();
        int available = event.getAvailableTickets();

        // check qty exceed available or not
        if (available < quantity) {
            message.setText("There are no enough seats available.");
            message.setTextFill(Color.RED);
            return; // prevent closing popupEvent
         }

        //Optional: Add to shared cart
        // ✅ Add or update cart in memory
        ShoppingCart shoppingCart = model.getShoppingCart(); // model holds the current user's cart

        if(validateAddtoCart(shoppingCart, quantity)) {
            shoppingCart.addItem(event, quantity);
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Successfully added to cart.");
            message.setText("Successfully added to cart.");
            message.setTextFill(Color.GREEN);
            //stage.close();
        }else{
            message.setText("There are not enough tickets available");
            message.setTextFill(Color.RED);
        }

        System.out.println("Current Shopping Cart:");
        for (CartItems item : shoppingCart.getItems()) {
            System.out.printf(" - %s x%d\n", item.getEvent().getEventName(), item.getQuantity());
        }

    }

    private boolean validateAddtoCart(ShoppingCart shoppingCart,int quantity) {
        for (CartItems item : shoppingCart.getItems()) {
            if (item.getEvent().getId() == event.getId()) {
                int currentQty = item.getQuantity();
                int newQty = currentQty + quantity;

                // ✅ Limit check: prevent exceeding available tickets
                if (newQty > event.getAvailableTickets()) {
                    System.out.printf("Cannot add %d more. Only %d available.%n",
                            quantity, event.getAvailableTickets() - currentQty);
                    return false;
                }
                return true;
            }
        }
        // ✅ New item, check limit first
        return quantity <= event.getAvailableTickets();
    }
        //stage.close();
    @FXML
    public void handleCancel() {
        System.out.println("Popup cancelled");
        stage.close();
    }

    public void showStage(Pane root) {
        StageUtils.showStage(stage, root, "Add Cart", 500, 250);
    }

}
