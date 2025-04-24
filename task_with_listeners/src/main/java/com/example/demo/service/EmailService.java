package com.example.demo.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.task.repository.TaskExecution;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EmailService {

    public void sendEmail(String to, String subject, String body) {
        log.info("Sending email to: {} with subject: {}",to, subject);
    }

    public String getBody(TaskExecution taskExecution) {
        return String.format("Hello,\n\nThe task '%s' (ID: %s) has been successfully completed." +
                        "\n\nRegards,\nYour Application",
                taskExecution.getTaskName(), taskExecution.getExecutionId());
    }

    public String getSubject(TaskExecution taskExecution) {
        return "Task Completed: " + taskExecution.getTaskName();
    }
}
