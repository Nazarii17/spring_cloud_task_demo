package com.ntj.file_batch_job.writer;

import com.ntj.file_batch_job.model.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.annotation.BeforeWrite;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;

public class ConsoleItemWriter implements ItemWriter<Person> {

    private static final Logger log = LoggerFactory.getLogger(ConsoleItemWriter.class);

    @Override
    public void write(Chunk<? extends Person> chunk) {
        for (Person person : chunk.getItems()) {
            log.info("Writing: {}", person);
        }
    }

    @BeforeWrite
    public void beforeWrite() {

        log.info("In WriterListener beforeWrite");

    }
}
