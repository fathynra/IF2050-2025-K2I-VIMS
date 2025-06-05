package com.vims.ui.controllers;

import com.vims.model.InvestmentProduct;
import com.vims.model.Investor;
import com.vims.controller.LoginController; 
import com.vims.dao.InvestorDao;

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
import javafx.collections.FXCollections;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale; 

public class BuyProductController {

    @FXML private Label productNameLabel;
    @FXML private Label productPriceLabel;
    @FXML private Label investorBalanceLabel;
    @FXML private Spinner<Integer> unitsToPurchaseSpinner;
    @FXML private ComboBox<String> paymentMethodComboBox;
    @FXML private Label totalCostLabel;
    @FXML private Button reviewOrderButton;

    private InvestmentProduct selectedProduct;
    private InvestorDao investorDao;
    private int currentInvestorId; 

    private NumberFormat currencyFormatter;

    public BuyProductController() {
        this.investorDao = new InvestorDao();
        Locale indonesiaLocale = Locale.forLanguageTag("id-ID"); 
        this.currencyFormatter = NumberFormat.getCurrencyInstance(indonesiaLocale); //buat currency

        Investor currentUser = LoginController.getCurrentLoggedInUser();
        if (currentUser != null) {
            this.currentInvestorId = currentUser.getAccountId();
        } else {
            System.err.println("BuyProductController: Tidak ada pengguna yang login saat controller dibuat! Menggunakan ID default -1.");
            this.currentInvestorId = -1; 
        }
    }

    public void initData(InvestmentProduct product) {
        this.selectedProduct = product;
        if (product != null) {
            productNameLabel.setText(product.getName() + " (" + product.getType() + ")");
            productPriceLabel.setText(currencyFormatter.format(product.getUnitPrice()));

            SpinnerValueFactory<Integer> valueFactory = 
                new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 1000, 1); 
            unitsToPurchaseSpinner.setValueFactory(valueFactory);
            unitsToPurchaseSpinner.setEditable(true);
            
            updateTotalCost(unitsToPurchaseSpinner.getValue()); 
            reviewOrderButton.setDisable(false);
        } else {
            productNameLabel.setText("Produk tidak valid");
            productPriceLabel.setText("-");
            totalCostLabel.setText(currencyFormatter.format(BigDecimal.ZERO));
            unitsToPurchaseSpinner.setDisable(true); 
            reviewOrderButton.setDisable(true);
        }

        paymentMethodComboBox.setItems(FXCollections.observableArrayList(
                "BCA", "Gopay", "OVO", "Mandiri", "Credit Card"
        ));
        paymentMethodComboBox.getSelectionModel().selectFirst();
        
        unitsToPurchaseSpinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) { 
                 updateTotalCost(newValue);
            } else {
                 updateTotalCost(0); 
            }
        });
        
        loadInvestorBalance();
    }
    
    private void loadInvestorBalance() {
        if (this.currentInvestorId == -1) {
            investorBalanceLabel.setText("Tidak dapat memuat saldo (user tidak login).");
            return;
        }
        Investor investor = this.investorDao.getInvestorById(this.currentInvestorId); 
        if (investor != null) {
            investorBalanceLabel.setText(currencyFormatter.format(investor.getBalance()));
        } else {
            investorBalanceLabel.setText("Tidak dapat memuat saldo");
            System.err.println("Investor dengan ID " + currentInvestorId + " tidak ditemukan untuk memuat saldo.");
        }
    }

    private void updateTotalCost(Integer units) {
        if (selectedProduct != null && units != null && units > 0) {
            BigDecimal total = selectedProduct.getUnitPrice().multiply(new BigDecimal(units));
            totalCostLabel.setText(currencyFormatter.format(total));
        } else {
            totalCostLabel.setText(currencyFormatter.format(BigDecimal.ZERO));
        }
    }

    @FXML
    private void handleReviewOrderAction() {
        if (this.currentInvestorId == -1) {
            showAlert(Alert.AlertType.ERROR, "Error Login", "Sesi pengguna tidak valid atau tidak ditemukan! Silakan login kembali.");
            return;
        }
        if (selectedProduct == null) {
            showAlert(Alert.AlertType.ERROR, "Error Produk", "Produk tidak valid atau tidak dipilih.");
            return;
        }
        String paymentMethod = paymentMethodComboBox.getValue();
        if (paymentMethod == null || paymentMethod.trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error Input", "Metode pembayaran belum dipilih.");
            return;
        }

        Integer units = unitsToPurchaseSpinner.getValue();
        if (units == null || units <= 0) {
            showAlert(Alert.AlertType.ERROR, "Error Input", "Jumlah unit harus lebih dari 0.");
            return;
        }
        
        BigDecimal currentTotalCost = selectedProduct.getUnitPrice().multiply(new BigDecimal(units));

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/OrderSummaryView.fxml"));
            Parent orderSummaryRoot = loader.load();

            OrderSummaryController controller = loader.getController();
            controller.initData(selectedProduct, units, paymentMethod, currentTotalCost, this.currentInvestorId, this);

            Stage summaryStage = new Stage();
            summaryStage.setTitle("Ringkasan Pembelian");
            summaryStage.setScene(new Scene(orderSummaryRoot));
            summaryStage.initModality(Modality.APPLICATION_MODAL);
            if (reviewOrderButton != null && reviewOrderButton.getScene() != null) {
                 summaryStage.initOwner(reviewOrderButton.getScene().getWindow());
            }
            summaryStage.showAndWait();

        } catch (IOException e) {
            System.err.println("Gagal membuka form ringkasan pesanan: " + e.getMessage());
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error Sistem", "Gagal membuka ringkasan pesanan.");
        } catch (Exception e) {
            System.err.println("Error tidak terduga saat menampilkan ringkasan pesanan: " + e.getMessage());
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error Sistem", "Terjadi error tidak terduga.");
        }
    }
    
    public void refreshAfterTransaction() {
        System.out.println("BuyProductController: Refreshing data setelah transaksi...");
        loadInvestorBalance(); 
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
        if (reviewOrderButton != null && reviewOrderButton.getScene() != null) {
            stage = (Stage) reviewOrderButton.getScene().getWindow();
        }
        if (stage != null) {
            stage.close();
        } else {
            System.err.println("Tidak bisa menutup window karena scene/tombol tidak ditemukan.");
        }
    }
}