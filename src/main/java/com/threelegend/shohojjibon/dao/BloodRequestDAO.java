package com.threelegend.shohojjibon.dao;

import com.threelegend.shohojjibon.model.BloodRequest;
import com.threelegend.shohojjibon.util.DBConnection;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Blood Request DAO
 * Handles database operations for blood donation requests
 * Shohoj Jibon - Makes your life easier
 * Developed by Three_Legend
 */
public class BloodRequestDAO {
    
    /**
     * Create a new blood request
     */
    public boolean createBloodRequest(BloodRequest request) {
        String sql = "INSERT INTO blood_requests (requester_id, donor_id, blood_bank_id, blood_group, " +
                    "patient_name, patient_age, patient_gender, patient_phone, patient_email, patient_address, " +
                    "patient_condition, medical_history, current_medications, urgency, contact_number, location, " +
                    "special_requirements, status, request_date) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setInt(1, request.getRequesterId());
            stmt.setInt(2, request.getDonorId());
            stmt.setInt(3, request.getBloodBankId());
            stmt.setString(4, request.getBloodGroup());
            stmt.setString(5, request.getPatientName());
            stmt.setString(6, request.getPatientAge());
            stmt.setString(7, request.getPatientGender());
            stmt.setString(8, request.getPatientPhone());
            stmt.setString(9, request.getPatientEmail());
            stmt.setString(10, request.getPatientAddress());
            stmt.setString(11, request.getPatientCondition());
            stmt.setString(12, request.getMedicalHistory());
            stmt.setString(13, request.getCurrentMedications());
            stmt.setString(14, request.getUrgency());
            stmt.setString(15, request.getContactNumber());
            stmt.setString(16, request.getLocation());
            stmt.setString(17, request.getSpecialRequirements());
            stmt.setString(18, request.getStatus());
            stmt.setTimestamp(19, Timestamp.valueOf(request.getRequestDate()));
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        request.setId(generatedKeys.getInt(1));
                        return true;
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("✗ Failed to create blood request: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Get blood requests by requester ID
     */
    public List<BloodRequest> getBloodRequestsByRequesterId(int requesterId) {
        List<BloodRequest> requests = new ArrayList<>();
        String sql = "SELECT * FROM blood_requests WHERE requester_id = ? ORDER BY request_date DESC";
        
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, requesterId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                requests.add(mapResultSetToBloodRequest(rs));
            }
        } catch (SQLException e) {
            System.err.println("✗ Failed to get blood requests by requester: " + e.getMessage());
            e.printStackTrace();
        }
        return requests;
    }
    
    /**
     * Get blood requests by blood bank ID
     */
    public List<BloodRequest> getBloodRequestsByBloodBankId(int bloodBankId) {
        List<BloodRequest> requests = new ArrayList<>();
        String sql = "SELECT * FROM blood_requests WHERE blood_bank_id = ? ORDER BY request_date DESC";
        
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, bloodBankId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                requests.add(mapResultSetToBloodRequest(rs));
            }
        } catch (SQLException e) {
            System.err.println("✗ Failed to get blood requests by blood bank: " + e.getMessage());
            e.printStackTrace();
        }
        return requests;
    }
    
    /**
     * Get blood requests by donor ID
     */
    public List<BloodRequest> getBloodRequestsByDonorId(int donorId) {
        List<BloodRequest> requests = new ArrayList<>();
        String sql = "SELECT * FROM blood_requests WHERE donor_id = ? ORDER BY request_date DESC";
        
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, donorId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                requests.add(mapResultSetToBloodRequest(rs));
            }
        } catch (SQLException e) {
            System.err.println("✗ Failed to get blood requests by donor: " + e.getMessage());
            e.printStackTrace();
        }
        return requests;
    }
    
    /**
     * Update blood request status
     */
    public boolean updateBloodRequestStatus(int requestId, String status, String notes) {
        String sql = "UPDATE blood_requests SET status = ?, blood_bank_notes = ?, " +
                    "approval_date = ? WHERE id = ?";
        
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, status);
            stmt.setString(2, notes);
            stmt.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
            stmt.setInt(4, requestId);
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("✗ Failed to update blood request status: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Mark blood request as completed
     */
    public boolean markBloodRequestCompleted(int requestId) {
        String sql = "UPDATE blood_requests SET status = 'COMPLETED', completion_date = ? WHERE id = ?";
        
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
            stmt.setInt(2, requestId);
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("✗ Failed to mark blood request as completed: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Get blood request by ID
     */
    public BloodRequest getBloodRequestById(int requestId) {
        String sql = "SELECT * FROM blood_requests WHERE id = ?";
        
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, requestId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToBloodRequest(rs);
            }
        } catch (SQLException e) {
            System.err.println("✗ Failed to get blood request by ID: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * Get blood request statistics for blood bank
     */
    public int getBloodRequestCountByStatus(int bloodBankId, String status) {
        String sql = "SELECT COUNT(*) FROM blood_requests WHERE blood_bank_id = ? AND status = ?";
        
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, bloodBankId);
            stmt.setString(2, status);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("✗ Failed to get blood request count: " + e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }
    
    /**
     * Map ResultSet to BloodRequest
     */
    private BloodRequest mapResultSetToBloodRequest(ResultSet rs) throws SQLException {
        BloodRequest request = new BloodRequest();
        
        request.setId(rs.getInt("id"));
        request.setRequesterId(rs.getInt("requester_id"));
        request.setDonorId(rs.getInt("donor_id"));
        request.setBloodBankId(rs.getInt("blood_bank_id"));
        request.setBloodGroup(rs.getString("blood_group"));
        request.setPatientName(rs.getString("patient_name"));
        request.setPatientAge(rs.getString("patient_age"));
        request.setPatientGender(rs.getString("patient_gender"));
        request.setPatientPhone(rs.getString("patient_phone"));
        request.setPatientEmail(rs.getString("patient_email"));
        request.setPatientAddress(rs.getString("patient_address"));
        request.setPatientCondition(rs.getString("patient_condition"));
        request.setMedicalHistory(rs.getString("medical_history"));
        request.setCurrentMedications(rs.getString("current_medications"));
        request.setUrgency(rs.getString("urgency"));
        request.setContactNumber(rs.getString("contact_number"));
        request.setLocation(rs.getString("location"));
        request.setSpecialRequirements(rs.getString("special_requirements"));
        request.setStatus(rs.getString("status"));
        request.setBloodBankNotes(rs.getString("blood_bank_notes"));
        
        Timestamp requestDate = rs.getTimestamp("request_date");
        if (requestDate != null) {
            request.setRequestDate(requestDate.toLocalDateTime());
        }
        
        Timestamp approvalDate = rs.getTimestamp("approval_date");
        if (approvalDate != null) {
            request.setApprovalDate(approvalDate.toLocalDateTime());
        }
        
        Timestamp completionDate = rs.getTimestamp("completion_date");
        if (completionDate != null) {
            request.setCompletionDate(completionDate.toLocalDateTime());
        }
        
        return request;
    }
}
