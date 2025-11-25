package com.bankapp.data;

import com.bankapp.model.User;
import java.util.HashMap;
import java.util.Map;

/**
 * UserRepository - Data access object for User entities.
 * Handles user storage and retrieval operations.
 */
public class UserRepository {
    private Map<String, User> users; // userId -> User

    public UserRepository() {
        this.users = new HashMap<>();
    }

    /**
     * Saves a user to the repository.
     *
     * @param user User object to save
     * @return true if saved successfully, false if user already exists
     */
    public boolean save(User user) {
        if (user == null || users.containsKey(user.getUserId())) {
            return false;
        }
        users.put(user.getUserId(), user);
        return true;
    }

    /**
     * Finds a user by user ID.
     *
     * @param userId User ID to search for
     * @return User object if found, null otherwise
     */
    public User findById(String userId) {
        return users.get(userId);
    }

    /**
     * Finds a user by username.
     *
     * @param username Username to search for
     * @return User object if found, null otherwise
     */
    public User findByUsername(String username) {
        return users.values().stream()
                .filter(user -> user.getUsername().equals(username))
                .findFirst()
                .orElse(null);
    }

    /**
     * Updates an existing user.
     *
     * @param user User object to update
     * @return true if updated successfully, false if user not found
     */
    public boolean update(User user) {
        if (user == null || !users.containsKey(user.getUserId())) {
            return false;
        }
        users.put(user.getUserId(), user);
        return true;
    }

    /**
     * Deletes a user from the repository.
     *
     * @param userId User ID to delete
     * @return true if deleted successfully, false if user not found
     */
    public boolean delete(String userId) {
        return users.remove(userId) != null;
    }

    /**
     * Checks if a user exists by ID.
     *
     * @param userId User ID to check
     * @return true if user exists, false otherwise
     */
    public boolean exists(String userId) {
        return users.containsKey(userId);
    }

    /**
     * Checks if a username is already taken.
     *
     * @param username Username to check
     * @return true if username exists, false otherwise
     */
    public boolean usernameExists(String username) {
        return users.values().stream()
                .anyMatch(user -> user.getUsername().equals(username));
    }

    /**
     * Gets the total number of users.
     *
     * @return User count
     */
    public int getUserCount() {
        return users.size();
    }

    /**
     * Clears all users from the repository.
     */
    public void clear() {
        users.clear();
    }
}
