package com.example.cinemate.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cinemate.R;
import com.example.cinemate.database.DBHelper;

public class LoginActivity extends AppCompatActivity {

    EditText usernameInput, passwordInput;
    Button loginBtn;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameInput = findViewById(R.id.editUsername);
        passwordInput = findViewById(R.id.editPassword);
        loginBtn = findViewById(R.id.buttonLogin);
        dbHelper = new DBHelper(this);
        Button signupBtn = findViewById(R.id.buttonToSignup);
        signupBtn.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
            startActivity(intent);
        });



        loginBtn.setOnClickListener(v -> {
            String username = usernameInput.getText().toString();
            String password = passwordInput.getText().toString();

            Cursor res = dbHelper.checkLogin(username, password);
            if (res.getCount() > 0) {
                res.moveToFirst();
                String name = res.getString(1); // kolom 1 = nama
                String profileImage = res.getString(3); // kolom 3 = URI gambar profil

                // Simpan ke SharedPreferences setelah login sukses
                SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("isLoggedIn", true);
                editor.putString("username", name);
                editor.putString("image", profileImage);
                editor.apply();

                // Pindah ke MainActivity
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Login failed!", Toast.LENGTH_SHORT).show();
            }
        });

    }
}


