package controller;

import dao.EventDao;
import dao.ShoppingCartDao;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.Event;
import model.Model;
import model.User;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import util.AlertUtils;
import util.StageUtils;


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
		//set up the table area
		setupDataRow();
		// click each row
		eventTable.setRowFactory(tv -> {
			TableRow<Event> row = new TableRow<>();
			row.setOnMouseClicked(event -> {
				if (!row.isEmpty() && event.getClickCount() == 1) {
					Event clickedEvent = row.getItem();
					// ✅ Check if tickets are sold out
					if (clickedEvent.getAvailableTickets() == 0) {
						AlertUtils.showInfo("Sold Out", "Sorry, this event is already sold out.", stage);
						return;
					}
					// ✅ Otherwise, open the popup
					openPopupStage(clickedEvent);
				}
			});
			return row;
		});
	}
	public void setupDataRow() {
		try {
			// Set up table column bindings
			eventName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEventName()));
			venue.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getVenue()));
			day.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDay()));
			price.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getPrice()));
			soldTickets.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getSoldTickets()));
			totalTickets.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getTotalTickets()));
			availableTickets.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getAvailableTickets()));

			// Set data into table
			ObservableList<Event> data = FXCollections.observableArrayList(
					model.getEventDao().getUpComingEvents()
			);
			eventTable.setItems(data);


		} catch (SQLException e) {
			System.out.println("Cannot setup data table:" + e.getMessage());
		}

	}
	private void openPopupStage(Event event) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/eventPopup.fxml"));
			// create new stage to handle stage.close popup
			Stage popupStage = new Stage();
			// Set modal behavior and owner to the home stage
			popupStage.initModality(Modality.WINDOW_MODAL);
			popupStage.initOwner(stage); // 'stage' is HomeController stage

			EventPopupController eventPopupController = new EventPopupController(popupStage, event, model);
			loader.setController(eventPopupController);
			GridPane root = loader.load();

			//show scene of PopupModal
			eventPopupController.showStage(root);

		} catch (IOException e) {
			System.out.println("Failed to open booking window: " + e.getMessage());
		}
	}

	public void showStage(Pane root) {
		StageUtils.showStage(stage, root, "Home", 600, 450);
		setupDataRow(); // refresh data when shoeing again
	}

	@FXML
	private void shoppingCartView() throws IOException {
//		if (model.getShoppingCart() == null || model.getShoppingCart().getItems().isEmpty()) {
//			AlertUtils.showInfo("Shopping Cart", "Your shopping cart is empty.", stage);
//			return;
//		}
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ShoppingCart.fxml"));
		ShoppingCartController controller = new ShoppingCartController(stage, model, this); // pass homeController to update data

		loader.setController(controller);
		Pane root = loader.load();
		controller.showStage(root);
		stage.close();

	}
	@FXML
	private void openHistoryView() throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/HistoryView.fxml"));
		OrderHistoryController controller = new OrderHistoryController(stage, model);
		loader.setController(controller);
		Pane root = loader.load();
		controller.showStage(root);
		stage.close();

	}
	@FXML
	private void handleChangePassword() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ChangePasswordPopup.fxml"));

			Stage popup = new Stage();
			popup.initOwner(stage);

			ChangePasswordController controller = new ChangePasswordController(model);
			controller.setStage(popup);
			loader.setController(controller);

			GridPane root = loader.load();

			// Refresh eventsTable after popup closes
			//popup.setOnHiding(event -> refreshEventTable());

			controller.showStage(root);

		} catch (IOException e) {
			AlertUtils.showError("Load Error", "Unable to load Add Event window.", stage);
		}

	}

	@FXML
	private void handleLogout() throws IOException {
		boolean confirm = AlertUtils.showConfirmation("Logout Confirmation",
				"Are you sure you want to logout?",
				stage);

		if (confirm) {
			model.logout(); // clear username amd shoppingCart memory
			parentStage.show(); // comeback to loginView
			stage.close();
		}

	}
}

