package com.melodyhub.repository;

import com.melodyhub.entity.ListeningHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ListeningHistoryRepository extends JpaRepository<ListeningHistory, Long> {
    Page<ListeningHistory> findByUserIdOrderByPlayedAtDesc(Long userId, Pageable pageable);
}

