package com.threelegend.shohojjibon.dao;

import com.threelegend.shohojjibon.model.Booking;
import com.threelegend.shohojjibon.util.DBConnection;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Booking Data Access Object
 * Shohoj Jibon - Makes your life easier
 * Developed by Three_Legend
 */
public class BookingDAO {
    
    /**
     * Create a new booking
     */
    public boolean createBooking(Booking booking) {
        String sql = "INSERT INTO bookings (user_id, service_type, service_id, booking_date, booking_time, end_date, quantity, total_amount, status, payment_status, payment_method, notes) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setInt(1, booking.getUserId());
            stmt.setString(2, booking.getServiceType());
            stmt.setInt(3, booking.getServiceId());
            stmt.setDate(4, Date.valueOf(booking.getBookingDate()));
            stmt.setTime(5, booking.getBookingTime() != null ? Time.valueOf(booking.getBookingTime()) : null);
            stmt.setDate(6, booking.getEndDate() != null ? Date.valueOf(booking.getEndDate()) : null);
            stmt.setInt(7, booking.getQuantity());
            stmt.setDouble(8, booking.getTotalAmount());
            stmt.setString(9, booking.getStatus());
            stmt.setString(10, booking.getPaymentStatus());
            stmt.setString(11, booking.getPaymentMethod());
            stmt.setString(12, booking.getNotes());
            
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    booking.setId(rs.getInt(1));
                }
                System.out.println("✓ Booking created: " + booking.getBookingReference());
                return true;
            }
        } catch (SQLException e) {
            System.err.println("✗ Failed to create booking: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Get bookings by user ID
     */
    public List<Booking> getBookingsByUserId(int userId) {
        List<Booking> bookings = new ArrayList<>();
        String sql = "SELECT * FROM bookings WHERE user_id = ? ORDER BY created_at DESC";
        
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                bookings.add(mapResultSetToBooking(rs));
            }
        } catch (SQLException e) {
            System.err.println("✗ Failed to get bookings: " + e.getMessage());
            e.printStackTrace();
        }
        return bookings;
    }
    
    /**
     * Get bookings for a provider's services (all types: BUS, DOCTOR, TEST)
     */
    public List<Booking> getBookingsByProviderId(int providerId) {
        List<Booking> bookings = new ArrayList<>();
        
        // Get BUS bookings
        String busSql = "SELECT b.* FROM bookings b " +
                       "INNER JOIN buses bus ON b.service_id = bus.id " +
                       "WHERE bus.provider_id = ? AND b.service_type = 'BUS'";
        
        // Get DOCTOR bookings
        String doctorSql = "SELECT b.* FROM bookings b " +
                          "INNER JOIN doctors d ON b.service_id = d.id " +
                          "WHERE d.provider_id = ? AND b.service_type = 'DOCTOR'";
        
        // Get TEST bookings
        String testSql = "SELECT b.* FROM bookings b " +
                        "INNER JOIN diagnostic_tests dt ON b.service_id = dt.id " +
                        "WHERE dt.provider_id = ? AND b.service_type = 'TEST'";
        
        // Get HOTEL bookings
        String hotelSql = "SELECT b.* FROM bookings b " +
                         "INNER JOIN hotels h ON b.service_id = h.id " +
                         "WHERE h.provider_id = ? AND b.service_type = 'HOTEL'";
        
        try (Connection conn = DBConnection.getInstance().getConnection()) {
            
            // Get BUS bookings
            try (PreparedStatement stmt = conn.prepareStatement(busSql)) {
                stmt.setInt(1, providerId);
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    bookings.add(mapResultSetToBooking(rs));
                }
            }
            
            // Get DOCTOR bookings
            try (PreparedStatement stmt = conn.prepareStatement(doctorSql)) {
                stmt.setInt(1, providerId);
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    bookings.add(mapResultSetToBooking(rs));
                }
            }
            
            // Get TEST bookings
            try (PreparedStatement stmt = conn.prepareStatement(testSql)) {
                stmt.setInt(1, providerId);
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    bookings.add(mapResultSetToBooking(rs));
                }
            }
            
            // Get HOTEL bookings
            try (PreparedStatement stmt = conn.prepareStatement(hotelSql)) {
                stmt.setInt(1, providerId);
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    bookings.add(mapResultSetToBooking(rs));
                }
            }
            
            // Sort all bookings by creation date (newest first)
            bookings.sort((b1, b2) -> b2.getCreatedAt().compareTo(b1.getCreatedAt()));
            
        } catch (SQLException e) {
            System.err.println("✗ Failed to get provider bookings: " + e.getMessage());
            e.printStackTrace();
        }
        return bookings;
    }
    
    /**
     * Update booking status
     */
    public boolean updateBookingStatus(int bookingId, String status) {
        String sql = "UPDATE bookings SET status = ? WHERE id = ?";
        
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, status);
            stmt.setInt(2, bookingId);
            
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                System.out.println("✓ Booking status updated: #BK" + String.format("%05d", bookingId));
                return true;
            }
        } catch (SQLException e) {
            System.err.println("✗ Failed to update booking status: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Update booking details
     */
    public boolean updateBooking(Booking booking) {
        String sql = "UPDATE bookings SET status = ?, total_amount = ?, notes = ? WHERE id = ?";
        
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, booking.getStatus());
            stmt.setDouble(2, booking.getTotalAmount());
            stmt.setString(3, booking.getNotes());
            stmt.setInt(4, booking.getId());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("✗ Failed to update booking: " + e.getMessage());
        }
        return false;
    }
    
    /**
     * Cancel a booking
     */
    public boolean cancelBooking(int bookingId) {
        return updateBookingStatus(bookingId, "CANCELLED");
    }
    
    /**
     * Map ResultSet to Booking object
     */
    private Booking mapResultSetToBooking(ResultSet rs) throws SQLException {
        Booking booking = new Booking();
        booking.setId(rs.getInt("id"));
        booking.setUserId(rs.getInt("user_id"));
        booking.setServiceType(rs.getString("service_type"));
        booking.setServiceId(rs.getInt("service_id"));
        
        Date bookingDate = rs.getDate("booking_date");
        if (bookingDate != null) {
            booking.setBookingDate(bookingDate.toLocalDate());
        }
        
        Time bookingTime = rs.getTime("booking_time");
        if (bookingTime != null) {
            booking.setBookingTime(bookingTime.toLocalTime());
        }
        
        Date endDate = rs.getDate("end_date");
        if (endDate != null) {
            booking.setEndDate(endDate.toLocalDate());
        }
        
        // Note: The database doesn't have service_date column
        // We'll use booking_date as service_date for now
        Date serviceDate = rs.getDate("booking_date");
        if (serviceDate != null) {
            booking.setServiceDate(serviceDate.toLocalDate().atStartOfDay());
        }
        
        // Note: provider_id and number_of_people columns don't exist in the database
        // We'll set default values for now
        booking.setProviderId(0); // Will need to be determined from service_id and service_type
        booking.setQuantity(rs.getInt("quantity"));
        booking.setNumberOfPeople(rs.getInt("quantity")); // Use quantity as number_of_people
        booking.setTotalAmount(rs.getDouble("total_amount"));
        booking.setStatus(rs.getString("status"));
        booking.setPaymentStatus(rs.getString("payment_status"));
        booking.setPaymentMethod(rs.getString("payment_method"));
        booking.setTransactionId(rs.getString("transaction_id"));
        booking.setNotes(rs.getString("notes"));
        // Note: special_requests column doesn't exist in the database
        // We'll use notes as special_requests for now
        booking.setSpecialRequests(rs.getString("notes"));
        
        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            booking.setCreatedAt(createdAt.toLocalDateTime());
        }
        
        Timestamp updatedAt = rs.getTimestamp("updated_at");
        if (updatedAt != null) {
            booking.setUpdatedAt(updatedAt.toLocalDateTime());
        }
        
        return booking;
    }
}

