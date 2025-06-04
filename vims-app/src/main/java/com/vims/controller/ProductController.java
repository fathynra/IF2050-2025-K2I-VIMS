package com.vims.controller;

import com.vims.dao.InvestmentProductDao;
import com.vims.model.InvestmentProduct;

import java.math.BigDecimal; 
import java.util.List;
import java.util.stream.Collectors;
import java.util.Arrays; 

public class ProductController {

    private InvestmentProductDao productDao;
    private static final List<String> VALID_PRODUCT_TYPES = Arrays.asList("stock", "bond", "real estate");

    public ProductController() {
        this.productDao = new InvestmentProductDao();
    }

    public ProductController(InvestmentProductDao productDao) {
        this.productDao = productDao;
    }

    public List<InvestmentProduct> getAllInvestmentProducts() {
        return productDao.getAllInvestmentProducts();
    }

    public InvestmentProduct getInvestmentProductById(int productId) {
        return productDao.getInvestmentProductById(productId);
    }

    public List<InvestmentProduct> searchProductsByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return getAllInvestmentProducts();
        }
        String lowerCaseName = name.toLowerCase();
        return productDao.getAllInvestmentProducts().stream()
                .filter(product -> product.getName().toLowerCase().contains(lowerCaseName))
                .collect(Collectors.toList());
    }

    public boolean addNewProduct(String name, String type, String riskLevel, String description, BigDecimal unitPrice) {

        if (name == null || name.trim().isEmpty() || 
            type == null || type.trim().isEmpty() || 
            unitPrice == null || unitPrice.compareTo(BigDecimal.ZERO) < 0 ) {
            System.err.println("Input tidak valid: Nama, tipe, harga, tidak boleh kosong/negatif.");
            return false;
        }

        String lowerCaseType = type.toLowerCase().trim();
        if (!VALID_PRODUCT_TYPES.contains(lowerCaseType)) {
            System.err.println("Tipe produk tidak valid: '" + type + "'. Harus salah satu dari: " + VALID_PRODUCT_TYPES);
            return false;
        }

        InvestmentProduct newProduct = new InvestmentProduct();
        newProduct.setName(name);
        newProduct.setType(lowerCaseType); 
        newProduct.setRiskLevel(riskLevel);
        newProduct.setDescription(description);
        newProduct.setUnitPrice(unitPrice);

        return productDao.addInvestmentProduct(newProduct);
    }

}