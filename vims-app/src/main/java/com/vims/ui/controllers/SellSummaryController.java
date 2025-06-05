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

public class SellSummaryController {

    @FXML private Label productNameLabel;
    @FXML private Label sellDateLabel;
    @FXML private Label unitsLabel;
    @FXML private Label withdrawalMethodLabel;
    @FXML private Label totalRevenueLabel;
    @FXML private CheckBox termsCheckbox;
    @FXML private Button finalConfirmSellButton;

    private InvestmentProduct productToSell;
    private int unitsToSell;
    private String selectedWithdrawalMethod;
    private BigDecimal totalSellRevenue;
    private int currentInvestorId;

    private SellProductController sellProductUiController; 
    private TransactionController transactionBusinessController;
    private NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));

    public SellSummaryController() {
        this.transactionBusinessController = new TransactionController();
    }

    public void initData(InvestmentProduct product, int units, String withdrawalMethod, BigDecimal totalRevenue, int investorId, SellProductController sellProductUiController) {
        this.productToSell = product;
        this.unitsToSell = units;
        this.selectedWithdrawalMethod = withdrawalMethod;
        this.totalSellRevenue = totalRevenue;
        this.currentInvestorId = investorId;
        this.sellProductUiController = sellProductUiController;

        productNameLabel.setText(product.getName() + " (" + product.getType() + ")");
        sellDateLabel.setText(LocalDate.now().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG).withLocale(new Locale("id", "ID"))));
        unitsLabel.setText(String.valueOf(units) + " units");
        withdrawalMethodLabel.setText(withdrawalMethod);
        totalRevenueLabel.setText(currencyFormatter.format(totalRevenue));

        termsCheckbox.selectedProperty().addListener((obs, wasSelected, isNowSelected) -> {
            finalConfirmSellButton.setDisable(!isNowSelected);
        });
    }

    @FXML
    private void handleFinalConfirmSellAction() {
        if (!termsCheckbox.isSelected()) {
            showAlert(Alert.AlertType.WARNING, "Peringatan", "Anda harus menyetujui transaksi penjualan.");
            return;
        }

        boolean success = transactionBusinessController.sellProduct(currentInvestorId, productToSell.getProductId(), unitsToSell);

        if (success) {
            showAlert(Alert.AlertType.INFORMATION, "Sukses", "Penjualan produk " + productToSell.getName() + " sebanyak " + unitsToSell + " unit berhasil!");
            closeWindow();
            if (sellProductUiController != null) {
                sellProductUiController.refreshAfterTransaction(); 
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "Gagal", "Penjualan produk gagal.");
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
        Stage stage = (Stage) finalConfirmSellButton.getScene().getWindow();
        stage.close();
    }
}