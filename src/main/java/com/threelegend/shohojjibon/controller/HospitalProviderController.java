package com.threelegend.shohojjibon.controller;

import com.threelegend.shohojjibon.dao.DoctorDAO;
import com.threelegend.shohojjibon.dao.DiagnosticTestDAO;
import com.threelegend.shohojjibon.dao.BookingDAO;
import com.threelegend.shohojjibon.dao.UserDAO;
import com.threelegend.shohojjibon.model.Doctor;
import com.threelegend.shohojjibon.model.DiagnosticTest;
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

import java.util.List;
import java.time.LocalDateTime;

/**
 * Hospital Provider Dashboard Controller
 * Manages doctors and diagnostic tests
 * Shohoj Jibon - Makes your life easier
 * Developed by Three_Legend
 */
public class HospitalProviderController {
    
    // FXML Components - Overview
    @FXML private Label welcomeLabel;
    @FXML private Label hospitalInfoLabel;
    @FXML private Label totalDoctorsLabel;
    @FXML private Label totalTestsLabel;
    @FXML private Label totalRevenueLabel;
    
    // FXML Components - Doctors
    @FXML private TableView<Doctor> doctorTable;
    @FXML private TableColumn<Doctor, String> doctorNameCol;
    @FXML private TableColumn<Doctor, String> specialtyCol;
    @FXML private TableColumn<Doctor, String> qualificationsCol;
    @FXML private TableColumn<Doctor, String> experienceCol;
    @FXML private TableColumn<Doctor, String> feeCol;
    @FXML private TableColumn<Doctor, String> availabilityCol;
    @FXML private TableColumn<Doctor, String> doctorStatusCol;
    @FXML private TableColumn<Doctor, Void> doctorActionsCol;
    
    // FXML Components - Tests
    @FXML private TableView<DiagnosticTest> testTable;
    @FXML private TableColumn<DiagnosticTest, String> testNameCol;
    @FXML private TableColumn<DiagnosticTest, String> categoryCol;
    @FXML private TableColumn<DiagnosticTest, String> priceCol;
    @FXML private TableColumn<DiagnosticTest, String> resultTimeCol;
    @FXML private TableColumn<DiagnosticTest, String> homeCollectionCol;
    @FXML private TableColumn<DiagnosticTest, String> testStatusCol;
    @FXML private TableColumn<DiagnosticTest, Void> testActionsCol;
    
    // FXML Components - Bookings
    @FXML private TableView<Booking> bookingTable;
    @FXML private TableColumn<Booking, String> bookingIdCol;
    @FXML private TableColumn<Booking, String> patientNameCol;
    @FXML private TableColumn<Booking, String> bookingDoctorNameCol;
    @FXML private TableColumn<Booking, String> appointmentDateCol;
    @FXML private TableColumn<Booking, String> appointmentTimeCol;
    @FXML private TableColumn<Booking, String> symptomsCol;
    @FXML private TableColumn<Booking, String> bookingStatusCol;
    @FXML private TableColumn<Booking, String> totalAmountCol;
    @FXML private TableColumn<Booking, Void> bookingActionsCol;
    
    // FXML Components - Booking Statistics
    @FXML private Label totalBookingsLabel;
    @FXML private Label pendingBookingsLabel;
    @FXML private Label confirmedBookingsLabel;
    @FXML private Label bookingRevenueLabel;
    
    // Data
    private ObservableList<Doctor> doctorList = FXCollections.observableArrayList();
    private ObservableList<DiagnosticTest> testList = FXCollections.observableArrayList();
    private ObservableList<Booking> bookingList = FXCollections.observableArrayList();
    private ServiceProvider currentProvider;
    
    // DAOs
    private final DoctorDAO doctorDAO = new DoctorDAO();
    private final DiagnosticTestDAO testDAO = new DiagnosticTestDAO();
    private final BookingDAO bookingDAO = new BookingDAO();
    private final UserDAO userDAO = new UserDAO();
    
