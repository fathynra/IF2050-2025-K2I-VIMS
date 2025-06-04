package com.vims.controller;

import com.vims.dao.TransactionDao;
import com.vims.dao.InvestorDao; 
import com.vims.dao.InvestmentProductDao; 
import com.vims.dao.InvestorInvestmentDao; 

import com.vims.model.Transaction;
import com.vims.model.Investor;
import com.vims.model.InvestmentProduct;
import com.vims.model.InvestorInvestment;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class TransactionController {

    private TransactionDao transactionDao;
    private InvestorDao investorDao;
    private InvestmentProductDao productDao;
    private InvestorInvestmentDao investorInvestmentDao;

    public TransactionController() {
        this.transactionDao = new TransactionDao();
        this.investorDao = new InvestorDao();
        this.productDao = new InvestmentProductDao();
        this.investorInvestmentDao = new InvestorInvestmentDao();
    }

    public TransactionController(TransactionDao transactionDao, InvestorDao investorDao, 
                                 InvestmentProductDao productDao, InvestorInvestmentDao investorInvestmentDao) {
        this.transactionDao = transactionDao;
        this.investorDao = investorDao;
        this.productDao = productDao;
        this.investorInvestmentDao = investorInvestmentDao;
    }

    public boolean buyProduct(int investorId, int productId, int quantity) {
        if (quantity <= 0) {
            System.err.println("Kuantitas pembelian harus lebih dari 0.");
            return false;
        }

        Investor investor = investorDao.getInvestorById(investorId);
        InvestmentProduct product = productDao.getInvestmentProductById(productId);

        if (investor == null) {
            System.err.println("Pembelian gagal: Investor dengan ID " + investorId + " tidak ditemukan.");
            return false;
        }
        if (product == null) {
            System.err.println("Pembelian gagal: Produk dengan ID " + productId + " tidak ditemukan.");
            return false;
        }

        BigDecimal unitPrice = product.getUnitPrice();
        BigDecimal totalCost = unitPrice.multiply(new BigDecimal(quantity));

        if (investor.getBalance().compareTo(totalCost) < 0) {
            System.err.println("Pembelian gagal: miskin.");
            return false; 
        }

        BigDecimal newBalance = investor.getBalance().subtract(totalCost);
        investor.setBalance(newBalance);
        boolean investorUpdated = investorDao.updateInvestor(investor); 
        
        InvestorInvestment currentOwnership = investorInvestmentDao.getInvestorInvestment(investorId, productId);
        int newOwnedQuantity;
        if (currentOwnership != null) {
            newOwnedQuantity = currentOwnership.getQuantityOwned() + quantity;
        } else {
            newOwnedQuantity = quantity;
        }
        InvestorInvestment newOwnership = new InvestorInvestment(investorId, productId, newOwnedQuantity);
        boolean ownershipUpdated = investorInvestmentDao.saveOrUpdateInvestorInvestment(newOwnership);


        Transaction transaction = new Transaction();
        transaction.setDatetime(LocalDateTime.now());
        transaction.setQuantity(quantity);
        transaction.setType("buy"); 
        transaction.setAccountId(investorId);
        transaction.setProductId(productId);
        boolean transactionAdded = transactionDao.addTransaction(transaction);

        if (investorUpdated && ownershipUpdated && transactionAdded) { 
            System.out.println("Pembelian berhasil untuk investor ID " + investorId + " produk ID " + productId);
            return true;
        }
        if (ownershipUpdated && transactionAdded) {
            System.out.println("Pembelian berhasil untuk investor ID " + investorId + " produk ID " + productId);
            return true; 
        } else {
            System.err.println("Pembelian gagal pada salah satu langkah penyimpanan data.");
            return false;
        }
    }

    public boolean sellProduct(int investorId, int productId, int quantity) {
        if (quantity <= 0) {
            System.err.println("Kuantitas penjualan harus lebih dari 0.");
            return false;
        }

        Investor investor = investorDao.getInvestorById(investorId);
        InvestmentProduct product = productDao.getInvestmentProductById(productId);
        InvestorInvestment currentOwnership = investorInvestmentDao.getInvestorInvestment(investorId, productId);

        if (investor == null) {
            System.err.println("Penjualan gagal: Investor dengan ID " + investorId + " tidak ditemukan.");
            return false;
        }
        if (product == null) {
            System.err.println("Penjualan gagal: Produk dengan ID " + productId + " tidak ditemukan.");
            return false;
        }
        if (currentOwnership == null || currentOwnership.getQuantityOwned() < quantity) {
            System.err.println("Penjualan gagal: Investor g ada unit."); 
            return false;
        }

        BigDecimal unitPrice = product.getUnitPrice();
        BigDecimal totalRevenue = unitPrice.multiply(new BigDecimal(quantity));

        BigDecimal newBalance = investor.getBalance().add(totalRevenue);
        investor.setBalance(newBalance);
        boolean investorUpdated = investorDao.updateInvestor(investor); 

        int newOwnedQuantity = currentOwnership.getQuantityOwned() - quantity;
        InvestorInvestment updatedOwnership = new InvestorInvestment(investorId, productId, newOwnedQuantity);
        boolean ownershipUpdated = investorInvestmentDao.saveOrUpdateInvestorInvestment(updatedOwnership); 

        Transaction transaction = new Transaction();
        transaction.setDatetime(LocalDateTime.now());
        transaction.setQuantity(quantity);
        transaction.setType("sell"); 
        transaction.setAccountId(investorId);
        transaction.setProductId(productId);
        boolean transactionAdded = transactionDao.addTransaction(transaction);

        if (investorUpdated && ownershipUpdated && transactionAdded) { 
            System.out.println("Pembelian berhasil untuk investor ID " + investorId + " produk ID " + productId);
            return true;
        }

        if (ownershipUpdated && transactionAdded) {
            System.out.println("Penjualan berhasil untuk investor ID " + investorId + " produk ID " + productId);
            return true; 
        } else {
            System.err.println("Penjualan gagal pada salah satu langkah penyimpanan data.");
            return false;
        }
    }
    public List<Transaction> getTransactionHistory(int investorId) {
        return transactionDao.getTransactionsByAccountId(investorId);
    }

    public List<Transaction> getAllTransactionsInSystem() {
        return transactionDao.getAllTransactionsInSystem();
    }

}