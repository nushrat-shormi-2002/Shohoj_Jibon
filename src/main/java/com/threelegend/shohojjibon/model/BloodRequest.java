package com.threelegend.shohojjibon.model;

import javafx.beans.property.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Blood Request Model
 * Represents a blood donation request from a user to a blood bank
 * Shohoj Jibon - Makes your life easier
 * Developed by Three_Legend
 */
public class BloodRequest {
    private final IntegerProperty id;
    private final IntegerProperty requesterId;
    private final IntegerProperty donorId;
    private final IntegerProperty bloodBankId;
    private final StringProperty bloodGroup;
    private final StringProperty patientName;
    private final StringProperty patientAge;
    private final StringProperty patientGender;
    private final StringProperty patientPhone;
    private final StringProperty patientEmail;
    private final StringProperty patientAddress;
    private final StringProperty patientCondition;
    private final StringProperty medicalHistory;
    private final StringProperty currentMedications;
    private final StringProperty urgency;
    private final StringProperty contactNumber;
    private final StringProperty location;
    private final StringProperty specialRequirements;
    private final StringProperty status; // PENDING, APPROVED, REJECTED, COMPLETED
    private final StringProperty bloodBankNotes;
    private final ObjectProperty<LocalDateTime> requestDate;
    private final ObjectProperty<LocalDateTime> approvalDate;
    private final ObjectProperty<LocalDateTime> completionDate;
    
    public BloodRequest() {
        this.id = new SimpleIntegerProperty();
        this.requesterId = new SimpleIntegerProperty();
        this.donorId = new SimpleIntegerProperty();
        this.bloodBankId = new SimpleIntegerProperty();
        this.bloodGroup = new SimpleStringProperty();
        this.patientName = new SimpleStringProperty();
        this.patientAge = new SimpleStringProperty();
        this.patientGender = new SimpleStringProperty();
        this.patientPhone = new SimpleStringProperty();
        this.patientEmail = new SimpleStringProperty();
        this.patientAddress = new SimpleStringProperty();
        this.patientCondition = new SimpleStringProperty();
        this.medicalHistory = new SimpleStringProperty();
        this.currentMedications = new SimpleStringProperty();
        this.urgency = new SimpleStringProperty();
        this.contactNumber = new SimpleStringProperty();
        this.location = new SimpleStringProperty();
        this.specialRequirements = new SimpleStringProperty();
        this.status = new SimpleStringProperty("PENDING");
        this.bloodBankNotes = new SimpleStringProperty();
        this.requestDate = new SimpleObjectProperty<>(LocalDateTime.now());
        this.approvalDate = new SimpleObjectProperty<>();
        this.completionDate = new SimpleObjectProperty<>();
    }
    
    // Getters and Setters
    public int getId() { return id.get(); }
    public void setId(int id) { this.id.set(id); }
    public IntegerProperty idProperty() { return id; }
    
    public int getRequesterId() { return requesterId.get(); }
    public void setRequesterId(int requesterId) { this.requesterId.set(requesterId); }
    public IntegerProperty requesterIdProperty() { return requesterId; }
    
    public int getDonorId() { return donorId.get(); }
    public void setDonorId(int donorId) { this.donorId.set(donorId); }
    public IntegerProperty donorIdProperty() { return donorId; }
    
    public int getBloodBankId() { return bloodBankId.get(); }
    public void setBloodBankId(int bloodBankId) { this.bloodBankId.set(bloodBankId); }
    public IntegerProperty bloodBankIdProperty() { return bloodBankId; }
    
    public String getBloodGroup() { return bloodGroup.get(); }
    public void setBloodGroup(String bloodGroup) { this.bloodGroup.set(bloodGroup); }
    public StringProperty bloodGroupProperty() { return bloodGroup; }
    
    public String getPatientName() { return patientName.get(); }
    public void setPatientName(String patientName) { this.patientName.set(patientName); }
    public StringProperty patientNameProperty() { return patientName; }
    
    public String getPatientAge() { return patientAge.get(); }
    public void setPatientAge(String patientAge) { this.patientAge.set(patientAge); }
    public StringProperty patientAgeProperty() { return patientAge; }
    
    public String getPatientGender() { return patientGender.get(); }
    public void setPatientGender(String patientGender) { this.patientGender.set(patientGender); }
    public StringProperty patientGenderProperty() { return patientGender; }
    
    public String getPatientPhone() { return patientPhone.get(); }
    public void setPatientPhone(String patientPhone) { this.patientPhone.set(patientPhone); }
    public StringProperty patientPhoneProperty() { return patientPhone; }
    
