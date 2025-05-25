package com.ntj.file_batch_job.config;

import com.ntj.file_batch_job.model.Person;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

/**
 * Configuration class dedicated to defining the FlatFileItemReader for Person objects.
 */
@Configuration
public class CsvItemReaderConfiguration {

    @Value("${file.input}")
    private Resource inputCsvResource;

    @Bean("csvFileItemReader")
    public FlatFileItemReader<Person> csvFileItemReader() {
        return new FlatFileItemReaderBuilder<Person>()
                .name("personItemReader")
                .resource(inputCsvResource)
                .delimited()
                .names(new String[]{"firstName", "lastName", "email", "registrationState"})
                .linesToSkip(1)
                .targetType(Person.class)
                .saveState(true)
                .build();
    }
}
