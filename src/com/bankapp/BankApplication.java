package com.bankapp;

import com.bankapp.controllers.AuthController;
import com.bankapp.controllers.AccountController;
import com.bankapp.controllers.TransactionController;
import com.bankapp.model.Account;
import com.bankapp.model.User;
import com.bankapp.services.BankService;
import com.bankapp.utils.ConsoleUtils;

/**
 * BankApplication - Main entry point for the Banking Application.
 * Implements the main menu loop and orchestrates all user interactions.
 * Demonstrates proper application architecture and user flow management.
 */
public class BankApplication {
    private final BankService bankService;
    private final AuthController authController;
    private final AccountController accountController;
    private final TransactionController transactionController;
    private User currentUser;

    /**
     * Constructor - initializes all controllers and services.
     */
    public BankApplication() {
        this.bankService = new BankService();
        this.authController = new AuthController(bankService.getAuthService());
        this.accountController = new AccountController(
                bankService.getAccountService(),
                bankService.getTransactionService()
        );
        this.transactionController = new TransactionController(bankService.getTransactionService());
        this.currentUser = null;
    }

    /**
     * Starts the application and shows the main menu.
     */
    public void start() {
        ConsoleUtils.printHeader("WELCOME TO BANKAPP");
        ConsoleUtils.printInfo("A Simple Java Banking Application");

        while (true) {
            if (currentUser == null) {
                showAuthMenu();
            } else {
                showMainMenu();
            }
        }
    }

    /**
     * Displays authentication menu (login/register).
     */
    private void showAuthMenu() {
        ConsoleUtils.printSubHeader("MAIN MENU");
        int choice = ConsoleUtils.readMenuChoice(
                "Login",
                "Register",
                "Exit"
        );

        switch (choice) {
            case 0: // Login
                currentUser = authController.handleLogin();
                if (currentUser != null) {
                    ConsoleUtils.pause();
                }
                break;
            case 1: // Register
                currentUser = authController.handleRegistration();
                if (currentUser != null) {
                    ConsoleUtils.pause();
                }
                break;
            case 2: // Exit
                ConsoleUtils.printInfo("Thank you for using BankApp. Goodbye!");
                System.exit(0);
                break;
            default:
                ConsoleUtils.printError("Invalid choice");
                ConsoleUtils.pause();
        }
    }

    /**
     * Displays main menu for logged-in users.
     */
    private void showMainMenu() {
        ConsoleUtils.clearScreen();
        ConsoleUtils.printHeader("MAIN MENU - " + currentUser.getFullName());
        ConsoleUtils.printInfo("Total Balance: " + ConsoleUtils.formatAmount(currentUser.getTotalBalance()));

        int choice = ConsoleUtils.readMenuChoice(
                "View Accounts",
                "Open New Account",
                "Manage Account",
                "Logout"
        );

        switch (choice) {
            case 0: // View Accounts
                accountController.displayUserAccounts(currentUser);
                ConsoleUtils.pause();
                break;
            case 1: // Open New Account
                showOpenAccountMenu();
                break;
            case 2: // Manage Account
                showManageAccountMenu();
                break;
            case 3: // Logout
                currentUser = null;
                ConsoleUtils.printSuccess("Logged out successfully");
                ConsoleUtils.pause();
                break;
            default:
                ConsoleUtils.printError("Invalid choice");
                ConsoleUtils.pause();
        }
    }

    /**
     * Displays menu for opening a new account.
     */
    private void showOpenAccountMenu() {
        int choice = ConsoleUtils.readMenuChoice(
                "Open Checking Account",
                "Open Savings Account",
                "Back to Main Menu"
        );

        switch (choice) {
            case 0: // Checking Account
                accountController.handleOpenCheckingAccount(currentUser);
                ConsoleUtils.pause();
                break;
            case 1: // Savings Account
                accountController.handleOpenSavingsAccount(currentUser);
                ConsoleUtils.pause();
                break;
            case 2: // Back
                break;
            default:
                ConsoleUtils.printError("Invalid choice");
                ConsoleUtils.pause();
        }
    }

    /**
     * Displays menu for managing an account (transactions, history, etc.).
     */
    private void showManageAccountMenu() {
        ConsoleUtils.printSubHeader("SELECT ACCOUNT");
        Account account = accountController.selectAccount(currentUser);
        
        if (account == null) {
            ConsoleUtils.pause();
            return;
        }

        while (true) {
            ConsoleUtils.clearScreen();
            accountController.displayAccountDetails(account);

            int choice = ConsoleUtils.readMenuChoice(
                    "Deposit",
                    "Withdraw",
                    "Transfer",
                    "View Transaction History",
                    "Back to Main Menu"
            );

            switch (choice) {
                case 0: // Deposit
                    transactionController.handleDeposit(account);
                    ConsoleUtils.pause();
                    break;
                case 1: // Withdraw
                    transactionController.handleWithdraw(account);
                    ConsoleUtils.pause();
                    break;
                case 2: // Transfer
                    transactionController.handleTransfer(account, currentUser);
                    ConsoleUtils.pause();
                    break;
                case 3: // View Transaction History
                    transactionController.displayTransactionHistory(account);
                    ConsoleUtils.pause();
                    break;
                case 4: // Back
                    return;
                default:
                    ConsoleUtils.printError("Invalid choice");
                    ConsoleUtils.pause();
            }
        }
    }

    /**
     * Main method - entry point for the application.
     *
     * @param args Command-line arguments (not used)
     */
    public static void main(String[] args) {
        try {
            BankApplication app = new BankApplication();
            app.start();
        } catch (Exception e) {
            ConsoleUtils.printError("An error occurred: " + e.getMessage());
            e.printStackTrace();
        } finally {
            ConsoleUtils.closeScanner();
        }
    }
}
