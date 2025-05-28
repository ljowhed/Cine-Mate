package com.example.cinemate.Fragments;

import static java.util.Locale.filter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.cinemate.R;
import com.example.cinemate.activities.LoginActivity;
import com.example.cinemate.adapters.ComingSoonAdapter;
import com.example.cinemate.adapters.GenreAdapter;
import com.example.cinemate.adapters.MovieAdapter;
import com.example.cinemate.adapters.TrailerAdapter;
import com.example.cinemate.database.DBHelper;
import com.example.cinemate.database.Trailer;
import com.example.cinemate.models.Movie;
import com.example.cinemate.utils.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private RecyclerView rvMovies, rvGenre, rvComingSoon;
    private MovieAdapter movieAdapter;
    private ComingSoonAdapter comingSoonAdapter;
    private DBHelper dbHelper;

    private TextView greetingText, signInText, tvChooseMovie;
    private ImageView profileImage, logoutButton,imageprofile;
    private SessionManager sessionManager; // ✅ Gunakan variabel global ini
    private EditText searchBar;
    private List<Movie> allMovies; // List global untuk semua film


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        dbHelper = new DBHelper(getContext());
        sessionManager = new SessionManager(requireContext()); // ✅ Inisialisasi yang benar

        // Inisialisasi UI
        greetingText = view.findViewById(R.id.greetingText);
        profileImage = view.findViewById(R.id.profileImage);
        imageprofile = view.findViewById(R.id.imageProfile);
        signInText = view.findViewById(R.id.signInText);
        tvChooseMovie = view.findViewById(R.id.tvChooseMovie);
        logoutButton = view.findViewById(R.id.logoutButton);
        searchBar = view.findViewById(R.id.searchBar);

        if (sessionManager.isLoggedIn()) {
            String username = sessionManager.getUserName(); // ✅ tidak null lagi
            String imageUri = requireContext()
                    .getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
                    .getString("image", "");

            greetingText.setText("Halo, " + username);
            greetingText.setVisibility(View.VISIBLE);
            signInText.setVisibility(View.GONE);
            profileImage.setVisibility(View.VISIBLE);
            imageprofile.setVisibility(View.GONE);
            tvChooseMovie.setVisibility(View.GONE);

            if (!imageUri.isEmpty()) {
                try {
                    profileImage.setImageURI(Uri.parse(imageUri));
                } catch (Exception e) {
                    profileImage.setImageResource(R.drawable.profile_sample);
                }
            } else {
                profileImage.setImageResource(R.drawable.profile_sample);
            }

            logoutButton.setVisibility(View.VISIBLE);
            logoutButton.setOnClickListener(v -> {
                sessionManager.logout();
                requireContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
                        .edit().clear().apply();
                startActivity(new Intent(getActivity(), LoginActivity.class));
                requireActivity().finish();
            });

        } else {
            greetingText.setVisibility(View.GONE);
            profileImage.setVisibility(View.GONE);
            signInText.setVisibility(View.VISIBLE);
            tvChooseMovie.setVisibility(View.VISIBLE);
            logoutButton.setVisibility(View.GONE);
        }

        signInText.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), LoginActivity.class));
        });

        // RecyclerView Now Showing
        rvMovies = view.findViewById(R.id.rv_now_showing);
        rvMovies.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        allMovies = dbHelper.getAllMovies();
        movieAdapter = new MovieAdapter(getContext(), dbHelper.getAllMovies());
        rvMovies.setAdapter(movieAdapter);

        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterMovies(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // RecyclerView Coming Soon
        rvComingSoon = view.findViewById(R.id.rv_coming_soon);
        rvComingSoon.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        comingSoonAdapter = new ComingSoonAdapter(getContext(), dbHelper.getComingSoonMovies());
        rvComingSoon.setAdapter(comingSoonAdapter);


        // RecyclerView Genre
        rvGenre = view.findViewById(R.id.rv_genre);
        rvGenre.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        List<String> genreList = new ArrayList<>();
        genreList.add("All");
        genreList.add("Action");
        genreList.add("Comedy");
        genreList.add("Horror");
        genreList.add("Romance");
        genreList.add("Fantasy");
        genreList.add("Slice Of Life");
        genreList.add("Thriller");

        GenreAdapter genreAdapter = new GenreAdapter(getContext(), genreList, selectedGenre -> {
            if (selectedGenre.equals("All")) {
                movieAdapter.setMovieList(dbHelper.getAllMovies());
            } else {
                movieAdapter.setMovieList(dbHelper.getMoviesByGenre(selectedGenre));
            }
            movieAdapter.setMovieList(allMovies);
            filterMovies(searchBar.getText().toString());
        });

        rvGenre.setAdapter(genreAdapter);

        // RecyclerView Trailer
        RecyclerView rvTrailer = view.findViewById(R.id.rv_trailer);
        rvTrailer.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));

        List<Trailer> trailerList = new ArrayList<>();
        TrailerAdapter trailerAdapter = new TrailerAdapter(requireContext(), trailerList);
        rvTrailer.setAdapter(trailerAdapter);

        String jsonUrl = "https://pirja8.github.io/trailerdata/trailer.json"; // ganti dengan link JSON kamu

        RequestQueue queue = Volley.newRequestQueue(requireContext());
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, jsonUrl, null,
                response -> {
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject obj = response.getJSONObject(i);
                            String title = obj.getString("title");
                            String trailerUrl = obj.getString("trailerUrl");

                            // Ambil image dari trailerUrl YouTube
                            String videoId = trailerUrl.split("v=")[1];
                            String imageUrl = "https://img.youtube.com/vi/" + videoId + "/hqdefault.jpg";

                            trailerList.add(new Trailer(title, imageUrl, trailerUrl));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    trailerAdapter.notifyDataSetChanged();
                },
                error -> Toast.makeText(requireContext(), "Gagal load trailer", Toast.LENGTH_SHORT).show()
        );

        queue.add(request);

        return view;
    }
    private void filterMovies(String keyword) {
        List<Movie> filteredNowShowing = new ArrayList<>();
        List<Movie> filteredComingSoon = new ArrayList<>();

        for (Movie movie : dbHelper.getAllMovies()) {
            if (movie.getTitle().toLowerCase().contains(keyword.toLowerCase())) {
                filteredNowShowing.add(movie);
            }
        }

        for (Movie movie : dbHelper.getComingSoonMovies()) {
            if (movie.getTitle().toLowerCase().contains(keyword.toLowerCase())) {
                filteredComingSoon.add(movie);
            }
        }

        movieAdapter.setFilteredList(filteredNowShowing);
        comingSoonAdapter.setFilteredList(filteredComingSoon);
    }
}
