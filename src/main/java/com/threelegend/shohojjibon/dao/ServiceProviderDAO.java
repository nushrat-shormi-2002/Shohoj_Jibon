package com.threelegend.shohojjibon.dao;

import com.threelegend.shohojjibon.model.ServiceProvider;
import com.threelegend.shohojjibon.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Service Provider Data Access Object
 * Shohoj Jibon - Makes your life easier
 * Developed by Three_Legend
 */
public class ServiceProviderDAO {
    
    /**
     * Create a new service provider
     */
    public boolean createProvider(ServiceProvider provider) {
        String sql = "INSERT INTO service_providers (user_id, provider_name, authority_type, business_license, description, address, website, verified) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setInt(1, provider.getUserId());
            stmt.setString(2, provider.getProviderName());
            stmt.setString(3, provider.getAuthorityType());
            stmt.setString(4, provider.getBusinessLicense());
            stmt.setString(5, provider.getDescription());
            stmt.setString(6, provider.getAddress());
            stmt.setString(7, provider.getWebsite());
            stmt.setBoolean(8, provider.isVerified());
            
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    provider.setId(rs.getInt(1));
                }
                System.out.println("✓ Service provider created: " + provider.getProviderName());
                return true;
            }
        } catch (SQLException e) {
            System.err.println("✗ Failed to create service provider: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Find provider by user ID
     */
    public ServiceProvider findByUserId(int userId) {
        String sql = "SELECT * FROM service_providers WHERE user_id = ?";
        
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToProvider(rs);
            }
        } catch (SQLException e) {
            System.err.println("✗ Failed to find provider by user ID: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * Find provider by ID
     */
    public ServiceProvider findById(int id) {
        String sql = "SELECT * FROM service_providers WHERE id = ?";
        
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToProvider(rs);
            }
        } catch (SQLException e) {
            System.err.println("✗ Failed to find provider by ID: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * Update provider information
     */
    public boolean updateProvider(ServiceProvider provider) {
        String sql = "UPDATE service_providers SET provider_name = ?, authority_type = ?, business_license = ?, description = ?, address = ?, website = ? WHERE id = ?";
        
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, provider.getProviderName());
            stmt.setString(2, provider.getAuthorityType());
            stmt.setString(3, provider.getBusinessLicense());
            stmt.setString(4, provider.getDescription());
            stmt.setString(5, provider.getAddress());
            stmt.setString(6, provider.getWebsite());
            stmt.setInt(7, provider.getId());
            
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                System.out.println("✓ Provider updated: " + provider.getProviderName());
                return true;
            }
        } catch (SQLException e) {
            System.err.println("✗ Failed to update provider: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Get all verified providers
     */
    public List<ServiceProvider> getAllVerifiedProviders() {
        List<ServiceProvider> providers = new ArrayList<>();
        String sql = "SELECT * FROM service_providers WHERE verified = TRUE ORDER BY rating DESC";
        
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                providers.add(mapResultSetToProvider(rs));
            }
        } catch (SQLException e) {
            System.err.println("✗ Failed to get verified providers: " + e.getMessage());
            e.printStackTrace();
        }
        return providers;
    }
    
    /**
     * Map ResultSet to ServiceProvider object
     */
    private ServiceProvider mapResultSetToProvider(ResultSet rs) throws SQLException {
        ServiceProvider provider = new ServiceProvider();
        provider.setId(rs.getInt("id"));
        provider.setUserId(rs.getInt("user_id"));
        provider.setProviderName(rs.getString("provider_name"));
        provider.setAuthorityType(rs.getString("authority_type"));
        provider.setBusinessLicense(rs.getString("business_license"));
        provider.setDescription(rs.getString("description"));
        provider.setAddress(rs.getString("address"));
        provider.setWebsite(rs.getString("website"));
        provider.setRating(rs.getDouble("rating"));
        provider.setTotalReviews(rs.getInt("total_reviews"));
        provider.setVerified(rs.getBoolean("verified"));
        
        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            provider.setCreatedAt(createdAt.toLocalDateTime());
        }
        
        Timestamp updatedAt = rs.getTimestamp("updated_at");
        if (updatedAt != null) {
            provider.setUpdatedAt(updatedAt.toLocalDateTime());
        }
        
        return provider;
    }
    
    /**
     * Get provider by ID
     */
    public ServiceProvider getProviderById(int id) {
        String sql = "SELECT * FROM service_providers WHERE id = ?";
        
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToProvider(rs);
            }
        } catch (SQLException e) {
            System.err.println("✗ Failed to get provider: " + e.getMessage());
        }
        return null;
    }
}

