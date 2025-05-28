package com.example.cinemate.activities;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cinemate.R;
import com.example.cinemate.models.Movie;

public class DetailActivity extends AppCompatActivity {
    ImageView imageView;
    TextView titleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        imageView = findViewById(R.id.detail_image);
        titleView = findViewById(R.id.detail_title);

        Movie movie = (Movie) getIntent().getSerializableExtra("MOVIE");
        if (movie != null) {
            imageView.setImageResource(Integer.parseInt(movie.getImage()));
            titleView.setText(movie.getTitle());
        }
    }
}
