package com.melodyhub.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(
    name = "telegram_accounts",
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_telegram_user_id", columnNames = {"telegram_user_id"})
    },
    indexes = {
        @Index(name = "idx_telegram_accounts_tg_id", columnList = "telegram_user_id"),
        @Index(name = "idx_telegram_accounts_user_id", columnList = "user_id")
    }
)
public class TelegramAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(name = "telegram_user_id", nullable = false, unique = true)
    private Long telegramUserId;

    @Column(name = "telegram_username", length = 100)
    private String telegramUsername;

    @Column(name = "linked_at", nullable = false, updatable = false)
    private LocalDateTime linkedAt;

    public TelegramAccount() {
    }

    public TelegramAccount(User user, Long telegramUserId, String telegramUsername) {
        this.user = user;
        this.telegramUserId = telegramUserId;
        this.telegramUsername = telegramUsername;
    }

    @PrePersist
    protected void onCreate() {
        this.linkedAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Long getTelegramUserId() {
        return telegramUserId;
    }

    public void setTelegramUserId(Long telegramUserId) {
        this.telegramUserId = telegramUserId;
    }

    public String getTelegramUsername() {
        return telegramUsername;
    }

    public void setTelegramUsername(String telegramUsername) {
        this.telegramUsername = telegramUsername;
    }

    public LocalDateTime getLinkedAt() {
        return linkedAt;
    }
}
