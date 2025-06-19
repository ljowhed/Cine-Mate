
# CineMate: Teman Nonton Film Pribadi Anda 🎬

CineMate adalah aplikasi mobile berbasis Android yang dirancang untuk memberikan pengalaman menonton film yang lebih personal, interaktif, dan menyenangkan bagi penggunanya. Di tengah pesatnya perkembangan teknologi digital, CineMate hadir untuk menjawab kebutuhan akan kemudahan dalam menemukan film favorit, memberikan penilaian, dan berbagi pengalaman menonton dengan sesama penggemar film.

-----

## 🌟 Fitur Utama

CineMate menawarkan berbagai fitur untuk meningkatkan pengalaman menonton film Anda:

  * **Pencarian Film Lengkap:** Temukan informasi film secara cepat dan akurat, termasuk sinopsis, durasi, rating, sutradara, dan genre.
  * **Daftar Favorit:** Buat dan kelola daftar film yang ingin Anda tonton atau sukai dengan mudah.
  * **Rekomendasi Personal:** Dapatkan rekomendasi film yang disesuaikan dengan minat dan preferensi tontonan Anda.
  * **Interaksi Sosial:** Berbagi ulasan, memberi rating, dan berdiskusi dengan pengguna lain yang memiliki minat serupa.
  * **Antarmuka Intuitif (UI/UX):** Nikmati desain antarmuka yang menarik, bersih, dan mudah digunakan, kompatibel dengan berbagai perangkat Android.
  * **Tonton Film:** Langsung tonton film yang Anda pilih dalam aplikasi (dengan asumsi integrasi API tontonan film).
  * **Manajemen Profil:** Kelola informasi akun Anda, termasuk foto profil dan kata sandi.

-----

## 🛠️ Teknologi yang Digunakan

Aplikasi CineMate dibangun dengan teknologi inti berikut:

  * **Bahasa Pemrograman:** Java
  * **IDE:** Android Studio
  * **Database Lokal:** SQLite
  * **Format Data:** JSON (untuk komunikasi dengan API film)
  * **Kontrol Versi:** Git & GitHub

-----

## 🚀 Instalasi & Penggunaan

Untuk menjalankan proyek CineMate di lingkungan pengembangan Anda, ikuti langkah-langkah berikut:

1.  **Clone Repositori:**
    ```bash
    git clone https://github.com/YOUR_USERNAME/YOUR_REPO_NAME.git
    ```
2.  **Buka di Android Studio:**
    Buka folder proyek yang telah di-clone menggunakan Android Studio.
3.  **Sinkronisasi Gradle:**
    Biarkan Android Studio melakukan sinkronisasi Gradle untuk mengunduh semua dependensi yang diperlukan.
4.  **Siapkan Emulator/Perangkat Fisik:**
    Pastikan Anda memiliki Android Virtual Device (AVD) yang sudah diatur atau perangkat Android fisik yang terhubung untuk menjalankan aplikasi.
5.  **Jalankan Aplikasi:**
    Klik tombol `Run` (ikon segitiga hijau) di Android Studio.

-----

## 💡 Flow Aplikasi

Berikut adalah gambaran umum alur navigasi dalam aplikasi CineMate:

1.  **Splashscreen:** Animasi logo singkat saat aplikasi dimuat.
2.  **Halaman Utama (Sebelum Login):**
      * Tampilan film yang sedang tayang dan akan datang.
      * Fitur pencarian film.
      * Navigasi terbatas: Home, Favorite, Profile (belum dapat diakses penuh).
      * Tombol "Sign In" dan opsi "Sign Up".
3.  **Sign In:** Masukkan Username dan Password untuk login.
4.  **Sign Up:** Buat akun baru dengan mengisi Username, Password, dan Konfirmasi Password.
5.  **Halaman Utama (Setelah Login):**
      * Sapaan personal ("Halo, [Nama Pengguna]").
      * Akses penuh ke semua fitur daftar film dan navigasi.
6.  **Halaman Favorit:** Tampilkan daftar film yang telah Anda tandai sebagai favorit.
7.  **Halaman Detail Film:** Lihat informasi lengkap film (genre, durasi, rating, sutradara, sinopsis) dan tombol "Playing Now" untuk menonton.
8.  **Halaman Film:** Streaming film yang dipilih.
9.  **Halaman Profil:** Lihat dan kelola informasi akun (foto, nama, ganti password, logout).

-----

## 📸 Tampilan Aplikasi

1. **Splash Screen**

   
   ![image](https://github.com/user-attachments/assets/b59590d1-eeb9-4b0b-8a9c-0500394bb49e)

   
3. **Halaman Beranda Sebelum Login**

   
   ![image](https://github.com/user-attachments/assets/fbb3be56-3b64-486d-8db5-c87359aab2a7)

   
5. **Halaman Detail Film**


   ![image](https://github.com/user-attachments/assets/cb5c9552-a49c-4cf8-a8f0-af6611d38b6c)

   
7. **Halaman Favorite film**

   
   ![image](https://github.com/user-attachments/assets/5a154620-57f8-4f4d-954f-d4d645f421a7)
   

-----


## 🔮 Rencana Pengembangan Lanjutan

Kami terus berinovasi untuk CineMate\! Beberapa fitur yang kami pertimbangkan untuk pengembangan di masa mendatang meliputi:

  * Sistem keamanan dan autentikasi yang lebih kuat.
  * Integrasi API film secara real-time untuk informasi yang selalu akurat dan terkini.
  * Algoritma rekomendasi film yang lebih canggih.
  * Fitur notifikasi untuk informasi penting (misalnya, film baru dirilis).
  * Pemanfaatan *cloud* untuk sinkronisasi data antar perangkat.
  * Fitur histori tontonan pengguna.

-----

Semoga CineMate dapat menjadi teman setia bagi Anda para pecinta dunia perfilman\!

