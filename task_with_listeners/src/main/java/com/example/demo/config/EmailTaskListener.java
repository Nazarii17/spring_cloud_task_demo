package com.example.demo.config;

import com.example.demo.service.EmailService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.task.listener.annotation.AfterTask;
import org.springframework.cloud.task.listener.annotation.BeforeTask;
import org.springframework.cloud.task.repository.TaskExecution;
import org.springframework.stereotype.Component;

@Slf4j
@AllArgsConstructor
@Component
public class EmailTaskListener {

    private final EmailService emailService;

    final String RECIPIENT_EMAIL = "random.user@gmail.com";

    @BeforeTask
    public void beforeTask(TaskExecution taskExecution) {
        log.info("Check if EMAIL recipient exists");

        validateRecipientEmail();

        log.info("Recipient email is valid: {}", RECIPIENT_EMAIL);
    }

    @AfterTask
    public void afterTask(TaskExecution taskExecution) {
        log.info("Preparing Email... TaskId={} TaskName={}",
                taskExecution.getExecutionId(), taskExecution.getTaskName());

        final String subject = emailService.getSubject(taskExecution);
        final String body = emailService.getBody(taskExecution);

        log.info("Sending Application Successfully Completed Email to {} for TaskId={}",
                RECIPIENT_EMAIL, taskExecution.getExecutionId());

        emailService.sendEmail(RECIPIENT_EMAIL, subject, body);
    }

    private void validateRecipientEmail() {
        if (RECIPIENT_EMAIL == null
                || RECIPIENT_EMAIL.trim().isEmpty()
                || !RECIPIENT_EMAIL.matches("^[\\w.-]+@[\\w.-]+\\.\\w+$")) {
            throw new IllegalArgumentException("Invalid or missing recipient email.");
        }
    }
}
