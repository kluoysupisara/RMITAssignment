package controller;

import dao.EventDao;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import model.Event;
import model.Model;
import model.User;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;


import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class HomeController {
	private Model model;
	private Stage stage;
	private Stage parentStage;
	@FXML
	private MenuItem viewProfile; // Corresponds to the Menu item "viewProfile" in HomeView.fxml
	@FXML
	private MenuItem updateProfile; // // Corresponds to the Menu item "updateProfile" in HomeView.fxml
	@FXML
	private Label welcomelabel;
	@FXML private TableView<Event> eventTable;
	@FXML private TableColumn<Event, String> eventName, venue, day;
	@FXML private TableColumn<Event, Number> price, soldTickets, totalTickets, availableTickets;
	public HomeController(Stage parentStage, Model model) {
		this.stage = new Stage();
		this.parentStage = parentStage;
		this.model = model;
	}
	
	// Add your code to complete the functionality of the program
	@FXML
	public void initialize() throws SQLException {
		// Access the user through the model
		User currentUser = model.getCurrentUser();
		welcomelabel.setText("Welcome! , " + currentUser.getPreferred_name());

//		//set value into the table
//		EventDao eventDao = new EventDao();
//		List<Event> events = eventDao.getUpComingEvents(); // Call your method
//		System.out.println("Upcoming Events: " + events.size());
//
//		for (Event e : events) {
//			System.out.println(
//					"Event: " + e.getEventName() +
//							", Venue: " + e.getVenue() +
//							", Day: " + e.getDay() +
//							", Price: $" + e.getPrice() +
//							", Sold: " + e.getSoldTickets() +
//							", Total: " + e.getTotalTickets() +
//							", Available: " + e.getAvailableTickets()
//			);
//		}

			// Set up table column bindings
			eventName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEventName()));
			venue.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getVenue()));
			day.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDay()));
			price.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getPrice()));
			soldTickets.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getSoldTickets()));
			totalTickets.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getTotalTickets()));
			availableTickets.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getAvailableTickets()));

			// Set data into table
			EventDao eventDao = new EventDao();
			ObservableList<Event> data = FXCollections.observableArrayList(eventDao.getUpComingEvents());
			eventTable.setItems(data);


	}
	
	
	
	public void showStage(Pane root) {
		Scene scene = new Scene(root, 1000, 500);
		stage.setScene(scene);
		stage.setResizable(false);
		stage.setTitle("Home");
		stage.show();
	}
}
