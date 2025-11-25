package com.bankapp.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Abstract base class for all account types.
 * Implements Abstraction principle - defines common interface for all accounts.
 * Uses Encapsulation - hides internal implementation details.
 */
public abstract class Account implements Serializable {
    private static final long serialVersionUID = 1L;
    protected String accountNumber;
    protected double balance;
    protected long createdAt;
    protected List<Transaction> transactions;
    protected boolean isActive;

    /**
     * Constructor for Account.
     *
     * @param accountNumber Unique account identifier
     * @param initialBalance Initial account balance
     */
    public Account(String accountNumber, double initialBalance) {
        this.accountNumber = accountNumber;
        this.balance = initialBalance;
        this.createdAt = System.currentTimeMillis();
        this.transactions = new ArrayList<>();
        this.isActive = true;
    }

    // ============= Getters =============

    public String getAccountNumber() {
        return accountNumber;
    }

    public double getBalance() {
        return balance;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public List<Transaction> getTransactions() {
        return new ArrayList<>(transactions);
    }

    public boolean isActive() {
        return isActive;
    }

    // ============= Abstract Methods =============

    /**
     * Abstract method to get account type.
     * Implements Polymorphism - different implementations in subclasses.
     *
     * @return Account type as string
     */
    public abstract String getAccountType();

    /**
     * Abstract method to check withdrawal eligibility.
     * Different account types may have different withdrawal rules.
     *
     * @param amount Amount to withdraw
     * @return true if withdrawal is allowed, false otherwise
     */
    public abstract boolean canWithdraw(double amount);

    /**
     * Abstract method to apply account-specific rules.
     * E.g., SavingsAccount may apply interest penalties.
     */
    public abstract void applyAccountSpecificRules();

    // ============= Transaction Methods =============

    /**
     * Deposits money into the account.
     *
     * @param amount  Amount to deposit
     * @param description Description of the deposit
     * @return true if deposit was successful, false otherwise
     */
    public boolean deposit(double amount, String description) {
        if (amount <= 0) {
            return false;
        }
        this.balance += amount;
        Transaction transaction = new Transaction(
                "DEP-" + System.nanoTime(),
                this.accountNumber,
                null,
                amount,
                "DEPOSIT",
                description
        );
        this.transactions.add(transaction);
        return true;
    }

    /**
     * Withdraws money from the account.
     *
     * @param amount  Amount to withdraw
     * @param description Description of the withdrawal
     * @return true if withdrawal was successful, false otherwise
     */
    public boolean withdraw(double amount, String description) {
        if (amount <= 0 || !canWithdraw(amount) || this.balance < amount) {
            return false;
        }
        this.balance -= amount;
        Transaction transaction = new Transaction(
                "WTH-" + System.nanoTime(),
                this.accountNumber,
                null,
                amount,
                "WITHDRAWAL",
                description
        );
        this.transactions.add(transaction);
        applyAccountSpecificRules();
        return true;
    }

    /**
     * Records a transfer out (sending money to another account).
     *
     * @param amount  Amount to transfer
     * @param toAccountNumber Recipient account number
     * @return true if transfer was recorded, false otherwise
     */
    public boolean transfer(double amount, String toAccountNumber) {
        if (amount <= 0 || !canWithdraw(amount) || this.balance < amount) {
            return false;
        }
        this.balance -= amount;
        Transaction transaction = new Transaction(
                "TRF-" + System.nanoTime(),
                this.accountNumber,
                toAccountNumber,
                amount,
                "TRANSFER_OUT",
                "Transfer to " + toAccountNumber
        );
        this.transactions.add(transaction);
        applyAccountSpecificRules();
        return true;
    }

    /**
     * Records a transfer in (receiving money from another account).
     *
     * @param amount  Amount received
     * @param fromAccountNumber Sender account number
     */
    public void receiveTransfer(double amount, String fromAccountNumber) {
        this.balance += amount;
        Transaction transaction = new Transaction(
                "TRF-" + System.nanoTime(),
                this.accountNumber,
                fromAccountNumber,
                amount,
                "TRANSFER_IN",
                "Transfer from " + fromAccountNumber
        );
        this.transactions.add(transaction);
    }

    /**
     * Closes the account (sets it to inactive).
     */
    public void closeAccount() {
        this.isActive = false;
    }

    /**
     * Gets recent transactions (last n transactions).
     *
     * @param count Number of recent transactions to retrieve
     * @return List of recent transactions
     */
    public List<Transaction> getRecentTransactions(int count) {
        int size = transactions.size();
        int startIndex = Math.max(0, size - count);
        return new ArrayList<>(transactions.subList(startIndex, size));
    }

    @Override
    public String toString() {
        return getAccountType() + "{" +
                "accountNumber='" + accountNumber + '\'' +
                ", balance=" + balance +
                ", isActive=" + isActive +
                ", transactionCount=" + transactions.size() +
                '}';
    }
}
