package com.example.cinemate.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cinemate.R;
import com.example.cinemate.database.DBHelper;

public class SignupActivity extends AppCompatActivity {

    EditText usernameInput, passwordInput, confirmPasswordInput;
    Button signupBtn;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        usernameInput = findViewById(R.id.editUsernameSignup);
        passwordInput = findViewById(R.id.editPasswordSignup);
        confirmPasswordInput = findViewById(R.id.editConfirmPassword);
        signupBtn = findViewById(R.id.buttonSignup);
        dbHelper = new DBHelper(this);

        signupBtn.setOnClickListener(v -> {
            String username = usernameInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();
            String confirmPassword = confirmPasswordInput.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "Semua field harus diisi", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!password.equals(confirmPassword)) {
                Toast.makeText(this, "Password dan konfirmasi tidak cocok", Toast.LENGTH_SHORT).show();
                return;
            }

            boolean registered = dbHelper.registerUser(username, password, "");
            if (registered) {
                Toast.makeText(this, "Sign up berhasil!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                intent.putExtra("username", username);
                intent.putExtra("image", "");
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Username sudah digunakan atau gagal mendaftar!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
