package com.bankapp.data;

import com.bankapp.model.Transaction;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * TransactionRepository - Data access object for Transaction entities.
 * Handles transaction storage and retrieval operations.
 */
public class TransactionRepository {
    private Map<String, List<Transaction>> accountTransactions; // accountNumber -> List of Transactions
    private List<Transaction> allTransactions; // Global transaction log

    public TransactionRepository() {
        this.accountTransactions = new HashMap<>();
        this.allTransactions = new ArrayList<>();
    }

    /**
     * Records a transaction in the repository.
     *
     * @param accountNumber Account number for the transaction
     * @param transaction Transaction object to save
     * @return true if saved successfully
     */
    public boolean saveTransaction(String accountNumber, Transaction transaction) {
        if (accountNumber == null || transaction == null) {
            return false;
        }
        accountTransactions.computeIfAbsent(accountNumber, k -> new ArrayList<>()).add(transaction);
        allTransactions.add(transaction);
        return true;
    }

    /**
     * Gets all transactions for a specific account.
     *
     * @param accountNumber Account number to retrieve transactions for
     * @return List of transactions for the account
     */
    public List<Transaction> getTransactionsByAccount(String accountNumber) {
        return new ArrayList<>(accountTransactions.getOrDefault(accountNumber, new ArrayList<>()));
    }

    /**
     * Gets recent transactions for an account.
     *
     * @param accountNumber Account number to retrieve transactions for
     * @param count Number of recent transactions to retrieve
     * @return List of recent transactions
     */
    public List<Transaction> getRecentTransactions(String accountNumber, int count) {
        List<Transaction> transactions = getTransactionsByAccount(accountNumber);
        int size = transactions.size();
        int startIndex = Math.max(0, size - count);
        return new ArrayList<>(transactions.subList(startIndex, size));
    }

    /**
     * Gets a transaction by its ID.
     *
     * @param transactionId Transaction ID to search for
     * @return Transaction object if found, null otherwise
     */
    public Transaction findById(String transactionId) {
        return allTransactions.stream()
                .filter(txn -> txn.getTransactionId().equals(transactionId))
                .findFirst()
                .orElse(null);
    }

    /**
     * Gets the transaction count for a specific account.
     *
     * @param accountNumber Account number
     * @return Transaction count for the account
     */
    public int getTransactionCount(String accountNumber) {
        return accountTransactions.getOrDefault(accountNumber, new ArrayList<>()).size();
    }

    /**
     * Gets the total transaction count across all accounts.
     *
     * @return Total transaction count
     */
    public int getTotalTransactionCount() {
        return allTransactions.size();
    }

    /**
     * Clears all transactions.
     */
    public void clear() {
        accountTransactions.clear();
        allTransactions.clear();
    }
}
