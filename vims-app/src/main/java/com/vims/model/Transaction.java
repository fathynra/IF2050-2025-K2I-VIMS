package com.vims.model;


import java.time.LocalDateTime; 

public class Transaction {

    private int transactionId;
    private LocalDateTime datetime; 
    private int quantity;
    private String type; 
    private int accountId; 
    private int productId;

    public Transaction() {
    }

    public Transaction(int transactionId, LocalDateTime datetime, int quantity, String type, int accountId, int productId) {
        this.transactionId = transactionId;
        this.datetime = datetime;
        this.quantity = quantity;
        this.type = type;
        this.accountId = accountId;
        this.productId = productId;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public LocalDateTime getDatetime() {
        return datetime;
    }

    public void setDatetime(LocalDateTime datetime) {
        this.datetime = datetime;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    @Override
    public String toString() {
        return "Transaction{" +
               "transactionId=" + transactionId +
               ", datetime=" + datetime +
               ", quantity=" + quantity +
               ", type='" + type + '\'' +
               ", accountId=" + accountId +
               ", productId=" + productId +
               '}';
    }
}