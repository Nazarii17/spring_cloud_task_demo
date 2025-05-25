package com.example.demo.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.task.listener.TaskExecutionListener;
import org.springframework.cloud.task.repository.TaskExecution;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;

@Slf4j
@Component
public class CustomTaskLifecycleListener implements TaskExecutionListener {

    @Override
    public void onTaskStartup(final TaskExecution taskExecution) {
        log.info("Before task startup. Task Start Time={} Current Local Date Time={}",
                taskExecution.getStartTime(), LocalDateTime.now());
    }

    @Override
    public void onTaskEnd(final TaskExecution taskExecution) {
        log.info("Task '{}' completed. Duration={}s",
                taskExecution.getTaskName(),
                Duration.between(taskExecution.getStartTime(), taskExecution.getEndTime()).toSeconds());

        log.info("On Task End. Task End Time={} Current Local Date Time={}",
                taskExecution.getEndTime(), LocalDateTime.now());
    }

    @Override
    public void onTaskFailed(final TaskExecution taskExecution, final Throwable throwable) {
        TaskExecutionListener.super.onTaskFailed(taskExecution, throwable);
    }
}