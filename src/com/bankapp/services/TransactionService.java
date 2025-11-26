package com.bankapp.services;

import com.bankapp.service.Bank;
import com.bankapp.model.Account;
import com.bankapp.model.Transaction;
import java.util.List;

/**
 * TransactionService - Handles transaction operations (deposit, withdraw, transfer).
 * Implements the Single Responsibility Principle - focuses on transaction logic.
 */
public class TransactionService {
    private Bank bank;

    /**
     * Constructor - initializes with data store.
     */
    public TransactionService(Bank bank) {
        this.bank = bank;
    }
    public void setBank(Bank bank) {
        this.bank = bank;
    }

    /**
     * Deposits money into an account.
     *
     * @param accountNumber Account number to deposit to
     * @param amount Amount to deposit
     * @param description Transaction description
     * @return true if deposit was successful, false otherwise
     */
    public boolean deposit(String accountNumber, double amount, String description) {
        if (amount <= 0) {
            return false;
        }

        Account account = findAccountByNumber(accountNumber);
        if (account == null || !account.isActive()) {
            return false;
        }

        if (account.deposit(amount, description)) {
            return true;
        }
        return false;
    }

    /**
     * Withdraws money from an account.
     *
     * @param accountNumber Account number to withdraw from
     * @param amount Amount to withdraw
     * @param description Transaction description
     * @return true if withdrawal was successful, false otherwise
     */
    public boolean withdraw(String accountNumber, double amount, String description) {
        if (amount <= 0) {
            return false;
        }

        Account account = findAccountByNumber(accountNumber);
        if (account == null || !account.isActive()) {
            return false;
        }

        if (account.withdraw(amount, description)) {
            return true;
        }
        return false;
    }

    /**
     * Transfers money between two accounts.
     *
     * @param fromAccountNumber Source account number
     * @param toAccountNumber Destination account number
     * @param amount Amount to transfer
     * @param description Transaction description
     * @return true if transfer was successful, false otherwise
     */
    public boolean transfer(String fromAccountNumber, String toAccountNumber, double amount, String description) {
        if (amount <= 0) {
            return false;
        }

        // Validate accounts
        Account fromAccount = findAccountByNumber(fromAccountNumber);
        Account toAccount = findAccountByNumber(toAccountNumber);

        if (fromAccount == null || toAccount == null ||
                !fromAccount.isActive() || !toAccount.isActive()) {
            return false;
        }

        // Check if transfer is possible
        if (!fromAccount.canWithdraw(amount) || fromAccount.getBalance() < amount) {
            return false;
        }

        // Perform transfer
        if (fromAccount.transfer(amount, toAccountNumber)) {
            toAccount.receiveTransfer(amount, fromAccountNumber);

            return true;
        }
        return false;
    }

    /**
     * Gets transaction history for an account.
     *
     * @param accountNumber Account number to get history for
     * @return List of transactions
     */
    public List<Transaction> getTransactionHistory(String accountNumber) {
        Account account = findAccountByNumber(accountNumber);
        if (account != null) {
            return account.getTransactions();
        }
        return List.of();
    }

    /**
     * Gets recent transactions for an account.
     *
     * @param accountNumber Account number to get history for
     * @param count Number of recent transactions to retrieve
     * @return List of recent transactions
     */
    public List<Transaction> getRecentTransactions(String accountNumber, int count) {
        Account account = findAccountByNumber(accountNumber);
        if (account != null) {
            return account.getRecentTransactions(count);
        }
        return List.of();
    }

    /**
     * Helper method to find an account across all users in the bank.
     * @param accountNumber The account number to find.
     * @return The Account object if found, otherwise null.
     */
    private Account findAccountByNumber(String accountNumber) {
        return bank.getAllAccounts().stream()
                .filter(acc -> acc.getAccountNumber().equals(accountNumber))
                .findFirst()
                .orElse(null);
    }
}
