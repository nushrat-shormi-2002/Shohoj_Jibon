package com.threelegend.shohojjibon.controller;

import com.threelegend.shohojjibon.dao.*;
import com.threelegend.shohojjibon.model.*;
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
import javafx.application.Platform;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Comprehensive User Dashboard Controller
 * Access ALL services in one place
 * Shohoj Jibon - Makes your life easier
 * Developed by Three_Legend
 */
public class UserDashboardController {
    
    // User Info
    @FXML private Label welcomeLabel;
    
    // Transport Tab
    @FXML private TextField busFromField;
    @FXML private TextField busToField;
    @FXML private TableView<Bus> busTable;
    @FXML private TableColumn<Bus, String> busNameCol;
    @FXML private TableColumn<Bus, String> busNumberCol;
    @FXML private TableColumn<Bus, String> busRouteCol;
    @FXML private TableColumn<Bus, String> busScheduleCol;
    @FXML private TableColumn<Bus, String> busFareCol;
    @FXML private TableColumn<Bus, String> busProviderCol;
    @FXML private TableColumn<Bus, Void> busActionsCol;
    
    // Doctors Tab
    @FXML private TextField doctorNameField;
    @FXML private ComboBox<String> specialtyCombo;
    @FXML private ComboBox<String> doctorLocationCombo;
    @FXML private TableView<Doctor> doctorTable;
    @FXML private TableColumn<Doctor, String> doctorNameCol;
    @FXML private TableColumn<Doctor, String> doctorSpecialtyCol;
    @FXML private TableColumn<Doctor, String> doctorLocationCol;
    @FXML private TableColumn<Doctor, String> doctorFeeCol;
    @FXML private TableColumn<Doctor, String> doctorAvailableCol;
    @FXML private TableColumn<Doctor, String> doctorContactCol;
    @FXML private TableColumn<Doctor, Void> doctorActionsCol;
    
    // Tests Tab
    @FXML private TextField testNameField;
    @FXML private ComboBox<String> testCategoryCombo;
    @FXML private ComboBox<String> testLocationCombo;
    @FXML private TableView<DiagnosticTest> testTable;
    @FXML private TableColumn<DiagnosticTest, String> testNameCol;
    @FXML private TableColumn<DiagnosticTest, String> testCategoryCol;
    @FXML private TableColumn<DiagnosticTest, String> testLocationCol;
    @FXML private TableColumn<DiagnosticTest, String> testPriceCol;
    @FXML private TableColumn<DiagnosticTest, String> testResultTimeCol;
    @FXML private TableColumn<DiagnosticTest, String> testHomeCollectionCol;
    @FXML private TableColumn<DiagnosticTest, Void> testActionsCol;
    
    // Hotels Tab
    @FXML private TextField hotelNameField;
    @FXML private TextField hotelLocationField;
    @FXML private ComboBox<String> roomTypeCombo;
    @FXML private ComboBox<String> sortByCombo;
    @FXML private TableView<Hotel> hotelTable;
    @FXML private TableColumn<Hotel, String> hotelNameCol;
    @FXML private TableColumn<Hotel, String> hotelLocationCol;
    @FXML private TableColumn<Hotel, String> hotelRoomTypeCol;
    @FXML private TableColumn<Hotel, String> hotelPriceCol;
    @FXML private TableColumn<Hotel, String> hotelAvailableRoomsCol;
    @FXML private TableColumn<Hotel, String> hotelRatingCol;
    @FXML private TableColumn<Hotel, Void> hotelActionsCol;
    
    // Blood Donors Tab
    @FXML private ComboBox<String> bloodGroupCombo;
    @FXML private TextField bloodDonorLocationField;
    @FXML private TableView<BloodDonor> bloodDonorTable;
    @FXML private TableColumn<BloodDonor, String> donorNameCol;
    @FXML private TableColumn<BloodDonor, String> donorBloodGroupCol;
    @FXML private TableColumn<BloodDonor, String> donorAgeCol;
    @FXML private TableColumn<BloodDonor, String> donorLocationCol;
    @FXML private TableColumn<BloodDonor, String> donorContactCol;
    @FXML private TableColumn<BloodDonor, String> donorEligibilityCol;
    @FXML private TableColumn<BloodDonor, String> donorBloodBankCol;
    @FXML private TableColumn<BloodDonor, Void> donorActionsCol;
    
    // Bookings Tab
    @FXML private TableView<Booking> bookingTable;
    @FXML private TableColumn<Booking, String> bookingIdCol;
    @FXML private TableColumn<Booking, String> bookingServiceCol;
    @FXML private TableColumn<Booking, String> bookingDetailsCol;
    @FXML private TableColumn<Booking, String> bookingDateCol;
    @FXML private TableColumn<Booking, String> bookingStatusCol;
    @FXML private TableColumn<Booking, Void> bookingActionsCol;
    
    // Data Lists
    private ObservableList<Bus> busList = FXCollections.observableArrayList();
    private ObservableList<Doctor> doctorList = FXCollections.observableArrayList();
    private ObservableList<DiagnosticTest> testList = FXCollections.observableArrayList();
    private ObservableList<Hotel> hotelList = FXCollections.observableArrayList();
    private ObservableList<BloodDonor> donorList = FXCollections.observableArrayList();
    private ObservableList<Booking> bookingList = FXCollections.observableArrayList();
    
    // DAOs
    private BusDAO busDAO = new BusDAO();
    private DoctorDAO doctorDAO = new DoctorDAO();
    private DiagnosticTestDAO testDAO = new DiagnosticTestDAO();
    private HotelDAO hotelDAO = new HotelDAO();
    private BloodDonorDAO donorDAO = new BloodDonorDAO();
    private BloodRequestDAO bloodRequestDAO = new BloodRequestDAO();
    private BookingDAO bookingDAO = new BookingDAO();
    private ServiceProviderDAO providerDAO = new ServiceProviderDAO();
    
    // Current User
    private User currentUser;
    
    @FXML
    public void initialize() {
        currentUser = SessionManager.getInstance().getCurrentUser();
        welcomeLabel.setText("Welcome, " + currentUser.getFullName() + "!");
        
        // Setup all tables
        setupBusTable();
        setupDoctorTable();
        setupTestTable();
        setupHotelTable();
        setupBloodDonorTable();
        setupBookingTable();
        
        // Load filter options
        loadFilterOptions();
        
        // Load initial data
        loadAllData();
    }
    
    // ==================== BUS METHODS ====================
    
