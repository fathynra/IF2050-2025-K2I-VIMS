package com.vims;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApp extends Application {


    private static Stage globalPrimaryStage;

    @Override
    public void start(Stage stage) {
        MainApp.globalPrimaryStage = stage;
        stage.setTitle("VIMS - Login");
        showLoginPage(stage);

    }

    public static void showLoginPage(Stage stageToUse) {
        try {

            FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("/ui/LoginPage.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root, 500, 450);
            
            stageToUse.setScene(scene);
            stageToUse.setTitle("VIMS - Login");

            if (!stageToUse.isShowing()) {
                stageToUse.show();
            }
        } catch (IOException e) {
            System.err.println("Gagal memuat LoginPage.fxml:");
            e.printStackTrace();

        }
    }

    public static void showMainLayout(Stage stageToUse) {
        try {
            FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("/ui/MainLayout.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root, 950, 700); 
            
            stageToUse.setTitle("VIMS - Vunguard Investment Management System");
            stageToUse.setScene(scene);

        } catch (IOException e) {
            System.err.println("Gagal memuat MainLayout.fxml:");
            e.printStackTrace();
        }
    }
    
    public static Stage getPrimaryStage() {
        return globalPrimaryStage;
    }

    public static void main(String[] args) {
        launch(args);

}