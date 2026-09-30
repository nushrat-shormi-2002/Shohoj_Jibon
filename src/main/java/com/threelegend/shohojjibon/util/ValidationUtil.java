package com.threelegend.shohojjibon.util;

import java.util.regex.Pattern;

/**
 * Validation Utility for input validation
 * Shohoj Jibon - Makes your life easier
 * Developed by Three_Legend
 * 
 * Provides validation methods for various input types
 */
public class ValidationUtil {
    
    // Regular expression patterns
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );
    
    private static final Pattern PHONE_PATTERN = Pattern.compile(
        "^01[0-9]{9}$"  // Bangladesh phone format
    );
    
    private static final Pattern USERNAME_PATTERN = Pattern.compile(
        "^[a-z0-9_]{3,20}$"  // Lowercase letters, numbers, underscore, 3-20 chars
    );
    
    /**
     * Validate email format
     * @param email Email to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email.trim()).matches();
    }
    
    /**
     * Validate phone number (Bangladesh format)
     * @param phone Phone number to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidPhone(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            return false;
        }
        String cleanPhone = phone.trim().replaceAll("[\\s-]", "");
        return PHONE_PATTERN.matcher(cleanPhone).matches();
    }
    
    /**
     * Validate username format
     * @param username Username to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            return false;
        }
        return USERNAME_PATTERN.matcher(username.trim()).matches();
    }
    
    /**
     * Check if string is null or empty
     * @param str String to check
     * @return true if null or empty, false otherwise
     */
    public static boolean isEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }
    
    /**
     * Check if string is not null and not empty
     * @param str String to check
     * @return true if not empty, false otherwise
     */
    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }
    
    /**
     * Validate price/amount (must be positive)
     * @param amount Amount to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidAmount(double amount) {
        return amount > 0;
    }
    
    /**
     * Validate rating (0-5 range)
     * @param rating Rating to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidRating(double rating) {
        return rating >= 0 && rating <= 5;
    }
    
    /**
     * Validate quantity (must be positive)
     * @param quantity Quantity to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidQuantity(int quantity) {
        return quantity > 0;
    }
    
    /**
     * Sanitize string input (remove leading/trailing whitespace)
     * @param input Input string
     * @return Sanitized string
     */
    public static String sanitize(String input) {
        return input == null ? "" : input.trim();
    }
    
    /**
     * Get email validation error message
     * @param email Email to validate
     * @return Error message or empty string if valid
     */
    public static String getEmailError(String email) {
        if (isEmpty(email)) {
            return "Email is required";
        }
        if (!isValidEmail(email)) {
            return "Invalid email format (e.g., user@example.com)";
        }
        return "";
    }
    
    /**
     * Get phone validation error message
     * @param phone Phone to validate
     * @return Error message or empty string if valid
     */
    public static String getPhoneError(String phone) {
        if (isEmpty(phone)) {
            return "Phone number is required";
        }
        if (!isValidPhone(phone)) {
            return "Invalid phone format (e.g., 01712345678)";
        }
        return "";
    }
    
    /**
     * Get username validation error message
     * @param username Username to validate
     * @return Error message or empty string if valid
     */
    public static String getUsernameError(String username) {
        if (isEmpty(username)) {
            return "Username is required";
        }
        if (username.length() < 3) {
            return "Username must be at least 3 characters";
        }
        if (username.length() > 20) {
            return "Username must be at most 20 characters";
        }
        if (!isValidUsername(username)) {
            return "Username must contain only lowercase letters, numbers, and underscore";
        }
        return "";
    }
}

