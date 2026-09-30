package com.threelegend.shohojjibon.dao;

import com.threelegend.shohojjibon.model.DiagnosticTest;
import com.threelegend.shohojjibon.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Diagnostic Test Data Access Object
 * Shohoj Jibon - Makes your life easier
 * Developed by Three_Legend
 */
public class DiagnosticTestDAO {
    
    /**
     * Create a new diagnostic test
     */
    public boolean createTest(DiagnosticTest test) {
        String sql = "INSERT INTO diagnostic_tests (provider_id, test_name, test_category, location, price, " +
                    "description, preparation_required, result_time, sample_type, is_home_collection, " +
                    "home_collection_charge, is_available) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setInt(1, test.getProviderId());
            stmt.setString(2, test.getTestName());
            stmt.setString(3, test.getTestCategory());
            stmt.setString(4, test.getLocation());
            stmt.setDouble(5, test.getPrice());
            stmt.setString(6, test.getDescription());
            stmt.setString(7, test.getPreparationRequired());
            stmt.setString(8, test.getResultTime());
            stmt.setString(9, test.getSampleType());
            stmt.setBoolean(10, test.isHomeCollection());
            stmt.setDouble(11, test.getHomeCollectionCharge());
            stmt.setBoolean(12, test.isAvailable());
            
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    test.setId(rs.getInt(1));
                }
                System.out.println("✓ Test added: " + test.getTestName());
                return true;
            }
        } catch (SQLException e) {
            System.err.println("✗ Failed to create test: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Get all tests by provider
     */
    public List<DiagnosticTest> getTestsByProvider(int providerId) {
        List<DiagnosticTest> tests = new ArrayList<>();
        String sql = "SELECT * FROM diagnostic_tests WHERE provider_id = ? ORDER BY test_name";
        
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, providerId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                tests.add(extractTestFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("✗ Failed to get tests: " + e.getMessage());
        }
        return tests;
    }
    
    /**
     * Get test by ID
     */
    public DiagnosticTest getTestById(int testId) {
        String sql = "SELECT * FROM diagnostic_tests WHERE id = ?";
        
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, testId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return extractTestFromResultSet(rs);
            }
        } catch (SQLException e) {
            System.err.println("✗ Failed to get test by ID: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * Update test
     */
    public boolean updateTest(DiagnosticTest test) {
        String sql = "UPDATE diagnostic_tests SET test_name = ?, test_category = ?, location = ?, price = ?, " +
                    "description = ?, preparation_required = ?, result_time = ?, sample_type = ?, " +
                    "is_home_collection = ?, home_collection_charge = ?, is_available = ? WHERE id = ?";
        
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, test.getTestName());
            stmt.setString(2, test.getTestCategory());
            stmt.setString(3, test.getLocation());
            stmt.setDouble(4, test.getPrice());
            stmt.setString(5, test.getDescription());
            stmt.setString(6, test.getPreparationRequired());
            stmt.setString(7, test.getResultTime());
            stmt.setString(8, test.getSampleType());
            stmt.setBoolean(9, test.isHomeCollection());
            stmt.setDouble(10, test.getHomeCollectionCharge());
            stmt.setBoolean(11, test.isAvailable());
            stmt.setInt(12, test.getId());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("✗ Failed to update test: " + e.getMessage());
        }
        return false;
    }
    
    /**
     * Delete test
     */
    public boolean deleteTest(int testId) {
        String sql = "DELETE FROM diagnostic_tests WHERE id = ?";
        
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, testId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("✗ Failed to delete test: " + e.getMessage());
        }
        return false;
    }
    
    /**
     * Search tests (for user dashboard)
     */
    public List<DiagnosticTest> searchTests(String keyword, String category, String location, String sortBy) {
        List<DiagnosticTest> tests = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM diagnostic_tests WHERE is_available = TRUE");
        
        List<String> params = new ArrayList<>();
        
        // Debug logging
        System.out.println("🔍 Test Search - Keyword: '" + keyword + "', Category: '" + category + "', Location: '" + location + "'");
        
        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append(" AND (test_name LIKE ? OR description LIKE ?)");
            String searchPattern = "%" + keyword.trim() + "%";
            params.add(searchPattern);
            params.add(searchPattern);
        }
        
        if (category != null && !category.isEmpty() && !category.equals("All")) {
            sql.append(" AND test_category = ?");
            params.add(category);
        }
        
        if (location != null && !location.trim().isEmpty() && !location.equals("All")) {
            sql.append(" AND location LIKE ?");
            params.add("%" + location.trim() + "%");
        }
        
        // Add sorting
        switch (sortBy != null ? sortBy : "name") {
            case "price_low":
                sql.append(" ORDER BY price ASC");
                break;
            case "price_high":
                sql.append(" ORDER BY price DESC");
                break;
            default:
                sql.append(" ORDER BY test_name ASC");
        }
        
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
            
            for (int i = 0; i < params.size(); i++) {
                stmt.setString(i + 1, params.get(i));
            }
            
            System.out.println("🔍 SQL Query: " + sql.toString());
            System.out.println("🔍 Parameters: " + params);
            
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                tests.add(extractTestFromResultSet(rs));
            }
            
            System.out.println("🔍 Found " + tests.size() + " tests");
        } catch (SQLException e) {
            System.err.println("✗ Failed to search tests: " + e.getMessage());
            e.printStackTrace();
        }
        return tests;
    }
    
    /**
     * Extract DiagnosticTest from ResultSet
     */
    private DiagnosticTest extractTestFromResultSet(ResultSet rs) throws SQLException {
        DiagnosticTest test = new DiagnosticTest();
        test.setId(rs.getInt("id"));
        test.setProviderId(rs.getInt("provider_id"));
        test.setTestName(rs.getString("test_name"));
        test.setTestCategory(rs.getString("test_category"));
        test.setLocation(rs.getString("location"));
        test.setPrice(rs.getDouble("price"));
        test.setDescription(rs.getString("description"));
        test.setPreparationRequired(rs.getString("preparation_required"));
        test.setResultTime(rs.getString("result_time"));
        test.setSampleType(rs.getString("sample_type"));
        test.setHomeCollection(rs.getBoolean("is_home_collection"));
        test.setHomeCollectionCharge(rs.getDouble("home_collection_charge"));
        test.setAvailable(rs.getBoolean("is_available"));
        return test;
    }
    
    /**
     * Get all test categories for filter dropdown
     */
    public List<String> getAllTestCategories() {
        List<String> categories = new ArrayList<>();
        String sql = "SELECT DISTINCT test_category FROM diagnostic_tests WHERE is_available = TRUE ORDER BY test_category";
        
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String category = rs.getString("test_category");
                if (category != null && !category.trim().isEmpty()) {
                    categories.add(category);
                }
            }
        } catch (SQLException e) {
            System.err.println("✗ Failed to get categories: " + e.getMessage());
        }
        return categories;
    }
    
    /**
     * Get all test locations for filter dropdown
     */
    public List<String> getAllTestLocations() {
        List<String> locations = new ArrayList<>();
        String sql = "SELECT DISTINCT location FROM diagnostic_tests WHERE is_available = TRUE ORDER BY location";
        
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String location = rs.getString("location");
                if (location != null && !location.trim().isEmpty()) {
                    locations.add(location);
                }
            }
        } catch (SQLException e) {
            System.err.println("✗ Failed to get locations: " + e.getMessage());
        }
        
        // Add default locations if none found
        if (locations.isEmpty()) {
            locations.addAll(Arrays.asList(
                "Dhaka", "Chittagong", "Sylhet", "Rajshahi", "Khulna", 
                "Barisal", "Rangpur", "Mymensingh", "Cumilla", "Narayanganj"
            ));
        }
        
        return locations;
    }
}

