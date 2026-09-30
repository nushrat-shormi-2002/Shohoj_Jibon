package com.threelegend.shohojjibon.model;

import javafx.beans.property.*;

/**
 * Doctor Model
 * Shohoj Jibon - Makes your life easier
 * Developed by Three_Legend
 */
public class Doctor {
    private final IntegerProperty id;
    private final IntegerProperty providerId;
    private final StringProperty name;
    private final StringProperty specialty;
    private final StringProperty qualifications;
    private final IntegerProperty experienceYears;
    private final StringProperty location;
    private final DoubleProperty consultationFee;
    private final StringProperty availableDays;
    private final StringProperty availableTime;
    private final StringProperty contactNumber;
    private final StringProperty email;
    private final StringProperty languagesSpoken;
    private final DoubleProperty rating;
    private final IntegerProperty totalReviews;
    private final BooleanProperty isAvailable;
    
    public Doctor() {
        this.id = new SimpleIntegerProperty();
        this.providerId = new SimpleIntegerProperty();
        this.name = new SimpleStringProperty();
        this.specialty = new SimpleStringProperty();
        this.qualifications = new SimpleStringProperty();
        this.experienceYears = new SimpleIntegerProperty();
        this.location = new SimpleStringProperty();
        this.consultationFee = new SimpleDoubleProperty();
        this.availableDays = new SimpleStringProperty();
        this.availableTime = new SimpleStringProperty();
        this.contactNumber = new SimpleStringProperty();
        this.email = new SimpleStringProperty();
        this.languagesSpoken = new SimpleStringProperty();
        this.rating = new SimpleDoubleProperty(0.0);
        this.totalReviews = new SimpleIntegerProperty(0);
        this.isAvailable = new SimpleBooleanProperty(true);
    }
    
    // Getters and Setters
    public int getId() { return id.get(); }
    public void setId(int id) { this.id.set(id); }
    public IntegerProperty idProperty() { return id; }
    
    public int getProviderId() { return providerId.get(); }
    public void setProviderId(int providerId) { this.providerId.set(providerId); }
    public IntegerProperty providerIdProperty() { return providerId; }
    
    public String getName() { return name.get(); }
    public void setName(String name) { this.name.set(name); }
    public StringProperty nameProperty() { return name; }
    
    public String getSpecialty() { return specialty.get(); }
    public void setSpecialty(String specialty) { this.specialty.set(specialty); }
    public StringProperty specialtyProperty() { return specialty; }
    
    public String getQualifications() { return qualifications.get(); }
    public void setQualifications(String qualifications) { this.qualifications.set(qualifications); }
    public StringProperty qualificationsProperty() { return qualifications; }
    
    public int getExperienceYears() { return experienceYears.get(); }
    public void setExperienceYears(int experienceYears) { this.experienceYears.set(experienceYears); }
    public IntegerProperty experienceYearsProperty() { return experienceYears; }
    
    public String getLocation() { return location.get(); }
    public void setLocation(String location) { this.location.set(location); }
    public StringProperty locationProperty() { return location; }
    
    public double getConsultationFee() { return consultationFee.get(); }
    public void setConsultationFee(double consultationFee) { this.consultationFee.set(consultationFee); }
    public DoubleProperty consultationFeeProperty() { return consultationFee; }
    
    public String getAvailableDays() { return availableDays.get(); }
    public void setAvailableDays(String availableDays) { this.availableDays.set(availableDays); }
    public StringProperty availableDaysProperty() { return availableDays; }
    
    public String getAvailableTime() { return availableTime.get(); }
    public void setAvailableTime(String availableTime) { this.availableTime.set(availableTime); }
    public StringProperty availableTimeProperty() { return availableTime; }
    
    public String getContactNumber() { return contactNumber.get(); }
    public void setContactNumber(String contactNumber) { this.contactNumber.set(contactNumber); }
    public StringProperty contactNumberProperty() { return contactNumber; }
    
    public String getEmail() { return email.get(); }
    public void setEmail(String email) { this.email.set(email); }
    public StringProperty emailProperty() { return email; }
    
    public String getLanguagesSpoken() { return languagesSpoken.get(); }
    public void setLanguagesSpoken(String languagesSpoken) { this.languagesSpoken.set(languagesSpoken); }
    public StringProperty languagesSpokenProperty() { return languagesSpoken; }
    
    public double getRating() { return rating.get(); }
    public void setRating(double rating) { this.rating.set(rating); }
    public DoubleProperty ratingProperty() { return rating; }
    
    public int getTotalReviews() { return totalReviews.get(); }
    public void setTotalReviews(int totalReviews) { this.totalReviews.set(totalReviews); }
    public IntegerProperty totalReviewsProperty() { return totalReviews; }
    
    public boolean isAvailable() { return isAvailable.get(); }
    public void setAvailable(boolean isAvailable) { this.isAvailable.set(isAvailable); }
    public BooleanProperty isAvailableProperty() { return isAvailable; }
    
    // Formatted properties
    public String getFormattedFee() {
        return String.format("৳%.2f", getConsultationFee());
    }
    
    public String getFormattedRating() {
        return String.format("%.1f ★ (%d reviews)", getRating(), getTotalReviews());
    }
}

