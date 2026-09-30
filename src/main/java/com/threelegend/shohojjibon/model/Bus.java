package com.threelegend.shohojjibon.model;

import javafx.beans.property.*;
import java.time.LocalTime;

/**
 * Bus Model
 * Shohoj Jibon - Makes your life easier
 * Developed by Three_Legend
 */
public class Bus {
    private final IntegerProperty id;
    private final IntegerProperty providerId;
    private final StringProperty busName;
    private final StringProperty busNumber;
    private final StringProperty startPoint;
    private final StringProperty endPoint;
    private final DoubleProperty fare;
    private final DoubleProperty distance;
    private final ObjectProperty<LocalTime> scheduleTime;
    private final ObjectProperty<LocalTime> arrivalTime;
    private final IntegerProperty totalSeats;
    private final IntegerProperty availableSeats;
    private final StringProperty busType;
    private final StringProperty amenities;
    private final BooleanProperty isActive;
    
    public Bus() {
        this.id = new SimpleIntegerProperty();
        this.providerId = new SimpleIntegerProperty();
        this.busName = new SimpleStringProperty();
        this.busNumber = new SimpleStringProperty();
        this.startPoint = new SimpleStringProperty();
        this.endPoint = new SimpleStringProperty();
        this.fare = new SimpleDoubleProperty();
        this.distance = new SimpleDoubleProperty();
        this.scheduleTime = new SimpleObjectProperty<>();
        this.arrivalTime = new SimpleObjectProperty<>();
        this.totalSeats = new SimpleIntegerProperty();
        this.availableSeats = new SimpleIntegerProperty();
        this.busType = new SimpleStringProperty();
        this.amenities = new SimpleStringProperty();
        this.isActive = new SimpleBooleanProperty(true);
    }
    
    // ID
    public int getId() { return id.get(); }
    public void setId(int id) { this.id.set(id); }
    public IntegerProperty idProperty() { return id; }
    
    // Provider ID
    public int getProviderId() { return providerId.get(); }
    public void setProviderId(int providerId) { this.providerId.set(providerId); }
    public IntegerProperty providerIdProperty() { return providerId; }
    
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
    
    // Fare
    public double getFare() { return fare.get(); }
    public void setFare(double fare) { this.fare.set(fare); }
    public DoubleProperty fareProperty() { return fare; }
    
    // Distance
    public double getDistance() { return distance.get(); }
    public void setDistance(double distance) { this.distance.set(distance); }
    public DoubleProperty distanceProperty() { return distance; }
    
    // Schedule Time
    public LocalTime getScheduleTime() { return scheduleTime.get(); }
    public void setScheduleTime(LocalTime scheduleTime) { this.scheduleTime.set(scheduleTime); }
    public ObjectProperty<LocalTime> scheduleTimeProperty() { return scheduleTime; }
    
    // Arrival Time
    public LocalTime getArrivalTime() { return arrivalTime.get(); }
    public void setArrivalTime(LocalTime arrivalTime) { this.arrivalTime.set(arrivalTime); }
    public ObjectProperty<LocalTime> arrivalTimeProperty() { return arrivalTime; }
    
    // Total Seats
    public int getTotalSeats() { return totalSeats.get(); }
    public void setTotalSeats(int totalSeats) { this.totalSeats.set(totalSeats); }
    public IntegerProperty totalSeatsProperty() { return totalSeats; }
    
    // Available Seats
    public int getAvailableSeats() { return availableSeats.get(); }
    public void setAvailableSeats(int availableSeats) { this.availableSeats.set(availableSeats); }
    public IntegerProperty availableSeatsProperty() { return availableSeats; }
    
    // Bus Type
    public String getBusType() { return busType.get(); }
    public void setBusType(String busType) { this.busType.set(busType); }
    public StringProperty busTypeProperty() { return busType; }
    
    // Amenities
    public String getAmenities() { return amenities.get(); }
    public void setAmenities(String amenities) { this.amenities.set(amenities); }
    public StringProperty amenitiesProperty() { return amenities; }
    
    // Is Active
    public boolean isActive() { return isActive.get(); }
    public void setActive(boolean isActive) { this.isActive.set(isActive); }
    public BooleanProperty isActiveProperty() { return isActive; }
    
    // Formatted properties for display
    public String getFormattedFare() {
        return String.format("৳%.2f", getFare());
    }
    
    public String getFormattedDistance() {
        return String.format("%.2f km", getDistance());
    }
    
    public String getRoute() {
        return getStartPoint() + " → " + getEndPoint();
    }
    
    public StringProperty routeProperty() {
        return new SimpleStringProperty(getRoute());
    }
}

