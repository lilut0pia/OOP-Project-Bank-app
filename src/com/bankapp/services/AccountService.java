package com.bankapp.services;

import com.bankapp.service.Bank;
import com.bankapp.model.User;
import com.bankapp.model.CheckingAccount;
import com.bankapp.model.SavingsAccount;
import com.bankapp.utils.IDGenerator;

/**
 * AccountService - Handles account operations (create, retrieve, close accounts).
 * Implements the Single Responsibility Principle - focuses on account management.
 */
public class AccountService {
    private final Bank bank;

    /**
     * Constructor - initializes with data store.
     */
    public AccountService(Bank bank) {
        this.bank = bank;
    }

    /**
     * Creates a new checking account for a user.
     *
     * @param user User to create account for
     * @param initialBalance Initial account balance
     * @param overdraftLimit Maximum overdraft amount
     * @return CheckingAccount object if successful, null if failed
     */
    public CheckingAccount createCheckingAccount(User user, double initialBalance, double overdraftLimit) {
        if (user == null || initialBalance < 0 || overdraftLimit < 0) {
            return null;
        }

        String accountNumber = IDGenerator.generateAccountNumber();
        CheckingAccount account = new CheckingAccount(user, accountNumber, initialBalance, overdraftLimit);
        // Add account to user
        if (user.addAccount(account)) {
            return account;
        }
        return null;
    }

    /**
     * Creates a new savings account for a user.
     *
     * @param user User to create account for
     * @param initialBalance Initial account balance
     * @param interestRate Annual interest rate
     * @return SavingsAccount object if successful, null if failed
     */
    public SavingsAccount createSavingsAccount(User user, double initialBalance, double interestRate) {
        if (user == null || initialBalance < 0 || interestRate < 0) {
            return null;
        }

        try {
            String accountNumber = IDGenerator.generateAccountNumber();
            SavingsAccount account = new SavingsAccount(user, accountNumber, initialBalance, interestRate);
            // Add account to user
            if (user.addAccount(account)) {
                return account;
            }
        } catch (IllegalArgumentException e) {
            // The exception from the SavingsAccount constructor is caught here.
            System.out.println("Error: " + e.getMessage());
        }
        return null;
    }
}
