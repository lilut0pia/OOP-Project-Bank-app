# BankApp - Quick Reference Guide

## 🚀 Quick Start (Choose One)

### Option 1: Windows (Easiest)
```batch
cd d:\Code\OOP\Project\BankApp
compile_and_run.bat
```

### Option 2: Linux/Mac
```bash
cd d:/Code/OOP/Project/BankApp
chmod +x compile_and_run.sh
./compile_and_run.sh
```

### Option 3: Manual Compilation
```bash
cd d:\Code\OOP\Project\BankApp
mkdir bin
javac -d bin -sourcepath src src\com\bankapp\*.java src\com\bankapp\model\*.java src\com\bankapp\services\*.java src\com\bankapp\controllers\*.java src\com\bankapp\data\*.java src\com\bankapp\utils\*.java
java -cp bin com.bankapp.BankApplication
```

---

## 📚 Documentation Quick Links

| Document | Purpose | Read Time |
|----------|---------|-----------|
| **README.md** | Complete overview & architecture | 20 min |
| **OOP_PRINCIPLES.md** | OOP design analysis with examples | 25 min |
| **INSTALLATION.md** | Setup & troubleshooting guide | 15 min |
| **CODE_WALKTHROUGH.md** | Code examples & design patterns | 30 min |
| **FILE_STRUCTURE.md** | File inventory & statistics | 10 min |
| **PROJECT_SUMMARY.md** | Project completion status | 10 min |

---

## 🎯 Feature Quick Reference

### Registration
```
Menu → Register
Enter: username, password, name, email
Result: User created with unique ID
```

### Login
```
Menu → Login
Enter: username, password
Result: Authenticated user with access to accounts
```

### Create Checking Account
```
Logged In → Open New Account → Checking
Enter: initial balance, overdraft limit
Result: Checking account with ACC######### number
```

### Create Savings Account
```
Logged In → Open New Account → Savings
Enter: initial balance, interest rate (0.03 = 3%)
Result: Savings account with 3% annual interest
```

### Deposit Money
```
Manage Account → Select Account → Deposit
Enter: amount, description
Result: Balance increases, transaction recorded
```

### Withdraw Money
```
Manage Account → Select Account → Withdraw
Enter: amount, description
Result: Balance decreases (if rules allow), transaction recorded
Note: Savings has 6/month limit, Checking allows unlimited
```

### Transfer Between Accounts
```
Manage Account → Select Account → Transfer
Enter: recipient account number, amount, reason
Result: Money moved between accounts, both record transaction
```

### View Transaction History
```
Manage Account → Select Account → View Transaction History
Result: All transactions displayed in chronological order
```

---

## 🏗️ Architecture Overview

```
┌─────────────────────────────────────────────┐
│          User Console Interface             │
│         (BankApplication)                   │
└────────────────┬────────────────────────────┘
                 │
    ┌────────────┼────────────┐
    │            │            │
┌───▼───┐    ┌──▼────┐   ┌──▼─────────┐
│AuthCtl│    │AcctCtl│   │TransactionC│
└───┬───┘    └──┬─────┘   └──┬────────┘
    │           │            │
┌───▼──────────▼──────────────▼───┐
│        BankService (Facade)     │
│  ├─AuthService                  │
│  ├─AccountService               │
│  └─TransactionService           │
└───┬──────────────────────────────┘
    │
┌───▼────────────────────────────────────┐
│    InMemoryDataStore (Singleton)       │
│  ├─UserRepository                      │
│  ├─AccountRepository                   │
│  └─TransactionRepository               │
└────────────────────────────────────────┘
```

---

## 🔐 Class Relationships

### User → Accounts
```
User (1) ──has many──> Account (*)
  └─ accounts: List<Account>
```

### Account Types
```
Account (abstract)
  ├─ CheckingAccount
  │   └─ features: overdraft, unlimited transactions
  └─ SavingsAccount
      └─ features: interest, 6/month withdrawal limit
```

### Account → Transactions
```
Account (1) ──has many──> Transaction (*)
  └─ transactions: List<Transaction>
```

---

## 📊 Key Data Models

