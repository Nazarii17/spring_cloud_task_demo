package com.example.demo.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class UserCleanupTask implements CommandLineRunner {

    @Override
    public void run(String... args) throws InterruptedException {
        log.info("🚀 Starting user cleanup task...");

        for (int i = 0; i < 10; i++) {
            final String email = buildEmail(i);

            log.info("🔍 Found user: [{}]", email);
            Thread.sleep(500);
            log.info("🗑️ Deleting user: [{}]", email);
        }

        log.info("✅ User cleanup task completed!");
    }

    private String buildEmail(final int i) {
        final String repeated = String.valueOf(i).repeat(5);
        return String.format("%s@gmail.com", repeated);
    }
}
