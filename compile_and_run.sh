#!/bin/bash
# ========================================
# BankApp Compilation and Execution Script
# ========================================

PROJECT_DIR="/d/Code/OOP/Project/BankApp"

# Change to project directory
cd "$PROJECT_DIR"

# Create bin directory if it doesn't exist
if [ ! -d "bin" ]; then
    echo "Creating bin directory..."
    mkdir -p bin
fi

# Clear previous compiled files
echo "Cleaning previous builds..."
rm -rf bin/*

# Compile Java files
echo ""
echo "========================================"
echo "Compiling BankApp..."
echo "========================================"
echo ""

javac -d bin -sourcepath src \
    src/com/bankapp/BankApplication.java \
    src/com/bankapp/model/*.java \
    src/com/bankapp/services/*.java \
    src/com/bankapp/controllers/*.java \
    src/com/bankapp/data/*.java \
    src/com/bankapp/utils/*.java

# Check compilation status
if [ $? -eq 0 ]; then
    echo ""
    echo "========================================"
    echo "Compilation Successful!"
    echo "========================================"
    echo ""
    echo "Starting BankApp..."
    echo "========================================"
    echo ""
    
    # Run the application
    java -cp bin com.bankapp.BankApplication
else
    echo ""
    echo "========================================"
    echo "Compilation Failed!"
    echo "========================================"
    echo "Please check the error messages above."
    read -p "Press Enter to exit..."
fi