    public String getPatientEmail() { return patientEmail.get(); }
    public void setPatientEmail(String patientEmail) { this.patientEmail.set(patientEmail); }
    public StringProperty patientEmailProperty() { return patientEmail; }
    
    public String getPatientAddress() { return patientAddress.get(); }
    public void setPatientAddress(String patientAddress) { this.patientAddress.set(patientAddress); }
    public StringProperty patientAddressProperty() { return patientAddress; }
    
    public String getPatientCondition() { return patientCondition.get(); }
    public void setPatientCondition(String patientCondition) { this.patientCondition.set(patientCondition); }
    public StringProperty patientConditionProperty() { return patientCondition; }
    
    public String getMedicalHistory() { return medicalHistory.get(); }
    public void setMedicalHistory(String medicalHistory) { this.medicalHistory.set(medicalHistory); }
    public StringProperty medicalHistoryProperty() { return medicalHistory; }
    
    public String getCurrentMedications() { return currentMedications.get(); }
    public void setCurrentMedications(String currentMedications) { this.currentMedications.set(currentMedications); }
    public StringProperty currentMedicationsProperty() { return currentMedications; }
    
    public String getUrgency() { return urgency.get(); }
    public void setUrgency(String urgency) { this.urgency.set(urgency); }
    public StringProperty urgencyProperty() { return urgency; }
    
    public String getContactNumber() { return contactNumber.get(); }
    public void setContactNumber(String contactNumber) { this.contactNumber.set(contactNumber); }
    public StringProperty contactNumberProperty() { return contactNumber; }
    
    public String getLocation() { return location.get(); }
    public void setLocation(String location) { this.location.set(location); }
    public StringProperty locationProperty() { return location; }
    
    public String getSpecialRequirements() { return specialRequirements.get(); }
    public void setSpecialRequirements(String specialRequirements) { this.specialRequirements.set(specialRequirements); }
    public StringProperty specialRequirementsProperty() { return specialRequirements; }
    
    public String getStatus() { return status.get(); }
    public void setStatus(String status) { this.status.set(status); }
    public StringProperty statusProperty() { return status; }
    
    public String getBloodBankNotes() { return bloodBankNotes.get(); }
    public void setBloodBankNotes(String bloodBankNotes) { this.bloodBankNotes.set(bloodBankNotes); }
    public StringProperty bloodBankNotesProperty() { return bloodBankNotes; }
    
    public LocalDateTime getRequestDate() { return requestDate.get(); }
    public void setRequestDate(LocalDateTime requestDate) { this.requestDate.set(requestDate); }
    public ObjectProperty<LocalDateTime> requestDateProperty() { return requestDate; }
    
    public LocalDateTime getApprovalDate() { return approvalDate.get(); }
    public void setApprovalDate(LocalDateTime approvalDate) { this.approvalDate.set(approvalDate); }
    public ObjectProperty<LocalDateTime> approvalDateProperty() { return approvalDate; }
    
    public LocalDateTime getCompletionDate() { return completionDate.get(); }
    public void setCompletionDate(LocalDateTime completionDate) { this.completionDate.set(completionDate); }
    public ObjectProperty<LocalDateTime> completionDateProperty() { return completionDate; }
    
    // Helper methods
    public boolean isPending() { return "PENDING".equals(getStatus()); }
    public boolean isApproved() { return "APPROVED".equals(getStatus()); }
    public boolean isRejected() { return "REJECTED".equals(getStatus()); }
    public boolean isCompleted() { return "COMPLETED".equals(getStatus()); }
    
    public String getStatusDisplay() {
        switch (getStatus()) {
            case "PENDING": return "⏳ Pending";
            case "APPROVED": return "✅ Approved";
            case "REJECTED": return "❌ Rejected";
            case "COMPLETED": return "🎉 Completed";
            default: return getStatus();
        }
    }
    
    public String getUrgencyDisplay() {
        switch (getUrgency()) {
            case "CRITICAL": return "🚨 Critical";
            case "HIGH": return "⚠️ High";
            case "MEDIUM": return "📋 Medium";
            case "LOW": return "📝 Low";
            default: return getUrgency();
        }
    }
    
    public StringProperty urgencyDisplayProperty() {
        return new SimpleStringProperty(getUrgencyDisplay());
    }
    
    public StringProperty statusDisplayProperty() {
        return new SimpleStringProperty(getStatusDisplay());
    }
}
