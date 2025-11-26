package com.bankapp.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Lớp trừu tượng Account - Lớp cơ sở cho tất cả các loại tài khoản.
 * Định nghĩa các thuộc tính và hành vi chung.
 */
public abstract class Account implements Serializable {
    // ID duy nhất cho việc serialization, giúp đảm bảo tương thích phiên bản
    private static final long serialVersionUID = 1L;

    protected User owner;
    protected String accountNumber;
    protected String accountHolderName; // Thêm lại trường này
    protected double balance;
    protected boolean isActive;
    protected List<Transaction> transactions;

    public Account(User owner, String accountNumber, double balance) {
        if (owner == null) {
            throw new IllegalArgumentException("Account must have an owner.");
        }
        this.owner = owner;
        this.accountNumber = accountNumber;
        this.accountHolderName = owner.getFullName();
        this.balance = balance;
        this.isActive = true;
        this.transactions = new ArrayList<>();
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public double getBalance() {
        return balance;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public List<Transaction> getTransactions() {
        return new ArrayList<>(transactions);
    }

    public User getOwner() {
        return owner;
    }

    /**
     * Đóng tài khoản bằng cách đặt nó thành không hoạt động.
     * Trong tương lai, có thể thêm logic kiểm tra số dư trước khi đóng.
     */
    public void closeAccount() {
        this.isActive = false;
    }

    /**
     * Nạp tiền vào tài khoản.
     * @param amount Số tiền cần nạp
     * @param description Mô tả giao dịch
     * @return true nếu thành công
     */
    public boolean deposit(double amount, String description) {
        if (amount > 0) {
            this.balance += amount;
            Transaction txn = new Transaction(
                "TXN-" + System.nanoTime(),
                null, // fromAccountNumber
                this.accountNumber, // toAccountNumber
                amount, "DEPOSIT", description
            );
            this.transactions.add(txn);
            return true;
        } else {
            System.out.println("Deposit amount must be greater than 0.");
            return false;
        }
    }

    /**
     * Rút tiền từ tài khoản.
     * @param amount Số tiền cần rút
     * @return true nếu rút thành công, false nếu thất bại
     */
    public boolean withdraw(double amount, String description) {
        if (amount <= 0 || !canWithdraw(amount)) {
            System.out.println("Invalid transaction or insufficient funds.");
            return false;
        }
        this.balance -= amount;
        applyAccountSpecificRules(); // Áp dụng các quy tắc riêng
        Transaction txn = new Transaction(
            "TXN-" + System.nanoTime(),
            this.accountNumber, // fromAccountNumber
            null, // toAccountNumber
            amount, "WITHDRAWAL", description
        );
        this.transactions.add(txn);
        return true;
    }

    /**
     * Thực hiện chuyển tiền từ tài khoản này.
     * @param amount Số tiền
     * @param toAccountNumber Tài khoản nhận
     * @return true nếu thành công
     */
    public boolean transfer(double amount, String toAccountNumber) {
        // Logic rút tiền được xử lý trong withdraw()
        this.balance -= amount;
        Transaction txn = new Transaction(
            "TXN-" + System.nanoTime(),
            this.accountNumber,
            toAccountNumber,
            amount, "TRANSFER_OUT", "Transfer to " + toAccountNumber
        );
        this.transactions.add(txn);
        return true;
    }

    /**
     * Nhận tiền chuyển khoản vào tài khoản này.
     * @param amount Số tiền
     * @param fromAccountNumber Tài khoản gửi
     */
    public void receiveTransfer(double amount, String fromAccountNumber) {
        this.balance += amount;
        Transaction txn = new Transaction(
            "TXN-" + System.nanoTime(),
            fromAccountNumber,
            this.accountNumber,
            amount, "TRANSFER_IN", "Transfer from " + fromAccountNumber
        );
        this.transactions.add(txn);
    }

    public List<Transaction> getRecentTransactions(int count) {
        int size = transactions.size();
        if (size <= count) {
            return new ArrayList<>(transactions);
        }
        return new ArrayList<>(transactions.subList(size - count, size));
    }

    // ============= Phương thức trừu tượng =============

    public abstract String getAccountType();

    public abstract boolean canWithdraw(double amount);

    public abstract void applyAccountSpecificRules();

    @Override
    public String toString() {
        return "Account [Acc No=" + accountNumber + ", Holder=" + accountHolderName + ", Balance=" + balance + "]";
    }
}