package com.example.cinemate.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cinemate.R;
import com.example.cinemate.database.DBHelper;

public class SignupActivity extends AppCompatActivity {

    private EditText usernameInput, passwordInput, confirmPasswordInput;
    private Button signupBtn, selectImageBtn;
    private ImageView profileImageView;
    private Uri selectedImageUri;
    private DBHelper dbHelper;

    private final ActivityResultLauncher<Intent> imagePickerLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                            selectedImageUri = result.getData().getData();
                            profileImageView.setImageURI(selectedImageUri);
                        }
                    }
            );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        initViews();
        dbHelper = new DBHelper(this);

        selectImageBtn.setOnClickListener(v -> openImagePicker());
        signupBtn.setOnClickListener(v -> handleSignup());
    }

    private void initViews() {
        usernameInput = findViewById(R.id.editUsernameSignup);
        passwordInput = findViewById(R.id.editPasswordSignup);
        confirmPasswordInput = findViewById(R.id.editConfirmPassword);
        signupBtn = findViewById(R.id.buttonSignup);
        selectImageBtn = findViewById(R.id.buttonSelectImage);
        profileImageView = findViewById(R.id.profileImageView);
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        imagePickerLauncher.launch(intent);
    }

    private void handleSignup() {
        String username = usernameInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();
        String confirmPassword = confirmPasswordInput.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "Semua field harus diisi", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.length() < 6) {
            Toast.makeText(this, "Password minimal 6 karakter", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Konfirmasi password tidak cocok", Toast.LENGTH_SHORT).show();
            return;
        }

        String imageUriStr = selectedImageUri != null ? selectedImageUri.toString() : "";

        boolean registered = dbHelper.registerUser(username, password, imageUriStr);
        if (registered) {
            Toast.makeText(this, "Sign up berhasil!", Toast.LENGTH_SHORT).show();

            // Simpan username & image ke SharedPreferences
            getSharedPreferences("UserPrefs", MODE_PRIVATE).edit()
                    .putString("username", username)
                    .putString("image", imageUriStr)
                    .apply();

            // Redirect ke MainActivity
            Intent intent = new Intent(SignupActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Username sudah digunakan atau terjadi kesalahan!", Toast.LENGTH_SHORT).show();
        }
    }
}
