package com.threelegend.shohojjibon.model;

import javafx.beans.property.*;
import java.time.LocalDate;

/**
 * Blood Donor Model
 * Shohoj Jibon - Makes your life easier
 * Developed by Three_Legend
 */
public class BloodDonor {
    private final IntegerProperty id;
    private final IntegerProperty providerId;
    private final StringProperty donorName;
    private final StringProperty bloodGroup;
    private final IntegerProperty age;
    private final StringProperty gender;
    private final StringProperty contactNumber;
    private final StringProperty email;
    private final StringProperty location;
    private final StringProperty address;
    private final ObjectProperty<LocalDate> lastDonationDate;
    private final BooleanProperty isAvailable;
    private final StringProperty medicalHistory;
    private final StringProperty emergencyContact;
    private final DoubleProperty weight;
    private final BooleanProperty hasDisease;
    private final IntegerProperty totalDonations;
    
    public BloodDonor() {
        this.id = new SimpleIntegerProperty();
        this.providerId = new SimpleIntegerProperty();
        this.donorName = new SimpleStringProperty();
        this.bloodGroup = new SimpleStringProperty();
        this.age = new SimpleIntegerProperty();
        this.gender = new SimpleStringProperty();
        this.contactNumber = new SimpleStringProperty();
        this.email = new SimpleStringProperty();
        this.location = new SimpleStringProperty();
        this.address = new SimpleStringProperty();
        this.lastDonationDate = new SimpleObjectProperty<>();
        this.isAvailable = new SimpleBooleanProperty(true);
        this.medicalHistory = new SimpleStringProperty();
        this.emergencyContact = new SimpleStringProperty();
        this.weight = new SimpleDoubleProperty();
        this.hasDisease = new SimpleBooleanProperty(false);
        this.totalDonations = new SimpleIntegerProperty(0);
    }
    
    // Getters and Setters
    public int getId() { return id.get(); }
    public void setId(int id) { this.id.set(id); }
    public IntegerProperty idProperty() { return id; }
    
    public int getProviderId() { return providerId.get(); }
    public void setProviderId(int providerId) { this.providerId.set(providerId); }
    public IntegerProperty providerIdProperty() { return providerId; }
    
    public String getDonorName() { return donorName.get(); }
    public void setDonorName(String donorName) { this.donorName.set(donorName); }
    public StringProperty donorNameProperty() { return donorName; }
    
    public String getBloodGroup() { return bloodGroup.get(); }
    public void setBloodGroup(String bloodGroup) { this.bloodGroup.set(bloodGroup); }
    public StringProperty bloodGroupProperty() { return bloodGroup; }
    
    public int getAge() { return age.get(); }
    public void setAge(int age) { this.age.set(age); }
    public IntegerProperty ageProperty() { return age; }
    
    public String getGender() { return gender.get(); }
    public void setGender(String gender) { this.gender.set(gender); }
    public StringProperty genderProperty() { return gender; }
    
    public String getContactNumber() { return contactNumber.get(); }
    public void setContactNumber(String contactNumber) { this.contactNumber.set(contactNumber); }
    public StringProperty contactNumberProperty() { return contactNumber; }
    
    public String getEmail() { return email.get(); }
    public void setEmail(String email) { this.email.set(email); }
    public StringProperty emailProperty() { return email; }
    
    public String getLocation() { return location.get(); }
    public void setLocation(String location) { this.location.set(location); }
    public StringProperty locationProperty() { return location; }
    
    public String getAddress() { return address.get(); }
    public void setAddress(String address) { this.address.set(address); }
    public StringProperty addressProperty() { return address; }
    
    public LocalDate getLastDonationDate() { return lastDonationDate.get(); }
    public void setLastDonationDate(LocalDate lastDonationDate) { this.lastDonationDate.set(lastDonationDate); }
    public ObjectProperty<LocalDate> lastDonationDateProperty() { return lastDonationDate; }
    
    public boolean isAvailable() { return isAvailable.get(); }
    public void setAvailable(boolean isAvailable) { this.isAvailable.set(isAvailable); }
    public BooleanProperty isAvailableProperty() { return isAvailable; }
    
    public String getMedicalHistory() { return medicalHistory.get(); }
    public void setMedicalHistory(String medicalHistory) { this.medicalHistory.set(medicalHistory); }
    public StringProperty medicalHistoryProperty() { return medicalHistory; }
    
    public String getEmergencyContact() { return emergencyContact.get(); }
    public void setEmergencyContact(String emergencyContact) { this.emergencyContact.set(emergencyContact); }
    public StringProperty emergencyContactProperty() { return emergencyContact; }
    
    public double getWeight() { return weight.get(); }
    public void setWeight(double weight) { this.weight.set(weight); }
    public DoubleProperty weightProperty() { return weight; }
    
    public boolean hasDisease() { return hasDisease.get(); }
    public void setHasDisease(boolean hasDisease) { this.hasDisease.set(hasDisease); }
    public BooleanProperty hasDiseaseProperty() { return hasDisease; }
    
    public int getTotalDonations() { return totalDonations.get(); }
    public void setTotalDonations(int totalDonations) { this.totalDonations.set(totalDonations); }
    public IntegerProperty totalDonationsProperty() { return totalDonations; }
    
    // Helper methods
    public String getAvailabilityStatus() {
        if (!isAvailable()) return "Not Available";
        if (lastDonationDate.get() == null) return "Available";
        
        LocalDate eligibleDate = lastDonationDate.get().plusMonths(3);
        if (LocalDate.now().isBefore(eligibleDate)) {
            return "Available from " + eligibleDate;
        }
        return "Available Now";
    }
    
    public boolean isEligibleToDonate() {
        if (!isAvailable()) return false;
        if (hasDisease()) return false;
        if (age.get() < 18 || age.get() > 60) return false;
        if (weight.get() < 50) return false;
        if (lastDonationDate.get() == null) return true;
        
        return LocalDate.now().isAfter(lastDonationDate.get().plusMonths(3));
    }
}

