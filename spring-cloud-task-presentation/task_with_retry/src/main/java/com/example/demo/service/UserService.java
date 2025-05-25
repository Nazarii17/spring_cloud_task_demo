package com.example.demo.service;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Service
@EnableRetry
public class UserService {

    @Value("${server.throw-error:true}")
    private boolean throwError;

    @Value("${server.pass-on-last-attempt:false}")
    private boolean passOnLastAttempt;

    private final AtomicInteger attemptCounter = new AtomicInteger(0);

    @Retryable(retryFor = {RuntimeException.class}, maxAttempts = 3, backoff = @Backoff(delay = 2000))
    @SneakyThrows
    public void deleteAllUsers() {
        int attempt = attemptCounter.incrementAndGet();
        log.info("🚀 Attempt {}: Starting user cleanup task...", attempt);

        for (int i = 0; i < 10; i++) {
            deleteUsers(i, attempt);
        }

        log.info("✅ User cleanup task completed successfully on attempt {}!", attempt);
    }

    private void deleteUsers(final int i, final int attempt) throws InterruptedException {
        final String email = buildEmail(i);
        log.info("🔍 Found user: [{}]", email);
        Thread.sleep(500);

        if (throwError && i == 3) {
            boolean shouldPass = passOnLastAttempt && attempt == 3;

            if (!shouldPass) {
                log.error("❌ Attempt {}: Cannot delete user [{}]. User is protected", attempt, email);
                throw new RuntimeException("Attempt " + attempt + ": Cannot delete user with email: " + email);
            } else {
                log.info("✅ Attempt {}: Successfully deleted user [{}]", attempt, email);
            }
        } else {
            log.info("🗑️ Deleting user: [{}]", email);
        }
    }

    private String buildEmail(final int i) {
        final String repeated = String.valueOf(i).repeat(5);
        return String.format("%s@gmail.com", repeated);
    }
}
