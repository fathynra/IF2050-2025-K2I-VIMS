package com.vims.controller;

import com.vims.dao.ProductRequestDao;
import com.vims.model.ProductRequest;

import java.util.List;
import java.util.UUID;

public class RequestController {

    private ProductRequestDao productRequestDao;

    public RequestController() {
        this.productRequestDao = new ProductRequestDao();
    }

    public RequestController(ProductRequestDao productRequestDao) {
        this.productRequestDao = productRequestDao;
    }

    public boolean submitProductRequest(int idInvestor, String productName, String productType, String reason, String notes) {
        // notes g kepake lagi sih klo g salah, tapi karena dia g bkin error jadi biarin aja :)
        if (productName == null || productName.trim().isEmpty()) {
            System.err.println("Nama produk tidak boleh kosong.");
            return false;
        }
        if (productType == null || productType.trim().isEmpty()) {
            System.err.println("Tipe produk tidak boleh kosong.");
            return false;
        }

        String idRequest = "REQ-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        String initialStatus = "PENDING";

        ProductRequest request = new ProductRequest();
        request.setIdRequest(idRequest);
        request.setIdInvestor(idInvestor);
        request.setNameProduct(productName);
        request.setProductType(productType); 
        request.setReason(reason);           
        request.setStatus(initialStatus);

        return productRequestDao.addProductRequest(request);
    }

    public List<ProductRequest> getProductRequestsByInvestor(int idInvestor) {
        return productRequestDao.getProductRequestsByInvestorId(idInvestor);
    }

    public List<ProductRequest> getAllPendingProductRequests() {
        List<ProductRequest> allRequests = productRequestDao.getAllProductRequests();
        allRequests.removeIf(request -> !"PENDING".equalsIgnoreCase(request.getStatus()));
        return allRequests;
    }
    
    public boolean approveProductRequest(String idRequest) {
        return productRequestDao.updateProductRequestStatus(idRequest, "APPROVED");
    }

    public boolean rejectProductRequest(String idRequest) {
        return productRequestDao.updateProductRequestStatus(idRequest, "REJECTED");
    }

}