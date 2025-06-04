package com.vims.dao;

import com.vims.model.InvestorInvestment;
import com.vims.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class InvestorInvestmentDao {

    public boolean saveOrUpdateInvestorInvestment(InvestorInvestment investment) {
        InvestorInvestment existing = getInvestorInvestment(investment.getAccountId(), investment.getProductId());
        String sql;
        boolean success = false;

        if (existing != null) {
            int newQuantity = investment.getQuantityOwned(); 

            if (newQuantity < 0) {
                System.err.println("Kuantitas tidak boleh negatif. Operasi update dibatalkan.");
                return false; 
            } else if (newQuantity == 0) {
                return deleteInvestorInvestment(investment.getAccountId(), investment.getProductId());
            }
            sql = "UPDATE investor_investment SET quantity_owned = ? WHERE account_id = ? AND product_id = ?";
             try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, newQuantity);
                pstmt.setInt(2, investment.getAccountId());
                pstmt.setInt(3, investment.getProductId());
                success = pstmt.executeUpdate() > 0;
            } catch (SQLException e) {
                System.err.println("Error saat update investor_investment: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            if (investment.getQuantityOwned() <= 0) {
                 System.err.println("Kuantitas untuk investasi baru harus lebih dari 0.");
                 return false;
            }
            sql = "INSERT INTO investor_investment (account_id, product_id, quantity_owned) VALUES (?, ?, ?)";
            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, investment.getAccountId());
                pstmt.setInt(2, investment.getProductId());
                pstmt.setInt(3, investment.getQuantityOwned());
                success = pstmt.executeUpdate() > 0;
            } catch (SQLException e) {
                System.err.println("Error saat insert investor_investment: " + e.getMessage());
                e.printStackTrace();
            }
        }
        return success;
    }

    public InvestorInvestment getInvestorInvestment(int accountId, int productId) {
        InvestorInvestment investment = null;
        String sql = "SELECT account_id, product_id, quantity_owned FROM investor_investment WHERE account_id = ? AND product_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, accountId);
            pstmt.setInt(2, productId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    investment = new InvestorInvestment();
                    investment.setAccountId(rs.getInt("account_id"));
                    investment.setProductId(rs.getInt("product_id"));
                    investment.setQuantityOwned(rs.getInt("quantity_owned"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error saat mengambil investor_investment by ID: " + e.getMessage());
            e.printStackTrace();
        }
        return investment;
    }

    public List<InvestorInvestment> getInvestmentsByAccountId(int accountId) {
        List<InvestorInvestment> investments = new ArrayList<>();
        String sql = "SELECT account_id, product_id, quantity_owned FROM investor_investment WHERE account_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, accountId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    InvestorInvestment investment = new InvestorInvestment();
                    investment.setAccountId(rs.getInt("account_id"));
                    investment.setProductId(rs.getInt("product_id"));
                    investment.setQuantityOwned(rs.getInt("quantity_owned"));
                    investments.add(investment);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error saat mengambil semua investasi by Account ID (" + accountId + "): " + e.getMessage());
            e.printStackTrace();
        }
        return investments;
    }

    public boolean deleteInvestorInvestment(int accountId, int productId) {
        String sql = "DELETE FROM investor_investment WHERE account_id = ? AND product_id = ?";
        boolean rowDeleted = false;
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, accountId);
            pstmt.setInt(2, productId);
            rowDeleted = pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error saat menghapus investor_investment: " + e.getMessage());
            e.printStackTrace();
        }
        return rowDeleted;
    }

}