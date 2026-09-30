package com.threelegend.shohojjibon.model;

import javafx.beans.property.*;
import java.time.LocalTime;

/**
 * Hotel Model
 * Shohoj Jibon - Makes your life easier
 * Developed by Three_Legend
 */
public class Hotel {
    private final IntegerProperty id;
    private final IntegerProperty providerId;
    private final StringProperty hotelName;
    private final StringProperty location;
    private final StringProperty address;
    private final StringProperty roomType;
    private final DoubleProperty pricePerNight;
    private final DoubleProperty rating;
    private final IntegerProperty totalRooms;
    private final IntegerProperty availableRooms;
    private final StringProperty amenities;
    private final StringProperty roomSize;
    private final IntegerProperty maxOccupancy;
    private final StringProperty contactNumber;
    private final StringProperty email;
    private final ObjectProperty<LocalTime> checkInTime;
    private final ObjectProperty<LocalTime> checkOutTime;
    private final StringProperty cancellationPolicy;
    private final BooleanProperty isAvailable;
    
    public Hotel() {
        this.id = new SimpleIntegerProperty();
        this.providerId = new SimpleIntegerProperty();
        this.hotelName = new SimpleStringProperty();
        this.location = new SimpleStringProperty();
        this.address = new SimpleStringProperty();
        this.roomType = new SimpleStringProperty();
        this.pricePerNight = new SimpleDoubleProperty();
        this.rating = new SimpleDoubleProperty(0.0);
        this.totalRooms = new SimpleIntegerProperty();
        this.availableRooms = new SimpleIntegerProperty();
        this.amenities = new SimpleStringProperty();
        this.roomSize = new SimpleStringProperty();
        this.maxOccupancy = new SimpleIntegerProperty();
        this.contactNumber = new SimpleStringProperty();
        this.email = new SimpleStringProperty();
        this.checkInTime = new SimpleObjectProperty<>();
        this.checkOutTime = new SimpleObjectProperty<>();
        this.cancellationPolicy = new SimpleStringProperty();
        this.isAvailable = new SimpleBooleanProperty(true);
    }
    
    // Getters and Setters
    public int getId() { return id.get(); }
    public void setId(int id) { this.id.set(id); }
    public IntegerProperty idProperty() { return id; }
    
    public int getProviderId() { return providerId.get(); }
    public void setProviderId(int providerId) { this.providerId.set(providerId); }
    public IntegerProperty providerIdProperty() { return providerId; }
    
    public String getHotelName() { return hotelName.get(); }
    public void setHotelName(String hotelName) { this.hotelName.set(hotelName); }
    public StringProperty hotelNameProperty() { return hotelName; }
    
    public String getLocation() { return location.get(); }
    public void setLocation(String location) { this.location.set(location); }
    public StringProperty locationProperty() { return location; }
    
    public String getAddress() { return address.get(); }
    public void setAddress(String address) { this.address.set(address); }
    public StringProperty addressProperty() { return address; }
    
    public String getRoomType() { return roomType.get(); }
    public void setRoomType(String roomType) { this.roomType.set(roomType); }
    public StringProperty roomTypeProperty() { return roomType; }
    
    public double getPricePerNight() { return pricePerNight.get(); }
    public void setPricePerNight(double pricePerNight) { this.pricePerNight.set(pricePerNight); }
    public DoubleProperty pricePerNightProperty() { return pricePerNight; }
    
    public double getRating() { return rating.get(); }
    public void setRating(double rating) { this.rating.set(rating); }
    public DoubleProperty ratingProperty() { return rating; }
    
    public int getTotalRooms() { return totalRooms.get(); }
    public void setTotalRooms(int totalRooms) { this.totalRooms.set(totalRooms); }
    public IntegerProperty totalRoomsProperty() { return totalRooms; }
    
    public int getAvailableRooms() { return availableRooms.get(); }
    public void setAvailableRooms(int availableRooms) { this.availableRooms.set(availableRooms); }
    public IntegerProperty availableRoomsProperty() { return availableRooms; }
    
    public String getAmenities() { return amenities.get(); }
    public void setAmenities(String amenities) { this.amenities.set(amenities); }
    public StringProperty amenitiesProperty() { return amenities; }
    
    public String getRoomSize() { return roomSize.get(); }
    public void setRoomSize(String roomSize) { this.roomSize.set(roomSize); }
    public StringProperty roomSizeProperty() { return roomSize; }
    
    public int getMaxOccupancy() { return maxOccupancy.get(); }
    public void setMaxOccupancy(int maxOccupancy) { this.maxOccupancy.set(maxOccupancy); }
    public IntegerProperty maxOccupancyProperty() { return maxOccupancy; }
    
    public String getContactNumber() { return contactNumber.get(); }
    public void setContactNumber(String contactNumber) { this.contactNumber.set(contactNumber); }
    public StringProperty contactNumberProperty() { return contactNumber; }
    
    public String getEmail() { return email.get(); }
    public void setEmail(String email) { this.email.set(email); }
    public StringProperty emailProperty() { return email; }
    
    public LocalTime getCheckInTime() { return checkInTime.get(); }
    public void setCheckInTime(LocalTime checkInTime) { this.checkInTime.set(checkInTime); }
    public ObjectProperty<LocalTime> checkInTimeProperty() { return checkInTime; }
    
    public LocalTime getCheckOutTime() { return checkOutTime.get(); }
    public void setCheckOutTime(LocalTime checkOutTime) { this.checkOutTime.set(checkOutTime); }
    public ObjectProperty<LocalTime> checkOutTimeProperty() { return checkOutTime; }
    
    public String getCancellationPolicy() { return cancellationPolicy.get(); }
    public void setCancellationPolicy(String cancellationPolicy) { this.cancellationPolicy.set(cancellationPolicy); }
    public StringProperty cancellationPolicyProperty() { return cancellationPolicy; }
    
    public boolean isAvailable() { return isAvailable.get(); }
    public void setAvailable(boolean isAvailable) { this.isAvailable.set(isAvailable); }
    public BooleanProperty isAvailableProperty() { return isAvailable; }
    
    // Formatted properties
    public String getFormattedPrice() {
        return String.format("৳%.2f/night", getPricePerNight());
    }
    
    public String getFormattedRating() {
        return String.format("%.1f ★", getRating());
    }
}

