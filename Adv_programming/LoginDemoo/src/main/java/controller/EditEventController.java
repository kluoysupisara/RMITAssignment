package controller;

import model.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Model;
import util.AlertUtils;

import java.io.IOException;

public class EditEventController {
    @FXML
    private TextField eventNameTextField;
    @FXML private TextField venueTextField;
    @FXML private ComboBox<String> dayComboBox;
    @FXML private TextField priceTextField;

    private final Stage dialogStage = new Stage();
    private Event event;
    private Model model;

    public EditEventController(Event event, Model model) {
        this.event = event;
        this.model = model;
    }

    public void showPopup() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/EditEvent.fxml"));
            loader.setController(this);
            Scene scene = new Scene(loader.load());
            dialogStage.setTitle("Edit Event");
            dialogStage.setScene(scene);
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.setResizable(false);

            populateFields();
            dialogStage.showAndWait();
        } catch (IOException e) {
            AlertUtils.showError("UI Error", "Could not load edit event view.", null);
        }
    }

    private void populateFields() {
        eventNameTextField.setText(event.getEventName());
        venueTextField.setText(event.getVenue());
        priceTextField.setText(String.valueOf(event.getPrice()));
        dayComboBox.getItems().addAll("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun");
        dayComboBox.setValue(event.getDay());
    }

    @FXML
    private void handleSave() {
        String name = eventNameTextField.getText().trim();
        String venue = venueTextField.getText().trim();
        String day = dayComboBox.getValue();
        double price;

        try {
            price = Double.parseDouble(priceTextField.getText().trim());
        } catch (NumberFormatException e) {
            AlertUtils.showError("Invalid Price", "Please enter a valid number for price.", null);
            return;
        }

        // Check for duplicates (excluding this event's ID)
        boolean isDuplicate = model.getEventDao().isDuplicateEvent(name, venue, day);
        if (isDuplicate) {
            AlertUtils.showError("Duplicate Event", "An event with the same name, venue, and day already exists.", null);
            return;
        }

        // ✅ Proceed with update
        event.setEventName(name);
        event.setVenue(venue);
        event.setDay(day);
        event.setPrice(price);

        model.getEventDao().updateEvent(event);
        dialogStage.close();
    }

    @FXML
    private void handelcancel() {
        dialogStage.close();
    }
}
