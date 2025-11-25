# BankApp - OOP Design Principles Analysis

## 1. ENCAPSULATION

Encapsulation is achieved by hiding internal data and providing controlled access through methods.

### Example 1: Account Balance Management

```java
public abstract class Account {
    protected double balance;  // Hidden from direct access
    
    public double getBalance() {
        return balance;  // Controlled read access
    }
    
    public boolean deposit(double amount, String description) {
        if (amount <= 0) {
            return false;  // Validation before modification
        }
        this.balance += amount;  // Controlled write access
        Transaction transaction = new Transaction(...);
        this.transactions.add(transaction);
        return true;
    }
}
```

**Benefits**:
- Users cannot directly manipulate balance
- All modifications are validated
- Transaction history is maintained automatically
- Consistent state is enforced

### Example 2: User Account Management

```java
public class User {
    private List<Account> accounts;  // Private collection
    
    public List<Account> getAccounts() {
        return new ArrayList<>(accounts);  // Return copy, not reference
    }
    
    public boolean addAccount(Account account) {
        if (account != null && !accounts.contains(account)) {
            return accounts.add(account);  // Controlled addition
        }
        return false;  // Prevents duplicates
    }
}
```

---

## 2. INHERITANCE

Inheritance allows code reuse and establishes hierarchical relationships.

### Class Hierarchy

```
        Account (Abstract)
           /          \
          /            \
    CheckingAccount    SavingsAccount
```

### Base Class: Account

```java
public abstract class Account {
    protected String accountNumber;
    protected double balance;
    protected List<Transaction> transactions;
    
    // Common methods for all accounts
    public boolean deposit(double amount, String description) {
        // Shared implementation
    }
    
    public boolean withdraw(double amount, String description) {
        // Shared logic with polymorphic behavior
    }
    
    // Methods to be implemented by subclasses
    public abstract String getAccountType();
    public abstract boolean canWithdraw(double amount);
    public abstract void applyAccountSpecificRules();
}
```

### Subclass: CheckingAccount

```java
public class CheckingAccount extends Account {
    private double overdraftLimit;
    
    @Override
    public String getAccountType() {
        return "CHECKING";
    }
    
    @Override
    public boolean canWithdraw(double amount) {
        return (this.balance + this.overdraftLimit) >= amount;
    }
    
    @Override
    public void applyAccountSpecificRules() {
        monthlyWithdrawals++;
        // Apply overdraft fees if needed
    }
}
```

### Subclass: SavingsAccount

```java
public class SavingsAccount extends Account {
    private double interestRate;
    private int withdrawalsThisMonth;
    private static final int MAX_MONTHLY_WITHDRAWALS = 6;
    
    @Override
    public String getAccountType() {
        return "SAVINGS";
    }
    
    @Override
    public boolean canWithdraw(double amount) {
        if (withdrawalsThisMonth >= MAX_MONTHLY_WITHDRAWALS) {
            return false;  // Different rule than CheckingAccount
        }
        if ((this.balance - amount) < MINIMUM_BALANCE) {
            return false;  // Different rule than CheckingAccount
        }
        return true;
    }
    
    @Override
    public void applyAccountSpecificRules() {
        withdrawalsThisMonth++;
        // Apply excess withdrawal penalties
    }
}
```

**Benefits**:
- Code reuse: Common logic in base class
- Consistency: Same interface for all accounts
- Extensibility: Easy to add new account types
- Maintenance: Changes to common logic in one place

---

## 3. POLYMORPHISM

Polymorphism allows objects of different types to be treated uniformly.

### Runtime Polymorphism

```java
public void processWithdrawal(Account account, double amount) {
    // At compile time: Account type
    // At runtime: CheckingAccount or SavingsAccount
    
    if (account.canWithdraw(amount)) {  // Polymorphic call
        account.withdraw(amount, "Withdrawal");
        // For CheckingAccount: checks overdraftLimit
        // For SavingsAccount: checks monthlyWithdrawals and minimumBalance
    }
}

// Usage
List<Account> accounts = new ArrayList<>();
accounts.add(new CheckingAccount("ACC001", 1000.0, 500.0));
accounts.add(new SavingsAccount("ACC002", 5000.0, 0.03));

for (Account account : accounts) {
    String type = account.getAccountType();  // Different output per type
    boolean canWithdraw = account.canWithdraw(100);  // Different logic per type
}
```

### Method Overriding

```java
// Base class method
public abstract class Account {
    public abstract boolean canWithdraw(double amount);
}

// CheckingAccount implementation
@Override
public boolean canWithdraw(double amount) {
    return (this.balance + this.overdraftLimit) >= amount;
}

// SavingsAccount implementation
@Override
public boolean canWithdraw(double amount) {
    if (withdrawalsThisMonth >= MAX_MONTHLY_WITHDRAWALS) {
        return false;
    }
    if ((this.balance - amount) < MINIMUM_BALANCE) {
        return false;
    }
    return true;
}

// Same method call, different behavior based on actual type
Account account = getAccountFromUser();
boolean result = account.canWithdraw(100);  // Calls appropriate implementation
```

**Benefits**:
- Flexible code: Works with any Account subclass
- Extensible: New account types don't need code changes
- Maintainable: Logic is in appropriate class
- Type-safe: Compile-time type checking

---

## 4. ABSTRACTION

Abstraction hides complexity and shows only necessary details.

