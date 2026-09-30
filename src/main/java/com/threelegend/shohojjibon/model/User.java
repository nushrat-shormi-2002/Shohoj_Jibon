package com.threelegend.shohojjibon.model;

import javafx.beans.property.*;
import java.time.LocalDateTime;

/**
 * User Model
 * Shohoj Jibon - Makes your life easier
 * Developed by Three_Legend
 */
public class User {
    private final IntegerProperty id;
    private final StringProperty username;
    private final StringProperty email;
    private final StringProperty passwordHash;
    private final StringProperty role;
    private final StringProperty fullName;
    private final StringProperty phone;
    private final StringProperty address;
    private final StringProperty profilePicture;
    private final BooleanProperty isActive;
    private final BooleanProperty emailVerified;
    private final BooleanProperty phoneVerified;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime lastLogin;
    
    public User() {
        this.id = new SimpleIntegerProperty();
        this.username = new SimpleStringProperty();
        this.email = new SimpleStringProperty();
        this.passwordHash = new SimpleStringProperty();
        this.role = new SimpleStringProperty();
        this.fullName = new SimpleStringProperty();
        this.phone = new SimpleStringProperty();
        this.address = new SimpleStringProperty();
        this.profilePicture = new SimpleStringProperty();
        this.isActive = new SimpleBooleanProperty(true);
        this.emailVerified = new SimpleBooleanProperty(false);
        this.phoneVerified = new SimpleBooleanProperty(false);
    }
    
    // ID
    public int getId() { return id.get(); }
    public void setId(int id) { this.id.set(id); }
    public IntegerProperty idProperty() { return id; }
    
    // Username
    public String getUsername() { return username.get(); }
    public void setUsername(String username) { this.username.set(username); }
    public StringProperty usernameProperty() { return username; }
    
    // Email
    public String getEmail() { return email.get(); }
    public void setEmail(String email) { this.email.set(email); }
    public StringProperty emailProperty() { return email; }
    
    // Password Hash
    public String getPasswordHash() { return passwordHash.get(); }
    public void setPasswordHash(String passwordHash) { this.passwordHash.set(passwordHash); }
    public StringProperty passwordHashProperty() { return passwordHash; }
    
    // Role
    public String getRole() { return role.get(); }
    public void setRole(String role) { this.role.set(role); }
    public StringProperty roleProperty() { return role; }
    
    // Full Name
    public String getFullName() { return fullName.get(); }
    public void setFullName(String fullName) { this.fullName.set(fullName); }
    public StringProperty fullNameProperty() { return fullName; }
    
    // Phone
    public String getPhone() { return phone.get(); }
    public void setPhone(String phone) { this.phone.set(phone); }
    public StringProperty phoneProperty() { return phone; }
    
    // Address
    public String getAddress() { return address.get(); }
    public void setAddress(String address) { this.address.set(address); }
    public StringProperty addressProperty() { return address; }
    
    // Profile Picture
    public String getProfilePicture() { return profilePicture.get(); }
    public void setProfilePicture(String profilePicture) { this.profilePicture.set(profilePicture); }
    public StringProperty profilePictureProperty() { return profilePicture; }
    
    // Is Active
    public boolean isActive() { return isActive.get(); }
    public void setActive(boolean isActive) { this.isActive.set(isActive); }
    public BooleanProperty isActiveProperty() { return isActive; }
    
    // Email Verified
    public boolean isEmailVerified() { return emailVerified.get(); }
    public void setEmailVerified(boolean emailVerified) { this.emailVerified.set(emailVerified); }
    public BooleanProperty emailVerifiedProperty() { return emailVerified; }
    
    // Phone Verified
    public boolean isPhoneVerified() { return phoneVerified.get(); }
    public void setPhoneVerified(boolean phoneVerified) { this.phoneVerified.set(phoneVerified); }
    public BooleanProperty phoneVerifiedProperty() { return phoneVerified; }
    
    // Created At
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    // Updated At
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    
    // Last Login
    public LocalDateTime getLastLogin() { return lastLogin; }
    public void setLastLogin(LocalDateTime lastLogin) { this.lastLogin = lastLogin; }
    
    @Override
    public String toString() {
        return "User{" +
                "id=" + getId() +
                ", username='" + getUsername() + '\'' +
                ", email='" + getEmail() + '\'' +
                ", role='" + getRole() + '\'' +
                ", fullName='" + getFullName() + '\'' +
                '}';
    }
}

