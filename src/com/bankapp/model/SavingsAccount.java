package com.bankapp.model;

/**
 * SavingsAccount class - concrete implementation of Account.
 * Represents a savings account with withdrawal restrictions and interest rates.
 * Demonstrates Inheritance and Polymorphism.
 */
public class SavingsAccount extends Account {
    private static final long serialVersionUID = 1L;
    private static final double MINIMUM_BALANCE = 100.0;
    private double interestRate; // Annual interest rate (e.g., 0.03 for 3%)
    private int withdrawalsThisMonth;
    private static final int MAX_MONTHLY_WITHDRAWALS = 6; // Federal regulation example
    private double withdrawalPenalty; // Penalty for exceeding withdrawal limit

    /**
     * Constructor for SavingsAccount.
     *
     * @param accountNumber   Unique account identifier
     * @param initialBalance  Initial account balance
     * @param interestRate    Annual interest rate (as decimal, e.g., 0.03 for 3%)
     */
    public SavingsAccount(String accountNumber, double initialBalance, double interestRate) {
        super(accountNumber, initialBalance);
        this.interestRate = interestRate;
        this.withdrawalsThisMonth = 0;
        this.withdrawalPenalty = 25.0; // Default penalty
    }

    /**
     * Constructor with default interest rate.
     *
     * @param accountNumber   Unique account identifier
     * @param initialBalance  Initial account balance
     */
    public SavingsAccount(String accountNumber, double initialBalance) {
        this(accountNumber, initialBalance, 0.025); // Default 2.5% interest
    }

    // ============= Getters and Setters =============

    public double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(double interestRate) {
        if (interestRate >= 0) {
            this.interestRate = interestRate;
        }
    }

    public int getWithdrawalsThisMonth() {
        return withdrawalsThisMonth;
    }

    public double getWithdrawalPenalty() {
        return withdrawalPenalty;
    }

    public void setWithdrawalPenalty(double penalty) {
        if (penalty >= 0) {
            this.withdrawalPenalty = penalty;
        }
    }

    // ============= Implementation of Abstract Methods =============

    /**
     * Returns the account type.
     *
     * @return "SAVINGS"
     */
    @Override
    public String getAccountType() {
        return "SAVINGS";
    }

    /**
     * Determines if a withdrawal can be made.
     * Savings accounts have monthly withdrawal limits and minimum balance requirements.
     *
     * @param amount Amount to withdraw
     * @return true if withdrawal is allowed, false otherwise
     */
    @Override
    public boolean canWithdraw(double amount) {
        if (!isActive) {
            return false;
        }
        // Check monthly withdrawal limit
        if (withdrawalsThisMonth >= MAX_MONTHLY_WITHDRAWALS) {
            return false;
        }
        // Check minimum balance requirement
        if ((this.balance - amount) < MINIMUM_BALANCE) {
            return false;
        }
        return true;
    }

    /**
     * Applies savings account specific rules.
     * Increments withdrawal counter and applies withdrawal penalty if limit exceeded.
     */
    @Override
    public void applyAccountSpecificRules() {
        withdrawalsThisMonth++;

        // If withdrawal limit is exceeded, apply penalty
        if (withdrawalsThisMonth > MAX_MONTHLY_WITHDRAWALS) {
            this.balance -= withdrawalPenalty;
            // Log penalty transaction
            Transaction penaltyTxn = new Transaction(
                    "PEN-" + System.nanoTime(),
                    this.accountNumber,
                    null,
                    withdrawalPenalty,
                    "WITHDRAWAL_PENALTY",
                    "Excess withdrawal penalty"
            );
            this.transactions.add(penaltyTxn);
        }
    }

    /**
     * Applies interest to the account balance.
     * Should be called monthly or periodically.
     * Annual interest rate is divided by 12 for monthly calculation.
     *
     * @return Interest amount applied
     */
    public double applyMonthlyInterest() {
        double monthlyRate = interestRate / 12.0;
        double interest = this.balance * monthlyRate;
        this.balance += interest;

        // Record interest transaction
        Transaction interestTxn = new Transaction(
                "INT-" + System.nanoTime(),
                this.accountNumber,
                null,
                interest,
                "INTEREST",
                "Monthly interest credit"
        );
        this.transactions.add(interestTxn);

        return interest;
    }

    /**
     * Resets monthly withdrawal counter.
     * Should be called at the start of each month.
     */
    public void resetMonthlyWithdrawals() {
        this.withdrawalsThisMonth = 0;
    }

    /**
     * Calculates projected annual interest based on current balance.
     *
     * @return Projected annual interest amount
     */
    public double getProjectedAnnualInterest() {
        return this.balance * interestRate;
    }

    @Override
    public String toString() {
        return "SavingsAccount{" +
                "accountNumber='" + accountNumber + '\'' +
                ", balance=" + balance +
                ", interestRate=" + (interestRate * 100) + "%" +
                ", withdrawalsThisMonth=" + withdrawalsThisMonth +
                ", isActive=" + isActive +
                ", transactionCount=" + transactions.size() +
                '}';
    }
}
