package com.example.demo.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Configuration
public class BatchJobConfig {

    @Bean
    public Job userMofificationJob(final JobRepository jobRepository,
                                   final @Qualifier("processDataFlow") Flow processDataFlow,
                                   final @Qualifier("updateOktaStep") Step updateOktaStep,
                                   final @Qualifier("generateReportStep") Step generateReportStep) {
        return new JobBuilder("userModificationJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(processDataFlow)
                .next(updateOktaStep)
                .next(generateReportStep)
                .end()
                .build();
    }

    @Bean
    public Step processDataStep(final JobRepository jobRepository,
                                final @Qualifier("transactionManager") PlatformTransactionManager transactionManager,
                                final @Qualifier("userItemReader") ItemReader<String> userItemReader,
                                final @Qualifier("userItemProcessor") ItemProcessor<String, String> userItemProcessor,
                                final @Qualifier("userItemWriter") ItemWriter<String> userItemWriter) {
        return new StepBuilder("processDataStep", jobRepository)
                .<String, String>chunk(2, transactionManager)
                .reader(userItemReader)
                .processor(userItemProcessor)
                .writer(userItemWriter)
                .allowStartIfComplete(true)
                .build();
    }


    @Bean
    public Flow processDataFlow(final @Qualifier("processDataStep") Step processDataStep) {
        return new FlowBuilder<Flow>("processDataFlow")
                .start(processDataStep)
                .end();
    }

    @Bean
    public ItemReader<String> userItemReader() {
        final List<String> userList = new ArrayList<>();
        userList.add("User 1");
        userList.add("User 2");
        userList.add("User 3");
        userList.add("User 4");
        userList.add("User 5");
        userList.add("User 6");
        userList.add("User 7");
        userList.add("User 8");

        return new UserItemReader(userList);
    }

    @Bean
    public ItemProcessor<String, String> userItemProcessor() {
        return new UserItemProcessor();
    }

    @Bean
    public ItemWriter<String> userItemWriter() {
        return items -> items
                .forEach(user -> log.info("🗑️ Writing user: [{}]", user));
    }

    @Bean("updateOktaStep")
    public Step updateOktaStep(final JobRepository jobRepository,
                           final @Qualifier("transactionManager") PlatformTransactionManager transactionManager) {
        return new StepBuilder("Update OKTA API", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    log.info("Updating OKTA API");
                    return RepeatStatus.FINISHED;
                },transactionManager)
                .build();
    }

    @Bean("generateReportStep")
    public Step genetateReportStep(final JobRepository jobRepository,
                           final @Qualifier("transactionManager") PlatformTransactionManager transactionManager) {
        return new StepBuilder("Generate Report Step", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    log.info("GENERATING REPORT. . . ");
                    return RepeatStatus.FINISHED;
                },transactionManager)
                .build();
    }
}

