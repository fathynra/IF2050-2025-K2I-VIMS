package com.vims.ui.controllers;

import com.vims.controller.LoginController;
import com.vims.controller.ProductController; 
import com.vims.controller.TransactionController; 
import com.vims.model.Investor;
import com.vims.model.InvestmentProduct;
import com.vims.model.Transaction;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.List;

public class TransactionHistoryController {

    @FXML private ScrollPane scrollPane;
    @FXML private VBox transactionContainerPane;
    @FXML private Label infoLabel;

    private TransactionController businessTransactionController;
    private ProductController businessProductController; 
    private Investor currentUser;

    public TransactionHistoryController() {
        this.businessTransactionController = new TransactionController();
        this.businessProductController = new ProductController();
        this.currentUser = LoginController.getCurrentLoggedInUser();
    }

    @FXML
    public void initialize() {
        loadTransactionHistory();
    }

    private void loadTransactionHistory() { //yang ini itu ada kea berdasarkan role user
        transactionContainerPane.getChildren().clear(); 

        if (currentUser == null) {
            infoLabel.setText("Tidak ada pengguna yang login.");
            showAlert(Alert.AlertType.ERROR, "Error", "Sesi pengguna tidak ditemukan.");
            return;
        }

        String userRole = currentUser.getRole();
        List<Transaction> transactions;

        if ("MANAGER".equalsIgnoreCase(userRole) || "ADMIN".equalsIgnoreCase(userRole)) {
            infoLabel.setText("Menampilkan semua transaksi sistem...");
            transactions = businessTransactionController.getAllTransactionsInSystem();
            if (transactions.isEmpty()) {
                infoLabel.setText("Belum ada transaksi di sistem.");
            } else {
                infoLabel.setText("Menampilkan " + transactions.size() + " transaksi sistem.");
            }
        } else if ("INVESTOR".equalsIgnoreCase(userRole)) {
            infoLabel.setText("Menampilkan riwayat transaksi Anda...");
            transactions = businessTransactionController.getTransactionHistory(currentUser.getAccountId());
            if (transactions.isEmpty()) {
                infoLabel.setText("Anda belum memiliki riwayat transaksi.");
            } else {
                infoLabel.setText("Menampilkan " + transactions.size() + " transaksi Anda.");
            }
        } else {
            infoLabel.setText("Peran pengguna tidak dikenali atau tidak memiliki akses ke riwayat transaksi.");
            return;
        }

        if (transactions != null && !transactions.isEmpty()) {
            for (Transaction tx : transactions) {
                try {
                    InvestmentProduct product = businessProductController.getInvestmentProductById(tx.getProductId());

                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/TransactionCard.fxml"));
                    Node transactionCardNode = loader.load();

                    TransactionCardController cardController = loader.getController();
                    cardController.setData(tx, product); 

                    transactionContainerPane.getChildren().add(transactionCardNode);
                } catch (IOException e) {
                    System.err.println("Gagal memuat TransactionCard.fxml untuk transaksi ID: " + tx.getTransactionId());
                    e.printStackTrace();
                }
            }
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}