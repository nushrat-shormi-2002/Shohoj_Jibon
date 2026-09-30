# 🩸 Shohoj Jibon - Complete Life Services Platform

**"Makes your life easier" - Developed by Three_Legend**

A comprehensive JavaFX desktop application providing essential life services including **Transport**, **Healthcare**, **Accommodation**, and **Blood Donation** - with a special focus on life-saving blood donation services.

---

## 🌟 Key Features

### 🩸 **Blood Donation System (CRITICAL FEATURE)**
- **Emergency Blood Requests** - Request blood for critical patients
- **Donor Management** - Comprehensive donor database with eligibility tracking
- **Blood Bank Integration** - Connect with verified blood banks
- **Urgency Levels** - Critical, High, Medium, Low priority system
- **Patient Details** - Complete patient information management
- **Medical History Tracking** - Safe donation practices

### 🚌 **Transport Services**
- **Multi-Stop Bus Routes** - Complete route management with fare calculation
- **Real-time Availability** - Live seat availability tracking
- **Route Planning** - Multiple stops with pickup/drop points
- **Fare Calculation** - Automatic fare calculation based on distance
- **Bus Types** - AC Sleeper, AC Seating, Non-AC, Deluxe options

### 🏥 **Healthcare Services**
- **Doctor Appointments** - Book consultations by specialty
- **Diagnostic Tests** - Comprehensive test booking with home collection
- **Specialty Search** - Find doctors by medical specialty
- **Location-based Search** - Find healthcare services by area
- **Medical History** - Track patient medical information

### 🏨 **Accommodation Services**
- **Hotel Booking** - Room reservation with multiple room types
- **Location-based Search** - Find hotels by area
- **Amenities Filter** - Search by available facilities
- **Pricing Options** - Budget to luxury accommodations
- **Availability Tracking** - Real-time room availability

### 👥 **User Management**
- **Role-based Access** - General Users, Service Providers, Admin
- **Secure Authentication** - BCrypt password hashing
- **Profile Management** - Complete user profile system
- **Session Management** - Secure login sessions

---

## 🛠 Technology Stack

| Component | Technology | Version |
|-----------|------------|---------|
| **Frontend** | JavaFX | 21 |
| **Backend** | Java | 17+ |
| **Database** | MySQL | 8.0+ |
| **Security** | BCrypt | Latest |
| **Build Tool** | Maven | 3.6+ |
| **UI Components** | ControlsFX | Latest |
| **Icons** | FontAwesomeFX | Latest |

---

## 💻 System Requirements

### Minimum Requirements
- **Java Development Kit (JDK)**: 17 or higher
- **MySQL Database**: 8.0 or higher (Direct MySQL or XAMPP)
- **Maven**: 3.6 or higher
- **RAM**: 4GB minimum (8GB recommended)
- **Disk Space**: 500MB free space
- **OS**: Windows 10/11, macOS, or Linux

### Recommended Setup
- **RAM**: 8GB or higher
- **SSD Storage**: For better performance
- **Internet**: For dependency downloads

---

## 🚀 Quick Start Guide

### Option 1: Direct MySQL Setup (Recommended)

