package com.example.demo.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.task.listener.annotation.AfterTask;
import org.springframework.cloud.task.repository.TaskExecution;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class EventTaskListener {

    @AfterTask
    public void afterTaskFirst(TaskExecution taskExecution) {
        log.info("Preparing Application Completed Event");

        String eventStatus;
        if (taskExecution.getExitCode() == 1 ) {
            eventStatus = "Failed";
        } else {
            eventStatus = "Successfully Completed";
        }

        log.info("Sending Application " + eventStatus + " Event");
    }
}
