package com.threelegend.shohojjibon.controller;

import com.threelegend.shohojjibon.dao.UserDAO;
import com.threelegend.shohojjibon.dao.ServiceProviderDAO;
import com.threelegend.shohojjibon.model.User;
import com.threelegend.shohojjibon.model.ServiceProvider;
import com.threelegend.shohojjibon.util.PasswordUtil;
import com.threelegend.shohojjibon.util.ValidationUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Register Controller
 * Shohoj Jibon - Makes your life easier
 * Developed by Three_Legend
 */
public class RegisterController {
    
    @FXML private TextField usernameField;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private TextField fullNameField;
    @FXML private TextField phoneField;
    @FXML private TextArea addressArea;
    @FXML private RadioButton userRadio;
    @FXML private RadioButton providerRadio;
    @FXML private VBox providerFields;
    @FXML private TextField providerNameField;
    @FXML private CheckBox transportCheck;
    @FXML private CheckBox hospitalCheck;
    @FXML private CheckBox accommodationCheck;
    @FXML private CheckBox bloodBankCheck;
    @FXML private TextArea descriptionArea;
    @FXML private Label errorLabel;
    @FXML private Button registerButton;
    
    private UserDAO userDAO = new UserDAO();
    private ServiceProviderDAO providerDAO = new ServiceProviderDAO();
    
    @FXML
    public void initialize() {
        errorLabel.setVisible(false);
        providerFields.setVisible(false);
        providerFields.setManaged(false);
        
        // Toggle provider fields visibility
        userRadio.setSelected(true);
        userRadio.setOnAction(e -> {
            providerFields.setVisible(false);
            providerFields.setManaged(false);
        });
        
        providerRadio.setOnAction(e -> {
            providerFields.setVisible(true);
            providerFields.setManaged(true);
        });
    }
    
    @FXML
    private void handleRegister(ActionEvent event) {
        // Validate input
        if (!validateInput()) {
            return;
        }
        
        try {
            // Create user
            User user = new User();
            user.setUsername(usernameField.getText().trim().toLowerCase());
            user.setEmail(emailField.getText().trim());
            user.setPasswordHash(PasswordUtil.hashPassword(passwordField.getText()));
            user.setRole(providerRadio.isSelected() ? "SERVICE_PROVIDER" : "GENERAL_USER");
            user.setFullName(fullNameField.getText().trim());
            user.setPhone(phoneField.getText().trim());
            user.setAddress(addressArea.getText().trim());
            user.setActive(true);
            
            // Save user
            if (userDAO.createUser(user)) {
                // If service provider, create provider record
                if (providerRadio.isSelected()) {
                    ServiceProvider provider = new ServiceProvider();
                    provider.setUserId(user.getId());
                    provider.setProviderName(providerNameField.getText().trim());
                    provider.setAuthorityType(getSelectedAuthorities());
                    provider.setDescription(descriptionArea.getText().trim());
                    provider.setAddress(addressArea.getText().trim());
                    provider.setVerified(false);
                    
                    if (!providerDAO.createProvider(provider)) {
                        showError("Failed to create service provider record");
                        return;
                    }
                }
                
                // Show success and go back to login
                showSuccess();
            } else {
                showError("Failed to create account. Please try again.");
            }
        } catch (Exception e) {
            System.err.println("✗ Registration error: " + e.getMessage());
            e.printStackTrace();
            showError("An error occurred during registration");
        }
    }
    
    @FXML
    private void handleBackToLogin(ActionEvent event) {
        loadLoginView();
    }
    
    private boolean validateInput() {
        // Username validation
        String usernameError = ValidationUtil.getUsernameError(usernameField.getText());
        if (!usernameError.isEmpty()) {
            showError(usernameError);
            return false;
        }
        
        // Check if username exists
        if (userDAO.usernameExists(usernameField.getText().trim().toLowerCase())) {
            showError("Username already exists");
            return false;
        }
        
        // Email validation
        String emailError = ValidationUtil.getEmailError(emailField.getText());
        if (!emailError.isEmpty()) {
            showError(emailError);
            return false;
        }
        
        // Check if email exists
        if (userDAO.emailExists(emailField.getText().trim())) {
            showError("Email already exists");
            return false;
        }
        
        // Password validation
        String password = passwordField.getText();
        if (password.length() < 6) {
            showError("Password must be at least 6 characters");
            return false;
        }
        
        // Password confirmation
        if (!password.equals(confirmPasswordField.getText())) {
            showError("Passwords do not match");
            return false;
        }
        
        // Full name
        if (ValidationUtil.isEmpty(fullNameField.getText())) {
            showError("Full name is required");
            return false;
        }
        
        // Phone validation
        String phoneError = ValidationUtil.getPhoneError(phoneField.getText());
        if (!phoneError.isEmpty()) {
            showError(phoneError);
            return false;
        }
        
        // Provider-specific validation
        if (providerRadio.isSelected()) {
            if (ValidationUtil.isEmpty(providerNameField.getText())) {
                showError("Provider name is required");
                return false;
            }
            
            if (!transportCheck.isSelected() && !hospitalCheck.isSelected() && 
                !accommodationCheck.isSelected() && !bloodBankCheck.isSelected()) {
                showError("Please select at least one service authority");
                return false;
            }
        }
        
        return true;
    }
    
    private String getSelectedAuthorities() {
        StringBuilder authorities = new StringBuilder();
        
        if (transportCheck.isSelected()) authorities.append("TRANSPORT,");
        if (hospitalCheck.isSelected()) authorities.append("HOSPITAL,");
        if (accommodationCheck.isSelected()) authorities.append("ACCOMMODATION,");
        if (bloodBankCheck.isSelected()) authorities.append("BLOOD_BANK,");
        
        // Remove trailing comma
        if (authorities.length() > 0) {
            authorities.setLength(authorities.length() - 1);
        }
        
        return authorities.toString();
    }
    
    private void showSuccess() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Registration Successful");
        alert.setHeaderText("Account Created!");
        alert.setContentText("Your account has been created successfully. You can now login.");
        alert.showAndWait();
        
        loadLoginView();
    }
    
    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }
    
    private void loadLoginView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/threelegend/shohojjibon/login.fxml"));
            Scene scene = new Scene(loader.load(), 900, 600);
            
            Stage stage = (Stage) registerButton.getScene().getWindow();
            stage.setTitle("Shohoj Jibon - Login");
            stage.setScene(scene);
        } catch (Exception e) {
            System.err.println("✗ Failed to load login view: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

