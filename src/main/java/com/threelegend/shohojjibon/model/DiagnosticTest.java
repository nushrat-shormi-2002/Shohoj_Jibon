package com.threelegend.shohojjibon.model;

import javafx.beans.property.*;

/**
 * Diagnostic Test Model
 * Shohoj Jibon - Makes your life easier
 * Developed by Three_Legend
 */
public class DiagnosticTest {
    private final IntegerProperty id;
    private final IntegerProperty providerId;
    private final StringProperty testName;
    private final StringProperty testCategory;
    private final StringProperty location;
    private final DoubleProperty price;
    private final StringProperty description;
    private final StringProperty preparationRequired;
    private final StringProperty resultTime;
    private final StringProperty sampleType;
    private final BooleanProperty isHomeCollection;
    private final DoubleProperty homeCollectionCharge;
    private final BooleanProperty isAvailable;
    
    public DiagnosticTest() {
        this.id = new SimpleIntegerProperty();
        this.providerId = new SimpleIntegerProperty();
        this.testName = new SimpleStringProperty();
        this.testCategory = new SimpleStringProperty();
        this.location = new SimpleStringProperty();
        this.price = new SimpleDoubleProperty();
        this.description = new SimpleStringProperty();
        this.preparationRequired = new SimpleStringProperty();
        this.resultTime = new SimpleStringProperty();
        this.sampleType = new SimpleStringProperty();
        this.isHomeCollection = new SimpleBooleanProperty(false);
        this.homeCollectionCharge = new SimpleDoubleProperty(0.0);
        this.isAvailable = new SimpleBooleanProperty(true);
    }
    
    // Getters and Setters
    public int getId() { return id.get(); }
    public void setId(int id) { this.id.set(id); }
    public IntegerProperty idProperty() { return id; }
    
    public int getProviderId() { return providerId.get(); }
    public void setProviderId(int providerId) { this.providerId.set(providerId); }
    public IntegerProperty providerIdProperty() { return providerId; }
    
    public String getTestName() { return testName.get(); }
    public void setTestName(String testName) { this.testName.set(testName); }
    public StringProperty testNameProperty() { return testName; }
    
    public String getTestCategory() { return testCategory.get(); }
    public void setTestCategory(String testCategory) { this.testCategory.set(testCategory); }
    public StringProperty testCategoryProperty() { return testCategory; }
    
    public String getLocation() { return location.get(); }
    public void setLocation(String location) { this.location.set(location); }
    public StringProperty locationProperty() { return location; }
    
    public double getPrice() { return price.get(); }
    public void setPrice(double price) { this.price.set(price); }
    public DoubleProperty priceProperty() { return price; }
    
    public String getDescription() { return description.get(); }
    public void setDescription(String description) { this.description.set(description); }
    public StringProperty descriptionProperty() { return description; }
    
    public String getPreparationRequired() { return preparationRequired.get(); }
    public void setPreparationRequired(String preparationRequired) { this.preparationRequired.set(preparationRequired); }
    public StringProperty preparationRequiredProperty() { return preparationRequired; }
    
    public String getResultTime() { return resultTime.get(); }
    public void setResultTime(String resultTime) { this.resultTime.set(resultTime); }
    public StringProperty resultTimeProperty() { return resultTime; }
    
    public String getSampleType() { return sampleType.get(); }
    public void setSampleType(String sampleType) { this.sampleType.set(sampleType); }
    public StringProperty sampleTypeProperty() { return sampleType; }
    
    public boolean isHomeCollection() { return isHomeCollection.get(); }
    public void setHomeCollection(boolean isHomeCollection) { this.isHomeCollection.set(isHomeCollection); }
    public BooleanProperty isHomeCollectionProperty() { return isHomeCollection; }
    
    public double getHomeCollectionCharge() { return homeCollectionCharge.get(); }
    public void setHomeCollectionCharge(double homeCollectionCharge) { this.homeCollectionCharge.set(homeCollectionCharge); }
    public DoubleProperty homeCollectionChargeProperty() { return homeCollectionCharge; }
    
    public boolean isAvailable() { return isAvailable.get(); }
    public void setAvailable(boolean isAvailable) { this.isAvailable.set(isAvailable); }
    public BooleanProperty isAvailableProperty() { return isAvailable; }
    
    // Formatted properties
    public String getFormattedPrice() {
        return String.format("৳%.2f", getPrice());
    }
    
    public String getFormattedHomeCollectionCharge() {
        return String.format("৳%.2f", getHomeCollectionCharge());
    }
    
    public String getTotalPrice() {
        double total = getPrice() + (isHomeCollection() ? getHomeCollectionCharge() : 0);
        return String.format("৳%.2f", total);
    }
}

