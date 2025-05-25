package com.example.demo.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;

import java.util.UUID;

@Slf4j
public class UserItemProcessor implements ItemProcessor<String, String> {
    @Override
    public String process(String item) {
        log.info("\uD83D\uDD04 Process user: {}", item);
        String cleanedName = item.replaceAll("\\d", "");
        String newName = cleanedName + "-" + UUID.randomUUID();
        log.info("> > > Converted user from {} to {}", item, newName);
        log.info("");
        return newName;
    }
}

