package controller;

import javafx.stage.Stage;
import model.Event;
import model.Model;

public class BookingController {
    private Model model;
    private Stage stage;
    private Event event;

    public BookingController(Event event, Model model) {
        this.event = event;
        this.model = model;
    }
}
