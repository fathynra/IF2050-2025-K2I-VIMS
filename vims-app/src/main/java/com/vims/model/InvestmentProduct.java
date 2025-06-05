package com.vims.model;

import java.math.BigDecimal;

public class InvestmentProduct {

    private int productId;
    private String name;
    private String type;
    private String riskLevel;
    private String description;
    private BigDecimal unitPrice;

    public InvestmentProduct() {
    }

    public InvestmentProduct(int productId, String name, String type, String riskLevel, 
                             String description,  BigDecimal unitPrice) {
        this.productId = productId;
        this.name = name;
        this.type = type;
        this.riskLevel = riskLevel;
        this.description = description;

        this.unitPrice = unitPrice;
    }


    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getRiskLevel() { return riskLevel; }
    public void setRiskLevel(String riskLevel) { this.riskLevel = riskLevel; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public BigDecimal getUnitPrice() { return unitPrice; }
    public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }


    @Override
    public String toString() {
        return "InvestmentProduct{" +
               "productId=" + productId +
               ", name='" + name + '\'' +
               ", type='" + type + '\'' +
               ", riskLevel='" + riskLevel + '\'' +
               ", description='" + description + '\'' +
               ", unitPrice=" + unitPrice +
               '}';
    }
}