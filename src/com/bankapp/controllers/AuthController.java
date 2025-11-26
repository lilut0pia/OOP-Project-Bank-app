package com.bankapp.controllers;

import com.bankapp.model.User;
import com.bankapp.services.AuthService;
import com.bankapp.utils.ConsoleUtils;
import java.util.Optional;
import com.bankapp.utils.InputValidator;

/**
 * AuthController - Handles authentication operations.
 * Implements the MVC Controller pattern - validates input and delegates to service layer.
 */
public class AuthController {
    private final AuthService authService;

    /**
     * Constructor - initializes with AuthService.
     *
     * @param authService AuthService instance
     */
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Handles user registration flow.
     *
     * @return User object if registration successful, null otherwise
     */
    public User handleRegistration() {
        ConsoleUtils.printHeader("USER REGISTRATION");

        // Get username
        String username = ConsoleUtils.readString("Enter username (4-20 chars, alphanumeric): ");
        if (!InputValidator.isValidUsername(username)) {
            ConsoleUtils.printError("Invalid username format");
            return null;
        }

        // Get password
        String password = ConsoleUtils.readString("Enter password (min 6 chars): ");
        if (!InputValidator.isValidPassword(password)) {
            ConsoleUtils.printError("Password must be at least 6 characters");
            return null;
        }

        // Get full name
        String fullName = ConsoleUtils.readString("Enter full name: ");
        if (!InputValidator.isValidFullName(fullName)) {
            ConsoleUtils.printError("Invalid full name (use letters and spaces only)");
            return null;
        }

        // Get email
        String email = ConsoleUtils.readString("Enter email: ");
        if (!InputValidator.isValidEmail(email)) {
            ConsoleUtils.printError("Invalid email format");
            return null;
        }

        // Attempt registration
        User newUser = authService.register(username, password, fullName, email);
        if (newUser != null) {
            ConsoleUtils.printSuccess("Registration successful!");
            ConsoleUtils.printInfo("Your User ID: " + newUser.getUserId());
            return newUser;
        } else {
            ConsoleUtils.printError("Registration failed. Username may already exist.");
            return null;
        }
    }

    /**
     * Handles user login flow.
     *
     * @return User object if login successful, null otherwise
     */
    public User handleLogin() {
        ConsoleUtils.printHeader("USER LOGIN");

        // Get username
        String username = ConsoleUtils.readString("Enter username: ");
        if (InputValidator.isNullOrEmpty(username)) {
            ConsoleUtils.printError("Username cannot be empty");
            return null;
        }

        // Get password
        String password = ConsoleUtils.readString("Enter password: ");
        if (InputValidator.isNullOrEmpty(password)) {
            ConsoleUtils.printError("Password cannot be empty");
            return null;
        }

        // Attempt login
        Optional<User> userOptional = authService.login(username, password);
        if (userOptional.isPresent()) {
            User loggedInUser = userOptional.get();
            ConsoleUtils.printSuccess("Login successful! Welcome, " + loggedInUser.getFullName());
            return loggedInUser;
        } else {
            ConsoleUtils.printError("Invalid username or password");
            return null;
        }
    }
}
