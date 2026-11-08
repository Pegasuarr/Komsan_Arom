package com.melodyhub.dto;

import java.time.LocalDateTime;

/**
 * Data Transfer Object for Song information returned to client applications.
 */
public class SongResponseDTO {

    private Long id;
    private String title;
    private String artist;
    private Long artistId;
    private String duration;
    private Integer durationSeconds;
    private String audioUrl;
    private String cover;
    private Long playCount;
    private Boolean active;
    private String genre;
    private LocalDateTime createdAt;

    public SongResponseDTO() {
    }

    public SongResponseDTO(Long id, String title, String artist, Long artistId, String duration,
                           Integer durationSeconds, String audioUrl, String cover,
                           Long playCount, Boolean active, LocalDateTime createdAt) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.artistId = artistId;
        this.duration = duration;
        this.durationSeconds = durationSeconds;
        this.audioUrl = audioUrl;
        this.cover = cover;
        this.playCount = playCount;
        this.active = active;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Long getArtistId() {
        return artistId;
    }

    public void setArtistId(Long artistId) {
        this.artistId = artistId;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public Integer getDurationSeconds() {
        return durationSeconds;
    }

    public void setDurationSeconds(Integer durationSeconds) {
        this.durationSeconds = durationSeconds;
    }

    public String getAudioUrl() {
        return audioUrl;
    }

    public void setAudioUrl(String audioUrl) {
        this.audioUrl = audioUrl;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public Long getPlayCount() {
        return playCount;
    }

    public void setPlayCount(Long playCount) {
        this.playCount = playCount;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}

