package com.melodyhub.repository;

import com.melodyhub.entity.Song;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SongRepository extends JpaRepository<Song, Long> {

    Page<Song> findByActiveTrue(Pageable pageable);

    @Query("SELECT s FROM Song s WHERE s.active = true AND " +
           "(LOWER(s.title) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(s.artist.name) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(COALESCE(s.genre, '')) LIKE LOWER(CONCAT('%', :query, '%')))")
    Page<Song> searchActiveSongs(@Param("query") String query, Pageable pageable);

    @Query("SELECT s FROM Song s WHERE s.active = true AND " +
           "(LOWER(COALESCE(s.genre, '')) = LOWER(:genre) OR " +
           "LOWER(COALESCE(s.genre, '')) LIKE LOWER(CONCAT('%', :genre, '%')) OR " +
           "(:genre = 'Korea' AND (LOWER(COALESCE(s.genre, '')) LIKE '%korea%' OR LOWER(COALESCE(s.genre, '')) LIKE '%k-pop%' OR LOWER(COALESCE(s.genre, '')) LIKE '%kpop%')) OR " +
           "LOWER(s.title) LIKE LOWER(CONCAT('%', :genre, '%')) OR " +
           "LOWER(s.artist.name) LIKE LOWER(CONCAT('%', :genre, '%')))")
    List<Song> findByPlaylistCategory(@Param("genre") String genre);

    Page<Song> findByArtistIdAndActiveTrue(Long artistId, Pageable pageable);

    List<Song> findTop10ByActiveTrueOrderByPlayCountDesc();

    List<Song> findTop10ByActiveTrueOrderByCreatedAtDesc();
}
