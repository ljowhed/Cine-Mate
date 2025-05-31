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
import com.example.cinemate.utils.SessionManager;

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

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Username dan password harus diisi!", Toast.LENGTH_SHORT).show();
                return;
            }

            Cursor res = dbHelper.checkLogin(username, password);
            if (res != null && res.moveToFirst()) {
                String name = res.getString(res.getColumnIndexOrThrow("username"));
                String profileImage = res.getString(res.getColumnIndexOrThrow("image"));

                SessionManager sessionManager = new SessionManager(this);

                sessionManager.saveLoginSession(name, profileImage, password);

                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Login gagal! Username atau password salah.", Toast.LENGTH_SHORT).show();
            }
        });

    }
}


