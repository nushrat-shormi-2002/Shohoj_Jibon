package com.threelegend.shohojjibon.controller;

import com.threelegend.shohojjibon.dao.BloodDonorDAO;
import com.threelegend.shohojjibon.dao.BloodRequestDAO;
import com.threelegend.shohojjibon.dao.UserDAO;
import com.threelegend.shohojjibon.model.BloodDonor;
import com.threelegend.shohojjibon.model.BloodRequest;
import com.threelegend.shohojjibon.model.ServiceProvider;
import com.threelegend.shohojjibon.model.User;
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

import java.time.LocalDate;
import java.util.List;

/**
 * Blood Bank Provider Dashboard Controller
 * Manages blood donors for emergency situations
 * Shohoj Jibon - Makes your life easier
 * Developed by Three_Legend
 */
public class BloodBankController {
    
    // FXML Components - Overview
    @FXML private Label welcomeLabel;
    @FXML private Label bloodBankInfoLabel;
    @FXML private Label totalDonorsLabel;
    @FXML private Label availableDonorsLabel;
    @FXML private Label totalDonationsLabel;
    @FXML private GridPane bloodGroupGrid;
    
    // FXML Components - Donors
    @FXML private TableView<BloodDonor> donorTable;
    @FXML private TableColumn<BloodDonor, String> donorNameCol;
    @FXML private TableColumn<BloodDonor, String> bloodGroupCol;
    @FXML private TableColumn<BloodDonor, String> ageCol;
    @FXML private TableColumn<BloodDonor, String> genderCol;
    @FXML private TableColumn<BloodDonor, String> contactCol;
    @FXML private TableColumn<BloodDonor, String> locationCol;
    @FXML private TableColumn<BloodDonor, String> lastDonationCol;
    @FXML private TableColumn<BloodDonor, String> eligibilityCol;
    @FXML private TableColumn<BloodDonor, String> statusCol;
    @FXML private TableColumn<BloodDonor, Void> actionsCol;
    
    // FXML Components - Blood Requests
    @FXML private TableView<BloodRequest> requestTable;
    @FXML private TableColumn<BloodRequest, String> requestIdCol;
    @FXML private TableColumn<BloodRequest, String> requesterNameCol;
    @FXML private TableColumn<BloodRequest, String> patientNameCol;
    @FXML private TableColumn<BloodRequest, String> requestBloodGroupCol;
    @FXML private TableColumn<BloodRequest, String> urgencyCol;
    @FXML private TableColumn<BloodRequest, String> requestContactCol;
    @FXML private TableColumn<BloodRequest, String> requestLocationCol;
    @FXML private TableColumn<BloodRequest, String> requestDateCol;
    @FXML private TableColumn<BloodRequest, String> requestStatusCol;
    @FXML private TableColumn<BloodRequest, Void> requestActionsCol;
    
    // FXML Components - Statistics
    @FXML private Label totalRequestsLabel;
    @FXML private Label pendingRequestsLabel;
    @FXML private Label approvedRequestsLabel;
    @FXML private Label completedRequestsLabel;
    
    // Data
    private ObservableList<BloodDonor> donorList = FXCollections.observableArrayList();
    private ObservableList<BloodRequest> requestList = FXCollections.observableArrayList();
    private ServiceProvider currentProvider;
    
    // DAOs
    private final BloodDonorDAO donorDAO = new BloodDonorDAO();
    private final BloodRequestDAO bloodRequestDAO = new BloodRequestDAO();
    private final UserDAO userDAO = new UserDAO();
    
    // Blood groups
    private final String[] BLOOD_GROUPS = {"A+", "A-", "B+", "B-", "O+", "O-", "AB+", "AB-"};
    
    @FXML
    public void initialize() {
        currentProvider = SessionManager.getInstance().getCurrentProvider();
        
        // Set welcome message
        welcomeLabel.setText("Welcome, " + currentProvider.getProviderName() + "!");
        bloodBankInfoLabel.setText("Address: " + currentProvider.getAddress() + " | Saving Lives Every Day");
        
        // Setup tables
        setupDonorTable();
        setupRequestTable();
        
        // Load data
        loadAllData();
        
        // Setup blood group statistics
        setupBloodGroupStats();
    }
    