### User Object
```java
User {
  userId: String              // "USER_ABC12345"
  username: String            // "john_doe"
  passwordHash: String        // SHA-256 hash
  fullName: String            // "John Doe"
  email: String               // "john@example.com"
  accounts: List<Account>     // Multiple accounts
  createdAt: long             // Timestamp
}
```

### Account Hierarchy
```java
Account (abstract) {
  accountNumber: String           // "ACC1234567890"
  balance: double                 // $1000.00
  transactions: List<Transaction> // Transaction history
  isActive: boolean               // true/false
  
  // Abstract methods - override in subclasses
  getAccountType(): String
  canWithdraw(amount): boolean
  applyAccountSpecificRules(): void
}

CheckingAccount extends Account {
  overdraftLimit: double
  monthlyWithdrawals: int
}

SavingsAccount extends Account {
  interestRate: double            // 0.03 = 3%
  withdrawalsThisMonth: int
  withdrawalPenalty: double
}
```

### Transaction Object
```java
Transaction {
  transactionId: String       // "TXN_1234567890"
  fromAccountNumber: String   // Source
  toAccountNumber: String     // Destination (if transfer)
  amount: double              // $500.00
  type: String                // DEPOSIT, WITHDRAWAL, TRANSFER_IN, TRANSFER_OUT, INTEREST
  description: String         // "Salary deposit"
  timestamp: long             // When it happened
  status: String              // "SUCCESS"
}
```

---

## 🛠️ Service Layer Quick Guide

### AuthService
```
register(username, password, fullName, email): User
  └─ Validates inputs, checks username uniqueness, creates user, hashes password

login(username, password): User
  └─ Finds user by username, verifies password, returns user or null

getUserById(userId): User
getUserByUsername(username): User
```

### AccountService
```
createCheckingAccount(user, initialBalance, overdraftLimit): CheckingAccount
  └─ Generates account number, creates account, adds to user

createSavingsAccount(user, initialBalance, interestRate): SavingsAccount
  └─ Generates account number, creates account, adds to user

getAccount(accountNumber): Account
closeAccount(accountNumber): boolean
accountExists(accountNumber): boolean
getAccountBalance(accountNumber): double
getAccountType(accountNumber): String
```

### TransactionService
```
deposit(accountNumber, amount, description): boolean
  └─ Validates, updates balance, records transaction

withdraw(accountNumber, amount, description): boolean
  └─ Checks restrictions, updates balance, records transaction

transfer(fromAccountNumber, toAccountNumber, amount, description): boolean
  └─ Updates both accounts, records transactions both sides

getTransactionHistory(accountNumber): List<Transaction>
getRecentTransactions(accountNumber, count): List<Transaction>
getTransaction(transactionId): Transaction
```

---

## ✅ OOP Principles Demonstrated

| Principle | Example | File |
|-----------|---------|------|
| **Encapsulation** | Private fields with public getters | User.java, Account.java |
| **Inheritance** | CheckingAccount, SavingsAccount extend Account | Account hierarchy |
| **Polymorphism** | canWithdraw() implemented differently | CheckingAccount vs SavingsAccount |
| **Abstraction** | Abstract Account class | Account.java |

---

## 🎨 Design Patterns Used

| Pattern | Example | Benefit |
|---------|---------|---------|
| **Singleton** | InMemoryDataStore | Single data storage instance |
| **Repository** | UserRepository, etc. | Data access abstraction |
| **Facade** | BankService | Simplifies service access |
| **MVC** | Controllers + Models + Services | Separation of concerns |
| **Template Method** | Account.withdraw() | Common structure, custom behavior |
| **Strategy** | canWithdraw() per account type | Different algorithms per type |

---

## 🔍 Validation Rules

### Username
- Length: 4-20 characters
- Pattern: alphanumeric + underscores only
- Uniqueness: Cannot be taken

### Password
- Minimum: 6 characters
- Storage: SHA-256 hashed
- Verification: Secure comparison

### Email
- Format: standard email pattern
- Example: user@example.com

### Amount
- Range: > 0 and ≤ $999,999,999.99
- Precision: Handled as double
- Display: Formatted as $X.XX

### Account Numbers
- Format: ACC + timestamp
- Length: 8-16 characters
- Uniqueness: Automatically generated

