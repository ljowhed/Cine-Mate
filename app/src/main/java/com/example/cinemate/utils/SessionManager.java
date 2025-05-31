package com.example.cinemate.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {

    private static final String PREF_NAME = "UserPrefs";
    private static final String USER_NAME = "username";
    private static final String USER_PASSWORD = "password";
    private static final String KEY_IMAGE = "image";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";

    private final SharedPreferences sharedPreferences;
    private final SharedPreferences.Editor editor;

    public SessionManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    // Simpan data login lengkap
    public void saveLoginSession(String username, String imageUri, String password) {
        editor.putString(USER_NAME, username);
        editor.putString(KEY_IMAGE, imageUri);
        editor.putString(USER_PASSWORD, password);
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.apply();
    }


    // Simpan username saja (kalau perlu update)
    public void setUserName(String name) {
        editor.putString(USER_NAME, name);
        editor.apply();
    }

    // Simpan gambar baru
    public void setImageUri(String uri) {
        editor.putString(KEY_IMAGE, uri);
        editor.apply();
    }

    // Hanya logout (tidak hapus foto profil kalau ingin disimpan)
    public void logout() {
        editor.remove(USER_NAME);
        editor.remove(USER_PASSWORD);
        editor.remove(KEY_IS_LOGGED_IN);
        editor.apply();
    }

    public void setLoginStatus(boolean status) {
        editor.putBoolean(KEY_IS_LOGGED_IN, status);
        editor.apply();
    }


    // Getter
    public boolean isLoggedIn() {
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public String getUserName() {
        return sharedPreferences.getString(USER_NAME, "Guest");
    }

    public String getImageUri() {
        return sharedPreferences.getString(KEY_IMAGE, "");
    }

    public String getPassword() {
        return sharedPreferences.getString(USER_PASSWORD, "");
    }
}
