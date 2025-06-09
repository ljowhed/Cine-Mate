package com.example.cinemate.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cinemate.R;
import com.example.cinemate.activities.DetailActivity;
import com.example.cinemate.database.DBHelper;
import com.example.cinemate.models.Movie;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {
    private final Context context;
    private List<Movie> movieList;

    private DBHelper dbHelper;

    public MovieAdapter(Context context, List<Movie> movieList, DBHelper dbHelper) {
        this.context = context;
        this.movieList = movieList;
        this.dbHelper = dbHelper;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_movie, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Movie movie = movieList.get(position);
        holder.txtTitle.setText(movie.getTitle());

        int imageResId = context.getResources().getIdentifier(movie.getImage(), "drawable", context.getPackageName());
        if (imageResId != 0) {
            holder.imgMovie.setImageResource(imageResId);
        } else {
            holder.imgMovie.setImageResource(R.drawable.banner1); // fallback image
        }

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetailActivity.class);
            intent.putExtra("title", movie.getTitle());
            intent.putExtra("imageResId", movie.getImage());
            intent.putExtra("description", movie.getDescription());
            context.startActivity(intent);
        });

        // Atur ikon sesuai status favorite
        if (movie.isFavorite()) {
            holder.btnFavorite.setImageResource(R.drawable.ic_favorite_filled);
        } else {
            holder.btnFavorite.setImageResource(R.drawable.ic_favorite_border);
        }

// Handle klik favorite
        holder.btnFavorite.setOnClickListener(v -> {
            SharedPreferences prefs = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
            boolean isLoggedIn = prefs.getBoolean("is_logged_in", false);

            if (!isLoggedIn) {
                Toast.makeText(context, "Anda harus login untuk menambahkan ke favorit.", Toast.LENGTH_SHORT).show();
                return; // keluar kalau belum login
            }

            boolean isFav = movie.isFavorite();
            movie.setFavorite(!isFav); // toggle status favorite

            if (movie.isFavorite()) {
                holder.btnFavorite.setImageResource(R.drawable.ic_favorite_filled);
            } else {
                holder.btnFavorite.setImageResource(R.drawable.ic_favorite_border);
            }

            // Simpan perubahan ke database
            dbHelper.updateFavorite(movie.getId(), movie.isFavorite());
        });



    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public void setMovieList(List<Movie> newMovieList) {
        this.movieList = newMovieList;
        notifyDataSetChanged();
    }

    public void setFilteredList(List<Movie> filteredList) {
        this.movieList = filteredList;
        notifyDataSetChanged();
    }

    public static class MovieViewHolder extends RecyclerView.ViewHolder {
        ImageView imgMovie;
        TextView txtTitle;
        ImageView btnFavorite;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            imgMovie = itemView.findViewById(R.id.iv_movie_poster);
            txtTitle = itemView.findViewById(R.id.tv_movie_title);
            btnFavorite = itemView.findViewById(R.id.btn_favorite);
        }
    }
}
