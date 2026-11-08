package com.melodyhub.repository;

import com.melodyhub.entity.LikedSong;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikedSongRepository extends JpaRepository<LikedSong, Long> {

    Page<LikedSong> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);

    Optional<LikedSong> findByUserIdAndSongId(Long userId, Long songId);

    boolean existsByUserIdAndSongId(Long userId, Long songId);

    void deleteByUserIdAndSongId(Long userId, Long songId);

    long countBySongId(Long songId);
}

