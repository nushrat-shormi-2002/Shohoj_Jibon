package com.threelegend.shohojjibon.model;

import javafx.beans.property.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;

/**
 * Booking Model
 * Shohoj Jibon - Makes your life easier
 * Developed by Three_Legend
 */
public class Booking {
    private final IntegerProperty id;
    private final IntegerProperty userId;
    private final IntegerProperty providerId;
    private final StringProperty serviceType;
    private final IntegerProperty serviceId;
    private final ObjectProperty<LocalDate> bookingDate;
    private final ObjectProperty<LocalTime> bookingTime;
    private final ObjectProperty<LocalDate> endDate;
    private final ObjectProperty<LocalDateTime> serviceDate;
    private final IntegerProperty quantity;
    private final IntegerProperty numberOfPeople;
    private final DoubleProperty totalAmount;
    private final StringProperty status;
    private final StringProperty paymentStatus;
    private final StringProperty paymentMethod;
    private final StringProperty transactionId;
    private final StringProperty notes;
    private final StringProperty specialRequests;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Additional display properties
    private final StringProperty serviceName;
    
    public Booking() {
        this.id = new SimpleIntegerProperty();
        this.userId = new SimpleIntegerProperty();
        this.providerId = new SimpleIntegerProperty();
        this.serviceType = new SimpleStringProperty();
        this.serviceId = new SimpleIntegerProperty();
        this.bookingDate = new SimpleObjectProperty<>();
        this.bookingTime = new SimpleObjectProperty<>();
        this.endDate = new SimpleObjectProperty<>();
        this.serviceDate = new SimpleObjectProperty<>();
        this.quantity = new SimpleIntegerProperty();
        this.numberOfPeople = new SimpleIntegerProperty();
        this.totalAmount = new SimpleDoubleProperty();
        this.status = new SimpleStringProperty();
        this.paymentStatus = new SimpleStringProperty();
        this.paymentMethod = new SimpleStringProperty();
        this.transactionId = new SimpleStringProperty();
        this.notes = new SimpleStringProperty();
        this.specialRequests = new SimpleStringProperty();
        this.serviceName = new SimpleStringProperty();
    }
    
    // Getters and Setters
    public int getId() { return id.get(); }
    public void setId(int id) { this.id.set(id); }
    public IntegerProperty idProperty() { return id; }
    
    public int getUserId() { return userId.get(); }
    public void setUserId(int userId) { this.userId.set(userId); }
    public IntegerProperty userIdProperty() { return userId; }
    
    public int getProviderId() { return providerId.get(); }
    public void setProviderId(int providerId) { this.providerId.set(providerId); }
    public IntegerProperty providerIdProperty() { return providerId; }
    
    public String getServiceType() { return serviceType.get(); }
    public void setServiceType(String serviceType) { this.serviceType.set(serviceType); }
    public StringProperty serviceTypeProperty() { return serviceType; }
    
    public int getServiceId() { return serviceId.get(); }
    public void setServiceId(int serviceId) { this.serviceId.set(serviceId); }
    public IntegerProperty serviceIdProperty() { return serviceId; }
    
    public LocalDate getBookingDate() { return bookingDate.get(); }
    public void setBookingDate(LocalDate bookingDate) { this.bookingDate.set(bookingDate); }
    public ObjectProperty<LocalDate> bookingDateProperty() { return bookingDate; }
    
    public LocalTime getBookingTime() { return bookingTime.get(); }
    public void setBookingTime(LocalTime bookingTime) { this.bookingTime.set(bookingTime); }
    public ObjectProperty<LocalTime> bookingTimeProperty() { return bookingTime; }
    
    public LocalDate getEndDate() { return endDate.get(); }
    public void setEndDate(LocalDate endDate) { this.endDate.set(endDate); }
    public ObjectProperty<LocalDate> endDateProperty() { return endDate; }
    
    public LocalDateTime getServiceDate() { return serviceDate.get(); }
    public void setServiceDate(LocalDateTime serviceDate) { this.serviceDate.set(serviceDate); }
    public ObjectProperty<LocalDateTime> serviceDateProperty() { return serviceDate; }
    
    public int getQuantity() { return quantity.get(); }
    public void setQuantity(int quantity) { this.quantity.set(quantity); }
    public IntegerProperty quantityProperty() { return quantity; }
    
    public int getNumberOfPeople() { return numberOfPeople.get(); }
    public void setNumberOfPeople(int numberOfPeople) { this.numberOfPeople.set(numberOfPeople); }
    public IntegerProperty numberOfPeopleProperty() { return numberOfPeople; }
    
    public double getTotalAmount() { return totalAmount.get(); }
    public void setTotalAmount(double totalAmount) { this.totalAmount.set(totalAmount); }
    public DoubleProperty totalAmountProperty() { return totalAmount; }
    
    public String getStatus() { return status.get(); }
    public void setStatus(String status) { this.status.set(status); }
    public StringProperty statusProperty() { return status; }
    
    public String getPaymentStatus() { return paymentStatus.get(); }
    public void setPaymentStatus(String paymentStatus) { this.paymentStatus.set(paymentStatus); }
    public StringProperty paymentStatusProperty() { return paymentStatus; }
    
    public String getPaymentMethod() { return paymentMethod.get(); }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod.set(paymentMethod); }
    public StringProperty paymentMethodProperty() { return paymentMethod; }
    
    public String getTransactionId() { return transactionId.get(); }
    public void setTransactionId(String transactionId) { this.transactionId.set(transactionId); }
    public StringProperty transactionIdProperty() { return transactionId; }
    
    public String getNotes() { return notes.get(); }
    public void setNotes(String notes) { this.notes.set(notes); }
    public StringProperty notesProperty() { return notes; }
    
    public String getSpecialRequests() { return specialRequests.get(); }
    public void setSpecialRequests(String specialRequests) { this.specialRequests.set(specialRequests); }
    public StringProperty specialRequestsProperty() { return specialRequests; }
    
    public String getServiceName() { return serviceName.get(); }
    public void setServiceName(String serviceName) { this.serviceName.set(serviceName); }
    public StringProperty serviceNameProperty() { return serviceName; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    
    // Formatted properties
    public String getFormattedAmount() {
        return String.format("৳%.2f", getTotalAmount());
    }
    
    public String getBookingReference() {
        return String.format("#BK%05d", getId());
    }
}

