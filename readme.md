# 📈 Crypto Price Chart App

Aplikasi desktop sederhana yang dibuat dengan JavaFX untuk menampilkan grafik harga historis cryptocurrency dari API publik CoinGecko.

## ✨ Fitur
* Menampilkan data harga historis dalam bentuk grafik garis.
* Mengambil data secara real-time dari CoinGecko API.
* Pilihan beberapa cryptocurrency (Bitcoin, Ethereum, dll.).
* Kemampuan untuk mengunduh tampilan grafik sebagai file gambar PNG.

---

## 🛠️ Prasyarat
Sebelum menjalankan proyek ini, pastikan Anda telah menginstal:
* **Java Development Kit (JDK)**: Versi **17** atau yang lebih baru.
* **Apache Maven**: Versi 3.6 atau yang lebih baru.

---

## 🚀 Cara Menjalankan Aplikasi

1.  **Clone Repository Ini**
    ```bash
    git clone [https://github.com/NAMA_USER_ANDA/NAMA_REPO_ANDA.git](https://github.com/NAMA_USER_ANDA/NAMA_REPO_ANDA.git)
    cd NAMA_REPO_ANDA
    ```

2.  **Jalankan Menggunakan Maven**
    Buka terminal di folder utama proyek dan jalankan perintah berikut:
    ```bash
    mvn javafx:run
    ```
    Maven akan secara otomatis mengunduh semua dependensi yang dibutuhkan dan meluncurkan aplikasi.

---

## 📂 Struktur Proyek
Proyek ini menerapkan prinsip OOP dengan memisahkan tanggung jawab ke dalam beberapa kelas:

* `App.java`: **Controller** utama yang menginisialisasi aplikasi dan menghubungkan UI dengan logika bisnis.
* `MainView.java`: **View** yang bertanggung jawab untuk membangun dan mengelola semua komponen antarmuka (UI).
* `CoinGeckoApiService.java`: Layanan yang menangani semua komunikasi jaringan dengan API CoinGecko.
* `ChartDataParser.java`: Utilitas untuk mem-parsing respons JSON dari API menjadi data yang bisa ditampilkan di grafik.
* `ChartImageExporter.java`: Utilitas untuk mengambil *snapshot* dari grafik dan menyimpannya sebagai file gambar.

---

## 🤝 Kontribusi
Kontribusi sangat kami harapkan! Jika Anda ingin membantu, silakan:
1.  *Fork* repository ini.
2.  Buat *branch* baru untuk fitur Anda (`git checkout -b fitur/NamaFiturBaru`).
3.  *Commit* perubahan Anda (`git commit -m 'Menambahkan FiturBaru'`).
4.  *Push* ke *branch* Anda (`git push origin fitur/NamaFiturBaru`).
5.  Buka *Pull Request*.