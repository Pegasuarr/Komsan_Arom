package com.melodyhub.service;

import com.melodyhub.dto.SongResponseDTO;
import com.melodyhub.entity.Artist;
import com.melodyhub.entity.Song;
import com.melodyhub.exception.BadRequestException;
import com.melodyhub.exception.ResourceNotFoundException;
import com.melodyhub.repository.ArtistRepository;
import com.melodyhub.repository.SongRepository;
import com.melodyhub.storage.StorageService;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service handling music catalog management, track uploads, and streaming logic.
 */
@Service
public class SongService {

    private final SongRepository songRepository;
    private final ArtistRepository artistRepository;
    private final StorageService storageService;

    public SongService(SongRepository songRepository,
                       ArtistRepository artistRepository,
                       StorageService storageService) {
        this.songRepository = songRepository;
        this.artistRepository = artistRepository;
        this.storageService = storageService;
    }

    /**
     * Uploads and persists a new audio track into the MelodyHub catalog.
     */
    @Transactional
    public SongResponseDTO uploadSong(String title,
                                      String artistName,
                                      MultipartFile audioFile,
                                      MultipartFile coverFile,
                                      String duration) {
        return uploadSong(title, artistName, audioFile, coverFile, duration, "Khmer");
    }

    @Transactional
    public SongResponseDTO uploadSong(String title,
                                      String artistName,
                                      MultipartFile audioFile,
                                      MultipartFile coverFile,
                                      String duration,
                                      String genre) {
        if (title == null || title.trim().isEmpty()) {
            throw new BadRequestException("Song title is required.");
        }
        if (artistName == null || artistName.trim().isEmpty()) {
            throw new BadRequestException("Artist name is required.");
        }
        if (audioFile == null || audioFile.isEmpty()) {
            throw new BadRequestException("Audio file is required.");
        }

        // 1. Find existing artist or create a new one
        String trimmedArtistName = artistName.trim();
        Artist artist = artistRepository.findByName(trimmedArtistName)
                .orElseGet(() -> {
                    Artist newArtist = new Artist(trimmedArtistName, "Independent artist on MelodyHub", null);
                    return artistRepository.save(newArtist);
                });

        // 2. Store audio file physically on disk
        String audioFilename = storageService.storeAudioFile(audioFile);

        // 3. Store cover image if provided
        String coverFilename = null;
        if (coverFile != null && !coverFile.isEmpty()) {
            coverFilename = storageService.storeCoverImage(coverFile);
        }

        // 4. Create and persist Song entity
        String formattedDuration = (duration != null && !duration.trim().isEmpty()) ? duration.trim() : "3:30";
        Song song = new Song(
                title.trim(),
                artist,
                formattedDuration,
                null,
                audioFilename,
                coverFilename,
                (genre != null && !genre.trim().isEmpty()) ? genre.trim() : "Khmer"
        );

        Song savedSong = songRepository.save(song);
        return mapToDTO(savedSong);
    }

    @Transactional(readOnly = true)
    public Page<SongResponseDTO> getAllSongs(Pageable pageable) {
        return songRepository.findByActiveTrue(pageable).map(this::mapToDTO);
    }

    @Transactional(readOnly = true)
    public List<SongResponseDTO> getTrendingSongs() {
        return songRepository.findTop10ByActiveTrueOrderByPlayCountDesc()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<SongResponseDTO> getRecentSongs() {
        return songRepository.findTop10ByActiveTrueOrderByCreatedAtDesc()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public SongResponseDTO getSongById(Long id) {
        Song song = songRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Song not found with id: " + id));
        return mapToDTO(song);
    }

    @Transactional(readOnly = true)
    public Page<SongResponseDTO> searchSongs(String query, Pageable pageable) {
        return songRepository.searchActiveSongs(query, pageable).map(this::mapToDTO);
    }

    @Transactional(readOnly = true)
    public Resource getAudioResource(Long id) {
        Song song = songRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Song not found with id: " + id));
        return storageService.loadAudioAsResource(song.getAudioFilePath());
    }

    @Transactional(readOnly = true)
    public Resource getCoverResource(Long id) {
        Song song = songRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Song not found with id: " + id));
        if (song.getCoverImagePath() == null) {
            throw new ResourceNotFoundException("Cover image not available for song id: " + id);
        }
        return storageService.loadCoverAsResource(song.getCoverImagePath());
    }

    @Transactional
    public void recordPlay(Long id) {
        Song song = songRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Song not found with id: " + id));
        song.setPlayCount(song.getPlayCount() + 1);
        songRepository.save(song);
    }

    /**
     * Updates an existing song title, artist, or playlist category.
     */
    @Transactional
    public SongResponseDTO updateSong(Long id, com.melodyhub.dto.SongUpdateRequestDTO request) {
        Song song = songRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Song not found with id: " + id));

        if (request.getTitle() != null && !request.getTitle().trim().isEmpty()) {
            song.setTitle(request.getTitle().trim());
        }

        if (request.getArtist() != null && !request.getArtist().trim().isEmpty()) {
            String trimmedArtistName = request.getArtist().trim();
            Artist artist = artistRepository.findByName(trimmedArtistName)
                    .orElseGet(() -> {
                        Artist newArtist = new Artist(trimmedArtistName, "Independent artist on MelodyHub", null);
                        return artistRepository.save(newArtist);
                    });
            song.setArtist(artist);
        }

        if (request.getGenre() != null && !request.getGenre().trim().isEmpty()) {
            song.setGenre(request.getGenre().trim());
        }

        Song saved = songRepository.save(song);
        return mapToDTO(saved);
    }

    /**
     * Deactivates a song from the catalog.
     */
    @Transactional
    public void deleteSong(Long id) {
        Song song = songRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Song not found with id: " + id));
        song.setActive(false);
        songRepository.save(song);
    }

    /**
     * Maps Song entity to SongResponseDTO with dynamic streaming URL.
     */
    public SongResponseDTO mapToDTO(Song song) {
        String streamUrl = "/api/songs/" + song.getId() + "/stream";
        String coverUrl = song.getCoverImagePath() != null
                ? "/api/songs/" + song.getId() + "/cover"
                : "https://images.unsplash.com/photo-1514525253161-7a46d19cd819?w=300&auto=format&fit=crop&q=80";

        SongResponseDTO dto = new SongResponseDTO(
                song.getId(),
                song.getTitle(),
                song.getArtist().getName(),
                song.getArtist().getId(),
                song.getDuration(),
                song.getDurationSeconds(),
                streamUrl,
                coverUrl,
                song.getPlayCount(),
                song.getActive(),
                song.getCreatedAt()
        );
        dto.setGenre(song.getGenre());
        return dto;
    }
}

