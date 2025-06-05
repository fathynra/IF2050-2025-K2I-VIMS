package com.vims.ui.controllers;

import com.vims.dao.InvestorInvestmentDao;
import com.vims.model.InvestmentProduct;
import com.vims.model.InvestorInvestment;
import com.vims.model.Investor; 
import com.vims.controller.LoginController;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

public class SellProductController {

    @FXML private Label productNameLabel;
    @FXML private Label productPriceLabel;
    @FXML private Label unitsOwnedLabel;
    @FXML private Spinner<Integer> unitsToSellSpinner;
    @FXML private ComboBox<String> withdrawalMethodComboBox;
    @FXML private Label totalRevenueLabel;
    @FXML private Button reviewSellButton;

    private InvestmentProduct selectedProduct;
    private InvestorInvestmentDao investorInvestmentDao;
    private int currentInvestorId; 
    private int maxUnitsToSell = 0;

    private NumberFormat currencyFormatter;

    public SellProductController() {
        this.investorInvestmentDao = new InvestorInvestmentDao();
        this.currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("id", "ID")); 

        Investor currentUser = LoginController.getCurrentLoggedInUser();
        if (currentUser != null) {
            this.currentInvestorId = currentUser.getAccountId();
        } else {
            System.err.println("SellProductController: Tidak ada pengguna yang login saat controller dibuat! Menggunakan ID default -1.");
            this.currentInvestorId = -1; 
        }
    }

    public void initData(InvestmentProduct product) {
        this.selectedProduct = product;
        if (product != null) {
            productNameLabel.setText(product.getName() + " (" + product.getType() + ")");
            productPriceLabel.setText(currencyFormatter.format(product.getUnitPrice()));
            loadUnitsOwned(); 
        } else {
            productNameLabel.setText("Produk tidak valid");
            productPriceLabel.setText("-");
            unitsOwnedLabel.setText("0");
            unitsToSellSpinner.setDisable(true);
            reviewSellButton.setDisable(true);
            updateTotalRevenue(0); 
        }

        withdrawalMethodComboBox.setItems(FXCollections.observableArrayList(
                "BCA", "Gopay", "OVO", "Mandiri", "Credit Card"
        ));
        withdrawalMethodComboBox.getSelectionModel().selectFirst();

        unitsToSellSpinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                updateTotalRevenue(newValue);
            } else {
                updateTotalRevenue(0);
            }
        });

    }
    
    private void loadUnitsOwned() {
        if (selectedProduct == null || this.currentInvestorId == -1) {
            unitsOwnedLabel.setText("0 unit (Tidak Dimiliki)");
            SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 0, 0);
            unitsToSellSpinner.setValueFactory(valueFactory);
            unitsToSellSpinner.setDisable(true);
            reviewSellButton.setDisable(true);
            if (this.currentInvestorId == -1) System.err.println("SellProductController.loadUnitsOwned: currentInvestorId tidak valid.");
            if (selectedProduct == null) System.err.println("SellProductController.loadUnitsOwned: selectedProduct null.");
            updateTotalRevenue(0);
            return;
        }

        InvestorInvestment ownership = investorInvestmentDao.getInvestorInvestment(this.currentInvestorId, selectedProduct.getProductId());
        if (ownership != null && ownership.getQuantityOwned() > 0) {
            maxUnitsToSell = ownership.getQuantityOwned();
            unitsOwnedLabel.setText(String.valueOf(maxUnitsToSell) + " unit");
            SpinnerValueFactory<Integer> valueFactory = 
                new SpinnerValueFactory.IntegerSpinnerValueFactory(1, maxUnitsToSell, Math.min(1, maxUnitsToSell));
            unitsToSellSpinner.setValueFactory(valueFactory);
            unitsToSellSpinner.setDisable(false);
            reviewSellButton.setDisable(false);
        } else {
            maxUnitsToSell = 0;
            unitsOwnedLabel.setText("0 unit (Tidak Dimiliki)");
            SpinnerValueFactory<Integer> valueFactory = 
                new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 0, 0);
            unitsToSellSpinner.setValueFactory(valueFactory);
            unitsToSellSpinner.setDisable(true);
            reviewSellButton.setDisable(true);
        }
        if(unitsToSellSpinner.getValueFactory() != null && unitsToSellSpinner.getValue() != null) {
            updateTotalRevenue(unitsToSellSpinner.getValue());
        } else {
            updateTotalRevenue(0);
        }
    }

    private void updateTotalRevenue(Integer units) {
        if (selectedProduct != null && units != null && units > 0 && units <= maxUnitsToSell) {
            BigDecimal total = selectedProduct.getUnitPrice().multiply(new BigDecimal(units));
            totalRevenueLabel.setText(currencyFormatter.format(total));
        } else {
            totalRevenueLabel.setText(currencyFormatter.format(BigDecimal.ZERO));
        }
    }

    @FXML
    private void handleReviewSellAction() {
        if (this.currentInvestorId == -1) {
            showAlert(Alert.AlertType.ERROR, "Error Login", "Sesi pengguna tidak valid atau tidak ditemukan! Silakan login kembali.");
            return;
        }
        if (selectedProduct == null) {
            showAlert(Alert.AlertType.ERROR, "Error Produk", "Produk tidak valid.");
            return;
        }
        String withdrawalMethod = withdrawalMethodComboBox.getValue();
        if (withdrawalMethod == null || withdrawalMethod.trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error Input", "Metode penarikan dana belum dipilih.");
            return;
        }

        Integer units = unitsToSellSpinner.getValue();
        if (units == null || units <= 0) {
            showAlert(Alert.AlertType.ERROR, "Error Input", "Jumlah unit jual harus lebih dari 0.");
            return;
        }
        if (units > maxUnitsToSell) {
             showAlert(Alert.AlertType.ERROR, "Error Input", "Jumlah unit jual (" + units + ") melebihi unit yang dimiliki (" + maxUnitsToSell + ").");
            return;
        }
        
        BigDecimal currentTotalRevenue = selectedProduct.getUnitPrice().multiply(new BigDecimal(units));

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/SellSummaryView.fxml"));
            Parent sellSummaryRoot = loader.load();

            SellSummaryController controller = loader.getController();
            controller.initData(selectedProduct, units, withdrawalMethod, currentTotalRevenue, this.currentInvestorId, this); 

            Stage summaryStage = new Stage();
            summaryStage.setTitle("Ringkasan Penjualan");
            summaryStage.setScene(new Scene(sellSummaryRoot));
            summaryStage.initModality(Modality.APPLICATION_MODAL);
            if (reviewSellButton != null && reviewSellButton.getScene() != null) {
                 summaryStage.initOwner(reviewSellButton.getScene().getWindow());
            }
            summaryStage.showAndWait();

        } catch (IOException e) {
            System.err.println("Gagal membuka form ringkasan penjualan: " + e.getMessage());
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error Sistem", "Gagal membuka ringkasan penjualan.");
        } catch (Exception e) {
            System.err.println("Error tidak terduga saat menampilkan ringkasan penjualan: " + e.getMessage());
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error Sistem", "Terjadi error tidak terduga.");
        }
    }
    
    public void refreshAfterTransaction() {
        System.out.println("SellProductController: Refreshing data setelah transaksi...");
        loadUnitsOwned(); 
    }

    @FXML
    private void handleCancelAction() {
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
        Stage stage = null;
        if (reviewSellButton != null && reviewSellButton.getScene() != null) {
            stage = (Stage) reviewSellButton.getScene().getWindow();
        }
        if (stage != null) {
            stage.close();
        } else {
            System.err.println("Tidak bisa menutup window karena scene/tombol tidak ditemukan.");
        }
    }
}