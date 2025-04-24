package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.task.configuration.EnableTask;


@EnableTask
@SpringBootApplication
public class TaskWithListeners {

    public static void main(String[] args) {
        SpringApplication.run(TaskWithListeners.class, args);
    }
}
