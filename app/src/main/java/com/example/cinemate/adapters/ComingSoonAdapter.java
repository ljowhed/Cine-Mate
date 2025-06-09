package com.example.cinemate.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cinemate.R;
import com.example.cinemate.database.DBHelper;
import com.example.cinemate.models.Movie;

import java.util.List;

public class ComingSoonAdapter extends RecyclerView.Adapter<ComingSoonAdapter.MovieViewHolder> {
    private Context context;
    private List<Movie> movieList;

    private DBHelper dbHelper;
    public ComingSoonAdapter(Context context, List<Movie> movieList) {
        this.context = context;
        this.movieList = movieList;
        this.dbHelper = new DBHelper(context); // inisialisasi dengan benar
    }


    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_coming_soon, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Movie movie = movieList.get(position);

        holder.titleTextView.setText(movie.getTitle());

        String imageName = movie.getPoster();
        int imageResId = context.getResources().getIdentifier(
                imageName, "drawable", context.getPackageName());

        Log.d("ComingSoonDebug", "imageName: " + imageName + ", resId: " + imageResId);

        if (imageResId != 0) {
            holder.posterImageView.setImageResource(imageResId);
        } else {
            holder.posterImageView.setImageResource(R.drawable.banner1); // fallback
        }

        if (movie.isFavorite()) {
            holder.btnFavorite.setImageResource(R.drawable.ic_favorite_filled);
        } else {
            holder.btnFavorite.setImageResource(R.drawable.ic_favorite_border);
        }

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
    public void setFilteredList(List<Movie> filteredList) {
        this.movieList = filteredList;
        notifyDataSetChanged();
    }
    public static class MovieViewHolder extends RecyclerView.ViewHolder {
        ImageView posterImageView;
        TextView titleTextView;

        ImageView btnFavorite;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            posterImageView = itemView.findViewById(R.id.ivPoster);
            titleTextView = itemView.findViewById(R.id.tvTitle);
            btnFavorite = itemView.findViewById(R.id.btn_favorite_coming);

        }
    }
}

