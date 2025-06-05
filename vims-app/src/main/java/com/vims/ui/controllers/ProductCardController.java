package com.vims.ui.controllers; 

import com.vims.model.InvestmentProduct;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.Locale;

public class ProductCardController {

    @FXML
    private VBox productCard;
    @FXML
    private Label productNameLabel;
    @FXML
    private Label productDescriptionLabel; 
    @FXML
    private Label priceLabel;
    @FXML
    private Label changeLabel; 
    @FXML
    private Button seeDetailsButton;
    @FXML
    private Button sellButton;
    @FXML
    private Button buyButton;

    private InvestmentProduct product;
    private MainLayoutController mainLayoutController;

    public void setMainLayoutController(MainLayoutController mainLayoutController) {
        this.mainLayoutController = mainLayoutController;
    }

    public void setData(InvestmentProduct product) {
        this.product = product;
        productNameLabel.setText(product.getName()); 

        productDescriptionLabel.setText(product.getType()); 


        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("id", "ID")); //idr strong
        priceLabel.setText(currencyFormat.format(product.getUnitPrice()));

        changeLabel.setText("+0.00 (+0.00%)"); 

    }

    @FXML
    private void handleBuyAction() {
        System.out.println("Tombol BUY diklik untuk produk: " + product.getName() + " (ID: " + product.getProductId() + ")");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/BuyProductView.fxml")); 
            Parent buyProductRoot = loader.load();

            BuyProductController controller = loader.getController();
            controller.initData(this.product); 

            Stage buyStage = new Stage();
            buyStage.setTitle("Beli Produk: " + this.product.getName());
            buyStage.setScene(new Scene(buyProductRoot));
            buyStage.initModality(Modality.APPLICATION_MODAL); 
            buyStage.showAndWait(); 

        } catch (IOException e) {
            System.err.println("Gagal membuka form pembelian: " + e.getMessage());
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Gagal Membuka Form Pembelian");
            alert.setContentText("Terjadi kesalahan saat mencoba membuka form pembelian produk.");
            alert.showAndWait();
        }
    }

    @FXML
    private void handleSellAction() {
        System.out.println("Tombol SELL diklik untuk produk: " + product.getName() + " (ID: " + product.getProductId() + ")");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/SellProductView.fxml"));
            Parent sellProductRoot = loader.load();

            SellProductController controller = loader.getController();
            controller.initData(this.product);

            Stage sellStage = new Stage();
            sellStage.setTitle("Jual Produk: " + this.product.getName());
            sellStage.setScene(new Scene(sellProductRoot));
            sellStage.initModality(Modality.APPLICATION_MODAL);
            if (sellButton != null && sellButton.getScene() != null) { 
                sellStage.initOwner(sellButton.getScene().getWindow());
            }
            sellStage.showAndWait();

        } catch (IOException e) {
            System.err.println("Gagal membuka form penjualan: " + e.getMessage());
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Gagal Membuka Form Penjualan");
            alert.setContentText("Terjadi kesalahan saat mencoba membuka form penjualan produk.");
            alert.showAndWait();
        }
    }

    @FXML
    private void handleSeeDetails() {
        System.out.println("Tombol SEE DETAILS diklik untuk produk: " + product.getName() + " (ID: " + product.getProductId() + ")");
        if (mainLayoutController != null && product != null) {
            mainLayoutController.loadViewWithControllerSetup("/ui/ProductDetailView.fxml", controller -> {
                if (controller instanceof ProductDetailController) {
                    ((ProductDetailController) controller).initData(this.product, this.mainLayoutController);
                }
            });
        } else {
            System.err.println("MainLayoutController atau produk tidak tersedia untuk menampilkan detail.");
        }
    }

    public VBox getRoot() {
        return productCard;
    }
}