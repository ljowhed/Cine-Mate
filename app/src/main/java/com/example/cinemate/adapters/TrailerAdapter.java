package com.example.cinemate.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cinemate.R;
import com.example.cinemate.database.Trailer;

import java.util.List;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerViewHolder> {

    private List<Trailer> trailerList;
    private Context context;

    public TrailerAdapter(Context context, List<Trailer> trailerList) {
        this.context = context;
        this.trailerList = trailerList;
    }

    @NonNull
    @Override
    public TrailerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_trailer, parent, false);
        return new TrailerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrailerViewHolder holder, int position) {
        Trailer trailer = trailerList.get(position);
        Glide.with(context).load(trailer.getImageUrl()).into(holder.imgTrailer);

        // Klik gambar untuk buka YouTube
        holder.imgTrailer.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(trailer.getTrailerUrl()));
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return trailerList.size();
    }

    public static class TrailerViewHolder extends RecyclerView.ViewHolder {
        ImageView imgTrailer;

        public TrailerViewHolder(@NonNull View itemView) {
            super(itemView);
            imgTrailer = itemView.findViewById(R.id.imgTrailer);
        }
    }
}
