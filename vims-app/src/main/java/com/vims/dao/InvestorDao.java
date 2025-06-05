package com.vims.dao;

import com.vims.model.Investor;
import com.vims.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class InvestorDao {

    public List<Investor> getAllInvestors() {
        List<Investor> investors = new ArrayList<>();
        String sql = "SELECT account_id, name, email, password, status, role, balance FROM investor"; 

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Investor investor = new Investor();
                investor.setAccountId(rs.getInt("account_id"));
                investor.setName(rs.getString("name"));
                investor.setEmail(rs.getString("email"));
                investor.setPassword(rs.getString("password")); 
                investor.setStatus(rs.getString("status"));
                investor.setRole(rs.getString("role"));         
                investor.setBalance(rs.getBigDecimal("balance"));
                investors.add(investor);
            }
        } catch (SQLException e) {
            System.err.println("Error saat mengambil semua investor: " + e.getMessage());
            e.printStackTrace();
        }
        return investors;
    }

    public Investor getInvestorById(int accountId) {
        Investor investor = null;

        String sql = "SELECT account_id, name, email, password, status, role, balance FROM investor WHERE account_id = ?"; 

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, accountId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    investor = new Investor();
                    investor.setAccountId(rs.getInt("account_id"));
                    investor.setName(rs.getString("name"));
                    investor.setEmail(rs.getString("email"));
                    investor.setPassword(rs.getString("password")); 
                    investor.setStatus(rs.getString("status"));
                    investor.setRole(rs.getString("role"));         
                    investor.setBalance(rs.getBigDecimal("balance"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error saat mengambil investor by ID (" + accountId + "): " + e.getMessage());
            e.printStackTrace();
        }
        return investor;
    }

    public Investor getInvestorByEmail(String email) {
        Investor investor = null;
        String sql = "SELECT account_id, name, email, password, status, role, balance FROM investor WHERE email = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, email);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    investor = new Investor();
                    investor.setAccountId(rs.getInt("account_id"));
                    investor.setName(rs.getString("name"));
                    investor.setEmail(rs.getString("email"));
                    investor.setPassword(rs.getString("password")); 
                    investor.setStatus(rs.getString("status"));
                    investor.setRole(rs.getString("role"));         
                    investor.setBalance(rs.getBigDecimal("balance"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error saat mengambil investor by email (" + email + "): " + e.getMessage());
            e.printStackTrace();
        }
        return investor;
    }

    public boolean updateInvestor(Investor investor) {
        String sql = "UPDATE investor SET name = ?, email = ?, password = ?, status = ?, role = ?, balance = ? WHERE account_id = ?";
        boolean rowUpdated = false;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, investor.getName());
            pstmt.setString(2, investor.getEmail());
            pstmt.setString(3, investor.getPassword()); 
            pstmt.setString(4, investor.getStatus());
            pstmt.setString(5, investor.getRole());     
            pstmt.setBigDecimal(6, investor.getBalance());
            pstmt.setInt(7, investor.getAccountId());

            rowUpdated = pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error saat update investor dengan ID (" + investor.getAccountId() + "): " + e.getMessage());
            e.printStackTrace();
        }
        return rowUpdated;
    }

}