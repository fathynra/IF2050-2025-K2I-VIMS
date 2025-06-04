package com.vims.controller;

import com.vims.dao.InvestorDao;
import com.vims.model.Investor; 

public class LoginController {

    private InvestorDao investorDao;
    private static Investor currentLoggedInUser; 

    public LoginController() {
        this.investorDao = new InvestorDao();
    }

    public LoginController(InvestorDao investorDao) {
        this.investorDao = investorDao;
    }

    public Investor login(String email, String password) {
        if (email == null || email.trim().isEmpty() || password == null || password.isEmpty()) {
            System.err.println("Email dan password tidak boleh kosong.");
            return null;
        }

        Investor investor = investorDao.getInvestorByEmail(email.trim());

        if (investor != null) {
            if (investor.getPassword().equals(password)) {
                if ("banned".equalsIgnoreCase(investor.getStatus())) {
                    System.err.println("Login gagal: Akun " + email + " telah diduar.");
                    currentLoggedInUser = null; 
                    return null;
                }
                currentLoggedInUser = investor; 
                System.out.println("Login berhasil untuk: " + investor.getName() + " sebagai " + investor.getRole());
                return investor;
            } else {
                System.err.println("Login gagal: Password no right untuk email " + email);
            }
        } else {
            System.err.println("Login gagal: Email " + email + " fiktif.");
        }
        currentLoggedInUser = null; 
        return null;
    }

    public static Investor getCurrentLoggedInUser() {
        return currentLoggedInUser;
    }

    public void logout() {
        System.out.println("User " + (currentLoggedInUser != null ? currentLoggedInUser.getName() : "N/A") + " telah logout.");
        currentLoggedInUser = null;
    }

}