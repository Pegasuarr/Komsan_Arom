package com.melodyhub.dto;

/**
 * DTO for updating existing Song attributes (title, artist, playlist category).
 */
public class SongUpdateRequestDTO {

    private String title;
    private String artist;
    private String genre;

    public SongUpdateRequestDTO() {
    }

    public SongUpdateRequestDTO(String title, String artist, String genre) {
        this.title = title;
        this.artist = artist;
        this.genre = genre;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }
}

