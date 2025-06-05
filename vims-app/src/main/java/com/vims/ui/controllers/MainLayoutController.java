package com.vims.ui.controllers;

import java.io.IOException;
import java.util.function.Consumer;

import com.vims.MainApp;
import com.vims.controller.LoginController;
import com.vims.model.Investor;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainLayoutController {

    @FXML private BorderPane contentPane;
    @FXML private VBox sidebarContainer; 
    @FXML private VBox navigationButtonsPane; 
    @FXML private Button logoutButton;
    @FXML private Label welcomeMessageLabel;

    private LoginController loginBusinessController;

    public MainLayoutController() {
        this.loginBusinessController = new LoginController();
    }

    @FXML
    public void initialize() {
        Investor currentUser = LoginController.getCurrentLoggedInUser();
        if (currentUser != null) {
            welcomeMessageLabel.setText("Selamat datang, " + currentUser.getName() + "! Peran: " + currentUser.getRole());
        } else {
            welcomeMessageLabel.setText("Selamat datang di VIMS!");
        }
        setupRoleBasedUI();
        handleShowProductView(); 
    }

    private void setupRoleBasedUI() {
        Investor currentUser = LoginController.getCurrentLoggedInUser();
        boolean isAdmin = false;
        boolean isManager = false;
        boolean isInvestor = false;

        navigationButtonsPane.getChildren().clear(); 

        Button productBtn = new Button("Produk");
        productBtn.setPrefWidth(Double.MAX_VALUE);
        productBtn.setOnAction(event -> handleShowProductView());
        navigationButtonsPane.getChildren().add(productBtn);

        Button requestBtn = new Button("Permintaan");
        requestBtn.setPrefWidth(Double.MAX_VALUE);
        requestBtn.setOnAction(event -> handleShowRequestView());
        navigationButtonsPane.getChildren().add(requestBtn);

        Button transactionBtn = new Button("Transaksi");
        transactionBtn.setPrefWidth(Double.MAX_VALUE);
        transactionBtn.setOnAction(event -> handleShowTransactionView());
        navigationButtonsPane.getChildren().add(transactionBtn);


        if (currentUser != null) {
            if ("ADMIN".equalsIgnoreCase(currentUser.getRole())) {
                isAdmin = true;
            }
            if ("MANAGER".equalsIgnoreCase(currentUser.getRole())) {
                isManager = true;
            }
            if ("INVESTOR".equalsIgnoreCase(currentUser.getRole())) {
                isInvestor = true;
            }
        }
        
        if (isAdmin) {
            Button manageInvestorsBtn = new Button("Manajemen Investor");
            manageInvestorsBtn.setId("manageInvestorsButton");
            manageInvestorsBtn.setPrefWidth(Double.MAX_VALUE); 
            manageInvestorsBtn.setOnAction(event -> handleShowManageInvestorsView());
            navigationButtonsPane.getChildren().add(manageInvestorsBtn);
        }

        if (isManager) { 
            Button addProductBtn = new Button("Tambah Produk");
            addProductBtn.setId("addProductButton");
            addProductBtn.setPrefWidth(Double.MAX_VALUE);
            addProductBtn.setOnAction(event -> handleShowAddProductView());
            navigationButtonsPane.getChildren().add(addProductBtn);
        }
    }
    
    @FXML
    private void handleLogoutAction() {
        loginBusinessController.logout();
        Stage currentStage = (Stage) logoutButton.getScene().getWindow(); 
        MainApp.showLoginPage(currentStage);
    }

    @FXML
    public void handleShowProductView() {
        loadViewWithControllerSetup("/ui/ProductListView.fxml", controller -> {
            if (controller instanceof ProductListController) {
                ((ProductListController) controller).setMainLayoutController(this);
            }
        });
    }

    @FXML
    public void handleShowRequestView() {
        loadViewWithControllerSetup("/ui/ProductRequestHistoryView.fxml", controller -> {
            if (controller instanceof ProductRequestHistoryController) {
                ((ProductRequestHistoryController) controller).setMainLayoutController(this);
            }
        });
    }

    @FXML
    public void handleShowTransactionView() {

        showPlaceholderView("Halaman Riwayat Transaksi akan segera tersedia.");
        System.out.println("handleShowTransactionView dipanggil - UI belum dimuat.");
        

        loadViewWithControllerSetup("/ui/TransactionHistoryView.fxml", controller -> {
        });

    }

    @FXML
    public void handleShowManageInvestorsView() {
        loadViewWithControllerSetup("/ui/ViewInvestorListPage.fxml", controller -> {});
    }
    
    @FXML
    public void handleShowAddProductView() {
        loadViewWithControllerSetup("/ui/AddProductView.fxml", controller -> {});
    }

    public void loadViewWithControllerSetup(String fxmlPath, Consumer<Object> controllerSetupLogic) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent viewRoot = loader.load();
            
            Object controller = loader.getController();
            if (controller != null && controllerSetupLogic != null) {
                controllerSetupLogic.accept(controller);
            }
            
            contentPane.setCenter(viewRoot);
        } catch (IOException e) {
            System.err.println("Gagal memuat view: " + fxmlPath + " - " + e.getMessage());
            Label errorLabel = new Label("Gagal memuat halaman: " + fxmlPath.substring(fxmlPath.lastIndexOf('/') + 1));
            errorLabel.setWrapText(true);
            contentPane.setCenter(errorLabel);
        }
    }
}