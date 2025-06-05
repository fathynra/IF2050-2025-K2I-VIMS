package com.vims.model;

public class InvestorInvestment {

    private int accountId; 
    private int productId; 
    private int quantityOwned;

    public InvestorInvestment() {
    }

    public InvestorInvestment(int accountId, int productId, int quantityOwned) {
        this.accountId = accountId;
        this.productId = productId;
        this.quantityOwned = quantityOwned;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getQuantityOwned() {
        return quantityOwned;
    }

    public void setQuantityOwned(int quantityOwned) {
        this.quantityOwned = quantityOwned;
    }

    @Override
    public String toString() {
        return "InvestorInvestment{" +
               "accountId=" + accountId +
               ", productId=" + productId +
               ", quantityOwned=" + quantityOwned +
               '}';
    }
}