package com.vims.ui.controllers;

import com.vims.controller.LoginController;
import com.vims.controller.RequestController; 
import com.vims.model.Investor;
import com.vims.model.ProductRequest;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;

import java.util.List;

public class ProductRequestHistoryController {

    @FXML private Label pageTitleLabel;
    @FXML private TableView<ProductRequest> requestHistoryTableView;
    @FXML private TableColumn<ProductRequest, String> requestIdColumn;
    @FXML private TableColumn<ProductRequest, Integer> investorIdColumn; 
    @FXML private TableColumn<ProductRequest, String> productNameColumn;
    @FXML private TableColumn<ProductRequest, String> productTypeColumn;
    @FXML private TableColumn<ProductRequest, String> statusColumn;
    @FXML private TableColumn<ProductRequest, Void> actionColumn;
    @FXML private Label infoLabel;
    @FXML private Button addNewRequestButton;


    private RequestController businessRequestController;
    private MainLayoutController mainLayoutController;
    private ObservableList<ProductRequest> observableRequestList;
    private Investor currentUser;

    public ProductRequestHistoryController() {
        this.businessRequestController = new RequestController();
        this.observableRequestList = FXCollections.observableArrayList();
        this.currentUser = LoginController.getCurrentLoggedInUser(); 
    }

    public void setMainLayoutController(MainLayoutController mainLayoutController) {
        this.mainLayoutController = mainLayoutController;
    }

    @FXML
    public void initialize() {
        requestIdColumn.setCellValueFactory(new PropertyValueFactory<>("idRequest"));
        investorIdColumn.setCellValueFactory(new PropertyValueFactory<>("idInvestor")); 
        productNameColumn.setCellValueFactory(new PropertyValueFactory<>("nameProduct"));
        productTypeColumn.setCellValueFactory(new PropertyValueFactory<>("productType"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        requestHistoryTableView.setItems(observableRequestList);
        configureViewBasedOnRole(); 
    }

    private void configureViewBasedOnRole() {
        if (currentUser == null) {
            infoLabel.setText("Sesi pengguna tidak ditemukan.");
            addNewRequestButton.setVisible(false);
            investorIdColumn.setVisible(false); 
            actionColumn.setVisible(false);
            return;
        }

        String userRole = currentUser.getRole();
        if ("INVESTOR".equalsIgnoreCase(userRole)) {
            pageTitleLabel.setText("Riwayat Permintaan Produk Anda");
            addNewRequestButton.setVisible(true);
            investorIdColumn.setVisible(false); 
            actionColumn.setVisible(false); 
            loadInvestorRequestHistory();
        } else if ("MANAGER".equalsIgnoreCase(userRole) || "ADMIN".equalsIgnoreCase(userRole)) {
            pageTitleLabel.setText("Daftar Permintaan Produk dari Investor");
            addNewRequestButton.setVisible(false); 
            investorIdColumn.setVisible(true);  
            actionColumn.setVisible(true);   
            setupManagerActionColumn();      
            loadAllPendingRequestsForManager(); 
        } else {
            infoLabel.setText("Peran pengguna tidak dikenali.");
            addNewRequestButton.setVisible(false);
            investorIdColumn.setVisible(false);
            actionColumn.setVisible(false);
        }
    }

    private void loadInvestorRequestHistory() {
        List<ProductRequest> requests = businessRequestController.getProductRequestsByInvestor(currentUser.getAccountId());
        populateTable(requests, "Anda belum memiliki riwayat permintaan produk.");
    }

    private void loadAllPendingRequestsForManager() {
        List<ProductRequest> requests = businessRequestController.getAllPendingProductRequests();
        populateTable(requests, "Tidak ada permintaan produk yang PENDING saat ini.");
    }

    private void populateTable(List<ProductRequest> requests, String emptyMessage) {
        if (requests != null && !requests.isEmpty()) {
            observableRequestList.setAll(requests);
            infoLabel.setText("Menampilkan " + requests.size() + " permintaan.");
        } else {
            observableRequestList.clear();
            infoLabel.setText(emptyMessage);
        }
    }
    
    private void setupManagerActionColumn() {
        actionColumn.setCellFactory(param -> new TableCell<>() {
            private final Button approveButton = new Button("Approve");
            private final Button rejectButton = new Button("Reject");
            private final HBox pane = new HBox(5, approveButton, rejectButton); 

            {
                approveButton.setStyle("-fx-background-color: #5cb85c; -fx-text-fill: white;");
                rejectButton.setStyle("-fx-background-color: #d9534f; -fx-text-fill: white;");

                approveButton.setOnAction(event -> {
                    ProductRequest request = getTableView().getItems().get(getIndex());
                    handleApproveRequest(request);
                });
                rejectButton.setOnAction(event -> {
                    ProductRequest request = getTableView().getItems().get(getIndex());
                    handleRejectRequest(request);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    ProductRequest request = getTableView().getItems().get(getIndex());
                    if ("PENDING".equalsIgnoreCase(request.getStatus())) {
                        setGraphic(pane);
                    } else {
                        setGraphic(null); 
                    }
                }
            }
        });
    }

    private void handleApproveRequest(ProductRequest request) {
        System.out.println("Manajer approve permintaan: " + request.getIdRequest());
        boolean success = businessRequestController.approveProductRequest(request.getIdRequest());
        if (success) {
            showAlert(Alert.AlertType.INFORMATION, "Sukses", "Permintaan produk '" + request.getNameProduct() + "' berhasil disetujui.");
        } else {
            showAlert(Alert.AlertType.ERROR, "Gagal", "Gagal menyetujui permintaan produk.");
        }
        loadAllPendingRequestsForManager(); 
    }

    private void handleRejectRequest(ProductRequest request) {
        System.out.println("Manajer reject permintaan: " + request.getIdRequest());
        boolean success = businessRequestController.rejectProductRequest(request.getIdRequest());
         if (success) {
            showAlert(Alert.AlertType.INFORMATION, "Sukses", "Permintaan produk '" + request.getNameProduct() + "' berhasil ditolak.");
        } else {
            showAlert(Alert.AlertType.ERROR, "Gagal", "Gagal menolak permintaan produk.");
        }
        loadAllPendingRequestsForManager(); 
    }

    @FXML
    private void handleAddNewRequestButton() {
        if (mainLayoutController != null) {
            mainLayoutController.loadViewWithControllerSetup("/ui/ProductRequestView.fxml", controller -> {
                if (controller instanceof ProductRequestViewController) {
                    ((ProductRequestViewController) controller).setMainLayoutController(mainLayoutController);
                }
            });
        } else {
            showAlert(Alert.AlertType.ERROR, "Error Navigasi", "Tidak dapat membuka form permintaan baru.");
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