---

## 💰 Account Rules

### Checking Account
- ✅ Unlimited deposits
- ✅ Unlimited withdrawals
- ✅ Optional overdraft protection
- ✅ Can go negative (with overdraft limit)
- ✅ No minimum balance

### Savings Account
- ✅ Unlimited deposits
- ⚠️ Maximum 6 withdrawals per month
- ✅ Monthly interest (e.g., 3% annually)
- ✅ Minimum balance: $100
- ✅ Penalty fee if exceed withdrawal limit

---

## 📁 Important Directories

```
src/
  com/bankapp/
    model/           ← Entity classes (User, Account, Transaction)
    services/        ← Business logic (AuthService, etc.)
    controllers/     ← UI handling (AuthController, etc.)
    data/            ← Data access (Repositories)
    utils/           ← Utilities (Validation, Security, etc.)

docs/
  uml/               ← UML diagrams (PlantUML format)
  *.md               ← Documentation files

compile_and_run.*   ← Compilation scripts
```

---

## 🐛 Troubleshooting

### "javac is not recognized"
→ Install Java JDK and add to PATH

### "Cannot find symbol"
→ Compile all files together, not individually

### "Class not found"
→ Check package name matches file path structure

### Input not working
→ Run from command line, not IDE console

### Wrong menu appears
→ Check you're logged in (currentUser != null)

---

## 🎓 Learning Paths

### Beginner (Start Here)
1. Read README.md
2. Compile and run application
3. Test basic features (register, login, deposit)
4. Review User.java and Account.java

### Intermediate
5. Study OOP_PRINCIPLES.md
6. Examine service layer (AuthService, TransactionService)
7. Review controller layer (How UI flows work)
8. Understand error handling

### Advanced
9. Read CODE_WALKTHROUGH.md
10. Study repository pattern (InMemoryDataStore)
11. Review UML diagrams
12. Design new features

### Extension
13. Add database support
14. Add new account types
15. Implement new services
16. Create web API

---

## 📊 Project Statistics

| Metric | Count |
|--------|-------|
| Java Classes | 26 |
| Lines of Code | ~2,515 |
| Methods | 150+ |
| Documentation Pages | 5 |
| UML Diagrams | 6 |
| Packages | 6 |
| Design Patterns | 6 |

---

## 🎯 Next Steps

1. **Compile & Run**
   ```bash
   cd BankApp
   compile_and_run.bat  (Windows) or compile_and_run.sh (Mac/Linux)
   ```

2. **Test Features**
   - Register new user
   - Create checking & savings accounts
   - Deposit and withdraw
   - Transfer between accounts
   - View transaction history

3. **Study Code**
   - Read source files with comments
   - Review OOP_PRINCIPLES.md
   - Examine UML diagrams
   - Follow CODE_WALKTHROUGH.md examples

4. **Extend Application**
   - Add database persistence
   - Implement interest calculation
   - Create admin panel
   - Add more account types

---

## 📞 File Quick Reference

| Need | File | Location |
|------|------|----------|
| Main entry | BankApplication.java | src/com/bankapp/ |
| User model | User.java | src/com/bankapp/model/ |
| Account types | Account*.java | src/com/bankapp/model/ |
| Authentication | AuthService.java | src/com/bankapp/services/ |
| Transactions | TransactionService.java | src/com/bankapp/services/ |
| User UI | AuthController.java | src/com/bankapp/controllers/ |
| Data storage | InMemoryDataStore.java | src/com/bankapp/data/ |
| Validation | InputValidator.java | src/com/bankapp/utils/ |
| Setup | INSTALLATION.md | docs/ |
| Architecture | README.md | root/ |

---

## ✨ Key Features

✅ User registration & login  
✅ Multiple account types  
✅ Deposit & withdrawal  
✅ Inter-account transfers  
✅ Complete transaction history  
✅ Interest calculation (savings)  
✅ Withdrawal limits (savings)  
✅ Input validation  
✅ Error handling  
✅ Clean architecture  

---

**BankApp is ready to use!** 🏦

Start with: `compile_and_run.bat` (Windows) or `compile_and_run.sh` (Mac/Linux)
