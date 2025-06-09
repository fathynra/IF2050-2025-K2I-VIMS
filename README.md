# VIMS – Vanguard Investment Management System

## 🌐Deskripsi Singkat 
VIMS adalah sistem manajemen aset investasi berbasis web yang dikembangkan untuk perusahaan investment management Vunguard. Sistem ini bertujuan untuk menyederhanakan proses pencatatan, pemantauan, dan pelaporan data investasi yang selama ini dilakukan secara manual. Hal ini dilakukan untuk mengurangi kerentanan terhadap kesalahan dan mendukung pengambilan keputusan berbasis data dengan lebih cepat dan akurat.

**Fitur utama sistem meliputi:**
- Manajemen produk investasi (tambah, hapus produk)
- Pembelian, penjualan, dan permintaan penambahan produk oleh **investor**
- Persetujuan/penolakan permintaan oleh **manajer investasi**
- Manajemen akun investor oleh **admin** (ban/unban)

## ⚙️Cara Menjalankan Aplikasi

### 1. Clone Repository
- `git clone` dari repository berikut “[IF2050-2025-K2I-VIMS](https://github.com/bryanadi989/IF2050-2025-K2I-VIMS.git)”

### 2. Set Up Repository
   - **Jalankan** `schema.sql` **(Struktur Tabel)**
     - Buka MySQL Workbench.
     - Connect ke localhost atau server MySQL.
     - Buat schema baru:
       - Klik kanan pada panel **SCHEMAS** → `Create Schema...` → beri nama: `vims` → klik `Apply`.
     - Aktifkan schema `vims` (klik dua kali sampai bold).
     - Import file `schema.sql`:
       - Menu: `File` → `Open SQL Script...` → pilih file `schema.sql` dari folder project → klik `Open`.
     - Jalankan script:
       - Klik ikon ⚡ (Execute All).
        
   - **Jalankan** `seeder.sql` **(Data Awal)**
     - Tetap di database `vims` (harus tetap aktif).
     - Import file `seeder.sql`:
       - Menu: `File` → `Open SQL Script...` → pilih `seeder.sql` → klik `Open`.
     - Jalankan script:
       - Klik ikon ⚡ (Execute All).

### 3. Konfigurasi Database di Aplikasi
   - Buka file konfigurasi koneksi _database_
     - `vims-app/src/main/java/com/vims/util/DatabaseConnection.java`
   - Ubah baris berikut agar sesuai dengan username dan password MySQL lokal
     ```java
     private static final String DB_USER = "root";
     private static final String DB_PASSWORD = "your_password";
     ```

### 4. Jalankan Aplikasi di VS Code
   - Buka file utama aplikasi:
     - `vims-app/src/main/java/com/vims/MainApp.java`
   - Jalankan fungsi utama dengan tekan run di atas fungsi berikut.
     - ![Screenshot 2025-06-07 124651](https://github.com/user-attachments/assets/2d9912ba-5af8-42c9-ad1b-d28ffcec11a6)

### 5. Login ke VIMS dapat menggunakan akun yang ada di database VIMS, tepatnya di tabel INVESTOR. Berikut 3 akun yang dapat langsung digunakan : 
   - Email : admin@vims.com ; password : adminpassword
   - Email : manager@vims.com ; password : managerpassword
   - Email : juankoch@example.com ; password : investorpassword

## 🧩Daftar Modul yang Diimplementasi 

Berikut adalah daftar modul utama dalam sistem VIMS beserta pembagian tugas.


### 📖 Modul: Model, Controller, DAO, dan Login
- **Deskripsi:**  
  Membangun struktur logika aplikasi (Model-Controller), koneksi database (DAO), serta autentikasi login awal pengguna.
- **Dikerjakan oleh:**  
  Bryan Adi Priasmoro – 18223087


### 📖 Modul: Product Interaction (lihat detail, beli, jual)
- **Deskripsi:**  
  Menampilkan daftar produk investasi, detail produk, fitur pembelian dan penjualan produk.
- **Dikerjakan oleh:**  
  Bryan Adi Priasmoro – 18223087


### 📖 Modul: Transaksi (View & Riwayat)
- **Deskripsi:**  
  Menampilkan daftar transaksi pembelian dan penjualan yang dilakukan investor.
- **Dikerjakan oleh:**  
  Vincentia Belinda Sumartoyo – 18223078  
  Farella Kamala Budianto – 18223046


### 📖 Modul: Permintaan Produk (Manager – Add Produk)
- **Deskripsi:**  
  Fitur manajer investasi untuk menambahkan produk investasi baru ke sistem.
- **Dikerjakan oleh:**  
  Fathimah Nurhumaida Ramadhani – 18223052


### 📖 Modul: Setup Database (SQL)
- **Deskripsi:**  
  Menyiapkan struktur database (`schema.sql`) dan data awal (`seeder.sql`).
- **Dikerjakan oleh:**  
  Fathimah Nurhumaida Ramadhani – 18223052


### 📖 Modul: Manajemen Investor (Admin)
- **Deskripsi:**  
  Admin dapat melihat semua akun investor, melakukan aksi Ban/Unban.
- **Dikerjakan oleh:**  
  Nurul Na’im Natifah – 18223106


### 📘 Laporan
- **Fathimah Nurhumaida Ramadhani** **- 18223052**: Bab 2 + 4.1
- **Nurul Na’im Natifah** **- 18223106**: Bab 3 
- **Vincentia Belinda Sumartoyo - 18223078**: Bab 4.2.1 - 4.2.11
- **Farella Kamala Budianto - 18223046**: Bab 4.3 + 5
- **Bryan Adi Priasmoro - 18223087**: Finalisasi laporan


## 📄 Daftar Tabel Basis Data yang Diimplementasi
- ### investor

| Nama Atribut | Tipe Data                          | Keterangan            |
|--------------|-------------------------------------|------------------------|
| `account_id` | `int` AUTO_INCREMENT (PK)          | ID unik akun           |
| `name`       | `varchar(255)`                    | Nama investor          |
| `email`      | `varchar(255)`                    | Email pengguna         |
| `password`   | `varchar(255)`                    | Password akun          |
| `status`     | `varchar(20)`         | Status akun (`active`,`banned`)           |
| `role`       | `varchar(20)` | Peran pengguna (`INVESTOR`,`MANAGER`,`ADMIN`)    |
| `balance`    | `decimal(15,2)`                   | Saldo akun             |

- ### investment_product
| Nama Atribut  | Tipe Data                                | Keterangan                          |
|---------------|-------------------------------------------|-------------------------------------|
| `product_id`  | `int` AUTO_INCREMENT (PK)                | ID unik produk                      |
| `name`        | `varchar(255)`                          | Nama produk investasi               |
| `type`        | `varchar(20)`    | Jenis produk (`stock`,`bond`,`real estate`)                       |
| `risk_level`  | `varchar(50)`                           | Tingkat risiko                      |
| `description` | `text`                                  | Deskripsi produk                    |
| `unit_price`  | `decimal(10,2)`                         | Harga per unit produk               |

- ### transactions
| Nama Atribut     | Tipe Data                               | Keterangan                          |
|------------------|------------------------------------------|-------------------------------------|
| `transaction_id` | `int` AUTO_INCREMENT (PK)               | ID transaksi                        |
| `datetime`       | `datetime`                              | Tanggal dan waktu transaksi         |
| `quantity`       | `int`                                   | Jumlah unit yang dibeli/dijual      |
| `type`           | `varchar(10)`                    | Jenis transaksi (`buy` atau `sell`)                    |
| `account_id`     | `int` (FK ke `investor.account_id`)     | ID investor                         |
| `product_id`     | `int` (FK ke `investment_product.product_id`) | ID produk                     |

- ### product_requests
| Nama Atribut   | Tipe Data                             | Keterangan                                 |
|----------------|----------------------------------------|--------------------------------------------|
| `idRequest`    | `varchar(50)` (PK)                    | ID permintaan                              |
| `idInvestor`   | `int` (FK ke `investor.account_id`)   | ID investor yang mengajukan permintaan     |
| `nameProduct`  | `varchar(255)`                        | Nama produk yang diminta                   |
| `productType`  | `varchar(100)`                        | Jenis produk yang diminta                  |
| `reason`       | `text`                                | Alasan permintaan                          |
| `status`       | `varchar(20)`                         | Status permintaan (`PENDING`, dll.)        |

- ### investor_investment
| Nama Atribut     | Tipe Data                             | Keterangan                                      |
|------------------|----------------------------------------|-------------------------------------------------|
| `account_id`     | `int` (FK ke `investor.account_id`)   | ID investor                                     |
| `product_id`     | `int` (FK ke `investment_product.product_id`) | ID produk                                 |
| `quantity_owned` | `int`                                | Jumlah unit produk yang dimiliki oleh investor |
