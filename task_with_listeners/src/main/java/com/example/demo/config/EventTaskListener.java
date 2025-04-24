package com.example.demo.config;

import com.example.demo.service.EventSender;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.task.listener.annotation.AfterTask;
import org.springframework.cloud.task.repository.TaskExecution;
import org.springframework.stereotype.Component;

@Slf4j
@AllArgsConstructor
@Component
public class EventTaskListener {

    private static final String APPLICATION_COMPLETED_EVENT = "Application Completed Event";
    private EventSender eventSender;

    @AfterTask
    public void afterTaskFirst(final TaskExecution taskExecution) {
        log.info("Sending {}", APPLICATION_COMPLETED_EVENT);

        eventSender.sendEvent(APPLICATION_COMPLETED_EVENT);
    }
}
