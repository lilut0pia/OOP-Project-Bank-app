# BankApp - Installation & Getting Started

## Quick Start (Windows)

### Option 1: Automatic Compilation and Run

```bash
# Open Command Prompt in the BankApp directory
# Double-click or run:
compile_and_run.bat
```

### Option 2: Manual Compilation

```bash
# Navigate to BankApp directory
cd d:\Code\OOP\Project\BankApp

# Create bin directory
mkdir bin

# Compile all Java files
javac -d bin -sourcepath src src\com\bankapp\*.java src\com\bankapp\model\*.java src\com\bankapp\services\*.java src\com\bankapp\controllers\*.java src\com\bankapp\data\*.java src\com\bankapp\utils\*.java

# Run the application
java -cp bin com.bankapp.BankApplication
```

## Quick Start (Linux/Mac)

```bash
# Navigate to BankApp directory
cd d:/Code/OOP/Project/BankApp

# Make script executable
chmod +x compile_and_run.sh

# Run the script
./compile_and_run.sh
```

---

## Project Structure Overview

```
BankApp/
├── src/
│   └── com/bankapp/
│       ├── BankApplication.java          Main entry point
│       ├── model/                         Data models
│       │   ├── User.java
│       │   ├── Account.java (abstract)
│       │   ├── CheckingAccount.java
│       │   ├── SavingsAccount.java
│       │   └── Transaction.java
│       ├── services/                      Business logic
│       │   ├── BankService.java
│       │   ├── AuthService.java
│       │   ├── AccountService.java
│       │   └── TransactionService.java
│       ├── controllers/                   User interaction handling
│       │   ├── AuthController.java
│       │   ├── AccountController.java
│       │   └── TransactionController.java
│       ├── data/                          Data access layer
│       │   ├── InMemoryDataStore.java
│       │   ├── UserRepository.java
│       │   ├── AccountRepository.java
│       │   └── TransactionRepository.java
│       └── utils/                         Utilities
│           ├── PasswordHasher.java
│           ├── InputValidator.java
│           ├── IDGenerator.java
│           └── ConsoleUtils.java
├── docs/
│   ├── uml/                               UML diagrams
│   │   ├── 01_UseCase_Diagram.puml
│   │   ├── 02_Class_Diagram.puml
│   │   ├── 03_Sequence_Login.puml
│   │   ├── 04_Sequence_Deposit.puml
│   │   ├── 05_Sequence_Transfer.puml
│   │   └── 06_Sequence_OpenSavingsAccount.puml
│   ├── OOP_PRINCIPLES.md
│   └── INSTALLATION.md (this file)
├── bin/                                   Compiled bytecode (created on compilation)
├── README.md                              Main documentation
├── compile_and_run.bat                    Windows compilation script
├── compile_and_run.sh                     Linux/Mac compilation script
```

---

## Troubleshooting

### Compilation Issues

**Problem**: "javac is not recognized"
**Solution**: Ensure Java JDK is installed and added to PATH
```bash
java -version  # Check if Java is installed
javac -version # Check if JDK is installed
```

**Problem**: "Package com.bankapp does not exist"
**Solution**: Ensure you're in the BankApp directory and using -sourcepath option
```bash
# Correct:
javac -d bin -sourcepath src src\com\bankapp\*.java

# Ensure src folder structure matches package names
src/com/bankapp/model/User.java  # Matches package com.bankapp.model
```

**Problem**: "Cannot find symbol"
**Solution**: Compile all files together, not one by one
```bash
# Wrong (compiles only User.java):
javac -d bin src\com\bankapp\model\User.java

# Correct (compiles all files):
javac -d bin -sourcepath src src\com\bankapp\model\*.java src\com\bankapp\services\*.java ...
```

### Runtime Issues

**Problem**: "Exception in thread main"
**Solution**: Check that you're using the correct main class
```bash
# Correct:
java -cp bin com.bankapp.BankApplication

# Incorrect:
java -cp bin BankApplication
java -cp bin com.bankapp  # Missing class name
```

**Problem**: Input not working in console
**Solution**: Some IDEs have issues with Scanner. Run from command line instead.

---

## System Requirements

- **Java Version**: Java 8 or higher
- **Operating System**: Windows, Linux, or macOS
- **Memory**: Minimum 512MB (for JVM)
- **Disk Space**: ~50MB for source code and compiled files

---

## Verifying Installation

### Step 1: Check Java Installation

```bash
java -version

# Expected output:
# java version "1.8.0_xxx" or higher
# Java(TM) SE Runtime Environment (build 1.8.0_xxx)
# Java HotSpot(TM) 64-Bit Server VM (build 25.xxx)
```

### Step 2: Check Java Compiler

```bash
javac -version

# Expected output:
# javac 1.8.0_xxx or higher
```

### Step 3: Navigate to Project

```bash
cd d:\Code\OOP\Project\BankApp
dir src\com\bankapp  # Verify folder structure
```

### Step 4: Compile

