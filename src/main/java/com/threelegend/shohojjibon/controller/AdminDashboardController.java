package com.threelegend.shohojjibon.controller;

import com.threelegend.shohojjibon.util.SessionManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

/**
 * Admin Dashboard Controller
 * Shohoj Jibon - Makes your life easier
 * Developed by Three_Legend
 */
public class AdminDashboardController {
    
    @FXML private Label welcomeLabel;
    @FXML private Button logoutButton;
    
    @FXML
    public void initialize() {
        String userName = SessionManager.getInstance().getCurrentUserFullName();
        welcomeLabel.setText("Welcome, " + userName + "!");
    }
    
    @FXML
    private void handleLogout(ActionEvent event) {
        SessionManager.getInstance().clearSession();
        loadLoginView();
    }
    
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
}

