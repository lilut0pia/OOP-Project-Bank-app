package com.bankapp.data;

/**
 * InMemoryDataStore - Central data storage for the banking application.
 * Implements the Repository Pattern for data access abstraction.
 * All data is stored in memory and will be lost on application shutdown
 * (unless extended with file/database persistence).
 */
public class InMemoryDataStore {
    private static InMemoryDataStore instance;
    private UserRepository userRepository;
    private AccountRepository accountRepository;
    private TransactionRepository transactionRepository;

    /**
     * Private constructor for Singleton pattern.
     */
    private InMemoryDataStore() {
        this.userRepository = new UserRepository();
        this.accountRepository = new AccountRepository();
        this.transactionRepository = new TransactionRepository();
    }

    /**
     * Gets the singleton instance of InMemoryDataStore.
     *
     * @return InMemoryDataStore instance
     */
    public static synchronized InMemoryDataStore getInstance() {
        if (instance == null) {
            instance = new InMemoryDataStore();
        }
        return instance;
    }

    /**
     * Gets the user repository.
     *
     * @return UserRepository instance
     */
    public UserRepository getUserRepository() {
        return userRepository;
    }

    /**
     * Gets the account repository.
     *
     * @return AccountRepository instance
     */
    public AccountRepository getAccountRepository() {
        return accountRepository;
    }

    /**
     * Gets the transaction repository.
     *
     * @return TransactionRepository instance
     */
    public TransactionRepository getTransactionRepository() {
        return transactionRepository;
    }

    /**
     * Clears all data from the data store.
     * Useful for testing or resetting the application.
     */
    public void clearAll() {
        userRepository.clear();
        accountRepository.clear();
        transactionRepository.clear();
    }
}