#### Step 1: Install MySQL
1. Download MySQL from [mysql.com](https://dev.mysql.com/downloads/)
2. Install MySQL Server
3. Start MySQL service

#### Step 2: Setup Database
```bash
# Connect to MySQL
mysql -u root -p

# Create database and import schema
source FINAL_DATABASE_SCHEMA.sql
```

#### Step 3: Run Application
```bash
cd Shohoj_Jibon
mvn clean javafx:run
```

### Option 2: XAMPP Setup

#### Step 1: Install XAMPP
1. Download from [apachefriends.org](https://www.apachefriends.org/)
2. Install and start Apache + MySQL services

#### Step 2: Import Database
1. Open `http://localhost/phpmyadmin`
2. Import `FINAL_DATABASE_SCHEMA.sql`

#### Step 3: Run Application
```bash
mvn clean javafx:run
```

---

## 🔑 Default Login Credentials

### 👤 General Users
| Username | Password | Role | Description |
|----------|----------|------|-------------|
| `john_doe` | `user123` | General User | Sample user account |
| `jane_smith` | `user123` | General User | Sample user account |
| `alex_brown` | `user123` | General User | Sample user account |
| `sarah_khan` | `user123` | General User | Sample user account |

### 🏢 Service Providers
| Username | Password | Authority | Provider Name |
|----------|----------|-----------|---------------|
| `green_line` | `provider123` | Transport | Green Line Transport |
| `shohag_paribahan` | `provider123` | Transport | Shohag Paribahan |
| `ena_transport` | `provider123` | Transport | Ena Transport |
| `care_hospital` | `provider123` | Hospital | Care Hospital |
| `labaid_diagnostics` | `provider123` | Hospital | Labaid Diagnostics |
| `sheraton_hotel` | `provider123` | Accommodation | Sheraton Hotel |
| `radisson_hotel` | `provider123` | Accommodation | Radisson Hotel |
| `six_seasons` | `provider123` | Accommodation | Six Seasons Hotel |
| `red_crescent_blood_bank` | `provider123` | Blood Bank | Red Crescent Blood Bank |

### 👨‍💼 Administrator
| Username | Password | Role |
|----------|----------|------|
| `admin` | `admin123` | System Administrator |

---

## 🗄️ Database Schema

### Core Tables (12 Total)
1. **`users`** - User accounts and authentication
2. **`service_providers`** - Service provider information
3. **`buses`** - Transport services with routes
4. **`bus_stops`** - Multiple stops for each route
5. **`doctors`** - Healthcare professionals
6. **`diagnostic_tests`** - Medical tests and procedures
7. **`hotels`** - Accommodation services
8. **`blood_donors`** - Blood donation system
9. **`blood_requests`** - Emergency blood requests
10. **`bookings`** - Universal booking system
11. **`reviews`** - User reviews and ratings
12. **`favorites`** - User wishlist system

### Database Configuration
```sql
-- Default MySQL Configuration
Host: localhost
Port: 3306
Database: shohoj_jibon_db
Username: root
Password: (empty for XAMPP, set your password for direct MySQL)
```

---

## 📁 Project Structure

```
Shohoj_Jibon/
├── FINAL_DATABASE_SCHEMA.sql     # Complete database schema
├── pom.xml                       # Maven configuration
├── README.md                     # This documentation
├── mvnw                          # Maven wrapper
├── mvnw.cmd                      # Maven wrapper (Windows)
└── src/
    └── main/
        ├── java/
        │   └── com/threelegend/shohojjibon/
        │       ├── MainApplication.java           # Application entry point
        │       ├── Launcher.java                  # JAR launcher
        │       ├── controller/                    # MVC Controllers
        │       │   ├── LoginController.java
        │       │   ├── RegisterController.java
        │       │   ├── UserDashboardController.java
        │       │   ├── ProviderDashboardController.java
        │       │   ├── AdminDashboardController.java
        │       │   ├── BloodBankController.java
        │       │   ├── HospitalProviderController.java
        │       │   └── HotelProviderController.java
        │       ├── model/                         # Data Models
        │       │   ├── User.java
        │       │   ├── ServiceProvider.java
        │       │   ├── Bus.java
        │       │   ├── BusStop.java
        │       │   ├── Doctor.java
        │       │   ├── DiagnosticTest.java
        │       │   ├── Hotel.java
        │       │   ├── BloodDonor.java
        │       │   ├── BloodRequest.java
        │       │   ├── Booking.java
        │       │   ├── Review.java
        │       │   ├── Favorite.java
        │       │   └── Notification.java
        │       ├── dao/                           # Data Access Objects
        │       │   ├── UserDAO.java
        │       │   ├── ServiceProviderDAO.java
        │       │   ├── BusDAO.java
        │       │   ├── DoctorDAO.java
        │       │   ├── HotelDAO.java
        │       │   ├── BloodDonorDAO.java
        │       │   ├── BloodRequestDAO.java
        │       │   ├── BookingDAO.java
        │       │   └── ReviewDAO.java
        │       └── util/                         # Utility Classes
        │           ├── DBConnection.java
        │           ├── PasswordUtil.java
        │           ├── SessionManager.java
        │           └── ValidationUtil.java
        └── resources/
            └── com/threelegend/shohojjibon/      # FXML UI Files
                ├── login.fxml
                ├── register.fxml
                ├── user-dashboard.fxml
                ├── user-dashboard-enhanced.fxml
                ├── provider-dashboard.fxml
                ├── admin-dashboard.fxml
                ├── bloodbank-provider-dashboard.fxml
                ├── hospital-provider-dashboard.fxml
                └── hotel-provider-dashboard.fxml
```

---

## 🔒 Security Features

### 🔐 Authentication & Authorization
- **BCrypt Password Hashing** - Industry-standard password security
- **Session Management** - Secure user sessions
- **Role-based Access Control** - Different access levels
- **Input Validation** - Comprehensive form validation

### 🛡️ Database Security
- **SQL Injection Prevention** - PreparedStatement for all queries
- **Parameter Binding** - Safe parameter handling
- **Data Isolation** - Provider-specific data access
- **Transaction Management** - ACID compliance

### 🔍 Data Validation
- **Email Format Validation** - Proper email format checking
- **Phone Number Validation** - Bangladesh phone number format
- **Username Validation** - Alphanumeric username requirements
- **Password Strength** - Minimum password requirements

---

## 🩸 Blood Donation System Details

### Critical Features
- **Emergency Blood Requests** - Life-saving blood request system
- **Donor Eligibility** - Age, weight, health status tracking
- **Blood Group Matching** - A+, A-, B+, B-, AB+, AB-, O+, O-
- **Urgency Levels** - Critical, High, Medium, Low priority
- **Medical History** - Safe donation practices
- **Location-based Matching** - Find nearby donors

### Donor Management
- **Donor Registration** - Complete donor information
- **Eligibility Tracking** - Health status and donation history
- **Contact Management** - Emergency contact information
- **Availability Status** - Real-time donor availability

### Request Process
1. **Patient Information** - Complete patient details
2. **Blood Group Matching** - Find compatible donors
3. **Urgency Assessment** - Priority-based matching
4. **Blood Bank Coordination** - Professional oversight
5. **Request Tracking** - Status updates throughout process

---

## 🚌 Transport System Features

### Multi-Stop Bus Routes
- **Route Planning** - Multiple pickup and drop points
- **Fare Calculation** - Distance-based pricing
- **Schedule Management** - Departure and arrival times
- **Seat Management** - Real-time availability tracking

### Bus Types Available
- **AC Sleeper** - Long-distance comfort
- **AC Seating** - Air-conditioned seating
- **Non-AC** - Standard service
- **Deluxe** - Premium service
- **Standard** - Basic service

---

## 🏥 Healthcare System Features

### Doctor Services
- **Specialty Search** - Cardiology, Pediatrics, Orthopedics, etc.
- **Location-based Search** - Find doctors by area
- **Appointment Booking** - Schedule consultations
- **Fee Management** - Transparent pricing

### Diagnostic Services
- **Test Categories** - Blood tests, imaging, cardiac tests
- **Home Collection** - Convenient sample collection
- **Result Tracking** - Test result management
- **Preparation Guidelines** - Test-specific instructions

---

## 🏨 Accommodation Features

### Hotel Services
- **Room Types** - Standard, Deluxe, Suite options
- **Amenities** - WiFi, Pool, Gym, Spa, etc.
- **Location Search** - Find hotels by area
- **Pricing Options** - Budget to luxury accommodations

### Booking Management
- **Availability Tracking** - Real-time room availability
- **Cancellation Policy** - Flexible booking options
- **Check-in/Check-out** - Time management
- **Occupancy Management** - Room capacity tracking

---



---

## 🚀 Building and Deployment

### Build JAR File
```bash
# Clean and build
mvn clean package

# Run JAR file
java -jar target/shohoj-jibon-1.0.0.jar
```

### Maven Commands
```bash
# Clean project
mvn clean

# Compile
mvn compile

# Run application
mvn javafx:run

# Package JAR
mvn package

# Install dependencies
mvn install
```

---

## 📊 System Statistics

### Database Tables: 12
### User Roles: 3 (General User, Service Provider, Admin)
### Service Types: 4 (Transport, Hospital, Accommodation, Blood Bank)
### Authority Types: 4 (TRANSPORT, HOSPITAL, ACCOMMODATION, BLOOD_BANK)
### Sample Data: Complete with realistic test data

---

## 🎯 Development Roadmap

### ✅ Completed Features
- [x] User authentication system
- [x] Role-based access control
- [x] Transport booking system
- [x] Healthcare services
- [x] Accommodation booking
- [x] Blood donation system
- [x] Multi-stop bus routes
- [x] Database schema design
- [x] Security implementation

### 🚧 Future Enhancements
- [ ] Payment integration
- [ ] Mobile application
- [ ] Real-time notifications
- [ ] Advanced analytics
- [ ] API development
- [ ] Cloud deployment

---

## 📞 Support & Contact

### Getting Help
- **Documentation** - Check this README and code comments
- **Database Issues** - Verify schema and connections
- **Application Errors** - Check Java version and dependencies

### Contact Information
- **Developer** - Three_Legend
- **Project** - Shohoj Jibon
- **Purpose** - Educational demonstration

---

## 📝 License & Credits

### License
This project is developed for educational purposes.
© 2025 Three_Legend. All rights reserved.

### Credits
- **Database Design** - Enhanced schema with 12+ tables
- **Backend Development** - DAO pattern, business logic, security
- **Frontend Development** - Modern JavaFX UI/UX
- **Blood Donation System** - Life-saving feature implementation
- **Integration** - MVC architecture, session management

### Technologies Used
- **JavaFX Community** - Excellent UI framework
- **MySQL** - Robust database management
- **BCrypt** - Secure password hashing
- **Maven** - Dependency management
- **ControlsFX** - Enhanced UI components

---

## 🎓 Learning Outcomes

This project demonstrates:
- ✅ **Advanced MVC Architecture** - Clean separation of concerns
- ✅ **Database Design** - Normalized schema with relationships
- ✅ **JDBC & PreparedStatement** - Secure database operations
- ✅ **Security Implementation** - BCrypt password hashing
- ✅ **Modern JavaFX UI** - Professional desktop application
- ✅ **Role-based Access Control** - Multi-user system
- ✅ **Input Validation** - Comprehensive form validation
- ✅ **Session Management** - Secure user sessions
- ✅ **CRUD Operations** - Complete data management
- ✅ **DAO Design Pattern** - Data access abstraction
- ✅ **Blood Donation System** - Life-saving feature implementation

---

## 🏆 Project Highlights

### 🩸 **Blood Donation System**
- **Life-saving Feature** - Critical for emergency situations
- **Donor Management** - Comprehensive donor database
- **Emergency Requests** - Urgent blood requirement handling
- **Medical Safety** - Health status and eligibility tracking

### 🚌 **Multi-Stop Transport**
- **Route Management** - Complex bus route handling
- **Fare Calculation** - Automatic pricing system
- **Real-time Updates** - Live availability tracking

### 🏥 **Healthcare Integration**
- **Doctor Appointments** - Medical consultation booking
- **Diagnostic Services** - Test booking and management
- **Specialty Search** - Find doctors by medical field

### 🏨 **Accommodation Services**
- **Hotel Booking** - Room reservation system
- **Amenities Management** - Facility-based search
- **Pricing Options** - Budget to luxury accommodations

---

**Thank you for using Shohoj Jibon!**

*"Makes your life easier" - Developed with ❤️ by Three_Legend*

---

## 📸 Application Screenshots

### Login Interface
Modern gradient-based login with secure authentication.

### User Dashboard
Comprehensive service search and booking interface.

### Provider Dashboard
Service management for different authority types.

### Blood Donation System
Life-saving blood request and donor management.

### Admin Dashboard
System administration and user management.

---

## 📋 Project Documentation

### 📄 Project Report
A comprehensive project report is available: `Shohoj_Jibon_Project_Report.txt`
- **Introduction & Objectives**
- **Framework & Technology Stack**
- **Detailed Feature Analysis**
- **Challenges & Solutions**
- **Resources & References**
- **Development Statistics**

### 🔧 Troubleshooting

#### Common Issues & Solutions

**Database Connection Failed**
```bash
# Check MySQL service status
# Windows: services.msc → MySQL
# Linux/Mac: sudo systemctl status mysql

# Verify database exists
mysql -u root -p -e "SHOW DATABASES;"
```

**JavaFX Runtime Error**
```bash
# Ensure JavaFX modules are available
mvn clean compile
mvn javafx:run
```

**Maven Build Issues**
```bash
# Clear Maven cache
mvn clean
mvn dependency:purge-local-repository
mvn install
```

**Memory Issues**
```bash
# Increase JVM memory
export MAVEN_OPTS="-Xmx2g -Xms1g"
mvn javafx:run
```

#### Performance Optimization
- **Database Indexing**: All tables have proper indexes
- **Connection Pooling**: Efficient database connection management
- **Memory Management**: Optimized for 4GB+ RAM systems
- **UI Responsiveness**: Asynchronous operations for better UX

---

## 📊 Enhanced Project Statistics

### Code Metrics
- **Total Lines of Code**: 2,500+ (excluding generated files)
- **Java Classes**: 25+ (Controllers, Models, DAOs, Utilities)
- **FXML UI Files**: 9 (Complete UI coverage)
- **Database Tables**: 12 (Normalized schema)
- **Sample Records**: 50+ (Realistic test data)

### Technical Specifications
- **Architecture**: MVC (Model-View-Controller)
- **Design Patterns**: DAO, Singleton, Factory
- **Security**: BCrypt + SQL Injection Prevention
- **Database**: MySQL 8.0+ with UTF8MB4 support
- **UI Framework**: JavaFX 21 with ControlsFX
- **Build System**: Maven 3.6+ with JavaFX plugin

### Feature Coverage
- **User Management**: 100% (Authentication, Authorization, Profiles)
- **Transport Services**: 100% (Multi-stop routes, fare calculation)
- **Healthcare Services**: 100% (Doctors, diagnostic tests, appointments)
- **Accommodation**: 100% (Hotel booking, room management)
- **Blood Donation**: 100% (Critical life-saving feature)
- **Admin Panel**: 100% (System administration)

---

## 🚀 Advanced Usage

### Development Mode
```bash
# Run with debug logging
mvn javafx:run -Djavafx.debug=true

# Run with custom database settings
mvn javafx:run -Ddb.host=localhost -Ddb.port=3306
```

### Production Deployment
```bash
# Create executable JAR
mvn clean package

# Run production JAR
java -jar target/shohoj-jibon-1.0.0.jar

# With custom JVM settings
java -Xmx2g -Xms1g -jar target/shohoj-jibon-1.0.0.jar
```

### Database Backup
```bash
# Backup database
mysqldump -u root -p shohoj_jibon_db > backup.sql

# Restore database
mysql -u root -p shohoj_jibon_db < backup.sql
```

---

## 🤝 Contributing

### Development Setup
1. Fork the repository
2. Create feature branch: `git checkout -b feature/new-feature`
3. Make changes and test thoroughly
4. Commit changes: `git commit -m "Add new feature"`
5. Push to branch: `git push origin feature/new-feature`
6. Create Pull Request

### Code Standards
- Follow Java naming conventions
- Add comprehensive comments
- Write unit tests for new features
- Update documentation for changes
- Ensure database compatibility

---

**Project Status:** ✅ **Production Ready**  
**Last Updated:** October 2025  
**Version:** 1.0.0  
**Database:** MySQL 8.0+ Compatible  
**Report Available:** `Shohoj_Jibon_Project_Report.txt`