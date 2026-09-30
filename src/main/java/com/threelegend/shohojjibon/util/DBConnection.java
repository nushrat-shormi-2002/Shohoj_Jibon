package com.threelegend.shohojjibon.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Database Connection Utility
 * Shohoj Jibon - Makes your life easier
 * Developed by Three_Legend
 * 
 * Manages database connections using Singleton pattern
 * Configured for XAMPP MySQL
 */
public class DBConnection {
    
    // XAMPP MySQL default configuration
    private static final String URL = "jdbc:mysql://localhost:3306/shohoj_jibon_db";
    private static final String USERNAME = "root";  // XAMPP default username
    private static final String PASSWORD = "root";      // XAMPP default password (empty)
    
    private static DBConnection instance;
    private Connection connection;
    
    /**
     * Private constructor to prevent instantiation
     */
    private DBConnection() {
        try {
            // Load MySQL JDBC Driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            System.out.println("✓ Database connection established successfully!");
        } catch (ClassNotFoundException e) {
            System.err.println("✗ MySQL JDBC Driver not found!");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("✗ Database connection failed!");
            System.err.println("  Make sure XAMPP MySQL is running and database 'shohoj_jibon_db' exists.");
            e.printStackTrace();
        }
    }
    
    /**
     * Get singleton instance of DBConnection
     * @return DBConnection instance
     */
    public static DBConnection getInstance() {
        if (instance == null) {
            synchronized (DBConnection.class) {
                if (instance == null) {
                    instance = new DBConnection();
                }
            }
        }
        return instance;
    }
    
    /**
     * Get database connection
     * @return Connection object
     * @throws SQLException if connection is closed or null
     */
    public Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                System.out.println("✓ Database reconnected successfully!");
            } catch (SQLException e) {
                System.err.println("✗ Failed to reconnect to database!");
                throw e;
            }
        }
        return connection;
    }
    
    /**
     * Test database connection
     * @return true if connection is successful, false otherwise
     */
    public static boolean testConnection() {
        try {
            Connection conn = getInstance().getConnection();
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            System.err.println("✗ Database connection test failed!");
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Close database connection
     */
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("✓ Database connection closed.");
            }
        } catch (SQLException e) {
            System.err.println("✗ Failed to close database connection!");
            e.printStackTrace();
        }
    }
    
    /**
     * Get database URL (for configuration display)
     * @return Database URL
     */
    public static String getDatabaseURL() {
        return URL;
    }
}

