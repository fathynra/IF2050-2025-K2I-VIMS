package com.vims.controller;

import com.vims.dao.InvestorDao;
import com.vims.model.Investor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Mock/Stub sederhana untuk InvestorDao khusus untuk testing LoginController.
 * Ini menggantikan InvestorDao asli agar tidak perlu koneksi database saat unit test.
 */
class MockInvestorDaoForLogin extends InvestorDao {
    private Map<String, Investor> usersDatabase = new HashMap<>();

    // Metode untuk menambahkan user ke "database" mock kita
    public void addMockUser(Investor user) {
        if (user != null && user.getEmail() != null) {
            usersDatabase.put(user.getEmail().toLowerCase(), user);
        }
    }

    @Override
    public Investor getInvestorByEmail(String email) {
        if (email == null) return null;
        return usersDatabase.get(email.toLowerCase());
    }

    // Override metode DAO lain yang mungkin secara tidak sengaja dipanggil agar tidak error
    // atau tidak melakukan apa-apa, karena fokus kita adalah getInvestorByEmail untuk login.
    @Override
    public boolean updateInvestor(Investor investor) {
        // Untuk tes login, ini mungkin tidak dipanggil. Jika dipanggil, kita bisa mock hasilnya.
        if (investor != null && usersDatabase.containsKey(investor.getEmail().toLowerCase())) {
            usersDatabase.put(investor.getEmail().toLowerCase(), investor);
            return true;
        }
        return false;
    }
    // Anda bisa override metode lain jika diperlukan oleh LoginController
}

public class LoginControllerTest {

    private LoginController loginController;
    private MockInvestorDaoForLogin mockInvestorDao;

    @BeforeEach
    void setUp() {
        // Setiap tes akan menggunakan instance baru dari mock DAO dan LoginController
        mockInvestorDao = new MockInvestorDaoForLogin();
        loginController = new LoginController(mockInvestorDao); // Menggunakan constructor injection

        // Tambahkan beberapa user contoh ke mock DAO kita
        Investor activeInvestor = new Investor(1, "Test Investor", "investor@vims.com", "pass123", "active", "INVESTOR", new BigDecimal("1000"));
        Investor adminUser = new Investor(2, "Test Admin", "admin@vims.com", "adminpass", "active", "ADMIN", new BigDecimal("0"));
        Investor bannedUser = new Investor(3, "Banned User", "banned@vims.com", "bannedpass", "banned", "INVESTOR", new BigDecimal("500"));

        mockInvestorDao.addMockUser(activeInvestor);
        mockInvestorDao.addMockUser(adminUser);
        mockInvestorDao.addMockUser(bannedUser);

        loginController.logout(); // Pastikan tidak ada user yang login di awal setiap tes
    }

    @Test
    @DisplayName("Login Berhasil dengan Kredensial Investor yang Valid dan Aktif")
    void login_shouldSucceed_whenCredentialsAreValidAndUserIsActive() {
        // Act
        Investor result = loginController.login("investor@vims.com", "pass123");

        // Assert
        assertNotNull(result, "Hasil login tidak boleh null untuk kredensial valid.");
        assertEquals("investor@vims.com", result.getEmail(), "Email pengguna yang login harus sesuai.");
        assertEquals("INVESTOR", result.getRole(), "Peran pengguna yang login harus sesuai.");
        assertEquals(result, LoginController.getCurrentLoggedInUser(), "Pengguna yang login harus diset sebagai pengguna saat ini.");
    }

    @Test
    @DisplayName("Login Gagal dengan Password Salah")
    void login_shouldFail_whenPasswordIsIncorrect() {
        // Act
        Investor result = loginController.login("investor@vims.com", "wrongpassword");

        // Assert
        assertNull(result, "Hasil login seharusnya null untuk password salah.");
        assertNull(LoginController.getCurrentLoggedInUser(), "Seharusnya tidak ada pengguna yang login.");
    }

    @Test
    @DisplayName("Login Gagal jika Email Tidak Ditemukan")
    void login_shouldFail_whenEmailNotFound() {
        // Act
        Investor result = loginController.login("unknown@vims.com", "pass123");

        // Assert
        assertNull(result, "Hasil login seharusnya null untuk email yang tidak ditemukan.");
        assertNull(LoginController.getCurrentLoggedInUser(), "Seharusnya tidak ada pengguna yang login.");
    }

    @Test
    @DisplayName("Login Gagal jika Akun Di-banned")
    void login_shouldFail_whenAccountIsBanned() {
        // Act
        Investor result = loginController.login("banned@vims.com", "bannedpass");

        // Assert
        assertNull(result, "Hasil login seharusnya null untuk akun yang dibanned.");
        assertNull(LoginController.getCurrentLoggedInUser(), "Seharusnya tidak ada pengguna yang login.");
    }

    @Test
    @DisplayName("Login Gagal jika Email Kosong")
    void login_shouldFail_whenEmailIsEmpty() {
        // Act
        Investor result = loginController.login("", "password");
        // Assert
        assertNull(result);
        assertNull(LoginController.getCurrentLoggedInUser());
    }

    @Test
    @DisplayName("Login Gagal jika Password Kosong")
    void login_shouldFail_whenPasswordIsEmpty() {
        // Act
        Investor result = loginController.login("test@example.com", "");
        // Assert
        assertNull(result);
        assertNull(LoginController.getCurrentLoggedInUser());
    }

    @Test
    @DisplayName("Logout Seharusnya Membersihkan Pengguna yang Login")
    void logout_shouldClearCurrentLoggedInUser() {
        // Arrange: login dulu
        loginController.login("admin@vims.com", "adminpass");
        assertNotNull(LoginController.getCurrentLoggedInUser(), "Seharusnya ada pengguna yang login sebelum logout.");

        // Act
        loginController.logout();

        // Assert
        assertNull(LoginController.getCurrentLoggedInUser(), "Seharusnya tidak ada pengguna yang login setelah logout.");
    }
}