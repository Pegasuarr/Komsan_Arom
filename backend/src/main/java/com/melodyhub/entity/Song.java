package com.melodyhub.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(
    name = "songs",
    indexes = {
        @Index(name = "idx_songs_title", columnList = "title"),
        @Index(name = "idx_songs_artist", columnList = "artist_id"),
        @Index(name = "idx_songs_play_count", columnList = "play_count DESC")
    }
)
public class Song {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String title;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "artist_id", nullable = false)
    private Artist artist;

    @Column(length = 20)
    private String duration; // Formatted e.g. "3:24"

    @Column(name = "duration_seconds")
    private Integer durationSeconds;

    @Column(name = "audio_file_path", nullable = false)
    private String audioFilePath;

    @Column(name = "cover_image_path")
    private String coverImagePath;

    @Column(name = "genre", length = 50)
    private String genre;

    @Column(name = "telegram_file_id")
    private String telegramFileId; // Cached Telegram audio file_id for instant bot sending

    @Column(name = "play_count", nullable = false)
    private Long playCount = 0L;

    @Column(nullable = false)
    private Boolean active = true;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public Song() {
    }

    public Song(String title, Artist artist, String duration, Integer durationSeconds, String audioFilePath, String coverImagePath) {
        this(title, artist, duration, durationSeconds, audioFilePath, coverImagePath, null);
    }

    public Song(String title, Artist artist, String duration, Integer durationSeconds, String audioFilePath, String coverImagePath, String genre) {
        this.title = title;
        this.artist = artist;
        this.duration = duration;
        this.durationSeconds = durationSeconds;
        this.audioFilePath = audioFilePath;
        this.coverImagePath = coverImagePath;
        this.genre = genre;
        this.playCount = 0L;
        this.active = true;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        if (this.playCount == null) {
            this.playCount = 0L;
        }
        if (this.active == null) {
            this.active = true;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
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

    public Artist getArtist() {
        return artist;
    }

    public void setArtist(Artist artist) {
        this.artist = artist;
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

    public String getAudioFilePath() {
        return audioFilePath;
    }

    public void setAudioFilePath(String audioFilePath) {
        this.audioFilePath = audioFilePath;
    }

    public String getCoverImagePath() {
        return coverImagePath;
    }

    public void setCoverImagePath(String coverImagePath) {
        this.coverImagePath = coverImagePath;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getTelegramFileId() {
        return telegramFileId;
    }

    public void setTelegramFileId(String telegramFileId) {
        this.telegramFileId = telegramFileId;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}

