package com.ntj.hello_spring_batch.listener;

import com.ntj.hello_spring_batch.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ItemWriteListener;
import org.springframework.batch.item.Chunk;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class WriterListener implements ItemWriteListener<User> {

    @Override
    public void afterWrite(Chunk<? extends User> items) {
        log.info("In WriterListener afterWrite");
        items.getItems().forEach(i -> log.info("Hello {}", i.name()));
    }
}
