package com.vims.ui.controllers;

import com.vims.controller.LoginController; 
import com.vims.controller.RequestController; 
import com.vims.model.Investor;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


public class ProductRequestViewController {

    @FXML private TextField productNameField;
    @FXML private ComboBox<String> productTypeComboBox;
    @FXML private TextArea reasonArea;
    @FXML private Button submitRequestButton;
    @FXML private Label errorLabel;

    private RequestController businessRequestController;
    private MainLayoutController mainLayoutController; 

    public ProductRequestViewController() {
        this.businessRequestController = new RequestController();
    }

    public void setMainLayoutController(MainLayoutController mainLayoutController) {
        this.mainLayoutController = mainLayoutController;
    }

    @FXML
    public void initialize() {
        errorLabel.setText("");

        productTypeComboBox.setItems(FXCollections.observableArrayList(
            "Stock", "Mutual Fund", "Bond", "ETF", "Other"
        ));

    }

    @FXML
    private void handleSubmitRequestAction() {
        String productName = productNameField.getText();
        String productType = productTypeComboBox.getValue();
        String reason = reasonArea.getText();

        Investor currentUser = LoginController.getCurrentLoggedInUser();
        if (currentUser == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "Tidak ada pengguna yang login. Silakan login terlebih dahulu.");

            closeWindow();
            return;
        }

        if (!"INVESTOR".equalsIgnoreCase(currentUser.getRole())) {
             showAlert(Alert.AlertType.WARNING, "Akses Ditolak", "Hanya investor yang dapat membuat permintaan produk.");
            return;
        }


        if (productName.isEmpty() || productType == null || productType.isEmpty() || reason.isEmpty()) {
            errorLabel.setText("Semua field (Nama Produk, Tipe, Alasan) harus diisi.");
            return;
        }

        boolean success = businessRequestController.submitProductRequest(
            currentUser.getAccountId(), 
            productName,
            productType,
            reason,
            null //notesnya g jdi
        );

        if (success) {
            showAlert(Alert.AlertType.INFORMATION, "Sukses", "Permintaan produk '" + productName + "' berhasil dikirim.");

            if (mainLayoutController != null) {

                mainLayoutController.handleShowProductView(); 
            } else {
                closeWindow(); 
            }
        } else {
            errorLabel.setText("Gagal mengirim permintaan. Cek konsol untuk detail.");
            showAlert(Alert.AlertType.ERROR, "Gagal", "Gagal mengirim permintaan produk.");
        }
    }

    @FXML
    private void handleCancelAction() {
        if (mainLayoutController != null) {
            mainLayoutController.handleShowProductView();
        } else {
            closeWindow();
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void closeWindow() {
        if (submitRequestButton != null && submitRequestButton.getScene() != null) {
            Stage stage = (Stage) submitRequestButton.getScene().getWindow();
            if (stage != null) {
                stage.close();
            }
        } else {
             System.out.println("Tidak bisa menutup window karena scene tidak ditemukan atau bukan stage terpisah.");
        }
    }
}