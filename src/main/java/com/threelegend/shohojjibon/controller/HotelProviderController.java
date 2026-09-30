package com.threelegend.shohojjibon.controller;

import com.threelegend.shohojjibon.dao.HotelDAO;
import com.threelegend.shohojjibon.dao.BookingDAO;
import com.threelegend.shohojjibon.dao.UserDAO;
import com.threelegend.shohojjibon.model.Hotel;
import com.threelegend.shohojjibon.model.Booking;
import com.threelegend.shohojjibon.model.User;
import com.threelegend.shohojjibon.model.ServiceProvider;
import com.threelegend.shohojjibon.util.SessionManager;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.time.LocalTime;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Hotel Provider Dashboard Controller
 * Manages hotel rooms and accommodations
 * Shohoj Jibon - Makes your life easier
 * Developed by Three_Legend
 */
public class HotelProviderController {
    
    // FXML Components - Overview
    @FXML private Label welcomeLabel;
    @FXML private Label hotelInfoLabel;
    @FXML private Label totalRoomsLabel;
    @FXML private Label availableRoomsLabel;
    @FXML private Label totalRevenueLabel;
    
    // FXML Components - Rooms
    @FXML private TableView<Hotel> roomTable;
    @FXML private TableColumn<Hotel, String> roomTypeCol;
    @FXML private TableColumn<Hotel, String> priceCol;
    @FXML private TableColumn<Hotel, String> roomsCol;
    @FXML private TableColumn<Hotel, String> availableCol;
    @FXML private TableColumn<Hotel, String> sizeCol;
    @FXML private TableColumn<Hotel, String> occupancyCol;
    @FXML private TableColumn<Hotel, String> amenitiesCol;
    @FXML private TableColumn<Hotel, String> statusCol;
    @FXML private TableColumn<Hotel, Void> actionsCol;
    
    // FXML Components - Bookings
    @FXML private TableView<Booking> bookingTable;
    @FXML private TableColumn<Booking, String> bookingIdCol;
    @FXML private TableColumn<Booking, String> guestNameCol;
    @FXML private TableColumn<Booking, String> bookingRoomTypeCol;
    @FXML private TableColumn<Booking, String> checkInDateCol;
    @FXML private TableColumn<Booking, String> checkOutDateCol;
    @FXML private TableColumn<Booking, String> guestsCol;
    @FXML private TableColumn<Booking, String> totalAmountCol;
    @FXML private TableColumn<Booking, String> bookingStatusCol;
    @FXML private TableColumn<Booking, Void> bookingActionsCol;
    
    // FXML Components - Booking Statistics
    @FXML private Label totalBookingsLabel;
    @FXML private Label pendingBookingsLabel;
    @FXML private Label confirmedBookingsLabel;
    @FXML private Label bookingRevenueLabel;
    
    // Data
    private ObservableList<Hotel> roomList = FXCollections.observableArrayList();
    private ObservableList<Booking> bookingList = FXCollections.observableArrayList();
    private ServiceProvider currentProvider;
    
    // DAOs
    private final HotelDAO hotelDAO = new HotelDAO();
    private final BookingDAO bookingDAO = new BookingDAO();
    private final UserDAO userDAO = new UserDAO();
    
    @FXML
    public void initialize() {
        currentProvider = SessionManager.getInstance().getCurrentProvider();
        
        // Set welcome message
        welcomeLabel.setText("Welcome, " + currentProvider.getProviderName() + "!");
        hotelInfoLabel.setText("Address: " + currentProvider.getAddress() + " | Authority: " + currentProvider.getAuthorityType());
        
        // Setup tables
        setupRoomTable();
        setupBookingTable();
        
        // Load data
        loadAllData();
    }
    
