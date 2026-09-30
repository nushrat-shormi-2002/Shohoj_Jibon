package com.threelegend.shohojjibon.model;

import javafx.beans.property.*;

/**
 * Bus Stop Model - For managing multiple stops on bus routes
 * Shohoj Jibon - Makes your life easier
 * Developed by Three_Legend
 */
public class BusStop {
    private final IntegerProperty id;
    private final IntegerProperty busId;
    private final StringProperty stopName;
    private final IntegerProperty stopOrder;
    private final IntegerProperty arrivalTimeOffset; // Minutes from start
    private final DoubleProperty fareFromStart;
    private final BooleanProperty isPickupPoint;
    private final BooleanProperty isDropPoint;
    private final StringProperty landmark;
    
    // Additional bus information for display
    private final StringProperty busName;
    private final StringProperty busNumber;
    private final StringProperty startPoint;
    private final StringProperty endPoint;
    
    public BusStop() {
        this.id = new SimpleIntegerProperty();
        this.busId = new SimpleIntegerProperty();
        this.stopName = new SimpleStringProperty();
        this.stopOrder = new SimpleIntegerProperty();
        this.arrivalTimeOffset = new SimpleIntegerProperty();
        this.fareFromStart = new SimpleDoubleProperty();
        this.isPickupPoint = new SimpleBooleanProperty(true);
        this.isDropPoint = new SimpleBooleanProperty(true);
        this.landmark = new SimpleStringProperty();
        this.busName = new SimpleStringProperty();
        this.busNumber = new SimpleStringProperty();
        this.startPoint = new SimpleStringProperty();
        this.endPoint = new SimpleStringProperty();
    }
    
    // ID
    public int getId() { return id.get(); }
    public void setId(int id) { this.id.set(id); }
    public IntegerProperty idProperty() { return id; }
    
    // Bus ID
    public int getBusId() { return busId.get(); }
    public void setBusId(int busId) { this.busId.set(busId); }
    public IntegerProperty busIdProperty() { return busId; }
    
    // Stop Name
    public String getStopName() { return stopName.get(); }
    public void setStopName(String stopName) { this.stopName.set(stopName); }
    public StringProperty stopNameProperty() { return stopName; }
    
    // Stop Order
    public int getStopOrder() { return stopOrder.get(); }
    public void setStopOrder(int stopOrder) { this.stopOrder.set(stopOrder); }
    public IntegerProperty stopOrderProperty() { return stopOrder; }
    
    // Arrival Time Offset
    public int getArrivalTimeOffset() { return arrivalTimeOffset.get(); }
    public void setArrivalTimeOffset(int arrivalTimeOffset) { this.arrivalTimeOffset.set(arrivalTimeOffset); }
    public IntegerProperty arrivalTimeOffsetProperty() { return arrivalTimeOffset; }
    
    // Fare From Start
    public double getFareFromStart() { return fareFromStart.get(); }
    public void setFareFromStart(double fareFromStart) { this.fareFromStart.set(fareFromStart); }
    public DoubleProperty fareFromStartProperty() { return fareFromStart; }
    
    // Is Pickup Point
    public boolean isPickupPoint() { return isPickupPoint.get(); }
    public void setPickupPoint(boolean isPickupPoint) { this.isPickupPoint.set(isPickupPoint); }
    public BooleanProperty isPickupPointProperty() { return isPickupPoint; }
    
    // Is Drop Point
    public boolean isDropPoint() { return isDropPoint.get(); }
    public void setDropPoint(boolean isDropPoint) { this.isDropPoint.set(isDropPoint); }
    public BooleanProperty isDropPointProperty() { return isDropPoint; }
    
    // Landmark
    public String getLandmark() { return landmark.get(); }
    public void setLandmark(String landmark) { this.landmark.set(landmark); }
    public StringProperty landmarkProperty() { return landmark; }
    
    // Bus Name
    public String getBusName() { return busName.get(); }
    public void setBusName(String busName) { this.busName.set(busName); }
    public StringProperty busNameProperty() { return busName; }
    
    // Bus Number
    public String getBusNumber() { return busNumber.get(); }
    public void setBusNumber(String busNumber) { this.busNumber.set(busNumber); }
    public StringProperty busNumberProperty() { return busNumber; }
    
    // Start Point
    public String getStartPoint() { return startPoint.get(); }
    public void setStartPoint(String startPoint) { this.startPoint.set(startPoint); }
    public StringProperty startPointProperty() { return startPoint; }
    
    // End Point
    public String getEndPoint() { return endPoint.get(); }
    public void setEndPoint(String endPoint) { this.endPoint.set(endPoint); }
    public StringProperty endPointProperty() { return endPoint; }
    
    // Helper methods
    public String getFormattedFare() {
        return String.format("৳%.2f", getFareFromStart());
    }
    
    public String getArrivalTimeFormatted() {
        int minutes = getArrivalTimeOffset();
        int hours = minutes / 60;
        int mins = minutes % 60;
        return String.format("+%dh %dm", hours, mins);
    }
    
    public String getStopType() {
        if (isPickupPoint() && isDropPoint()) {
            return "Both";
        } else if (isPickupPoint()) {
            return "Pickup Only";
        } else if (isDropPoint()) {
            return "Drop Only";
        }
        return "N/A";
    }
    
    @Override
    public String toString() {
        return String.format("%d. %s (%s)", getStopOrder(), getStopName(), getFormattedFare());
    }
}

