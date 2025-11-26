package com.bankapp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import com.bankapp.controllers.AccountController;
import com.bankapp.controllers.AuthController;
import com.bankapp.controllers.AdminController;
import com.bankapp.controllers.TransactionController;
import com.bankapp.model.Account;
import com.bankapp.model.Admin;
import com.bankapp.model.User;
import com.bankapp.service.Bank;
import com.bankapp.services.AccountService;
import com.bankapp.services.AuthService;
import com.bankapp.services.TransactionService;
import java.util.List;
import com.bankapp.utils.ConsoleUtils;

public class BankApplication {

    // --- Central Data Store ---
    private static Bank bank = new Bank();

    // --- Services (Business Logic Layer) ---
    private static final AuthService authService = new AuthService(bank);
    private static final AccountService accountService = new AccountService(bank);
    private static final TransactionService transactionService = new TransactionService(bank);

    // --- Controllers (User Interaction Layer) ---
    private static final AuthController authController = new AuthController(authService);
    private static final AccountController accountController = new AccountController(accountService, transactionService);
    private static final TransactionController transactionController = new TransactionController(transactionService);
    private static final AdminController adminController = new AdminController(bank);

    // --- Application State ---
    private static User currentUser = null;
    private static Admin currentAdmin = null;
    private static final String DATA_FILE = "bank_data.dat";

