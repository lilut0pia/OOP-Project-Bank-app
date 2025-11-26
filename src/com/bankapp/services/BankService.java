package com.bankapp.services;

import com.bankapp.service.Bank;
import com.bankapp.model.SavingsAccount;
import java.util.HashMap;
import java.util.Map;

/**
 * BankService - Facade service that coordinates multiple services.
 * Provides a unified interface for bank operations.
 * Implements the Facade Pattern to simplify client interactions.
 */
public class BankService {
    private final Bank bank;
    private final AuthService authService;
    private final AccountService accountService;
    private final TransactionService transactionService;
    private final Map<String, SavingsAccount> interestRates; // For demonstration

    /**
     * Constructor - initializes all sub-services.
     */
    public BankService(Bank bank) {
        this.bank = bank;
        this.authService = new AuthService(bank);
        this.accountService = new AccountService(bank); // Cập nhật AccountService
        this.transactionService = new TransactionService(bank);
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
        // Lấy dữ liệu trực tiếp từ đối tượng Bank
        int totalUsers = bank.getAllUsers().size();
        long totalAccounts = bank.getAllUsers().stream()
                .mapToLong(user -> user.getAccounts().size())
                .sum();

        stats.append("=== BANK SYSTEM STATISTICS ===\n");
        stats.append(String.format("Total Users: %d%n", totalUsers));
        stats.append(String.format("Total Accounts: %d%n", totalAccounts));
        return stats.toString();
    }
}
