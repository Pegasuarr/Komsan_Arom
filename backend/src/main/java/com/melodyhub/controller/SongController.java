package com.melodyhub.controller;

import com.melodyhub.dto.SongResponseDTO;
import com.melodyhub.service.SongService;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourceRegion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * REST Controller for Song management, uploads, and HTTP Range audio streaming.
 */
@RestController
@RequestMapping("/songs")
public class SongController {

    private final SongService songService;

    public SongController(SongService songService) {
        this.songService = songService;
    }

    /**
     * Upload a new song track with audio file and optional cover image.
     */
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<SongResponseDTO> uploadSong(
            @RequestParam("title") String title,
            @RequestParam("artist") String artist,
            @RequestParam("audio") MultipartFile audio,
            @RequestParam(value = "cover", required = false) MultipartFile cover,
            @RequestParam(value = "duration", required = false) String duration,
            @RequestParam(value = "genre", required = false) String genre) {

        SongResponseDTO createdSong = songService.uploadSong(title, artist, audio, cover, duration, genre);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdSong);
    }

    /**
     * Retrieve all active songs with pagination.
     */
    @GetMapping
    public ResponseEntity<Page<SongResponseDTO>> getAllSongs(
            @PageableDefault(size = 20, sort = "createdAt") Pageable pageable) {
        return ResponseEntity.ok(songService.getAllSongs(pageable));
    }

    /**
     * Retrieve trending songs based on play count.
     */
    @GetMapping("/trending")
    public ResponseEntity<List<SongResponseDTO>> getTrendingSongs() {
        return ResponseEntity.ok(songService.getTrendingSongs());
    }

    /**
     * Retrieve quick picks / recent songs.
     */
    @GetMapping("/quick-picks")
    public ResponseEntity<List<SongResponseDTO>> getQuickPicks() {
        return ResponseEntity.ok(songService.getRecentSongs());
    }

    /**
     * Search songs by title or artist.
     */
    @GetMapping("/search")
    public ResponseEntity<Page<SongResponseDTO>> searchSongs(
            @RequestParam("q") String query,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(songService.searchSongs(query, pageable));
    }

    /**
     * Get a single song by its ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SongResponseDTO> getSongById(@PathVariable Long id) {
        return ResponseEntity.ok(songService.getSongById(id));
    }

    /**
     * Stream audio track with HTTP Range request support (HTTP 206 Partial Content).
     * This enables seamless audio seeking, scrub bar skipping, and bandwidth efficiency.
     */
    @GetMapping("/{id}/stream")
    public ResponseEntity<ResourceRegion> streamAudio(
            @PathVariable Long id,
            @RequestHeader HttpHeaders headers) throws IOException {

        Resource audioResource = songService.getAudioResource(id);
        long contentLength = audioResource.contentLength();

        HttpRange range = headers.getRange().isEmpty() ? null : headers.getRange().get(0);
        ResourceRegion region;

        if (range != null) {
            long start = range.getRangeStart(contentLength);
            long end = range.getRangeEnd(contentLength);
            long rangeLength = Math.min(1024 * 1024L, end - start + 1); // 1MB chunks
            region = new ResourceRegion(audioResource, start, rangeLength);
            return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
                    .contentType(MediaTypeFactory.getMediaType(audioResource).orElse(MediaType.parseMediaType("audio/mpeg")))
                    .header(HttpHeaders.ACCEPT_RANGES, "bytes")
                    .body(region);
        } else {
            long rangeLength = Math.min(1024 * 1024L, contentLength);
            region = new ResourceRegion(audioResource, 0, rangeLength);
            return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
                    .contentType(MediaTypeFactory.getMediaType(audioResource).orElse(MediaType.parseMediaType("audio/mpeg")))
                    .header(HttpHeaders.ACCEPT_RANGES, "bytes")
                    .body(region);
        }
    }

    /**
     * Serve album cover image.
     */
    @GetMapping("/{id}/cover")
    public ResponseEntity<Resource> getSongCover(@PathVariable Long id) {
        Resource coverResource = songService.getCoverResource(id);
        MediaType mediaType = MediaTypeFactory.getMediaType(coverResource).orElse(MediaType.IMAGE_JPEG);

        return ResponseEntity.ok()
                .contentType(mediaType)
                .body(coverResource);
    }

    /**
     * Record a stream / increment play count.
     */
    @PostMapping("/{id}/play")
    public ResponseEntity<Void> recordPlay(@PathVariable Long id) {
        songService.recordPlay(id);
        return ResponseEntity.ok().build();
    }

    /**
     * Update an existing song title, artist, or playlist category.
     */
    @PutMapping("/{id}")
    public ResponseEntity<SongResponseDTO> updateSong(
            @PathVariable Long id,
            @RequestBody com.melodyhub.dto.SongUpdateRequestDTO request) {
        return ResponseEntity.ok(songService.updateSong(id, request));
    }

    /**
     * Delete / deactivate a song.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSong(@PathVariable Long id) {
        songService.deleteSong(id);
        return ResponseEntity.noContent().build();
    }
}

