package com.vims.ui.controllers;

import com.vims.model.InvestmentProduct;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

import java.text.NumberFormat;
import java.util.Locale;

public class ProductDetailController {

    @FXML private Label productNameHeaderLabel;
    @FXML private Label productTypeDetailLabel;
    @FXML private Label riskLevelLabel;
    @FXML private Label priceLabel;
    @FXML private TextArea descriptionArea;
    @FXML private Button backButton;

    private InvestmentProduct currentProduct;
    private MainLayoutController mainLayoutController; 

    private NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));

    public void initData(InvestmentProduct product, MainLayoutController mainLayoutController) {
        this.currentProduct = product;
        this.mainLayoutController = mainLayoutController;

        if (product != null) {
            productNameHeaderLabel.setText(product.getName()); 

            productTypeDetailLabel.setText("Tipe: " + product.getType()); 

            riskLevelLabel.setText(product.getRiskLevel() != null ? product.getRiskLevel() : "N/A");
            priceLabel.setText(currencyFormatter.format(product.getUnitPrice()));
            descriptionArea.setText(product.getDescription() != null ? product.getDescription() : "Tidak ada deskripsi.");
        } else {

            productNameHeaderLabel.setText("Produk Tidak Ditemukan");
            productTypeDetailLabel.setText("");
            riskLevelLabel.setText("");
            priceLabel.setText("");
            descriptionArea.setText("Detail produk tidak dapat dimuat.");
        }
    }

    @FXML
    private void handleBackAction() {
        if (mainLayoutController != null) {

            mainLayoutController.handleShowProductView(); 
        } else {
            System.err.println("MainLayoutController tidak ter-set. Tidak bisa kembali.");
        }
    }
}