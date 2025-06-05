package com.vims.ui.controllers;

import com.vims.model.InvestmentProduct;
import com.vims.model.Transaction;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;

public class TransactionCardController {

    @FXML private Label productNameLabel;
    @FXML private Label transactionTypeLabel;
    @FXML private Label dateTimeLabel;
    @FXML private Label unitsLabel;


    @FXML private Label investorIdLabel;

    private DateTimeFormatter dateTimeFormatter = 
        DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM).withLocale(new Locale("id", "ID"));

    public void setData(Transaction transaction, InvestmentProduct product) {
        if (product != null) {
            productNameLabel.setText(product.getName() + " (" + product.getType() + ")");
        } else {
            productNameLabel.setText("Produk ID: " + transaction.getProductId() + " (Data tidak ditemukan)");
        }

        String typeDisplay = "N/A";
        if ("buy".equalsIgnoreCase(transaction.getType())) {
            typeDisplay = "Pembelian";
        } else if ("sell".equalsIgnoreCase(transaction.getType())) {
            typeDisplay = "Penjualan";
        }
        transactionTypeLabel.setText(typeDisplay);

        if (transaction.getDatetime() != null) {
            dateTimeLabel.setText(transaction.getDatetime().format(dateTimeFormatter));
        } else {
            dateTimeLabel.setText("N/A");
        }

        unitsLabel.setText(String.valueOf(transaction.getQuantity()) + " unit");
        investorIdLabel.setText(String.valueOf(transaction.getAccountId()));

    }
}