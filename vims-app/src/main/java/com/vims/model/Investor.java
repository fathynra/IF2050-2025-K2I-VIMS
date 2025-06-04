package com.vims.model;

import java.math.BigDecimal;

public class Investor { 

    private int accountId;
    private String name;
    private String email;    // yg ini buat login
    private String password; 
    private String status;
    private String role;   
    private BigDecimal balance;

    public Investor() {
    }

    public Investor(int accountId, String name, String email, String password, String status, String role, BigDecimal balance) {
        this.accountId = accountId;
        this.name = name;
        this.email = email;
        this.password = password;
        this.status = status;
        this.role = role;
        this.balance = balance;
    }


    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "Investor{" +
               "accountId=" + accountId +
               ", name='" + name + '\'' +
               ", email='" + email + '\'' +
               // Jangan sertakan password di toString() untuk keamanan
               ", status='" + status + '\'' +
               ", role='" + role + '\'' +
               ", balance=" + balance +
               '}';
    }
}