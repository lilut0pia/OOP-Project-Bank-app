package com.bankapp.utils;

import java.util.Scanner;

/**
 * ConsoleUtils - Utility class for console input/output operations.
 * Provides helper methods for displaying messages and reading user input.
 */
public class ConsoleUtils {
    private static final Scanner scanner = new Scanner(System.in);
    private static final String SEPARATOR = "=".repeat(60);
    private static final String DASH_SEPARATOR = "-".repeat(60);

    /**
     * Prints a header message.
     *
     * @param message Message to display as header
     */
    public static void printHeader(String message) {
        System.out.println("\n" + SEPARATOR);
        System.out.println("  " + message);
        System.out.println(SEPARATOR);
    }

    /**
     * Prints a subheader message.
     *
     * @param message Message to display as subheader
     */
    public static void printSubHeader(String message) {
        System.out.println("\n" + DASH_SEPARATOR);
        System.out.println("  " + message);
        System.out.println(DASH_SEPARATOR);
    }

    /**
     * Prints a success message.
     *
     * @param message Success message
     */
    public static void printSuccess(String message) {
        System.out.println("✓ " + message);
    }

    /**
     * Prints an error message.
     *
     * @param message Error message
     */
    public static void printError(String message) {
        System.out.println("✗ Error: " + message);
    }

    /**
     * Prints an info message.
     *
     * @param message Info message
     */
    public static void printInfo(String message) {
        System.out.println("ℹ " + message);
    }

    /**
     * Prints a warning message.
     *
     * @param message Warning message
     */
    public static void printWarning(String message) {
        System.out.println("⚠ " + message);
    }

    /**
     * Reads a string input from the user.
     *
     * @param prompt Prompt to display
     * @return User input
     */
    public static String readString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    /**
     * Reads a numeric input from the user with validation.
     *
     * @param prompt Prompt to display
     * @return Double value or -1 if invalid
     */
    public static double readDouble(String prompt) {
        try {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            return Double.parseDouble(input);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    /**
     * Reads an integer input from the user with validation.
     *
     * @param prompt Prompt to display
     * @return Integer value or -1 if invalid
     */
    public static int readInt(String prompt) {
        try {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    /**
     * Reads a password input from the user (does not echo to console).
     *
     * @param prompt Prompt to display
     * @return Password input
     */
    public static String readPassword(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    /**
     * Displays a menu and gets user's choice.
     *
     * @param options Menu options
     * @return User's choice (0-based index) or -1 if invalid
     */
    public static int readMenuChoice(String... options) {
        for (int i = 0; i < options.length; i++) {
            System.out.println((i + 1) + ". " + options[i]);
        }
        int choice = readInt("Enter your choice: ");
        return (choice >= 1 && choice <= options.length) ? choice - 1 : -1;
    }

    /**
     * Pauses execution and waits for user to press Enter.
     */
    public static void pause() {
        System.out.print("\nPress Enter to continue...");
        scanner.nextLine();
    }

    /**
     * Clears the console screen (works on most terminals).
     */
    public static void clearScreen() {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (Exception e) {
            // If clear fails, just print new lines
            for (int i = 0; i < 50; i++) {
                System.out.println();
            }
        }
    }

    /**
     * Prints a formatted amount with currency symbol.
     *
     * @param amount Amount to format
     * @return Formatted amount string
     */
    public static String formatAmount(double amount) {
        return String.format("$%.2f", amount);
    }

    /**
     * Closes the scanner resource.
     */
    public static void closeScanner() {
        scanner.close();
    }
}
