package com.vims.controller;

import com.vims.dao.InvestorDao;
import com.vims.dao.InvestmentProductDao;
import com.vims.dao.InvestorInvestmentDao;
import com.vims.dao.TransactionDao;
import com.vims.model.InvestmentProduct;
import com.vims.model.Investor;
import com.vims.model.InvestorInvestment;
import com.vims.model.Transaction;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;


class MockTransactionDaoForTest extends TransactionDao {
    public boolean addTransactionCalled = false;
    public Transaction lastAddedTransaction = null;

    @Override
    public boolean addTransaction(Transaction transaction) {
        addTransactionCalled = true;
        lastAddedTransaction = transaction;
        if (transaction.getTransactionId() == 0) {
            transaction.setTransactionId(1); // ID dummy
        }
        return true;
    }
    @Override
    public List<Transaction> getTransactionsByAccountId(int accountId) {
        return new ArrayList<>();
    }

    @Override
    public List<Transaction> getAllTransactionsInSystem() {
        return new ArrayList<>();
    }
}

class MockInvestorDaoForTest extends InvestorDao {
    private Map<Integer, Investor> investors = new HashMap<>();
    public boolean updateInvestorCalled = false;
    public Investor lastUpdatedInvestor = null;

    public void addInvestor(Investor investor) {
        investors.put(investor.getAccountId(), investor);
    }

    @Override
    public Investor getInvestorById(int accountId) {
        return investors.get(accountId);
    }

    @Override
    public boolean updateInvestor(Investor investor) {
        updateInvestorCalled = true;
        lastUpdatedInvestor = investor;
        if (investors.containsKey(investor.getAccountId())) {
            investors.put(investor.getAccountId(), investor);
            return true;
        }
        return false;
    }
    @Override
    public Investor getInvestorByEmail(String email) { return null;}
    @Override
    public List<Investor> getAllInvestors() { return new ArrayList<>(investors.values()); }
}

class MockProductDaoForTest extends InvestmentProductDao {
    private Map<Integer, InvestmentProduct> products = new HashMap<>();

    public void addProduct(InvestmentProduct product) {
        products.put(product.getProductId(), product);
    }

    @Override
    public InvestmentProduct getInvestmentProductById(int productId) {
        return products.get(productId);
    }
     @Override
    public List<InvestmentProduct> getAllInvestmentProducts() {return new ArrayList<>(products.values());}
    @Override
    public boolean addInvestmentProduct(InvestmentProduct product) { return false; }
}

class MockInvestorInvestmentDaoForTest extends InvestorInvestmentDao {
    private Map<String, InvestorInvestment> holdings = new HashMap<>();
    public boolean saveOrUpdateCalled = false;
    public InvestorInvestment lastSavedOrUpdatedHolding = null;

    private String getKey(int accountId, int productId) {
        return accountId + "-" + productId;
    }

    @Override
    public InvestorInvestment getInvestorInvestment(int accountId, int productId) {
        return holdings.get(getKey(accountId, productId));
    }

    @Override
    public boolean saveOrUpdateInvestorInvestment(InvestorInvestment investment) {
        saveOrUpdateCalled = true;
        lastSavedOrUpdatedHolding = investment;
        holdings.put(getKey(investment.getAccountId(), investment.getProductId()), investment);
        return true;
    }
    @Override
    public List<InvestorInvestment> getInvestmentsByAccountId(int accountId) { return new ArrayList<>(); }
    @Override
    public boolean deleteInvestorInvestment(int accountId, int productId) { return true; }
}

public class TransactionControllerTest {

    private TransactionController transactionController;
    private MockTransactionDaoForTest mockTransactionDao;
    private MockInvestorDaoForTest mockInvestorDao;
    private MockProductDaoForTest mockProductDao;
    private MockInvestorInvestmentDaoForTest mockInvestorInvestmentDao;

    private Investor testInvestor;
    private InvestmentProduct testProduct;

    @BeforeEach
    void setUp() {
        mockTransactionDao = new MockTransactionDaoForTest();
        mockInvestorDao = new MockInvestorDaoForTest();
        mockProductDao = new MockProductDaoForTest();
        mockInvestorInvestmentDao = new MockInvestorInvestmentDaoForTest();

        transactionController = new TransactionController(
            mockTransactionDao, 
            mockInvestorDao, 
            mockProductDao, 
            mockInvestorInvestmentDao
        );

        testInvestor = new Investor(1, "Test Buyer", "buyer@vims.com", "password", "active", "INVESTOR", new BigDecimal("1000.00"));
        mockInvestorDao.addInvestor(testInvestor);
        testProduct = new InvestmentProduct(101, "Saham Super", "stock", "Tinggi", "Saham perusahaan super", new BigDecimal("100.00"));
        mockProductDao.addProduct(testProduct);
    }

