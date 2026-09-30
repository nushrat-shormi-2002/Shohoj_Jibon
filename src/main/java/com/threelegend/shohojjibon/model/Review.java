package com.threelegend.shohojjibon.model;

import javafx.beans.property.*;
import java.time.LocalDateTime;

/**
 * Review Model (NEW FEATURE)
 * Shohoj Jibon - Makes your life easier
 * Developed by Three_Legend
 */
public class Review {
    private final IntegerProperty id;
    private final IntegerProperty userId;
    private final StringProperty serviceType;
    private final IntegerProperty serviceId;
    private final IntegerProperty bookingId;
    private final DoubleProperty rating;
    private final StringProperty reviewText;
    private final BooleanProperty isVerified;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Additional display properties
    private final StringProperty userName;
    private final StringProperty serviceName;
    
    public Review() {
        this.id = new SimpleIntegerProperty();
        this.userId = new SimpleIntegerProperty();
        this.serviceType = new SimpleStringProperty();
        this.serviceId = new SimpleIntegerProperty();
        this.bookingId = new SimpleIntegerProperty();
        this.rating = new SimpleDoubleProperty();
        this.reviewText = new SimpleStringProperty();
        this.isVerified = new SimpleBooleanProperty(false);
        this.userName = new SimpleStringProperty();
        this.serviceName = new SimpleStringProperty();
    }
    
    // Getters and Setters
    public int getId() { return id.get(); }
    public void setId(int id) { this.id.set(id); }
    public IntegerProperty idProperty() { return id; }
    
    public int getUserId() { return userId.get(); }
    public void setUserId(int userId) { this.userId.set(userId); }
    public IntegerProperty userIdProperty() { return userId; }
    
    public String getServiceType() { return serviceType.get(); }
    public void setServiceType(String serviceType) { this.serviceType.set(serviceType); }
    public StringProperty serviceTypeProperty() { return serviceType; }
    
    public int getServiceId() { return serviceId.get(); }
    public void setServiceId(int serviceId) { this.serviceId.set(serviceId); }
    public IntegerProperty serviceIdProperty() { return serviceId; }
    
    public int getBookingId() { return bookingId.get(); }
    public void setBookingId(int bookingId) { this.bookingId.set(bookingId); }
    public IntegerProperty bookingIdProperty() { return bookingId; }
    
    public double getRating() { return rating.get(); }
    public void setRating(double rating) { this.rating.set(rating); }
    public DoubleProperty ratingProperty() { return rating; }
    
    public String getReviewText() { return reviewText.get(); }
    public void setReviewText(String reviewText) { this.reviewText.set(reviewText); }
    public StringProperty reviewTextProperty() { return reviewText; }
    
    public boolean isVerified() { return isVerified.get(); }
    public void setVerified(boolean isVerified) { this.isVerified.set(isVerified); }
    public BooleanProperty isVerifiedProperty() { return isVerified; }
    
    public String getUserName() { return userName.get(); }
    public void setUserName(String userName) { this.userName.set(userName); }
    public StringProperty userNameProperty() { return userName; }
    
    public String getServiceName() { return serviceName.get(); }
    public void setServiceName(String serviceName) { this.serviceName.set(serviceName); }
    public StringProperty serviceNameProperty() { return serviceName; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    
    // Formatted properties
    public String getFormattedRating() {
        return String.format("%.1f ★", getRating());
    }
    
    public String getStarDisplay() {
        int fullStars = (int) getRating();
        StringBuilder stars = new StringBuilder();
        for (int i = 0; i < fullStars; i++) {
            stars.append("★");
        }
        for (int i = fullStars; i < 5; i++) {
            stars.append("☆");
        }
        return stars.toString();
    }
}

