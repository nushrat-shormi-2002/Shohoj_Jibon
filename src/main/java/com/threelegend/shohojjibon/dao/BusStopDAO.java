package com.threelegend.shohojjibon.dao;

import com.threelegend.shohojjibon.model.BusStop;
import com.threelegend.shohojjibon.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Bus Stop Data Access Object
 * Shohoj Jibon - Makes your life easier
 * Developed by Three_Legend
 */
public class BusStopDAO {
    
    /**
     * Get all stops for a bus
     */
    public List<BusStop> getStopsByBusId(int busId) {
        List<BusStop> stops = new ArrayList<>();
        String sql = "SELECT * FROM bus_stops WHERE bus_id = ? ORDER BY stop_order ASC";
        
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, busId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                stops.add(mapResultSetToBusStop(rs));
            }
        } catch (SQLException e) {
            System.err.println("✗ Failed to get bus stops: " + e.getMessage());
            e.printStackTrace();
        }
        return stops;
    }
    
    /**
     * Create a new bus stop
     */
    public boolean createBusStop(BusStop stop) {
        String sql = "INSERT INTO bus_stops (bus_id, stop_name, stop_order, arrival_time_offset, fare_from_start, is_pickup_point, is_drop_point, landmark) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setInt(1, stop.getBusId());
            stmt.setString(2, stop.getStopName());
            stmt.setInt(3, stop.getStopOrder());
            stmt.setInt(4, stop.getArrivalTimeOffset());
            stmt.setDouble(5, stop.getFareFromStart());
            stmt.setBoolean(6, stop.isPickupPoint());
            stmt.setBoolean(7, stop.isDropPoint());
            stmt.setString(8, stop.getLandmark());
            
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    stop.setId(rs.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("✗ Failed to create bus stop: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Update bus stop
     */
    public boolean updateBusStop(BusStop stop) {
        String sql = "UPDATE bus_stops SET stop_name = ?, stop_order = ?, arrival_time_offset = ?, fare_from_start = ?, is_pickup_point = ?, is_drop_point = ?, landmark = ? WHERE id = ?";
        
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, stop.getStopName());
            stmt.setInt(2, stop.getStopOrder());
            stmt.setInt(3, stop.getArrivalTimeOffset());
            stmt.setDouble(4, stop.getFareFromStart());
            stmt.setBoolean(5, stop.isPickupPoint());
            stmt.setBoolean(6, stop.isDropPoint());
            stmt.setString(7, stop.getLandmark());
            stmt.setInt(8, stop.getId());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("✗ Failed to update bus stop: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Delete a bus stop
     */
    public boolean deleteBusStop(int stopId) {
        String sql = "DELETE FROM bus_stops WHERE id = ?";
        
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, stopId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("✗ Failed to delete bus stop: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Delete all stops for a bus
     */
    public boolean deleteAllStopsForBus(int busId) {
        String sql = "DELETE FROM bus_stops WHERE bus_id = ?";
        
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, busId);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("✗ Failed to delete bus stops: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Search buses by stop name
     */
    public List<Integer> searchBusesByStop(String stopName) {
        List<Integer> busIds = new ArrayList<>();
        String sql = "SELECT DISTINCT bus_id FROM bus_stops WHERE stop_name LIKE ? ORDER BY bus_id";
        
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, "%" + stopName + "%");
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                busIds.add(rs.getInt("bus_id"));
            }
        } catch (SQLException e) {
            System.err.println("✗ Failed to search buses by stop: " + e.getMessage());
            e.printStackTrace();
        }
        return busIds;
    }
    
    /**
     * Get stops between two points
     */
    public List<BusStop> getStopsBetween(int busId, String fromStop, String toStop) {
        List<BusStop> stops = new ArrayList<>();
        String sql = "SELECT * FROM bus_stops WHERE bus_id = ? AND stop_order BETWEEN " +
                     "(SELECT stop_order FROM bus_stops WHERE bus_id = ? AND stop_name = ?) AND " +
                     "(SELECT stop_order FROM bus_stops WHERE bus_id = ? AND stop_name = ?) " +
                     "ORDER BY stop_order ASC";
        
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, busId);
            stmt.setInt(2, busId);
            stmt.setString(3, fromStop);
            stmt.setInt(4, busId);
            stmt.setString(5, toStop);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                stops.add(mapResultSetToBusStop(rs));
            }
        } catch (SQLException e) {
            System.err.println("✗ Failed to get stops between points: " + e.getMessage());
            e.printStackTrace();
        }
        return stops;
    }
    
    /**
     * Map ResultSet to BusStop object
     */
    private BusStop mapResultSetToBusStop(ResultSet rs) throws SQLException {
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
        return stop;
    }
}