```bash
mkdir bin
javac -d bin -sourcepath src src\com\bankapp\*.java src\com\bankapp\model\*.java src\com\bankapp\services\*.java src\com\bankapp\controllers\*.java src\com\bankapp\data\*.java src\com\bankapp\utils\*.java
```

### Step 5: Run

```bash
java -cp bin com.bankapp.BankApplication
```

---

## Understanding the Application Flow

### 1. Application Startup

```
BankApplication.main()
    └─> BankApplication constructor
        ├─> BankService initialization
        │   ├─> AuthService
        │   ├─> AccountService
        │   └─> TransactionService
        ├─> AuthController
        ├─> AccountController
        └─> TransactionController
    └─> application.start()
```

### 2. User Authentication Flow

```
User Registration/Login
    └─> AuthController.handleRegistration/Login()
        └─> AuthService.register/login()
            └─> UserRepository operations
                └─> InMemoryDataStore
                    └─> Validation & Storage
```

### 3. Account Operations Flow

```
Account Management
    ├─> AccountController.handleOpen*Account()
    │   └─> AccountService.create*Account()
    │       └─> AccountRepository.save()
    │
    └─> TransactionController.handle*()
        └─> TransactionService.*()
            └─> AccountRepository operations
```

---

## Testing the Application

### Test Scenario 1: Basic Registration and Login

```
1. Select "Register"
2. Enter: username=testuser, password=Test123, name=Test User, email=test@example.com
3. Verify success message
4. Select "Login"
5. Enter same credentials
6. Verify login successful
```

### Test Scenario 2: Account Creation and Deposit

```
1. Login with test account
2. Select "Open New Account"
3. Choose "Open Checking Account"
4. Enter initial amount: 1000.00
5. Enter overdraft limit: 500.00
6. Verify account created
7. Select "Manage Account"
8. Choose the checking account
9. Select "Deposit"
10. Enter amount: 500.00
11. Verify new balance is 1500.00
```

### Test Scenario 3: Savings Account with Withdrawal Limit

```
1. Open Savings Account with 5000.00 and 3% interest
2. Verify account type shows "SAVINGS"
3. Try to withdraw 6 times (should succeed)
4. Try to withdraw 7th time (should fail)
5. Verify error message about withdrawal limit
```

### Test Scenario 4: Transfer Between Accounts

```
1. Create two accounts (one checking, one savings)
2. Select checking account
3. Select "Transfer"
4. Enter savings account number
5. Enter amount: 500.00
6. Verify both accounts updated correctly
7. View transaction history in both accounts
```

---

## File Descriptions

### Core Classes

| File | Purpose |
|------|---------|
| `BankApplication.java` | Main entry point, menu orchestration |
| `User.java` | User entity, account management |
| `Account.java` | Abstract base class for accounts |
| `CheckingAccount.java` | Checking account implementation |
| `SavingsAccount.java` | Savings account with interest |
| `Transaction.java` | Transaction record entity |

### Service Layer

| File | Purpose |
|------|---------|
| `BankService.java` | Facade service, coordinates all services |
| `AuthService.java` | User registration, login, authentication |
| `AccountService.java` | Account creation, retrieval, management |
| `TransactionService.java` | Deposit, withdraw, transfer operations |

### Controller Layer

| File | Purpose |
|------|---------|
| `AuthController.java` | Handles registration/login UI |
| `AccountController.java` | Handles account management UI |
| `TransactionController.java` | Handles transaction UI |

### Data Access Layer

| File | Purpose |
|------|---------|
| `InMemoryDataStore.java` | Singleton data storage manager |
| `UserRepository.java` | User data CRUD operations |
| `AccountRepository.java` | Account data CRUD operations |
| `TransactionRepository.java` | Transaction data CRUD operations |

### Utilities

| File | Purpose |
|------|---------|
| `PasswordHasher.java` | Password hashing and verification |
| `InputValidator.java` | Input validation |
| `IDGenerator.java` | Unique ID generation |
| `ConsoleUtils.java` | Console I/O utilities |

---

## Performance Notes

- **In-Memory Storage**: All data stored in RAM, lost on application exit
- **Single-Threaded**: Not thread-safe for concurrent access
- **Scalability**: Suitable for learning/demo, not production

---

## Next Steps

1. **Study the Code**: Review OOP_PRINCIPLES.md for design details
2. **Run Test Scenarios**: Follow the testing guide above
3. **Explore Extensions**: Read EXTENSIBILITY section in README.md
4. **Implement Features**: Add database, web UI, or other enhancements
5. **Deploy**: Prepare for production use (add database, security, etc.)

---

## Additional Resources

- **UML Diagrams**: Located in `docs/uml/` (PlantUML format)
- **Architecture Docs**: See `README.md` for complete architecture
- **OOP Analysis**: See `OOP_PRINCIPLES.md` for design principle details

---

## Support

For issues or questions:
1. Check compilation error messages
2. Review the troubleshooting section above
3. Verify project structure matches expected layout
4. Ensure Java version is 8 or higher

---

**Happy Banking! 🏦**
