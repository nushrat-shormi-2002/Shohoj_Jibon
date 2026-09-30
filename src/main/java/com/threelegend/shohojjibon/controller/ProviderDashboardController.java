package com.threelegend.shohojjibon.controller;

import com.threelegend.shohojjibon.dao.BookingDAO;
import com.threelegend.shohojjibon.dao.BusDAO;
import com.threelegend.shohojjibon.dao.BusStopDAO;
import com.threelegend.shohojjibon.model.Booking;
import com.threelegend.shohojjibon.model.Bus;
import com.threelegend.shohojjibon.model.BusStop;
import com.threelegend.shohojjibon.model.ServiceProvider;
import com.threelegend.shohojjibon.util.SessionManager;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

/**
 * Provider Dashboard Controller - Full Featured
 * Shohoj Jibon - Makes your life easier
 * Developed by Three_Legend
 */
public class ProviderDashboardController {
    
    // FXML Components - Overview Tab
    @FXML private Label welcomeLabel;
    @FXML private Label authoritiesLabel;
    @FXML private Label totalBusesLabel;
    @FXML private Label totalBookingsLabel;
    @FXML private Label totalRevenueLabel;
    
    // FXML Components - Bus Management Tab
    @FXML private TableView<Bus> busTable;
    @FXML private TableColumn<Bus, String> busNameCol;
    @FXML private TableColumn<Bus, String> busNumberCol;
    @FXML private TableColumn<Bus, String> routeCol;
    @FXML private TableColumn<Bus, String> fareCol;
    @FXML private TableColumn<Bus, String> seatsCol;
    @FXML private TableColumn<Bus, String> scheduleCol;
    @FXML private TableColumn<Bus, String> busTypeCol;
    @FXML private TableColumn<Bus, String> statusCol;
    @FXML private TableColumn<Bus, Void> actionsCol;
    
    // FXML Components - Bookings Tab
    @FXML private TableView<Booking> bookingTable;
    @FXML private TableColumn<Booking, String> bookingRefCol;
    @FXML private TableColumn<Booking, String> serviceTypeCol;
    @FXML private TableColumn<Booking, String> bookingDateCol;
    @FXML private TableColumn<Booking, String> quantityCol;
    @FXML private TableColumn<Booking, String> amountCol;
    @FXML private TableColumn<Booking, String> bookingStatusCol;
    @FXML private TableColumn<Booking, String> paymentStatusCol;
    @FXML private TableColumn<Booking, Void> bookingActionsCol;
    
    @FXML private Button logoutButton;
    @FXML private TabPane mainTabPane;
    
    // DAOs
    private final BusDAO busDAO = new BusDAO();
    private final BookingDAO bookingDAO = new BookingDAO();
    private final BusStopDAO busStopDAO = new BusStopDAO();
    
    // Data
    private ObservableList<Bus> busList = FXCollections.observableArrayList();
    private ObservableList<Booking> bookingList = FXCollections.observableArrayList();
    private ServiceProvider currentProvider;
    
    @FXML
    public void initialize() {
        currentProvider = SessionManager.getInstance().getCurrentProvider();
        
        // Set welcome message
        welcomeLabel.setText("Welcome, " + currentProvider.getProviderName() + "!");
        authoritiesLabel.setText("Authorities: " + currentProvider.getAuthorityType());
        
        // Setup tables
        setupBusTable();
        setupBookingTable();
        
        // Load data
        loadAllData();
    }
    
