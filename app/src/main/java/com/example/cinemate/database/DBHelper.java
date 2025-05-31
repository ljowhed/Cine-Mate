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
    private static final int DATABASE_VERSION = 3;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_MOVIES_TABLE = "CREATE TABLE movies (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "title TEXT, " +
                "genre TEXT, " +
                "description TEXT, " +
                "image TEXT, " +
                "status TEXT)";
        db.execSQL(CREATE_MOVIES_TABLE);

        String CREATE_USERS_TABLE = "CREATE TABLE users (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "username TEXT, " +
                "password TEXT, " +
                "image TEXT)";
        db.execSQL(CREATE_USERS_TABLE);

        // Tambahkan data film awal
        db.execSQL("INSERT INTO movies (title, genre, description, image, status) VALUES " +
                "('Inside_Out_2', 'Animation', 'Riley menghadapi emosi baru saat beranjak remaja.', 'inside_out_2', 'Playing Now')," +
                "('Deadpool_3', 'Action', 'Deadpool bergabung dengan MCU dan membawa kekacauan.', 'deadpool_3', 'Playing Now')," +
                "('Harry Potter', 'Fantasy', 'Merupakan film fantasi tentang sihir.', 'poster_harry_potter', 'Playing Now')," +
                "('Avangers', 'Action', 'Merupakan film tentang pahlawan.', 'poster_avengers', 'Playing Now')," +
                "('Despicable Me 4', 'Comedy', 'Gru kembali dengan petualangan baru bersama para minion.', 'despicable_me_4', 'Playing Now')," +
                "('Batman', 'Action', 'Merupakan film tentang pahlawan Batman.', 'poster_batman', 'Coming Soon')," +
                "('Elemental', 'Fantasy', 'Film kartun tentang elemen air dan api.', 'poster_elemental', 'Coming Soon')," +
                "('Inception', 'Thriller', 'Film mata-mata penuh teka-teki.', 'poster_inception', 'Coming Soon')," +
                "('Waktu Maghrib 2', 'Horror', 'Film horor Indonesia.', 'poster_waktu_maghrib2', 'Playing Now')," +
                "('Little Forest', 'Slice Of Life', 'Film bertema hidup sederhana.', 'poster_little_forest', 'Coming Soon')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            db.execSQL("ALTER TABLE movies ADD COLUMN status TEXT");
        }
        if (oldVersion < 3) {
            db.execSQL("CREATE TABLE IF NOT EXISTS users (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "username TEXT, " +
                    "password TEXT, " +
                    "image TEXT)");
        }
    }

    // USER METHODS
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

    // MOVIE METHODS
    public void insertMovie(String title, String genre, String description, String image, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("title", title);
        values.put("genre", genre);
        values.put("description", description);
        values.put("image", image);
        values.put("status", status);

        db.insert("movies", null, values);
        db.close();
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

    private Movie parseMovie(Cursor cursor) {
        int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
        String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
        String image = cursor.getString(cursor.getColumnIndexOrThrow("image"));
        String genre = cursor.getString(cursor.getColumnIndexOrThrow("genre"));
        String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));
        String status = cursor.getString(cursor.getColumnIndexOrThrow("status"));

        return new Movie(id, title, image, genre, description, status, image);
    }

    public boolean checkUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT id FROM users WHERE username = ? AND password = ?", new String[]{username, password});
        boolean exists = cursor.moveToFirst();
        cursor.close();
        db.close();
        return exists;
    }


}
