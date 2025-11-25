package com.bankapp.utils;

import java.util.Random;

/**
 * IDGenerator - Utility class for generating unique identifiers.
 * Generates IDs for users, accounts, and transactions.
 */
public class IDGenerator {
    private static final Random random = new Random();
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    /**
     * Generates a unique user ID.
     * Format: USER_XXXXXXXX (e.g., USER_ABC12345)
     *
     * @return Generated user ID
     */
    public static String generateUserId() {
        return "USER_" + generateRandomString(8);
    }

    /**
     * Generates a unique account number.
     * Format: ACC_XXXXXXXX (e.g., ACC_12ABC345)
     *
     * @return Generated account number
     */
    public static String generateAccountNumber() {
        return "ACC" + System.currentTimeMillis() % 1000000;
    }

    /**
     * Generates a unique transaction ID.
     * Format: TXN_XXXXXXXXXXXX
     *
     * @return Generated transaction ID
     */
    public static String generateTransactionId() {
        return "TXN_" + System.nanoTime();
    }

    /**
     * Generates a random string of specified length.
     *
     * @param length Length of the string to generate
     * @return Random string
     */
    private static String generateRandomString(int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
        }
        return sb.toString();
    }
}
