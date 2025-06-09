package com.example.cinemate.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cinemate.R;
import com.example.cinemate.adapters.MovieAdapter;
import com.example.cinemate.database.DBHelper;
import com.example.cinemate.models.Movie;

import java.util.List;

public class FavoriteFragment extends Fragment {

    RecyclerView recyclerView;
    MovieAdapter movieAdapter;
    DBHelper dbHelper;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewFavorite);
        dbHelper = new DBHelper(getContext());

        List<Movie> favoriteMovies = dbHelper.getFavoriteMovies();

        movieAdapter = new MovieAdapter(getContext(), favoriteMovies, dbHelper);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerView.setAdapter(movieAdapter);

        return view;
    }

}
