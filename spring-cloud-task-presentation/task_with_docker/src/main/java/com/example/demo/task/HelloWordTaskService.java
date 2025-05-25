package com.example.demo.task;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class HelloWordTaskService implements CommandLineRunner {

    @Override
    public void run(String... args) {
        try {
            // Default message
            final String message = (args.length > 0) ? args[0] : "Hello World";

            // Parse sleep duration (default to 5 seconds)
            final int sleepDuration = (args.length > 1) ? Integer.parseInt(args[1]) : 5;

            // Sleep for the specified duration
            System.out.println("Sleeping for " + sleepDuration + " seconds...");
            Thread.sleep(sleepDuration * 1000L);

            // Print message after delay
            System.out.println(message);
        } catch (NumberFormatException e) {
            System.out.println("Invalid sleep duration. Please provide a valid integer.");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Sleep interrupted");
        }
    }
}