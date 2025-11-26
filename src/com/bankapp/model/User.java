package com.bankapp.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * User class representing a bank customer.
 * Encapsulates user information and manages associated accounts.
 */
public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    private String userId;
    private String username;
    private String passwordHash;
    private String fullName;
    private String email;
    private List<Account> accounts;
    private long createdAt;

    /**
     * Constructor for creating a new User.
     *
     * @param userId       Unique identifier for the user
     * @param username     Username for login
     * @param passwordHash Hashed password for security
     * @param fullName     User's full name
     * @param email        User's email address
     */
    public User(String userId, String username, String passwordHash, String fullName, String email) {
        this.userId = userId;
        this.username = username;
        this.passwordHash = passwordHash;
        this.fullName = fullName;
        this.email = email;
        this.accounts = new ArrayList<>();
        this.createdAt = System.currentTimeMillis();
    }

    // ============= Getters and Setters =============

    public String getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Account> getAccounts() {
        return new ArrayList<>(accounts);
    }

    public long getCreatedAt() {
        return createdAt;
    }

    // ============= Account Management =============

    /**
     * Verifies the provided hashed password.
     * In a real application, this would involve comparing hashes.
     * @param passwordHash The hashed password to compare against.
     * @return true if the password matches.
     */
    public boolean verifyPassword(String passwordHash) {
        return this.passwordHash.equals(passwordHash);
    }

    /**
     * Adds an account to the user's account list.
     * Prevents duplicate accounts.
     *
     * @param account Account to be added
     * @return true if account was added successfully, false otherwise
     */
    public boolean addAccount(Account account) {
        if (account != null && !accounts.contains(account)) {
            return accounts.add(account);
        }
        return false;
    }

    /**
     * Retrieves an account by its account number.
     *
     * @param accountNumber The account number to search for
     * @return Account object if found, null otherwise
     */
    public Account getAccountByNumber(String accountNumber) {
        return accounts.stream()
                .filter(acc -> acc.getAccountNumber().equals(accountNumber))
                .findFirst()
                .orElse(null);
    }

    /**
     * Gets all checking accounts for the user.
     *
     * @return List of CheckingAccount objects
     */
    public List<Account> getCheckingAccounts() {
        List<Account> checkingAccounts = new ArrayList<>();
        for (Account account : accounts) {
            if (account instanceof CheckingAccount) {
                checkingAccounts.add(account);
            }
        }
        return checkingAccounts;
    }

    /**
     * Gets all savings accounts for the user.
     *
     * @return List of SavingsAccount objects
     */
    public List<Account> getSavingsAccounts() {
        List<Account> savingsAccounts = new ArrayList<>();
        for (Account account : accounts) {
            if (account instanceof SavingsAccount) {
                savingsAccounts.add(account);
            }
        }
        return savingsAccounts;
    }

    /**
     * Gets the total balance across all accounts.
     *
     * @return Total balance
     */
    public double getTotalBalance() {
        return accounts.stream()
                .mapToDouble(Account::getBalance)
                .sum();
    }

    @Override
    public String toString() {
        return "User{" +
                "userId='" + userId + '\'' +
                ", username='" + username + '\'' +
                ", fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                ", accountCount=" + accounts.size() +
                ", totalBalance=" + getTotalBalance() +
                '}';
    }
}
