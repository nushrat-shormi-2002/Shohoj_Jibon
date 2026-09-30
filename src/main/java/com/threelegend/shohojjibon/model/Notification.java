package com.threelegend.shohojjibon.model;

import javafx.beans.property.*;
import java.time.LocalDateTime;

/**
 * Notification Model (NEW FEATURE)
 * Shohoj Jibon - Makes your life easier
 * Developed by Three_Legend
 */
public class Notification {
    private final IntegerProperty id;
    private final IntegerProperty userId;
    private final StringProperty title;
    private final StringProperty message;
    private final StringProperty notificationType;
    private final BooleanProperty isRead;
    private final IntegerProperty relatedBookingId;
    private LocalDateTime createdAt;
    
    public Notification() {
        this.id = new SimpleIntegerProperty();
        this.userId = new SimpleIntegerProperty();
        this.title = new SimpleStringProperty();
        this.message = new SimpleStringProperty();
        this.notificationType = new SimpleStringProperty();
        this.isRead = new SimpleBooleanProperty(false);
        this.relatedBookingId = new SimpleIntegerProperty();
    }
    
    // Getters and Setters
    public int getId() { return id.get(); }
    public void setId(int id) { this.id.set(id); }
    public IntegerProperty idProperty() { return id; }
    
    public int getUserId() { return userId.get(); }
    public void setUserId(int userId) { this.userId.set(userId); }
    public IntegerProperty userIdProperty() { return userId; }
    
    public String getTitle() { return title.get(); }
    public void setTitle(String title) { this.title.set(title); }
    public StringProperty titleProperty() { return title; }
    
    public String getMessage() { return message.get(); }
    public void setMessage(String message) { this.message.set(message); }
    public StringProperty messageProperty() { return message; }
    
    public String getNotificationType() { return notificationType.get(); }
    public void setNotificationType(String notificationType) { this.notificationType.set(notificationType); }
    public StringProperty notificationTypeProperty() { return notificationType; }
    
    public boolean isRead() { return isRead.get(); }
    public void setRead(boolean isRead) { this.isRead.set(isRead); }
    public BooleanProperty isReadProperty() { return isRead; }
    
    public int getRelatedBookingId() { return relatedBookingId.get(); }
    public void setRelatedBookingId(int relatedBookingId) { this.relatedBookingId.set(relatedBookingId); }
    public IntegerProperty relatedBookingIdProperty() { return relatedBookingId; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    // Helper method to get icon based on type
    public String getTypeIcon() {
        return switch (getNotificationType()) {
            case "BOOKING" -> "📅";
            case "PAYMENT" -> "💳";
            case "REVIEW" -> "⭐";
            case "PROMOTION" -> "🎉";
            default -> "ℹ️";
        };
    }
}

