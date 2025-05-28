package com.example.cinemate.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    private static final String PREF_NAME = "UserPrefs";
    private static final String USER_NAME = "username";
    private static final String KEY_IMAGE = "image";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";

    private final SharedPreferences sharedPreferences;
    private final SharedPreferences.Editor editor;

    public SessionManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void saveLoginSession(String username, String imageUri) {
        editor.putString(USER_NAME, username);
        editor.putString(KEY_IMAGE, imageUri);
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.apply();
    }

    public void logout() {
        editor.clear();
        editor.apply();
    }

    public boolean isLoggedIn() {
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public String getUserName() {
        return sharedPreferences.getString(USER_NAME, "Guest");
    }

    public String getImageUri() {
        return sharedPreferences.getString(KEY_IMAGE, "");
    }


}
