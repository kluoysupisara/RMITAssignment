package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import model.Event;
import model.Model; // optional if you use shared model for cart
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
        System.out.println("quantity: " + quantity);

        // check qty exceed available or not
        if (available < quantity) {
            message.setText("There are no enough seats available.");
            message.setTextFill(Color.RED);
            return; // prevent closing popupEvent
         }
        System.out.println("Added to cart: " + event.getEventName() + " x" + quantity);

        // Optional: Add to shared cart
//        if (model != null) {
//            model.getCart().add(event, quantity);
//        }
//        stage.close();
    }
    @FXML
    public void handleCancel() {
        System.out.println("Popup cancelled");
        stage.close();
    }

    public void showStage(Pane root) {
        StageUtils.showStage(stage, root, "Add Cart", 500, 250);
    }

}
