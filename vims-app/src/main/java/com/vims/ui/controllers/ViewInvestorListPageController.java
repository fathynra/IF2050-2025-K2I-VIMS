package com.vims.ui.controllers;

import com.vims.controller.InvestorController; 
import com.vims.model.Investor;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;

import java.util.List;

public class ViewInvestorListPageController {

    @FXML private TableView<Investor> investorTableView;
    @FXML private TableColumn<Investor, Integer> accountIdColumn;
    @FXML private TableColumn<Investor, String> nameColumn;
    @FXML private TableColumn<Investor, String> emailColumn;
    @FXML private TableColumn<Investor, String> roleColumn;
    @FXML private TableColumn<Investor, String> statusColumn;
    @FXML private TableColumn<Investor, Void> actionColumn; 

    private InvestorController businessInvestorController;
    private ObservableList<Investor> observableInvestorList;

    public ViewInvestorListPageController() {
        this.businessInvestorController = new InvestorController();
        this.observableInvestorList = FXCollections.observableArrayList();
    }

    @FXML
    public void initialize() {
        // Setup kolom tabel
        accountIdColumn.setCellValueFactory(new PropertyValueFactory<>("accountId"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        setupActionColumn();

        loadInvestorData();
    }

    private void setupActionColumn() {
        actionColumn.setCellFactory(param -> new TableCell<>() {
            private final Button banUnbanButton = new Button();
            private final HBox pane = new HBox(banUnbanButton);

            {
                pane.setSpacing(5);
                banUnbanButton.setOnAction(event -> {
                    Investor investor = getTableView().getItems().get(getIndex());
                    handleBanUnbanAction(investor);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    Investor investor = getTableView().getItems().get(getIndex());
                    if ("banned".equalsIgnoreCase(investor.getStatus())) {
                        banUnbanButton.setText("Unban");
                        banUnbanButton.setStyle("-fx-background-color: #5cb85c; -fx-text-fill: white;"); // Hijau
                    } else {
                        banUnbanButton.setText("Ban");
                        banUnbanButton.setStyle("-fx-background-color: #d9534f; -fx-text-fill: white;"); // Merah
                    }
                    setGraphic(pane);
                }
            }
        });
    }

    private void handleBanUnbanAction(Investor investor) {
        boolean success;
        String actionMessage;

        if ("banned".equalsIgnoreCase(investor.getStatus())) {
            success = businessInvestorController.unbanInvestor(investor.getAccountId());
            actionMessage = success ? "diaktifkan kembali." : "gagal diaktifkan.";
        } else {
            success = businessInvestorController.banInvestor(investor.getAccountId());
            actionMessage = success ? "diblokir." : "gagal diblokir.";
        }

        if (success) {
            showAlert(Alert.AlertType.INFORMATION, "Sukses", "Investor " + investor.getName() + " berhasil " + actionMessage);
        } else {
            showAlert(Alert.AlertType.ERROR, "Gagal", "Operasi pada investor " + investor.getName() + " " + actionMessage);
        }
        loadInvestorData(); 
    }

    @FXML
    private void handleRefreshAction() {
        loadInvestorData();
    }

    private void loadInvestorData() {
        List<Investor> investors = businessInvestorController.getAllInvestors();
        observableInvestorList.setAll(investors);
        investorTableView.setItems(observableInvestorList);
        System.out.println("Data investor dimuat ke tabel: " + (investors != null ? investors.size() : 0) + " item.");
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}