### Abstract Account Class

```java
public abstract class Account {
    // Hide implementation details
    protected List<Transaction> transactions;
    
    // Show only essential interface
    public abstract String getAccountType();
    public abstract boolean canWithdraw(double amount);
    
    // Complex logic hidden from users
    public boolean withdraw(double amount, String description) {
        // Multiple validations
        // Transaction creation
        // Rule application
        // Internal consistency checks
    }
}
```

### Facade Pattern in BankService

```java
// Hide complexity of multiple services
public class BankService {
    private AuthService authService;
    private AccountService accountService;
    private TransactionService transactionService;
    
    // Simple interface
    public User login(String username, String password) {
        return authService.login(username, password);
    }
    
    public boolean transfer(String from, String to, double amount) {
        return transactionService.transfer(from, to, amount);
    }
}

// Client only interacts with BankService
BankService bank = new BankService();
User user = bank.login("john", "password");  // Hides AuthService complexity
boolean success = bank.transfer("ACC001", "ACC002", 100);  // Hides TransactionService
```

### Repository Pattern - Data Access Abstraction

```java
// Abstraction hides how data is stored
public interface IUserRepository {
    User findById(String userId);
    boolean save(User user);
}

// Could implement with database
public class DatabaseUserRepository implements IUserRepository {
    public User findById(String userId) {
        String sql = "SELECT * FROM users WHERE id = ?";
        // Execute SQL query
    }
}

// Could implement with files
public class FileUserRepository implements IUserRepository {
    public User findById(String userId) {
        // Read from file
    }
}

// Could implement with in-memory store
public class InMemoryUserRepository implements IUserRepository {
    private Map<String, User> users;
    public User findById(String userId) {
        return users.get(userId);
    }
}

// Client uses same interface regardless of implementation
UserRepository repo = new InMemoryUserRepository();
User user = repo.findById("USER_123");  // Abstraction hides storage mechanism
```

**Benefits**:
- Complexity management: Users see only what's necessary
- Flexibility: Implementation details can change
- Separation of concerns: Each class has specific responsibility
- Testability: Can mock abstractions

---

## Summary of OOP Principles

| Principle | Implementation | Benefit |
|-----------|----------------|---------|
| **Encapsulation** | Private fields, public methods | Data protection, validation |
| **Inheritance** | Account hierarchy | Code reuse, consistency |
| **Polymorphism** | Method overriding | Flexible, extensible code |
| **Abstraction** | Abstract classes, interfaces | Complexity management |

---

## OOP Design Patterns Used

### 1. Template Method Pattern

```java
// Account defines structure
public abstract class Account {
    public boolean withdraw(double amount, String description) {
        if (canWithdraw(amount)) {  // Polymorphic call
            this.balance -= amount;
            applyAccountSpecificRules();  // Template hook
            return true;
        }
        return false;
    }
    
    public abstract boolean canWithdraw(double amount);  // Step 1
    public abstract void applyAccountSpecificRules();    // Step 2
}

// Subclasses implement steps
public class CheckingAccount extends Account {
    @Override
    public boolean canWithdraw(double amount) {
        // Checking-specific logic
    }
    
    @Override
    public void applyAccountSpecificRules() {
        // Checking-specific rules
    }
}
```

### 2. Strategy Pattern

```java
// Different withdrawal strategies
public abstract class Account {
    public abstract boolean canWithdraw(double amount);  // Strategy
}

// CheckingAccount strategy
public class CheckingAccount extends Account {
    @Override
    public boolean canWithdraw(double amount) {
        return balance >= amount - overdraftLimit;
    }
}

// SavingsAccount strategy
public class SavingsAccount extends Account {
    @Override
    public boolean canWithdraw(double amount) {
        return withdrawalsThisMonth < 6 && balance >= amount;
    }
}

// Use any strategy without knowing the type
Account account = getAccountFromUser();
if (account.canWithdraw(amount)) {  // Uses appropriate strategy
    account.withdraw(amount, "desc");
}
```

### 3. Singleton Pattern

```java
public class InMemoryDataStore {
    private static InMemoryDataStore instance;
    
    private InMemoryDataStore() {
        // Private constructor
    }
    
    public static synchronized InMemoryDataStore getInstance() {
        if (instance == null) {
            instance = new InMemoryDataStore();
        }
        return instance;
    }
}

// Usage
InMemoryDataStore store = InMemoryDataStore.getInstance();
// Always get the same instance
```

---

## Best Practices Demonstrated

1. **SOLID Principles**
   - Single Responsibility: Each class has one reason to change
   - Open/Closed: Open for extension (subclasses), closed for modification
   - Liskov Substitution: Subclasses can replace base class
   - Interface Segregation: Classes depend on specific interfaces
   - Dependency Inversion: Depend on abstractions, not concrete classes

2. **Clean Code**
   - Meaningful names
   - Small, focused methods
   - Comments explaining "why", not "what"
   - No code duplication
   - Proper error handling

3. **Maintainability**
   - Logical package structure
   - Clear separation of concerns
   - Easy to test individual components
   - Easy to extend with new features

---

## Conclusion

BankApp demonstrates that proper application of OOP principles leads to:
- **Maintainable** code that's easy to understand and modify
- **Extensible** design that accommodates new requirements
- **Robust** systems with proper error handling and validation
- **Professional** architecture following industry standards
