package com.vims.model;

public class ProductRequest {

    private String idRequest;
    private int idInvestor; 
    private String nameProduct;
    private String productType; 
    private String reason;      
    private String status;


    public ProductRequest() {
    }

    public ProductRequest(String idRequest, int idInvestor, String nameProduct, 
                          String productType, String reason, String status) {
        this.idRequest = idRequest;
        this.idInvestor = idInvestor;
        this.nameProduct = nameProduct;
        this.productType = productType; 
        this.reason = reason;           
        this.status = status;
    }


    public String getIdRequest() {
        return idRequest;
    }

    public void setIdRequest(String idRequest) {
        this.idRequest = idRequest;
    }

    public int getIdInvestor() {
        return idInvestor;
    }

    public void setIdInvestor(int idInvestor) {
        this.idInvestor = idInvestor;
    }

    public String getNameProduct() {
        return nameProduct;
    }

    public void setNameProduct(String nameProduct) {
        this.nameProduct = nameProduct;
    }

    public String getProductType() { 
        return productType;
    }

    public void setProductType(String productType) { 
        this.productType = productType;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) { 
        this.reason = reason;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "ProductRequest{" +
               "idRequest='" + idRequest + '\'' +
               ", idInvestor=" + idInvestor +
               ", nameProduct='" + nameProduct + '\'' +
               ", productType='" + productType + '\'' + 
               ", reason='" + reason + '\'' +          
               ", status='" + status + '\'' +
               '}';
    }
}