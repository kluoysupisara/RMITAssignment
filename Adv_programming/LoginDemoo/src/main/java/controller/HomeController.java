package controller;

import dao.EventDao;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;
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
	@FXML private TableColumn<Event, Void> actionColumn;

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
			addButtonToTable();

//			eventTable.setRowFactory(tv -> {
//				TableRow<Event> row = new TableRow<>();
//				row.setOnMouseClicked(event -> {
//					if (!row.isEmpty() && event.getClickCount() == 2) {
//						Event clickedEvent = row.getItem();
//						openBookingStage(clickedEvent);
//					}
//				});
//				return row;
//			});



	}
//	private void openBookingStage(Event event) {
//		try {
//			FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/BookingView.fxml"));
//			BookingController controller = new BookingController(event, model);
//			loader.setController(controller);
//			VBox root = loader.load();
//
//			Stage bookingStage = new Stage();
//			bookingStage.setTitle("Book Tickets");
//			bookingStage.setScene(new Scene(root));
//			bookingStage.show();
//
//		} catch (IOException e) {
//			e.printStackTrace();
//			Alert alert = new Alert(Alert.AlertType.ERROR);
//			alert.setContentText("Failed to open booking window.");
//			alert.showAndWait();
//		}
//	}

	private void addButtonToTable() {
		Callback<TableColumn<Event, Void>, TableCell<Event, Void>> cellFactory = new Callback<>() {
			@Override
			public TableCell<Event, Void> call(final TableColumn<Event, Void> param) {
				return new TableCell<>() {

					private final Button btn = new Button("Book");

					{
						btn.setOnAction((ActionEvent event) -> {
							Event selectedEvent = getTableView().getItems().get(getIndex());
							openBookingStage(selectedEvent);
						});
						btn.setStyle("-fx-background-color: #2a9df4; -fx-text-fill: white;");
					}

					@Override
					public void updateItem(Void item, boolean empty) {
						super.updateItem(item, empty);
						if (empty) {
							setGraphic(null);
						} else {
							setGraphic(btn);
						}
					}
				};
			}
		};

		actionColumn.setCellFactory(cellFactory);
	}





	public void showStage(Pane root) {
		Scene scene = new Scene(root, 1000, 500);
		stage.setScene(scene);
		stage.setResizable(false);
		stage.setTitle("Home");
		stage.show();
	}
}
