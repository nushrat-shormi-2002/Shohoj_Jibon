package com.threelegend.shohojjibon.dao;

import com.threelegend.shohojjibon.model.Doctor;
import com.threelegend.shohojjibon.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Doctor Data Access Object
 * Shohoj Jibon - Makes your life easier
 * Developed by Three_Legend
 */
public class DoctorDAO {
    
    /**
     * Create a new doctor
     */
    public boolean createDoctor(Doctor doctor) {
        String sql = "INSERT INTO doctors (provider_id, name, specialty, qualifications, experience_years, " +
                    "location, consultation_fee, available_days, available_time, contact_number, email, " +
                    "languages_spoken, is_available) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setInt(1, doctor.getProviderId());
            stmt.setString(2, doctor.getName());
            stmt.setString(3, doctor.getSpecialty());
            stmt.setString(4, doctor.getQualifications());
            stmt.setInt(5, doctor.getExperienceYears());
            stmt.setString(6, doctor.getLocation());
            stmt.setDouble(7, doctor.getConsultationFee());
            stmt.setString(8, doctor.getAvailableDays());
            stmt.setString(9, doctor.getAvailableTime());
            stmt.setString(10, doctor.getContactNumber());
            stmt.setString(11, doctor.getEmail());
            stmt.setString(12, doctor.getLanguagesSpoken());
            stmt.setBoolean(13, doctor.isAvailable());
            
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    doctor.setId(rs.getInt(1));
                }
                System.out.println("✓ Doctor added: " + doctor.getName());
                return true;
            }
        } catch (SQLException e) {
            System.err.println("✗ Failed to create doctor: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Get all doctors by provider
     */
    public List<Doctor> getDoctorsByProvider(int providerId) {
        List<Doctor> doctors = new ArrayList<>();
        String sql = "SELECT * FROM doctors WHERE provider_id = ? ORDER BY name";
        
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, providerId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                doctors.add(extractDoctorFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("✗ Failed to get doctors: " + e.getMessage());
        }
        return doctors;
    }
    
    /**
     * Get doctor by ID
     */
    public Doctor getDoctorById(int doctorId) {
        String sql = "SELECT * FROM doctors WHERE id = ?";
        
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, doctorId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return extractDoctorFromResultSet(rs);
            }
        } catch (SQLException e) {
            System.err.println("✗ Failed to get doctor by ID: " + e.getMessage());
        }
        return null;
    }
    
    /**
     * Update doctor
     */
    public boolean updateDoctor(Doctor doctor) {
        String sql = "UPDATE doctors SET name = ?, specialty = ?, qualifications = ?, experience_years = ?, " +
                    "location = ?, consultation_fee = ?, available_days = ?, available_time = ?, " +
                    "contact_number = ?, email = ?, languages_spoken = ?, is_available = ? WHERE id = ?";
        
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, doctor.getName());
            stmt.setString(2, doctor.getSpecialty());
            stmt.setString(3, doctor.getQualifications());
            stmt.setInt(4, doctor.getExperienceYears());
            stmt.setString(5, doctor.getLocation());
            stmt.setDouble(6, doctor.getConsultationFee());
            stmt.setString(7, doctor.getAvailableDays());
            stmt.setString(8, doctor.getAvailableTime());
            stmt.setString(9, doctor.getContactNumber());
            stmt.setString(10, doctor.getEmail());
            stmt.setString(11, doctor.getLanguagesSpoken());
            stmt.setBoolean(12, doctor.isAvailable());
            stmt.setInt(13, doctor.getId());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("✗ Failed to update doctor: " + e.getMessage());
        }
        return false;
    }
    
    /**
     * Delete doctor
     */
    public boolean deleteDoctor(int doctorId) {
        String sql = "DELETE FROM doctors WHERE id = ?";
        
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, doctorId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("✗ Failed to delete doctor: " + e.getMessage());
        }
        return false;
    }
    
    /**
     * Search doctors (for user dashboard)
     * Supports individual search criteria: name, specialty, location
     */
    public List<Doctor> searchDoctors(String name, String specialty, String location, String sortBy) {
        List<Doctor> doctors = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM doctors WHERE is_available = TRUE");
        
        List<String> params = new ArrayList<>();
        
        // Debug logging
        System.out.println("🔍 Doctor Search - Name: '" + name + "', Specialty: '" + specialty + "', Location: '" + location + "'");
        
        // Search by name (exact match or partial match)
        if (name != null && !name.trim().isEmpty()) {
            sql.append(" AND name LIKE ?");
            params.add("%" + name.trim() + "%");
        }
        
        // Search by specialty (exact match)
        if (specialty != null && !specialty.trim().isEmpty() && !specialty.equals("All")) {
            sql.append(" AND specialty = ?");
            params.add(specialty.trim());
        }
        
        // Search by location (partial match)
        if (location != null && !location.trim().isEmpty() && !location.equals("All")) {
            sql.append(" AND location LIKE ?");
            params.add("%" + location.trim() + "%");
        }
        
        // Add sorting
        switch (sortBy != null ? sortBy : "name") {
            case "fee_low":
                sql.append(" ORDER BY consultation_fee ASC");
                break;
            case "fee_high":
                sql.append(" ORDER BY consultation_fee DESC");
                break;
            case "experience":
                sql.append(" ORDER BY experience_years DESC");
                break;
            case "rating":
                sql.append(" ORDER BY rating DESC");
                break;
            default:
                sql.append(" ORDER BY name ASC");
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
                doctors.add(extractDoctorFromResultSet(rs));
            }
            
            System.out.println("🔍 Found " + doctors.size() + " doctors");
        } catch (SQLException e) {
            System.err.println("✗ Failed to search doctors: " + e.getMessage());
            e.printStackTrace();
        }
        return doctors;
    }
    
    /**
     * Extract Doctor from ResultSet
     */
    private Doctor extractDoctorFromResultSet(ResultSet rs) throws SQLException {
        Doctor doctor = new Doctor();
        doctor.setId(rs.getInt("id"));
        doctor.setProviderId(rs.getInt("provider_id"));
        doctor.setName(rs.getString("name"));
        doctor.setSpecialty(rs.getString("specialty"));
        doctor.setQualifications(rs.getString("qualifications"));
        doctor.setExperienceYears(rs.getInt("experience_years"));
        doctor.setLocation(rs.getString("location"));
        doctor.setConsultationFee(rs.getDouble("consultation_fee"));
        doctor.setAvailableDays(rs.getString("available_days"));
        doctor.setAvailableTime(rs.getString("available_time"));
        doctor.setContactNumber(rs.getString("contact_number"));
        doctor.setEmail(rs.getString("email"));
        doctor.setLanguagesSpoken(rs.getString("languages_spoken"));
        doctor.setRating(rs.getDouble("rating"));
        doctor.setTotalReviews(rs.getInt("total_reviews"));
        doctor.setAvailable(rs.getBoolean("is_available"));
        return doctor;
    }
    
    /**
     * Get all specialties for filter dropdown
     */
    public List<String> getAllSpecialties() {
        List<String> specialties = new ArrayList<>();
        String sql = "SELECT DISTINCT specialty FROM doctors WHERE is_available = TRUE ORDER BY specialty";
        
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String specialty = rs.getString("specialty");
                if (specialty != null && !specialty.trim().isEmpty()) {
                    specialties.add(specialty);
                }
            }
        } catch (SQLException e) {
            System.err.println("✗ Failed to get specialties: " + e.getMessage());
        }
        return specialties;
    }
    
    /**
     * Get all doctor locations for filter dropdown
     */
    public List<String> getAllDoctorLocations() {
        List<String> locations = new ArrayList<>();
        String sql = "SELECT DISTINCT location FROM doctors WHERE is_available = TRUE ORDER BY location";
        
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
    
    /**
     * Check if there are any doctors in the database (for debugging)
     */
    public int getTotalDoctorCount() {
        String sql = "SELECT COUNT(*) FROM doctors";
        
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int count = rs.getInt(1);
                System.out.println("🔍 Total doctors in database: " + count);
                return count;
            }
        } catch (SQLException e) {
            System.err.println("✗ Failed to count doctors: " + e.getMessage());
        }
        return 0;
    }
    
    /**
     * Create sample doctors if database is empty (for testing)
     */
    public void createSampleDoctors() {
        String sql = "INSERT INTO doctors (name, specialty, location, consultation_fee, experience_years, contact_number, available_days, qualifications, rating, is_available) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            // Sample doctors
            String[][] sampleDoctors = {
                {"Dr. Ahmed Rahman", "Cardiology", "Dhaka", "1500", "10", "01712345678", "Mon-Fri", "MBBS, MD", "4.5", "1"},
                {"Dr. Fatima Begum", "Pediatrics", "Chittagong", "1200", "8", "01712345679", "Mon-Sat", "MBBS, DCH", "4.3", "1"},
                {"Dr. Karim Hassan", "Orthopedics", "Sylhet", "1800", "12", "01712345680", "Tue-Sat", "MBBS, MS", "4.7", "1"},
                {"Dr. Salma Khan", "Gynecology", "Dhaka", "1600", "9", "01712345681", "Mon-Fri", "MBBS, DGO", "4.4", "1"},
                {"Dr. Rahim Ali", "Neurology", "Rajshahi", "2000", "15", "01712345682", "Mon-Thu", "MBBS, MD", "4.8", "1"}
            };
            
            for (String[] doctor : sampleDoctors) {
                stmt.setString(1, doctor[0]);
                stmt.setString(2, doctor[1]);
                stmt.setString(3, doctor[2]);
                stmt.setDouble(4, Double.parseDouble(doctor[3]));
                stmt.setInt(5, Integer.parseInt(doctor[4]));
                stmt.setString(6, doctor[5]);
                stmt.setString(7, doctor[6]);
                stmt.setString(8, doctor[7]);
                stmt.setDouble(9, Double.parseDouble(doctor[8]));
                stmt.setBoolean(10, Boolean.parseBoolean(doctor[9]));
                
                stmt.executeUpdate();
            }
            
            System.out.println("✅ Sample doctors created successfully!");
            
        } catch (SQLException e) {
            System.err.println("✗ Failed to create sample doctors: " + e.getMessage());
        }
    }
    
    /**
     * Search doctors by name only
     */
    public List<Doctor> searchDoctorsByName(String name) {
        return searchDoctors(name, null, null, null);
    }
    
    /**
     * Search doctors by specialty only
     */
    public List<Doctor> searchDoctorsBySpecialty(String specialty) {
        return searchDoctors(null, specialty, null, null);
    }
    
    /**
     * Search doctors by location only
     */
    public List<Doctor> searchDoctorsByLocation(String location) {
        return searchDoctors(null, null, location, null);
    }
}

