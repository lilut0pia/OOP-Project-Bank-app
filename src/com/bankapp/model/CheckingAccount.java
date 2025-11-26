package com.bankapp.model;

/**
 * CheckingAccount class - concrete implementation of Account.
 * Represents a standard checking account with no special restrictions.
 * Demonstrates Inheritance and Polymorphism.
 */
public class CheckingAccount extends Account {
    private static final long serialVersionUID = 1L;
    private static final double MINIMUM_BALANCE = 0.0;
    private double overdraftLimit;
    private int monthlyWithdrawals;
    private int maxMonthlyWithdrawals;

    /**
     * Constructor for CheckingAccount.
     *
     * @param owner            The user who owns this account
     * @param accountNumber    Unique account identifier
     * @param initialBalance   Initial account balance
     * @param overdraftLimit   Maximum overdraft amount allowed
     */
    public CheckingAccount(User owner, String accountNumber, double initialBalance, double overdraftLimit) {
        super(owner, accountNumber, initialBalance);
        this.overdraftLimit = overdraftLimit;
        this.monthlyWithdrawals = 0;
        this.maxMonthlyWithdrawals = Integer.MAX_VALUE; // Unlimited by default
    }

    /**
     * Constructor with default overdraft limit.
     *
     * @param owner           The user who owns this account
     * @param accountNumber   Unique account identifier
     * @param initialBalance  Initial account balance
     */
    public CheckingAccount(User owner, String accountNumber, double initialBalance) {
        this(owner, accountNumber, initialBalance, 0.0);
    }

    // ============= Getters and Setters =============

    public double getOverdraftLimit() {
        return overdraftLimit;
    }

    public void setOverdraftLimit(double overdraftLimit) {
        if (overdraftLimit >= 0) {
            this.overdraftLimit = overdraftLimit;
        }
    }

    public int getMonthlyWithdrawals() {
        return monthlyWithdrawals;
    }

    // ============= Implementation of Abstract Methods =============

    /**
     * Returns the account type.
     *
     * @return "CHECKING"
     */
    @Override
    public String getAccountType() {
        return "CHECKING";
    }

    /**
     * Determines if a withdrawal can be made.
     * Checking accounts allow withdrawals if balance + overdraft limit is sufficient.
     *
     * @param amount Amount to withdraw
     * @return true if withdrawal is allowed, false otherwise
     */
    @Override
    public boolean canWithdraw(double amount) {
        return !isActive || (this.balance + this.overdraftLimit) >= amount;
    }

    /**
     * Applies checking account specific rules.
     * Currently, no special rules are applied after withdrawal.
     */
    @Override
    public void applyAccountSpecificRules() {
        monthlyWithdrawals++;
        // Could add overdraft fee logic here if balance goes negative
        if (this.balance < 0) {
            // Apply overdraft fee (example)
            // this.balance -= 35.0; // Overdraft fee
        }
    }

    /**
     * Resets monthly withdrawal counter (should be called monthly).
     */
    public void resetMonthlyWithdrawals() {
        this.monthlyWithdrawals = 0;
    }

    @Override
    public String toString() {
        return "CheckingAccount{" +
                "accountNumber='" + accountNumber + '\'' +
                ", balance=" + balance +
                ", overdraftLimit=" + overdraftLimit +
                ", isActive=" + isActive +
                ", transactionCount=" + transactions.size() +
                '}';
    }
}
