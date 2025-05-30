package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.task.configuration.EnableTask;

@EnableTask
@SpringBootApplication
public class FirstTask {

    public static void main(String[] args) {
        SpringApplication.run(FirstTask.class, args);
    }
}
