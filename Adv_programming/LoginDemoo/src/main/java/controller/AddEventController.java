package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Event;
import model.Model;
import util.AlertUtils;
import util.StageUtils;

import java.io.IOException;

public class AddEventController {
    @FXML private TextField eventNameField;
    @FXML private TextField venueField;
    @FXML private ComboBox<String> dayComboBox;
    @FXML private TextField priceField;
    @FXML private TextField capacityField;
    private Model model;
    private Stage stage;
    public void setStage(Stage dialogStage) {
        this.stage = dialogStage;
    }
    public AddEventController(Model model) {
        this.model = model;
    }
    @FXML
    public void initialize() {
        dayComboBox.getItems().addAll("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun");
    }
//    public void showPopup() {
//        try {
//
//            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AddEvent.fxml"));
//            // Set modal behavior and owner to the home stage
//            stage.initModality(Modality.WINDOW_MODAL);
//            stage.initOwner(parentStage); // 'stage' is HomeController stage
//            loader.setController(this);
//            GridPane root = loader.load();
//
//            //show scene of PopupModal
//            this.showStage(root);
//
//
//        } catch (IOException e) {
//            System.out.println(e.getMessage());
//            AlertUtils.showError("UI Error", "Could not load add event view.", null);
//        }
//    }
    @FXML
    private void handleAddEvent() {
        String name = eventNameField.getText().trim();
        String venue = venueField.getText().trim();
        String day = dayComboBox.getValue();
        String priceText = priceField.getText().trim();
        String capacityText = capacityField.getText().trim();

        if (name.isEmpty() || venue.isEmpty() || day == null || priceText.isEmpty() || capacityText.isEmpty()) {
            AlertUtils.showError("Input Error", "All fields must be filled in.", null);
            return;
        }

        double price;
        int capacity;
        try {
            price = Double.parseDouble(priceText);
            capacity = Integer.parseInt(capacityText);
        } catch (NumberFormatException e) {
            AlertUtils.showError("Format Error", "Price and Capacity must be numeric values.", null);
            return;
        }

        if (model.getEventDao().isDuplicateEvent(name, venue, day)) {
            AlertUtils.showError("Duplicate Event", "An event with the same name, venue, and day already exists.", null);
            return;
        }

        model.getEventDao().insertEvent(name, venue, day, price, capacity);
        AlertUtils.showInfo("Success", "Event added successfully!", null);
        stage.close();
    }

    @FXML
    private void handleCancel() {
        stage.close();
    }
    public void showStage(Pane root) {
        StageUtils.showStage(stage, root, "Add Event", 500, 350);
    }
}
