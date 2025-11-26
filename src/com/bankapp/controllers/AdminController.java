package com.bankapp.controllers;

import com.bankapp.model.Admin;
import com.bankapp.service.Bank;
import com.bankapp.utils.ConsoleUtils;
import com.bankapp.utils.IDGenerator;
import com.bankapp.utils.InputValidator;
import com.bankapp.utils.PasswordHasher;
import java.io.File;

public class AdminController {
    private Bank bank;

    public AdminController(Bank bank) {
        this.bank = bank;
    }

    public void setBank(Bank bank) {
        this.bank = bank;
    }

    public void handleAdminRegistration() {
        ConsoleUtils.printSubHeader("ADMIN REGISTRATION");
        String username = ConsoleUtils.readString("Enter username: ");
        String password = ConsoleUtils.readPassword("Enter password: ");
        String fullName = ConsoleUtils.readString("Enter full name: ");
        String email = ConsoleUtils.readString("Enter email: ");

        if (!InputValidator.isValidEmail(email)) {
            ConsoleUtils.printError("Invalid email format.");
            return;
        }

        String passwordHash = PasswordHasher.hashPassword(password);
        Admin newAdmin = new Admin(IDGenerator.generateUserId(), username, passwordHash, fullName, email);

        if (bank.registerAdmin(newAdmin)) {
            ConsoleUtils.printSuccess("Admin registered successfully!");
        } else {
            ConsoleUtils.printError("Admin registration failed. An admin may already exist.");
        }
    }

    public Admin handleAdminLogin() {
        ConsoleUtils.printSubHeader("ADMIN LOGIN");
        String username = ConsoleUtils.readString("Enter username: ");
        String password = ConsoleUtils.readPassword("Enter password: ");
        String passwordHash = PasswordHasher.hashPassword(password);

        Admin admin = bank.loginAdmin(username, passwordHash);
        if (admin != null) {
            ConsoleUtils.printSuccess("Admin login successful!");
        } else {
            ConsoleUtils.printError("Invalid username or password.");
        }
        return admin;
    }

    public void handleViewAllUsers() {
        bank.getAllUsers().forEach(System.out::println);
    }

    public void handleViewAllAccounts() {
        bank.getAllAccounts().forEach(System.out::println);
    }

    public void handleResetSystem() {
        ConsoleUtils.printWarning("!!! WARNING !!!");
        ConsoleUtils.printWarning("This action will delete ALL users, accounts, and admin data.");
        ConsoleUtils.printWarning("This is irreversible.");
        String confirmation = ConsoleUtils.readString("Type 'CONFIRM' to proceed: ");

        if ("CONFIRM".equals(confirmation)) {
            // 1. Reset the in-memory bank object
            bank.reset();

            // 2. Delete the data file
            File dataFile = new File("bank_data.dat");
            if (dataFile.exists()) {
                dataFile.delete();
            }

            ConsoleUtils.printSuccess("System data has been reset. The application will now exit.");
            System.exit(0);
        } else {
            ConsoleUtils.printInfo("System reset cancelled.");
        }
    }
}