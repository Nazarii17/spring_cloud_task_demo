package com.example.demo.task;

import com.example.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class UserCleanupTask implements CommandLineRunner {

    private final UserService userService;

    @Override
    public void run(String... args) {
        userService.deleteAllUsers();
    }
}
