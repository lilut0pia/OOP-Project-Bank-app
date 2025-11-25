package com.bankapp.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * PasswordHasher - Utility class for password hashing and verification.
 * Uses SHA-256 algorithm with Base64 encoding for security.
 * Note: In production, use bcrypt or Argon2 instead.
 */
public class PasswordHasher {

    /**
     * Hashes a password using SHA-256 algorithm.
     *
     * @param password Plain text password to hash
     * @return Hashed password (Base64 encoded)
     */
    public static String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = digest.digest(password.getBytes());
            return Base64.getEncoder().encodeToString(hashedBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not available", e);
        }
    }

    /**
     * Verifies a plain text password against a hashed password.
     *
     * @param plainPassword Plain text password to verify
     * @param hashedPassword Hashed password to compare against
     * @return true if passwords match, false otherwise
     */
    public static boolean verifyPassword(String plainPassword, String hashedPassword) {
        String hashedInput = hashPassword(plainPassword);
        return hashedInput.equals(hashedPassword);
    }
}
