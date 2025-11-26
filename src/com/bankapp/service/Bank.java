package com.bankapp.service;

import com.bankapp.model.Account;
import com.bankapp.model.Admin;
import com.bankapp.model.User;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The Bank class acts as a central repository for all users, accounts, and the admin.
 * It simulates an in-memory database for the application.
 */
public class Bank implements Serializable {
    private static final long serialVersionUID = 1L;
    private final List<User> users;
    private Admin admin; // Only one admin in the system

    public Bank() {
        this.users = new ArrayList<>();
        this.admin = null;
    }

    /**
     * Registers the single admin account for the bank.
     * Can only be done once.
     * @param newAdmin The Admin object to register.
     * @return true if registration is successful, false if an admin already exists.
     */
    public boolean registerAdmin(Admin newAdmin) {
        if (this.admin == null) {
            this.admin = newAdmin;
            return true;
        }
        System.out.println("Error: An admin account already exists.");
        return false;
    }

    /**
     * Logs in as an admin.
     * @param username The admin's username.
     * @param passwordHash The hashed password.
     * @return The Admin object on successful login, otherwise null.
     */
    public Admin loginAdmin(String username, String passwordHash) {
        if (admin != null && admin.getUsername().equals(username) && admin.verifyPassword(passwordHash)) {
            return admin;
        }
        System.out.println("Invalid admin username or password.");
        return null;
    }

    /**
     * Adds a new user to the bank.
     * @param user The user to add.
     */
    public void addUser(User user) {
        if (user != null) {
            users.add(user);
        }
    }

    /**
     * Finds a user by their ID.
     * @param userId The user's ID.
     * @return The User if found, otherwise null.
     */
    public User findUserById(String userId) {
        return users.stream()
                .filter(user -> user.getUserId().equals(userId))
                .findFirst()
                .orElse(null);
    }

    /**
     * Returns a copy of the list of all users.
     * @return A list of all users.
     */
    public List<User> getAllUsers() {
        return new ArrayList<>(users);
    }

    /**
     * Gets a list of all accounts from all users in the bank.
     * @return A list of all accounts.
     */
    public List<Account> getAllAccounts() {
        return users.stream()
                .flatMap(user -> user.getAccounts().stream())
                .collect(Collectors.toList());
    }

    /**
     * Resets the bank to its initial state, clearing all users and the admin.
     */
    public void reset() {
        this.users.clear();
        this.admin = null;
    }
}