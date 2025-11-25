# BankApp - Java OOP Banking Application

## 📋 Table of Contents

1. [Project Overview](#project-overview)
2. [Architecture & Design Patterns](#architecture--design-patterns)
3. [OOP Principles](#oop-principles)
4. [Project Structure](#project-structure)
5. [Features](#features)
6. [Installation & Setup](#installation--setup)
7. [How to Run](#how-to-run)
8. [Example Usage](#example-usage)
9. [UML Diagrams](#uml-diagrams)
10. [Design Decisions](#design-decisions)
11. [Extensibility Guide](#extensibility-guide)
12. [Error Handling](#error-handling)

---

## Project Overview

**BankApp** is a comprehensive Java console-based banking application demonstrating professional OOP design, clean architecture, and best practices. The system supports user registration, account management, transactions, and a complete transaction history.

### Key Features
- ✅ User registration and secure authentication
- ✅ Multiple account types (Checking & Savings)
- ✅ Money deposits and withdrawals
- ✅ Inter-account transfers
- ✅ Complete transaction history tracking
- ✅ Interest calculation for savings accounts
- ✅ Account restrictions and validations
- ✅ In-memory data persistence

---

## Architecture & Design Patterns

### Clean Architecture Layers

```
┌─────────────────────────────────────┐
│    Presentation (Console UI)        │  ← BankApplication + Controllers
├─────────────────────────────────────┤
│      Application/Controllers        │  ← AuthController, AccountController, TransactionController
├─────────────────────────────────────┤
│    Business Logic (Services)        │  ← BankService, AuthService, AccountService, TransactionService
├─────────────────────────────────────┤
│      Data Access (Repositories)     │  ← InMemoryDataStore with Repositories
└─────────────────────────────────────┘
```

### Design Patterns Used

1. **Singleton Pattern**: `InMemoryDataStore` - Ensures single instance of data storage
2. **Repository Pattern**: `UserRepository`, `AccountRepository`, `TransactionRepository` - Abstracts data access
3. **Facade Pattern**: `BankService` - Simplifies access to multiple services
4. **MVC Pattern**: Controllers manage user interaction, Models represent data, Services handle business logic
5. **Template Method Pattern**: `Account` abstract class defines structure for all account types
6. **Strategy Pattern**: `CheckingAccount` and `SavingsAccount` implement different withdrawal strategies

---

## OOP Principles

### 1. **Encapsulation**
- Private fields with public getters/setters
- `Account` class encapsulates balance management
- `User` class manages account relationships internally

```java
// Example: Encapsulation in Account class
public abstract class Account {
    private double balance;        // Private
    
    public double getBalance() {   // Controlled access
        return balance;
    }
    
    public boolean deposit(double amount, String description) {
        // Internal validation and state management
        if (amount <= 0) return false;
        this.balance += amount;
        return true;
    }
}
```

### 2. **Inheritance**
- `CheckingAccount` and `SavingsAccount` inherit from abstract `Account` class
- Reuses common functionality while allowing customization

```java
public class CheckingAccount extends Account {
    // Inherits all Account methods and fields
    // Overrides: getAccountType(), canWithdraw(), applyAccountSpecificRules()
}

public class SavingsAccount extends Account {
    // Inherits all Account methods and fields
    // Adds: interest rate management, withdrawal limits
}
```

### 3. **Polymorphism**
- Abstract methods in `Account` are overridden differently in each subclass
- Runtime type determines behavior

```java
Account account = user.getAccounts().get(0);
// At runtime, could be CheckingAccount or SavingsAccount

String type = account.getAccountType();  // Returns "CHECKING" or "SAVINGS"
boolean canWithdraw = account.canWithdraw(100);  // Different logic per type
```

### 4. **Abstraction**
- Abstract `Account` class defines contract without implementation
- Repositories abstract data access details
- Services hide business logic complexity

```java
public abstract class Account {
    public abstract String getAccountType();
    public abstract boolean canWithdraw(double amount);
    public abstract void applyAccountSpecificRules();
}
```

---

## Project Structure

```
BankApp/
├── src/com/bankapp/
│   ├── BankApplication.java                 (Main entry point)
│   ├── model/
│   │   ├── User.java                        (User entity)
│   │   ├── Account.java                     (Abstract account)
│   │   ├── CheckingAccount.java             (Checking account implementation)
│   │   ├── SavingsAccount.java              (Savings account implementation)
│   │   └── Transaction.java                 (Transaction entity)
│   ├── services/
│   │   ├── BankService.java                 (Facade service)
│   │   ├── AuthService.java                 (Authentication logic)
│   │   ├── AccountService.java              (Account management)
│   │   └── TransactionService.java          (Transaction operations)
│   ├── controllers/
│   │   ├── AuthController.java              (Auth UI handling)
│   │   ├── AccountController.java           (Account UI handling)
│   │   └── TransactionController.java       (Transaction UI handling)
│   ├── data/
│   │   ├── InMemoryDataStore.java           (Data storage singleton)
│   │   ├── UserRepository.java              (User data access)
│   │   ├── AccountRepository.java           (Account data access)
│   │   └── TransactionRepository.java       (Transaction data access)
│   └── utils/
│       ├── PasswordHasher.java              (Password hashing)
│       ├── InputValidator.java              (Input validation)
│       ├── IDGenerator.java                 (ID generation)
│       └── ConsoleUtils.java                (Console UI utilities)
└── docs/
    └── uml/
        ├── 01_UseCase_Diagram.puml
        ├── 02_Class_Diagram.puml
        ├── 03_Sequence_Login.puml
        ├── 04_Sequence_Deposit.puml
        ├── 05_Sequence_Transfer.puml
        └── 06_Sequence_OpenSavingsAccount.puml
```

---

## Features

### 1. User Authentication
- **Registration**: Create new account with username, password, name, and email
- **Login**: Secure authentication with password hashing
- **Validation**: Email, username, and password format validation

### 2. Account Management
- **Checking Accounts**: Standard accounts with optional overdraft protection
- **Savings Accounts**: Interest-bearing accounts with withdrawal limits (max 6/month)
- **Multiple Accounts**: Users can have multiple accounts of each type
- **Account Operations**: View balance, account type, transaction count

### 3. Transactions
- **Deposits**: Add money to accounts
- **Withdrawals**: Remove money with account-specific restrictions
- **Transfers**: Move money between accounts
- **Transaction History**: Complete audit trail with timestamps

### 4. Savings Account Features
- **Interest Calculation**: Monthly interest application (annual rate)
- **Withdrawal Limits**: Federal regulation-compliant 6 withdrawal limit
- **Withdrawal Penalties**: Penalty fee for exceeding limits
- **Minimum Balance**: $100 minimum balance requirement

### 5. Checking Account Features
- **Overdraft Protection**: Optional overdraft limit
- **Unlimited Transactions**: No monthly limits
- **Fee Calculation**: Can apply overdraft fees (extensible)

---

## Installation & Setup

### Prerequisites
- Java 8 or higher
- Windows/Linux/Mac command line

### Step 1: Create Project Structure
```bash
# Navigate to your workspace
cd d:\Code\OOP\Project

# The folder structure is already created
```

### Step 2: Compile the Project
```bash
# Windows
cd d:\Code\OOP\Project\BankApp
javac -d bin src\com\bankapp\*.java src\com\bankapp\model\*.java src\com\bankapp\services\*.java src\com\bankapp\controllers\*.java src\com\bankapp\data\*.java src\com\bankapp\utils\*.java

# Linux/Mac
javac -d bin src/com/bankapp/*.java src/com/bankapp/model/*.java src/com/bankapp/services/*.java src/com/bankapp/controllers/*.java src/com/bankapp/data/*.java src/com/bankapp/utils/*.java
```

### Step 3: Run the Application
```bash
# Windows
java -cp bin com.bankapp.BankApplication

# Linux/Mac
java -cp bin com.bankapp.BankApplication
```

---

## How to Run

### Complete Compilation Script

```bash
@echo off
REM BankApp Compilation Script

cd d:\Code\OOP\Project\BankApp

REM Create bin directory if it doesn't exist
if not exist bin mkdir bin

REM Compile all Java files
echo Compiling BankApp...
javac -d bin -sourcepath src src\com\bankapp\*.java src\com\bankapp\model\*.java src\com\bankapp\services\*.java src\com\bankapp\controllers\*.java src\com\bankapp\data\*.java src\com\bankapp\utils\*.java

if %ERRORLEVEL% EQU 0 (
    echo Compilation successful!
    echo Running BankApp...
    java -cp bin com.bankapp.BankApplication
) else (
    echo Compilation failed!
    pause
)
```

---

## Example Usage

### Scenario: User Registration and Banking

```
========================================
   WELCOME TO BANKAPP
========================================
ℹ A Simple Java Banking Application

============================================================
  MAIN MENU
============================================================
1. Login
2. Register
3. Exit
Enter your choice: 2

============================================================
  USER REGISTRATION
============================================================
Enter username (4-20 chars, alphanumeric): john_doe
Enter password (min 6 chars): MySecurePass123
Enter full name: John Doe
Enter email: john@example.com
✓ Registration successful!
ℹ Your User ID: USER_ABC12345

============================================================
  MAIN MENU
============================================================
1. Login
2. Register
3. Exit
Enter your choice: 1

============================================================
  USER LOGIN
============================================================
Enter username: john_doe
Enter password: MySecurePass123
✓ Login successful! Welcome, John Doe

============================================================
  MAIN MENU - John Doe
============================================================
ℹ Total Balance: $0.00

1. View Accounts
2. Open New Account
3. Manage Account
4. Logout
Enter your choice: 2

1. Open Checking Account
2. Open Savings Account
3. Back to Main Menu
Enter your choice: 1

============================================================
  OPEN CHECKING ACCOUNT
============================================================
Enter initial deposit amount: $1000.00
Enter overdraft limit (optional, press 0 for none): $500.00
✓ Checking account created successfully!
ℹ Account Number: ACC1234567890
ℹ Initial Balance: $1000.00

Press Enter to continue...

============================================================
  MAIN MENU - John Doe
============================================================
ℹ Total Balance: $1000.00

1. View Accounts
2. Open New Account
3. Manage Account
4. Logout
Enter your choice: 2

1. Open Checking Account
2. Open Savings Account
3. Back to Main Menu
Enter your choice: 2

============================================================
  OPEN SAVINGS ACCOUNT
============================================================
Enter initial deposit amount: $5000.00
Enter annual interest rate (e.g., 0.025 for 2.5%): 0.030
✓ Savings account created successfully!
ℹ Account Number: ACC9876543210
ℹ Initial Balance: $5000.00
ℹ Interest Rate: 3.00%

============================================================
  SELECT ACCOUNT
============================================================
1. CHECKING Account: ACC1234567890 | Balance: $1000.00
2. SAVINGS Account: ACC9876543210 | Balance: $5000.00
Select account (enter number): 1

============================================================
  ACCOUNT DETAILS
============================================================
Account Number: ACC1234567890
Account Type: CHECKING
Balance: $1000.00
Status: Active
Total Transactions: 1

1. Deposit
2. Withdraw
3. Transfer
4. View Transaction History
5. Back to Main Menu
Enter your choice: 1

============================================================
  DEPOSIT MONEY
============================================================
ℹ Account: ACC1234567890 (CHECKING)
Enter deposit amount: $500.00
Enter description (optional): Salary Deposit
✓ Deposit successful!
ℹ New Balance: $1500.00

============================================================
  DEPOSIT MONEY
============================================================
Enter another deposit? (Y/N): n

1. Deposit
2. Withdraw
3. Transfer
4. View Transaction History
5. Back to Main Menu
Enter your choice: 3

============================================================
  TRANSFER MONEY
============================================================
ℹ Account: ACC1234567890 (CHECKING)
ℹ Current Balance: $1500.00
Enter destination account number: ACC9876543210
Enter transfer amount: $200.00
Enter transfer reason (optional): Savings transfer
✓ Transfer successful!
ℹ Recipient Account: ACC9876543210
ℹ Amount Transferred: $200.00
ℹ New Balance: $1300.00

1. Deposit
2. Withdraw
3. Transfer
4. View Transaction History
5. Back to Main Menu
Enter your choice: 4

============================================================
  TRANSACTION HISTORY
============================================================
ℹ Account: ACC1234567890

[2025-11-25 10:15:30] DEPOSIT: $1000.00 - Initial Deposit (SUCCESS)
[2025-11-25 10:16:45] DEPOSIT: $500.00 - Salary Deposit (SUCCESS)
[2025-11-25 10:18:20] TRANSFER_OUT: $200.00 - Transfer to ACC9876543210 (SUCCESS)
```

---

## UML Diagrams

The project includes comprehensive UML diagrams:

### 1. **Use Case Diagram** (`01_UseCase_Diagram.puml`)
Shows all main features and user interactions:
- Register, Login
- Open Checking/Savings Accounts
- Deposit, Withdraw, Transfer
- View Transaction History & Account Details
- Admin functions

### 2. **Class Diagram** (`02_Class_Diagram.puml`)
Shows complete class structure with:
- Abstract `Account` class with concrete implementations
- `User` entity with account management
- All service classes and their relationships
- Data access layer repositories
- Utility classes

### 3. **Sequence Diagrams**
- **Login Flow** (`03_Sequence_Login.puml`): User authentication process
- **Deposit Flow** (`04_Sequence_Deposit.puml`): Money deposit process
- **Transfer Flow** (`05_Sequence_Transfer.puml`): Inter-account transfer
- **Open Savings Account** (`06_Sequence_OpenSavingsAccount.puml`): Account creation

---

## Design Decisions

### 1. **Abstract Account Class**
**Decision**: Use abstract class instead of interface
**Rationale**: Accounts share significant common behavior (balance management, transactions). Abstract class allows shared implementation while enforcing contract.

### 2. **Separate Service & Controller Layers**
**Decision**: Separate controllers from services
**Rationale**: Controllers handle UI validation/interaction, services handle business logic. Improves testability and reusability.

### 3. **Singleton Data Store**
**Decision**: InMemoryDataStore as Singleton
**Rationale**: Single source of truth for all data. Prevents multiple data copies. Thread-safe for future enhancements.

### 4. **Transaction Objects**
**Decision**: Create immutable transaction records
**Rationale**: Audit trail requires unchangeable transaction history. Prevents accidental data modification.

### 5. **Password Hashing**
**Decision**: Use SHA-256 with Base64 encoding
**Rationale**: Simple demonstration of security concepts. (Production should use bcrypt/Argon2)

---

## Extensibility Guide

### 1. **Add Database Persistence**

Replace `InMemoryDataStore` with database-backed repositories:

```java
// Create DatabaseUserRepository
public class DatabaseUserRepository implements IUserRepository {
    private Connection connection;
    
    @Override
    public boolean save(User user) {
        String sql = "INSERT INTO users (id, username, password_hash...) VALUES (?, ?, ...)";
        // Execute SQL
    }
}

// Update InMemoryDataStore to use database repositories
```

### 2. **Add Interest Calculation Scheduler**

Create a scheduled task to apply interest:

```java
// Create InterestScheduler
public class InterestScheduler {
    public void scheduleMonthlyInterest() {
        // Query all SavingsAccount
        // Apply monthly interest
        // Update database
    }
}
```

### 3. **Add Admin Panel**

Create admin functionality:

```java
// Create AdminService
public class AdminService {
    public void viewAllUsers() { }
    public void viewSystemStats() { }
    public void applyMonthlyInterest() { }
    public void generateReport() { }
}
```

### 4. **Add Fee Management**

Implement various fees:

```java
// Create FeeService
public class FeeService {
    public void applyOverdraftFee(CheckingAccount account) { }
    public void applyMonthlyMaintenanceFee(Account account) { }
    public void applyExcessWithdrawalFee(SavingsAccount account) { }
}
```

### 5. **Add Account Notifications**

Implement notifications for transactions:

```java
// Create NotificationService
public interface NotificationService {
    void notifyDeposit(User user, Account account, double amount);
    void notifyWithdrawal(User user, Account account, double amount);
    void notifyTransfer(User user, double amount, String recipient);
}
```

### 6. **Add Export/Reporting**

Generate transaction reports:

```java
// Create ReportService
public class ReportService {
    public String generateMonthlyStatement(Account account) { }
    public String generateTaxReport(User user) { }
    public String exportTransactionCSV(Account account) { }
}
```

### 7. **Add Multiple Currencies**

Support different currencies:

```java
// Create CurrencyConverter
public class CurrencyConverter {
    public double convert(double amount, String from, String to) { }
}

// Modify Account to store currency
```

### 8. **Add Loan Module**

Implement loan functionality:

```java
// Create Loan entity
public class Loan {
    private String loanId;
    private User borrower;
    private double amount;
    private double interestRate;
    private LocalDate disbursementDate;
    // Loan methods
}

// Create LoanService
public class LoanService {
    public Loan createLoan(User user, double amount, double rate) { }
    public boolean payLoanInstallment(Loan loan, double amount) { }
}
```

### 9. **Add Authentication Enhancement**

Implement stronger authentication:

```java
// Create 2FA Service
public class TwoFactorAuthService {
    public void sendOTP(User user) { }
    public boolean verifyOTP(User user, String otp) { }
}

// Create Token-based auth
public class TokenService {
    public String generateToken(User user) { }
    public boolean validateToken(String token) { }
}
```

### 10. **Add Email Notifications**

Send email alerts:

```java
// Create EmailService
public class EmailService {
    public void sendTransactionAlert(User user, Transaction transaction) { }
    public void sendMonthlyStatement(User user, Account account) { }
    public void sendPasswordResetLink(User user, String token) { }
}
```

---

## Error Handling

### Validation Layers

The application implements validation at multiple levels:

1. **Input Validation** (`InputValidator.java`)
   - Username: 4-20 chars, alphanumeric & underscores
   - Password: Minimum 6 characters
   - Email: Valid email format
   - Amount: Positive, max 999,999,999.99

2. **Business Logic Validation**
   - Sufficient balance checks
   - Account status verification
   - Duplicate prevention
   - Account restriction enforcement

3. **Data Access Validation**
   - Null checks
   - Entity existence verification
   - Relationship integrity

### Exception Handling

- Try-catch blocks in critical sections
- Graceful error messages to users
- Logging for debugging

### Specific Error Scenarios

| Scenario | Handling |
|----------|----------|
| Invalid username format | Return null, display error |
| Username already exists | Return null, prompt to try different username |
| Incorrect password | Return null, allow retry |
| Insufficient balance | Return false, display current balance |
| Savings account overdraft | Block withdrawal, explain 6/month limit |
| Transfer to non-existent account | Return false, prompt for valid account |
| Invalid amount input | Return -1 or -infinity, re-prompt |

---

## Testing Recommendations

### Unit Tests

```java
// Test password hashing
@Test
public void testPasswordHashing() {
    String hash = PasswordHasher.hashPassword("test123");
    assertTrue(PasswordHasher.verifyPassword("test123", hash));
    assertFalse(PasswordHasher.verifyPassword("wrong", hash));
}

// Test account deposit
@Test
public void testDeposit() {
    CheckingAccount account = new CheckingAccount("ACC001", 100.0);
    assertTrue(account.deposit(50.0, "Test deposit"));
    assertEquals(150.0, account.getBalance());
}

// Test withdrawal restrictions
@Test
public void testSavingsWithdrawalLimit() {
    SavingsAccount account = new SavingsAccount("ACC001", 500.0);
    for (int i = 0; i < 6; i++) {
        assertTrue(account.withdraw(10.0, "Withdrawal " + i));
    }
    assertFalse(account.canWithdraw(10.0)); // 7th withdrawal blocked
}
```

### Integration Tests

- User registration → Login flow
- Create account → Deposit → Withdrawal flow
- Transfer between accounts → Verify both sides
- Complete transaction history tracking

### System Tests

- Full user journey from registration to multiple transactions
- Error recovery and validation
- Data persistence across sessions

---

## Future Enhancements

1. **Database Integration**: Replace in-memory storage
2. **Web UI**: REST API + Angular/React frontend
3. **Mobile App**: Native mobile applications
4. **Advanced Features**: Loans, investments, bill payments
5. **Security**: OAuth2, JWT, encryption
6. **Performance**: Caching, indexing, optimization
7. **Analytics**: Spending patterns, recommendations
8. **Blockchain**: Distributed ledger integration

---

## Conclusion

BankApp demonstrates professional Java development with:
- ✅ Clean Architecture
- ✅ SOLID Principles
- ✅ Design Patterns
- ✅ OOP Best Practices
- ✅ Comprehensive Error Handling
- ✅ Extensible Design

The project serves as an excellent foundation for understanding enterprise application architecture!

---

**Author**: Senior Java Developer  
**Version**: 1.0  
**License**: MIT  
**Last Updated**: November 25, 2025