    @FXML
    public void initialize() {
        currentProvider = SessionManager.getInstance().getCurrentProvider();
        
        // Set welcome message
        welcomeLabel.setText("Welcome, " + currentProvider.getProviderName() + "!");
        hospitalInfoLabel.setText("Address: " + currentProvider.getAddress() + " | Authority: " + currentProvider.getAuthorityType());
        
        // Setup tables
        setupDoctorTable();
        setupTestTable();
        setupBookingTable();
        
        // Load data
        loadAllData();
    }
    
    /**
     * Setup Doctor Table
     */
    private void setupDoctorTable() {
        doctorNameCol.setCellValueFactory(data -> data.getValue().nameProperty());
        specialtyCol.setCellValueFactory(data -> data.getValue().specialtyProperty());
        qualificationsCol.setCellValueFactory(data -> data.getValue().qualificationsProperty());
        experienceCol.setCellValueFactory(data -> 
            new SimpleStringProperty(data.getValue().getExperienceYears() + " years"));
        feeCol.setCellValueFactory(data -> 
            new SimpleStringProperty(data.getValue().getFormattedFee()));
        availabilityCol.setCellValueFactory(data -> 
            new SimpleStringProperty(data.getValue().getAvailableDays()));
        doctorStatusCol.setCellValueFactory(data -> 
            new SimpleStringProperty(data.getValue().isAvailable() ? "✓ Active" : "✗ Inactive"));
        
        // Actions column
        doctorActionsCol.setCellFactory(param -> new TableCell<>() {
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
                    Doctor doctor = getTableView().getItems().get(getIndex());
                    handleEditDoctor(doctor);
                });
                
                toggleBtn.setOnAction(e -> {
                    Doctor doctor = getTableView().getItems().get(getIndex());
                    handleToggleDoctorStatus(doctor);
                });
                
                deleteBtn.setOnAction(e -> {
                    Doctor doctor = getTableView().getItems().get(getIndex());
                    handleDeleteDoctor(doctor);
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
    
    /**
     * Setup Test Table
     */
    private void setupTestTable() {
        testNameCol.setCellValueFactory(data -> data.getValue().testNameProperty());
        categoryCol.setCellValueFactory(data -> data.getValue().testCategoryProperty());
        priceCol.setCellValueFactory(data -> 
            new SimpleStringProperty(data.getValue().getFormattedPrice()));
        resultTimeCol.setCellValueFactory(data -> data.getValue().resultTimeProperty());
        homeCollectionCol.setCellValueFactory(data -> 
            new SimpleStringProperty(data.getValue().isHomeCollection() ? "✓ Available" : "✗ No"));
        testStatusCol.setCellValueFactory(data -> 
            new SimpleStringProperty(data.getValue().isAvailable() ? "✓ Active" : "✗ Inactive"));
        
        // Actions column
        testActionsCol.setCellFactory(param -> new TableCell<>() {
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
                    DiagnosticTest test = getTableView().getItems().get(getIndex());
                    handleEditTest(test);
                });
                
                toggleBtn.setOnAction(e -> {
                    DiagnosticTest test = getTableView().getItems().get(getIndex());
                    handleToggleTestStatus(test);
                });
                
                deleteBtn.setOnAction(e -> {
                    DiagnosticTest test = getTableView().getItems().get(getIndex());
                    handleDeleteTest(test);
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
    
    /**
     * Load all data
     */
    private void loadAllData() {
        loadDoctors();
        loadTests();
        loadBookings();
        updateStatistics();
    }
    
    private void loadDoctors() {
        doctorList.clear();
        List<Doctor> doctors = doctorDAO.getDoctorsByProvider(currentProvider.getId());
        doctorList.addAll(doctors);
    }
    
    private void loadTests() {
        testList.clear();
        List<DiagnosticTest> tests = testDAO.getTestsByProvider(currentProvider.getId());
        testList.addAll(tests);
    }
    
    private void updateStatistics() {
        totalDoctorsLabel.setText(String.valueOf(doctorList.size()));
        totalTestsLabel.setText(String.valueOf(testList.size()));
        // Revenue calculation would be from bookings
        totalRevenueLabel.setText("৳0.00");
    }
    
    /**
     * Handle Add Doctor
     */
    @FXML
    private void handleAddDoctor() {
        showDoctorDialog(null);
    }
    
    private void handleEditDoctor(Doctor doctor) {
        showDoctorDialog(doctor);
    }
    
    private void showDoctorDialog(Doctor existingDoctor) {
        boolean isEdit = existingDoctor != null;
        
        Dialog<Doctor> dialog = new Dialog<>();
        dialog.setTitle(isEdit ? "Edit Doctor" : "Add New Doctor");
        dialog.setHeaderText(isEdit ? "Update doctor information" : "Enter doctor details");
        
        ButtonType saveButtonType = new ButtonType(isEdit ? "Update" : "Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);
        
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));
        
        TextField nameField = new TextField();
        nameField.setPromptText("Dr. Name");
        TextField specialtyField = new TextField();
        specialtyField.setPromptText("e.g., Cardiology");
        TextField qualificationsField = new TextField();
        qualificationsField.setPromptText("e.g., MBBS, MD");
        TextField experienceField = new TextField();
        experienceField.setPromptText("Years");
        TextField locationField = new TextField();
        locationField.setPromptText("Location");
        TextField feeField = new TextField();
        feeField.setPromptText("Consultation Fee");
        TextField daysField = new TextField();
        daysField.setPromptText("e.g., Mon, Wed, Fri");
        TextField timeField = new TextField();
        timeField.setPromptText("e.g., 9:00 AM - 5:00 PM");
        TextField contactField = new TextField();
        contactField.setPromptText("Contact Number");
        TextField emailField = new TextField();
        emailField.setPromptText("Email");
        TextField languagesField = new TextField();
        languagesField.setPromptText("e.g., Bengali, English");
        
        if (isEdit) {
            nameField.setText(existingDoctor.getName());
            specialtyField.setText(existingDoctor.getSpecialty());
            qualificationsField.setText(existingDoctor.getQualifications());
            experienceField.setText(String.valueOf(existingDoctor.getExperienceYears()));
            locationField.setText(existingDoctor.getLocation());
            feeField.setText(String.valueOf(existingDoctor.getConsultationFee()));
            daysField.setText(existingDoctor.getAvailableDays());
            timeField.setText(existingDoctor.getAvailableTime());
            contactField.setText(existingDoctor.getContactNumber());
            emailField.setText(existingDoctor.getEmail());
            languagesField.setText(existingDoctor.getLanguagesSpoken());
        }
        
        grid.add(new Label("Name *:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Specialty *:"), 0, 1);
        grid.add(specialtyField, 1, 1);
        grid.add(new Label("Qualifications:"), 0, 2);
        grid.add(qualificationsField, 1, 2);
        grid.add(new Label("Experience (years):"), 0, 3);
        grid.add(experienceField, 1, 3);
        grid.add(new Label("Location *:"), 0, 4);
        grid.add(locationField, 1, 4);
        grid.add(new Label("Consultation Fee *:"), 0, 5);
        grid.add(feeField, 1, 5);
        grid.add(new Label("Available Days:"), 0, 6);
        grid.add(daysField, 1, 6);
        grid.add(new Label("Available Time:"), 0, 7);
        grid.add(timeField, 1, 7);
        grid.add(new Label("Contact Number:"), 0, 8);
        grid.add(contactField, 1, 8);
        grid.add(new Label("Email:"), 0, 9);
        grid.add(emailField, 1, 9);
        grid.add(new Label("Languages:"), 0, 10);
        grid.add(languagesField, 1, 10);
        
        dialog.getDialogPane().setContent(grid);
        
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                try {
                    Doctor doctor = isEdit ? existingDoctor : new Doctor();
                    doctor.setProviderId(currentProvider.getId());
                    doctor.setName(nameField.getText().trim());
                    doctor.setSpecialty(specialtyField.getText().trim());
                    doctor.setQualifications(qualificationsField.getText().trim());
                    doctor.setExperienceYears(Integer.parseInt(experienceField.getText().trim()));
                    doctor.setLocation(locationField.getText().trim());
                    doctor.setConsultationFee(Double.parseDouble(feeField.getText().trim()));
                    doctor.setAvailableDays(daysField.getText().trim());
                    doctor.setAvailableTime(timeField.getText().trim());
                    doctor.setContactNumber(contactField.getText().trim());
                    doctor.setEmail(emailField.getText().trim());
                    doctor.setLanguagesSpoken(languagesField.getText().trim());
                    return doctor;
                } catch (Exception e) {
                    showError("Invalid input. Please check all fields.");
                    return null;
                }
            }
            return null;
        });
        
        dialog.showAndWait().ifPresent(doctor -> {
            if (isEdit) {
                if (doctorDAO.updateDoctor(doctor)) {
                    showInfo("Doctor updated successfully!");
                    loadAllData();
                } else {
                    showError("Failed to update doctor.");
                }
            } else {
                if (doctorDAO.createDoctor(doctor)) {
                    showInfo("Doctor added successfully!");
                    loadAllData();
                } else {
                    showError("Failed to add doctor.");
                }
            }
        });
    }
    
    private void handleToggleDoctorStatus(Doctor doctor) {
        doctor.setAvailable(!doctor.isAvailable());
        if (doctorDAO.updateDoctor(doctor)) {
            showInfo("Doctor status updated!");
            loadAllData();
        } else {
            showError("Failed to update status.");
        }
    }
    
    private void handleDeleteDoctor(Doctor doctor) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Doctor");
        alert.setHeaderText("Delete " + doctor.getName() + "?");
        alert.setContentText("This action cannot be undone.");
        
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                if (doctorDAO.deleteDoctor(doctor.getId())) {
                    showInfo("Doctor deleted successfully!");
                    loadAllData();
                } else {
                    showError("Failed to delete doctor.");
                }
            }
        });
    }
    
    /**
     * Handle Add Test
     */
    @FXML
    private void handleAddTest() {
        showTestDialog(null);
    }
    
    private void handleEditTest(DiagnosticTest test) {
        showTestDialog(test);
    }
    
    private void showTestDialog(DiagnosticTest existingTest) {
        boolean isEdit = existingTest != null;
        
        Dialog<DiagnosticTest> dialog = new Dialog<>();
        dialog.setTitle(isEdit ? "Edit Test" : "Add New Test");
        dialog.setHeaderText(isEdit ? "Update test information" : "Enter test details");
        
        ButtonType saveButtonType = new ButtonType(isEdit ? "Update" : "Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);
        
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));
        
        TextField nameField = new TextField();
        nameField.setPromptText("Test Name");
        TextField categoryField = new TextField();
        categoryField.setPromptText("e.g., Blood Test");
        TextField locationField = new TextField();
        locationField.setPromptText("Location");
        TextField priceField = new TextField();
        priceField.setPromptText("Price");
        TextArea descField = new TextArea();
        descField.setPromptText("Description");
        descField.setPrefRowCount(2);
        TextField prepField = new TextField();
        prepField.setPromptText("e.g., Fasting required");
        TextField resultTimeField = new TextField();
        resultTimeField.setPromptText("e.g., 24 hours");
        TextField sampleField = new TextField();
        sampleField.setPromptText("e.g., Blood");
        CheckBox homeCollectionCheck = new CheckBox("Home Collection Available");
        TextField homeChargeField = new TextField();
        homeChargeField.setPromptText("Home Collection Charge");
        
        if (isEdit) {
            nameField.setText(existingTest.getTestName());
            categoryField.setText(existingTest.getTestCategory());
            locationField.setText(existingTest.getLocation());
            priceField.setText(String.valueOf(existingTest.getPrice()));
            descField.setText(existingTest.getDescription());
            prepField.setText(existingTest.getPreparationRequired());
            resultTimeField.setText(existingTest.getResultTime());
            sampleField.setText(existingTest.getSampleType());
            homeCollectionCheck.setSelected(existingTest.isHomeCollection());
            homeChargeField.setText(String.valueOf(existingTest.getHomeCollectionCharge()));
        }
        
        grid.add(new Label("Test Name *:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Category *:"), 0, 1);
        grid.add(categoryField, 1, 1);
        grid.add(new Label("Location *:"), 0, 2);
        grid.add(locationField, 1, 2);
        grid.add(new Label("Price *:"), 0, 3);
        grid.add(priceField, 1, 3);
        grid.add(new Label("Description:"), 0, 4);
        grid.add(descField, 1, 4);
        grid.add(new Label("Preparation:"), 0, 5);
        grid.add(prepField, 1, 5);
        grid.add(new Label("Result Time:"), 0, 6);
        grid.add(resultTimeField, 1, 6);
        grid.add(new Label("Sample Type:"), 0, 7);
        grid.add(sampleField, 1, 7);
        grid.add(homeCollectionCheck, 0, 8, 2, 1);
        grid.add(new Label("Home Charge:"), 0, 9);
        grid.add(homeChargeField, 1, 9);
        
        dialog.getDialogPane().setContent(grid);
        
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                try {
                    DiagnosticTest test = isEdit ? existingTest : new DiagnosticTest();
                    test.setProviderId(currentProvider.getId());
                    test.setTestName(nameField.getText().trim());
                    test.setTestCategory(categoryField.getText().trim());
                    test.setLocation(locationField.getText().trim());
                    test.setPrice(Double.parseDouble(priceField.getText().trim()));
                    test.setDescription(descField.getText().trim());
                    test.setPreparationRequired(prepField.getText().trim());
                    test.setResultTime(resultTimeField.getText().trim());
                    test.setSampleType(sampleField.getText().trim());
                    test.setHomeCollection(homeCollectionCheck.isSelected());
                    test.setHomeCollectionCharge(Double.parseDouble(homeChargeField.getText().trim()));
                    return test;
                } catch (Exception e) {
                    showError("Invalid input. Please check all fields.");
                    return null;
                }
            }
            return null;
        });
        
        dialog.showAndWait().ifPresent(test -> {
            if (isEdit) {
                if (testDAO.updateTest(test)) {
                    showInfo("Test updated successfully!");
                    loadAllData();
                } else {
                    showError("Failed to update test.");
                }
            } else {
                if (testDAO.createTest(test)) {
                    showInfo("Test added successfully!");
                    loadAllData();
                } else {
                    showError("Failed to add test.");
                }
            }
        });
    }
    
    private void handleToggleTestStatus(DiagnosticTest test) {
        test.setAvailable(!test.isAvailable());
        if (testDAO.updateTest(test)) {
            showInfo("Test status updated!");
            loadAllData();
        } else {
            showError("Failed to update status.");
        }
    }
    
    private void handleDeleteTest(DiagnosticTest test) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Test");
        alert.setHeaderText("Delete " + test.getTestName() + "?");
        alert.setContentText("This action cannot be undone.");
        
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                if (testDAO.deleteTest(test.getId())) {
                    showInfo("Test deleted successfully!");
                    loadAllData();
                } else {
                    showError("Failed to delete test.");
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
    private void handleRefreshDoctors() {
        loadDoctors();
        updateStatistics();
        showInfo("Doctors refreshed!");
    }
    
    @FXML
    private void handleRefreshTests() {
        loadTests();
        updateStatistics();
        showInfo("Tests refreshed!");
    }
    
    /**
     * Setup Booking Table
     */
    private void setupBookingTable() {
        bookingIdCol.setCellValueFactory(data -> data.getValue().idProperty().asString());
        patientNameCol.setCellValueFactory(data -> {
            try {
                User user = userDAO.findById(data.getValue().getUserId());
                return new SimpleStringProperty(user != null ? user.getFullName() : "Unknown");
            } catch (Exception e) {
                return new SimpleStringProperty("Unknown");
            }
        });
        bookingDoctorNameCol.setCellValueFactory(data -> {
            try {
                String serviceType = data.getValue().getServiceType();
                if ("DOCTOR".equals(serviceType)) {
                    Doctor doctor = doctorDAO.getDoctorById(data.getValue().getServiceId());
                    return new SimpleStringProperty(doctor != null ? "Dr. " + doctor.getName() : "Unknown Doctor");
                } else if ("TEST".equals(serviceType)) {
                    DiagnosticTest test = testDAO.getTestById(data.getValue().getServiceId());
                    return new SimpleStringProperty(test != null ? test.getTestName() : "Unknown Test");
                } else {
                    return new SimpleStringProperty("N/A");
                }
            } catch (Exception e) {
                return new SimpleStringProperty("Unknown");
            }
        });
        appointmentDateCol.setCellValueFactory(data -> {
            LocalDateTime serviceDate = data.getValue().getServiceDate();
            if (serviceDate != null) {
                return new SimpleStringProperty(serviceDate.toLocalDate().toString());
            } else {
                return new SimpleStringProperty("N/A");
            }
        });
        appointmentTimeCol.setCellValueFactory(data -> {
            String specialRequests = data.getValue().getSpecialRequests();
            if (specialRequests != null && specialRequests.contains("Time:")) {
                String time = specialRequests.substring(specialRequests.indexOf("Time:") + 5);
                if (time.contains(",")) {
                    time = time.substring(0, time.indexOf(","));
                }
                return new SimpleStringProperty(time.trim());
            }
            return new SimpleStringProperty("N/A");
        });
        symptomsCol.setCellValueFactory(data -> {
            String specialRequests = data.getValue().getSpecialRequests();
            if (specialRequests != null && specialRequests.contains("Symptoms:")) {
                String symptoms = specialRequests.substring(specialRequests.indexOf("Symptoms:") + 9);
                if (symptoms.contains(",")) {
                    symptoms = symptoms.substring(0, symptoms.indexOf(","));
                }
                return new SimpleStringProperty(symptoms.trim());
            }
            return new SimpleStringProperty("N/A");
        });
        bookingStatusCol.setCellValueFactory(data -> data.getValue().statusProperty());
        totalAmountCol.setCellValueFactory(data -> 
            new SimpleStringProperty(String.format("৳%.2f", data.getValue().getTotalAmount())));
        
        // Actions column
        bookingActionsCol.setCellFactory(param -> new TableCell<>() {
            private final Button confirmBtn = new Button("✅ Confirm");
            private final Button cancelBtn = new Button("❌ Cancel");
            private final Button completeBtn = new Button("✅ Complete");
            private final Button viewBtn = new Button("👁️ View");
            private final HBox buttons = new HBox(5, confirmBtn, cancelBtn, completeBtn, viewBtn);
            
            {
                confirmBtn.setStyle("-fx-background-color: #10b981; -fx-text-fill: white; -fx-cursor: hand; -fx-padding: 3 8; -fx-font-size: 10px;");
                cancelBtn.setStyle("-fx-background-color: #ef4444; -fx-text-fill: white; -fx-cursor: hand; -fx-padding: 3 8; -fx-font-size: 10px;");
                completeBtn.setStyle("-fx-background-color: #3b82f6; -fx-text-fill: white; -fx-cursor: hand; -fx-padding: 3 8; -fx-font-size: 10px;");
                viewBtn.setStyle("-fx-background-color: #6b7280; -fx-text-fill: white; -fx-cursor: hand; -fx-padding: 3 8; -fx-font-size: 10px;");
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
     * Load bookings for this hospital
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
        int totalBookings = bookingList.size();
        int pendingBookings = (int) bookingList.stream().filter(b -> "PENDING".equals(b.getStatus())).count();
        int confirmedBookings = (int) bookingList.stream().filter(b -> "CONFIRMED".equals(b.getStatus())).count();
        
        double revenue = bookingList.stream()
            .filter(b -> "CONFIRMED".equals(b.getStatus()) || "COMPLETED".equals(b.getStatus()))
            .mapToDouble(Booking::getTotalAmount)
            .sum();
        
        totalBookingsLabel.setText(String.valueOf(totalBookings));
        pendingBookingsLabel.setText(String.valueOf(pendingBookings));
        confirmedBookingsLabel.setText(String.valueOf(confirmedBookings));
        bookingRevenueLabel.setText(String.format("৳%.2f", revenue));
    }
    
    /**
     * Handle refresh bookings
     */
    @FXML
    private void handleRefreshBookings() {
        loadBookings();
        showInfo("Bookings refreshed!");
    }
    
    /**
     * Handle confirm booking
     */
    private void handleConfirmBooking(Booking booking) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirm Booking");
        confirm.setHeaderText("Confirm this appointment?");
        confirm.setContentText("This will confirm the appointment with the patient.");
        
        if (confirm.showAndWait().orElse(null) == ButtonType.OK) {
            booking.setStatus("CONFIRMED");
            if (bookingDAO.updateBooking(booking)) {
                showInfo("Appointment confirmed successfully!");
                loadBookings();
            } else {
                showError("Failed to confirm appointment");
            }
        }
    }
    
    /**
     * Handle cancel booking
     */
    private void handleCancelBooking(Booking booking) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Cancel Booking");
        confirm.setHeaderText("Cancel this appointment?");
        confirm.setContentText("This will cancel the appointment. The patient will be notified.");
        
        if (confirm.showAndWait().orElse(null) == ButtonType.OK) {
            booking.setStatus("CANCELLED");
            if (bookingDAO.updateBooking(booking)) {
                showInfo("Appointment cancelled successfully!");
                loadBookings();
            } else {
                showError("Failed to cancel appointment");
            }
        }
    }
    
    /**
     * Handle complete booking
     */
    private void handleCompleteBooking(Booking booking) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Complete Booking");
        confirm.setHeaderText("Mark appointment as completed?");
        confirm.setContentText("This will mark the appointment as completed.");
        
        if (confirm.showAndWait().orElse(null) == ButtonType.OK) {
            booking.setStatus("COMPLETED");
            if (bookingDAO.updateBooking(booking)) {
                showInfo("Appointment marked as completed!");
                loadBookings();
            } else {
                showError("Failed to complete appointment");
            }
        }
    }
    
    /**
     * Show booking details
     */
    private void showBookingDetails(Booking booking) {
        try {
            User user = userDAO.findById(booking.getUserId());
            Doctor doctor = doctorDAO.getDoctorById(booking.getServiceId());
            
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Booking Details");
            alert.setHeaderText("Booking #" + booking.getId());
            
            StringBuilder details = new StringBuilder();
            details.append("Patient: ").append(user != null ? user.getFullName() : "Unknown").append("\n");
            details.append("Contact: ").append(user != null ? user.getPhone() : "N/A").append("\n");
            details.append("Doctor: ").append(doctor != null ? "Dr. " + doctor.getName() : "Unknown").append("\n");
            details.append("Specialty: ").append(doctor != null ? doctor.getSpecialty() : "N/A").append("\n");
            details.append("Appointment Date: ").append(booking.getServiceDate().toLocalDate()).append("\n");
            details.append("Amount: ").append(String.format("৳%.2f", booking.getTotalAmount())).append("\n");
            details.append("Status: ").append(booking.getStatus()).append("\n");
            
            if (booking.getSpecialRequests() != null && !booking.getSpecialRequests().isEmpty()) {
                details.append("\nDetails:\n").append(booking.getSpecialRequests());
            }
            
            alert.setContentText(details.toString());
            alert.setResizable(true);
            alert.getDialogPane().setPrefWidth(500);
            alert.showAndWait();
        } catch (Exception e) {
            showError("Failed to load booking details: " + e.getMessage());
        }
    }
    
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

