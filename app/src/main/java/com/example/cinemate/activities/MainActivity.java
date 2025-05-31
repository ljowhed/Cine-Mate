package com.example.cinemate.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.cinemate.Fragments.ProfileFragment;
import com.example.cinemate.R;
import com.example.cinemate.Fragments.FavoriteFragment;
import com.example.cinemate.Fragments.HomeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Wajib dipanggil dulu sebelum akses elemen layout
        setContentView(R.layout.activity_main);

        // Ambil referensi view dari layout
        TextView textViewHalo = findViewById(R.id.textViewHalo);
        ImageView imageProfile = findViewById(R.id.imageViewProfile);
        TextView signInButton = findViewById(R.id.signInButton); // atau Button sesuai yang kamu pakai

        // Ambil data dari intent
        Intent intent = getIntent();
        String username = intent.getStringExtra("username");
        String profileImage = intent.getStringExtra("image");

        if (username != null) {
            textViewHalo.setText("Halo, " + username);
            imageProfile.setVisibility(View.VISIBLE);
            signInButton.setVisibility(View.GONE);
            // imageProfile.setImageResource(R.drawable.default_profile); // kalau ingin pakai gambar
        } else {
            textViewHalo.setText("Sign In");
            imageProfile.setVisibility(View.GONE);
            signInButton.setVisibility(View.VISIBLE);
        }

        // Aksi ketika tombol Sign In ditekan
        signInButton.setOnClickListener(v -> {
            Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(loginIntent);
        });

        // Bottom Navigation
        bottomNav = findViewById(R.id.bottom_nav);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new HomeFragment())
                    .commit();
        }

        bottomNav.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            if (item.getItemId() == R.id.menu_profile) {
                selectedFragment = new ProfileFragment();
            } else if (item.getItemId() == R.id.menu_home) {
                selectedFragment = new HomeFragment();
            } // tambahkan yang lain sesuai kebutuhan

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
            return true;
        });

    }
}