    public static void main(String[] args) {
        // Tải dữ liệu từ tệp khi khởi động
        loadData();

        // Đăng ký một "shutdown hook" để tự động lưu dữ liệu khi ứng dụng đóng.
        // Điều này đảm bảo dữ liệu được lưu ngay cả khi người dùng đóng cửa sổ console (Ctrl+C).
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            ConsoleUtils.printInfo("\nApplication is shutting down. Saving data...");
            saveData();
            ConsoleUtils.printSuccess("Data saved successfully.");
        }));


        ConsoleUtils.printHeader("WELCOME TO THE BANKING APPLICATION");

        boolean running = true;
        while (running) {
            if (currentUser != null) {
                showMainMenu(); // Nếu người dùng đã đăng nhập, hiển thị menu chính của người dùng
            } else if (currentAdmin != null) {
                showAdminMenu(); // Nếu admin đã đăng nhập, hiển thị menu của admin
            } else {
                showTopLevelMenu(); // Nếu không ai đăng nhập, hiển thị menu lựa chọn portal
            }
        }

        // Dòng này có thể sẽ không bao giờ được gọi vì vòng lặp là vô hạn
        // và việc thoát được xử lý bằng System.exit(0) hoặc shutdown hook.
        ConsoleUtils.closeScanner();
        ConsoleUtils.printInfo("Thank you for using the Banking Application!");
    }

    /**
     * Lưu trạng thái hiện tại của ứng dụng (danh sách người dùng) vào một tệp.
     */
    private static void saveData() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DATA_FILE))) {
            oos.writeObject(bank);
        } catch (IOException e) {
            ConsoleUtils.printError("A critical error occurred while saving data: " + e.getMessage());
        }
    }

    /**
     * Tải trạng thái ứng dụng từ tệp.
     */
    @SuppressWarnings("unchecked")
    private static void loadData() {
        File file = new File(DATA_FILE);
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(DATA_FILE))) {
                Object obj = ois.readObject();
                if (obj instanceof Bank) {
                    bank = (Bank) obj;
                    // Re-initialize services with the loaded bank data
                    authService.setBank(bank);
                    transactionService.setBank(bank);
                    adminController.setBank(bank);
                    ConsoleUtils.printSuccess("Successfully loaded application data from " + DATA_FILE);
                }
            } catch (IOException | ClassNotFoundException e) {
                ConsoleUtils.printError("Could not load data from file: " + e.getMessage());
            }
        } else {
            ConsoleUtils.printInfo("Data file not found. Starting a new session.");
        }
    }

    private static void showTopLevelMenu() {
        ConsoleUtils.printHeader("WELCOME TO THE BANKING APPLICATION");
        int choice = ConsoleUtils.readMenuChoice("User Portal", "Admin Portal", "Exit");

        switch (choice) {
            case 0: // User Portal
                handleUserPortal();
                break;
            case 1: // Admin Portal
                handleAdminPortal();
                break;
            case 2: // Exit
                System.exit(0);
                break;
            default:
                ConsoleUtils.printError("Invalid choice. Please try again.");
                break;
        }
    }

    private static void handleUserPortal() {
        if (currentUser == null) {
            showAuthMenu();
        } else {
            showMainMenu();
        }
    }

    private static void showAuthMenu() {
        ConsoleUtils.printSubHeader("AUTHENTICATION MENU");
        int choice = ConsoleUtils.readMenuChoice("Login", "Register", "Exit");

        switch (choice) {
            case 0: // Login
                currentUser = authController.handleLogin();
                if (currentUser != null) {
                    ConsoleUtils.printSuccess("Login successful! Welcome, " + currentUser.getFullName());
                }
                break;
            case 1: // Register
                currentUser = authController.handleRegistration();
                if (currentUser != null) {
                    ConsoleUtils.printSuccess("Auto-login successful! Welcome, " + currentUser.getFullName());
                }
                break;
            case 2: // Exit to top-level menu
                System.exit(0);
                break;
            default:
                ConsoleUtils.printError("Invalid choice. Please try again.");
                break;
        }
    }

    private static void showMainMenu() {
        ConsoleUtils.printHeader("MAIN MENU | Logged in as: " + currentUser.getUsername());
        int choice = ConsoleUtils.readMenuChoice(
            "View My Accounts",
            "Open New Account",
            "Perform a Transaction",
            "View Transaction History",
            "Logout"
        );

        switch (choice) {
            case 0: // View My Accounts
                accountController.displayUserAccounts(currentUser);
                ConsoleUtils.pause();
                break;
            case 1: // Open New Account
                showOpenAccountMenu();
                break;
            case 2: // Perform a Transaction
                showTransactionMenu();
                break;
            case 3: // View Transaction History
                Account accForHistory = accountController.selectAccount(currentUser);
                if (accForHistory != null) {
                    transactionController.displayTransactionHistory(accForHistory);
                }
                ConsoleUtils.pause();
                break;
            case 4: // Logout
                currentUser = null;
                ConsoleUtils.printInfo("You have been logged out.");
                break;
            default:
                ConsoleUtils.printError("Invalid choice. Please try again.");
                break;
        }
    }

    private static void showOpenAccountMenu() {
        ConsoleUtils.printSubHeader("OPEN NEW ACCOUNT");
        int choice = ConsoleUtils.readMenuChoice("Open Checking Account", "Open Savings Account", "Back to Main Menu");

        switch (choice) {
            case 0:
                accountController.handleOpenCheckingAccount(currentUser);
                break;
            case 1:
                accountController.handleOpenSavingsAccount(currentUser);
                break;
            case 2:
                return; // Go back
            default:
                ConsoleUtils.printError("Invalid choice.");
                break;
        }
        ConsoleUtils.pause();
    }

    private static void showTransactionMenu() {
        ConsoleUtils.printSubHeader("PERFORM A TRANSACTION");
        Account selectedAccount = accountController.selectAccount(currentUser);
        if (selectedAccount == null) {
            ConsoleUtils.pause();
            return;
        }

        int choice = ConsoleUtils.readMenuChoice("Deposit", "Withdraw", "Transfer", "Back to Main Menu");

        switch (choice) {
            case 0: // Deposit
                transactionController.handleDeposit(selectedAccount);
                break;
            case 1: // Withdraw
                transactionController.handleWithdraw(selectedAccount);
                break;
            case 2: // Transfer
                transactionController.handleTransfer(selectedAccount, currentUser);
                break;
            case 3: // Back
                return;
            default:
                ConsoleUtils.printError("Invalid choice.");
                break;
        }
        ConsoleUtils.pause();
    }

    private static void handleAdminPortal() {
        if (currentAdmin == null) {
            showAdminAuthMenu();
        } else {
            showAdminMenu();
        }
    }

    private static void showAdminAuthMenu() {
        ConsoleUtils.printSubHeader("ADMIN PORTAL");
        int choice = ConsoleUtils.readMenuChoice("Admin Login", "Register Admin", "Back");

        switch (choice) {
            case 0: // Admin Login
                currentAdmin = adminController.handleAdminLogin();
                break;
            case 1: // Register Admin
                adminController.handleAdminRegistration();
                break;
            case 2: // Back
                return;
            default:
                ConsoleUtils.printError("Invalid choice.");
                break;
        }
    }

    private static void showAdminMenu() {
        ConsoleUtils.printHeader("ADMIN MENU | Logged in as: " + currentAdmin.getUsername());
        int choice = ConsoleUtils.readMenuChoice(
            "View All Users", "View All Accounts", "Reset System Data", "Logout"
        );

        if (choice == 0) adminController.handleViewAllUsers();
        else if (choice == 1) adminController.handleViewAllAccounts();
        else if (choice == 2) adminController.handleResetSystem();
        else if (choice == 3) currentAdmin = null;
        else ConsoleUtils.printError("Invalid choice.");
        ConsoleUtils.pause();
    }
}