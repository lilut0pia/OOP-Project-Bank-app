# BankApp - Code Walkthrough & Examples

## Table of Contents
1. [Application Flow](#application-flow)
2. [Key Code Examples](#key-code-examples)
3. [Design Pattern Examples](#design-pattern-examples)
4. [Error Handling Examples](#error-handling-examples)
5. [Extension Examples](#extension-examples)

---

## Application Flow

### User Registration Flow

```java
// User starts application
public static void main(String[] args) {
    BankApplication app = new BankApplication();
    app.start();  // Enters main menu loop
}

// User selects Register
User newUser = authController.handleRegistration();
// ↓ Inside AuthController
String username = ConsoleUtils.readString("Enter username: ");

// Validate username
if (!InputValidator.isValidUsername(username)) {
    ConsoleUtils.printError("Invalid username format");
    return null;
}

// Register with AuthService
User user = authService.register(username, password, fullName, email);
// ↓ Inside AuthService
String userId = IDGenerator.generateUserId();  // Generate unique ID
String passwordHash = PasswordHasher.hashPassword(password);  // Hash password
User newUser = new User(userId, username, passwordHash, fullName, email);
userRepository.save(newUser);  // Save to repository
// ↓ Inside UserRepository
users.put(userId, newUser);  // Store in HashMap
return true;
```

### Login Flow

```java
// User enters credentials
User loggedInUser = authController.handleLogin();
// ↓ Inside AuthController
String username = ConsoleUtils.readString("Enter username: ");
String password = ConsoleUtils.readString("Enter password: ");

// Authenticate
User user = authService.login(username, password);
// ↓ Inside AuthService
User foundUser = userRepository.findByUsername(username);
if (foundUser != null && PasswordHasher.verifyPassword(password, foundUser.getPasswordHash())) {
    return foundUser;  // Login successful
}
return null;  // Login failed
```

### Account Opening Flow

```java
// User creates checking account
CheckingAccount account = accountController.handleOpenCheckingAccount(user);
// ↓ Inside AccountController
double initialBalance = ConsoleUtils.readDouble("Enter initial deposit: ");
double overdraftLimit = ConsoleUtils.readDouble("Enter overdraft limit: ");

// Create account
CheckingAccount account = accountService.createCheckingAccount(user, initialBalance, overdraftLimit);
// ↓ Inside AccountService
String accountNumber = IDGenerator.generateAccountNumber();  // ACC1234567890
CheckingAccount account = new CheckingAccount(accountNumber, initialBalance, overdraftLimit);
accountRepository.save(account);  // Save to repository
user.addAccount(account);  // Add to user's accounts
return account;
```

### Transaction Flow (Deposit)

```java
// User deposits money
boolean success = transactionController.handleDeposit(account);
// ↓ Inside TransactionController
double amount = ConsoleUtils.readDouble("Enter deposit amount: ");
if (!InputValidator.isValidAmount(amount)) {
    return false;
}

// Process deposit
success = transactionService.deposit(accountNumber, amount, description);
// ↓ Inside TransactionService
Account account = accountRepository.findByAccountNumber(accountNumber);
if (account.deposit(amount, description)) {  // Account deposits internally
    accountRepository.update(account);  // Save changes
    return true;
}
// ↓ Inside Account.deposit()
if (amount <= 0) {
    return false;  // Validation
}
this.balance += amount;  // Update balance
Transaction transaction = new Transaction(...);  // Record transaction
this.transactions.add(transaction);  // Add to history
return true;
```

---

## Key Code Examples

### Example 1: Abstract Class with Polymorphism

```java
// ABSTRACT BASE CLASS
public abstract class Account {
    protected String accountNumber;
    protected double balance;
    protected List<Transaction> transactions;
    
    // Common method - same for all accounts
    public boolean deposit(double amount, String description) {
        if (amount <= 0) return false;
        this.balance += amount;
        Transaction txn = new Transaction(...);
        this.transactions.add(txn);
        return true;
    }
    
    // Abstract method - different for each account type
    public abstract boolean canWithdraw(double amount);
    public abstract String getAccountType();
    public abstract void applyAccountSpecificRules();
}

// CHECKING ACCOUNT IMPLEMENTATION
public class CheckingAccount extends Account {
    private double overdraftLimit;
    
    @Override
    public String getAccountType() {
        return "CHECKING";
    }
    
    @Override
    public boolean canWithdraw(double amount) {
        // Checking: allow overdraft
        return (this.balance + this.overdraftLimit) >= amount;
    }
    
    @Override
    public void applyAccountSpecificRules() {
        monthlyWithdrawals++;
        // Could apply overdraft fee here
    }
}

// SAVINGS ACCOUNT IMPLEMENTATION
public class SavingsAccount extends Account {
    private int withdrawalsThisMonth;
    private static final int MAX_MONTHLY_WITHDRAWALS = 6;
    private static final double MINIMUM_BALANCE = 100.0;
    
    @Override
    public String getAccountType() {
        return "SAVINGS";
    }
    
    @Override
    public boolean canWithdraw(double amount) {
        // Savings: check monthly limit and minimum balance
        if (withdrawalsThisMonth >= MAX_MONTHLY_WITHDRAWALS) {
            return false;
        }
        if ((this.balance - amount) < MINIMUM_BALANCE) {
            return false;
        }
        return true;
    }
    
    @Override
    public void applyAccountSpecificRules() {
        withdrawalsThisMonth++;
        if (withdrawalsThisMonth > MAX_MONTHLY_WITHDRAWALS) {
            this.balance -= withdrawalPenalty;  // Apply penalty
        }
    }
}

// USAGE - POLYMORPHISM IN ACTION
public void processWithdrawal(Account account, double amount) {
    if (account.canWithdraw(amount)) {  // Polymorphic call
        account.withdraw(amount, "Withdrawal");
        // For CheckingAccount: checks overdraftLimit
        // For SavingsAccount: checks withdrawalsThisMonth and minimumBalance
    }
}

// Client code
List<Account> accounts = new ArrayList<>();
accounts.add(new CheckingAccount("ACC001", 1000.0, 500.0));
accounts.add(new SavingsAccount("ACC002", 5000.0, 0.03));

for (Account account : accounts) {
    String type = account.getAccountType();  // "CHECKING" or "SAVINGS"
    boolean canWithdraw = account.canWithdraw(100);  // Different logic per type
    System.out.println(type + " can withdraw: " + canWithdraw);
}
```

### Example 2: Encapsulation

```java
public class User {
    private String userId;              // Private - hidden
    private String username;            // Private - hidden
    private String passwordHash;        // Private - hidden
    private String fullName;            // Private - hidden
    private String email;               // Private - hidden
    private List<Account> accounts;     // Private - hidden
    
    // Controlled read access
    public String getUserId() {
        return userId;
    }
    
    public String getFullName() {
        return fullName;
    }
    
    // Controlled write access with validation
    public void setFullName(String fullName) {
        if (InputValidator.isValidFullName(fullName)) {
            this.fullName = fullName;
        }
    }
    
    // Return copy to prevent external modification
    public List<Account> getAccounts() {
        return new ArrayList<>(accounts);
    }
    
    // Controlled modification
    public boolean addAccount(Account account) {
        if (account != null && !accounts.contains(account)) {
            return accounts.add(account);
        }
        return false;
    }
    
    // Complex computation hidden from caller
    public double getTotalBalance() {
        return accounts.stream()
                .mapToDouble(Account::getBalance)
                .sum();
    }
}

// Client cannot directly access internals
User user = new User("USER_001", "john", "hashed", "John Doe", "john@example.com");
user.userId = "USER_002";  // COMPILE ERROR - cannot assign to private field
user.fullName = "Jane Doe";  // OK - uses setter with validation
user.accounts.add(account);  // Cannot add directly - must use addAccount()
```

### Example 3: Repository Pattern (Data Access Abstraction)

```java
// REPOSITORY INTERFACE - defines contract
public interface IUserRepository {
    boolean save(User user);
    User findById(String userId);
    User findByUsername(String username);
    boolean update(User user);
    boolean delete(String userId);
}

// IMPLEMENTATION - in-memory storage
public class UserRepository {
    private Map<String, User> users = new HashMap<>();
    
    public boolean save(User user) {
        if (user == null || users.containsKey(user.getUserId())) {
            return false;
        }
        users.put(user.getUserId(), user);
        return true;
    }
    
    public User findById(String userId) {
        return users.get(userId);
    }
}

// SERVICE - uses repository without knowing implementation
public class AuthService {
    private UserRepository userRepository;
    
    public User login(String username, String password) {
        // Doesn't care HOW user is retrieved (database, file, etc.)
        User user = userRepository.findByUsername(username);
        if (user != null && PasswordHasher.verifyPassword(password, user.getPasswordHash())) {
            return user;
        }
        return null;
    }
}

// Can easily switch implementations without changing AuthService
// Replace with DatabaseUserRepository, FileUserRepository, etc.
```

### Example 4: Singleton Pattern (Data Store)

```java
public class InMemoryDataStore {
    private static InMemoryDataStore instance;  // Single instance
    private UserRepository userRepository;
    private AccountRepository accountRepository;
    private TransactionRepository transactionRepository;
    
    // Private constructor - prevent instantiation
    private InMemoryDataStore() {
        this.userRepository = new UserRepository();
        this.accountRepository = new AccountRepository();
        this.transactionRepository = new TransactionRepository();
    }
    
    // Thread-safe singleton getter
    public static synchronized InMemoryDataStore getInstance() {
        if (instance == null) {
            instance = new InMemoryDataStore();
        }
        return instance;
    }
    
    // Public methods to access repositories
    public UserRepository getUserRepository() {
        return userRepository;
    }
    
    public AccountRepository getAccountRepository() {
        return accountRepository;
    }
    
    public TransactionRepository getTransactionRepository() {
        return transactionRepository;
    }
}

// Usage - always get same instance
InMemoryDataStore store1 = InMemoryDataStore.getInstance();
InMemoryDataStore store2 = InMemoryDataStore.getInstance();
assert store1 == store2;  // Same object
```

### Example 5: Input Validation

```java
public class InputValidator {
    
    // Username: 4-20 chars, alphanumeric and underscores
    public static boolean isValidUsername(String username) {
        if (username == null || username.isEmpty()) {
            return false;
        }
        if (username.length() < 4 || username.length() > 20) {
            return false;
        }
        return username.matches("^[a-zA-Z0-9_]+$");  // Regex validation
    }
    
    // Amount: positive, max 999,999,999.99
    public static boolean isValidAmount(double amount) {
        return amount > 0 && amount <= 999999999.99;
    }
    
    // Email: basic validation
    public static boolean isValidEmail(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }
        return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    }
    
    // Utility method for parsing with validation
    public static double parseAmount(String amountStr) {
        try {
            double amount = Double.parseDouble(amountStr);
            return isValidAmount(amount) ? amount : -1;
        } catch (NumberFormatException e) {
            return -1;  // Invalid number
        }
    }
}

// Usage
if (InputValidator.isValidUsername(username)) {
    // Process username
} else {
    ConsoleUtils.printError("Invalid username format");
}

double amount = InputValidator.parseAmount("1000.50");
if (amount < 0) {
    ConsoleUtils.printError("Invalid amount");
}
```

---

## Design Pattern Examples

### 1. Facade Pattern (BankService)

```java
// FACADE - simplifies access to multiple services
public class BankService {
    private AuthService authService;
    private AccountService accountService;
    private TransactionService transactionService;
    
    public User login(String username, String password) {
        return authService.login(username, password);
    }
    
    public CheckingAccount createCheckingAccount(User user, double balance, double overdraft) {
        return accountService.createCheckingAccount(user, balance, overdraft);
    }
    
    public boolean transfer(String from, String to, double amount) {
        return transactionService.transfer(from, to, amount);
    }
}

// CLIENT - simple interface instead of juggling multiple services
BankService bank = new BankService();
User user = bank.login("john", "password");  // Hides AuthService details
bank.createCheckingAccount(user, 1000, 500);  // Hides AccountService details
bank.transfer("ACC001", "ACC002", 100);  // Hides TransactionService details
```

### 2. Template Method Pattern (Account Withdrawal)

```java
public abstract class Account {
    public boolean withdraw(double amount, String description) {
        // Template method - defines algorithm structure
        
        if (!canWithdraw(amount)) {  // Step 1: Check withdrawal eligibility
            return false;  // Overridden in subclasses
        }
        
        if (this.balance < amount) {  // Step 2: Check balance
            return false;
        }
        
        this.balance -= amount;  // Step 3: Deduct amount
        
        Transaction txn = new Transaction(...);  // Step 4: Record transaction
        this.transactions.add(txn);
        
        applyAccountSpecificRules();  // Step 5: Apply specific rules (hook)
        // Overridden in subclasses
        
        return true;
    }
    
    // To be implemented by subclasses
    public abstract boolean canWithdraw(double amount);
    public abstract void applyAccountSpecificRules();
}

// CheckingAccount customizes behavior
public class CheckingAccount extends Account {
    @Override
    public boolean canWithdraw(double amount) {
        return (this.balance + overdraftLimit) >= amount;  // Allow overdraft
    }
    
    @Override
    public void applyAccountSpecificRules() {
        monthlyWithdrawals++;  // Track withdrawals
        if (this.balance < 0) {
            this.balance -= 35.0;  // Overdraft fee
        }
    }
}

// SavingsAccount customizes differently
public class SavingsAccount extends Account {
    @Override
    public boolean canWithdraw(double amount) {
        return withdrawalsThisMonth < 6 && (balance - amount) >= 100;
    }
    
    @Override
    public void applyAccountSpecificRules() {
        withdrawalsThisMonth++;  // Track withdrawals
        if (withdrawalsThisMonth > 6) {
            this.balance -= withdrawalPenalty;  // Excess withdrawal fee
        }
    }
}
```

---

## Error Handling Examples

### Example 1: Null Checks

```java
public User login(String username, String password) {
    // Validate inputs
    if (InputValidator.isNullOrEmpty(username) || InputValidator.isNullOrEmpty(password)) {
        return null;  // Invalid input
    }
    
    // Find user
    User user = userRepository.findByUsername(username);
    if (user == null) {
        return null;  // User not found
    }
    
    // Verify password
    if (PasswordHasher.verifyPassword(password, user.getPasswordHash())) {
        return user;  // Success
    }
    
    return null;  // Wrong password
}
```

### Example 2: Validation Before State Change

```java
public boolean deposit(double amount, String description) {
    // Validate amount
    if (amount <= 0) {
        return false;
    }
    
    if (!InputValidator.isValidAmount(amount)) {
        return false;
    }
    
    // Only update state if validation passes
    this.balance += amount;
    Transaction txn = new Transaction(...);
    this.transactions.add(txn);
    return true;
}

public boolean withdraw(double amount, String description) {
    // Multiple validation layers
    if (amount <= 0) {
        return false;
    }
    
    if (!canWithdraw(amount)) {  // Account-specific check
        return false;
    }
    
    if (this.balance < amount) {  // Insufficient balance
        return false;
    }
    
    // Only proceed if all checks pass
    this.balance -= amount;
    Transaction txn = new Transaction(...);
    this.transactions.add(txn);
    applyAccountSpecificRules();
    return true;
}
```

### Example 3: Transfer with Rollback

```java
public boolean transfer(String fromAccountNumber, String toAccountNumber, double amount) {
    // Validate inputs
    if (amount <= 0) {
        return false;
    }
    
    // Get accounts
    Account fromAccount = accountRepository.findByAccountNumber(fromAccountNumber);
    Account toAccount = accountRepository.findByAccountNumber(toAccountNumber);
    
    // Validate accounts exist
    if (fromAccount == null || toAccount == null) {
        return false;
    }
    
    // Validate accounts are active
    if (!fromAccount.isActive() || !toAccount.isActive()) {
        return false;
    }
    
    // Perform transfer
    if (fromAccount.transfer(amount, toAccountNumber)) {
        // If first part succeeds, complete the transfer
        toAccount.receiveTransfer(amount, fromAccountNumber);
        
        // Update repository
        accountRepository.update(fromAccount);
        accountRepository.update(toAccount);
        
        return true;
    }
    
    // If transfer failed, both accounts unchanged (implicit rollback)
    return false;
}
```

---

## Extension Examples

### Example 1: Adding Interest Calculation

```java
// NEW SERVICE
public class InterestService {
    private AccountRepository accountRepository;
    private TransactionRepository transactionRepository;
    
    // Apply monthly interest to all savings accounts
    public void applyMonthlyInterest() {
        // Query all accounts and apply interest to savings accounts
        for (Account account : getAllAccounts()) {
            if (account instanceof SavingsAccount) {
                SavingsAccount savings = (SavingsAccount) account;
                double interest = savings.applyMonthlyInterest();
                accountRepository.update(savings);
                
                // Log the interest transaction
                Transaction interestTxn = new Transaction(
                        "INT-" + System.nanoTime(),
                        account.getAccountNumber(),
                        null,
                        interest,
                        "INTEREST",
                        "Monthly interest credit"
                );
                transactionRepository.saveTransaction(account.getAccountNumber(), interestTxn);
            }
        }
    }
}

// IN SAVINGS ACCOUNT
public double applyMonthlyInterest() {
    double monthlyRate = interestRate / 12.0;
    double interest = this.balance * monthlyRate;
    this.balance += interest;
    return interest;
}

// USAGE
InterestService interestService = new InterestService();
interestService.applyMonthlyInterest();  // Called monthly
```

### Example 2: Adding Database Support

```java
// NEW INTERFACE
public interface IUserRepository {
    boolean save(User user);
    User findById(String userId);
    boolean update(User user);
}

// DATABASE IMPLEMENTATION
public class DatabaseUserRepository implements IUserRepository {
    private Connection connection;
    
    @Override
    public boolean save(User user) {
        String sql = "INSERT INTO users (id, username, password_hash, full_name, email) " +
                     "VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, user.getUserId());
            stmt.setString(2, user.getUsername());
            stmt.setString(3, user.getPasswordHash());
            stmt.setString(4, user.getFullName());
            stmt.setString(5, user.getEmail());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }
    
    @Override
    public User findById(String userId) {
        String sql = "SELECT * FROM users WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new User(
                        rs.getString("id"),
                        rs.getString("username"),
                        rs.getString("password_hash"),
                        rs.getString("full_name"),
                        rs.getString("email")
                );
            }
        } catch (SQLException e) {
            return null;
        }
        return null;
    }
}

// SERVICES USE SAME INTERFACE - NO CHANGES NEEDED
public class AuthService {
    private IUserRepository userRepository;  // Can be any implementation
    
    public User login(String username, String password) {
        // Same code works with database or in-memory repository
        User user = userRepository.findById(userId);
        // ...
    }
}

// SWITCH IMPLEMENTATION
IUserRepository repo = new DatabaseUserRepository();  // Database
// OR
IUserRepository repo = new InMemoryUserRepository();  // In-memory
// AuthService works with both!
```

### Example 3: Adding Loan Module

```java
// NEW ENTITY
public class Loan {
    private String loanId;
    private String userId;
    private double principalAmount;
    private double interestRate;
    private int termMonths;
    private double monthlyPayment;
    private double amountPaid;
    private LocalDate startDate;
    private LoanStatus status;
    
    // Getters, setters, business logic
}

// NEW SERVICE
public class LoanService {
    private LoanRepository loanRepository;
    private AccountService accountService;
    private TransactionService transactionService;
    
    public Loan createLoan(String userId, double amount, double rate, int months) {
        // Validate user creditworthiness
        // Create loan
        // Set up monthly payment schedule
        String loanId = IDGenerator.generateLoanId();
        Loan loan = new Loan(loanId, userId, amount, rate, months);
        loanRepository.save(loan);
        return loan;
    }
    
    public boolean payLoanInstallment(String loanId, double amount) {
        Loan loan = loanRepository.findById(loanId);
        if (loan != null && loan.getStatus() == LoanStatus.ACTIVE) {
            loan.addPayment(amount);
            loanRepository.update(loan);
            
            // Record transaction
            transactionService.recordLoanPayment(loan.getUserId(), loanId, amount);
            
            // Check if loan is paid off
            if (loan.isPaidOff()) {
                loan.setStatus(LoanStatus.COMPLETED);
            }
            
            return true;
        }
        return false;
    }
}

// INTEGRATE WITH EXISTING SYSTEM
public class BankService {
    private AuthService authService;
    private AccountService accountService;
    private TransactionService transactionService;
    private LoanService loanService;  // NEW
    
    public Loan applyForLoan(String userId, double amount, double rate, int months) {
        return loanService.createLoan(userId, amount, rate, months);
    }
}
```

---

## Summary

This code walkthrough demonstrates:

1. **Clean Architecture**: Clear separation of concerns
2. **OOP Principles**: Encapsulation, Inheritance, Polymorphism, Abstraction
3. **Design Patterns**: Facade, Singleton, Template Method, Repository, Strategy
4. **Error Handling**: Validation, null checks, graceful failure
5. **Extensibility**: Easy to add new features without modifying existing code

The BankApp architecture makes it straightforward to add new features and maintain code quality as the application grows!
