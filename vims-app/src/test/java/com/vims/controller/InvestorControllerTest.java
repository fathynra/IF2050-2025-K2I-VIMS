package com.vims.controller;

import com.vims.dao.InvestorDao;
import com.vims.model.Investor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class MockInvestorDaoForInvestorController extends InvestorDao {
    private Map<Integer, Investor> investorsDatabase = new HashMap<>();
    private int nextId = 1;


    public void addMockInvestor(Investor investor) {
        if (investor != null) {

            if (investor.getAccountId() == 0) {
                investor.setAccountId(nextId++);
            } else if (investor.getAccountId() >= nextId) {
                nextId = investor.getAccountId() + 1;
            }
            investorsDatabase.put(investor.getAccountId(), investor);
        }
    }

    public void clearDatabase() {
        investorsDatabase.clear();
        nextId = 1;
    }

    @Override
    public List<Investor> getAllInvestors() {
        if (investorsDatabase.isEmpty()) {
            return new ArrayList<>();
        }
        return new ArrayList<>(investorsDatabase.values());
    }

    @Override
    public Investor getInvestorById(int accountId) {
        return investorsDatabase.get(accountId);
    }

    @Override
    public Investor getInvestorByEmail(String email) {
        if (email == null) return null;
        return investorsDatabase.values().stream()
                .filter(inv -> email.equalsIgnoreCase(inv.getEmail()))
                .findFirst()
                .orElse(null);
    }

    @Override
    public boolean updateInvestor(Investor investor) {
        if (investor != null && investorsDatabase.containsKey(investor.getAccountId())) {
            investorsDatabase.put(investor.getAccountId(), new Investor(
                investor.getAccountId(),
                investor.getName(),
                investor.getEmail(),
                investor.getPassword(), 
                investor.getStatus(),
                investor.getRole(),
                investor.getBalance()
            ));
            return true;
        }
        return false;
    }
}


public class InvestorControllerTest {

    private InvestorController investorController;
    private MockInvestorDaoForInvestorController mockDao;

    private Investor investor1;
    private Investor investor2_banned;
    private Investor investor3_admin;

    @BeforeEach
    void setUp() {
        mockDao = new MockInvestorDaoForInvestorController();
        investorController = new InvestorController(mockDao); 

        investor1 = new Investor(1, "Alice Wonderland", "alice@vims.com", "passAlice", "active", "INVESTOR", new BigDecimal("1000.00"));
        investor2_banned = new Investor(2, "Bob The Builder", "bob@vims.com", "passBob", "banned", "INVESTOR", new BigDecimal("500.00"));
        investor3_admin = new Investor(3, "Charlie Admin", "charlie@vims.com", "passCharlie", "active", "ADMIN", new BigDecimal("0.00"));

        mockDao.addMockInvestor(investor1);
        mockDao.addMockInvestor(investor2_banned);
        mockDao.addMockInvestor(investor3_admin);
    }

    @Test
    @DisplayName("getAllInvestors should return all investors from DAO")
    void getAllInvestors_shouldReturnAllInvestors() {
        List<Investor> result = investorController.getAllInvestors();

        assertNotNull(result);
        assertEquals(3, result.size(), "Should return 3 investors");
        assertTrue(result.stream().anyMatch(inv -> inv.getEmail().equals("alice@vims.com")), "Alice should be in the list");
        assertTrue(result.stream().anyMatch(inv -> inv.getEmail().equals("bob@vims.com")), "Bob should be in the list");
        assertTrue(result.stream().anyMatch(inv -> inv.getEmail().equals("charlie@vims.com")), "Charlie should be in the list");
    }

    @Test
    @DisplayName("getAllInvestors should return empty list if DAO returns no investors")
    void getAllInvestors_shouldReturnEmptyList_whenNoInvestors() {
        mockDao.clearDatabase(); 

        List<Investor> result = investorController.getAllInvestors();

        assertNotNull(result);
        assertTrue(result.isEmpty(), "Should return an empty list");
    }

    @Test
    @DisplayName("getInvestorDetails should return investor if ID exists")
    void getInvestorDetails_shouldReturnInvestor_whenIdExists() {

        Investor result = investorController.getInvestorDetails(1);

        assertNotNull(result);
        assertEquals("Alice Wonderland", result.getName());
        assertEquals("alice@vims.com", result.getEmail());
    }

    @Test
    @DisplayName("getInvestorDetails should return null if ID does not exist")
    void getInvestorDetails_shouldReturnNull_whenIdDoesNotExist() {

        Investor result = investorController.getInvestorDetails(99); 

        assertNull(result);
    }

    @Test
    @DisplayName("banInvestor should set status to 'banned' for an active investor")
    void banInvestor_shouldSetStatusToBanned_forActiveInvestor() {
        boolean result = investorController.banInvestor(investor1.getAccountId()); 

        assertTrue(result, "Ban operation should succeed");
        Investor updatedInvestor = mockDao.getInvestorById(investor1.getAccountId());
        assertNotNull(updatedInvestor);
        assertEquals("banned", updatedInvestor.getStatus().toLowerCase(), "Investor status should be 'banned'");
    }

    @Test
    @DisplayName("banInvestor should return true if investor is already banned")
    void banInvestor_shouldReturnTrue_ifInvestorAlreadyBanned() {

        boolean result = investorController.banInvestor(investor2_banned.getAccountId()); 

        assertTrue(result, "Ban operation should still 'succeed' (idempotent)");
        Investor investor = mockDao.getInvestorById(investor2_banned.getAccountId());
        assertNotNull(investor);
        assertEquals("banned", investor.getStatus().toLowerCase(), "Investor status should remain 'banned'");
    }

    @Test
    @DisplayName("banInvestor should return false if investor ID does not exist")
    void banInvestor_shouldReturnFalse_ifInvestorIdDoesNotExist() {

        boolean result = investorController.banInvestor(99); 

        assertFalse(result, "Ban operation should fail for non-existent investor");
    }

    @Test
    @DisplayName("unbanInvestor should set status to 'active' for a banned investor")
    void unbanInvestor_shouldSetStatusToActive_forBannedInvestor() {
        boolean result = investorController.unbanInvestor(investor2_banned.getAccountId()); // Bob (banned)

        assertTrue(result, "Unban operation should succeed");
        Investor updatedInvestor = mockDao.getInvestorById(investor2_banned.getAccountId());
        assertNotNull(updatedInvestor);
        assertEquals("active", updatedInvestor.getStatus().toLowerCase(), "Investor status should be 'active'");
    }

    @Test
    @DisplayName("unbanInvestor should return true if investor is already active")
    void unbanInvestor_shouldReturnTrue_ifInvestorAlreadyActive() {
        boolean result = investorController.unbanInvestor(investor1.getAccountId()); 

        assertTrue(result, "Unban operation should still 'succeed' (idempotent)");
        Investor investor = mockDao.getInvestorById(investor1.getAccountId());
        assertNotNull(investor);
        assertEquals("active", investor.getStatus().toLowerCase(), "Investor status should remain 'active'");
    }

    @Test
    @DisplayName("unbanInvestor should return false if investor ID does not exist")
    void unbanInvestor_shouldReturnFalse_ifInvestorIdDoesNotExist() {
        boolean result = investorController.unbanInvestor(99); 

        assertFalse(result, "Unban operation should fail for non-existent investor");
    }

}