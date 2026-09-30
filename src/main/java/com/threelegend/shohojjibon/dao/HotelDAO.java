package com.threelegend.shohojjibon.dao;

import com.threelegend.shohojjibon.model.Hotel;
import com.threelegend.shohojjibon.util.DBConnection;

import java.sql.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Hotel Data Access Object
 * Shohoj Jibon - Makes your life easier
 * Developed by Three_Legend
 */
public class HotelDAO {
    
    /**
     * Create a new hotel/room
     */
    public boolean createHotel(Hotel hotel) {
        String sql = "INSERT INTO hotels (provider_id, hotel_name, location, address, room_type, " +
                    "price_per_night, total_rooms, available_rooms, amenities, room_size, max_occupancy, " +
                    "contact_number, email, check_in_time, check_out_time, cancellation_policy, is_available) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setInt(1, hotel.getProviderId());
            stmt.setString(2, hotel.getHotelName());
            stmt.setString(3, hotel.getLocation());
            stmt.setString(4, hotel.getAddress());
            stmt.setString(5, hotel.getRoomType());
            stmt.setDouble(6, hotel.getPricePerNight());
            stmt.setInt(7, hotel.getTotalRooms());
            stmt.setInt(8, hotel.getAvailableRooms());
            stmt.setString(9, hotel.getAmenities());
            stmt.setString(10, hotel.getRoomSize());
            stmt.setInt(11, hotel.getMaxOccupancy());
            stmt.setString(12, hotel.getContactNumber());
            stmt.setString(13, hotel.getEmail());
            stmt.setTime(14, Time.valueOf(hotel.getCheckInTime()));
            stmt.setTime(15, Time.valueOf(hotel.getCheckOutTime()));
            stmt.setString(16, hotel.getCancellationPolicy());
            stmt.setBoolean(17, hotel.isAvailable());
            
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    hotel.setId(rs.getInt(1));
                }
                System.out.println("✓ Hotel room added: " + hotel.getHotelName() + " - " + hotel.getRoomType());
                return true;
            }
        } catch (SQLException e) {
            System.err.println("✗ Failed to create hotel room: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Get all hotels/rooms by provider
     */
    public List<Hotel> getHotelsByProvider(int providerId) {
        List<Hotel> hotels = new ArrayList<>();
        String sql = "SELECT * FROM hotels WHERE provider_id = ? ORDER BY room_type, price_per_night";
        
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, providerId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                hotels.add(extractHotelFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("✗ Failed to get hotels: " + e.getMessage());
        }
        return hotels;
    }
    
    /**
     * Get hotel by ID
     */
    public Hotel getHotelById(int hotelId) {
        String sql = "SELECT * FROM hotels WHERE id = ?";
        
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, hotelId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return extractHotelFromResultSet(rs);
            }
        } catch (SQLException e) {
            System.err.println("✗ Failed to get hotel by ID: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * Update hotel/room
     */
    public boolean updateHotel(Hotel hotel) {
        String sql = "UPDATE hotels SET hotel_name = ?, location = ?, address = ?, room_type = ?, " +
                    "price_per_night = ?, total_rooms = ?, available_rooms = ?, amenities = ?, " +
                    "room_size = ?, max_occupancy = ?, contact_number = ?, email = ?, " +
                    "check_in_time = ?, check_out_time = ?, cancellation_policy = ?, is_available = ? " +
                    "WHERE id = ?";
        
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, hotel.getHotelName());
            stmt.setString(2, hotel.getLocation());
            stmt.setString(3, hotel.getAddress());
            stmt.setString(4, hotel.getRoomType());
            stmt.setDouble(5, hotel.getPricePerNight());
            stmt.setInt(6, hotel.getTotalRooms());
            stmt.setInt(7, hotel.getAvailableRooms());
            stmt.setString(8, hotel.getAmenities());
            stmt.setString(9, hotel.getRoomSize());
            stmt.setInt(10, hotel.getMaxOccupancy());
            stmt.setString(11, hotel.getContactNumber());
            stmt.setString(12, hotel.getEmail());
            stmt.setTime(13, Time.valueOf(hotel.getCheckInTime()));
            stmt.setTime(14, Time.valueOf(hotel.getCheckOutTime()));
            stmt.setString(15, hotel.getCancellationPolicy());
            stmt.setBoolean(16, hotel.isAvailable());
            stmt.setInt(17, hotel.getId());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("✗ Failed to update hotel: " + e.getMessage());
        }
        return false;
    }
    
    /**
     * Delete hotel/room
     */
    public boolean deleteHotel(int hotelId) {
        String sql = "DELETE FROM hotels WHERE id = ?";
        
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, hotelId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("✗ Failed to delete hotel: " + e.getMessage());
        }
        return false;
    }
    
    /**
     * Search hotels (for user dashboard)
     * Supports individual search criteria: name, location, room type
     */
    public List<Hotel> searchHotels(String name, String location, String roomType, String sortBy) {
        List<Hotel> hotels = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM hotels WHERE is_available = TRUE");
        
        List<String> params = new ArrayList<>();
        
        // Debug logging
        System.out.println("🔍 Hotel Search - Name: '" + name + "', Location: '" + location + "', RoomType: '" + roomType + "'");
        
        // Search by name (partial match)
        if (name != null && !name.trim().isEmpty()) {
            sql.append(" AND hotel_name LIKE ?");
            params.add("%" + name.trim() + "%");
        }
        
        // Search by location (partial match)
        if (location != null && !location.trim().isEmpty() && !location.equals("All")) {
            sql.append(" AND location LIKE ?");
            params.add("%" + location.trim() + "%");
        }
        
        // Search by room type (exact match)
        if (roomType != null && !roomType.isEmpty() && !roomType.equals("All")) {
            sql.append(" AND room_type = ?");
            params.add(roomType);
        }
        
        // Add sorting
        switch (sortBy != null ? sortBy : "name") {
            case "price_low":
                sql.append(" ORDER BY price_per_night ASC");
                break;
            case "price_high":
                sql.append(" ORDER BY price_per_night DESC");
                break;
            case "rating":
                sql.append(" ORDER BY rating DESC");
                break;
            case "name":
                sql.append(" ORDER BY hotel_name ASC");
                break;
            default:
                sql.append(" ORDER BY hotel_name ASC");
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
                hotels.add(extractHotelFromResultSet(rs));
            }
            
            System.out.println("🔍 Found " + hotels.size() + " hotels");
        } catch (SQLException e) {
            System.err.println("✗ Failed to search hotels: " + e.getMessage());
            e.printStackTrace();
        }
        return hotels;
    }
    
    /**
     * Search hotels by name only
     */
    public List<Hotel> searchHotelsByName(String name) {
        return searchHotels(name, null, null, null);
    }
    
    /**
     * Search hotels by location only
     */
    public List<Hotel> searchHotelsByLocation(String location) {
        return searchHotels(null, location, null, null);
    }
    
    /**
     * Search hotels by room type only
     */
    public List<Hotel> searchHotelsByRoomType(String roomType) {
        return searchHotels(null, null, roomType, null);
    }
    
    /**
     * Extract Hotel from ResultSet
     */
    private Hotel extractHotelFromResultSet(ResultSet rs) throws SQLException {
        Hotel hotel = new Hotel();
        hotel.setId(rs.getInt("id"));
        hotel.setProviderId(rs.getInt("provider_id"));
        hotel.setHotelName(rs.getString("hotel_name"));
        hotel.setLocation(rs.getString("location"));
        hotel.setAddress(rs.getString("address"));
        hotel.setRoomType(rs.getString("room_type"));
        hotel.setPricePerNight(rs.getDouble("price_per_night"));
        hotel.setRating(rs.getDouble("rating"));
        hotel.setTotalRooms(rs.getInt("total_rooms"));
        hotel.setAvailableRooms(rs.getInt("available_rooms"));
        hotel.setAmenities(rs.getString("amenities"));
        hotel.setRoomSize(rs.getString("room_size"));
        hotel.setMaxOccupancy(rs.getInt("max_occupancy"));
        hotel.setContactNumber(rs.getString("contact_number"));
        hotel.setEmail(rs.getString("email"));
        hotel.setCheckInTime(rs.getTime("check_in_time").toLocalTime());
        hotel.setCheckOutTime(rs.getTime("check_out_time").toLocalTime());
        hotel.setCancellationPolicy(rs.getString("cancellation_policy"));
        hotel.setAvailable(rs.getBoolean("is_available"));
        return hotel;
    }
}

