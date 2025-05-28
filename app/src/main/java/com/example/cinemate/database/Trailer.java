package com.example.cinemate.database;

public class Trailer {
    private String title;
    private String imageUrl;
    private String trailerUrl;

    public Trailer(String title, String imageUrl, String trailerUrl) {
        this.title = title;
        this.imageUrl = imageUrl;
        this.trailerUrl = trailerUrl;
    }

    public String getTitle() { return title; }
    public String getImageUrl() { return imageUrl; }
    public String getTrailerUrl() { return trailerUrl; }
}
