package com.melodyhub.repository;

import com.melodyhub.entity.Playlist;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlaylistRepository extends JpaRepository<Playlist, Long> {
    List<Playlist> findByOwnerIdOrderByCreatedAtDesc(Long ownerId);
    Page<Playlist> findByOwnerId(Long ownerId, Pageable pageable);
}
