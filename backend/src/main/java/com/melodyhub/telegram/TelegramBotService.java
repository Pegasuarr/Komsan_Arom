package com.melodyhub.telegram;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.melodyhub.entity.Song;
import com.melodyhub.repository.SongRepository;
import com.melodyhub.storage.StorageService;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.*;

/**
 * MelodyHub Telegram Music Bot Service.
 * Provides interactive music browsing: All Songs, Popular Songs, and Playlists (Khmer, English, Chinese, Korea, Japan, France, Phonk).
 */
@Service
@Transactional
public class TelegramBotService {

    private static final Logger logger = LoggerFactory.getLogger(TelegramBotService.class);

    @Value("${telegram.bot.token}")
    private String botToken;

    @Value("${telegram.bot.username:komsan_arom_music_bot}")
    private String botUsername;

    @Value("${telegram.bot.enabled:true}")
    private boolean enabled;

    private final SongRepository songRepository;
    private final StorageService storageService;
    private final ObjectMapper objectMapper;
    private final HttpClient httpClient;

    private volatile boolean running = false;
    private Thread pollingThread;
    private long lastUpdateId = 0;

    public TelegramBotService(SongRepository songRepository, StorageService storageService) {
        this.songRepository = songRepository;
        this.storageService = storageService;
        this.objectMapper = new ObjectMapper();
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
    }

    @PostConstruct
    public void start() {
        if (!enabled || botToken == null || botToken.trim().isEmpty()) {
            logger.info("Telegram Bot is disabled or bot token is not configured.");
            return;
        }

        running = true;
        pollingThread = new Thread(this::pollUpdates, "melodyhub-telegram-bot");
        pollingThread.setDaemon(true);
        pollingThread.start();
        logger.info("Telegram Bot [@{}] started polling.", botUsername);
    }

    @PreDestroy
    public void stop() {
        running = false;
        if (pollingThread != null) {
            pollingThread.interrupt();
        }
        logger.info("Telegram Bot stopped.");
    }

