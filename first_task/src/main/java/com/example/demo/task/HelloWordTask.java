package com.example.demo.task;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class HelloWordTask implements CommandLineRunner {

    @Override
    public void run(String... args) {
        System.out.println("Hello World");
    }
}
