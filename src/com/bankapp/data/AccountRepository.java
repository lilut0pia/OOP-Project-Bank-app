package com.bankapp.data;

import com.bankapp.model.Account;
import java.util.HashMap;
import java.util.Map;

/**
 * AccountRepository - Data access object for Account entities.
 * Handles account storage and retrieval operations.
 */
public class AccountRepository {
    private Map<String, Account> accounts; // accountNumber -> Account

    public AccountRepository() {
        this.accounts = new HashMap<>();
    }

    /**
     * Saves an account to the repository.
     *
     * @param account Account object to save
     * @return true if saved successfully, false if account already exists
     */
    public boolean save(Account account) {
        if (account == null || accounts.containsKey(account.getAccountNumber())) {
            return false;
        }
        accounts.put(account.getAccountNumber(), account);
        return true;
    }

    /**
     * Finds an account by account number.
     *
     * @param accountNumber Account number to search for
     * @return Account object if found, null otherwise
     */
    public Account findByAccountNumber(String accountNumber) {
        return accounts.get(accountNumber);
    }

    /**
     * Updates an existing account.
     *
     * @param account Account object to update
     * @return true if updated successfully, false if account not found
     */
    public boolean update(Account account) {
        if (account == null || !accounts.containsKey(account.getAccountNumber())) {
            return false;
        }
        accounts.put(account.getAccountNumber(), account);
        return true;
    }

    /**
     * Deletes an account from the repository.
     *
     * @param accountNumber Account number to delete
     * @return true if deleted successfully, false if account not found
     */
    public boolean delete(String accountNumber) {
        return accounts.remove(accountNumber) != null;
    }

    /**
     * Checks if an account exists.
     *
     * @param accountNumber Account number to check
     * @return true if account exists, false otherwise
     */
    public boolean exists(String accountNumber) {
        return accounts.containsKey(accountNumber);
    }

    /**
     * Gets the total number of accounts.
     *
     * @return Account count
     */
    public int getAccountCount() {
        return accounts.size();
    }

    /**
     * Clears all accounts from the repository.
     */
    public void clear() {
        accounts.clear();
    }
}
