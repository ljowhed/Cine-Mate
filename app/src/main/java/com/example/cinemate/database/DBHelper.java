package com.example.cinemate.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.cinemate.models.Movie;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "cinemate.db";
    private static final int DATABASE_VERSION = 6;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE movies (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "title TEXT, " +
                "genre TEXT, " +
                "description TEXT, " +
                "image TEXT, " +
                "status TEXT, " +
                "duration TEXT, " +
                "age_rating TEXT, " +
                "rating_score REAL, " +
                "is_favorite INTEGER DEFAULT 0)");

        db.execSQL("CREATE TABLE users (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "username TEXT, " +
                "password TEXT, " +
                "image TEXT)");

        // Seed data awal (tanpa field baru agar tidak error)
        db.execSQL("INSERT INTO movies (title, genre, description, image, status,  duration, age_rating, rating_score) VALUES " +
                "('Inside_Out_2', 'Animation', 'Riley menghadapi emosi baru saat beranjak remaja.', 'inside_out_2', 'Playing Now', '1h 40m', 'SU', 8.5)," +
                "('Deadpool_3', 'Action', 'Deadpool bergabung dengan MCU dan membawa kekacauan.', 'deadpool_3', 'Playing Now', '2h 5m', '17+', 8.9)," +
                "('Harry Potter', 'Fantasy', 'Merupakan film fantasi tentang sihir.', 'poster_harry_potter', 'Playing Now', '2h 32m', '13+', 8.2)," +
                "('Avangers', 'Action', 'Merupakan film tentang pahlawan.', 'poster_avengers', 'Playing Now', '2h 23m', '13+', 8.4)," +
                "('Despicable Me 4', 'Comedy', 'Gru kembali dengan petualangan baru bersama para minion.', 'despicable_me_4', 'Playing Now', '1h 35m', 'SU', 7.8)," +
                "('Batman', 'Action', 'Merupakan film tentang pahlawan Batman.', 'poster_batman', 'Coming Soon', '2h 15m', '13+', 8.1)," +
                "('Elemental', 'Fantasy', 'Film kartun tentang elemen air dan api.', 'poster_elemental', 'Coming Soon', '1h 45m', 'SU', 7.6)," +
                "('Inception', 'Thriller', 'Film mata-mata penuh teka-teki.', 'poster_inception', 'Coming Soon', '2h 28m', '17+', 8.8)," +
                "('Waktu Maghrib 2', 'Horror', 'Film horor Indonesia.', 'poster_waktu_maghrib2', 'Playing Now', '1h 50m', '13+', 7.5)," +
                "('Little Forest', 'Slice Of Life', 'Film bertema hidup sederhana.', 'poster_little_forest', 'Coming Soon', '1h 43m', 'SU', 8.0)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("ALTER TABLE movies ADD COLUMN rating REAL"); // atau TEXT jika rating Anda string
    }


    // ===== USER SECTION =====

    public boolean isUsernameExists(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT id FROM users WHERE username = ?", new String[]{username});
        boolean exists = cursor.moveToFirst();
        cursor.close();
        db.close();
        return exists;
    }

    public boolean registerUser(String username, String password, String imageUri) {
        if (isUsernameExists(username)) return false;

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("password", password);
        values.put("image", imageUri);

        long result = db.insert("users", null, values);
        db.close();
        return result != -1;
    }

    public Cursor checkLogin(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM users WHERE username=? AND password=?", new String[]{username, password});
    }

    public boolean updatePassword(String username, String newPassword) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("password", newPassword);
        int rows = db.update("users", values, "username = ?", new String[]{username});
        db.close();
        return rows > 0;
    }

    public boolean updateImageUri(String username, String imageUri) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("image", imageUri);
        int rows = db.update("users", values, "username = ?", new String[]{username});
        db.close();
        return rows > 0;
    }

    public boolean checkUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT id FROM users WHERE username = ? AND password = ?", new String[]{username, password});
        boolean exists = cursor.moveToFirst();
        cursor.close();
        db.close();
        return exists;
    }

    // ===== MOVIE SECTION =====

    public void insertMovie(String title, String genre, String description, String image, String status,
                            String duration, String ageRating, double ratingScore, boolean isFavorite) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("title", title);
        values.put("genre", genre);
        values.put("description", description);
        values.put("image", image);
        values.put("status", status);
        values.put("duration", duration);
        values.put("age_rating", ageRating);
        values.put("rating_score", ratingScore);
        values.put("is_favorite", isFavorite ? 1 : 0);
        db.insert("movies", null, values);
        db.close();
    }

    public void updateFavorite(int movieId, boolean isFavorite) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("is_favorite", isFavorite ? 1 : 0);
        db.update("movies", values, "id = ?", new String[]{String.valueOf(movieId)});
        db.close();
    }

    public List<Movie> getFavoriteMovies() {
        List<Movie> favoriteList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM movies WHERE is_favorite = 1", null);

        if (cursor.moveToFirst()) {
            do {
                favoriteList.add(parseMovie(cursor));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return favoriteList;
    }

    public ArrayList<Movie> getAllMovies() {
        ArrayList<Movie> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM movies WHERE status = 'Playing Now'", null);

        if (cursor.moveToFirst()) {
            do {
                list.add(parseMovie(cursor));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return list;
    }

    public List<Movie> getComingSoonMovies() {
        List<Movie> movieList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM movies WHERE status = 'Coming Soon'", null);

        if (cursor.moveToFirst()) {
            do {
                movieList.add(parseMovie(cursor));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return movieList;
    }

    public ArrayList<Movie> getMoviesByGenre(String genreFilter) {
        ArrayList<Movie> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM movies WHERE genre = ?", new String[]{genreFilter});

        if (cursor.moveToFirst()) {
            do {
                list.add(parseMovie(cursor));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return list;
    }

    private Movie parseMovie(Cursor cursor) {
        int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
        String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
        String genre = cursor.getString(cursor.getColumnIndexOrThrow("genre"));
        String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));
        String image = cursor.getString(cursor.getColumnIndexOrThrow("image"));
        String status = cursor.getString(cursor.getColumnIndexOrThrow("status"));
        String duration = cursor.getString(cursor.getColumnIndexOrThrow("duration"));
        String ageRating = cursor.getString(cursor.getColumnIndexOrThrow("age_rating"));
        double ratingScore = cursor.getDouble(cursor.getColumnIndexOrThrow("rating_score"));
        boolean isFavorite = cursor.getInt(cursor.getColumnIndexOrThrow("is_favorite")) == 1;

        return new Movie(id, title, image, genre, description, status, duration, ageRating, ratingScore, isFavorite);
    }
}
