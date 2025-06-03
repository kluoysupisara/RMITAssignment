package controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import model.Event;
import model.GroupedEvent;
import model.Model;
import model.Order;
import util.AlertUtils;
import util.StageUtils;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AdminDashboardController {
    /*
    @FXML private TableView<Event> eventsTable;
    @FXML private TableColumn<Event, String> eventNameColumn;
    @FXML private TableColumn<Event, String> venueDayColumn;
    @FXML private TableColumn<Event, String> priceColumn;
    @FXML private TableColumn<Event, Boolean> enabledColumn;
    @FXML private TableColumn<Event, Void> actionColumn;

     */
    @FXML private TableView<GroupedEvent> eventsTable;
    @FXML private TableColumn<GroupedEvent, String> eventNameColumn;
    @FXML private TableColumn<GroupedEvent, Void> venueDayColumn;
    @FXML private TableColumn<GroupedEvent, String> priceColumn;
    @FXML private TableColumn<GroupedEvent, Boolean> enabledColumn;
    @FXML private TableColumn<GroupedEvent, Void> actionColumn;

    @FXML private TableView<Order> ordersTable;
    @FXML private TableColumn<Order, String> orderNumberColumn;
    @FXML private TableColumn<Order, String> usernameColumn;
    @FXML private TableColumn<Order, String> orderDateColumn;
    @FXML private TableColumn<Order, String> totalColumn;
    @FXML private TableColumn<Order, Void> itemsColumn;

    private Stage stage;
    private Stage parentStage;
    private Model model;

    public AdminDashboardController(Stage parentStage, Model model) {
        this.stage = new Stage();
        this.parentStage = parentStage;
        this.model = model;
    }

    @FXML
    public void initialize() {
        //setupEventTable();
        setupGroupedEventTable();
        setupOrderTable();
    }

    /*

    private void setupEventTable() {
        eventNameColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getEventName()));

        venueDayColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getVenue() + " - " + cellData.getValue().getDay()));

        priceColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.format("$%.2f", cellData.getValue().getPrice())));
        // setup EnabledColumn
        setupEnabledColumn();
        // setup ActionColumn
        setupActionColumn();

        eventsTable.setRowFactory(tv -> new TableRow<>() {
            @Override
            protected void updateItem(Event item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setOpacity(1.0);
                } else {
                    setOpacity(item.getEnabled() ? 1.0 : 0.5);
                }
            }
        });

        try {
            List<Event> events = model.getEventDao().getAllEvents();
            eventsTable.setItems(FXCollections.observableArrayList(events));
        } catch (Exception e) {
            AlertUtils.showError("Database Error", "Failed to load events.", stage);
        }
    }

     */

    private void setupGroupedEventTable() {
        eventNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEventName()));

        venueDayColumn.setCellFactory(col -> new TableCell<GroupedEvent, Void>() {
            private final ComboBox<Event> comboBox = new ComboBox<>();
            {

                comboBox.setCellFactory(listView -> new ListCell<>() {
                    @Override
                    protected void updateItem(Event item, boolean empty) {
                        super.updateItem(item, empty);
                        setText((empty || item == null) ? null : item.getVenue() + " - " + item.getDay());
                    }
                });
                comboBox.setButtonCell(new ListCell<>() {
                    @Override
                    protected void updateItem(Event item, boolean empty) {
                        super.updateItem(item, empty);
                        setText((empty || item == null) ? null : item.getVenue() + " - " + item.getDay());
                    }
                });
                comboBox.setOnAction(e -> {
                    GroupedEvent grouped = getTableView().getItems().get(getIndex());
                    Event selectedEvent = comboBox.getSelectionModel().getSelectedItem();
                    grouped.setSelectedEvent(selectedEvent);
                    getTableView().refresh();
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getIndex() >= getTableView().getItems().size()) {
                    setGraphic(null);
                } else {
                    GroupedEvent grouped = getTableView().getItems().get(getIndex());
                    comboBox.setItems(FXCollections.observableArrayList(grouped.getEvent()));

                    Event toSelect = grouped.getSelectedEvent();
                    if (toSelect == null && !grouped.getEvent().isEmpty()) {
                        toSelect = grouped.getEvent().get(0);
                        grouped.setSelectedEvent(toSelect);
                    }

                    comboBox.getSelectionModel().select(toSelect);
                    setGraphic(comboBox);
                }
            }
        });
        priceColumn.setCellValueFactory(cellData -> {
            Event selected = cellData.getValue().getSelectedEvent();
            if (selected != null) {
                return new SimpleStringProperty(String.format("$%.2f", selected.getPrice()));
            } else {
                return new SimpleStringProperty("N/A");
            }
        });

//        priceColumn.setCellValueFactory(cellData -> new SimpleStringProperty(
//                String.format("$%.2f", cellData.getValue().getSelectedEvent().getPrice()))
//       );

        setupEnabledColumn();
        setupActionColumn();

        eventsTable.setRowFactory(tv -> new TableRow<>() {
            @Override
            protected void updateItem(GroupedEvent item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty || item.getSelectedEvent() == null) {
                    setOpacity(1.0);
                } else {
                    setOpacity(item.getSelectedEvent().getEnabled() ? 1.0 : 0.5);
                }
            }
        });

        try {
            List<Event> allEvents = model.getEventDao().getAllEvents();
            List<GroupedEvent> grouped = groupEvents(allEvents);
            eventsTable.setItems(FXCollections.observableArrayList(grouped));
        } catch (Exception e) {
            AlertUtils.showError("Database Error", "Failed to load events.", stage);
        }
    }
    private List<GroupedEvent> groupEvents(List<Event> allEvents) {
        Map<String, List<Event>> groupedMap = allEvents.stream()
                .collect(Collectors.groupingBy(Event::getEventName));

        return groupedMap.entrySet().stream()
                .map(entry -> new GroupedEvent(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }


    private void setupOrderTable() {
        orderNumberColumn.setCellValueFactory(data -> javafx.beans.binding.Bindings.createStringBinding(
                data.getValue()::getOrderNumber));
        usernameColumn.setCellValueFactory(data -> javafx.beans.binding.Bindings.createStringBinding(
                data.getValue()::getUsername));
        orderDateColumn.setCellValueFactory(data -> javafx.beans.binding.Bindings.createStringBinding(
                () -> data.getValue().getOrderDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))));
        totalColumn.setCellValueFactory(data -> javafx.beans.binding.Bindings.createStringBinding(
                () -> String.format("$%.2f", data.getValue().getOrderPrice())));

        refreshOrderData();
    }

    private void refreshOrderData() {
        try {
            List<Order> orders = model.getOrderDao().getAllOrders();
            ordersTable.setItems(FXCollections.observableArrayList(orders));
        } catch (Exception e) {
            AlertUtils.showError("Database Error", "Failed to load orders.",stage);
        }
    }

    @FXML
    private void handleAddEvent() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AddEvent.fxml"));

            Stage popup = new Stage();
            popup.initOwner(stage);

            AddEventController controller = new AddEventController(model);
            controller.setStage(popup);
            loader.setController(controller);

            GridPane root = loader.load();

            // Refresh eventsTable after popup closes
            popup.setOnHiding(event -> refreshEventTable());

            controller.showStage(root);

        } catch (IOException e) {
            AlertUtils.showError("Load Error", "Unable to load Add Event window.", stage);
        }

    }


    @FXML
    private void handleRefreshOrders() {
        refreshOrderData();
    }

    @FXML
    private void handleExportOrders() {
        AlertUtils.showInfo("Export Orders", "This will export all orders to a file.", stage);
    }
    private void setupEnabledColumn() {
        enabledColumn.setCellFactory(column -> new TableCell<>() {
            private final CheckBox checkBox = new CheckBox();

            {
                checkBox.setOnAction(event -> {
                    GroupedEvent grouped = getTableView().getItems().get(getIndex());
                    Event selectedEvent = grouped.getSelectedEvent();
                    if (selectedEvent != null) {
                        boolean newValue = checkBox.isSelected();
                        selectedEvent.setEnabled(newValue);
                        model.getEventDao().setEventEnabled(selectedEvent.getId(), newValue);
                        getTableView().refresh(); // update opacity
                    }
                });
            }

            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getIndex() >= getTableView().getItems().size()) {
                    setGraphic(null);
                } else {
                    GroupedEvent grouped = getTableView().getItems().get(getIndex());
                    Event selectedEvent = grouped.getSelectedEvent();

                    if (selectedEvent != null) {
                        checkBox.setSelected(selectedEvent.getEnabled());
                        checkBox.setDisable(false);
                    } else {
                        checkBox.setSelected(false);
                        checkBox.setDisable(true);  // disable checkbox if no selection
                    }

                    setGraphic(checkBox);
                    setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                    setStyle("-fx-alignment: CENTER;");
                }
            }
        });
    }

    private void setupActionColumn() {
        actionColumn.setCellFactory(col -> new TableCell<>() {
            private final Button editButton = new Button("Edit");
            private final Button deleteButton = new Button("Delete");

            {
                editButton.setOnAction(event -> {
                    GroupedEvent grouped = getTableView().getItems().get(getIndex());
                    Event selectedEvent = grouped.getSelectedEvent();
                    if (selectedEvent != null) {
                        EditEventController controller = new EditEventController(selectedEvent, model);
                        controller.showPopup();
                        getTableView().refresh();
                    }
                });

                deleteButton.setOnAction(e -> {
                    GroupedEvent grouped = getTableView().getItems().get(getIndex());
                    Event selectedEvent = grouped.getSelectedEvent();
                    if (selectedEvent != null) {
                        boolean confirm = AlertUtils.showConfirmation("Delete", "Are you sure you want to delete this event?", stage);
                        if (confirm) {
                            model.getEventDao().deleteEvent(selectedEvent.getId());
                            grouped.getEvent().remove(selectedEvent);

                            if (grouped.getEvent().isEmpty()) {
                                getTableView().getItems().remove(grouped);
                            } else {
                                grouped.setSelectedEvent(grouped.getEvent().get(0));
                                getTableView().refresh();
                            }
                        }
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getIndex() >= getTableView().getItems().size()) {
                    setGraphic(null);
                } else {
                    GroupedEvent grouped = getTableView().getItems().get(getIndex());
                    Event selectedEvent = grouped.getSelectedEvent();

                    boolean disable = (selectedEvent == null || !selectedEvent.getEnabled());
                    editButton.setOpacity(1);
                    deleteButton.setOpacity(1);
                    //editButton.setDisable(disable);
                    //deleteButton.setDisable(disable);

                    HBox hbox = new HBox(10, editButton, deleteButton);
                    hbox.setStyle("-fx-alignment: center");
                    setGraphic(hbox);
                }
            }
        });
    }
    /*
    private void setupEnabledColumn() {
        enabledColumn.setCellFactory(column -> new TableCell<>() {
            private final CheckBox checkBox = new CheckBox();

            {
                checkBox.setOnAction(event -> {
                    Event eventItem = getTableView().getItems().get(getIndex());
                    boolean newValue = checkBox.isSelected();
                    eventItem.setEnabled(newValue);
                    model.getEventDao().setEventEnabled(eventItem.getId(), newValue);
                    getTableView().refresh(); // ✅ triggers opacity update
                });
            }

            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getIndex() >= getTableView().getItems().size()) {
                    setGraphic(null);
                } else {
                    Event eventItem = getTableView().getItems().get(getIndex());
                    checkBox.setSelected(eventItem.getEnabled());

                    // 👇 Center-align the checkbox
                    setGraphic(checkBox);
                    setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                    setStyle("-fx-alignment: CENTER;");
                }
            }
        });
    }
    private void setupActionColumn() {
        actionColumn.setCellFactory(col -> new TableCell<>() {
            private final Button editButton = new Button("Edit");
            private final Button deleteButton = new Button("Delete");

            {
                editButton.setOnAction(event -> {
                    Event selectedEvent = getTableView().getItems().get(getIndex());
                    EditEventController controller = new EditEventController(selectedEvent, model);
                    controller.showPopup();
                    getTableView().refresh();  // Refresh after editing
                });
                deleteButton.setOnAction(e -> {
                    Event event = getTableView().getItems().get(getIndex());
                    boolean confirm = AlertUtils.showConfirmation("Delete", "Are you sure you want to delete this event?", stage);
                    if (confirm) {
                        model.getEventDao().deleteEvent(event.getId());
                        getTableView().getItems().remove(event);
                    }
                });
                editButton.setStyle("-fx-cursor: hand;");
                deleteButton.setStyle("-fx-cursor: hand;");
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getIndex() >= getTableView().getItems().size()) {
                    setGraphic(null);
                } else {
                    // Disable buttons based on event enabled status
                    Event event = getTableView().getItems().get(getIndex());
                    boolean isDisabled = !event.getEnabled();
                    editButton.setDisable(isDisabled);
                    deleteButton.setDisable(isDisabled);

                    HBox hbox = new HBox(10, editButton, deleteButton);
                    hbox.setStyle("-fx-alignment: center");
                    setGraphic(hbox);
                }
            }
        });

    }
     */
    @FXML
    private void handleLogout() {
        model.logout();
        parentStage.show();
        stage.close();
    }

    public void showStage(Pane root) {
        StageUtils.showStage(stage, root, "Admin Dashboard", 800, 600);
    }
    private void refreshEventTable() {
        try {
            List<Event> allEvents = model.getEventDao().getAllEvents();
            List<GroupedEvent> grouped = groupEvents(allEvents);
            eventsTable.setItems(FXCollections.observableArrayList(grouped));
        } catch (Exception e) {
            AlertUtils.showError("Database Error", "Failed to reload events.", stage);
        }
    }
}
