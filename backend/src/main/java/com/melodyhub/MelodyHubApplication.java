package com.melodyhub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MelodyHubApplication {

    public static void main(String[] args) {
        SpringApplication.run(MelodyHubApplication.class, args);
    }
}

