package com.bankapp.services;

import com.bankapp.data.InMemoryDataStore;
import com.bankapp.data.UserRepository;
import com.bankapp.model.User;
import com.bankapp.utils.IDGenerator;
import com.bankapp.utils.InputValidator;
import com.bankapp.utils.PasswordHasher;

/**
 * AuthService - Handles user authentication and registration.
 * Implements the Single Responsibility Principle - focuses on auth operations only.
 */
public class AuthService {
    private final UserRepository userRepository;

    /**
     * Constructor - initializes with data store.
     */
    public AuthService() {
        this.userRepository = InMemoryDataStore.getInstance().getUserRepository();
    }

    /**
     * Registers a new user.
     * Validates input and checks for duplicate usernames.
     *
     * @param username User's username
     * @param password User's password
     * @param fullName User's full name
     * @param email User's email
     * @return User object if registration successful, null if failed
     */
    public User register(String username, String password, String fullName, String email) {
        // Validate inputs
        if (!InputValidator.isValidUsername(username)) {
            return null;
        }
        if (!InputValidator.isValidPassword(password)) {
            return null;
        }
        if (!InputValidator.isValidFullName(fullName)) {
            return null;
        }
        if (!InputValidator.isValidEmail(email)) {
            return null;
        }

        // Check for duplicate username
        if (userRepository.usernameExists(username)) {
            return null;
        }

        // Create new user with hashed password
        String userId = IDGenerator.generateUserId();
        String passwordHash = PasswordHasher.hashPassword(password);
        User newUser = new User(userId, username, passwordHash, fullName, email);

        // Save user
        if (userRepository.save(newUser)) {
            return newUser;
        }
        return null;
    }

    /**
     * Authenticates a user (login).
     *
     * @param username User's username
     * @param password User's password
     * @return User object if login successful, null if failed
     */
    public User login(String username, String password) {
        if (InputValidator.isNullOrEmpty(username) || InputValidator.isNullOrEmpty(password)) {
            return null;
        }

        User user = userRepository.findByUsername(username);
        if (user == null) {
            return null;
        }

        // Verify password
        if (PasswordHasher.verifyPassword(password, user.getPasswordHash())) {
            return user;
        }
        return null;
    }

    /**
     * Finds a user by their ID.
     *
     * @param userId User ID to search for
     * @return User object if found, null otherwise
     */
    public User getUserById(String userId) {
        return userRepository.findById(userId);
    }

    /**
     * Finds a user by their username.
     *
     * @param username Username to search for
     * @return User object if found, null otherwise
     */
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
