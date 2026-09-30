package com.threelegend.shohojjibon.util;

import com.threelegend.shohojjibon.model.User;
import com.threelegend.shohojjibon.model.ServiceProvider;

/**
 * Session Manager for user session handling
 * Shohoj Jibon - Makes your life easier
 * Developed by Three_Legend
 * 
 * Manages logged-in user session using Singleton pattern
 */
public class SessionManager {
    
    private static SessionManager instance;
    private User currentUser;
    private ServiceProvider currentProvider;
    
    /**
     * Private constructor to prevent instantiation
     */
    private SessionManager() {
        this.currentUser = null;
        this.currentProvider = null;
    }
    
    /**
     * Get singleton instance of SessionManager
     * @return SessionManager instance
     */
    public static SessionManager getInstance() {
        if (instance == null) {
            synchronized (SessionManager.class) {
                if (instance == null) {
                    instance = new SessionManager();
                }
            }
        }
        return instance;
    }
    
    /**
     * Set current logged-in user
     * @param user User object
     */
    public void setCurrentUser(User user) {
        this.currentUser = user;
        System.out.println("✓ User session started: " + (user != null ? user.getUsername() : "null"));
    }
    
    /**
     * Get current logged-in user
     * @return Current user or null if not logged in
     */
    public User getCurrentUser() {
        return currentUser;
    }
    
    /**
     * Set current service provider (if user is a provider)
     * @param provider ServiceProvider object
     */
    public void setCurrentProvider(ServiceProvider provider) {
        this.currentProvider = provider;
        System.out.println("✓ Provider session set: " + (provider != null ? provider.getProviderName() : "null"));
    }
    
    /**
     * Get current service provider
     * @return Current provider or null if user is not a provider
     */
    public ServiceProvider getCurrentProvider() {
        return currentProvider;
    }
    
    /**
     * Check if user is logged in
     * @return true if user is logged in, false otherwise
     */
    public boolean isLoggedIn() {
        return currentUser != null;
    }
    
    /**
     * Check if current user is a service provider
     * @return true if current user is a service provider, false otherwise
     */
    public boolean isServiceProvider() {
        return currentUser != null && "SERVICE_PROVIDER".equals(currentUser.getRole());
    }
    
    /**
     * Check if current user is an admin
     * @return true if current user is an admin, false otherwise
     */
    public boolean isAdmin() {
        return currentUser != null && "ADMIN".equals(currentUser.getRole());
    }
    
    /**
     * Check if current user is a general user
     * @return true if current user is a general user, false otherwise
     */
    public boolean isGeneralUser() {
        return currentUser != null && "GENERAL_USER".equals(currentUser.getRole());
    }
    
    /**
     * Get current user ID
     * @return User ID or -1 if not logged in
     */
    public int getCurrentUserId() {
        return currentUser != null ? currentUser.getId() : -1;
    }
    
    /**
     * Get current provider ID
     * @return Provider ID or -1 if not a provider
     */
    public int getCurrentProviderId() {
        return currentProvider != null ? currentProvider.getId() : -1;
    }
    
    /**
     * Clear session (logout)
     */
    public void clearSession() {
        String username = currentUser != null ? currentUser.getUsername() : "unknown";
        this.currentUser = null;
        this.currentProvider = null;
        System.out.println("✓ User session cleared: " + username);
    }
    
    /**
     * Get current user's full name
     * @return Full name or "Guest" if not logged in
     */
    public String getCurrentUserFullName() {
        return currentUser != null ? currentUser.getFullName() : "Guest";
    }
    
    /**
     * Get current user's role
     * @return Role or "GUEST" if not logged in
     */
    public String getCurrentUserRole() {
        return currentUser != null ? currentUser.getRole() : "GUEST";
    }
}

