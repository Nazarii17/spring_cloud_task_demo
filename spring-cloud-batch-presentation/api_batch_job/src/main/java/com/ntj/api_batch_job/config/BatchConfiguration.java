package com.ntj.api_batch_job.config;

import com.ntj.api_batch_job.decider.JobDecider;
import com.ntj.api_batch_job.dto.UserRecord;
import com.ntj.api_batch_job.entity.ApiUser;
import com.ntj.api_batch_job.listener.*;
import com.ntj.api_batch_job.processor.UserProcessor;
import com.ntj.api_batch_job.reader.HttpUserItemReader;
import com.ntj.api_batch_job.writer.UserItemWriter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.core.step.tasklet.TaskletStep;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class BatchConfiguration {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager platformTransactionManager;

    @Bean
    public Job importUserJob(@Qualifier("firstStep") final Step firstStep,
                             @Qualifier("addedStep") final Step addedStep,
                             @Qualifier("notAddedStep") final Step notAddedStep,
                             final JobDecider decider) {
        return new JobBuilder("testJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(firstStep)

                .next(decider)
                    .on("ADDED").to(addedStep)
                .from(decider)
                    .on("NOT_ADDED").to(notAddedStep)
                .end()

                .build();
    }

    @Bean("firstStep")
    public Step firstStep(final RefreshUsersListener refreshUsersListener,
                          final HttpUserItemReader httpUserItemReader,
                          final UserProcessor userProcessor,
                          final UserItemWriter userItemWriter,
                          final AfterReaderListener afterReaderListener,
                          final AfterProcessorListener afterProcessorListener,
                          final AnnotationWriterListener annotationWriterListener) {
        return new StepBuilder("firstStep", jobRepository)
                .<UserRecord, ApiUser>chunk(2, platformTransactionManager)

                .reader(httpUserItemReader)
                .processor(userProcessor)
                .writer(userItemWriter)
                .listener(refreshUsersListener)

//                .listener(afterReaderListener)
//                .listener(annotationWriterListener)
//                .listener(afterProcessorListener)

                .build();
    }

    @Bean("addedStep")
    public TaskletStep addedStep(final JobRepository jobRepository,
                                 final PlatformTransactionManager platformTransactionManager) {
        return new StepBuilder("added_step", jobRepository)
                .tasklet(new Tasklet() {
                    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) {
                        log.info("Extra User Added");
                        return RepeatStatus.FINISHED;
                    }
                }, platformTransactionManager)
                .build();
    }

    @Bean("notAddedStep")
    public TaskletStep notAddedStep(final JobRepository jobRepository,
                                 final PlatformTransactionManager platformTransactionManager) {
        return new StepBuilder("not_added_step", jobRepository)
                .tasklet(new Tasklet() {
                    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) {
                        log.info("Extra User NOT Added");
                        return RepeatStatus.FINISHED;
                    }
                }, platformTransactionManager)
                .build();
    }
}

