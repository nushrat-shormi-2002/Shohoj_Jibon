package com.threelegend.shohojjibon.model;

import javafx.beans.property.*;
import java.time.LocalDateTime;

/**
 * Service Provider Model
 * Shohoj Jibon - Makes your life easier
 * Developed by Three_Legend
 */
public class ServiceProvider {
    private final IntegerProperty id;
    private final IntegerProperty userId;
    private final StringProperty providerName;
    private final StringProperty authorityType;
    private final StringProperty businessLicense;
    private final StringProperty description;
    private final StringProperty address;
    private final StringProperty website;
    private final DoubleProperty rating;
    private final IntegerProperty totalReviews;
    private final BooleanProperty verified;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    public ServiceProvider() {
        this.id = new SimpleIntegerProperty();
        this.userId = new SimpleIntegerProperty();
        this.providerName = new SimpleStringProperty();
        this.authorityType = new SimpleStringProperty();
        this.businessLicense = new SimpleStringProperty();
        this.description = new SimpleStringProperty();
        this.address = new SimpleStringProperty();
        this.website = new SimpleStringProperty();
        this.rating = new SimpleDoubleProperty(0.0);
        this.totalReviews = new SimpleIntegerProperty(0);
        this.verified = new SimpleBooleanProperty(false);
    }
    
    // ID
    public int getId() { return id.get(); }
    public void setId(int id) { this.id.set(id); }
    public IntegerProperty idProperty() { return id; }
    
    // User ID
    public int getUserId() { return userId.get(); }
    public void setUserId(int userId) { this.userId.set(userId); }
    public IntegerProperty userIdProperty() { return userId; }
    
    // Provider Name
    public String getProviderName() { return providerName.get(); }
    public void setProviderName(String providerName) { this.providerName.set(providerName); }
    public StringProperty providerNameProperty() { return providerName; }
    
    // Authority Type
    public String getAuthorityType() { return authorityType.get(); }
    public void setAuthorityType(String authorityType) { this.authorityType.set(authorityType); }
    public StringProperty authorityTypeProperty() { return authorityType; }
    
    // Business License
    public String getBusinessLicense() { return businessLicense.get(); }
    public void setBusinessLicense(String businessLicense) { this.businessLicense.set(businessLicense); }
    public StringProperty businessLicenseProperty() { return businessLicense; }
    
    // Description
    public String getDescription() { return description.get(); }
    public void setDescription(String description) { this.description.set(description); }
    public StringProperty descriptionProperty() { return description; }
    
    // Address
    public String getAddress() { return address.get(); }
    public void setAddress(String address) { this.address.set(address); }
    public StringProperty addressProperty() { return address; }
    
    // Website
    public String getWebsite() { return website.get(); }
    public void setWebsite(String website) { this.website.set(website); }
    public StringProperty websiteProperty() { return website; }
    
    // Rating
    public double getRating() { return rating.get(); }
    public void setRating(double rating) { this.rating.set(rating); }
    public DoubleProperty ratingProperty() { return rating; }
    
    // Total Reviews
    public int getTotalReviews() { return totalReviews.get(); }
    public void setTotalReviews(int totalReviews) { this.totalReviews.set(totalReviews); }
    public IntegerProperty totalReviewsProperty() { return totalReviews; }
    
    // Verified
    public boolean isVerified() { return verified.get(); }
    public void setVerified(boolean verified) { this.verified.set(verified); }
    public BooleanProperty verifiedProperty() { return verified; }
    
    // Created At
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    // Updated At
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    
    /**
     * Check if provider has specific authority
     */
    public boolean hasAuthority(String authority) {
        return authorityType.get() != null && authorityType.get().contains(authority);
    }
    
    @Override
    public String toString() {
        return "ServiceProvider{" +
                "id=" + getId() +
                ", providerName='" + getProviderName() + '\'' +
                ", authorityType='" + getAuthorityType() + '\'' +
                ", rating=" + getRating() +
                ", verified=" + isVerified() +
                '}';
    }
}

