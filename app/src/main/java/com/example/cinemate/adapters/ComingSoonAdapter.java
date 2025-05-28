package com.example.cinemate.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cinemate.R;
import com.example.cinemate.models.Movie;

import java.util.List;

public class ComingSoonAdapter extends RecyclerView.Adapter<ComingSoonAdapter.MovieViewHolder> {
    private Context context;
    private List<Movie> movieList;

    public ComingSoonAdapter(Context context, List<Movie> movieList) {
        this.context = context;
        this.movieList = movieList;
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

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            posterImageView = itemView.findViewById(R.id.ivPoster);
            titleTextView = itemView.findViewById(R.id.tvTitle);
        }
    }
}

