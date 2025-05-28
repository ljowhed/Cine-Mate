package com.example.cinemate.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.example.cinemate.R;
import java.util.List;

public class GenreAdapter extends RecyclerView.Adapter<GenreAdapter.ViewHolder> {

    private final List<String> genres;
    private final Context context;
    private int selectedPosition = 0;
    private OnGenreClickListener listener;

    public interface OnGenreClickListener {
        void onGenreClick(String genre);
    }

    public GenreAdapter(Context context, List<String> genres, OnGenreClickListener listener) {
        this.context = context;
        this.genres = genres;
        this.listener = listener;
    }

    @Override
    public GenreAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_genre, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(GenreAdapter.ViewHolder holder, int position) {
        String genre = genres.get(position);
        holder.tvGenre.setText(genre);

        if (selectedPosition == position) {
            holder.tvGenre.setBackground(ContextCompat.getDrawable(context, R.drawable.genre_chip_selected_bg));
        } else {
            holder.tvGenre.setBackground(ContextCompat.getDrawable(context, R.drawable.genre_chip_bg));
        }

        holder.tvGenre.setOnClickListener(v -> {
            int previousSelected = selectedPosition;
            selectedPosition = holder.getAdapterPosition();
            notifyItemChanged(previousSelected);
            notifyItemChanged(selectedPosition);
            listener.onGenreClick(genre);
        });
    }

    @Override
    public int getItemCount() {
        return genres.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvGenre;

        public ViewHolder(View itemView) {
            super(itemView);
            tvGenre = itemView.findViewById(R.id.tvGenre);
        }
    }
}
