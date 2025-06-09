package com.example.cinemate.models;

public class Movie {
    private int id;
    private String title;
    private String image;
    private String genre;
    private String description;
    private String status;
    private String poster;
    private String director;
    private String ageRating;
    private String duration;
    private double ratingScore;


    private boolean isFavorite;

    public Movie(int id, String title, String image, String genre, String description, String status, String duration, String ageRating, double ratingScore, boolean isFavorite) {
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    // ✅ Constructor yang benar
    public Movie(int id, String title, String image, String genre, String description, String status, String poster,
                 String director, String ageRating, String duration, double ratingScore) {
        this.id = id;
        this.title = title;
        this.image = image;
        this.genre = genre;
        this.description = description;
        this.status = status;
        this.poster = poster;
        this.director = director;
        this.ageRating = ageRating;
        this.duration = duration;
        this.ratingScore = ratingScore;
    }


    // ✅ Getter dan Setter
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getAgeRating() {
        return ageRating;
    }

    public void setAgeRating(String ageRating) {
        this.ageRating = ageRating;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public double getRatingScore() {
        return ratingScore;
    }

    public void setRatingScore(double ratingScore) {
        this.ratingScore = ratingScore;
    }

}