    /**
     * Setup Donor Table
     */
    private void setupDonorTable() {
        donorNameCol.setCellValueFactory(data -> data.getValue().donorNameProperty());
        bloodGroupCol.setCellValueFactory(data -> data.getValue().bloodGroupProperty());
        ageCol.setCellValueFactory(data -> data.getValue().ageProperty().asString());
        genderCol.setCellValueFactory(data -> data.getValue().genderProperty());
        contactCol.setCellValueFactory(data -> data.getValue().contactNumberProperty());
        locationCol.setCellValueFactory(data -> data.getValue().locationProperty());
        lastDonationCol.setCellValueFactory(data -> {
            LocalDate lastDonation = data.getValue().getLastDonationDate();
            return new SimpleStringProperty(lastDonation != null ? lastDonation.toString() : "Never");
        });
        eligibilityCol.setCellValueFactory(data -> 
            new SimpleStringProperty(data.getValue().isEligibleToDonate() ? "✓ Eligible" : "Not Eligible"));
        statusCol.setCellValueFactory(data -> 
            new SimpleStringProperty(data.getValue().isAvailable() ? "✓ Available" : "✗ Unavailable"));
        
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
                    BloodDonor donor = getTableView().getItems().get(getIndex());
                    handleEditDonor(donor);
                });
                
                toggleBtn.setOnAction(e -> {
                    BloodDonor donor = getTableView().getItems().get(getIndex());
                    handleToggleDonorStatus(donor);
                });
                
                deleteBtn.setOnAction(e -> {
                    BloodDonor donor = getTableView().getItems().get(getIndex());
                    handleDeleteDonor(donor);
                });
            }
            
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : buttons);
            }
        });
        
        donorTable.setItems(donorList);
    }
    
    /**
     * Setup Blood Request Table
     */
    private void setupRequestTable() {
        requestIdCol.setCellValueFactory(data -> data.getValue().idProperty().asString());
        
        requesterNameCol.setCellValueFactory(data -> {
            try {
                User requester = userDAO.findById(data.getValue().getRequesterId());
                return new SimpleStringProperty(requester != null ? requester.getFullName() : "Unknown");
            } catch (Exception e) {
                return new SimpleStringProperty("Unknown");
            }
        });
        
        patientNameCol.setCellValueFactory(data -> data.getValue().patientNameProperty());
        requestBloodGroupCol.setCellValueFactory(data -> data.getValue().bloodGroupProperty());
        urgencyCol.setCellValueFactory(data -> data.getValue().urgencyDisplayProperty());
        requestContactCol.setCellValueFactory(data -> data.getValue().contactNumberProperty());
        requestLocationCol.setCellValueFactory(data -> data.getValue().locationProperty());
        
        requestDateCol.setCellValueFactory(data -> {
            if (data.getValue().getRequestDate() != null) {
                return new SimpleStringProperty(data.getValue().getRequestDate().toLocalDate().toString());
            } else {
                return new SimpleStringProperty("N/A");
            }
        });
        
        requestStatusCol.setCellValueFactory(data -> data.getValue().statusDisplayProperty());
        
        // Actions column
        requestActionsCol.setCellFactory(param -> new TableCell<>() {
            private final Button approveBtn = new Button("✅ Approve");
            private final Button rejectBtn = new Button("❌ Reject");
            private final Button completeBtn = new Button("🎉 Complete");
            private final Button viewBtn = new Button("👁️ View");
            private final HBox buttons = new HBox(5);
            
            {
                approveBtn.setStyle("-fx-background-color: #10b981; -fx-text-fill: white; -fx-cursor: hand; -fx-padding: 5 8;");
                rejectBtn.setStyle("-fx-background-color: #ef4444; -fx-text-fill: white; -fx-cursor: hand; -fx-padding: 5 8;");
                completeBtn.setStyle("-fx-background-color: #3b82f6; -fx-text-fill: white; -fx-cursor: hand; -fx-padding: 5 8;");
                viewBtn.setStyle("-fx-background-color: #6b7280; -fx-text-fill: white; -fx-cursor: hand; -fx-padding: 5 8;");
                buttons.setAlignment(Pos.CENTER);
                
                approveBtn.setOnAction(e -> {
                    BloodRequest request = getTableView().getItems().get(getIndex());
                    handleApproveRequest(request);
                });
                
                rejectBtn.setOnAction(e -> {
                    BloodRequest request = getTableView().getItems().get(getIndex());
                    handleRejectRequest(request);
                });
                
                completeBtn.setOnAction(e -> {
                    BloodRequest request = getTableView().getItems().get(getIndex());
                    handleCompleteRequest(request);
                });
                
                viewBtn.setOnAction(e -> {
                    BloodRequest request = getTableView().getItems().get(getIndex());
                    handleViewRequest(request);
                });
            }
            
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    BloodRequest request = getTableView().getItems().get(getIndex());
                    buttons.getChildren().clear();
                    
                    if (request.isPending()) {
                        buttons.getChildren().addAll(approveBtn, rejectBtn, viewBtn);
                    } else if (request.isApproved()) {
                        buttons.getChildren().addAll(completeBtn, viewBtn);
                    } else {
                        buttons.getChildren().add(viewBtn);
                    }
                    
                    setGraphic(buttons);
                }
            }
        });
        
        requestTable.setItems(requestList);
    }
    
    /**
     * Setup blood group statistics
     */
    private void setupBloodGroupStats() {
        bloodGroupGrid.getChildren().clear();
        
        int col = 0;
        int row = 0;
        
        for (String bloodGroup : BLOOD_GROUPS) {
            VBox card = new VBox(5);
            card.setAlignment(Pos.CENTER);
            card.setStyle("-fx-background-color: #fef2f2; -fx-padding: 15; -fx-background-radius: 8; -fx-border-color: #dc2626; -fx-border-width: 2; -fx-border-radius: 8;");
            card.setPrefWidth(120);
            
            Label bgLabel = new Label(bloodGroup);
            bgLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #dc2626;");
            
            Label countLabel = new Label("0 donors");
            countLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #666;");
            countLabel.setId("count_" + bloodGroup.replace("+", "pos").replace("-", "neg"));
            
            card.getChildren().addAll(bgLabel, countLabel);
            bloodGroupGrid.add(card, col, row);
            
            col++;
            if (col > 3) {
                col = 0;
                row++;
            }
        }
    }
    
    /**
     * Update blood group statistics
     */
    private void updateBloodGroupStats() {
        for (String bloodGroup : BLOOD_GROUPS) {
            int count = (int) donorList.stream()
                .filter(d -> d.getBloodGroup().equals(bloodGroup) && d.isEligibleToDonate())
                .count();
            
            String id = "count_" + bloodGroup.replace("+", "pos").replace("-", "neg");
            Label label = (Label) bloodGroupGrid.lookup("#" + id);
            if (label != null) {
                label.setText(count + " eligible");
            }
        }
    }
    
    /**
     * Load all data
     */
    private void loadAllData() {
        loadDonors();
        loadBloodRequests();
        updateStatistics();
        updateBloodGroupStats();
    }
    
    private void loadDonors() {
        donorList.clear();
        List<BloodDonor> donors = donorDAO.getDonorsByProvider(currentProvider.getId());
        donorList.addAll(donors);
    }
    
    private void loadBloodRequests() {
        requestList.clear();
        List<BloodRequest> requests = bloodRequestDAO.getBloodRequestsByBloodBankId(currentProvider.getId());
        requestList.addAll(requests);
    }
    
    private void updateStatistics() {
        totalDonorsLabel.setText(String.valueOf(donorList.size()));
        
        long available = donorList.stream().filter(BloodDonor::isEligibleToDonate).count();
        availableDonorsLabel.setText(String.valueOf(available));
        
        int totalDonations = donorList.stream().mapToInt(BloodDonor::getTotalDonations).sum();
        totalDonationsLabel.setText(String.valueOf(totalDonations));
    }
    
    /**
     * Handle Add Donor
     */
    @FXML
    private void handleAddDonor() {
        showDonorDialog(null);
    }
    
    private void handleEditDonor(BloodDonor donor) {
        showDonorDialog(donor);
    }
    
    private void showDonorDialog(BloodDonor existingDonor) {
        boolean isEdit = existingDonor != null;
        
        Dialog<BloodDonor> dialog = new Dialog<>();
        dialog.setTitle(isEdit ? "Edit Donor" : "Register New Donor");
        dialog.setHeaderText(isEdit ? "Update donor information" : "Enter donor details");
        
        ButtonType saveButtonType = new ButtonType(isEdit ? "Update" : "Register", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);
        
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));
        
        // Fields
        TextField nameField = new TextField();
        nameField.setPromptText("Donor Name");
        
        ComboBox<String> bloodGroupCombo = new ComboBox<>();
        bloodGroupCombo.getItems().addAll(BLOOD_GROUPS);
        bloodGroupCombo.setPromptText("Select Blood Group");
        
        TextField ageField = new TextField();
        ageField.setPromptText("Age (18-60)");
        
        ComboBox<String> genderCombo = new ComboBox<>();
        genderCombo.getItems().addAll("Male", "Female", "Other");
        genderCombo.setPromptText("Gender");
        
        TextField contactField = new TextField();
        contactField.setPromptText("Contact Number");
        
        TextField emailField = new TextField();
        emailField.setPromptText("Email");
        
        TextField locationField = new TextField();
        locationField.setPromptText("Location");
        
        TextArea addressField = new TextArea();
        addressField.setPromptText("Full Address");
        addressField.setPrefRowCount(2);
        
        DatePicker lastDonationPicker = new DatePicker();
        lastDonationPicker.setPromptText("Last Donation Date (optional)");
        
        TextField weightField = new TextField();
        weightField.setPromptText("Weight in kg (min 50)");
        
        TextField emergencyContactField = new TextField();
        emergencyContactField.setPromptText("Emergency Contact");
        
        TextArea medicalHistoryField = new TextArea();
        medicalHistoryField.setPromptText("Medical History (optional)");
        medicalHistoryField.setPrefRowCount(2);
        
        CheckBox hasDiseaseCheck = new CheckBox("Has chronic disease (makes ineligible)");
        CheckBox isAvailableCheck = new CheckBox("Currently available");
        isAvailableCheck.setSelected(true);
        
        // Pre-fill if editing
        if (isEdit) {
            nameField.setText(existingDonor.getDonorName());
            bloodGroupCombo.setValue(existingDonor.getBloodGroup());
            ageField.setText(String.valueOf(existingDonor.getAge()));
            genderCombo.setValue(existingDonor.getGender());
            contactField.setText(existingDonor.getContactNumber());
            emailField.setText(existingDonor.getEmail());
            locationField.setText(existingDonor.getLocation());
            addressField.setText(existingDonor.getAddress());
            lastDonationPicker.setValue(existingDonor.getLastDonationDate());
            weightField.setText(String.valueOf(existingDonor.getWeight()));
            emergencyContactField.setText(existingDonor.getEmergencyContact());
            medicalHistoryField.setText(existingDonor.getMedicalHistory());
            hasDiseaseCheck.setSelected(existingDonor.hasDisease());
            isAvailableCheck.setSelected(existingDonor.isAvailable());
        }
        
        grid.add(new Label("Name *:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Blood Group *:"), 0, 1);
        grid.add(bloodGroupCombo, 1, 1);
        grid.add(new Label("Age *:"), 0, 2);
        grid.add(ageField, 1, 2);
        grid.add(new Label("Gender *:"), 0, 3);
        grid.add(genderCombo, 1, 3);
        grid.add(new Label("Contact *:"), 0, 4);
        grid.add(contactField, 1, 4);
        grid.add(new Label("Email:"), 0, 5);
        grid.add(emailField, 1, 5);
        grid.add(new Label("Location *:"), 0, 6);
        grid.add(locationField, 1, 6);
        grid.add(new Label("Address:"), 0, 7);
        grid.add(addressField, 1, 7);
        grid.add(new Label("Weight (kg) *:"), 0, 8);
        grid.add(weightField, 1, 8);
        grid.add(new Label("Last Donation:"), 0, 9);
        grid.add(lastDonationPicker, 1, 9);
        grid.add(new Label("Emergency Contact:"), 0, 10);
        grid.add(emergencyContactField, 1, 10);
        grid.add(new Label("Medical History:"), 0, 11);
        grid.add(medicalHistoryField, 1, 11);
        grid.add(hasDiseaseCheck, 0, 12, 2, 1);
        grid.add(isAvailableCheck, 0, 13, 2, 1);
        
        dialog.getDialogPane().setContent(grid);
        
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                try {
                    BloodDonor donor = isEdit ? existingDonor : new BloodDonor();
                    donor.setProviderId(currentProvider.getId());
                    donor.setDonorName(nameField.getText().trim());
                    donor.setBloodGroup(bloodGroupCombo.getValue());
                    donor.setAge(Integer.parseInt(ageField.getText().trim()));
                    donor.setGender(genderCombo.getValue());
                    donor.setContactNumber(contactField.getText().trim());
                    donor.setEmail(emailField.getText().trim());
                    donor.setLocation(locationField.getText().trim());
                    donor.setAddress(addressField.getText().trim());
                    donor.setLastDonationDate(lastDonationPicker.getValue());
                    donor.setWeight(Double.parseDouble(weightField.getText().trim()));
                    donor.setEmergencyContact(emergencyContactField.getText().trim());
                    donor.setMedicalHistory(medicalHistoryField.getText().trim());
                    donor.setHasDisease(hasDiseaseCheck.isSelected());
                    donor.setAvailable(isAvailableCheck.isSelected());
                    
                    if (!isEdit) {
                        donor.setTotalDonations(0);
                    }
                    
                    return donor;
                } catch (Exception e) {
                    showError("Invalid input. Please check all fields.");
                    return null;
                }
            }
            return null;
        });
        
        dialog.showAndWait().ifPresent(donor -> {
            if (isEdit) {
                if (donorDAO.updateDonor(donor)) {
                    showInfo("Donor updated successfully!");
                    loadAllData();
                } else {
                    showError("Failed to update donor.");
                }
            } else {
                if (donorDAO.createDonor(donor)) {
                    showInfo("Donor registered successfully!");
                    loadAllData();
                } else {
                    showError("Failed to register donor.");
                }
            }
        });
    }
    
    private void handleToggleDonorStatus(BloodDonor donor) {
        donor.setAvailable(!donor.isAvailable());
        if (donorDAO.updateDonor(donor)) {
            showInfo("Donor status updated!");
            loadAllData();
        } else {
            showError("Failed to update status.");
        }
    }
    
    private void handleDeleteDonor(BloodDonor donor) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Donor");
        alert.setHeaderText("Delete " + donor.getDonorName() + " from registry?");
        alert.setContentText("This action cannot be undone.");
        
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                if (donorDAO.deleteDonor(donor.getId())) {
                    showInfo("Donor deleted successfully!");
                    loadAllData();
                } else {
                    showError("Failed to delete donor.");
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
    private void handleRefreshDonors() {
        loadDonors();
        updateStatistics();
        updateBloodGroupStats();
        showInfo("Donors refreshed!");
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
    
    private void showSuccess(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    // ==================== BLOOD REQUEST HANDLERS ====================
    
    private void handleApproveRequest(BloodRequest request) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Approve Blood Request");
        dialog.setHeaderText("Approve Request #" + request.getId());
        dialog.setContentText("Add notes (optional):");
        
        dialog.showAndWait().ifPresent(notes -> {
            if (bloodRequestDAO.updateBloodRequestStatus(request.getId(), "APPROVED", notes)) {
                showSuccess("Blood request approved successfully!");
                loadBloodRequests();
                updateRequestStatistics();
            } else {
                showError("Failed to approve blood request");
            }
        });
    }
    
    private void handleRejectRequest(BloodRequest request) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Reject Blood Request");
        dialog.setHeaderText("Reject Request #" + request.getId());
        dialog.setContentText("Reason for rejection:");
        
        dialog.showAndWait().ifPresent(reason -> {
            if (bloodRequestDAO.updateBloodRequestStatus(request.getId(), "REJECTED", reason)) {
                showSuccess("Blood request rejected");
                loadBloodRequests();
                updateRequestStatistics();
            } else {
                showError("Failed to reject blood request");
            }
        });
    }
    
    private void handleCompleteRequest(BloodRequest request) {
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Complete Blood Request");
        confirmAlert.setHeaderText("Mark Request as Completed");
        confirmAlert.setContentText("Has the blood donation been completed successfully?");
        
        confirmAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                if (bloodRequestDAO.markBloodRequestCompleted(request.getId())) {
                    // Update donor information
                    updateDonorAfterDonation(request.getDonorId());
                    showSuccess("Blood request marked as completed!");
                    loadBloodRequests();
                    loadDonors(); // Refresh donor data
                    updateRequestStatistics();
                } else {
                    showError("Failed to complete blood request");
                }
            }
        });
    }
    
    private void handleViewRequest(BloodRequest request) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Blood Request Details");
        alert.setHeaderText("Request #" + request.getId() + " - " + request.getStatusDisplay());
        
        StringBuilder details = new StringBuilder();
        details.append("Patient Name: ").append(request.getPatientName()).append("\n");
        details.append("Blood Group: ").append(request.getBloodGroup()).append("\n");
        details.append("Urgency: ").append(request.getUrgencyDisplay()).append("\n");
        details.append("Contact: ").append(request.getContactNumber()).append("\n");
        details.append("Location: ").append(request.getLocation()).append("\n");
        details.append("Status: ").append(request.getStatusDisplay()).append("\n");
        details.append("Request Date: ").append(request.getRequestDate().toLocalDate()).append("\n");
        
        if (request.getPatientCondition() != null && !request.getPatientCondition().isEmpty()) {
            details.append("\nPatient Condition:\n").append(request.getPatientCondition()).append("\n");
        }
        
        if (request.getSpecialRequirements() != null && !request.getSpecialRequirements().isEmpty()) {
            details.append("\nSpecial Requirements:\n").append(request.getSpecialRequirements()).append("\n");
        }
        
        if (request.getBloodBankNotes() != null && !request.getBloodBankNotes().isEmpty()) {
            details.append("\nBlood Bank Notes:\n").append(request.getBloodBankNotes()).append("\n");
        }
        
        if (request.isApproved() || request.isCompleted()) {
            // Show donor contact information only for approved/completed requests
            try {
                BloodDonor donor = donorDAO.getDonorById(request.getDonorId());
                if (donor != null) {
                    details.append("\n🔒 DONOR CONTACT INFORMATION:\n");
                    details.append("Donor Name: ").append(donor.getDonorName()).append("\n");
                    details.append("Contact: ").append(donor.getContactNumber()).append("\n");
                    if (donor.getEmail() != null && !donor.getEmail().isEmpty()) {
                        details.append("Email: ").append(donor.getEmail()).append("\n");
                    }
                    details.append("Address: ").append(donor.getAddress()).append("\n");
                }
            } catch (Exception e) {
                details.append("\n⚠️ Could not retrieve donor contact information");
            }
        }
        
        alert.setContentText(details.toString());
        alert.setResizable(true);
        alert.getDialogPane().setPrefWidth(600);
        alert.showAndWait();
    }
    
    private void updateDonorAfterDonation(int donorId) {
        try {
            BloodDonor donor = donorDAO.getDonorById(donorId);
            if (donor != null) {
                // Update last donation date to today
                donor.setLastDonationDate(LocalDate.now());
                // Increment total donations
                donor.setTotalDonations(donor.getTotalDonations() + 1);
                // Update in database
                donorDAO.updateDonor(donor);
            }
        } catch (Exception e) {
            System.err.println("Failed to update donor after donation: " + e.getMessage());
        }
    }
    
    private void updateRequestStatistics() {
        if (totalRequestsLabel != null) {
            totalRequestsLabel.setText(String.valueOf(requestList.size()));
        }
        if (pendingRequestsLabel != null) {
            long pending = requestList.stream().filter(BloodRequest::isPending).count();
            pendingRequestsLabel.setText(String.valueOf(pending));
        }
        if (approvedRequestsLabel != null) {
            long approved = requestList.stream().filter(BloodRequest::isApproved).count();
            approvedRequestsLabel.setText(String.valueOf(approved));
        }
        if (completedRequestsLabel != null) {
            long completed = requestList.stream().filter(BloodRequest::isCompleted).count();
            completedRequestsLabel.setText(String.valueOf(completed));
        }
    }
    
    @FXML
    private void handleRefreshRequests() {
        loadBloodRequests();
        updateRequestStatistics();
        showSuccess("Blood requests refreshed successfully!");
    }
}

