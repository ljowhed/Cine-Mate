package com.example.cinemate.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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
    private SessionManager sessionManager;
    private EditText searchBar;
    private TextView greetingText, signInText, tvChooseMovie;
    private ImageView profileImage, logoutButton, imageprofile;
    private List<Movie> allMovies;
    private WebView webView;
    private String selectedGenre = "All";
    private String currentKeyword = "";

    private void applyFilters() {
        List<Movie> baseList;
        if (selectedGenre.equals("All")) {
            baseList = dbHelper.getAllMovies();
        } else {
            baseList = dbHelper.getMoviesByGenre(selectedGenre);
        }

        List<Movie> filteredNowShowing = new ArrayList<>();
        for (Movie movie : baseList) {
            if (movie.getTitle().toLowerCase().contains(currentKeyword.toLowerCase())) {
                filteredNowShowing.add(movie);
            }
        }

        movieAdapter.setFilteredList(filteredNowShowing);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        dbHelper = new DBHelper(getContext());
        sessionManager = new SessionManager(requireContext());

        // UI Elements
        greetingText = view.findViewById(R.id.greetingText);
        profileImage = view.findViewById(R.id.profileImage);
        imageprofile = view.findViewById(R.id.imageProfile);
        signInText = view.findViewById(R.id.signInText);
        tvChooseMovie = view.findViewById(R.id.tvChooseMovie);
        logoutButton = view.findViewById(R.id.logoutButton);
        searchBar = view.findViewById(R.id.searchBar);
        webView = view.findViewById(R.id.webViewTrailer);

        // WebView setup
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setMediaPlaybackRequiresUserGesture(false); // Autoplay allowed
        webView.setWebChromeClient(new WebChromeClient());

        // User session check
        if (sessionManager.isLoggedIn()) {
            String username = sessionManager.getUserName();
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
                    profileImage.setImageResource(R.drawable.default_profil);
                }
            } else {
                profileImage.setImageResource(R.drawable.default_profil);
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

        // Now Showing
        rvMovies = view.findViewById(R.id.rv_now_showing);
        rvMovies.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        allMovies = dbHelper.getAllMovies();
        movieAdapter = new MovieAdapter(getContext(), allMovies);
        rvMovies.setAdapter(movieAdapter);

        // Search filter
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                currentKeyword = s.toString();
                applyFilters();
            }
            @Override public void afterTextChanged(Editable s) {}
        });


        // Coming Soon
        rvComingSoon = view.findViewById(R.id.rv_coming_soon);
        rvComingSoon.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        comingSoonAdapter = new ComingSoonAdapter(getContext(), dbHelper.getComingSoonMovies());
        rvComingSoon.setAdapter(comingSoonAdapter);

        // Genre
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



        GenreAdapter genreAdapter = new GenreAdapter(getContext(), genreList, genre -> {
            selectedGenre = genre;
            applyFilters();
        });

        rvGenre.setAdapter(genreAdapter);




        // Trailer RecyclerView
        RecyclerView rvTrailer = view.findViewById(R.id.rv_trailer);
        rvTrailer.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        List<Trailer> trailerList = new ArrayList<>();
        TrailerAdapter trailerAdapter = new TrailerAdapter(requireContext(), trailerList);
        rvTrailer.setAdapter(trailerAdapter);

        // Load trailer JSON & play first
        String jsonUrl = "https://raw.githubusercontent.com/ljowhed/erika/refs/heads/main/trailer.json";

        RequestQueue queue = Volley.newRequestQueue(requireContext());
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, jsonUrl, null,
                response -> {
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject obj = response.getJSONObject(i);
                            String title = obj.getString("title");
                            String trailerUrl = obj.getString("trailerUrl");

                            String videoId = getYoutubeVideoId(trailerUrl);
                            if (videoId == null) {
                                Log.w("TRAILER_PARSE", "Gagal ambil videoId dari URL: " + trailerUrl);
                                continue;
                            }

                            String imageUrl = "https://img.youtube.com/vi/" + videoId + "/hqdefault.jpg";
                            trailerList.add(new Trailer(title, imageUrl, trailerUrl));

                            // Autoplay trailer pertama di WebView
                            if (i == 0) {
                                String autoplayUrl = "https://www.youtube.com/embed/" + videoId + "?autoplay=1&mute=1";
                                webView.loadUrl(autoplayUrl);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    trailerAdapter.notifyDataSetChanged();
                },
                error -> {
                    String errMsg = (error.getMessage() != null) ? error.getMessage() : "Unknown error";
                    Log.e("TRAILER_ERROR", "Error: " + errMsg, error);
                    Toast.makeText(requireContext(), "Gagal load trailer", Toast.LENGTH_SHORT).show();
                }
        );
        queue.add(request);

        return view;
    }

    private String getYoutubeVideoId(String url) {
        try {
            Uri uri = Uri.parse(url);
            String vParam = uri.getQueryParameter("v");
            if (vParam != null && !vParam.isEmpty()) {
                return vParam;
            }
            List<String> segments = uri.getPathSegments();
            if (!segments.isEmpty()) {
                return segments.get(segments.size() - 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
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
