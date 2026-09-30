package com.threelegend.shohojjibon.dao;

import com.threelegend.shohojjibon.model.BloodDonor;
import com.threelegend.shohojjibon.util.DBConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Blood Donor Data Access Object
 * Critical system for emergency blood needs
 * Shohoj Jibon - Makes your life easier
 * Developed by Three_Legend
 */
public class BloodDonorDAO {
    
    /**
     * Create a new blood donor
     */
    public boolean createDonor(BloodDonor donor) {
        String sql = "INSERT INTO blood_donors (provider_id, donor_name, blood_group, age, gender, " +
                    "contact_number, email, location, address, last_donation_date, is_available, " +
                    "medical_history, emergency_contact, weight, has_disease, total_donations) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setInt(1, donor.getProviderId());
            stmt.setString(2, donor.getDonorName());
            stmt.setString(3, donor.getBloodGroup());
            stmt.setInt(4, donor.getAge());
            stmt.setString(5, donor.getGender());
            stmt.setString(6, donor.getContactNumber());
            stmt.setString(7, donor.getEmail());
            stmt.setString(8, donor.getLocation());
            stmt.setString(9, donor.getAddress());
            stmt.setDate(10, donor.getLastDonationDate() != null ? Date.valueOf(donor.getLastDonationDate()) : null);
            stmt.setBoolean(11, donor.isAvailable());
            stmt.setString(12, donor.getMedicalHistory());
            stmt.setString(13, donor.getEmergencyContact());
            stmt.setDouble(14, donor.getWeight());
            stmt.setBoolean(15, donor.hasDisease());
            stmt.setInt(16, donor.getTotalDonations());
            
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    donor.setId(rs.getInt(1));
                }
                System.out.println("✓ Blood donor added: " + donor.getDonorName() + " (" + donor.getBloodGroup() + ")");
                return true;
            }
        } catch (SQLException e) {
            System.err.println("✗ Failed to create donor: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Get all donors by provider
     */
    public List<BloodDonor> getDonorsByProvider(int providerId) {
        List<BloodDonor> donors = new ArrayList<>();
        String sql = "SELECT * FROM blood_donors WHERE provider_id = ? ORDER BY blood_group, donor_name";
        
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, providerId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                donors.add(extractDonorFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("✗ Failed to get donors: " + e.getMessage());
        }
        return donors;
    }
    
    /**
     * Update donor
     */
    public boolean updateDonor(BloodDonor donor) {
        String sql = "UPDATE blood_donors SET donor_name = ?, blood_group = ?, age = ?, gender = ?, " +
                    "contact_number = ?, email = ?, location = ?, address = ?, last_donation_date = ?, " +
                    "is_available = ?, medical_history = ?, emergency_contact = ?, weight = ?, " +
                    "has_disease = ?, total_donations = ? WHERE id = ?";
        
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, donor.getDonorName());
            stmt.setString(2, donor.getBloodGroup());
            stmt.setInt(3, donor.getAge());
            stmt.setString(4, donor.getGender());
            stmt.setString(5, donor.getContactNumber());
            stmt.setString(6, donor.getEmail());
            stmt.setString(7, donor.getLocation());
            stmt.setString(8, donor.getAddress());
            stmt.setDate(9, donor.getLastDonationDate() != null ? Date.valueOf(donor.getLastDonationDate()) : null);
            stmt.setBoolean(10, donor.isAvailable());
            stmt.setString(11, donor.getMedicalHistory());
            stmt.setString(12, donor.getEmergencyContact());
            stmt.setDouble(13, donor.getWeight());
            stmt.setBoolean(14, donor.hasDisease());
            stmt.setInt(15, donor.getTotalDonations());
            stmt.setInt(16, donor.getId());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("✗ Failed to update donor: " + e.getMessage());
        }
        return false;
    }
    
    /**
     * Delete donor
     */
    public boolean deleteDonor(int donorId) {
        String sql = "DELETE FROM blood_donors WHERE id = ?";
        
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, donorId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("✗ Failed to delete donor: " + e.getMessage());
        }
        return false;
    }
    
    /**
     * CRITICAL: Search available blood donors for emergency situations
     * This method prioritizes donors who are currently eligible to donate
     */
    public List<BloodDonor> searchDonors(String bloodGroup, String location) {
        List<BloodDonor> donors = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
            "SELECT * FROM blood_donors WHERE is_available = TRUE AND has_disease = FALSE " +
            "AND age >= 18 AND age <= 60 AND weight >= 50"
        );
        
        List<String> params = new ArrayList<>();
        
        if (bloodGroup != null && !bloodGroup.isEmpty() && !bloodGroup.equals("All")) {
            sql.append(" AND blood_group = ?");
            params.add(bloodGroup);
        }
        
        if (location != null && !location.trim().isEmpty()) {
            sql.append(" AND location LIKE ?");
            params.add("%" + location.trim() + "%");
        }
        
        // Prioritize donors who haven't donated recently (eligible now)
        sql.append(" ORDER BY " +
                  "CASE WHEN last_donation_date IS NULL THEN 0 " +
                  "WHEN DATEDIFF(CURDATE(), last_donation_date) >= 90 THEN 1 " +
                  "ELSE 2 END, " +
                  "last_donation_date ASC, total_donations DESC");
        
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
            
            for (int i = 0; i < params.size(); i++) {
                stmt.setString(i + 1, params.get(i));
            }
            
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                donors.add(extractDonorFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("✗ Failed to search donors: " + e.getMessage());
            e.printStackTrace();
        }
        return donors;
    }
    
    /**
     * Get eligible donors count by blood group
     */
    public int getEligibleDonorsCount(String bloodGroup) {
        String sql = "SELECT COUNT(*) FROM blood_donors WHERE is_available = TRUE AND has_disease = FALSE " +
                    "AND age >= 18 AND age <= 60 AND weight >= 50 AND blood_group = ? " +
                    "AND (last_donation_date IS NULL OR DATEDIFF(CURDATE(), last_donation_date) >= 90)";
        
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, bloodGroup);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("✗ Failed to count donors: " + e.getMessage());
        }
        return 0;
    }
    
    /**
     * Get donor by ID
     */
    public BloodDonor getDonorById(int donorId) {
        String sql = "SELECT * FROM blood_donors WHERE id = ?";
        
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, donorId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return extractDonorFromResultSet(rs);
            }
        } catch (SQLException e) {
            System.err.println("✗ Failed to get donor by ID: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * Extract BloodDonor from ResultSet
     */
    private BloodDonor extractDonorFromResultSet(ResultSet rs) throws SQLException {
        BloodDonor donor = new BloodDonor();
        donor.setId(rs.getInt("id"));
        donor.setProviderId(rs.getInt("provider_id"));
        donor.setDonorName(rs.getString("donor_name"));
        donor.setBloodGroup(rs.getString("blood_group"));
        donor.setAge(rs.getInt("age"));
        donor.setGender(rs.getString("gender"));
        donor.setContactNumber(rs.getString("contact_number"));
        donor.setEmail(rs.getString("email"));
        donor.setLocation(rs.getString("location"));
        donor.setAddress(rs.getString("address"));
        
        Date lastDonation = rs.getDate("last_donation_date");
        if (lastDonation != null) {
            donor.setLastDonationDate(lastDonation.toLocalDate());
        }
        
        donor.setAvailable(rs.getBoolean("is_available"));
        donor.setMedicalHistory(rs.getString("medical_history"));
        donor.setEmergencyContact(rs.getString("emergency_contact"));
        donor.setWeight(rs.getDouble("weight"));
        donor.setHasDisease(rs.getBoolean("has_disease"));
        donor.setTotalDonations(rs.getInt("total_donations"));
        return donor;
    }
}