    private void setupBusTable() {
        busNameCol.setCellValueFactory(data -> data.getValue().busNameProperty());
        busNumberCol.setCellValueFactory(data -> data.getValue().busNumberProperty());
        busRouteCol.setCellValueFactory(data -> data.getValue().routeProperty());
        busScheduleCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getScheduleTime() != null ? data.getValue().getScheduleTime().toString() : ""));
        busFareCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getFormattedFare()));
        busProviderCol.setCellValueFactory(data -> {
            try {
                ServiceProvider provider = providerDAO.getProviderById(data.getValue().getProviderId());
                return new SimpleStringProperty(provider != null ? provider.getProviderName() : "Unknown");
            } catch (Exception e) {
                return new SimpleStringProperty("Unknown");
            }
        });
        
        busActionsCol.setCellFactory(param -> new TableCell<>() {
            private final Button viewBtn = new Button("👁️ View");
            private final Button bookBtn = new Button("📝 Book");
            private final HBox buttons = new HBox(5, viewBtn, bookBtn);
            
            {
                viewBtn.setStyle("-fx-background-color: #3b82f6; -fx-text-fill: white; -fx-cursor: hand; -fx-padding: 5 10;");
                bookBtn.setStyle("-fx-background-color: #10b981; -fx-text-fill: white; -fx-cursor: hand; -fx-padding: 5 10;");
                buttons.setAlignment(Pos.CENTER);
                
                viewBtn.setOnAction(e -> {
                    Bus bus = getTableView().getItems().get(getIndex());
                    showBusDetails(bus);
                });
                
                bookBtn.setOnAction(e -> {
                    Bus bus = getTableView().getItems().get(getIndex());
                    handleBookBus(bus);
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
    
    @FXML
    private void handleSearchBuses() {
        String from = busFromField.getText().trim();
        String to = busToField.getText().trim();
        
        if (from.isEmpty() && to.isEmpty()) {
            showWarning("Please enter at least one search criteria");
            return;
        }
        
        List<Bus> results = busDAO.searchBuses(from, to);
        busList.clear();
        busList.addAll(results);
        showInfo("Found " + results.size() + " bus(es)");
    }
    
    @FXML
    private void handleShowAllBuses() {
        busList.clear();
        busList.addAll(busDAO.getAllBuses());
    }
    
    /**
     * Test method to verify route details functionality
     * This can be called to test the complete route display
     */
    public void testRouteDetails() {
        try {
            // Test with the first available bus
            List<Bus> buses = busDAO.getAllBuses();
            if (!buses.isEmpty()) {
                Bus testBus = buses.get(0);
                System.out.println("Testing route details for: " + testBus.getBusName());
                
                // Test route summary
                Map<String, Object> summary = busDAO.getRouteSummary(testBus.getId());
                System.out.println("Route Summary: " + summary);
                
                // Test bus stops
                List<BusStop> stops = busDAO.getBusStops(testBus.getId());
                System.out.println("Total Stops: " + stops.size());
                
                for (BusStop stop : stops) {
                    System.out.println(stop.toString());
                }
                
                // Test the scrollable dialog
                System.out.println("Testing scrollable dialog...");
                showBusDetails(testBus);
            }
        } catch (Exception e) {
            System.err.println("Error testing route details: " + e.getMessage());
        }
    }
    
    /**
     * Test method to verify doctor search functionality
     * This can be called to test individual search criteria
     */
    public void testDoctorSearch() {
        try {
            System.out.println("=== Testing Doctor Search Functionality ===");
            
            // Test search by name
            System.out.println("\n1. Testing search by name 'Ahmed':");
            List<Doctor> nameResults = doctorDAO.searchDoctorsByName("Ahmed");
            System.out.println("Found " + nameResults.size() + " doctors with name 'Ahmed'");
            for (Doctor doctor : nameResults) {
                System.out.println("  - " + doctor.getName() + " (" + doctor.getSpecialty() + ")");
            }
            
            // Test search by specialty
            System.out.println("\n2. Testing search by specialty 'Cardiology':");
            List<Doctor> specialtyResults = doctorDAO.searchDoctorsBySpecialty("Cardiology");
            System.out.println("Found " + specialtyResults.size() + " cardiologists");
            for (Doctor doctor : specialtyResults) {
                System.out.println("  - " + doctor.getName() + " (" + doctor.getLocation() + ")");
            }
            
            // Test search by location
            System.out.println("\n3. Testing search by location 'Banani':");
            List<Doctor> locationResults = doctorDAO.searchDoctorsByLocation("Banani");
            System.out.println("Found " + locationResults.size() + " doctors in Banani");
            for (Doctor doctor : locationResults) {
                System.out.println("  - " + doctor.getName() + " (" + doctor.getSpecialty() + ")");
            }
            
            // Test combined search
            System.out.println("\n4. Testing combined search (Cardiology + Banani):");
            List<Doctor> combinedResults = doctorDAO.searchDoctors(null, "Cardiology", "Banani", null);
            System.out.println("Found " + combinedResults.size() + " cardiologists in Banani");
            for (Doctor doctor : combinedResults) {
                System.out.println("  - " + doctor.getName() + " (" + doctor.getConsultationFee() + " BDT)");
            }
            
            System.out.println("\n=== Doctor Search Test Complete ===");
            
        } catch (Exception e) {
            System.err.println("Error testing doctor search: " + e.getMessage());
        }
    }
    
    /**
     * Test method to verify hotel search functionality
     * This can be called to test individual search criteria and sorting
     */
    public void testHotelSearch() {
        try {
            System.out.println("=== Testing Hotel Search Functionality ===");
            
            // Test search by name
            System.out.println("\n1. Testing search by name 'Grand':");
            List<Hotel> nameResults = hotelDAO.searchHotels("Grand", null, null, null);
            System.out.println("Found " + nameResults.size() + " hotels with name 'Grand'");
            for (Hotel hotel : nameResults) {
                System.out.println("  - " + hotel.getHotelName() + " (" + hotel.getLocation() + ") - " + hotel.getFormattedPrice());
            }
            
            // Test search by location
            System.out.println("\n2. Testing search by location 'Dhaka':");
            List<Hotel> locationResults = hotelDAO.searchHotels(null, "Dhaka", null, null);
            System.out.println("Found " + locationResults.size() + " hotels in Dhaka");
            for (Hotel hotel : locationResults) {
                System.out.println("  - " + hotel.getHotelName() + " (" + hotel.getRoomType() + ") - " + hotel.getFormattedPrice());
            }
            
            // Test search by room type
            System.out.println("\n3. Testing search by room type 'Deluxe':");
            List<Hotel> roomTypeResults = hotelDAO.searchHotels(null, null, "Deluxe", null);
            System.out.println("Found " + roomTypeResults.size() + " Deluxe rooms");
            for (Hotel hotel : roomTypeResults) {
                System.out.println("  - " + hotel.getHotelName() + " (" + hotel.getLocation() + ") - " + hotel.getFormattedPrice());
            }
            
            // Test price sorting (low to high)
            System.out.println("\n4. Testing price sorting (low to high):");
            List<Hotel> priceLowResults = hotelDAO.searchHotels(null, null, null, "price_low");
            System.out.println("Found " + priceLowResults.size() + " hotels sorted by price (low to high)");
            for (Hotel hotel : priceLowResults) {
                System.out.println("  - " + hotel.getHotelName() + " - " + hotel.getFormattedPrice());
            }
            
            // Test price sorting (high to low)
            System.out.println("\n5. Testing price sorting (high to low):");
            List<Hotel> priceHighResults = hotelDAO.searchHotels(null, null, null, "price_high");
            System.out.println("Found " + priceHighResults.size() + " hotels sorted by price (high to low)");
            for (Hotel hotel : priceHighResults) {
                System.out.println("  - " + hotel.getHotelName() + " - " + hotel.getFormattedPrice());
            }
            
            // Test combined search with sorting
            System.out.println("\n6. Testing combined search (Dhaka + Deluxe + Price Low to High):");
            List<Hotel> combinedResults = hotelDAO.searchHotels(null, "Dhaka", "Deluxe", "price_low");
            System.out.println("Found " + combinedResults.size() + " Deluxe rooms in Dhaka sorted by price");
            for (Hotel hotel : combinedResults) {
                System.out.println("  - " + hotel.getHotelName() + " - " + hotel.getFormattedPrice());
            }
            
            System.out.println("\n=== Hotel Search Test Complete ===");
            
        } catch (Exception e) {
            System.err.println("Error testing hotel search: " + e.getMessage());
        }
    }
    
    private void showBusDetails(Bus bus) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Bus Route Details");
        alert.setHeaderText(bus.getBusName() + " - " + bus.getBusNumber());
        
        // Create a scrollable text area for the route details
        TextArea textArea = new TextArea();
        textArea.setEditable(false);
        textArea.setWrapText(true);
        textArea.setPrefRowCount(20); // Set initial height
        textArea.setPrefColumnCount(80); // Set initial width
        textArea.setStyle("-fx-font-family: 'Consolas', 'Monaco', monospace; -fx-font-size: 12px;");
        
        StringBuilder details = new StringBuilder();
        
        // Get route summary
        Map<String, Object> summary = busDAO.getRouteSummary(bus.getId());
        if (!summary.isEmpty()) {
            details.append("Route: ").append(bus.getRoute()).append("\n");
            details.append("Schedule: ").append(bus.getScheduleTime()).append("\n");
            details.append("Total Stops: ").append(summary.get("totalStops")).append("\n");
            details.append("Total Distance: ").append(String.format("%.2f km", bus.getDistance())).append("\n");
            details.append("Total Fare: ").append(String.format("৳%.2f", (Double)summary.get("totalFare"))).append("\n\n");
        }
        
        // Get all stops for this bus
        List<BusStop> stops = busDAO.getBusStops(bus.getId());
        if (!stops.isEmpty()) {
            details.append("Route Details:\n");
            details.append("────────────────────────────────────────\n");
            
            for (BusStop stop : stops) {
                details.append(String.format("%d. %s\n", stop.getStopOrder(), stop.getStopName()));
                details.append(String.format("   Fare from start: %s\n", stop.getFormattedFare()));
                details.append(String.format("   Time from start: %s\n", stop.getArrivalTimeFormatted()));
                details.append(String.format("   Type: %s\n", stop.getStopType()));
                if (stop.getLandmark() != null && !stop.getLandmark().trim().isEmpty()) {
                    details.append(String.format("   Landmark: %s\n", stop.getLandmark()));
                }
                details.append("\n");
            }
        }
        
        // Add provider information
        try {
            ServiceProvider provider = providerDAO.getProviderById(bus.getProviderId());
            if (provider != null) {
                details.append("Provider: ").append(provider.getProviderName()).append("\n");
                details.append("Address: ").append(provider.getAddress()).append("\n");
            }
        } catch (Exception e) {
            // Ignore
        }
        
        // Set the text content
        textArea.setText(details.toString());
        
        // Create a scroll pane for the text area
        ScrollPane scrollPane = new ScrollPane(textArea);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setPrefViewportWidth(700);
        scrollPane.setPrefViewportHeight(500);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        
        // Set the scroll pane as the content
        alert.getDialogPane().setContent(scrollPane);
        alert.getDialogPane().setPrefWidth(750); // Make dialog wider
        alert.getDialogPane().setPrefHeight(600); // Make dialog taller
        
        // Auto-scroll to top when dialog opens
        textArea.setScrollTop(0);
        
        alert.showAndWait();
    }
    
    private void handleBookBus(Bus bus) {
        Dialog<Booking> dialog = new Dialog<>();
        dialog.setTitle("Book Bus");
        dialog.setHeaderText("Book: " + bus.getBusName() + " - " + bus.getBusNumber());
        
        ButtonType bookButtonType = new ButtonType("Confirm Booking", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(bookButtonType, ButtonType.CANCEL);
        
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));
        
        DatePicker datePicker = new DatePicker(LocalDate.now());
        TextField passengersField = new TextField("1");
        
        grid.add(new Label("Travel Date:"), 0, 0);
        grid.add(datePicker, 1, 0);
        grid.add(new Label("Number of Passengers:"), 0, 1);
        grid.add(passengersField, 1, 1);
        
        dialog.getDialogPane().setContent(grid);
        
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == bookButtonType) {
                try {
                    Booking booking = new Booking();
                    booking.setUserId(currentUser.getId());
                    booking.setServiceType("BUS");
                    booking.setServiceId(bus.getId());
                    booking.setProviderId(bus.getProviderId());
                    booking.setBookingDate(LocalDate.now());
                    booking.setServiceDate(datePicker.getValue().atStartOfDay());
                    booking.setNumberOfPeople(Integer.parseInt(passengersField.getText()));
                    booking.setTotalAmount(bus.getFare() * Integer.parseInt(passengersField.getText()));
                    booking.setStatus("PENDING");
                    return booking;
                } catch (Exception e) {
                    showError("Invalid input");
                    return null;
                }
            }
            return null;
        });
        
        dialog.showAndWait().ifPresent(booking -> {
            if (bookingDAO.createBooking(booking)) {
                showSuccess("Bus booked successfully!");
                loadBookings();
            } else {
                showError("Failed to book bus");
            }
        });
    }
    
    // ==================== DOCTOR METHODS ====================
    
    private void setupDoctorTable() {
        doctorNameCol.setCellValueFactory(data -> data.getValue().nameProperty());
        doctorSpecialtyCol.setCellValueFactory(data -> data.getValue().specialtyProperty());
        doctorLocationCol.setCellValueFactory(data -> data.getValue().locationProperty());
        doctorFeeCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getFormattedFee()));
        doctorAvailableCol.setCellValueFactory(data -> data.getValue().availableDaysProperty());
        doctorContactCol.setCellValueFactory(data -> data.getValue().contactNumberProperty());
        
        doctorActionsCol.setCellFactory(param -> new TableCell<>() {
            private final Button viewBtn = new Button("👁️ Details");
            private final Button bookBtn = new Button("📝 Book Appointment");
            private final HBox buttons = new HBox(5, viewBtn, bookBtn);
            
            {
                viewBtn.setStyle("-fx-background-color: #10b981; -fx-text-fill: white; -fx-cursor: hand; -fx-padding: 5 10;");
                bookBtn.setStyle("-fx-background-color: #3b82f6; -fx-text-fill: white; -fx-cursor: hand; -fx-padding: 5 10;");
                buttons.setAlignment(Pos.CENTER);
                
                viewBtn.setOnAction(e -> {
                    Doctor doctor = getTableView().getItems().get(getIndex());
                    showDoctorDetails(doctor);
                });
                
                bookBtn.setOnAction(e -> {
                    Doctor doctor = getTableView().getItems().get(getIndex());
                    handleBookDoctor(doctor);
                });
            }
            
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : buttons);
            }
        });
        
        doctorTable.setItems(doctorList);
    }
    
    @FXML
    private void handleSearchDoctors() {
        String name = doctorNameField.getText().trim();
        String specialty = specialtyCombo.getValue();
        String location = doctorLocationCombo.getValue();
        
        List<Doctor> results;
        
        // Determine which search criteria to use
        if (!name.isEmpty() && (specialty == null || specialty.equals("All")) && (location == null || location.equals("All"))) {
            // Search by name only
            results = doctorDAO.searchDoctorsByName(name);
            showInfo("Searching by name: '" + name + "' - Found " + results.size() + " doctor(s)");
        } else if (name.isEmpty() && specialty != null && !specialty.equals("All") && (location == null || location.equals("All"))) {
            // Search by specialty only
            results = doctorDAO.searchDoctorsBySpecialty(specialty);
            showInfo("Searching by specialty: '" + specialty + "' - Found " + results.size() + " doctor(s)");
        } else if (name.isEmpty() && (specialty == null || specialty.equals("All")) && location != null && !location.equals("All")) {
            // Search by location only
            results = doctorDAO.searchDoctorsByLocation(location);
            showInfo("Searching by location: '" + location + "' - Found " + results.size() + " doctor(s)");
        } else {
            // Combined search
            results = doctorDAO.searchDoctors(name.isEmpty() ? null : name, 
                                           (specialty == null || specialty.equals("All")) ? null : specialty, 
                                           (location == null || location.equals("All")) ? null : location, 
                                           null);
            showInfo("Combined search - Found " + results.size() + " doctor(s)");
        }
        
        doctorList.clear();
        doctorList.addAll(results);
    }
    
    @FXML
    private void handleShowAllDoctors() {
        doctorList.clear();
        doctorList.addAll(doctorDAO.searchDoctors(null, null, null, null));
        showInfo("Showing all available doctors");
    }
    
    private void showDoctorDetails(Doctor doctor) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Doctor Details");
        alert.setHeaderText("Dr. " + doctor.getName());
        
        StringBuilder details = new StringBuilder();
        details.append("Specialty: ").append(doctor.getSpecialty()).append("\n");
        details.append("Qualifications: ").append(doctor.getQualifications()).append("\n");
        details.append("Experience: ").append(doctor.getExperienceYears()).append(" years\n");
        details.append("Consultation Fee: ").append(doctor.getFormattedFee()).append("\n");
        details.append("Location: ").append(doctor.getLocation()).append("\n");
        details.append("Available Days: ").append(doctor.getAvailableDays()).append("\n");
        details.append("Available Time: ").append(doctor.getAvailableTime()).append("\n");
        details.append("Contact: ").append(doctor.getContactNumber()).append("\n");
        if (doctor.getEmail() != null && !doctor.getEmail().isEmpty()) {
            details.append("Email: ").append(doctor.getEmail()).append("\n");
        }
        details.append("Languages: ").append(doctor.getLanguagesSpoken()).append("\n");
        details.append("Rating: ").append(doctor.getRating()).append("/5 (").append(doctor.getTotalReviews()).append(" reviews)\n");
        
        alert.setContentText(details.toString());
        alert.setResizable(true);
        alert.getDialogPane().setPrefWidth(500);
        alert.showAndWait();
    }
    
    private void handleBookDoctor(Doctor doctor) {
        Dialog<Booking> dialog = new Dialog<>();
        dialog.setTitle("Book Doctor Appointment");
        dialog.setHeaderText("Book appointment with Dr. " + doctor.getName() + " (" + doctor.getSpecialty() + ")");
        
        ButtonType bookButtonType = new ButtonType("Confirm Appointment", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(bookButtonType, ButtonType.CANCEL);
        
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));
        
        DatePicker appointmentDatePicker = new DatePicker();
        appointmentDatePicker.setValue(LocalDate.now().plusDays(1));
        appointmentDatePicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.isBefore(LocalDate.now()));
            }
        });
        
        ComboBox<String> timeSlotCombo = new ComboBox<>();
        timeSlotCombo.getItems().addAll("09:00 AM", "10:00 AM", "11:00 AM", "12:00 PM", "02:00 PM", "03:00 PM", "04:00 PM", "05:00 PM");
        timeSlotCombo.setValue("09:00 AM");
        
        TextArea symptomsArea = new TextArea();
        symptomsArea.setPromptText("Describe your symptoms or reason for consultation...");
        symptomsArea.setPrefRowCount(3);
        
        TextField contactField = new TextField();
        contactField.setText(currentUser.getPhone());
        
        grid.add(new Label("Appointment Date:"), 0, 0);
        grid.add(appointmentDatePicker, 1, 0);
        grid.add(new Label("Preferred Time:"), 0, 1);
        grid.add(timeSlotCombo, 1, 1);
        grid.add(new Label("Symptoms/Reason:"), 0, 2);
        grid.add(symptomsArea, 1, 2);
        grid.add(new Label("Contact Number:"), 0, 3);
        grid.add(contactField, 1, 3);
        
        dialog.getDialogPane().setContent(grid);
        
        Platform.runLater(() -> appointmentDatePicker.requestFocus());
        
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == bookButtonType) {
                try {
                    Booking booking = new Booking();
                    booking.setUserId(currentUser.getId());
                    booking.setServiceType("DOCTOR");
                    booking.setServiceId(doctor.getId());
                    booking.setProviderId(doctor.getProviderId());
                    booking.setBookingDate(LocalDate.now());
                    booking.setServiceDate(appointmentDatePicker.getValue().atStartOfDay());
                    booking.setNumberOfPeople(1);
                    booking.setTotalAmount(doctor.getConsultationFee());
                    booking.setStatus("PENDING");
                    booking.setSpecialRequests("Time: " + timeSlotCombo.getValue() + 
                                            ", Symptoms: " + symptomsArea.getText() + 
                                            ", Contact: " + contactField.getText());
                    return booking;
                } catch (Exception e) {
                    showError("Invalid input: " + e.getMessage());
                    return null;
                }
            }
            return null;
        });
        
        dialog.showAndWait().ifPresent(booking -> {
            if (bookingDAO.createBooking(booking)) {
                showSuccess("Doctor appointment booked successfully!\n" +
                           "Appointment Date: " + booking.getServiceDate().toLocalDate() + "\n" +
                           "Time: " + timeSlotCombo.getValue() + "\n" +
                           "Total Fee: " + doctor.getFormattedFee());
                loadBookings();
            } else {
                showError("Failed to book appointment");
            }
        });
    }
    
    // ==================== TEST METHODS ====================
    
    private void setupTestTable() {
        testNameCol.setCellValueFactory(data -> data.getValue().testNameProperty());
        testCategoryCol.setCellValueFactory(data -> data.getValue().testCategoryProperty());
        testLocationCol.setCellValueFactory(data -> data.getValue().locationProperty());
        testPriceCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getFormattedPrice()));
        testResultTimeCol.setCellValueFactory(data -> data.getValue().resultTimeProperty());
        testHomeCollectionCol.setCellValueFactory(data -> 
            new SimpleStringProperty(data.getValue().isHomeCollection() ? "✓ Yes" : "✗ No"));
        
        testActionsCol.setCellFactory(param -> new TableCell<>() {
            private final Button viewBtn = new Button("👁️ Details");
            private final Button bookBtn = new Button("📝 Book Test");
            private final HBox buttons = new HBox(5, viewBtn, bookBtn);
            
            {
                viewBtn.setStyle("-fx-background-color: #8b5cf6; -fx-text-fill: white; -fx-cursor: hand; -fx-padding: 5 10;");
                bookBtn.setStyle("-fx-background-color: #10b981; -fx-text-fill: white; -fx-cursor: hand; -fx-padding: 5 10;");
                buttons.setAlignment(Pos.CENTER);
                
                viewBtn.setOnAction(e -> {
                    DiagnosticTest test = getTableView().getItems().get(getIndex());
                    showTestDetails(test);
                });
                
                bookBtn.setOnAction(e -> {
                    DiagnosticTest test = getTableView().getItems().get(getIndex());
                    handleBookTest(test);
                });
            }
            
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : buttons);
            }
        });
        
        testTable.setItems(testList);
    }
    
    @FXML
    private void handleSearchTests() {
        String name = testNameField.getText().trim();
        String category = testCategoryCombo.getValue();
        String location = testLocationCombo.getValue();
        
        List<DiagnosticTest> results = testDAO.searchTests(name, category, location, null);
        testList.clear();
        testList.addAll(results);
        showInfo("Found " + results.size() + " test(s)");
    }
    
    @FXML
    private void handleShowAllTests() {
        testList.clear();
        testList.addAll(testDAO.searchTests("", "", "", null));
    }
    
    private void showTestDetails(DiagnosticTest test) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Test Details");
        alert.setHeaderText(test.getTestName());
        
        StringBuilder details = new StringBuilder();
        details.append("Category: ").append(test.getTestCategory()).append("\n");
        details.append("Price: ").append(test.getFormattedPrice()).append("\n");
        details.append("Location: ").append(test.getLocation()).append("\n");
        details.append("Result Time: ").append(test.getResultTime()).append("\n");
        details.append("Sample Type: ").append(test.getSampleType()).append("\n");
        details.append("Home Collection: ").append(test.isHomeCollection() ? "Available" : "Not Available").append("\n");
        if (test.isHomeCollection()) {
            details.append("Home Collection Charge: ").append(test.getFormattedHomeCollectionCharge()).append("\n");
        }
        if (test.getDescription() != null && !test.getDescription().isEmpty()) {
            details.append("\nDescription:\n").append(test.getDescription()).append("\n");
        }
        if (test.getPreparationRequired() != null && !test.getPreparationRequired().isEmpty()) {
            details.append("\nPreparation Required:\n").append(test.getPreparationRequired()).append("\n");
        }
        
        alert.setContentText(details.toString());
        alert.setResizable(true);
        alert.getDialogPane().setPrefWidth(550);
        alert.showAndWait();
    }
    
    private void handleBookTest(DiagnosticTest test) {
        Dialog<Booking> dialog = new Dialog<>();
        dialog.setTitle("Book Diagnostic Test");
        dialog.setHeaderText("Book: " + test.getTestName() + " (" + test.getTestCategory() + ")");
        
        ButtonType bookButtonType = new ButtonType("Confirm Booking", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(bookButtonType, ButtonType.CANCEL);
        
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));
        
        DatePicker testDatePicker = new DatePicker();
        testDatePicker.setValue(LocalDate.now().plusDays(1));
        testDatePicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.isBefore(LocalDate.now()));
            }
        });
        
        ComboBox<String> timeSlotCombo = new ComboBox<>();
        timeSlotCombo.getItems().addAll("09:00 AM", "10:00 AM", "11:00 AM", "12:00 PM", "02:00 PM", "03:00 PM", "04:00 PM", "05:00 PM");
        timeSlotCombo.setValue("09:00 AM");
        
        TextArea symptomsArea = new TextArea();
        symptomsArea.setPromptText("Describe your symptoms or reason for this test...");
        symptomsArea.setPrefRowCount(3);
        
        TextField contactField = new TextField();
        contactField.setText(currentUser.getPhone());
        
        CheckBox homeCollectionCheck = new CheckBox("Home Collection Required");
        homeCollectionCheck.setSelected(test.isHomeCollection());
        homeCollectionCheck.setDisable(!test.isHomeCollection());
        
        TextField addressField = new TextField();
        addressField.setText(currentUser.getAddress());
        addressField.setDisable(!test.isHomeCollection());
        
        // Enable/disable address field based on home collection checkbox
        homeCollectionCheck.setOnAction(e -> {
            addressField.setDisable(!homeCollectionCheck.isSelected());
        });
        
        grid.add(new Label("Test Date:"), 0, 0);
        grid.add(testDatePicker, 1, 0);
        grid.add(new Label("Preferred Time:"), 0, 1);
        grid.add(timeSlotCombo, 1, 1);
        grid.add(new Label("Symptoms/Reason:"), 0, 2);
        grid.add(symptomsArea, 1, 2);
        grid.add(new Label("Contact Number:"), 0, 3);
        grid.add(contactField, 1, 3);
        grid.add(new Label("Home Collection:"), 0, 4);
        grid.add(homeCollectionCheck, 1, 4);
        grid.add(new Label("Address:"), 0, 5);
        grid.add(addressField, 1, 5);
        
        dialog.getDialogPane().setContent(grid);
        
        Platform.runLater(() -> testDatePicker.requestFocus());
        
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == bookButtonType) {
                try {
                    Booking booking = new Booking();
                    booking.setUserId(currentUser.getId());
                    booking.setServiceType("TEST");
                    booking.setServiceId(test.getId());
                    booking.setProviderId(test.getProviderId());
                    booking.setBookingDate(LocalDate.now());
                    booking.setServiceDate(testDatePicker.getValue().atStartOfDay());
                    booking.setNumberOfPeople(1);
                    
                    // Calculate total amount including home collection charge if applicable
                    double totalAmount = test.getPrice();
                    if (homeCollectionCheck.isSelected() && test.isHomeCollection()) {
                        totalAmount += test.getHomeCollectionCharge();
                    }
                    booking.setTotalAmount(totalAmount);
                    
                    booking.setStatus("PENDING");
                    booking.setSpecialRequests("Time: " + timeSlotCombo.getValue() + 
                                            ", Symptoms: " + symptomsArea.getText() + 
                                            ", Contact: " + contactField.getText() +
                                            ", Home Collection: " + (homeCollectionCheck.isSelected() ? "Yes" : "No") +
                                            ", Address: " + addressField.getText());
                    return booking;
                } catch (Exception e) {
                    showError("Invalid input: " + e.getMessage());
                    return null;
                }
            }
            return null;
        });
        
        dialog.showAndWait().ifPresent(booking -> {
            if (bookingDAO.createBooking(booking)) {
                showSuccess("Diagnostic test booked successfully!\n" +
                           "Test Date: " + booking.getServiceDate().toLocalDate() + "\n" +
                           "Time: " + timeSlotCombo.getValue() + "\n" +
                           "Total Amount: " + String.format("৳%.2f", booking.getTotalAmount()) + "\n" +
                           "Home Collection: " + (homeCollectionCheck.isSelected() ? "Yes" : "No"));
                loadBookings();
            } else {
                showError("Failed to book test");
            }
        });
    }
    
    // ==================== HOTEL METHODS ====================
    
    private void setupHotelTable() {
        hotelNameCol.setCellValueFactory(data -> data.getValue().hotelNameProperty());
        hotelLocationCol.setCellValueFactory(data -> data.getValue().locationProperty());
        hotelRoomTypeCol.setCellValueFactory(data -> data.getValue().roomTypeProperty());
        hotelPriceCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getFormattedPrice()));
        hotelAvailableRoomsCol.setCellValueFactory(data -> data.getValue().availableRoomsProperty().asString());
        hotelRatingCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getRating() + "⭐"));
        
        hotelActionsCol.setCellFactory(param -> new TableCell<>() {
            private final Button viewBtn = new Button("👁️ View");
            private final Button bookBtn = new Button("📝 Book");
            private final HBox buttons = new HBox(5, viewBtn, bookBtn);
            
            {
                viewBtn.setStyle("-fx-background-color: #f59e0b; -fx-text-fill: white; -fx-cursor: hand; -fx-padding: 5 10;");
                bookBtn.setStyle("-fx-background-color: #10b981; -fx-text-fill: white; -fx-cursor: hand; -fx-padding: 5 10;");
                buttons.setAlignment(Pos.CENTER);
                
                viewBtn.setOnAction(e -> {
                    Hotel hotel = getTableView().getItems().get(getIndex());
                    showHotelDetails(hotel);
                });
                
                bookBtn.setOnAction(e -> {
                    Hotel hotel = getTableView().getItems().get(getIndex());
                    handleBookHotel(hotel);
                });
            }
            
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : buttons);
            }
        });
        
        hotelTable.setItems(hotelList);
    }
    
    @FXML
    private void handleSearchHotels() {
        String name = hotelNameField.getText().trim();
        String location = hotelLocationField.getText().trim();
        String roomType = roomTypeCombo.getValue();
        String sortBy = sortByCombo.getValue();
        
        List<Hotel> results;
        
        // Convert sortBy to sortParam
        String sortParam = null;
        if (sortBy != null) {
            switch (sortBy) {
                case "Price (Low to High)":
                    sortParam = "price_low";
                    break;
                case "Price (High to Low)":
                    sortParam = "price_high";
                    break;
                case "Rating":
                    sortParam = "rating";
                    break;
                case "All":
                default:
                    sortParam = "All";
                    break;
            }
        }
        
        // Determine which search criteria to use
        if (!name.isEmpty() && location.isEmpty() && (roomType == null || roomType.equals("All"))) {
            // Search by name only
            results = hotelDAO.searchHotels(name, null, null, sortParam);
            showInfo("Searching by name: '" + name + "' - Found " + results.size() + " hotel(s)");
        } else if (name.isEmpty() && !location.isEmpty() && (roomType == null || roomType.equals("All"))) {
            // Search by location only
            results = hotelDAO.searchHotels(null, location, null, sortParam);
            showInfo("Searching by location: '" + location + "' - Found " + results.size() + " hotel(s)");
        } else if (name.isEmpty() && location.isEmpty() && roomType != null && !roomType.equals("All")) {
            // Search by room type only
            results = hotelDAO.searchHotels(null, null, roomType, sortParam);
            showInfo("Searching by room type: '" + roomType + "' - Found " + results.size() + " hotel(s)");
        } else {
            // Combined search
            results = hotelDAO.searchHotels(name, location, roomType, sortParam);
            showInfo("Combined search - Found " + results.size() + " hotel(s)");
        }
        
        hotelList.clear();
        hotelList.addAll(results);
    }
    
    @FXML
    private void handleShowAllHotels() {
        hotelList.clear();
        hotelList.addAll(hotelDAO.searchHotels(null, null, null, null));
        showInfo("Showing all available hotels");
    }
    
    private void showHotelDetails(Hotel hotel) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Hotel Details");
        alert.setHeaderText(hotel.getHotelName());
        
        StringBuilder details = new StringBuilder();
        details.append("Location: ").append(hotel.getLocation()).append("\n");
        details.append("Address: ").append(hotel.getAddress()).append("\n");
        details.append("Room Type: ").append(hotel.getRoomType()).append("\n");
        details.append("Price per Night: ").append(hotel.getFormattedPrice()).append("\n");
        details.append("Room Size: ").append(hotel.getRoomSize()).append("\n");
        details.append("Max Occupancy: ").append(hotel.getMaxOccupancy()).append(" person(s)\n");
        details.append("Available Rooms: ").append(hotel.getAvailableRooms()).append("/").append(hotel.getTotalRooms()).append("\n");
        details.append("Rating: ").append(hotel.getRating()).append("⭐\n");
        details.append("Check-in: ").append(hotel.getCheckInTime()).append("\n");
        details.append("Check-out: ").append(hotel.getCheckOutTime()).append("\n");
        if (hotel.getAmenities() != null && !hotel.getAmenities().isEmpty()) {
            details.append("\nAmenities:\n").append(hotel.getAmenities()).append("\n");
        }
        details.append("\nContact: ").append(hotel.getContactNumber()).append("\n");
        if (hotel.getEmail() != null && !hotel.getEmail().isEmpty()) {
            details.append("Email: ").append(hotel.getEmail()).append("\n");
        }
        
        alert.setContentText(details.toString());
        alert.setResizable(true);
        alert.getDialogPane().setPrefWidth(550);
        alert.showAndWait();
    }
    
    private void handleBookHotel(Hotel hotel) {
        if (hotel.getAvailableRooms() <= 0) {
            showWarning("No rooms available!");
            return;
        }
        
        Dialog<Booking> dialog = new Dialog<>();
        dialog.setTitle("Book Hotel");
        dialog.setHeaderText("Book: " + hotel.getHotelName());
        
        ButtonType bookButtonType = new ButtonType("Confirm Booking", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(bookButtonType, ButtonType.CANCEL);
        
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));
        
        DatePicker checkInPicker = new DatePicker(LocalDate.now());
        DatePicker checkOutPicker = new DatePicker(LocalDate.now().plusDays(1));
        TextField guestsField = new TextField("1");
        TextField roomsField = new TextField("1");
        
        grid.add(new Label("Check-in Date:"), 0, 0);
        grid.add(checkInPicker, 1, 0);
        grid.add(new Label("Check-out Date:"), 0, 1);
        grid.add(checkOutPicker, 1, 1);
        grid.add(new Label("Number of Guests:"), 0, 2);
        grid.add(guestsField, 1, 2);
        grid.add(new Label("Number of Rooms:"), 0, 3);
        grid.add(roomsField, 1, 3);
        
        dialog.getDialogPane().setContent(grid);
        
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == bookButtonType) {
                try {
                    int rooms = Integer.parseInt(roomsField.getText());
                    if (rooms > hotel.getAvailableRooms()) {
                        showError("Only " + hotel.getAvailableRooms() + " room(s) available!");
                        return null;
                    }
                    
                    long nights = java.time.temporal.ChronoUnit.DAYS.between(
                        checkInPicker.getValue(), checkOutPicker.getValue());
                    
                    if (nights <= 0) {
                        showError("Check-out must be after check-in!");
                        return null;
                    }
                    
                    Booking booking = new Booking();
                    booking.setUserId(currentUser.getId());
                    booking.setServiceType("HOTEL");
                    booking.setServiceId(hotel.getId());
                    booking.setProviderId(hotel.getProviderId());
                    booking.setBookingDate(LocalDate.now());
                    booking.setServiceDate(checkInPicker.getValue().atStartOfDay());
                    booking.setNumberOfPeople(Integer.parseInt(guestsField.getText()));
                    booking.setTotalAmount(hotel.getPricePerNight() * rooms * nights);
                    booking.setStatus("PENDING");
                    booking.setSpecialRequests("Rooms: " + rooms + ", Nights: " + nights);
                    return booking;
                } catch (Exception e) {
                    showError("Invalid input: " + e.getMessage());
                    return null;
                }
            }
            return null;
        });
        
        dialog.showAndWait().ifPresent(booking -> {
            if (bookingDAO.createBooking(booking)) {
                showSuccess("Hotel booked successfully!");
                loadBookings();
                loadHotels();
            } else {
                showError("Failed to book hotel");
            }
        });
    }
    
    // ==================== BLOOD DONOR METHODS ====================
    
    private void setupBloodDonorTable() {
        donorNameCol.setCellValueFactory(data -> data.getValue().donorNameProperty());
        donorBloodGroupCol.setCellValueFactory(data -> data.getValue().bloodGroupProperty());
        donorAgeCol.setCellValueFactory(data -> data.getValue().ageProperty().asString());
        donorLocationCol.setCellValueFactory(data -> data.getValue().locationProperty());
        donorContactCol.setCellValueFactory(data -> data.getValue().contactNumberProperty());
        donorEligibilityCol.setCellValueFactory(data -> 
            new SimpleStringProperty(data.getValue().isEligibleToDonate() ? "✓ Eligible Now" : "Not Eligible"));
        donorBloodBankCol.setCellValueFactory(data -> {
            try {
                ServiceProvider provider = providerDAO.getProviderById(data.getValue().getProviderId());
                return new SimpleStringProperty(provider != null ? provider.getProviderName() : "Unknown");
            } catch (Exception e) {
                return new SimpleStringProperty("Unknown");
            }
        });
        
        donorActionsCol.setCellFactory(param -> new TableCell<>() {
            private final Button viewBtn = new Button("👁️ Details");
            private final Button requestBtn = new Button("🩸 Request Blood");
            private final HBox buttonBox = new HBox(5);
            
            {
                viewBtn.setStyle("-fx-background-color: #dc2626; -fx-text-fill: white; -fx-cursor: hand; -fx-padding: 5 8;");
                requestBtn.setStyle("-fx-background-color: #059669; -fx-text-fill: white; -fx-cursor: hand; -fx-padding: 5 8;");
                
                buttonBox.getChildren().addAll(viewBtn, requestBtn);
                
                viewBtn.setOnAction(e -> {
                    BloodDonor donor = getTableView().getItems().get(getIndex());
                    showDonorDetails(donor);
                });
                
                requestBtn.setOnAction(e -> {
                    BloodDonor donor = getTableView().getItems().get(getIndex());
                    handleRequestBloodDonation(donor);
                });
            }
            
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : buttonBox);
            }
        });
        
        bloodDonorTable.setItems(donorList);
    }
    
    @FXML
    private void handleSearchBloodDonors() {
        String bloodGroup = bloodGroupCombo.getValue();
        String location = bloodDonorLocationField.getText().trim();
        
        if (bloodGroup == null || bloodGroup.equals("All")) {
            showWarning("Please select a blood group for emergency search!");
            return;
        }
        
        List<BloodDonor> results = donorDAO.searchDonors(bloodGroup, location);
        donorList.clear();
        donorList.addAll(results);
        
        long eligible = results.stream().filter(BloodDonor::isEligibleToDonate).count();
        showInfo("Found " + results.size() + " donor(s) - " + eligible + " eligible now!");
    }
    
    private void showDonorDetails(BloodDonor donor) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Donor Information");
        alert.setHeaderText("Blood Group: " + donor.getBloodGroup());
        
        StringBuilder details = new StringBuilder();
        details.append("🔒 SECURE BLOOD DONATION SYSTEM\n");
        details.append("==============================\n\n");
        
        details.append("Blood Group: ").append(donor.getBloodGroup()).append("\n");
        details.append("Age: ").append(donor.getAge()).append(" years\n");
        details.append("Gender: ").append(donor.getGender()).append("\n");
        details.append("Weight: ").append(donor.getWeight()).append(" kg\n");
        details.append("Location: ").append(donor.getLocation()).append("\n");
        
        details.append("\nEligibility Status: ").append(donor.isEligibleToDonate() ? "✓ ELIGIBLE TO DONATE" : "✗ NOT ELIGIBLE").append("\n");
        details.append("Total Donations: ").append(donor.getTotalDonations()).append("\n");
        if (donor.getLastDonationDate() != null) {
            details.append("Last Donation: ").append(donor.getLastDonationDate()).append("\n");
        } else {
            details.append("Last Donation: Never donated\n");
        }
        
        try {
            ServiceProvider provider = providerDAO.getProviderById(donor.getProviderId());
            if (provider != null) {
                details.append("\nBlood Bank: ").append(provider.getProviderName()).append("\n");
                details.append("Blood Bank Address: ").append(provider.getAddress()).append("\n");
            }
        } catch (Exception e) {
            // Ignore
        }
        
        details.append("\n🔒 PRIVACY PROTECTION:\n");
        details.append("• Personal contact details are protected\n");
        details.append("• Contact information will be shared only after blood bank approval\n");
        details.append("• This ensures donor privacy and security\n");
        
        details.append("\n📋 TO REQUEST BLOOD DONATION:\n");
        details.append("• Click 'Request Blood Donation' button\n");
        details.append("• Fill out the request form with patient details\n");
        details.append("• Blood bank will review and approve your request\n");
        details.append("• You will receive donor contact only after approval\n");
        
        alert.setContentText(details.toString());
        alert.setResizable(true);
        alert.getDialogPane().setPrefWidth(700);
        alert.showAndWait();
    }
    
    private void handleRequestBloodDonation(BloodDonor donor) {
        if (!donor.isEligibleToDonate()) {
            showWarning("This donor is not currently eligible to donate blood.");
            return;
        }
        
        Dialog<BloodRequest> dialog = new Dialog<>();
        dialog.setTitle("Blood Donation Request Form");
        dialog.setHeaderText("Complete Patient Details for Blood Request\nBlood Group: " + donor.getBloodGroup());
        
        ButtonType requestButtonType = new ButtonType("Submit Request", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(requestButtonType, ButtonType.CANCEL);
        
        // Create scrollable content
        ScrollPane scrollPane = new ScrollPane();
        GridPane grid = new GridPane();
        grid.setHgap(15);
        grid.setVgap(12);
        grid.setPadding(new Insets(20));
        
        // Patient Basic Information
        Label basicInfoLabel = new Label("👤 Patient Basic Information");
        basicInfoLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #dc2626;");
        grid.add(basicInfoLabel, 0, 0, 2, 1);
        
        TextField patientNameField = new TextField();
        TextField patientAgeField = new TextField();
        ComboBox<String> patientGenderCombo = new ComboBox<>();
        patientGenderCombo.getItems().addAll("Male", "Female", "Other");
        TextField patientPhoneField = new TextField();
        TextField patientEmailField = new TextField();
        TextArea patientAddressArea = new TextArea();
        patientAddressArea.setPrefRowCount(2);
        
        grid.add(new Label("Full Name *:"), 0, 1);
        grid.add(patientNameField, 1, 1);
        grid.add(new Label("Age *:"), 0, 2);
        grid.add(patientAgeField, 1, 2);
        grid.add(new Label("Gender *:"), 0, 3);
        grid.add(patientGenderCombo, 1, 3);
        grid.add(new Label("Phone Number *:"), 0, 4);
        grid.add(patientPhoneField, 1, 4);
        grid.add(new Label("Email:"), 0, 5);
        grid.add(patientEmailField, 1, 5);
        grid.add(new Label("Address *:"), 0, 6);
        grid.add(patientAddressArea, 1, 6);
        
        // Medical Information
        Label medicalInfoLabel = new Label("🏥 Medical Information");
        medicalInfoLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #dc2626;");
        grid.add(medicalInfoLabel, 0, 7, 2, 1);
        
        TextArea patientConditionArea = new TextArea();
        patientConditionArea.setPrefRowCount(3);
        TextArea medicalHistoryArea = new TextArea();
        medicalHistoryArea.setPrefRowCount(2);
        TextArea currentMedicationsArea = new TextArea();
        currentMedicationsArea.setPrefRowCount(2);
        
        grid.add(new Label("Current Medical Condition *:"), 0, 8);
        grid.add(patientConditionArea, 1, 8);
        grid.add(new Label("Medical History:"), 0, 9);
        grid.add(medicalHistoryArea, 1, 9);
        grid.add(new Label("Current Medications:"), 0, 10);
        grid.add(currentMedicationsArea, 1, 10);
        
        // Request Details
        Label requestInfoLabel = new Label("📋 Request Details");
        requestInfoLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #dc2626;");
        grid.add(requestInfoLabel, 0, 11, 2, 1);
        
        ComboBox<String> urgencyCombo = new ComboBox<>();
        urgencyCombo.getItems().addAll("CRITICAL", "HIGH", "MEDIUM", "LOW");
        urgencyCombo.setValue("MEDIUM");
        TextField contactField = new TextField(currentUser.getPhone());
        TextField locationField = new TextField();
        TextArea specialRequirementsArea = new TextArea();
        specialRequirementsArea.setPrefRowCount(2);
        
        grid.add(new Label("Urgency Level *:"), 0, 12);
        grid.add(urgencyCombo, 1, 12);
        grid.add(new Label("Your Contact Number *:"), 0, 13);
        grid.add(contactField, 1, 13);
        grid.add(new Label("Location *:"), 0, 14);
        grid.add(locationField, 1, 14);
        grid.add(new Label("Special Requirements:"), 0, 15);
        grid.add(specialRequirementsArea, 1, 15);
        
        // Add required field indicators
        Label requiredNote = new Label("* Required fields");
        requiredNote.setStyle("-fx-font-size: 12px; -fx-text-fill: #666;");
        grid.add(requiredNote, 0, 16, 2, 1);
        
        scrollPane.setContent(grid);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(500);
        dialog.getDialogPane().setContent(scrollPane);
        
        Platform.runLater(() -> patientNameField.requestFocus());
        
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == requestButtonType) {
                try {
                    // Validate required fields
                    if (patientNameField.getText().trim().isEmpty()) {
                        showError("Patient name is required!");
                        return null;
                    }
                    if (patientAgeField.getText().trim().isEmpty()) {
                        showError("Patient age is required!");
                        return null;
                    }
                    if (patientGenderCombo.getValue() == null) {
                        showError("Patient gender is required!");
                        return null;
                    }
                    if (patientPhoneField.getText().trim().isEmpty()) {
                        showError("Patient phone number is required!");
                        return null;
                    }
                    if (patientAddressArea.getText().trim().isEmpty()) {
                        showError("Patient address is required!");
                        return null;
                    }
                    if (patientConditionArea.getText().trim().isEmpty()) {
                        showError("Patient medical condition is required!");
                        return null;
                    }
                    if (contactField.getText().trim().isEmpty()) {
                        showError("Your contact number is required!");
                        return null;
                    }
                    if (locationField.getText().trim().isEmpty()) {
                        showError("Location is required!");
                        return null;
                    }
                    
                    BloodRequest request = new BloodRequest();
                    request.setRequesterId(currentUser.getId());
                    request.setDonorId(donor.getId());
                    request.setBloodBankId(donor.getProviderId());
                    request.setBloodGroup(donor.getBloodGroup());
                    request.setPatientName(patientNameField.getText().trim());
                    request.setPatientAge(patientAgeField.getText().trim());
                    request.setPatientGender(patientGenderCombo.getValue());
                    request.setPatientPhone(patientPhoneField.getText().trim());
                    request.setPatientEmail(patientEmailField.getText().trim());
                    request.setPatientAddress(patientAddressArea.getText().trim());
                    request.setPatientCondition(patientConditionArea.getText().trim());
                    request.setMedicalHistory(medicalHistoryArea.getText().trim());
                    request.setCurrentMedications(currentMedicationsArea.getText().trim());
                    request.setUrgency(urgencyCombo.getValue());
                    request.setContactNumber(contactField.getText().trim());
                    request.setLocation(locationField.getText().trim());
                    request.setSpecialRequirements(specialRequirementsArea.getText().trim());
                    request.setStatus("PENDING");
                    return request;
                } catch (Exception e) {
                    showError("Invalid input: " + e.getMessage());
                    return null;
                }
            }
            return null;
        });
        
        dialog.showAndWait().ifPresent(request -> {
            if (bloodRequestDAO.createBloodRequest(request)) {
                showSuccess("Blood donation request submitted successfully!\n\n" +
                           "Request Details:\n" +
                           "• Patient: " + request.getPatientName() + " (" + request.getPatientAge() + " years, " + request.getPatientGender() + ")\n" +
                           "• Blood Group: " + request.getBloodGroup() + "\n" +
                           "• Urgency: " + request.getUrgencyDisplay() + "\n" +
                           "• Status: " + request.getStatusDisplay() + "\n" +
                           "• Contact: " + request.getContactNumber() + "\n" +
                           "• Location: " + request.getLocation() + "\n\n" +
                           "The blood bank will review your request and contact you if approved.\n" +
                           "You will receive donor contact information only after approval.");
            } else {
                showError("Failed to submit blood donation request");
            }
        });
    }
    
    // ==================== BOOKING METHODS ====================
    
    private void setupBookingTable() {
        bookingIdCol.setCellValueFactory(data -> data.getValue().idProperty().asString());
        bookingServiceCol.setCellValueFactory(data -> data.getValue().serviceTypeProperty());
        bookingDetailsCol.setCellValueFactory(data -> {
            // Get service details based on type
            return new SimpleStringProperty("Booking #" + data.getValue().getId());
        });
        bookingDateCol.setCellValueFactory(data -> 
            new SimpleStringProperty(data.getValue().getBookingDate().toString()));
        bookingStatusCol.setCellValueFactory(data -> data.getValue().statusProperty());
        
        bookingActionsCol.setCellFactory(param -> new TableCell<>() {
            private final Button viewBtn = new Button("👁️ View");
            private final Button cancelBtn = new Button("❌ Cancel");
            private final HBox buttons = new HBox(5, viewBtn, cancelBtn);
            
            {
                viewBtn.setStyle("-fx-background-color: #3b82f6; -fx-text-fill: white; -fx-cursor: hand; -fx-padding: 5 10;");
                cancelBtn.setStyle("-fx-background-color: #ef4444; -fx-text-fill: white; -fx-cursor: hand; -fx-padding: 5 10;");
                buttons.setAlignment(Pos.CENTER);
                
                viewBtn.setOnAction(e -> {
                    Booking booking = getTableView().getItems().get(getIndex());
                    showBookingDetails(booking);
                });
                
                cancelBtn.setOnAction(e -> {
                    Booking booking = getTableView().getItems().get(getIndex());
                    handleCancelBooking(booking);
                });
            }
            
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    Booking booking = getTableView().getItems().get(getIndex());
                    cancelBtn.setDisable("CANCELLED".equals(booking.getStatus()));
                    setGraphic(buttons);
                }
            }
        });
        
        bookingTable.setItems(bookingList);
    }
    
    private void loadBookings() {
        bookingList.clear();
        bookingList.addAll(bookingDAO.getBookingsByUserId(currentUser.getId()));
    }
    
    @FXML
    private void handleRefreshBookings() {
        loadBookings();
        showInfo("Bookings refreshed!");
    }
    
    private void showBookingDetails(Booking booking) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Booking Details");
        alert.setHeaderText("Booking #" + booking.getId());
        
        StringBuilder details = new StringBuilder();
        details.append("Service Type: ").append(booking.getServiceType()).append("\n");
        details.append("Booking Date: ").append(booking.getBookingDate()).append("\n");
        details.append("Service Date: ").append(booking.getServiceDate()).append("\n");
        details.append("Number of People: ").append(booking.getNumberOfPeople()).append("\n");
        details.append("Total Amount: ").append(String.format("৳%.2f", booking.getTotalAmount())).append("\n");
        details.append("Status: ").append(booking.getStatus()).append("\n");
        if (booking.getSpecialRequests() != null && !booking.getSpecialRequests().isEmpty()) {
            details.append("Special Requests: ").append(booking.getSpecialRequests()).append("\n");
        }
        
        alert.setContentText(details.toString());
        alert.showAndWait();
    }
    
    private void handleCancelBooking(Booking booking) {
        if ("CANCELLED".equals(booking.getStatus())) {
            showWarning("Booking is already cancelled!");
            return;
        }
        
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Cancel Booking");
        confirm.setHeaderText("Cancel Booking #" + booking.getId() + "?");
        confirm.setContentText("Are you sure you want to cancel this booking?");
        
        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                booking.setStatus("CANCELLED");
                if (bookingDAO.updateBooking(booking)) {
                    showSuccess("Booking cancelled successfully!");
                    loadBookings();
                } else {
                    showError("Failed to cancel booking");
                }
            }
        });
    }
    
    // ==================== LOAD DATA METHODS ====================
    
    private void loadFilterOptions() {
        // Debug: Check database content
        System.out.println("🔍 Loading filter options...");
        int doctorCount = doctorDAO.getTotalDoctorCount();
        
        // Create sample data if database is empty
        if (doctorCount == 0) {
            System.out.println("🔍 No doctors found, creating sample data...");
            doctorDAO.createSampleDoctors();
        }
        
        // Blood groups
        bloodGroupCombo.getItems().addAll("All", "A+", "A-", "B+", "B-", "O+", "O-", "AB+", "AB-");
        bloodGroupCombo.setValue("All");
        
        // Doctor specialties
        specialtyCombo.getItems().add("All");
        specialtyCombo.getItems().addAll(doctorDAO.getAllSpecialties());
        specialtyCombo.setValue("All");
        
        // Doctor locations
        doctorLocationCombo.getItems().add("All");
        doctorLocationCombo.getItems().addAll(doctorDAO.getAllDoctorLocations());
        doctorLocationCombo.setValue("All");
        
        // Test categories
        testCategoryCombo.getItems().add("All");
        testCategoryCombo.getItems().addAll(testDAO.getAllTestCategories());
        testCategoryCombo.setValue("All");
        
        // Test locations
        testLocationCombo.getItems().add("All");
        testLocationCombo.getItems().addAll(testDAO.getAllTestLocations());
        testLocationCombo.setValue("All");
        
        // Room types
        roomTypeCombo.getItems().addAll("All", "Single", "Double", "Deluxe", "Suite", "Family");
        roomTypeCombo.setValue("All");
        
        // Sort options
        sortByCombo.getItems().addAll("Name", "Price (Low to High)", "Price (High to Low)", "Rating");
        sortByCombo.setValue("Name");
    }
    
    private void loadAllData() {
        loadBuses();
        loadDoctors();
        loadTests();
        loadHotels();
        loadBookings();
    }
    
    private void loadBuses() {
        busList.clear();
        busList.addAll(busDAO.getAllBuses());
    }
    
    private void loadDoctors() {
        doctorList.clear();
        doctorList.addAll(doctorDAO.searchDoctors(null, null, null, null));
    }
    
    private void loadTests() {
        testList.clear();
        testList.addAll(testDAO.searchTests("", "", "", null));
    }
    
    private void loadHotels() {
        hotelList.clear();
        hotelList.addAll(hotelDAO.searchHotels(null, null, null, null));
    }
    
    // ==================== UTILITY METHODS ====================
    
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
    
    private void showSuccess(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    private void showInfo(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    private void showWarning(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
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

