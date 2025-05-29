package controller;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import model.Event;
import model.Model;



public class BookingController {
    private Model model;
    private Stage stage;
    private Event event;
    private Stage parentStage;

    //@FXML private Label eventName;

    public BookingController(Event event, Stage parentStage, Model model) {
        this.event = event;
        this.parentStage = parentStage;
        this.model = model;
        this.stage = new Stage();
    }
    public void showStage(Pane root) {
        Scene scene = new Scene(root, 500, 300);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("Booking");
        stage.show();
    }

    @FXML
    public void initialize(){
        System.out.println("inside booking controller");
        //eventName.setText(event.getEventName());
    }
}
