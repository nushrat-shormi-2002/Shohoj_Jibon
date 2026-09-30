package com.threelegend.shohojjibon.util;

import org.mindrot.jbcrypt.BCrypt;

/**
 * Password Utility for secure password handling
 * Shohoj Jibon - Makes your life easier
 * Developed by Three_Legend
 * 
 * Uses BCrypt for password hashing and verification
 */
public class PasswordUtil {
    
    // BCrypt work factor (higher = more secure but slower)
    private static final int BCRYPT_ROUNDS = 10;
    
    /**
     * Hash a password using BCrypt
     * @param plainPassword Plain text password
     * @return Hashed password
     */
    public static String hashPassword(String plainPassword) {
        if (plainPassword == null || plainPassword.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt(BCRYPT_ROUNDS));
    }
    
    /**
     * Verify a password against a hash
     * @param plainPassword Plain text password to verify
     * @param hashedPassword Hashed password from database
     * @return true if password matches, false otherwise
     */
    public static boolean verifyPassword(String plainPassword, String hashedPassword) {
        if (plainPassword == null || hashedPassword == null) {
            return false;
        }
        try {
            return BCrypt.checkpw(plainPassword, hashedPassword);
        } catch (Exception e) {
            System.err.println("Error verifying password: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Check if password meets minimum strength requirements
     * @param password Password to check
     * @return true if password is strong enough, false otherwise
     */
    public static boolean isPasswordStrong(String password) {
        if (password == null || password.length() < 6) {
            return false;
        }
        
        boolean hasUpper = false;
        boolean hasLower = false;
        boolean hasDigit = false;
        
        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) hasUpper = true;
            if (Character.isLowerCase(c)) hasLower = true;
            if (Character.isDigit(c)) hasDigit = true;
        }
        
        // Require at least lowercase and either uppercase or digit
        return hasLower && (hasUpper || hasDigit);
    }
    
    /**
     * Get password strength message
     * @param password Password to evaluate
     * @return Strength message
     */
    public static String getPasswordStrengthMessage(String password) {
        if (password == null || password.isEmpty()) {
            return "Password is required";
        }
        if (password.length() < 6) {
            return "Password must be at least 6 characters";
        }
        if (!isPasswordStrong(password)) {
            return "Password must contain lowercase and either uppercase or number";
        }
        return "Password is strong";
    }
}

