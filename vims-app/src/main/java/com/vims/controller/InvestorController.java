package com.vims.controller;

import com.vims.dao.InvestorDao;
import com.vims.model.Investor;

import java.util.List;
import java.util.ArrayList; 

public class InvestorController {

    private InvestorDao investorDao;

    public InvestorController() {
        this.investorDao = new InvestorDao(); 
    }

    public InvestorController(InvestorDao investorDao) {
        this.investorDao = investorDao;
    }


    public List<Investor> getAllInvestors() {
        List<Investor> investors = investorDao.getAllInvestors();
        return investors != null ? investors : new ArrayList<>(); 
    }

    public Investor getInvestorDetails(int accountId) {
        return investorDao.getInvestorById(accountId);
    }

    public boolean banInvestor(int accountId) {
        Investor investor = investorDao.getInvestorById(accountId);
        if (investor != null) {
            if ("banned".equalsIgnoreCase(investor.getStatus())) {
                System.out.println("Investor ID " + accountId + " sudah dalam status 'banned'.");
                return true; 
            }
            investor.setStatus("banned");
            return investorDao.updateInvestor(investor); 
        }
        System.err.println("Gagal memblokir: Investor dengan ID " + accountId + " tidak ditemukan.");
        return false;
    }

    public boolean unbanInvestor(int accountId) {
        Investor investor = investorDao.getInvestorById(accountId);
        if (investor != null) {
             if ("active".equalsIgnoreCase(investor.getStatus())) {
                System.out.println("Investor ID " + accountId + " sudah dalam status 'active'.");
                return true; 
            }
            investor.setStatus("active");
            return investorDao.updateInvestor(investor);
        }
        System.err.println("Gagal mengaktifkan: Investor dengan ID " + accountId + " tidak ditemukan.");
        return false;
    }

}