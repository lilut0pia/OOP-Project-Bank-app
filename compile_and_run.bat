@echo off
REM ========================================
REM BankApp Compilation and Execution Script
REM ========================================

setlocal enabledelayedexpansion

REM Set project directory
set PROJECT_DIR=d:\Code\OOP\Project\BankApp

REM Change to project directory
cd /d %PROJECT_DIR%

REM Create bin directory if it doesn't exist
if not exist bin (
    echo Creating bin directory...
    mkdir bin
)

REM Clear previous compiled files
echo Cleaning previous builds...
del /s /q bin\* 2>nul

REM Compile Java files
echo.
echo ========================================
echo Compiling BankApp...
echo ========================================
echo.

javac -d bin -sourcepath src ^
    src\com\bankapp\BankApplication.java ^
    src\com\bankapp\model\*.java ^
    src\com\bankapp\services\*.java ^
    src\com\bankapp\controllers\*.java ^
    src\com\bankapp\data\*.java ^
    src\com\bankapp\utils\*.java

REM Check compilation status
if %ERRORLEVEL% EQU 0 (
    echo.
    echo ========================================
    echo Compilation Successful!
    echo ========================================
    echo.
    echo Starting BankApp...
    echo ========================================
    echo.
    
    REM Run the application
    java -cp bin com.bankapp.BankApplication
) else (
    echo.
    echo ========================================
    echo Compilation Failed!
    echo ========================================
    echo Please check the error messages above.
    pause
)

endlocal
