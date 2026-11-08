package com.melodyhub.config;

import com.melodyhub.repository.SongRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

/**
 * Data Initializer for MelodyHub.
 * Preserves user-uploaded catalog integrity without inserting unwanted dummy songs.
 */
@Configuration
public class DataInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    private final SongRepository songRepository;

    public DataInitializer(SongRepository songRepository) {
        this.songRepository = songRepository;
    }

    @Override
    public void run(String... args) {
        logger.info("MelodyHub database ready. Total catalog songs: {}", songRepository.count());
    }
}
