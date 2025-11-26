package com.bankapp.services;

import com.bankapp.service.Bank;
import com.bankapp.model.User;
import com.bankapp.utils.IDGenerator;
import com.bankapp.utils.PasswordHasher;

import java.util.Optional;

/**
 * AuthService - Manages user authentication logic like registration and login.
 * It also holds the in-memory store of all users.
 */
public class AuthService {

    private Bank bank;

    public AuthService(Bank bank) {
        this.bank = bank;
    }

    public void setBank(Bank bank) {
        this.bank = bank;
    }
    
    /**
     * Registers a new user.
     *
     * @param username The username for the new user.
     * @param password The plain text password for the new user.
     * @param fullName The full name of the new user.
     * @param email    The email address of the new user.
     * @return The newly created User object, or null if the username already exists.
     */
    public User register(String username, String password, String fullName, String email) {
        // Check if username already exists in the bank
        boolean usernameExists = bank.getAllUsers().stream()
                .anyMatch(u -> u.getUsername().equals(username));
        if (usernameExists) {
            return null; // Username already exists
        }
        String userId = IDGenerator.generateUserId();
        String passwordHash = PasswordHasher.hashPassword(password);
        User newUser = new User(userId, username, passwordHash, fullName, email);
        bank.addUser(newUser);
        return newUser;
    }

    /**
     * Authenticates a user.
     *
     * @param username The username to log in with.
     * @param password The plain text password.
     * @return An Optional containing the User if login is successful, otherwise an empty Optional.
     */
    public Optional<User> login(String username, String password) {
        return bank.getAllUsers().stream()
                .filter(user -> user.getUsername().equals(username) && user.verifyPassword(PasswordHasher.hashPassword(password)))
                .findFirst();
    }

    /**
     * Retrieves a user by their unique user ID.
     *
     * @param userId The ID of the user to find.
     */
    public Optional<User> getUserById(String userId) {
        if (userId == null || userId.trim().isEmpty()) {
            return Optional.empty();
        }
        // Use the bank object to find the user by ID
        return Optional.ofNullable(bank.findUserById(userId));
    }
}