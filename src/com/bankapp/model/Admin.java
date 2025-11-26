package com.bankapp.model;

import com.bankapp.service.Bank;
import java.util.List;

/**
 * Lớp Admin đại diện cho một quản trị viên ngân hàng.
 * Kế thừa từ User và có thêm các quyền để xem dữ liệu người dùng.
 */
public class Admin extends User {

    /**
     * Constructor để tạo một đối tượng Admin.
     *
     * @param userId       ID duy nhất của admin
     * @param username     Tên đăng nhập
     * @param passwordHash Mật khẩu đã được mã hóa
     * @param fullName     Tên đầy đủ
     * @param email        Email
     */
    public Admin(String userId, String username, String passwordHash, String fullName, String email) {
        super(userId, username, passwordHash, fullName, email);
    }

    /**
     * Xem danh sách tất cả người dùng trong ngân hàng.
     *
     * @param bank Đối tượng Bank chứa dữ liệu
     */
    public void viewAllUsers(Bank bank) {
        System.out.println("--- All Users List ---");
        List<User> users = bank.getAllUsers();
        if (users.isEmpty()) {
            System.out.println("No users found in the system.");
        } else {
            users.forEach(System.out::println);
        }
        System.out.println("---------------------------------");
    }

    /**
     * Xem thông tin chi tiết của một người dùng cụ thể, bao gồm các tài khoản của họ.
     *
     * @param bank   Đối tượng Bank chứa dữ liệu
     * @param userId ID của người dùng cần xem
     */
    public void viewUserDetails(Bank bank, String userId) {
        User user = bank.findUserById(userId);
        if (user != null) {
            System.out.println(user); // In thông tin cơ bản của User
            user.getAccounts().forEach(System.out::println); // In thông tin từng tài khoản
        } else {
            System.out.println("User not found with ID: " + userId);
        }
    }

    /**
     * Xem tổng số tài khoản có trong ngân hàng.
     * @param bank Đối tượng Bank chứa dữ liệu.
     */
    public void viewTotalAccountCount(Bank bank) {
        List<Account> allAccounts = bank.getAllAccounts();
        System.out.println("--- Account Statistics ---");
        System.out.println("Total number of accounts in the bank: " + allAccounts.size());
        System.out.println("--------------------------");
    }

    /**
     * Xem chi tiết tất cả các tài khoản trong ngân hàng.
     * @param bank Đối tượng Bank chứa dữ liệu.
     */
    public void viewAllAccountsDetails(Bank bank) {
        System.out.println("--- All Accounts Details ---");
        List<Account> allAccounts = bank.getAllAccounts();
        if (allAccounts.isEmpty()) {
            System.out.println("No accounts found in the system.");
        } else {
            allAccounts.forEach(System.out::println);
        }
        System.out.println("---------------------------------");
    }
}