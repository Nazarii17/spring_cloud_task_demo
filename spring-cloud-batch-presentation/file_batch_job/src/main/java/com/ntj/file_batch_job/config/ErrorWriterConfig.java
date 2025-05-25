package com.ntj.file_batch_job.config;

import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.PassThroughLineAggregator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

import java.nio.charset.StandardCharsets;

@Configuration
public class ErrorWriterConfig {

    @Value("${batch-job.error-csv.header}")
    private String errorCsvHeader;

    @Bean
    public FlatFileItemWriter<String> errorCsvWriter() {
        final FlatFileItemWriter<String> writer = new FlatFileItemWriter<>();

        writer.setName("errorCsvWriter");
        writer.setResource(new FileSystemResource("file_batch_job/src/main/resources/error_output.csv"));
        writer.setHeaderCallback(writer1 -> writer1.write(errorCsvHeader));
        writer.setLineAggregator(new PassThroughLineAggregator<>());
        writer.setAppendAllowed(false);
        writer.setShouldDeleteIfExists(true);
        writer.setEncoding(StandardCharsets.UTF_8.name());

        return writer;
    }

    @Bean
    public String errorCsvHeaderValue() {
        return errorCsvHeader;
    }
}