    /**
     * Setup Room Table
     */
    private void setupRoomTable() {
        roomTypeCol.setCellValueFactory(data -> data.getValue().roomTypeProperty());
        priceCol.setCellValueFactory(data -> 
            new SimpleStringProperty(data.getValue().getFormattedPrice()));
        roomsCol.setCellValueFactory(data -> 
            data.getValue().totalRoomsProperty().asString());
        availableCol.setCellValueFactory(data -> 
            data.getValue().availableRoomsProperty().asString());
        sizeCol.setCellValueFactory(data -> data.getValue().roomSizeProperty());
        occupancyCol.setCellValueFactory(data -> 
            new SimpleStringProperty(data.getValue().getMaxOccupancy() + " guests"));
        amenitiesCol.setCellValueFactory(data -> data.getValue().amenitiesProperty());
        statusCol.setCellValueFactory(data -> 
            new SimpleStringProperty(data.getValue().isAvailable() ? "✓ Active" : "✗ Inactive"));
        
        // Actions column
        actionsCol.setCellFactory(param -> new TableCell<>() {
            private final Button editBtn = new Button("📝");
            private final Button deleteBtn = new Button("🗑️");
            private final Button toggleBtn = new Button("🔄");
            private final HBox buttons = new HBox(5, editBtn, toggleBtn, deleteBtn);
            
            {
                editBtn.setStyle("-fx-background-color: #3b82f6; -fx-text-fill: white; -fx-cursor: hand; -fx-padding: 5 10;");
                toggleBtn.setStyle("-fx-background-color: #f59e0b; -fx-text-fill: white; -fx-cursor: hand; -fx-padding: 5 10;");
                deleteBtn.setStyle("-fx-background-color: #ef4444; -fx-text-fill: white; -fx-cursor: hand; -fx-padding: 5 10;");
                buttons.setAlignment(Pos.CENTER);
                
                editBtn.setOnAction(e -> {
                    Hotel room = getTableView().getItems().get(getIndex());
                    handleEditRoom(room);
                });
                
                toggleBtn.setOnAction(e -> {
                    Hotel room = getTableView().getItems().get(getIndex());
                    handleToggleRoomStatus(room);
                });
                
                deleteBtn.setOnAction(e -> {
                    Hotel room = getTableView().getItems().get(getIndex());
                    handleDeleteRoom(room);
                });
            }
            
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : buttons);
            }
        });
        
        roomTable.setItems(roomList);
    }
    
    /**
     * Setup Booking Table
     */
    private void setupBookingTable() {
        bookingIdCol.setCellValueFactory(data -> data.getValue().idProperty().asString());
        guestNameCol.setCellValueFactory(data -> {
            try {
                User user = userDAO.findById(data.getValue().getUserId());
                return new SimpleStringProperty(user != null ? user.getFullName() : "Unknown");
            } catch (Exception e) {
                return new SimpleStringProperty("Unknown");
            }
        });
        bookingRoomTypeCol.setCellValueFactory(data -> {
            try {
                Hotel hotel = hotelDAO.getHotelById(data.getValue().getServiceId());
                return new SimpleStringProperty(hotel != null ? hotel.getRoomType() : "Unknown");
            } catch (Exception e) {
                return new SimpleStringProperty("Unknown");
            }
        });
        checkInDateCol.setCellValueFactory(data -> {
            LocalDateTime serviceDate = data.getValue().getServiceDate();
            if (serviceDate != null) {
                return new SimpleStringProperty(serviceDate.toLocalDate().toString());
            } else {
                return new SimpleStringProperty("N/A");
            }
        });
        checkOutDateCol.setCellValueFactory(data -> {
            LocalDateTime endDate = data.getValue().getServiceDate();
            if (endDate != null) {
                // For hotels, we'll use service_date + 1 day as checkout
                return new SimpleStringProperty(endDate.toLocalDate().plusDays(1).toString());
            } else {
                return new SimpleStringProperty("N/A");
            }
        });
        guestsCol.setCellValueFactory(data -> 
            new SimpleStringProperty(String.valueOf(data.getValue().getNumberOfPeople())));
        totalAmountCol.setCellValueFactory(data -> 
            new SimpleStringProperty(String.format("৳%.2f", data.getValue().getTotalAmount())));
        bookingStatusCol.setCellValueFactory(data -> data.getValue().statusProperty());
        
        // Actions column
        bookingActionsCol.setCellFactory(param -> new TableCell<>() {
            private final Button confirmBtn = new Button("✓ Confirm");
            private final Button cancelBtn = new Button("✗ Cancel");
            private final Button completeBtn = new Button("✓ Complete");
            private final Button viewBtn = new Button("👁️ View");
            private final HBox buttons = new HBox(5, confirmBtn, cancelBtn, completeBtn, viewBtn);
            
            {
                confirmBtn.setStyle("-fx-background-color: #10b981; -fx-text-fill: white; -fx-cursor: hand; -fx-padding: 5 10;");
                cancelBtn.setStyle("-fx-background-color: #ef4444; -fx-text-fill: white; -fx-cursor: hand; -fx-padding: 5 10;");
                completeBtn.setStyle("-fx-background-color: #3b82f6; -fx-text-fill: white; -fx-cursor: hand; -fx-padding: 5 10;");
                viewBtn.setStyle("-fx-background-color: #8b5cf6; -fx-text-fill: white; -fx-cursor: hand; -fx-padding: 5 10;");
                buttons.setAlignment(Pos.CENTER);
                
                confirmBtn.setOnAction(e -> {
                    Booking booking = getTableView().getItems().get(getIndex());
                    handleConfirmBooking(booking);
                });
                
                cancelBtn.setOnAction(e -> {
                    Booking booking = getTableView().getItems().get(getIndex());
                    handleCancelBooking(booking);
                });
                
                completeBtn.setOnAction(e -> {
                    Booking booking = getTableView().getItems().get(getIndex());
                    handleCompleteBooking(booking);
                });
                
                viewBtn.setOnAction(e -> {
                    Booking booking = getTableView().getItems().get(getIndex());
                    showBookingDetails(booking);
                });
            }
            
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    Booking booking = getTableView().getItems().get(getIndex());
                    String status = booking.getStatus();
                    
                    // Show appropriate buttons based on status
                    confirmBtn.setVisible("PENDING".equals(status));
                    cancelBtn.setVisible(!"CANCELLED".equals(status) && !"COMPLETED".equals(status));
                    completeBtn.setVisible("CONFIRMED".equals(status));
                    viewBtn.setVisible(true);
                    
                    setGraphic(buttons);
                }
            }
        });
        
        bookingTable.setItems(bookingList);
    }
    
    /**
     * Load all data
     */
    private void loadAllData() {
        loadRooms();
        loadBookings();
        updateStatistics();
    }
    
    private void loadRooms() {
        roomList.clear();
        List<Hotel> rooms = hotelDAO.getHotelsByProvider(currentProvider.getId());
        roomList.addAll(rooms);
    }
    
    /**
     * Load bookings for this hotel
     */
    private void loadBookings() {
        bookingList.clear();
        List<Booking> bookings = bookingDAO.getBookingsByProviderId(currentProvider.getId());
        bookingList.addAll(bookings);
        updateBookingStatistics();
    }
    
    /**
     * Update booking statistics
     */
    private void updateBookingStatistics() {
        if (totalBookingsLabel != null) {
            totalBookingsLabel.setText(String.valueOf(bookingList.size()));
        }
        
        long pendingCount = bookingList.stream()
            .filter(b -> "PENDING".equals(b.getStatus()))
            .count();
        if (pendingBookingsLabel != null) {
            pendingBookingsLabel.setText(String.valueOf(pendingCount));
        }
        
        long confirmedCount = bookingList.stream()
            .filter(b -> "CONFIRMED".equals(b.getStatus()))
            .count();
        if (confirmedBookingsLabel != null) {
            confirmedBookingsLabel.setText(String.valueOf(confirmedCount));
        }
        
        double totalRevenue = bookingList.stream()
            .filter(b -> !"CANCELLED".equals(b.getStatus()))
            .mapToDouble(Booking::getTotalAmount)
            .sum();
        if (bookingRevenueLabel != null) {
            bookingRevenueLabel.setText(String.format("৳%.2f", totalRevenue));
        }
    }
    
    private void updateStatistics() {
        totalRoomsLabel.setText(String.valueOf(roomList.size()));
        
        int totalAvailable = roomList.stream()
            .mapToInt(Hotel::getAvailableRooms)
            .sum();
        availableRoomsLabel.setText(String.valueOf(totalAvailable));
        
        // Revenue from bookings
        double totalRevenue = bookingList.stream()
            .filter(b -> !"CANCELLED".equals(b.getStatus()))
            .mapToDouble(Booking::getTotalAmount)
            .sum();
        totalRevenueLabel.setText(String.format("৳%.2f", totalRevenue));
    }
    
    /**
     * Handle Refresh Bookings
     */
    @FXML
    private void handleRefreshBookings() {
        loadBookings();
        showInfo("Bookings refreshed!");
    }
    
    /**
     * Handle Confirm Booking
     */
    private void handleConfirmBooking(Booking booking) {
        if (bookingDAO.updateBookingStatus(booking.getId(), "CONFIRMED")) {
            showInfo("Booking confirmed successfully!");
            loadBookings();
        } else {
            showError("Failed to confirm booking");
        }
    }
    
    /**
     * Handle Cancel Booking
     */
    private void handleCancelBooking(Booking booking) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Cancel Booking");
        alert.setHeaderText("Cancel booking #" + booking.getId() + "?");
        alert.setContentText("This action cannot be undone.");
        
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                if (bookingDAO.updateBookingStatus(booking.getId(), "CANCELLED")) {
                    showInfo("Booking cancelled successfully!");
                    loadBookings();
                } else {
                    showError("Failed to cancel booking");
                }
            }
        });
    }
    
    /**
     * Handle Complete Booking
     */
    private void handleCompleteBooking(Booking booking) {
        if (bookingDAO.updateBookingStatus(booking.getId(), "COMPLETED")) {
            showInfo("Booking marked as completed!");
            loadBookings();
        } else {
            showError("Failed to complete booking");
        }
    }
    
    /**
     * Show Booking Details
     */
    private void showBookingDetails(Booking booking) {
        try {
            User user = userDAO.findById(booking.getUserId());
            Hotel hotel = hotelDAO.getHotelById(booking.getServiceId());
            
            StringBuilder details = new StringBuilder();
            details.append("Booking ID: #").append(booking.getId()).append("\n");
            details.append("Guest Name: ").append(user != null ? user.getFullName() : "Unknown").append("\n");
            details.append("Room Type: ").append(hotel != null ? hotel.getRoomType() : "Unknown").append("\n");
            details.append("Check-in Date: ").append(booking.getServiceDate() != null ? booking.getServiceDate().toLocalDate() : "N/A").append("\n");
            details.append("Number of Guests: ").append(booking.getNumberOfPeople()).append("\n");
            details.append("Total Amount: ").append(String.format("৳%.2f", booking.getTotalAmount())).append("\n");
            details.append("Status: ").append(booking.getStatus()).append("\n");
            details.append("Payment Status: ").append(booking.getPaymentStatus()).append("\n");
            
            if (booking.getSpecialRequests() != null && !booking.getSpecialRequests().isEmpty()) {
                details.append("\nSpecial Requests:\n").append(booking.getSpecialRequests());
            }
            
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Booking Details");
            alert.setHeaderText("Booking #" + booking.getId());
            alert.setContentText(details.toString());
            alert.setResizable(true);
            alert.getDialogPane().setPrefWidth(500);
            alert.showAndWait();
        } catch (Exception e) {
            showError("Failed to load booking details: " + e.getMessage());
        }
    }
    
    /**
     * Handle Add Room
     */
    @FXML
    private void handleAddRoom() {
        showRoomDialog(null);
    }
    
    private void handleEditRoom(Hotel room) {
        showRoomDialog(room);
    }
    
    private void showRoomDialog(Hotel existingRoom) {
        boolean isEdit = existingRoom != null;
        
        Dialog<Hotel> dialog = new Dialog<>();
        dialog.setTitle(isEdit ? "Edit Room Type" : "Add New Room Type");
        dialog.setHeaderText(isEdit ? "Update room information" : "Enter room details");
        
        ButtonType saveButtonType = new ButtonType(isEdit ? "Update" : "Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);
        
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));
        
        // Fields
        TextField roomTypeField = new TextField();
        roomTypeField.setPromptText("e.g., Deluxe Suite");
        
        TextField priceField = new TextField();
        priceField.setPromptText("Price per night");
        
        TextField totalRoomsField = new TextField();
        totalRoomsField.setPromptText("Total rooms");
        
        TextField availableRoomsField = new TextField();
        availableRoomsField.setPromptText("Available rooms");
        
        TextField roomSizeField = new TextField();
        roomSizeField.setPromptText("e.g., 400 sq ft");
        
        TextField maxOccupancyField = new TextField();
        maxOccupancyField.setPromptText("Max guests");
        
        TextArea amenitiesField = new TextArea();
        amenitiesField.setPromptText("e.g., AC, WiFi, TV, Mini Bar");
        amenitiesField.setPrefRowCount(3);
        
        TextField checkInField = new TextField();
        checkInField.setPromptText("e.g., 2:00 PM");
        
        TextField checkOutField = new TextField();
        checkOutField.setPromptText("e.g., 12:00 PM");
        
        TextField contactField = new TextField();
        contactField.setPromptText("Contact number");
        
        TextField emailField = new TextField();
        emailField.setPromptText("Email");
        
        TextArea policyField = new TextArea();
        policyField.setPromptText("Cancellation policy");
        policyField.setPrefRowCount(2);
        
        // Pre-fill if editing
        if (isEdit) {
            roomTypeField.setText(existingRoom.getRoomType());
            priceField.setText(String.valueOf(existingRoom.getPricePerNight()));
            totalRoomsField.setText(String.valueOf(existingRoom.getTotalRooms()));
            availableRoomsField.setText(String.valueOf(existingRoom.getAvailableRooms()));
            roomSizeField.setText(existingRoom.getRoomSize());
            maxOccupancyField.setText(String.valueOf(existingRoom.getMaxOccupancy()));
            amenitiesField.setText(existingRoom.getAmenities());
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("h:mm a");
            checkInField.setText(existingRoom.getCheckInTime().format(formatter));
            checkOutField.setText(existingRoom.getCheckOutTime().format(formatter));
            contactField.setText(existingRoom.getContactNumber());
            emailField.setText(existingRoom.getEmail());
            policyField.setText(existingRoom.getCancellationPolicy());
        }
        
        grid.add(new Label("Room Type *:"), 0, 0);
        grid.add(roomTypeField, 1, 0);
        grid.add(new Label("Price/Night *:"), 0, 1);
        grid.add(priceField, 1, 1);
        grid.add(new Label("Total Rooms *:"), 0, 2);
        grid.add(totalRoomsField, 1, 2);
        grid.add(new Label("Available Rooms *:"), 0, 3);
        grid.add(availableRoomsField, 1, 3);
        grid.add(new Label("Room Size:"), 0, 4);
        grid.add(roomSizeField, 1, 4);
        grid.add(new Label("Max Guests *:"), 0, 5);
        grid.add(maxOccupancyField, 1, 5);
        grid.add(new Label("Amenities:"), 0, 6);
        grid.add(amenitiesField, 1, 6);
        grid.add(new Label("Check-in Time:"), 0, 7);
        grid.add(checkInField, 1, 7);
        grid.add(new Label("Check-out Time:"), 0, 8);
        grid.add(checkOutField, 1, 8);
        grid.add(new Label("Contact:"), 0, 9);
        grid.add(contactField, 1, 9);
        grid.add(new Label("Email:"), 0, 10);
        grid.add(emailField, 1, 10);
        grid.add(new Label("Cancellation Policy:"), 0, 11);
        grid.add(policyField, 1, 11);
        
        dialog.getDialogPane().setContent(grid);
        
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                try {
                    Hotel room = isEdit ? existingRoom : new Hotel();
                    room.setProviderId(currentProvider.getId());
                    room.setHotelName(currentProvider.getProviderName());
                    room.setLocation(currentProvider.getAddress());
                    room.setAddress(currentProvider.getAddress());
                    room.setRoomType(roomTypeField.getText().trim());
                    room.setPricePerNight(Double.parseDouble(priceField.getText().trim()));
                    room.setTotalRooms(Integer.parseInt(totalRoomsField.getText().trim()));
                    room.setAvailableRooms(Integer.parseInt(availableRoomsField.getText().trim()));
                    room.setRoomSize(roomSizeField.getText().trim());
                    room.setMaxOccupancy(Integer.parseInt(maxOccupancyField.getText().trim()));
                    room.setAmenities(amenitiesField.getText().trim());
                    
                    // Parse times
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("h:mm a");
                    room.setCheckInTime(LocalTime.parse(checkInField.getText().trim().toUpperCase(), formatter));
                    room.setCheckOutTime(LocalTime.parse(checkOutField.getText().trim().toUpperCase(), formatter));
                    
                    room.setContactNumber(contactField.getText().trim());
                    room.setEmail(emailField.getText().trim());
                    room.setCancellationPolicy(policyField.getText().trim());
                    
                    return room;
                } catch (Exception e) {
                    showError("Invalid input. Please check all fields.");
                    return null;
                }
            }
            return null;
        });
        
        dialog.showAndWait().ifPresent(room -> {
            if (isEdit) {
                if (hotelDAO.updateHotel(room)) {
                    showInfo("Room type updated successfully!");
                    loadAllData();
                } else {
                    showError("Failed to update room type.");
                }
            } else {
                if (hotelDAO.createHotel(room)) {
                    showInfo("Room type added successfully!");
                    loadAllData();
                } else {
                    showError("Failed to add room type.");
                }
            }
        });
    }
    
    private void handleToggleRoomStatus(Hotel room) {
        room.setAvailable(!room.isAvailable());
        if (hotelDAO.updateHotel(room)) {
            showInfo("Room status updated!");
            loadAllData();
        } else {
            showError("Failed to update status.");
        }
    }
    
    private void handleDeleteRoom(Hotel room) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Room Type");
        alert.setHeaderText("Delete " + room.getRoomType() + "?");
        alert.setContentText("This action cannot be undone.");
        
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                if (hotelDAO.deleteHotel(room.getId())) {
                    showInfo("Room type deleted successfully!");
                    loadAllData();
                } else {
                    showError("Failed to delete room type.");
                }
            }
        });
    }
    
    @FXML
    private void handleRefreshData() {
        loadAllData();
        showInfo("Data refreshed!");
    }
    
    @FXML
    private void handleRefreshRooms() {
        loadRooms();
        updateStatistics();
        showInfo("Rooms refreshed!");
    }
    
    // This method is now implemented above with full booking management
    
    @FXML
    private void handleLogout() {
        try {
            SessionManager.getInstance().clearSession();
            Stage stage = (Stage) welcomeLabel.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/threelegend/shohojjibon/login.fxml"));
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
            stage.setTitle("Shohoj Jibon - Login");
        } catch (Exception e) {
            showError("Failed to logout: " + e.getMessage());
        }
    }
    
    private void showInfo(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

