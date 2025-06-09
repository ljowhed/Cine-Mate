package com.example.cinemate.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cinemate.R;

public class DetailActivity extends AppCompatActivity {

    ImageView imgPoster;
    TextView tvTitle, tvDirector, tvGenre, tvAge, tvDuration, tvRating, tvDescription;
    Button btnChooseMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // Inisialisasi view
        imgPoster = findViewById(R.id.imgPoster);
        tvTitle = findViewById(R.id.tvTitle);
        tvDirector = findViewById(R.id.tvDirector);
        tvGenre = findViewById(R.id.tvGenre);
        tvAge = findViewById(R.id.tvAge);
        tvDuration = findViewById(R.id.tvDuration);
        tvRating = findViewById(R.id.tvRating);
        tvDescription = findViewById(R.id.tvDescription);
        btnChooseMovie = findViewById(R.id.btnChooseMovie);

        // ✅ Ambil data dari intent
        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        String image = intent.getStringExtra("image");
        String genre = intent.getStringExtra("genre");
        String description = intent.getStringExtra("description");
        String director = intent.getStringExtra("director");
        String ageRating = intent.getStringExtra("age_rating");
        String duration = intent.getStringExtra("duration");
        double rating = intent.getDoubleExtra("rating_score", 0.0);

        // ✅ Set data ke view
        int imageRes = getResources().getIdentifier(image, "drawable", getPackageName());
        imgPoster.setImageResource(imageRes);
        tvTitle.setText(title);
        tvDirector.setText("Director : " + director);
        tvGenre.setText(genre);
        tvAge.setText(ageRating);
        tvDuration.setText(duration);
        tvRating.setText(String.valueOf(rating));
        tvDescription.setText(description);

        btnChooseMovie.setOnClickListener(v -> {
            Toast.makeText(this, "You chose " + title, Toast.LENGTH_SHORT).show();
        });
    }
}
