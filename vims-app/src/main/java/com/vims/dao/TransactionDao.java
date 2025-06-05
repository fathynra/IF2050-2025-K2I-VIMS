package com.vims.dao;

import com.vims.model.Transaction;
import com.vims.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp; 
import java.util.ArrayList;
import java.util.List;

public class TransactionDao {
    public boolean addTransaction(Transaction transaction) {
        String sql = "INSERT INTO transactions (datetime, quantity, type, account_id, product_id) VALUES (?, ?, ?, ?, ?)";
        boolean rowInserted = false;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setTimestamp(1, Timestamp.valueOf(transaction.getDatetime())); 
            pstmt.setInt(2, transaction.getQuantity());
            pstmt.setString(3, transaction.getType());
            pstmt.setInt(4, transaction.getAccountId());
            pstmt.setInt(5, transaction.getProductId());

            rowInserted = pstmt.executeUpdate() > 0;

            if (rowInserted) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        transaction.setTransactionId(generatedKeys.getInt(1));
                    }
                }
            }

        } catch (SQLException e) {
            System.err.println("Error saat menambahkan transaksi: " + e.getMessage());
            e.printStackTrace();
        }
        return rowInserted;
    }

    public List<Transaction> getTransactionsByAccountId(int accountId) {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT transaction_id, datetime, quantity, type, account_id, product_id FROM transactions WHERE account_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, accountId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Transaction transaction = new Transaction();
                    transaction.setTransactionId(rs.getInt("transaction_id"));
                    Timestamp ts = rs.getTimestamp("datetime");
                    if (ts != null) {
                        transaction.setDatetime(ts.toLocalDateTime());
                    }
                    transaction.setQuantity(rs.getInt("quantity"));
                    transaction.setType(rs.getString("type"));
                    transaction.setAccountId(rs.getInt("account_id"));
                    transaction.setProductId(rs.getInt("product_id"));
                    transactions.add(transaction);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error saat mengambil transaksi by Account ID (" + accountId + "): " + e.getMessage());
            e.printStackTrace();
        }
        return transactions;
    }

    public List<Transaction> getAllTransactionsInSystem() {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT transaction_id, datetime, quantity, type, account_id, product_id FROM transactions ORDER BY datetime DESC";

        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql); 
            ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Transaction transaction = new Transaction();
                transaction.setTransactionId(rs.getInt("transaction_id"));
                Timestamp ts = rs.getTimestamp("datetime");
                if (ts != null) {
                    transaction.setDatetime(ts.toLocalDateTime());
                }
                transaction.setQuantity(rs.getInt("quantity"));
                transaction.setType(rs.getString("type"));
                transaction.setAccountId(rs.getInt("account_id"));
                transaction.setProductId(rs.getInt("product_id"));
                transactions.add(transaction);
            }
        } catch (SQLException e) {
            System.err.println("Error saat mengambil semua transaksi sistem: " + e.getMessage());
            e.printStackTrace();
        }
        return transactions;
    }

}