package com.example.demo.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EventSender {

    public void sendEvent(String event) {
        log.info("Event successfully sent: {}", event);
    }
}
