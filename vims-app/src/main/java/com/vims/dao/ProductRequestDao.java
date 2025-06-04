package com.vims.dao;

import com.vims.model.ProductRequest;
import com.vims.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductRequestDao {

    public boolean addProductRequest(ProductRequest request) {
        String sql = "INSERT INTO product_requests (idRequest, idInvestor, nameProduct, productType, reason, status) VALUES (?, ?, ?, ?, ?, ?)";
        boolean rowInserted = false;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, request.getIdRequest());
            pstmt.setInt(2, request.getIdInvestor());
            pstmt.setString(3, request.getNameProduct());
            pstmt.setString(4, request.getProductType()); 
            pstmt.setString(5, request.getReason());      
            pstmt.setString(6, request.getStatus());

            rowInserted = pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error saat menambahkan product request: " + e.getMessage());
            e.printStackTrace();
        }
        return rowInserted;
    }

    public List<ProductRequest> getProductRequestsByInvestorId(int idInvestor) {
        List<ProductRequest> requests = new ArrayList<>();
        String sql = "SELECT idRequest, idInvestor, nameProduct, productType, reason, status FROM product_requests WHERE idInvestor = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idInvestor);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    ProductRequest request = new ProductRequest();
                    request.setIdRequest(rs.getString("idRequest"));
                    request.setIdInvestor(rs.getInt("idInvestor"));
                    request.setNameProduct(rs.getString("nameProduct"));
                    request.setProductType(rs.getString("productType")); 
                    request.setReason(rs.getString("reason"));           
                    request.setStatus(rs.getString("status"));
                    requests.add(request);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error saat mengambil product requests by Investor ID (" + idInvestor + "): " + e.getMessage());
            e.printStackTrace();
        }
        return requests;
    }
    
    public List<ProductRequest> getAllProductRequests() {
        List<ProductRequest> requests = new ArrayList<>();
        String sql = "SELECT idRequest, idInvestor, nameProduct, productType, reason, status FROM product_requests"; 
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                ProductRequest request = new ProductRequest();
                request.setIdRequest(rs.getString("idRequest"));
                request.setIdInvestor(rs.getInt("idInvestor"));
                request.setNameProduct(rs.getString("nameProduct"));
                request.setProductType(rs.getString("productType")); 
                request.setReason(rs.getString("reason"));          
                request.setStatus(rs.getString("status"));
                requests.add(request);
            }
        } catch (SQLException e) {
            System.err.println("Error saat mengambil semua product requests: " + e.getMessage());
            e.printStackTrace();
        }
        return requests;
    }

    public boolean updateProductRequestStatus(String idRequest, String newStatus) {
        String sql = "UPDATE product_requests SET status = ? WHERE idRequest = ?";
        boolean rowUpdated = false;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, newStatus);
            pstmt.setString(2, idRequest);
            rowUpdated = pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error saat update status product request (" + idRequest + "): " + e.getMessage());
            e.printStackTrace();
        }
        return rowUpdated;
    }

}