    /**
     * Setup Bus Table
     */
    private void setupBusTable() {
        busNameCol.setCellValueFactory(data -> data.getValue().busNameProperty());
        busNumberCol.setCellValueFactory(data -> data.getValue().busNumberProperty());
        routeCol.setCellValueFactory(data -> {
            Bus bus = data.getValue();
            List<BusStop> stops = busStopDAO.getStopsByBusId(bus.getId());
            if (stops.isEmpty()) {
                return new SimpleStringProperty(bus.getRoute());
            } else {
                // Show first 3 stops and last stop
                StringBuilder route = new StringBuilder();
                int count = Math.min(3, stops.size());
                for (int i = 0; i < count; i++) {
                    route.append(stops.get(i).getStopName());
                    if (i < count - 1) route.append(" ▸ ");
                }
                if (stops.size() > 3) {
                    route.append(" ... ▸ ").append(stops.get(stops.size() - 1).getStopName());
                    route.append(" (").append(stops.size()).append(" stops)");
                }
                return new SimpleStringProperty(route.toString());
            }
        });
        fareCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getFormattedFare()));
        seatsCol.setCellValueFactory(data -> new SimpleStringProperty(
            data.getValue().getAvailableSeats() + "/" + data.getValue().getTotalSeats()
        ));
        scheduleCol.setCellValueFactory(data -> new SimpleStringProperty(
            data.getValue().getScheduleTime() != null ? 
            data.getValue().getScheduleTime().format(DateTimeFormatter.ofPattern("hh:mm a")) : "N/A"
        ));
        busTypeCol.setCellValueFactory(data -> data.getValue().busTypeProperty());
        statusCol.setCellValueFactory(data -> new SimpleStringProperty(
            data.getValue().isActive() ? "✓ Active" : "✗ Inactive"
        ));
        
        // Actions column
        actionsCol.setCellFactory(param -> new TableCell<>() {
            private final Button viewStopsBtn = new Button("🚏");
            private final Button editBtn = new Button("✏️");
            private final Button deleteBtn = new Button("🗑️");
            private final HBox buttons = new HBox(5, viewStopsBtn, editBtn, deleteBtn);
            
            {
                viewStopsBtn.setStyle("-fx-background-color: #10b981; -fx-text-fill: white; -fx-cursor: hand; -fx-padding: 5 10;");
                editBtn.setStyle("-fx-background-color: #3b82f6; -fx-text-fill: white; -fx-cursor: hand; -fx-padding: 5 10;");
                deleteBtn.setStyle("-fx-background-color: #ef4444; -fx-text-fill: white; -fx-cursor: hand; -fx-padding: 5 10;");
                buttons.setAlignment(Pos.CENTER);
                
                viewStopsBtn.setOnAction(event -> {
                    Bus bus = getTableView().getItems().get(getIndex());
                    handleViewStops(bus);
                });
                
                editBtn.setOnAction(event -> {
                    Bus bus = getTableView().getItems().get(getIndex());
                    handleEditBus(bus);
                });
                
                deleteBtn.setOnAction(event -> {
                    Bus bus = getTableView().getItems().get(getIndex());
                    handleDeleteBus(bus);
                });
            }
            
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : buttons);
            }
        });
        
        busTable.setItems(busList);
    }
    
    /**
     * Setup Booking Table
     */
    private void setupBookingTable() {
        bookingRefCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getBookingReference()));
        serviceTypeCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getServiceType()));
        bookingDateCol.setCellValueFactory(data -> new SimpleStringProperty(
            data.getValue().getBookingDate() != null ? data.getValue().getBookingDate().toString() : "N/A"
        ));
        quantityCol.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getQuantity())));
        amountCol.setCellValueFactory(data -> new SimpleStringProperty(
            String.format("৳%.2f", data.getValue().getTotalAmount())
        ));
        bookingStatusCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getStatus()));
        paymentStatusCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getPaymentStatus()));
        
        // Actions column
        bookingActionsCol.setCellFactory(param -> new TableCell<>() {
            private final Button viewBtn = new Button("👁️ View");
            private final HBox buttons = new HBox(5, viewBtn);
            
            {
                viewBtn.setStyle("-fx-background-color: #3b82f6; -fx-text-fill: white; -fx-cursor: hand; -fx-padding: 5 10;");
                buttons.setAlignment(Pos.CENTER);
                
                viewBtn.setOnAction(event -> {
                    Booking booking = getTableView().getItems().get(getIndex());
                    handleViewBooking(booking);
                });
            }
            
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : buttons);
            }
        });
        
        bookingTable.setItems(bookingList);
    }
    
    /**
     * Load all data
     */
    private void loadAllData() {
        loadBuses();
        loadBookings();
        updateStatistics();
    }
    
    /**
     * Load buses
     */
    private void loadBuses() {
        busList.clear();
        List<Bus> buses = busDAO.getBusesByProvider(currentProvider.getId());
        busList.addAll(buses);
    }
    
    /**
     * Load bookings
     */
    private void loadBookings() {
        bookingList.clear();
        List<Booking> bookings = bookingDAO.getBookingsByProviderId(currentProvider.getId());
        bookingList.addAll(bookings);
    }
    
    /**
     * Update statistics
     */
    private void updateStatistics() {
        totalBusesLabel.setText(String.valueOf(busList.size()));
        totalBookingsLabel.setText(String.valueOf(bookingList.size()));
        
        double totalRevenue = bookingList.stream()
            .filter(b -> !"CANCELLED".equals(b.getStatus()))
            .mapToDouble(Booking::getTotalAmount)
            .sum();
        totalRevenueLabel.setText(String.format("৳%.2f", totalRevenue));
    }
    
    /**
     * Show Add Bus Dialog
     */
    @FXML
    private void handleShowAddBusDialog() {
        showBusDialog(null);
    }
    
    /**
     * Edit Bus
     */
    private void handleEditBus(Bus bus) {
        showBusDialog(bus);
    }
    
    /**
     * Show Simplified Bus Dialog with Stop Management  
     * ONLY: Bus Name, Bus Number, Start Point, End Point, Schedule Time (12-hour), and Stops
     */
    private void showBusDialog(Bus existingBus) {
        boolean isEdit = existingBus != null;
        
        // Create custom stage for better control
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setTitle(isEdit ? "Edit Bus" : "Add New Local Bus");
        dialogStage.setWidth(900);
        dialogStage.setHeight(700);
        
        // Main container
        VBox mainContainer = new VBox(15);
        mainContainer.setPadding(new Insets(20));
        mainContainer.setStyle("-fx-background-color: white;");
        
        // Title
        Label titleLabel = new Label(isEdit ? "✏️ Edit Bus with Stops" : "🚌 Add New Local Bus with Stops");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #10b981;");
        
        // === BASIC INFO SECTION ===
        VBox basicInfoSection = new VBox(10);
        basicInfoSection.setStyle("-fx-background-color: #f8f9fa; -fx-padding: 15; -fx-background-radius: 8;");
        
        Label basicInfoTitle = new Label("📝 Basic Information");
        basicInfoTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #333;");
        
        GridPane basicGrid = new GridPane();
        basicGrid.setHgap(15);
        basicGrid.setVgap(10);
        
        TextField busNameField = new TextField();
        busNameField.setPromptText("e.g., Mirpur to Motijheel Express");
        busNameField.setPrefWidth(300);
        
        TextField busNumberField = new TextField();
        busNumberField.setPromptText("e.g., ML-001");
        
        TextField startPointField = new TextField();
        startPointField.setPromptText("e.g., Mirpur 10");
        
        TextField endPointField = new TextField();
        endPointField.setPromptText("e.g., Motijheel");
        
        TextField scheduleTimeField = new TextField();
        scheduleTimeField.setPromptText("e.g., 06:30 AM or 2:30 PM");
        
        // Pre-populate fields if editing
        if (isEdit) {
            busNameField.setText(existingBus.getBusName());
            busNumberField.setText(existingBus.getBusNumber());
            startPointField.setText(existingBus.getStartPoint());
            endPointField.setText(existingBus.getEndPoint());
            
            // Format time to 12-hour format
            DateTimeFormatter formatter12 = DateTimeFormatter.ofPattern("h:mm a");
            scheduleTimeField.setText(existingBus.getScheduleTime().format(formatter12));
        }
        
        basicGrid.add(new Label("Bus Name:"), 0, 0);
        basicGrid.add(busNameField, 1, 0);
        basicGrid.add(new Label("Bus Number:"), 2, 0);
        basicGrid.add(busNumberField, 3, 0);
        
        basicGrid.add(new Label("Start Point:"), 0, 1);
        basicGrid.add(startPointField, 1, 1);
        basicGrid.add(new Label("End Point:"), 2, 1);
        basicGrid.add(endPointField, 3, 1);
        
        basicGrid.add(new Label("Schedule Time:"), 0, 2);
        basicGrid.add(scheduleTimeField, 1, 2);
        Label timeFormatLabel = new Label("(12-hour format: HH:MM AM/PM)");
        timeFormatLabel.setStyle("-fx-text-fill: #666; -fx-font-size: 11px;");
        basicGrid.add(timeFormatLabel, 2, 2, 2, 1);
        
        basicInfoSection.getChildren().addAll(basicInfoTitle, basicGrid);
        
        // === STOPS SECTION ===
        VBox stopsSection = new VBox(10);
        stopsSection.setStyle("-fx-background-color: #f8f9fa; -fx-padding: 15; -fx-background-radius: 8;");
        VBox.setVgrow(stopsSection, Priority.ALWAYS);
        
        HBox stopsHeader = new HBox(15);
        stopsHeader.setAlignment(Pos.CENTER_LEFT);
        
        Label stopsTitle = new Label("🚏 Bus Stops (In Order)");
        stopsTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #333;");
        HBox.setHgrow(stopsTitle, Priority.ALWAYS);
        
        Button addStopBtn = new Button("➕ Add Stop");
        addStopBtn.setStyle("-fx-background-color: #10b981; -fx-text-fill: white; -fx-font-weight: bold; -fx-cursor: hand;");
        
        Button moveUpBtn = new Button("⬆️ Move Up");
        moveUpBtn.setStyle("-fx-background-color: #3b82f6; -fx-text-fill: white; -fx-cursor: hand;");
        
        Button moveDownBtn = new Button("⬇️ Move Down");
        moveDownBtn.setStyle("-fx-background-color: #3b82f6; -fx-text-fill: white; -fx-cursor: hand;");
        
        Button deleteStopBtn = new Button("🗑️ Delete");
        deleteStopBtn.setStyle("-fx-background-color: #ef4444; -fx-text-fill: white; -fx-cursor: hand;");
        
        stopsHeader.getChildren().addAll(stopsTitle, addStopBtn, moveUpBtn, moveDownBtn, deleteStopBtn);
        
        // Stops Table
        TableView<BusStop> stopsTable = new TableView<>();
        stopsTable.setPlaceholder(new Label("No stops added yet. Click '➕ Add Stop' to begin."));
        VBox.setVgrow(stopsTable, Priority.ALWAYS);
        
        TableColumn<BusStop, Number> orderCol = new TableColumn<>("#");
        orderCol.setCellValueFactory(data -> data.getValue().stopOrderProperty());
        orderCol.setPrefWidth(50);
        
        TableColumn<BusStop, String> stopNameCol = new TableColumn<>("Stop Name");
        stopNameCol.setCellValueFactory(data -> data.getValue().stopNameProperty());
        stopNameCol.setPrefWidth(180);
        
        TableColumn<BusStop, Number> timeCol = new TableColumn<>("Time (min)");
        timeCol.setCellValueFactory(data -> data.getValue().arrivalTimeOffsetProperty());
        timeCol.setPrefWidth(90);
        
        TableColumn<BusStop, Number> fareCol = new TableColumn<>("Fare (৳)");
        fareCol.setCellValueFactory(data -> data.getValue().fareFromStartProperty());
        fareCol.setPrefWidth(90);
        
        TableColumn<BusStop, String> typeCol = new TableColumn<>("Type");
        typeCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getStopType()));
        typeCol.setPrefWidth(100);
        
        TableColumn<BusStop, String> landmarkCol = new TableColumn<>("Landmark");
        landmarkCol.setCellValueFactory(data -> data.getValue().landmarkProperty());
        landmarkCol.setPrefWidth(200);
        
        stopsTable.getColumns().addAll(orderCol, stopNameCol, timeCol, fareCol, typeCol, landmarkCol);
        
        ObservableList<BusStop> tempStops = FXCollections.observableArrayList();
        stopsTable.setItems(tempStops);
        
        // Load existing stops if editing
        if (isEdit) {
            List<BusStop> existingStops = busStopDAO.getStopsByBusId(existingBus.getId());
            tempStops.addAll(existingStops);
        }
        
        // Summary Label
        Label summaryLabel = new Label("Total Stops: 0 | Total Fare: ৳0.00");
        summaryLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #10b981;");
        
        stopsSection.getChildren().addAll(stopsHeader, stopsTable, summaryLabel);
        
        // Update summary method
        Runnable updateSummary = () -> {
            double totalFare = tempStops.isEmpty() ? 0 : tempStops.get(tempStops.size() - 1).getFareFromStart();
            summaryLabel.setText(String.format("Total Stops: %d | Total Fare: ৳%.2f", tempStops.size(), totalFare));
        };
        
        // Update summary after loading existing stops
        updateSummary.run();
        
        // === ADD STOP DIALOG ===
        addStopBtn.setOnAction(e -> {
            Dialog<BusStop> stopDialog = new Dialog<>();
            stopDialog.setTitle("Add Bus Stop");
            stopDialog.setHeaderText("Enter stop details");
            
            ButtonType addButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
            stopDialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);
            
            GridPane stopGrid = new GridPane();
            stopGrid.setHgap(10);
            stopGrid.setVgap(10);
            stopGrid.setPadding(new Insets(20));
            
            TextField stopNameFld = new TextField();
            stopNameFld.setPromptText("e.g., Farmgate");
            TextField timeFld = new TextField();
            timeFld.setPromptText("e.g., 15");
            TextField fareFld = new TextField();
            fareFld.setPromptText("e.g., 25");
            TextField landmarkFld = new TextField();
            landmarkFld.setPromptText("e.g., Farmgate Railgate");
            
            CheckBox pickupCheck = new CheckBox("Pickup Point");
            pickupCheck.setSelected(true);
            CheckBox dropCheck = new CheckBox("Drop Point");
            dropCheck.setSelected(true);
            
            // Auto-suggest next values
            if (!tempStops.isEmpty()) {
                BusStop lastStop = tempStops.get(tempStops.size() - 1);
                timeFld.setText(String.valueOf(lastStop.getArrivalTimeOffset() + 10));
                fareFld.setText(String.valueOf((int)(lastStop.getFareFromStart() + 10)));
            } else {
                timeFld.setText("0");
                fareFld.setText("0");
                pickupCheck.setSelected(true);
                dropCheck.setSelected(false);
            }
            
            stopGrid.add(new Label("Stop Name *:"), 0, 0);
            stopGrid.add(stopNameFld, 1, 0);
            stopGrid.add(new Label("Time from start (min) *:"), 0, 1);
            stopGrid.add(timeFld, 1, 1);
            stopGrid.add(new Label("Fare from start (৳) *:"), 0, 2);
            stopGrid.add(fareFld, 1, 2);
            stopGrid.add(new Label("Landmark:"), 0, 3);
            stopGrid.add(landmarkFld, 1, 3);
            stopGrid.add(pickupCheck, 0, 4);
            stopGrid.add(dropCheck, 1, 4);
            
            stopDialog.getDialogPane().setContent(stopGrid);
            
            stopDialog.setResultConverter(dialogButton -> {
                if (dialogButton == addButtonType) {
                    try {
                        BusStop newStop = new BusStop();
                        newStop.setStopOrder(tempStops.size() + 1);
                        newStop.setStopName(stopNameFld.getText().trim());
                        newStop.setArrivalTimeOffset(Integer.parseInt(timeFld.getText().trim()));
                        newStop.setFareFromStart(Double.parseDouble(fareFld.getText().trim()));
                        newStop.setLandmark(landmarkFld.getText().trim());
                        newStop.setPickupPoint(pickupCheck.isSelected());
                        newStop.setDropPoint(dropCheck.isSelected());
                        return newStop;
                    } catch (Exception ex) {
                        showError("Invalid input. Please check all fields.");
                        return null;
                    }
                }
                return null;
            });
            
            stopDialog.showAndWait().ifPresent(stop -> {
                tempStops.add(stop);
                updateSummary.run();
            });
        });
        
        // Move Up
        moveUpBtn.setOnAction(e -> {
            int selectedIndex = stopsTable.getSelectionModel().getSelectedIndex();
            if (selectedIndex > 0) {
                BusStop stop = tempStops.remove(selectedIndex);
                tempStops.add(selectedIndex - 1, stop);
                // Reorder
                for (int i = 0; i < tempStops.size(); i++) {
                    tempStops.get(i).setStopOrder(i + 1);
                }
                stopsTable.getSelectionModel().select(selectedIndex - 1);
            }
        });
        
        // Move Down
        moveDownBtn.setOnAction(e -> {
            int selectedIndex = stopsTable.getSelectionModel().getSelectedIndex();
            if (selectedIndex >= 0 && selectedIndex < tempStops.size() - 1) {
                BusStop stop = tempStops.remove(selectedIndex);
                tempStops.add(selectedIndex + 1, stop);
                // Reorder
                for (int i = 0; i < tempStops.size(); i++) {
                    tempStops.get(i).setStopOrder(i + 1);
                }
                stopsTable.getSelectionModel().select(selectedIndex + 1);
            }
        });
        
        // Delete Stop
        deleteStopBtn.setOnAction(e -> {
            BusStop selected = stopsTable.getSelectionModel().getSelectedItem();
            if (selected != null) {
                tempStops.remove(selected);
                // Reorder
                for (int i = 0; i < tempStops.size(); i++) {
                    tempStops.get(i).setStopOrder(i + 1);
                }
                updateSummary.run();
            }
        });
        
        // === BUTTONS ===
        HBox buttonBox = new HBox(15);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);
        
        Button saveButton = new Button(isEdit ? "💾 Update Bus" : "💾 Save Bus");
        saveButton.setStyle("-fx-background-color: #10b981; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 10 30; -fx-cursor: hand;");
        
        Button cancelButton = new Button("✖️ Cancel");
        cancelButton.setStyle("-fx-background-color: #6b7280; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10 30; -fx-cursor: hand;");
        
        buttonBox.getChildren().addAll(cancelButton, saveButton);
        
        // Save Button Action
        saveButton.setOnAction(e -> {
            try {
                // Validate basic info
                if (busNameField.getText().trim().isEmpty() || 
                    busNumberField.getText().trim().isEmpty() ||
                    startPointField.getText().trim().isEmpty() ||
                    endPointField.getText().trim().isEmpty() ||
                    scheduleTimeField.getText().trim().isEmpty()) {
                    showError("Please fill in all required fields!");
                    return;
                }
                
                if (tempStops.isEmpty()) {
                    showError("Please add at least one stop!");
                    return;
                }
                
                // Parse time (12-hour format)
                String timeStr = scheduleTimeField.getText().trim().toUpperCase();
                LocalTime scheduleTime;
                try {
                    DateTimeFormatter formatter12 = DateTimeFormatter.ofPattern("h:mm a");
                    scheduleTime = LocalTime.parse(timeStr, formatter12);
                } catch (Exception ex) {
                    showError("Invalid time format! Use: HH:MM AM/PM (e.g., 06:30 AM or 2:30 PM)");
                    return;
                }
                
                if (isEdit) {
                    // UPDATE MODE
                    existingBus.setBusName(busNameField.getText().trim());
                    existingBus.setBusNumber(busNumberField.getText().trim());
                    existingBus.setStartPoint(startPointField.getText().trim());
                    existingBus.setEndPoint(endPointField.getText().trim());
                    existingBus.setScheduleTime(scheduleTime);
                    
                    // Update bus
                    if (busDAO.updateBus(existingBus)) {
                        // Delete all existing stops
                        busStopDAO.deleteAllStopsForBus(existingBus.getId());
                        
                        // Save new stops
                        for (BusStop stop : tempStops) {
                            stop.setBusId(existingBus.getId());
                            busStopDAO.createBusStop(stop);
                        }
                        
                        // Update bus fare and distance based on last stop
                        if (!tempStops.isEmpty()) {
                            BusStop lastStop = tempStops.get(tempStops.size() - 1);
                            busDAO.updateBusFareAndDistance(existingBus.getId(), lastStop.getFareFromStart(), 0);
                        }
                        
                        showInfo("Bus updated successfully with " + tempStops.size() + " stops!");
                        loadAllData();
                        dialogStage.close();
                    } else {
                        showError("Failed to update bus. Please try again.");
                    }
                } else {
                    // CREATE MODE
                    Bus newBus = new Bus();
                    newBus.setProviderId(currentProvider.getId());
                    newBus.setBusName(busNameField.getText().trim());
                    newBus.setBusNumber(busNumberField.getText().trim());
                    newBus.setStartPoint(startPointField.getText().trim());
                    newBus.setEndPoint(endPointField.getText().trim());
                    newBus.setScheduleTime(scheduleTime);
                    
                    // Save bus
                    if (busDAO.createBus(newBus)) {
                        // Save all stops
                        for (BusStop stop : tempStops) {
                            stop.setBusId(newBus.getId());
                            busStopDAO.createBusStop(stop);
                        }
                        
                        // Update bus fare and distance based on last stop
                        if (!tempStops.isEmpty()) {
                            BusStop lastStop = tempStops.get(tempStops.size() - 1);
                            busDAO.updateBusFareAndDistance(newBus.getId(), lastStop.getFareFromStart(), 0);
                        }
                        
                        showInfo("Bus added successfully with " + tempStops.size() + " stops!");
                        loadAllData();
                        dialogStage.close();
                    } else {
                        showError("Failed to create bus. Please try again.");
                    }
                }
            } catch (Exception ex) {
                showError("Error: " + ex.getMessage());
                ex.printStackTrace();
            }
        });
        
        cancelButton.setOnAction(e -> dialogStage.close());
        
        // Add all sections to main container
        mainContainer.getChildren().addAll(titleLabel, basicInfoSection, stopsSection, buttonBox);
        
        // Create scene and show
        Scene scene = new Scene(mainContainer);
        dialogStage.setScene(scene);
        dialogStage.showAndWait();
    }
    
    /**
     * Delete Bus
     */
    private void handleDeleteBus(Bus bus) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Bus");
        alert.setHeaderText("Delete " + bus.getBusName() + "?");
        alert.setContentText("This action cannot be undone. All stops will also be deleted.");
        
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            if (busDAO.deleteBus(bus.getId())) {
                showInfo("Bus deleted successfully!");
                loadAllData();
            } else {
                showError("Failed to delete bus.");
            }
        }
    }
    
    /**
     * View Bus Stops
     */
    private void handleViewStops(Bus bus) {
        List<BusStop> stops = busStopDAO.getStopsByBusId(bus.getId());
        
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Bus Stops - " + bus.getBusName());
        alert.setHeaderText(bus.getBusNumber() + ": " + bus.getRoute());
        
        if (stops.isEmpty()) {
            alert.setContentText("No stops defined for this bus.\n\nThis is a direct route from " + 
                               bus.getStartPoint() + " to " + bus.getEndPoint() + ".");
        } else {
            StringBuilder content = new StringBuilder();
            content.append(String.format("Total Stops: %d\n", stops.size()));
            content.append(String.format("Total Distance: %.2f km\n", bus.getDistance()));
            content.append(String.format("Total Fare: %s\n\n", bus.getFormattedFare()));
            content.append("Route Details:\n");
            content.append("─".repeat(50)).append("\n\n");
            
            for (BusStop stop : stops) {
                content.append(String.format("%d. %s\n", stop.getStopOrder(), stop.getStopName()));
                content.append(String.format("   ├─ Fare from start: %s\n", stop.getFormattedFare()));
                content.append(String.format("   ├─ Time from start: %s\n", stop.getArrivalTimeFormatted()));
                content.append(String.format("   ├─ Type: %s\n", stop.getStopType()));
                if (stop.getLandmark() != null && !stop.getLandmark().isEmpty()) {
                    content.append(String.format("   └─ Landmark: %s\n", stop.getLandmark()));
                }
                content.append("\n");
            }
            
            // Create a scrollable TextArea for better viewing
            TextArea textArea = new TextArea(content.toString());
            textArea.setEditable(false);
            textArea.setWrapText(true);
            textArea.setPrefWidth(580);
            textArea.setPrefHeight(400);
            textArea.setStyle("-fx-font-family: 'Consolas', 'Courier New', monospace; -fx-font-size: 13px;");
            
            alert.getDialogPane().setContent(textArea);
        }
        
        alert.setResizable(true);
        alert.getDialogPane().setPrefWidth(600);
        alert.getDialogPane().setPrefHeight(500);
        alert.showAndWait();
    }
    
    /**
     * View Booking Details
     */
    private void handleViewBooking(Booking booking) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Booking Details");
        alert.setHeaderText("Booking Reference: " + booking.getBookingReference());
        
        StringBuilder content = new StringBuilder();
        content.append("Service Type: ").append(booking.getServiceType()).append("\n");
        content.append("Service ID: ").append(booking.getServiceId()).append("\n");
        content.append("Booking Date: ").append(booking.getBookingDate()).append("\n");
        content.append("Quantity: ").append(booking.getQuantity()).append("\n");
        content.append("Total Amount: ৳").append(String.format("%.2f", booking.getTotalAmount())).append("\n");
        content.append("Status: ").append(booking.getStatus()).append("\n");
        content.append("Payment Status: ").append(booking.getPaymentStatus()).append("\n");
        if (booking.getPaymentMethod() != null) {
            content.append("Payment Method: ").append(booking.getPaymentMethod()).append("\n");
        }
        if (booking.getNotes() != null && !booking.getNotes().isEmpty()) {
            content.append("Notes: ").append(booking.getNotes()).append("\n");
        }
        
        alert.setContentText(content.toString());
        alert.showAndWait();
    }
    
    /**
     * Refresh Data
     */
    @FXML
    private void handleRefreshData() {
        loadAllData();
        showInfo("Data refreshed successfully!");
    }
    
    @FXML
    private void handleRefreshBuses() {
        loadBuses();
        updateStatistics();
        showInfo("Bus list refreshed!");
    }
    
    @FXML
    private void handleRefreshBookings() {
        loadBookings();
        updateStatistics();
        showInfo("Bookings refreshed!");
    }
    
    /**
     * Logout
     */
    @FXML
    private void handleLogout(ActionEvent event) {
        SessionManager.getInstance().clearSession();
        loadLoginView();
    }
    
    /**
     * Load Login View
     */
    private void loadLoginView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/threelegend/shohojjibon/login.fxml"));
            Scene scene = new Scene(loader.load(), 900, 600);
            
            Stage stage = (Stage) logoutButton.getScene().getWindow();
            stage.setTitle("Shohoj Jibon - Login");
            stage.setScene(scene);
            stage.setMaximized(false);
        } catch (Exception e) {
            System.err.println("✗ Failed to load login view: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Show Info Alert
     */
    private void showInfo(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    /**
     * Show Error Alert
     */
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
