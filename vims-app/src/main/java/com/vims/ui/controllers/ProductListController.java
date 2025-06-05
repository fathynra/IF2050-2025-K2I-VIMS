package com.vims.ui.controllers; 

import com.vims.controller.ProductController;
import com.vims.model.InvestmentProduct;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader; 
import javafx.scene.Node; 
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.TilePane; 


import java.io.IOException; 

import java.util.List;
import java.util.stream.Collectors;


public class ProductListController {

    private MainLayoutController mainLayoutController;
    @FXML
    private TextField searchField;

    @FXML
    private TilePane productContainerPane; 

    private ProductController businessProductController;
    private List<InvestmentProduct> allProductsList; 

    public void setMainLayoutController(MainLayoutController mainLayoutController) {
        this.mainLayoutController = mainLayoutController;
        if (this.mainLayoutController != null) {
            loadAndDisplayProductData(); 
        } else {
            System.err.println("ProductListController: MainLayoutController null saat mencoba memuat data produk.");
        }
    }

    public ProductListController() {
        this.businessProductController = new ProductController();
    }

    @FXML
    public void initialize() {

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filterProductData(newValue);
        });
    }

    private void loadAndDisplayProductData() {
        allProductsList = businessProductController.getAllInvestmentProducts();
        displayProducts(allProductsList); 
    }
    
    private void displayProducts(List<InvestmentProduct> productsToDisplay) {
        productContainerPane.getChildren().clear(); 

        if (productsToDisplay != null) {
            for (InvestmentProduct product : productsToDisplay) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/ProductCard.fxml"));
                    Node productCardNode = loader.load(); 

                    ProductCardController cardController = loader.getController();
                    cardController.setData(product);
                    cardController.setMainLayoutController(mainLayoutController); 

                    productContainerPane.getChildren().add(productCardNode);
                } catch (IOException e) {
                    System.err.println("Gagal memuat ProductCard.fxml untuk produk: " + product.getName());
                    e.printStackTrace();
                }
            }
        }
    }
    

    private void filterProductData(String searchText) {
        if (searchText == null || searchText.isEmpty()) {
            displayProducts(allProductsList); 
            return;
        }

        String lowerCaseSearchText = searchText.toLowerCase();
        
        List<InvestmentProduct> filteredList = allProductsList.stream()
            .filter(product -> product.getName().toLowerCase().contains(lowerCaseSearchText) ||
                               product.getType().toLowerCase().contains(lowerCaseSearchText))
            .collect(Collectors.toList());
            
        displayProducts(filteredList);
    }
    @FXML
    private void handleAddRequestButton() {
        System.out.println("Tombol 'Add Request' diklik!");
        if (mainLayoutController != null) {
            mainLayoutController.loadViewWithControllerSetup("/ui/ProductRequestView.fxml", controller -> {
                if (controller instanceof ProductRequestViewController) {
                    ((ProductRequestViewController) controller).setMainLayoutController(mainLayoutController);
                }
            });
        } else {
            System.err.println("MainLayoutController tidak di-set di ProductListController. Tidak bisa navigasi.");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Kesalahan Navigasi");
            alert.setContentText("Tidak dapat membuka form permintaan produk.");
            alert.showAndWait();
        }
    }
}