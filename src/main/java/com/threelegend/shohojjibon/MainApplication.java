package com.threelegend.shohojjibon;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import com.threelegend.shohojjibon.util.DBConnection;

/**
 * Main Application Class
 * Shohoj Jibon - Makes your life easier
 * Developed by Three_Legend
 * 
 * Entry point for the JavaFX application
 */
public class MainApplication extends Application {
    
    @Override
    public void start(Stage stage) {
        try {
            // Test database connection
            if (!DBConnection.testConnection()) {
                System.err.println("═══════════════════════════════════════════════════════════");
                System.err.println("✗ DATABASE CONNECTION FAILED!");
                System.err.println("  Please make sure:");
                System.err.println("  1. XAMPP is running");
                System.err.println("  2. MySQL service is started");
                System.err.println("  3. Database 'shohoj_jibon_db' exists");
                System.err.println("  4. Run the database_schema.sql script");
                System.err.println("═══════════════════════════════════════════════════════════");
                System.exit(1);
            }
            
            System.out.println("═══════════════════════════════════════════════════════════");
            System.out.println("  Shohoj Jibon - Makes your life easier");
            System.out.println("  Developed by Three_Legend");
            System.out.println("═══════════════════════════════════════════════════════════");
            
            // Load login view
            FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("login.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 900, 600);
            
            // Set stage properties
            stage.setTitle("Shohoj Jibon - Login");
            stage.setScene(scene);
            stage.setMinWidth(900);
            stage.setMinHeight(600);
            
            // Optional: Set application icon (if you have one)
            // stage.getIcons().add(new Image(MainApplication.class.getResourceAsStream("icon.png")));
            
            stage.show();

            System.out.println("✓ Application started successfully!");
            System.out.println("═══════════════════════════════════════════════════════════\n");
            
        } catch (Exception e) {
            System.err.println("✗ Failed to start application!");
            e.printStackTrace();
        }
    }
    
    @Override
    public void stop() {
        // Close database connection when application closes
        DBConnection.getInstance().closeConnection();
        System.out.println("\n═══════════════════════════════════════════════════════════");
        System.out.println("  Application closed. Thank you for using Shohoj Jibon!");
        System.out.println("  Developed by Three_Legend");
        System.out.println("═══════════════════════════════════════════════════════════");
    }
    
    public static void main(String[] args) {
        launch();
    }
}