    private void pollUpdates() {
        while (running) {
            try {
                String url = String.format("https://api.telegram.org/bot%s/getUpdates?offset=%d&timeout=15",
                        botToken, lastUpdateId + 1);

                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(url))
                        .GET()
                        .timeout(Duration.ofSeconds(20))
                        .build();

                HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

                if (response.statusCode() == 200) {
                    JsonNode root = objectMapper.readTree(response.body());
                    if (root.path("ok").asBoolean()) {
                        JsonNode results = root.path("result");
                        for (JsonNode update : results) {
                            lastUpdateId = update.path("update_id").asLong();
                            processUpdate(update);
                        }
                    }
                } else {
                    logger.warn("Telegram API returned HTTP {}", response.statusCode());
                    Thread.sleep(3000);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            } catch (Exception e) {
                logger.debug("Telegram polling warning: {}", e.getMessage());
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }
    }

    private void processUpdate(JsonNode update) {
        try {
            // 1. Text Message
            if (update.has("message")) {
                JsonNode message = update.get("message");
                long chatId = message.path("chat").path("id").asLong();
                String text = message.path("text").asText("");
                String firstName = message.path("from").path("first_name").asText("Music Lover");

                handleTextMessage(chatId, firstName, text);
            }
            // 2. Inline Keyboard Callback
            else if (update.has("callback_query")) {
                JsonNode callback = update.get("callback_query");
                long chatId = callback.path("message").path("chat").path("id").asLong();
                String data = callback.path("data").asText("");

                handleCallbackQuery(chatId, data);
            }
        } catch (Exception e) {
            logger.error("Error processing Telegram update", e);
        }
    }

    private void handleTextMessage(long chatId, String firstName, String text) {
        String trimmed = text.trim();

        if (trimmed.startsWith("/start")) {
            String[] parts = trimmed.split("\\s+");
            if (parts.length > 1 && parts[1].startsWith("play_")) {
                try {
                    Long songId = Long.parseLong(parts[1].substring(5));
                    sendSongDetails(chatId, songId);
                    return;
                } catch (NumberFormatException ignored) {}
            }
            sendWelcomeMessage(chatId, firstName);
        } else if (trimmed.startsWith("/songs") || trimmed.equalsIgnoreCase("songs")) {
            sendAllSongs(chatId);
        } else if (trimmed.startsWith("/popular") || trimmed.startsWith("/trending") || trimmed.equalsIgnoreCase("popular")) {
            sendPopularSongs(chatId);
        } else if (trimmed.startsWith("/playlists") || trimmed.equalsIgnoreCase("playlists")) {
            sendPlaylistsMenu(chatId);
        } else if (trimmed.startsWith("/help")) {
            sendHelpMessage(chatId);
        } else if (trimmed.startsWith("/search")) {
            String query = trimmed.replaceFirst("/search", "").trim();
            if (query.isEmpty()) {
                sendMessage(chatId, "Please specify what you want to search for.\nExample: <code>/search your_song</code>", null);
            } else {
                searchAndSendSongs(chatId, query);
            }
        } else if (trimmed.startsWith("/play_")) {
            try {
                Long songId = Long.parseLong(trimmed.substring(6));
                sendSongDetails(chatId, songId);
            } catch (NumberFormatException e) {
                sendMessage(chatId, "Invalid song ID.", null);
            }
        } else {
            // Direct text search
            searchAndSendSongs(chatId, trimmed);
        }
    }

    private void handleCallbackQuery(long chatId, String data) {
        if ("menu".equalsIgnoreCase(data)) {
            sendWelcomeMessage(chatId, "Music Lover");
        } else if ("songs".equalsIgnoreCase(data)) {
            sendAllSongs(chatId);
        } else if ("popular".equalsIgnoreCase(data) || "trending".equalsIgnoreCase(data)) {
            sendPopularSongs(chatId);
        } else if ("playlists".equalsIgnoreCase(data)) {
            sendPlaylistsMenu(chatId);
        } else if (data.startsWith("pl_")) {
            String playlistName = data.substring(3);
            sendPlaylistSongs(chatId, playlistName);
        } else if ("help".equalsIgnoreCase(data) || "help_search".equalsIgnoreCase(data)) {
            sendHelpMessage(chatId);
        } else if (data.startsWith("play_")) {
            try {
                Long songId = Long.parseLong(data.substring(5));
                sendSongDetails(chatId, songId);
            } catch (NumberFormatException ignored) {}
        }
    }

    private void sendWelcomeMessage(long chatId, String firstName) {
        String text = String.format(
                "Welcome to <b>MelodyHub</b>, <b>%s</b>!\n\n" +
                "Select an option below to stream your music:",
                escapeHtml(firstName)
        );

        List<List<Map<String, String>>> inlineKeyboard = new ArrayList<>();

        // Option 1: All Trending
        List<Map<String, String>> row1 = new ArrayList<>();
        Map<String, String> trendingBtn = new HashMap<>();
        trendingBtn.put("text", "🔥 All Trending");
        trendingBtn.put("callback_data", "popular");
        row1.add(trendingBtn);
        inlineKeyboard.add(row1);

        // Option 2: All Playlists
        List<Map<String, String>> row2 = new ArrayList<>();
        Map<String, String> playlistBtn = new HashMap<>();
        playlistBtn.put("text", "📁 All Playlists");
        playlistBtn.put("callback_data", "playlists");
        row2.add(playlistBtn);
        inlineKeyboard.add(row2);

        Map<String, Object> replyMarkup = Collections.singletonMap("inline_keyboard", inlineKeyboard);
        sendMessage(chatId, text, replyMarkup);
    }

    private void sendAllSongs(long chatId) {
        List<Song> songs = songRepository.findAll();

        if (songs.isEmpty()) {
            String emptyMsg = "<b>Your Catalog is Currently Empty.</b>\n\n" +
                    "You can upload your songs using the MelodyHub Web App, and they will immediately appear here!";

            List<List<Map<String, String>>> inlineKeyboard = new ArrayList<>();
            List<Map<String, String>> row = new ArrayList<>();
            Map<String, String> menuBtn = new HashMap<>();
            menuBtn.put("text", "⬅️ Main Menu");
            menuBtn.put("callback_data", "menu");
            row.add(menuBtn);
            inlineKeyboard.add(row);

            sendMessage(chatId, emptyMsg, Collections.singletonMap("inline_keyboard", inlineKeyboard));
            return;
        }

        StringBuilder sb = new StringBuilder("🎵 <b>All Catalog Songs:</b>\n\n");
        List<List<Map<String, String>>> inlineKeyboard = new ArrayList<>();

        for (int i = 0; i < songs.size(); i++) {
            Song s = songs.get(i);
            sb.append(String.format("%d. <b>%s</b> — %s (%s)\n",
                    i + 1,
                    escapeHtml(s.getTitle()),
                    escapeHtml(s.getArtist().getName()),
                    s.getDuration() != null ? s.getDuration() : "3:30"));

            List<Map<String, String>> row = new ArrayList<>();
            Map<String, String> playBtn = new HashMap<>();
            playBtn.put("text", "▶️ Play: " + s.getTitle());
            playBtn.put("callback_data", "play_" + s.getId());
            row.add(playBtn);
            inlineKeyboard.add(row);
        }

        // Back button
        List<Map<String, String>> backRow = new ArrayList<>();
        Map<String, String> backBtn = new HashMap<>();
        backBtn.put("text", "⬅️ Main Menu");
        backBtn.put("callback_data", "menu");
        backRow.add(backBtn);
        inlineKeyboard.add(backRow);

        sendMessage(chatId, sb.toString(), Collections.singletonMap("inline_keyboard", inlineKeyboard));
    }

    private void sendPopularSongs(long chatId) {
        List<Song> songs = songRepository.findTop10ByActiveTrueOrderByPlayCountDesc();

        if (songs.isEmpty()) {
            String empty = "🔥 <b>All Trending Songs</b>\n\n" +
                    "No trending songs yet.\n" +
                    "Upload tracks in MelodyHub to start streaming!";

            List<List<Map<String, String>>> inlineKeyboard = new ArrayList<>();
            List<Map<String, String>> row = new ArrayList<>();
            Map<String, String> menuBtn = new HashMap<>();
            menuBtn.put("text", "⬅️ Main Menu");
            menuBtn.put("callback_data", "menu");
            row.add(menuBtn);
            inlineKeyboard.add(row);

            sendMessage(chatId, empty, Collections.singletonMap("inline_keyboard", inlineKeyboard));
            return;
        }

        StringBuilder sb = new StringBuilder("🔥 <b>All Trending Songs:</b>\n\n");
        List<List<Map<String, String>>> inlineKeyboard = new ArrayList<>();

        for (int i = 0; i < songs.size(); i++) {
            Song s = songs.get(i);
            sb.append(String.format("%d. <b>%s</b> — %s\n   Streams: <code>%d</code> • %s\n",
                    i + 1,
                    escapeHtml(s.getTitle()),
                    escapeHtml(s.getArtist().getName()),
                    s.getPlayCount() != null ? s.getPlayCount() : 0,
                    s.getDuration() != null ? s.getDuration() : "3:30"));

            List<Map<String, String>> row = new ArrayList<>();
            Map<String, String> playBtn = new HashMap<>();
            playBtn.put("text", "▶️ Play: " + s.getTitle());
            playBtn.put("callback_data", "play_" + s.getId());
            row.add(playBtn);
            inlineKeyboard.add(row);
        }

        List<Map<String, String>> navRow = new ArrayList<>();
        Map<String, String> plBtn = new HashMap<>();
        plBtn.put("text", "📁 All Playlists");
        plBtn.put("callback_data", "playlists");
        navRow.add(plBtn);

        Map<String, String> backBtn = new HashMap<>();
        backBtn.put("text", "⬅️ Main Menu");
        backBtn.put("callback_data", "menu");
        navRow.add(backBtn);
        inlineKeyboard.add(navRow);

        sendMessage(chatId, sb.toString(), Collections.singletonMap("inline_keyboard", inlineKeyboard));
    }

    private void sendPlaylistsMenu(long chatId) {
        String text = "📁 <b>MelodyHub Playlists</b>\n\nSelect a playlist to view its songs:";

        List<List<Map<String, String>>> inlineKeyboard = new ArrayList<>();

        // Row 1: Khmer & English
        List<Map<String, String>> row1 = new ArrayList<>();
        Map<String, String> khBtn = new HashMap<>();
        khBtn.put("text", "🇰🇭 Khmer");
        khBtn.put("callback_data", "pl_Khmer");
        row1.add(khBtn);

        Map<String, String> enBtn = new HashMap<>();
        enBtn.put("text", "🇬🇧 English");
        enBtn.put("callback_data", "pl_English");
        row1.add(enBtn);
        inlineKeyboard.add(row1);

        // Row 2: Chinese & Korea
        List<Map<String, String>> row2 = new ArrayList<>();
        Map<String, String> cnBtn = new HashMap<>();
        cnBtn.put("text", "🇨🇳 Chinese");
        cnBtn.put("callback_data", "pl_Chinese");
        row2.add(cnBtn);

        Map<String, String> krBtn = new HashMap<>();
        krBtn.put("text", "🇰🇷 Korea");
        krBtn.put("callback_data", "pl_Korea");
        row2.add(krBtn);
        inlineKeyboard.add(row2);

        // Row 3: Japan & France
        List<Map<String, String>> row3 = new ArrayList<>();
        Map<String, String> jpBtn = new HashMap<>();
        jpBtn.put("text", "🇯🇵 Japan");
        jpBtn.put("callback_data", "pl_Japan");
        row3.add(jpBtn);

        Map<String, String> frBtn = new HashMap<>();
        frBtn.put("text", "🇫🇷 France");
        frBtn.put("callback_data", "pl_France");
        row3.add(frBtn);
        inlineKeyboard.add(row3);

        // Row 4: Phonk
        List<Map<String, String>> row4 = new ArrayList<>();
        Map<String, String> phBtn = new HashMap<>();
        phBtn.put("text", "🚗 Phonk");
        phBtn.put("callback_data", "pl_Phonk");
        row4.add(phBtn);
        inlineKeyboard.add(row4);

        // Row 5: Navigation
        List<Map<String, String>> row5 = new ArrayList<>();
        Map<String, String> trendBtn = new HashMap<>();
        trendBtn.put("text", "🔥 All Trending");
        trendBtn.put("callback_data", "popular");
        row5.add(trendBtn);

        Map<String, String> backBtn = new HashMap<>();
        backBtn.put("text", "⬅️ Main Menu");
        backBtn.put("callback_data", "menu");
        row5.add(backBtn);
        inlineKeyboard.add(row5);

        sendMessage(chatId, text, Collections.singletonMap("inline_keyboard", inlineKeyboard));
    }

    private void sendPlaylistSongs(long chatId, String playlistName) {
        List<Song> songs = songRepository.findByPlaylistCategory(playlistName);

        List<List<Map<String, String>>> inlineKeyboard = new ArrayList<>();

        if (songs.isEmpty()) {
            String empty = String.format("📁 <b>%s Playlist</b>\n\n" +
                    "No songs in this playlist yet.\n" +
                    "Upload tracks in MelodyHub and select <b>%s</b> to play them here!",
                    escapeHtml(playlistName), escapeHtml(playlistName));

            List<Map<String, String>> row = new ArrayList<>();
            Map<String, String> plBtn = new HashMap<>();
            plBtn.put("text", "⬅️ Back to Playlists");
            plBtn.put("callback_data", "playlists");
            row.add(plBtn);
            inlineKeyboard.add(row);

            List<Map<String, String>> row2 = new ArrayList<>();
            Map<String, String> menuBtn = new HashMap<>();
            menuBtn.put("text", "🏠 Main Menu");
            menuBtn.put("callback_data", "menu");
            row2.add(menuBtn);
            inlineKeyboard.add(row2);

            sendMessage(chatId, empty, Collections.singletonMap("inline_keyboard", inlineKeyboard));
            return;
        }

        StringBuilder sb = new StringBuilder(String.format("📁 <b>%s Playlist Songs:</b>\n\n", escapeHtml(playlistName)));

        for (int i = 0; i < songs.size(); i++) {
            Song s = songs.get(i);
            sb.append(String.format("%d. <b>%s</b> — %s (%s)\n",
                    i + 1,
                    escapeHtml(s.getTitle()),
                    escapeHtml(s.getArtist().getName()),
                    s.getDuration() != null ? s.getDuration() : "3:30"));

            List<Map<String, String>> row = new ArrayList<>();
            Map<String, String> playBtn = new HashMap<>();
            playBtn.put("text", "▶️ Play: " + s.getTitle());
            playBtn.put("callback_data", "play_" + s.getId());
            row.add(playBtn);
            inlineKeyboard.add(row);
        }

        List<Map<String, String>> navRow = new ArrayList<>();
        Map<String, String> plBtn = new HashMap<>();
        plBtn.put("text", "⬅️ Back to Playlists");
        plBtn.put("callback_data", "playlists");
        navRow.add(plBtn);

        Map<String, String> menuBtn = new HashMap<>();
        menuBtn.put("text", "🏠 Main Menu");
        menuBtn.put("callback_data", "menu");
        navRow.add(menuBtn);
        inlineKeyboard.add(navRow);

        sendMessage(chatId, sb.toString(), Collections.singletonMap("inline_keyboard", inlineKeyboard));
    }

    private void searchAndSendSongs(long chatId, String query) {
        Page<Song> results = songRepository.searchActiveSongs(query, PageRequest.of(0, 5));

        if (results.isEmpty()) {
            sendMessage(chatId, "No tracks found for: <b>" + escapeHtml(query) + "</b>\nTry another title or artist name.", null);
            return;
        }

        StringBuilder sb = new StringBuilder("<b>Search Results for \"" + escapeHtml(query) + "\":</b>\n\n");
        List<List<Map<String, String>>> inlineKeyboard = new ArrayList<>();

        for (Song s : results) {
            sb.append(String.format("• <b>%s</b> — %s\n",
                    escapeHtml(s.getTitle()),
                    escapeHtml(s.getArtist().getName())));

            List<Map<String, String>> row = new ArrayList<>();
            Map<String, String> playBtn = new HashMap<>();
            playBtn.put("text", "▶️ Play: " + s.getTitle());
            playBtn.put("callback_data", "play_" + s.getId());
            row.add(playBtn);
            inlineKeyboard.add(row);
        }

        Map<String, Object> replyMarkup = Collections.singletonMap("inline_keyboard", inlineKeyboard);
        sendMessage(chatId, sb.toString(), replyMarkup);
    }

    private void sendSongDetails(long chatId, Long songId) {
        Optional<Song> optSong = songRepository.findById(songId);
        if (optSong.isEmpty()) {
            sendMessage(chatId, "Song not found.", null);
            return;
        }

        Song song = optSong.get();

        // Increment play count
        song.setPlayCount(song.getPlayCount() + 1);
        songRepository.save(song);

        // If audioFilePath is a direct URL, send audio directly
        if (song.getAudioFilePath() != null && song.getAudioFilePath().startsWith("http")) {
            sendAudioFileDirectly(chatId, song);
            return;
        } else if (song.getAudioFilePath() != null && !song.getAudioFilePath().isEmpty()) {
            sendLocalAudioFile(chatId, song);
            return;
        }

        String text = String.format(
                "🎵 <b>Now Playing:</b>\n\n" +
                "Track: <b>%s</b>\n" +
                "Artist: <b>%s</b>\n" +
                "Duration: <code>%s</code>\n" +
                "Streams: <code>%d</code>",
                escapeHtml(song.getTitle()),
                escapeHtml(song.getArtist().getName()),
                song.getDuration() != null ? song.getDuration() : "3:30",
                song.getPlayCount() != null ? song.getPlayCount() : 1
        );

        sendMessage(chatId, text, createNowPlayingKeyboard());
    }

    private Map<String, Object> createNowPlayingKeyboard() {
        List<List<Map<String, String>>> inlineKeyboard = new ArrayList<>();

        List<Map<String, String>> row1 = new ArrayList<>();
        Map<String, String> plBtn = new HashMap<>();
        plBtn.put("text", "📁 All Playlists");
        plBtn.put("callback_data", "playlists");
        row1.add(plBtn);

        Map<String, String> trendBtn = new HashMap<>();
        trendBtn.put("text", "🔥 All Trending");
        trendBtn.put("callback_data", "popular");
        row1.add(trendBtn);
        inlineKeyboard.add(row1);

        List<Map<String, String>> row2 = new ArrayList<>();
        Map<String, String> menuBtn = new HashMap<>();
        menuBtn.put("text", "🏠 Main Menu");
        menuBtn.put("callback_data", "menu");
        row2.add(menuBtn);
        inlineKeyboard.add(row2);

        return Collections.singletonMap("inline_keyboard", inlineKeyboard);
    }

    private void sendAudioFileDirectly(long chatId, Song song) {
        try {
            Map<String, Object> payload = new HashMap<>();
            payload.put("chat_id", chatId);
            payload.put("audio", song.getAudioFilePath());
            payload.put("title", song.getTitle());
            payload.put("performer", song.getArtist().getName());
            payload.put("caption", "<b>" + escapeHtml(song.getTitle()) + "</b> by <i>" + escapeHtml(song.getArtist().getName()) + "</i>\nPowered by MelodyHub");
            payload.put("parse_mode", "HTML");
            payload.put("reply_markup", createNowPlayingKeyboard());

            String json = objectMapper.writeValueAsString(payload);
            String url = String.format("https://api.telegram.org/bot%s/sendAudio", botToken);

            HttpRequest req = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            httpClient.sendAsync(req, HttpResponse.BodyHandlers.ofString())
                    .thenAccept(res -> {
                        if (res.statusCode() != 200) {
                            logger.warn("sendAudio failed HTTP {}: {}", res.statusCode(), res.body());
                        }
                    });
        } catch (Exception e) {
            logger.warn("Failed to send audio directly: {}", e.getMessage());
        }
    }

    private void sendLocalAudioFile(long chatId, Song song) {
        try {
            org.springframework.core.io.Resource resource = storageService.loadAudioAsResource(song.getAudioFilePath());
            if (!resource.exists() || !resource.isReadable()) {
                logger.warn("Audio file not found on disk: {}", song.getAudioFilePath());
                return;
            }

            byte[] audioBytes = resource.getInputStream().readAllBytes();
            String boundary = "----MelodyHubBoundary" + UUID.randomUUID().toString().replace("-", "");

            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            // chat_id
            baos.write(("--" + boundary + "\r\n").getBytes(StandardCharsets.UTF_8));
            baos.write("Content-Disposition: form-data; name=\"chat_id\"\r\n\r\n".getBytes(StandardCharsets.UTF_8));
            baos.write((chatId + "\r\n").getBytes(StandardCharsets.UTF_8));

            // title
            baos.write(("--" + boundary + "\r\n").getBytes(StandardCharsets.UTF_8));
            baos.write("Content-Disposition: form-data; name=\"title\"\r\n\r\n".getBytes(StandardCharsets.UTF_8));
            baos.write((song.getTitle() + "\r\n").getBytes(StandardCharsets.UTF_8));

            // performer
            baos.write(("--" + boundary + "\r\n").getBytes(StandardCharsets.UTF_8));
            baos.write("Content-Disposition: form-data; name=\"performer\"\r\n\r\n".getBytes(StandardCharsets.UTF_8));
            baos.write((song.getArtist().getName() + "\r\n").getBytes(StandardCharsets.UTF_8));

            // caption
            baos.write(("--" + boundary + "\r\n").getBytes(StandardCharsets.UTF_8));
            baos.write("Content-Disposition: form-data; name=\"caption\"\r\n\r\n".getBytes(StandardCharsets.UTF_8));
            baos.write((song.getTitle() + " - " + song.getArtist().getName() + "\r\n").getBytes(StandardCharsets.UTF_8));

            // reply_markup
            String markupJson = objectMapper.writeValueAsString(createNowPlayingKeyboard());
            baos.write(("--" + boundary + "\r\n").getBytes(StandardCharsets.UTF_8));
            baos.write("Content-Disposition: form-data; name=\"reply_markup\"\r\n\r\n".getBytes(StandardCharsets.UTF_8));
            baos.write((markupJson + "\r\n").getBytes(StandardCharsets.UTF_8));

            // audio file data
            baos.write(("--" + boundary + "\r\n").getBytes(StandardCharsets.UTF_8));
            baos.write(("Content-Disposition: form-data; name=\"audio\"; filename=\"" + song.getAudioFilePath() + "\"\r\n").getBytes(StandardCharsets.UTF_8));
            baos.write("Content-Type: audio/mpeg\r\n\r\n".getBytes(StandardCharsets.UTF_8));
            baos.write(audioBytes);
            baos.write("\r\n".getBytes(StandardCharsets.UTF_8));

            // end boundary
            baos.write(("--" + boundary + "--\r\n").getBytes(StandardCharsets.UTF_8));

            String url = String.format("https://api.telegram.org/bot%s/sendAudio", botToken);

            HttpRequest req = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "multipart/form-data; boundary=" + boundary)
                    .POST(HttpRequest.BodyPublishers.ofByteArray(baos.toByteArray()))
                    .build();

            httpClient.sendAsync(req, HttpResponse.BodyHandlers.ofString())
                    .thenAccept(res -> {
                        if (res.statusCode() != 200) {
                            logger.warn("sendAudio multipart upload failed HTTP {}: {}", res.statusCode(), res.body());
                        }
                    });
        } catch (Exception e) {
            logger.warn("Failed to upload local audio to Telegram: {}", e.getMessage());
        }
    }

