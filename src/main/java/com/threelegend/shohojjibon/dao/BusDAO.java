package com.threelegend.shohojjibon.dao;

import com.threelegend.shohojjibon.model.Bus;
import com.threelegend.shohojjibon.model.BusStop;
import com.threelegend.shohojjibon.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Bus Data Access Object
 * Shohoj Jibon - Makes your life easier
 * Developed by Three_Legend
 */
public class BusDAO {
    
    /**
     * Search buses by route
     */
    public List<Bus> searchBusByRoute(String startPoint, String endPoint) {
        List<Bus> buses = new ArrayList<>();
        String sql = "SELECT * FROM buses WHERE start_point LIKE ? AND end_point LIKE ? AND is_active = TRUE ORDER BY fare ASC";
        
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, "%" + startPoint + "%");
            stmt.setString(2, "%" + endPoint + "%");
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                buses.add(mapResultSetToBus(rs));
            }
        } catch (SQLException e) {
            System.err.println("✗ Failed to search buses: " + e.getMessage());
            e.printStackTrace();
        }
        return buses;
    }
    
    /**
     * Get all buses for a provider
     */
    public List<Bus> getBusesByProvider(int providerId) {
        List<Bus> buses = new ArrayList<>();
        String sql = "SELECT * FROM buses WHERE provider_id = ? ORDER BY created_at DESC";
        
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, providerId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                buses.add(mapResultSetToBus(rs));
            }
        } catch (SQLException e) {
            System.err.println("✗ Failed to get buses for provider: " + e.getMessage());
            e.printStackTrace();
        }
        return buses;
    }
    
    /**
     * Create a new bus - Simplified for local buses with stops
     */
    public boolean createBus(Bus bus) {
        String sql = "INSERT INTO buses (provider_id, bus_name, bus_number, start_point, end_point, schedule_time) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setInt(1, bus.getProviderId());
            stmt.setString(2, bus.getBusName());
            stmt.setString(3, bus.getBusNumber());
            stmt.setString(4, bus.getStartPoint());
            stmt.setString(5, bus.getEndPoint());
            stmt.setTime(6, Time.valueOf(bus.getScheduleTime()));
            
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    bus.setId(rs.getInt(1));
                }
                System.out.println("✓ Bus created: " + bus.getBusName());
                return true;
            }
        } catch (SQLException e) {
            System.err.println("✗ Failed to create bus: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Update bus fare and distance based on stops
     */
    public boolean updateBusFareAndDistance(int busId, double fare, double distance) {
        String sql = "UPDATE buses SET fare = ?, distance = ? WHERE id = ?";
        
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setDouble(1, fare);
            stmt.setDouble(2, distance);
            stmt.setInt(3, busId);
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("✗ Failed to update bus fare and distance: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Update bus information
     */
    public boolean updateBus(Bus bus) {
        String sql = "UPDATE buses SET bus_name = ?, bus_number = ?, start_point = ?, end_point = ?, fare = ?, distance = ?, schedule_time = ?, arrival_time = ?, total_seats = ?, available_seats = ?, bus_type = ?, amenities = ?, is_active = ? WHERE id = ?";
        
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, bus.getBusName());
            stmt.setString(2, bus.getBusNumber());
            stmt.setString(3, bus.getStartPoint());
            stmt.setString(4, bus.getEndPoint());
            stmt.setDouble(5, bus.getFare());
            stmt.setDouble(6, bus.getDistance());
            stmt.setTime(7, Time.valueOf(bus.getScheduleTime()));
            stmt.setTime(8, bus.getArrivalTime() != null ? Time.valueOf(bus.getArrivalTime()) : null);
            stmt.setInt(9, bus.getTotalSeats());
            stmt.setInt(10, bus.getAvailableSeats());
            stmt.setString(11, bus.getBusType());
            stmt.setString(12, bus.getAmenities());
            stmt.setBoolean(13, bus.isActive());
            stmt.setInt(14, bus.getId());
            
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                System.out.println("✓ Bus updated: " + bus.getBusName());
                return true;
            }
        } catch (SQLException e) {
            System.err.println("✗ Failed to update bus: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Delete a bus
     */
    public boolean deleteBus(int busId) {
        String sql = "DELETE FROM buses WHERE id = ?";
        
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, busId);
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                System.out.println("✓ Bus deleted: ID " + busId);
                return true;
            }
        } catch (SQLException e) {
            System.err.println("✗ Failed to delete bus: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Get bus by ID
     */
    public Bus findById(int id) {
        String sql = "SELECT * FROM buses WHERE id = ?";
        
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToBus(rs);
            }
        } catch (SQLException e) {
            System.err.println("✗ Failed to find bus: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * Map ResultSet to Bus object
     */
    private Bus mapResultSetToBus(ResultSet rs) throws SQLException {
        Bus bus = new Bus();
        bus.setId(rs.getInt("id"));
        bus.setProviderId(rs.getInt("provider_id"));
        bus.setBusName(rs.getString("bus_name"));
        bus.setBusNumber(rs.getString("bus_number"));
        bus.setStartPoint(rs.getString("start_point"));
        bus.setEndPoint(rs.getString("end_point"));
        bus.setFare(rs.getDouble("fare"));
        bus.setDistance(rs.getDouble("distance"));
        
        Time scheduleTime = rs.getTime("schedule_time");
        if (scheduleTime != null) {
            bus.setScheduleTime(scheduleTime.toLocalTime());
        }
        
        Time arrivalTime = rs.getTime("arrival_time");
        if (arrivalTime != null) {
            bus.setArrivalTime(arrivalTime.toLocalTime());
        }
        
        bus.setTotalSeats(rs.getInt("total_seats"));
        bus.setAvailableSeats(rs.getInt("available_seats"));
        bus.setBusType(rs.getString("bus_type"));
        bus.setAmenities(rs.getString("amenities"));
        bus.setActive(rs.getBoolean("is_active"));
        
        return bus;
    }
    
    /**
     * Search buses by route (from/to) using bus_stops table
     * This method finds buses that pass through both stops in the correct order
     */
    public List<Bus> searchBuses(String from, String to) {
        List<Bus> buses = new ArrayList<>();
        
        // If both from and to are provided, use bus_stops table for proper route search
        if (from != null && !from.trim().isEmpty() && to != null && !to.trim().isEmpty()) {
            String sql = """
                SELECT DISTINCT b.* 
                FROM buses b
                INNER JOIN bus_stops bs1 ON b.id = bs1.bus_id 
                INNER JOIN bus_stops bs2 ON b.id = bs2.bus_id
                WHERE b.is_active = TRUE
                  AND bs1.stop_name LIKE ?
                  AND bs2.stop_name LIKE ?
                  AND bs1.stop_order < bs2.stop_order
                ORDER BY b.schedule_time
                """;
            
            try (Connection conn = DBConnection.getInstance().getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                
                stmt.setString(1, "%" + from.trim() + "%");
                stmt.setString(2, "%" + to.trim() + "%");
                
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    buses.add(mapResultSetToBus(rs));
                }
            } catch (SQLException e) {
                System.err.println("✗ Failed to search buses by stops: " + e.getMessage());
            }
        } 
        // If only one parameter is provided, fall back to simple search
        else {
            StringBuilder sql = new StringBuilder("SELECT * FROM buses WHERE is_active = TRUE");
            List<String> params = new ArrayList<>();
            
            if (from != null && !from.trim().isEmpty()) {
                sql.append(" AND (start_point LIKE ? OR end_point LIKE ?)");
                params.add("%" + from.trim() + "%");
                params.add("%" + from.trim() + "%");
            }
            
            if (to != null && !to.trim().isEmpty()) {
                sql.append(" AND (start_point LIKE ? OR end_point LIKE ?)");
                params.add("%" + to.trim() + "%");
                params.add("%" + to.trim() + "%");
            }
            
            sql.append(" ORDER BY schedule_time");
            
            try (Connection conn = DBConnection.getInstance().getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
                
                for (int i = 0; i < params.size(); i++) {
                    stmt.setString(i + 1, params.get(i));
                }
                
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    buses.add(mapResultSetToBus(rs));
                }
            } catch (SQLException e) {
                System.err.println("✗ Failed to search buses: " + e.getMessage());
            }
        }
        
        return buses;
    }
    
    /**
     * Search buses that pass through a specific stop
     */
    public List<Bus> searchBusesByStop(String stopName) {
        List<Bus> buses = new ArrayList<>();
        String sql = """
            SELECT DISTINCT b.* 
            FROM buses b
            INNER JOIN bus_stops bs ON b.id = bs.bus_id
            WHERE b.is_active = TRUE
              AND bs.stop_name LIKE ?
            ORDER BY b.schedule_time
            """;
        
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, "%" + stopName.trim() + "%");
            
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                buses.add(mapResultSetToBus(rs));
            }
        } catch (SQLException e) {
            System.err.println("✗ Failed to search buses by stop: " + e.getMessage());
        }
        
        return buses;
    }
    
    /**
     * Get all active buses
     */
    public List<Bus> getAllBuses() {
        List<Bus> buses = new ArrayList<>();
        String sql = "SELECT * FROM buses WHERE is_active = TRUE ORDER BY schedule_time";
        
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                buses.add(mapResultSetToBus(rs));
            }
        } catch (SQLException e) {
            System.err.println("✗ Failed to get all buses: " + e.getMessage());
        }
        return buses;
    }
    
    /**
     * Get all stops for a specific bus route
     */
    public List<BusStop> getBusStops(int busId) {
        List<BusStop> stops = new ArrayList<>();
        String sql = """
            SELECT bs.*, b.bus_name, b.bus_number, b.start_point, b.end_point
            FROM bus_stops bs
            INNER JOIN buses b ON bs.bus_id = b.id
            WHERE bs.bus_id = ?
            ORDER BY bs.stop_order
            """;
        
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, busId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                BusStop stop = new BusStop();
                stop.setId(rs.getInt("id"));
                stop.setBusId(rs.getInt("bus_id"));
                stop.setStopName(rs.getString("stop_name"));
                stop.setStopOrder(rs.getInt("stop_order"));
                stop.setArrivalTimeOffset(rs.getInt("arrival_time_offset"));
                stop.setFareFromStart(rs.getDouble("fare_from_start"));
                stop.setPickupPoint(rs.getBoolean("is_pickup_point"));
                stop.setDropPoint(rs.getBoolean("is_drop_point"));
                stop.setLandmark(rs.getString("landmark"));
                
                // Add bus information
                stop.setBusName(rs.getString("bus_name"));
                stop.setBusNumber(rs.getString("bus_number"));
                stop.setStartPoint(rs.getString("start_point"));
                stop.setEndPoint(rs.getString("end_point"));
                
                stops.add(stop);
            }
        } catch (SQLException e) {
            System.err.println("✗ Failed to get bus stops: " + e.getMessage());
        }
        
        return stops;
    }
    
    /**
     * Get route summary for a specific bus
     */
    public Map<String, Object> getRouteSummary(int busId) {
        Map<String, Object> summary = new HashMap<>();
        String sql = """
            SELECT 
                COUNT(*) as total_stops,
                MAX(fare_from_start) as total_fare,
                MAX(arrival_time_offset) as total_time_minutes,
                b.distance,
                b.bus_name,
                b.bus_number,
                b.start_point,
                b.end_point
            FROM bus_stops bs
            INNER JOIN buses b ON bs.bus_id = b.id
            WHERE bs.bus_id = ?
            GROUP BY b.id, b.distance, b.bus_name, b.bus_number, b.start_point, b.end_point
            """;
        
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, busId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                summary.put("totalStops", rs.getInt("total_stops"));
                summary.put("totalFare", rs.getDouble("total_fare"));
                summary.put("totalTimeMinutes", rs.getInt("total_time_minutes"));
                summary.put("distance", rs.getDouble("distance"));
                summary.put("busName", rs.getString("bus_name"));
                summary.put("busNumber", rs.getString("bus_number"));
                summary.put("startPoint", rs.getString("start_point"));
                summary.put("endPoint", rs.getString("end_point"));
            }
        } catch (SQLException e) {
            System.err.println("✗ Failed to get route summary: " + e.getMessage());
        }
        
        return summary;
    }
}

