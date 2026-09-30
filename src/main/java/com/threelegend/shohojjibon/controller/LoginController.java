package com.threelegend.shohojjibon.controller;

import com.threelegend.shohojjibon.dao.UserDAO;
import com.threelegend.shohojjibon.dao.ServiceProviderDAO;
import com.threelegend.shohojjibon.model.User;
import com.threelegend.shohojjibon.model.ServiceProvider;
import com.threelegend.shohojjibon.util.SessionManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

/**
 * Login Controller
 * Shohoj Jibon - Makes your life easier
 * Developed by Three_Legend
 */
public class LoginController {
    
    @FXML
    private TextField usernameField;
    
    @FXML
    private PasswordField passwordField;
    
    @FXML
    private Label errorLabel;
    
    @FXML
    private Button loginButton;
    
    private UserDAO userDAO = new UserDAO();
    private ServiceProviderDAO providerDAO = new ServiceProviderDAO();
    
    @FXML
    public void initialize() {
        errorLabel.setVisible(false);
    }
    
    @FXML
    private void handleLogin(ActionEvent event) {
        String username = usernameField.getText().trim();
        String password = passwordField.getText();
        
        // Validate input
        if (username.isEmpty() || password.isEmpty()) {
            showError("Please enter both username and password");
            return;
        }
        
        // Authenticate user
        User user = userDAO.authenticate(username, password);
        
        if (user != null) {
            // Set session
            SessionManager.getInstance().setCurrentUser(user);
            
            // If service provider, load provider info
            if ("SERVICE_PROVIDER".equals(user.getRole())) {
                ServiceProvider provider = providerDAO.findByUserId(user.getId());
                SessionManager.getInstance().setCurrentProvider(provider);
                
                // Load provider dashboard
                loadProviderDashboard();
            } else if ("ADMIN".equals(user.getRole())) {
                // Load admin dashboard
                loadAdminDashboard();
            } else {
                // Load user dashboard
                loadUserDashboard();
            }
        } else {
            showError("Invalid username or password");
        }
    }
    
    @FXML
    private void handleRegister(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/threelegend/shohojjibon/register.fxml"));
            Scene scene = new Scene(loader.load(), 900, 700);
            
            Stage stage = (Stage) loginButton.getScene().getWindow();
            stage.setTitle("Shohoj Jibon - Register");
            stage.setScene(scene);
        } catch (Exception e) {
            System.err.println("✗ Failed to load register view: " + e.getMessage());
            e.printStackTrace();
            showError("Failed to open registration page");
        }
    }
    
    private void loadUserDashboard() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/threelegend/shohojjibon/user-dashboard-new.fxml"));
            Scene scene = new Scene(loader.load(), 1400, 800);
            
            Stage stage = (Stage) loginButton.getScene().getWindow();
            stage.setTitle("Shohoj Jibon - All Services in One Place");
            stage.setScene(scene);
            stage.setMaximized(true);
            
            System.out.println("✓ User dashboard loaded for: " + SessionManager.getInstance().getCurrentUserFullName());
        } catch (Exception e) {
            System.err.println("✗ Failed to load user dashboard: " + e.getMessage());
            e.printStackTrace();
            showError("Failed to load dashboard");
        }
    }
    
    private void loadProviderDashboard() {
        try {
            ServiceProvider provider = SessionManager.getInstance().getCurrentProvider();
            String authorityType = provider.getAuthorityType();
            
            // Route to appropriate dashboard based on authority type
            String fxmlFile;
            if ("HOSPITAL".equalsIgnoreCase(authorityType)) {
                fxmlFile = "/com/threelegend/shohojjibon/hospital-provider-dashboard.fxml";
            } else if ("ACCOMMODATION".equalsIgnoreCase(authorityType)) {
                fxmlFile = "/com/threelegend/shohojjibon/hotel-provider-dashboard.fxml";
            } else if ("BLOOD_BANK".equalsIgnoreCase(authorityType)) {
                fxmlFile = "/com/threelegend/shohojjibon/bloodbank-provider-dashboard.fxml";
            } else {
                fxmlFile = "/com/threelegend/shohojjibon/provider-dashboard.fxml";
            }
            
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Scene scene = new Scene(loader.load(), 1200, 700);
            
            Stage stage = (Stage) loginButton.getScene().getWindow();
            stage.setTitle("Shohoj Jibon - Provider Dashboard");
            stage.setScene(scene);
            stage.setMaximized(true);
            
            System.out.println("✓ Provider dashboard loaded for: " + provider.getProviderName() + " (" + authorityType + ")");
        } catch (Exception e) {
            System.err.println("✗ Failed to load provider dashboard: " + e.getMessage());
            e.printStackTrace();
            showError("Failed to load dashboard");
        }
    }
    
    private void loadAdminDashboard() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/threelegend/shohojjibon/admin-dashboard.fxml"));
            Scene scene = new Scene(loader.load(), 1200, 700);
            
            Stage stage = (Stage) loginButton.getScene().getWindow();
            stage.setTitle("Shohoj Jibon - Admin Dashboard");
            stage.setScene(scene);
            stage.setMaximized(true);
            
            System.out.println("✓ Admin dashboard loaded");
        } catch (Exception e) {
            System.err.println("✗ Failed to load admin dashboard: " + e.getMessage());
            e.printStackTrace();
            showError("Failed to load dashboard");
        }
    }
    
    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }
}

