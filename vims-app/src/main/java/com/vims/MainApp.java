package com.vims; 

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApp extends Application {

    private static Stage globalPrimaryStage; // Menyimpan referensi ke stage utama

    @Override
    public void start(Stage stage) {
        MainApp.globalPrimaryStage = stage; // Simpan stage yang diberikan oleh JavaFX
        stage.setTitle("VIMS - Login"); // Judul awal untuk jendela login
        showLoginPage(stage); // Panggil metode untuk menampilkan halaman login
    }

    // Metode untuk menampilkan halaman login
    public static void showLoginPage(Stage stageToUse) {
        try {
            // Path ke FXML login Anda (sesuaikan jika berbeda)
            // Menggunakan MainApp.class.getResource() lebih aman untuk path resource
            FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("/ui/LoginPage.fxml")); 
            Parent root = loader.load();
            Scene scene = new Scene(root, 500, 450); // Sesuaikan ukuran jendela login jika perlu

            stageToUse.setScene(scene);
            stageToUse.setTitle("VIMS - Login"); // Set judul lagi untuk konsistensi jika dipanggil dari logout
            if (!stageToUse.isShowing()) {
                stageToUse.show();
            }
        } catch (IOException e) {
            System.err.println("Gagal memuat LoginPage.fxml:");
            e.printStackTrace();
            // Di aplikasi nyata, Anda mungkin ingin menampilkan dialog error kepada pengguna
        }
    }

    // Metode untuk menampilkan layout utama setelah login berhasil
    // Stage currentStageToClose adalah stage login yang akan ditutup/diganti
    public static void showMainLayout(Stage currentStageToClose) {
        Stage stageForMainLayout = globalPrimaryStage; // Gunakan stage utama global

        // Jika stage login yang ditutup adalah stage yang berbeda dari stage utama global
        // (misalnya jika login adalah dialog terpisah), tutup stage login tersebut.
        // Namun, dalam alur kita saat ini, currentStageToClose akan sama dengan globalPrimaryStage.
        if (currentStageToClose != null && currentStageToClose != globalPrimaryStage) {
            currentStageToClose.close();
        }

        try {
            FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("/ui/MainLayout.fxml"));
            Parent root = loader.load();
            // Sesuaikan ukuran jendela untuk layout utama jika perlu
            Scene scene = new Scene(root, 900, 700); 

            stageForMainLayout.setTitle("VIMS - Vunguard Investment Management System");
            stageForMainLayout.setScene(scene);
            // Tidak perlu stageForMainLayout.show() jika scene di-set pada stage yang sudah visible
        } catch (IOException e) {
            System.err.println("Gagal memuat MainLayout.fxml:");
            e.printStackTrace();
        }
    }

    // Metode untuk mendapatkan primaryStage, mungkin berguna nanti
    public static Stage getPrimaryStage() {
        return globalPrimaryStage;
    }

    public static void main(String[] args) {
        launch(args); // Metode statis dari kelas Application untuk meluncurkan aplikasi JavaFX
    }
}