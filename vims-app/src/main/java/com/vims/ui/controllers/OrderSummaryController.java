package com.vims.ui.controllers;

import com.vims.controller.TransactionController;
import com.vims.model.InvestmentProduct;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;

public class OrderSummaryController {

    @FXML private Label productNameLabel;
    @FXML private Label purchaseDateLabel;
    @FXML private Label unitsLabel;
    @FXML private Label paymentMethodLabel;
    @FXML private Label totalCostLabel;
    @FXML private CheckBox termsCheckbox;
    @FXML private Button finalConfirmBuyButton;

    private InvestmentProduct productToBuy;
    private int unitsToBuy;
    private String selectedPaymentMethod;
    private BigDecimal totalBuyCost;
    private int currentInvestorId; 

    private BuyProductController buyProductUiController; 
    private TransactionController transactionBusinessController;
    private NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));

    public OrderSummaryController() {
        this.transactionBusinessController = new TransactionController();
    }

    public void initData(InvestmentProduct product, int units, String paymentMethod, BigDecimal totalCost, int investorId, BuyProductController buyProductUiController) {
        this.productToBuy = product;
        this.unitsToBuy = units;
        this.selectedPaymentMethod = paymentMethod;
        this.totalBuyCost = totalCost;
        this.currentInvestorId = investorId;
        this.buyProductUiController = buyProductUiController; 

        productNameLabel.setText(product.getName() + " (" + product.getType() + ")");
        purchaseDateLabel.setText(LocalDate.now().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG).withLocale(new Locale("id", "ID"))));
        unitsLabel.setText(String.valueOf(units) + " units");
        paymentMethodLabel.setText(paymentMethod);
        totalCostLabel.setText(currencyFormatter.format(totalCost));

        termsCheckbox.selectedProperty().addListener((obs, wasSelected, isNowSelected) -> {
            finalConfirmBuyButton.setDisable(!isNowSelected);
        });
    }

    @FXML
    private void handleFinalConfirmBuyAction() {
        if (!termsCheckbox.isSelected()) {
            showAlert(Alert.AlertType.WARNING, "Peringatan", "Anda harus menyetujui syarat dan ketentuan.");
            return;
        }

        boolean success = transactionBusinessController.buyProduct(currentInvestorId, productToBuy.getProductId(), unitsToBuy);

        if (success) {
            showAlert(Alert.AlertType.INFORMATION, "Sukses", "Pembelian produk " + productToBuy.getName() + " sebanyak " + unitsToBuy + " unit berhasil!");
            closeWindow(); 
            if (buyProductUiController != null) {
                buyProductUiController.refreshAfterTransaction();
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "Gagal", "Pembelian produk gagal. Cek konsol untuk detail.");
        }
    }

    @FXML
    private void handleCancelSummaryAction() {
        closeWindow();
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void closeWindow() {
        Stage stage = (Stage) finalConfirmBuyButton.getScene().getWindow();
        stage.close();
    }
}