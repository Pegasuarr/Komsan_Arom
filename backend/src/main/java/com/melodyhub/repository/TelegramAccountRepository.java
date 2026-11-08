package com.melodyhub.repository;

import com.melodyhub.entity.TelegramAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TelegramAccountRepository extends JpaRepository<TelegramAccount, Long> {
    Optional<TelegramAccount> findByTelegramUserId(Long telegramUserId);
    Optional<TelegramAccount> findByUserId(Long userId);
    boolean existsByTelegramUserId(Long telegramUserId);
    boolean existsByUserId(Long userId);
}

