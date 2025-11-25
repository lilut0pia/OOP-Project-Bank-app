package com.bankapp.utils;

/**
 * InputValidator - Utility class for input validation.
 * Provides methods to validate user inputs for security and correctness.
 */
public class InputValidator {

    /**
     * Validates a username.
     * Requirements: 4-20 characters, alphanumeric and underscores only.
     *
     * @param username Username to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidUsername(String username) {
        if (username == null || username.isEmpty()) {
            return false;
        }
        if (username.length() < 4 || username.length() > 20) {
            return false;
        }
        return username.matches("^[a-zA-Z0-9_]+$");
    }

    /**
     * Validates a password.
     * Requirements: At least 6 characters.
     *
     * @param password Password to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidPassword(String password) {
        if (password == null || password.isEmpty()) {
            return false;
        }
        return password.length() >= 6;
    }

    /**
     * Validates an email address.
     * Basic email validation using regex.
     *
     * @param email Email to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidEmail(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }
        return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    }

    /**
     * Validates a full name.
     * Requirements: 2-50 characters, letters and spaces only.
     *
     * @param fullName Full name to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidFullName(String fullName) {
        if (fullName == null || fullName.isEmpty()) {
            return false;
        }
        if (fullName.length() < 2 || fullName.length() > 50) {
            return false;
        }
        return fullName.matches("^[a-zA-Z\\s]+$");
    }

    /**
     * Validates a monetary amount.
     * Requirements: Positive number, max 2 decimal places.
     *
     * @param amount Amount to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidAmount(double amount) {
        return amount > 0 && amount <= 999999999.99;
    }

    /**
     * Validates an amount string and converts to double.
     *
     * @param amountStr Amount string to validate
     * @return Amount as double, or -1 if invalid
     */
    public static double parseAmount(String amountStr) {
        try {
            double amount = Double.parseDouble(amountStr);
            return isValidAmount(amount) ? amount : -1;
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    /**
     * Validates an account number.
     * Requirements: 8-16 characters, alphanumeric only.
     *
     * @param accountNumber Account number to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidAccountNumber(String accountNumber) {
        if (accountNumber == null || accountNumber.isEmpty()) {
            return false;
        }
        if (accountNumber.length() < 8 || accountNumber.length() > 16) {
            return false;
        }
        return accountNumber.matches("^[a-zA-Z0-9]+$");
    }

    /**
     * Checks if a string is null or empty.
     *
     * @param str String to check
     * @return true if null or empty, false otherwise
     */
    public static boolean isNullOrEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }
}
