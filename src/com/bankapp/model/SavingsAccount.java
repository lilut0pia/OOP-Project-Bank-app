package com.bankapp.model;

/**
 * SavingsAccount class - concrete implementation of Account.
 * Represents a savings account with withdrawal restrictions and interest rates.
 * Demonstrates Inheritance and Polymorphism.
 */
public class SavingsAccount extends Account { // Lỗi: SavingsAccount không có constructor phù hợp
    private static final long serialVersionUID = 1L;
    private static final double MINIMUM_BALANCE = 100.0;
    private static final double MAX_INTEREST_RATE = 0.05; // Giới hạn lãi suất tối đa là 1%
    private double interestRate; // Annual interest rate (e.g., 0.03 for 3%)
    private int withdrawalsThisMonth;
    private static final int MAX_MONTHLY_WITHDRAWALS = 6; // Federal regulation example
    private double withdrawalPenalty; // Penalty for exceeding withdrawal limit

    /**
     * Constructor for SavingsAccount.
     *
     * @param owner           The user who owns this account
     * @param accountNumber   Unique account identifier
     * @param initialBalance  Initial account balance
     * @param interestRate    Annual interest rate (as decimal, e.g., 0.03 for 3%)
     */
    public SavingsAccount(User owner, String accountNumber, double initialBalance, double interestRate) {
        super(owner, accountNumber, initialBalance);
        // Validate and set interest rate directly in the constructor
        if (interestRate < 0 || interestRate > MAX_INTEREST_RATE) {
            throw new IllegalArgumentException("Invalid interest rate. The rate must be between 0 and " + (MAX_INTEREST_RATE * 100) + "%.");
        }
        this.interestRate = interestRate;
        this.withdrawalsThisMonth = 0;
        this.withdrawalPenalty = 25.0; // Default penalty
    }

    /**
     * Constructor with default interest rate and penalty.
     *
     * @param owner           The user who owns this account
     * @param accountNumber   Unique account identifier
     * @param initialBalance  Initial account balance
     */
    public SavingsAccount(User owner, String accountNumber, double initialBalance) {
        this(owner, accountNumber, initialBalance, 0.025); // Default 2.5% interest
    }

    // ============= Getters and Setters =============

    public double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(double interestRate) {
        if (interestRate >= 0 && interestRate <= MAX_INTEREST_RATE) {
            this.interestRate = interestRate;
        } else {
            throw new IllegalArgumentException("Invalid interest rate. The rate must be between 0 and " + (MAX_INTEREST_RATE * 100) + "%.");
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
