package com.bankapp.services;

import com.bankapp.data.InMemoryDataStore;
import com.bankapp.data.AccountRepository;
import com.bankapp.model.Account;
import com.bankapp.model.User;
import com.bankapp.model.CheckingAccount;
import com.bankapp.model.SavingsAccount;
import com.bankapp.utils.IDGenerator;

/**
 * AccountService - Handles account operations (create, retrieve, close accounts).
 * Implements the Single Responsibility Principle - focuses on account management.
 */
public class AccountService {
    private final AccountRepository accountRepository;

    /**
     * Constructor - initializes with data store.
     */
    public AccountService() {
        this.accountRepository = InMemoryDataStore.getInstance().getAccountRepository();
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
        CheckingAccount account = new CheckingAccount(accountNumber, initialBalance, overdraftLimit);

        // Save account to repository
        if (accountRepository.save(account)) {
            // Add account to user
            if (user.addAccount(account)) {
                return account;
            }
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

        String accountNumber = IDGenerator.generateAccountNumber();
        SavingsAccount account = new SavingsAccount(accountNumber, initialBalance, interestRate);

        // Save account to repository
        if (accountRepository.save(account)) {
            // Add account to user
            if (user.addAccount(account)) {
                return account;
            }
        }
        return null;
    }

    /**
     * Retrieves an account by its account number.
     *
     * @param accountNumber Account number to search for
     * @return Account object if found, null otherwise
     */
    public Account getAccount(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber);
    }

    /**
     * Closes an account.
     *
     * @param accountNumber Account number to close
     * @return true if account was closed, false otherwise
     */
    public boolean closeAccount(String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber);
        if (account != null) {
            account.closeAccount();
            accountRepository.update(account);
            return true;
        }
        return false;
    }

    /**
     * Checks if an account exists.
     *
     * @param accountNumber Account number to check
     * @return true if account exists, false otherwise
     */
    public boolean accountExists(String accountNumber) {
        return accountRepository.exists(accountNumber);
    }

    /**
     * Gets account balance.
     *
     * @param accountNumber Account number
     * @return Account balance, or -1 if account not found
     */
    public double getAccountBalance(String accountNumber) {
        Account account = getAccount(accountNumber);
        return account != null ? account.getBalance() : -1;
    }

    /**
     * Gets account type.
     *
     * @param accountNumber Account number
     * @return Account type (CHECKING, SAVINGS), or null if not found
     */
    public String getAccountType(String accountNumber) {
        Account account = getAccount(accountNumber);
        return account != null ? account.getAccountType() : null;
    }
}
