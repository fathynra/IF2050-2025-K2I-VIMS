package com.vims.dao;

import com.vims.model.InvestmentProduct;
import com.vims.util.DatabaseConnection; 

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement; 
import java.util.ArrayList;
import java.util.List;

public class InvestmentProductDao {


    public List<InvestmentProduct> getAllInvestmentProducts() {
        List<InvestmentProduct> products = new ArrayList<>();
        String sql = "SELECT product_id, name, type, risk_level, description, unit_price FROM investment_product"; 

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                InvestmentProduct product = new InvestmentProduct();
                product.setProductId(rs.getInt("product_id"));
                product.setName(rs.getString("name"));
                product.setType(rs.getString("type"));
                product.setRiskLevel(rs.getString("risk_level"));
                product.setDescription(rs.getString("description"));
                product.setUnitPrice(rs.getBigDecimal("unit_price"));
                products.add(product);
            }
        } catch (SQLException e) {
            System.err.println("Error saat mengambil semua produk investasi: " + e.getMessage());
            e.printStackTrace();
        }
        return products;
    }

    public InvestmentProduct getInvestmentProductById(int productId) {
        InvestmentProduct product = null;
        String sql = "SELECT product_id, name, type, risk_level, description, unit_price FROM investment_product WHERE product_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, productId); 
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    product = new InvestmentProduct();
                    product.setProductId(rs.getInt("product_id"));
                    product.setName(rs.getString("name"));
                    product.setType(rs.getString("type"));
                    product.setRiskLevel(rs.getString("risk_level")); 
                    product.setDescription(rs.getString("description"));
                    product.setUnitPrice(rs.getBigDecimal("unit_price"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error saat mengambil produk investasi by ID ("+ productId +"): " + e.getMessage());
            e.printStackTrace();
        }
        return product;
    }

    public boolean addInvestmentProduct(InvestmentProduct product) {
        // product_id yg disini auto nambah
        String sql = "INSERT INTO investment_product (name, type, risk_level, description, unit_price) VALUES (?, ?, ?, ?, ?)";
        boolean rowInserted = false;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, product.getName());
            pstmt.setString(2, product.getType());
            pstmt.setString(3, product.getRiskLevel());
            pstmt.setString(4, product.getDescription());
            pstmt.setBigDecimal(5, product.getUnitPrice());

            rowInserted = pstmt.executeUpdate() > 0;

            if (rowInserted) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        product.setProductId(generatedKeys.getInt(1)); // Set ID yang di-generate ke objek
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error saat menambahkan produk investasi: " + e.getMessage());
            e.printStackTrace();
        }
        return rowInserted;
    }

}