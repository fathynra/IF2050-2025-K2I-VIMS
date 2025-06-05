package com.vims.ui.controllers;

import com.vims.controller.ProductController; 
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

public class AddProductController {

    @FXML private TextField productNameField;
    @FXML private ComboBox<String> productTypeComboBox;
    @FXML private TextField riskLevelField;
    @FXML private TextField unitPriceField;
    @FXML private TextArea descriptionArea;
    @FXML private Button addProductButton;
    @FXML private Label errorLabel;

    private ProductController businessProductController;
    private final List<String> validProductTypes = Arrays.asList("stock", "bond", "real estate");


    public AddProductController() {
        this.businessProductController = new ProductController();
    }

    @FXML
    public void initialize() {
        errorLabel.setText("");
        productTypeComboBox.setItems(FXCollections.observableArrayList(validProductTypes));
        productTypeComboBox.getSelectionModel().selectFirst(); 

    }

    @FXML
    private void handleAddProductAction() {
        String name = productNameField.getText();
        String type = productTypeComboBox.getValue();
        String riskLevel = riskLevelField.getText();
        String description = descriptionArea.getText();

        BigDecimal unitPrice;
        try {
            unitPrice = new BigDecimal(unitPriceField.getText());
            if (unitPrice.compareTo(BigDecimal.ZERO) < 0) {
                errorLabel.setText("Harga unit tidak boleh negatif.");
                return;
            }
        } catch (NumberFormatException e) {
            errorLabel.setText("Format harga unit tidak valid.");
            return;
        }

        if (name.isEmpty() || type == null || type.isEmpty()) {
            errorLabel.setText("Nama produk dan tipe produk tidak boleh kosong.");
            return;
        }

        boolean success = businessProductController.addNewProduct(name, type, riskLevel, description, unitPrice);

        if (success) {
            showAlert(Alert.AlertType.INFORMATION, "Sukses", "Produk '" + name + "' berhasil ditambahkan.");

            clearForm();

        } else {
            errorLabel.setText("Gagal menambahkan produk. Periksa input atau konsol.");
            showAlert(Alert.AlertType.ERROR, "Gagal", "Gagal menambahkan produk. Pastikan tipe produk valid dan input lainnya benar.");
        }
    }

    @FXML
    private void handleCancelAction() {
        closeWindow();
    }

    private void clearForm() {
        productNameField.clear();
        productTypeComboBox.getSelectionModel().selectFirst();
        riskLevelField.clear();
        unitPriceField.clear();
        descriptionArea.clear();
        errorLabel.setText("");
    }

    private void closeWindow() {
        Stage stage = (Stage) addProductButton.getScene().getWindow();
        stage.close();
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}