    private void sendHelpMessage(long chatId) {
        String help = "<b>MelodyHub Bot Commands & Guide</b>\n\n" +
                "• <code>/songs</code> - Show all songs in your catalog\n" +
                "• <code>/popular</code> - Show the most played songs\n" +
                "• <code>/playlists</code> - Choose songs by playlist (Khmer, English, Chinese, Korea, Japan, France, Phonk)\n" +
                "• <code>/search &lt;query&gt;</code> - Search any song title or artist\n" +
                "• <code>/start</code> - Return to Main Menu\n\n" +
                "Tip: You can also send any text directly to search!";

        List<List<Map<String, String>>> inlineKeyboard = new ArrayList<>();
        List<Map<String, String>> row = new ArrayList<>();
        Map<String, String> backBtn = new HashMap<>();
        backBtn.put("text", "⬅️ Main Menu");
        backBtn.put("callback_data", "menu");
        row.add(backBtn);
        inlineKeyboard.add(row);

        sendMessage(chatId, help, Collections.singletonMap("inline_keyboard", inlineKeyboard));
    }

    private void sendMessage(long chatId, String htmlText, Object replyMarkup) {
        try {
            Map<String, Object> payload = new HashMap<>();
            payload.put("chat_id", chatId);
            payload.put("text", htmlText);
            payload.put("parse_mode", "HTML");

            if (replyMarkup != null) {
                payload.put("reply_markup", replyMarkup);
            }

            String json = objectMapper.writeValueAsString(payload);
            String url = String.format("https://api.telegram.org/bot%s/sendMessage", botToken);

            HttpRequest req = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            httpClient.sendAsync(req, HttpResponse.BodyHandlers.ofString())
                    .thenAccept(res -> {
                        if (res.statusCode() != 200) {
                            logger.warn("Telegram sendMessage returned HTTP {}: {}", res.statusCode(), res.body());
                        }
                    })
                    .exceptionally(ex -> {
                        logger.warn("Failed to deliver Telegram message: {}", ex.getMessage());
                        return null;
                    });
        } catch (Exception e) {
            logger.warn("Failed to serialize Telegram message: {}", e.getMessage());
        }
    }

    private String escapeHtml(String text) {
        if (text == null) return "";
        return text.replace("&", "&amp;")
                   .replace("<", "&lt;")
                   .replace(">", "&gt;");
    }
}
