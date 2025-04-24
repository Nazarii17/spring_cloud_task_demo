package com.example.demo.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.task.listener.annotation.AfterTask;
import org.springframework.cloud.task.listener.annotation.BeforeTask;
import org.springframework.cloud.task.repository.TaskExecution;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class EmailTaskListener {

    final String RECIPIENT_EMAIL = "random.user@gmail.com";

    @BeforeTask
    public void beforeTask(TaskExecution taskExecution) {
        log.info("Check if EMAIL recipient exist");
    }

    @AfterTask
    public void afterTask(TaskExecution taskExecution) {
        log.info("Preparing Email... TaskId={} TaskName={}",
                taskExecution.getExecutionId(), taskExecution.getTaskName());

        final String emailStatus = getEmailStatus(taskExecution);

        log.info("Sending Application " + emailStatus + " Email to {} for TaskId={}",
                RECIPIENT_EMAIL, taskExecution.getExecutionId());
    }

    private static String getEmailStatus(TaskExecution taskExecution) {
        String emailStatus;
        if (taskExecution.getExitCode() == 1 ) {
            emailStatus = "Failed";
        } else {
             emailStatus = "Successfully Completed";
        }
        return emailStatus;
    }
}
