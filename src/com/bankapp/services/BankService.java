package com.bankapp.services;

import com.bankapp.model.SavingsAccount;
import java.util.HashMap;
import java.util.Map;

/**
 * BankService - Facade service that coordinates multiple services.
 * Provides a unified interface for bank operations.
 * Implements the Facade Pattern to simplify client interactions.
 */
public class BankService {
    private final AuthService authService;
    private final AccountService accountService;
    private final TransactionService transactionService;
    private final Map<String, SavingsAccount> interestRates; // For demonstration

    /**
     * Constructor - initializes all sub-services.
     */
    public BankService() {
        this.authService = new AuthService();
        this.accountService = new AccountService();
        this.transactionService = new TransactionService();
        this.interestRates = new HashMap<>();
    }

    /**
     * Gets the AuthService.
     *
     * @return AuthService instance
     */
    public AuthService getAuthService() {
        return authService;
    }

    /**
     * Gets the AccountService.
     *
     * @return AccountService instance
     */
    public AccountService getAccountService() {
        return accountService;
    }

    /**
     * Gets the TransactionService.
     *
     * @return TransactionService instance
     */
    public TransactionService getTransactionService() {
        return transactionService;
    }

    /**
     * Gets system statistics.
     *
     * @return Statistics summary
     */
    public String getSystemStats() {
        StringBuilder stats = new StringBuilder();
        stats.append("=== BANK SYSTEM STATISTICS ===\n");
        stats.append(String.format("Total Users: %d%n",
                authService.getUserById("USER_001") != null ? "N/A" : 0));
        stats.append(String.format("Total Accounts: %d%n", accountService.accountExists("ACC_001") ? "N/A" : 0));
        return stats.toString();
    }
}