    @Test
    @DisplayName("Pembelian Produk Berhasil jika Saldo Cukup dan Produk Ada")
    void buyProduct_shouldSucceed_whenBalanceIsSufficientAndProductExists() {
        int investorId = testInvestor.getAccountId();
        int productId = testProduct.getProductId();
        int quantityToBuy = 2;

        boolean result = transactionController.buyProduct(investorId, productId, quantityToBuy);
        assertTrue(result, "Pembelian seharusnya berhasil.");
        
        BigDecimal expectedBalance = new BigDecimal("1000.00").subtract(new BigDecimal("100.00").multiply(new BigDecimal(quantityToBuy)));
        assertNotNull(mockInvestorDao.lastUpdatedInvestor, "Investor seharusnya diupdate.");
        assertEquals(0, expectedBalance.compareTo(mockInvestorDao.lastUpdatedInvestor.getBalance()), "Saldo investor seharusnya berkurang sesuai total pembelian.");
        
        assertTrue(mockTransactionDao.addTransactionCalled, "Metode addTransaction di DAO seharusnya dipanggil.");
        assertNotNull(mockTransactionDao.lastAddedTransaction, "Objek transaksi seharusnya dibuat.");
        assertEquals(quantityToBuy, mockTransactionDao.lastAddedTransaction.getQuantity());
        assertEquals("buy", mockTransactionDao.lastAddedTransaction.getType());


        assertTrue(mockInvestorInvestmentDao.saveOrUpdateCalled, "Metode saveOrUpdate di InvestorInvestmentDao seharusnya dipanggil.");
        assertNotNull(mockInvestorInvestmentDao.lastSavedOrUpdatedHolding, "Data kepemilikan seharusnya disimpan/diupdate.");
        assertEquals(quantityToBuy, mockInvestorInvestmentDao.lastSavedOrUpdatedHolding.getQuantityOwned());
    }

    @Test
    @DisplayName("Pembelian Produk Gagal jika Saldo Tidak Cukup")
    void buyProduct_shouldFail_whenBalanceIsInsufficient() {

        testInvestor.setBalance(new BigDecimal("50.00")); 
        mockInvestorDao.addInvestor(testInvestor); 
        
        int investorId = testInvestor.getAccountId();
        int productId = testProduct.getProductId();
        int quantityToBuy = 2; 

        boolean result = transactionController.buyProduct(investorId, productId, quantityToBuy);

        assertFalse(result, "Pembelian seharusnya gagal karena saldo tidak cukup.");
        assertFalse(mockTransactionDao.addTransactionCalled, "addTransaction seharusnya tidak dipanggil.");
        assertEquals(0, new BigDecimal("50.00").compareTo(mockInvestorDao.getInvestorById(investorId).getBalance()), "Saldo investor seharusnya tidak berubah jika pembelian gagal karena tidak cukup saldo.");
    }

    @Test
    @DisplayName("Pembelian Produk Gagal jika Produk Tidak Ditemukan")
    void buyProduct_shouldFail_whenProductNotFound() {
        int investorId = testInvestor.getAccountId();
        int nonExistentProductId = 999;
        int quantityToBuy = 1;
        
        boolean result = transactionController.buyProduct(investorId, nonExistentProductId, quantityToBuy);
        assertFalse(result, "Pembelian seharusnya gagal jika produk tidak ditemukan.");
    }

    @Test
    @DisplayName("Pembelian Produk Gagal jika Investor Tidak Ditemukan")
    void buyProduct_shouldFail_whenInvestorNotFound() {
        int nonExistentInvestorId = 999;
        int productId = testProduct.getProductId();
        int quantityToBuy = 1;

        boolean result = transactionController.buyProduct(nonExistentInvestorId, productId, quantityToBuy);
        assertFalse(result, "Pembelian seharusnya gagal jika investor tidak ditemukan.");
    }

    @Test
    @DisplayName("Pembelian Produk Gagal jika Kuantitas Tidak Valid (<=0)")
    void buyProduct_shouldFail_whenQuantityIsInvalid() {
        int investorId = testInvestor.getAccountId();
        int productId = testProduct.getProductId();
        
        boolean resultZero = transactionController.buyProduct(investorId, productId, 0);
        assertFalse(resultZero, "Pembelian seharusnya gagal untuk kuantitas 0.");

        boolean resultNegative = transactionController.buyProduct(investorId, productId, -1);
        assertFalse(resultNegative, "Pembelian seharusnya gagal untuk kuantitas negatif.");
    }
}