-- =====================================================
-- Shohoj Jibon - Final Database Schema
-- "Makes your life easier"
-- Developed by Three_Legend
-- Based on current working database structure
-- =====================================================

-- Create database if it doesn't exist
CREATE DATABASE IF NOT EXISTS shohoj_jibon_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE shohoj_jibon_db;

-- =====================================================
-- Table: users
-- Stores both general users and service providers
-- =====================================================
CREATE TABLE IF NOT EXISTS users (
    id INT NOT NULL AUTO_INCREMENT,
    username VARCHAR(50) COLLATE utf8mb4_unicode_ci NOT NULL,
    email VARCHAR(100) COLLATE utf8mb4_unicode_ci NOT NULL,
    password_hash VARCHAR(255) COLLATE utf8mb4_unicode_ci NOT NULL,
    role ENUM('GENERAL_USER', 'SERVICE_PROVIDER', 'ADMIN') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'GENERAL_USER',
    full_name VARCHAR(100) COLLATE utf8mb4_unicode_ci NOT NULL,
    phone VARCHAR(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    address TEXT COLLATE utf8mb4_unicode_ci,
    profile_picture VARCHAR(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    is_active TINYINT(1) DEFAULT 1,
    email_verified TINYINT(1) DEFAULT 0,
    phone_verified TINYINT(1) DEFAULT 0,
    created_at TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    last_login TIMESTAMP NULL DEFAULT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY username (username),
    UNIQUE KEY email (email),
    KEY idx_username (username),
    KEY idx_email (email),
    KEY idx_role (role),
    KEY idx_active (is_active)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- Table: service_providers
-- Additional info for service provider users
-- =====================================================
CREATE TABLE IF NOT EXISTS service_providers (
    id INT NOT NULL AUTO_INCREMENT,
    user_id INT NOT NULL,
    provider_name VARCHAR(100) COLLATE utf8mb4_unicode_ci NOT NULL,
    authority_type SET('TRANSPORT', 'HOSPITAL', 'ACCOMMODATION', 'BLOOD_BANK') COLLATE utf8mb4_unicode_ci NOT NULL,
    business_license VARCHAR(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    description TEXT COLLATE utf8mb4_unicode_ci,
    address VARCHAR(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    website VARCHAR(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    rating DECIMAL(3, 2) DEFAULT 0.00,
    total_reviews INT DEFAULT 0,
    verified TINYINT(1) DEFAULT 0,
    created_at TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY user_id (user_id),
    KEY idx_user_id (user_id),
    KEY idx_authority (authority_type),
    KEY idx_verified (verified),
    KEY idx_rating (rating),
    CONSTRAINT service_providers_chk_1 CHECK ((rating >= 0) and (rating <= 5)),
    CONSTRAINT service_providers_ibfk_1 FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- Table: buses
-- Transport authority data
-- =====================================================
CREATE TABLE IF NOT EXISTS buses (
    id INT NOT NULL AUTO_INCREMENT,
    provider_id INT NOT NULL,
    bus_name VARCHAR(100) COLLATE utf8mb4_unicode_ci NOT NULL,
    bus_number VARCHAR(50) COLLATE utf8mb4_unicode_ci NOT NULL,
    start_point VARCHAR(100) COLLATE utf8mb4_unicode_ci NOT NULL,
    end_point VARCHAR(100) COLLATE utf8mb4_unicode_ci NOT NULL,
    fare DECIMAL(10, 2) DEFAULT 0.00 COMMENT 'Maximum fare (auto-calculated from stops)',
    distance DECIMAL(10, 2) DEFAULT 0.00 COMMENT 'Total distance (auto-calculated)',
    schedule_time TIME NOT NULL,
    arrival_time TIME DEFAULT NULL,
    total_seats INT DEFAULT 50,
    available_seats INT DEFAULT 50,
    bus_type ENUM('AC_SLEEPER', 'AC_SEATING', 'NON_AC', 'DELUXE', 'STANDARD') COLLATE utf8mb4_unicode_ci DEFAULT 'NON_AC',
    amenities VARCHAR(255) COLLATE utf8mb4_unicode_ci DEFAULT 'Standard Service',
    is_active TINYINT(1) DEFAULT 1,
    created_at TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    KEY idx_provider (provider_id),
    KEY idx_route (start_point, end_point),
    KEY idx_fare (fare),
    KEY idx_active (is_active),
    CONSTRAINT buses_ibfk_1 FOREIGN KEY (provider_id) REFERENCES service_providers (id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- Table: bus_stops
-- Multiple stop points for each bus route
-- =====================================================
CREATE TABLE IF NOT EXISTS bus_stops (
    id INT NOT NULL AUTO_INCREMENT,
    bus_id INT NOT NULL,
    stop_name VARCHAR(150) COLLATE utf8mb4_unicode_ci NOT NULL,
    stop_order INT NOT NULL,
    arrival_time_offset INT DEFAULT 0 COMMENT 'Minutes from start',
    fare_from_start DECIMAL(10, 2) DEFAULT 0.00,
    is_pickup_point TINYINT(1) DEFAULT 1,
    is_drop_point TINYINT(1) DEFAULT 1,
    landmark VARCHAR(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    created_at TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY unique_bus_stop_order (bus_id, stop_order),
    KEY idx_bus (bus_id),
    KEY idx_stop_name (stop_name),
    KEY idx_order (bus_id, stop_order),
    CONSTRAINT bus_stops_ibfk_1 FOREIGN KEY (bus_id) REFERENCES buses (id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- Table: doctors
-- Hospital authority - doctor data
-- =====================================================
CREATE TABLE IF NOT EXISTS doctors (
    id INT NOT NULL AUTO_INCREMENT,
    provider_id INT NOT NULL,
    name VARCHAR(100) COLLATE utf8mb4_unicode_ci NOT NULL,
    specialty VARCHAR(100) COLLATE utf8mb4_unicode_ci NOT NULL,
    qualifications VARCHAR(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    experience_years INT DEFAULT 0,
    location VARCHAR(100) COLLATE utf8mb4_unicode_ci NOT NULL,
    consultation_fee DECIMAL(10, 2) NOT NULL,
    available_days VARCHAR(100) COLLATE utf8mb4_unicode_ci NOT NULL,
    available_time VARCHAR(100) COLLATE utf8mb4_unicode_ci NOT NULL,
    contact_number VARCHAR(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    email VARCHAR(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    languages_spoken VARCHAR(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    rating DECIMAL(3, 2) DEFAULT 0.00,
    total_reviews INT DEFAULT 0,
    is_available TINYINT(1) DEFAULT 1,
    created_at TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    KEY idx_provider (provider_id),
    KEY idx_specialty (specialty),
    KEY idx_location (location),
    KEY idx_fee (consultation_fee),
    KEY idx_rating (rating),
    KEY idx_available (is_available),
    CONSTRAINT doctors_chk_1 CHECK ((rating >= 0) and (rating <= 5)),
    CONSTRAINT doctors_ibfk_1 FOREIGN KEY (provider_id) REFERENCES service_providers (id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- Table: diagnostic_tests
-- Hospital authority - diagnostic test data
-- =====================================================
CREATE TABLE IF NOT EXISTS diagnostic_tests (
    id INT NOT NULL AUTO_INCREMENT,
    provider_id INT NOT NULL,
    test_name VARCHAR(100) COLLATE utf8mb4_unicode_ci NOT NULL,
    test_category VARCHAR(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    location VARCHAR(100) COLLATE utf8mb4_unicode_ci NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    description TEXT COLLATE utf8mb4_unicode_ci,
    preparation_required TEXT COLLATE utf8mb4_unicode_ci,
    result_time VARCHAR(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    sample_type VARCHAR(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    is_home_collection TINYINT(1) DEFAULT 0,
    home_collection_charge DECIMAL(10, 2) DEFAULT 0.00,
    is_available TINYINT(1) DEFAULT 1,
    created_at TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    KEY idx_provider (provider_id),
    KEY idx_test_name (test_name),
    KEY idx_category (test_category),
    KEY idx_location (location),
    KEY idx_price (price),
    KEY idx_available (is_available),
    CONSTRAINT diagnostic_tests_ibfk_1 FOREIGN KEY (provider_id) REFERENCES service_providers (id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- Table: hotels
-- Accommodation authority data
-- =====================================================
CREATE TABLE IF NOT EXISTS hotels (
    id INT NOT NULL AUTO_INCREMENT,
    provider_id INT NOT NULL,
    hotel_name VARCHAR(100) COLLATE utf8mb4_unicode_ci NOT NULL,
    location VARCHAR(100) COLLATE utf8mb4_unicode_ci NOT NULL,
    address VARCHAR(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    room_type VARCHAR(50) COLLATE utf8mb4_unicode_ci NOT NULL,
    price_per_night DECIMAL(10, 2) NOT NULL,
    rating DECIMAL(3, 2) DEFAULT 0.00,
    total_rooms INT NOT NULL DEFAULT 10,
    available_rooms INT NOT NULL DEFAULT 10,
    amenities TEXT COLLATE utf8mb4_unicode_ci,
    room_size VARCHAR(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    max_occupancy INT DEFAULT 2,
    contact_number VARCHAR(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    email VARCHAR(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    check_in_time TIME DEFAULT '14:00:00',
    check_out_time TIME DEFAULT '12:00:00',
    cancellation_policy TEXT COLLATE utf8mb4_unicode_ci,
    is_available TINYINT(1) DEFAULT 1,
    created_at TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    KEY idx_provider (provider_id),
    KEY idx_location (location),
    KEY idx_price (price_per_night),
    KEY idx_rating (rating),
    KEY idx_available (is_available),
    CONSTRAINT hotels_chk_1 CHECK ((rating >= 0) and (rating <= 5)),
    CONSTRAINT hotels_ibfk_1 FOREIGN KEY (provider_id) REFERENCES service_providers (id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- Table: blood_donors (CRITICAL FEATURE)
-- Blood Donation System - Saves Lives
-- =====================================================
CREATE TABLE IF NOT EXISTS blood_donors (
    id INT NOT NULL AUTO_INCREMENT,
    provider_id INT NOT NULL,
    donor_name VARCHAR(255) COLLATE utf8mb4_unicode_ci NOT NULL,
    blood_group VARCHAR(5) COLLATE utf8mb4_unicode_ci NOT NULL,
    age INT NOT NULL,
    gender VARCHAR(20) COLLATE utf8mb4_unicode_ci NOT NULL,
    contact_number VARCHAR(20) COLLATE utf8mb4_unicode_ci NOT NULL,
    email VARCHAR(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    location VARCHAR(255) COLLATE utf8mb4_unicode_ci NOT NULL,
    address TEXT COLLATE utf8mb4_unicode_ci,
    last_donation_date DATE DEFAULT NULL,
    is_available TINYINT(1) DEFAULT 1,
    medical_history TEXT COLLATE utf8mb4_unicode_ci,
    emergency_contact VARCHAR(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    weight DECIMAL(5, 2) DEFAULT 0.00,
    has_disease TINYINT(1) DEFAULT 0,
    total_donations INT DEFAULT 0,
    created_at TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    KEY provider_id (provider_id),
    KEY idx_blood_group (blood_group),
    KEY idx_location (location),
    KEY idx_eligibility (is_available, has_disease, age, weight),
    CONSTRAINT blood_donors_ibfk_1 FOREIGN KEY (provider_id) REFERENCES service_providers (id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- Table: blood_requests (ENHANCED VERSION)
-- Blood Donation System - Request Management with all Java app columns
-- =====================================================
CREATE TABLE IF NOT EXISTS blood_requests (
    id INT NOT NULL AUTO_INCREMENT,
    requester_id INT NOT NULL,
    donor_id INT NOT NULL,
    blood_bank_id INT NOT NULL,
    blood_group VARCHAR(5) COLLATE utf8mb4_unicode_ci NOT NULL,
    patient_name VARCHAR(100) COLLATE utf8mb4_unicode_ci NOT NULL,
    patient_condition TEXT COLLATE utf8mb4_unicode_ci,
    urgency ENUM('CRITICAL', 'HIGH', 'MEDIUM', 'LOW') COLLATE utf8mb4_unicode_ci DEFAULT 'MEDIUM',
    contact_number VARCHAR(20) COLLATE utf8mb4_unicode_ci NOT NULL,
    location VARCHAR(100) COLLATE utf8mb4_unicode_ci NOT NULL,
    special_requirements TEXT COLLATE utf8mb4_unicode_ci,
    status ENUM('PENDING', 'APPROVED', 'REJECTED', 'COMPLETED') COLLATE utf8mb4_unicode_ci DEFAULT 'PENDING',
    blood_bank_notes TEXT COLLATE utf8mb4_unicode_ci,
    request_date TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
    approval_date TIMESTAMP NULL DEFAULT NULL,
    completion_date TIMESTAMP NULL DEFAULT NULL,
    patient_age VARCHAR(10) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    patient_gender VARCHAR(10) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    patient_phone VARCHAR(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    patient_email VARCHAR(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    patient_address TEXT COLLATE utf8mb4_unicode_ci,
    medical_history TEXT COLLATE utf8mb4_unicode_ci,
    current_medications TEXT COLLATE utf8mb4_unicode_ci,
    PRIMARY KEY (id),
    KEY idx_requester (requester_id),
    KEY idx_donor (donor_id),
    KEY idx_blood_bank (blood_bank_id),
    KEY idx_status (status),
    KEY idx_blood_group (blood_group),
    KEY idx_urgency (urgency),
    CONSTRAINT blood_requests_ibfk_1 FOREIGN KEY (requester_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT blood_requests_ibfk_2 FOREIGN KEY (donor_id) REFERENCES blood_donors (id) ON DELETE CASCADE,
    CONSTRAINT blood_requests_ibfk_3 FOREIGN KEY (blood_bank_id) REFERENCES service_providers (id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- Table: bookings
-- Stores all types of bookings
-- =====================================================
CREATE TABLE IF NOT EXISTS bookings (
    id INT NOT NULL AUTO_INCREMENT,
    user_id INT NOT NULL,
    service_type ENUM('BUS', 'DOCTOR', 'TEST', 'HOTEL') COLLATE utf8mb4_unicode_ci NOT NULL,
    service_id INT NOT NULL,
    booking_date DATE NOT NULL,
    booking_time TIME DEFAULT NULL,
    end_date DATE DEFAULT NULL,
    quantity INT DEFAULT 1,
    total_amount DECIMAL(10, 2) NOT NULL,
    status ENUM('PENDING', 'CONFIRMED', 'CANCELLED', 'COMPLETED', 'REFUNDED') COLLATE utf8mb4_unicode_ci DEFAULT 'PENDING',
    payment_status ENUM('UNPAID', 'PAID', 'REFUNDED') COLLATE utf8mb4_unicode_ci DEFAULT 'UNPAID',
    payment_method VARCHAR(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    transaction_id VARCHAR(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    notes TEXT COLLATE utf8mb4_unicode_ci,
    created_at TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    KEY idx_user (user_id),
    KEY idx_service (service_type, service_id),
    KEY idx_status (status),
    KEY idx_payment_status (payment_status),
    KEY idx_booking_date (booking_date),
    CONSTRAINT bookings_ibfk_1 FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- Insert Sample Admin User
-- =====================================================
-- Password: admin123 (hashed with BCrypt)
INSERT IGNORE INTO users (username, email, password_hash, role, full_name, phone, is_active, email_verified) VALUES
('admin', 'admin@shohojjibon.com', '$2a$10$E7zYXGwKqJmCqQ6XvJqN0O.KOcIcJGPXQJY8Qm.MJ4QUJQNWwB6aK', 'ADMIN', 'System Administrator', '01700000000', 1, 1);

-- =====================================================
-- Insert Sample General Users
-- =====================================================
-- Password: user123 (hashed with BCrypt)
INSERT IGNORE INTO users (username, email, password_hash, role, full_name, phone, address, is_active, email_verified) VALUES
('john_doe', 'john@example.com', '$2a$10$vI8aWBnW3fID.ZQ4/zo1G.q1lRps.9cGQeJUc1eJLPEa2kKyYQPKy', 'GENERAL_USER', 'John Doe', '01712345678', 'Dhanmondi, Dhaka', 1, 1),
('jane_smith', 'jane@example.com', '$2a$10$vI8aWBnW3fID.ZQ4/zo1G.q1lRps.9cGQeJUc1eJLPEa2kKyYQPKy', 'GENERAL_USER', 'Jane Smith', '01812345678', 'Gulshan, Dhaka', 1, 1),
('alex_brown', 'alex@example.com', '$2a$10$vI8aWBnW3fID.ZQ4/zo1G.q1lRps.9cGQeJUc1eJLPEa2kKyYQPKy', 'GENERAL_USER', 'Alex Brown', '01912345678', 'Banani, Dhaka', 1, 1),
('sarah_khan', 'sarah@example.com', '$2a$10$vI8aWBnW3fID.ZQ4/zo1G.q1lRps.9cGQeJUc1eJLPEa2kKyYQPKy', 'GENERAL_USER', 'Sarah Khan', '01612345678', 'Uttara, Dhaka', 1, 1);

-- =====================================================
-- Insert Sample Service Provider Users
-- =====================================================
-- Password: provider123 (hashed with BCrypt)
INSERT IGNORE INTO users (username, email, password_hash, role, full_name, phone, is_active, email_verified) VALUES
('green_line', 'greenline@transport.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'SERVICE_PROVIDER', 'Green Line Transport', '01712345001', 1, 1),
('shohag_paribahan', 'shohag@transport.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'SERVICE_PROVIDER', 'Shohag Paribahan', '01712345002', 1, 1),
('ena_transport', 'ena@transport.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'SERVICE_PROVIDER', 'Ena Transport', '01712345003', 1, 1),
('care_hospital', 'care@hospital.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'SERVICE_PROVIDER', 'Care Hospital', '01712345004', 1, 1),
('labaid_diagnostics', 'labaid@hospital.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'SERVICE_PROVIDER', 'Labaid Diagnostics', '01712345005', 1, 1),
('sheraton_hotel', 'sheraton@hotel.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'SERVICE_PROVIDER', 'Sheraton Hotel', '01712345006', 1, 1),
('radisson_hotel', 'radisson@hotel.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'SERVICE_PROVIDER', 'Radisson Hotel', '01712345007', 1, 1),
('six_seasons', 'sixseasons@hotel.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'SERVICE_PROVIDER', 'Six Seasons Hotel', '01712345008', 1, 1),
('red_crescent_blood_bank', 'bloodbank@redcrescent.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'SERVICE_PROVIDER', 'Red Crescent Blood Bank', '01712345013', 1, 1);

-- =====================================================
-- Insert Service Providers
-- =====================================================
INSERT IGNORE INTO service_providers (user_id, provider_name, authority_type, description, address, rating, total_reviews, verified) VALUES
(5, 'Green Line Transport', 'TRANSPORT', 'Premium AC bus service across Bangladesh with modern fleet', 'Mohakhali, Dhaka', 4.5, 120, 1),
(6, 'Shohag Paribahan', 'TRANSPORT', 'Comfortable and affordable bus service since 1985', 'Gabtoli, Dhaka', 4.2, 95, 1),
(7, 'Ena Transport', 'TRANSPORT', 'Luxury travel experience with top-notch facilities', 'Sayedabad, Dhaka', 4.7, 150, 1),
(8, 'Care Hospital', 'HOSPITAL', 'Multi-specialty hospital with experienced doctors and modern equipment', 'Banani, Dhaka', 4.6, 200, 1),
(9, 'Labaid Diagnostics', 'HOSPITAL', 'Advanced diagnostic center with state-of-the-art technology', 'Dhanmondi, Dhaka', 4.8, 180, 1),
(10, 'Sheraton Hotel', 'ACCOMMODATION', 'Luxury 5-star hotel with world-class amenities', 'Gulshan, Dhaka', 4.9, 250, 1),
(11, 'Radisson Hotel', 'ACCOMMODATION', 'Premium business hotel in the heart of Dhaka', 'Motijheel, Dhaka', 4.7, 180, 1),
(12, 'Six Seasons Hotel', 'ACCOMMODATION', 'Contemporary luxury hotel with exceptional service', 'Gulshan, Dhaka', 4.8, 220, 1),
(13, 'Red Crescent Blood Bank', 'BLOOD_BANK', 'Emergency blood donation services and donor management', 'Dhanmondi, Dhaka', 4.9, 150, 1);

-- =====================================================
-- Insert Sample Blood Donors (CRITICAL FEATURE)
-- =====================================================
INSERT IGNORE INTO blood_donors (provider_id, donor_name, blood_group, age, gender, contact_number, email, location, address, last_donation_date, is_available, medical_history, emergency_contact, weight, has_disease, total_donations) VALUES
-- Blood Bank 1 (provider_id = 9)
(9, 'Ahmed Rahman', 'A+', 28, 'Male', '01711111120', 'ahmed@email.com', 'Dhanmondi, Dhaka', 'Road 27, Dhanmondi', '2024-08-15', 1, 'No major health issues', '01711111121', 70.5, 0, 5),
(9, 'Fatima Begum', 'B+', 25, 'Female', '01711111122', 'fatima@email.com', 'Gulshan, Dhaka', 'Gulshan Avenue', '2024-09-10', 1, 'Healthy, regular donor', '01711111123', 55.0, 0, 3),
(9, 'Karim Hassan', 'O+', 32, 'Male', '01711111124', 'karim@email.com', 'Banani, Dhaka', 'Banani Road 11', '2024-07-20', 1, 'No medical issues', '01711111125', 75.0, 0, 8),
(9, 'Nusrat Jahan', 'AB+', 29, 'Female', '01711111126', 'nusrat@email.com', 'Uttara, Dhaka', 'Uttara Sector 7', '2024-10-05', 1, 'Regular health checkups', '01711111127', 58.0, 0, 4),
(9, 'Rashid Khan', 'A-', 35, 'Male', '01711111128', 'rashid@email.com', 'Mirpur, Dhaka', 'Mirpur 10', '2024-06-30', 1, 'No health problems', '01711111129', 72.0, 0, 6),
(9, 'Shabnam Ahmed', 'B-', 26, 'Female', '01711111130', 'shabnam@email.com', 'Motijheel, Dhaka', 'Motijheel C/A', '2024-08-25', 1, 'Healthy lifestyle', '01711111131', 52.0, 0, 2),
(9, 'Tanvir Islam', 'O-', 30, 'Male', '01711111132', 'tanvir@email.com', 'Farmgate, Dhaka', 'Farmgate', '2024-09-15', 1, 'No medical history', '01711111133', 68.0, 0, 7),
(9, 'Zara Khan', 'AB-', 24, 'Female', '01711111134', 'zara@email.com', 'Dhanmondi, Dhaka', 'Road 15, Dhanmondi', '2024-10-01', 1, 'Regular donor', '01711111135', 50.0, 0, 3);

-- =====================================================
-- Verify Database Creation
-- =====================================================
SELECT 'Database schema created successfully!' AS Status;
SELECT 'All tables created with proper structure for Java application compatibility' AS Message;

-- Show all tables
SHOW TABLES;

-- Show blood_requests table structure to verify all columns
DESCRIBE blood_requests;

-- =====================================================
-- End of Final Database Schema
-- =====================================================
-- 
-- Default Credentials:
-- Admin: username=admin, password=admin123
-- Users: username=john_doe, password=user123
-- Providers: username=green_line, password=provider123
-- 
-- This schema matches your current working database structure
-- =====================================================
