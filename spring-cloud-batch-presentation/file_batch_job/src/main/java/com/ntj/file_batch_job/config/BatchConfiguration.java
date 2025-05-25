package com.ntj.file_batch_job.config;

import com.ntj.file_batch_job.listener.order.CustomFirstInOrderExecutionListener1;
import com.ntj.file_batch_job.listener.order.CustomFirstInOrderExecutionListener2;
import com.ntj.file_batch_job.listener.ErrorReaderListener;
import com.ntj.file_batch_job.listener.FileCheckListener;
import com.ntj.file_batch_job.model.Person;
import com.ntj.file_batch_job.processor.PersonItemProcessor;
import com.ntj.file_batch_job.writer.ConsoleItemWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.ItemReadListener;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@RequiredArgsConstructor
@Configuration
public class BatchConfiguration {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final FileCheckListener fileCheckListener;
    private final CustomFirstInOrderExecutionListener1 customFirstInOrderExecutionListener1;
    private final CustomFirstInOrderExecutionListener2 customFirstInOrderExecutionListener2;

    @Bean
    public Job importUserJob(FlatFileItemReader<Person> csvFileItemReader,
                             ErrorReaderListener errorReaderListener,
                             PersonItemProcessor personItemProcessor) {
        return new JobBuilder("importUserJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .flow(readUsersFromFile(csvFileItemReader, errorReaderListener, personItemProcessor))
                .end()
                .listener(customFirstInOrderExecutionListener2)
                .listener(customFirstInOrderExecutionListener1)
                .listener(fileCheckListener)
                .build();
    }

    @Bean
    public Step readUsersFromFile(FlatFileItemReader<Person> csvFileItemReader,
                                  ErrorReaderListener errorReaderListener,
                                  PersonItemProcessor personItemProcessor) {
        return new StepBuilder("readUsersFromFile", jobRepository)
                .<Person, Person>chunk(1, transactionManager)
                .reader(csvFileItemReader)
                .processor(personItemProcessor)
                .writer(consoleItemWriter())
                .listener((ItemReadListener<? super Person>) errorReaderListener)
                .listener((StepExecutionListener) errorReaderListener)
                .build();
    }

    @Bean
    public PersonItemProcessor personItemProcessor() {
        return new PersonItemProcessor();
    }

    @Bean
    public ConsoleItemWriter consoleItemWriter() {
        return new ConsoleItemWriter();
    }
}

