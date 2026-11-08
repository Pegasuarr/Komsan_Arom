package com.melodyhub.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(
    name = "playlist_songs",
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_playlist_song", columnNames = {"playlist_id", "song_id"})
    },
    indexes = {
        @Index(name = "idx_playlist_songs_playlist", columnList = "playlist_id"),
        @Index(name = "idx_playlist_songs_song", columnList = "song_id"),
        @Index(name = "idx_playlist_songs_position", columnList = "playlist_id, position")
    }
)
public class PlaylistSong {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "playlist_id", nullable = false)
    private Playlist playlist;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "song_id", nullable = false)
    private Song song;

    @Column(nullable = false)
    private Integer position = 0;

    @Column(name = "added_at", nullable = false, updatable = false)
    private LocalDateTime addedAt;

    public PlaylistSong() {
    }

    public PlaylistSong(Playlist playlist, Song song, Integer position) {
        this.playlist = playlist;
        this.song = song;
        this.position = position != null ? position : 0;
    }

    @PrePersist
    protected void onCreate() {
        this.addedAt = LocalDateTime.now();
        if (this.position == null) {
            this.position = 0;
        }
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Playlist getPlaylist() {
        return playlist;
    }

    public void setPlaylist(Playlist playlist) {
        this.playlist = playlist;
    }

    public Song getSong() {
        return song;
    }

    public void setSong(Song song) {
        this.song = song;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public LocalDateTime getAddedAt() {
        return addedAt;
    }

    public void setAddedAt(LocalDateTime addedAt) {
        this.addedAt = addedAt;
    }
}

