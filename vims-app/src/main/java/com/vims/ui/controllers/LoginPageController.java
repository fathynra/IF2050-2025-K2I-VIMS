package com.vims.ui.controllers;

import com.vims.MainApp;
import com.vims.controller.LoginController;
import com.vims.model.Investor; 

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginPageController {

    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private Button loginButton;
    @FXML private Label errorLabel;

    private LoginController businessLoginController;

    public LoginPageController() {
        this.businessLoginController = new LoginController();
    }

    @FXML
    public void initialize() {
        errorLabel.setText("");
    }

    @FXML
    private void handleLoginAction() {
        String email = emailField.getText();
        String password = passwordField.getText();

        if (email.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Email dan password tidak boleh kosong.");
            return;
        }

        Investor loggedInUser = businessLoginController.login(email, password);

        if (loggedInUser != null) {
            errorLabel.setText(""); 
            System.out.println("Login berhasil dari UI: " + loggedInUser.getName() + " sebagai " + loggedInUser.getRole());

            try {
                Stage currentStage = (Stage) loginButton.getScene().getWindow();
                MainApp mainApp = new MainApp(); 
                MainApp.showMainLayout(currentStage); 

            } catch (Exception e) {
                e.printStackTrace();
                errorLabel.setText("Gagal navigasi ke halaman utama.");
            }

        } else {
            errorLabel.setText("Login gagal. Periksa kembali email atau password Anda.");
            passwordField.clear(); 
        }
    }
}