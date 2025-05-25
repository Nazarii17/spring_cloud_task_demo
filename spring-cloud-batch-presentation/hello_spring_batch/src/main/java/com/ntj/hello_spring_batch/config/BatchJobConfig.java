package com.ntj.hello_spring_batch.config;

import com.ntj.hello_spring_batch.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.core.step.tasklet.TaskletStep;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.CompositeItemProcessor;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class BatchJobConfig {

    private final AppProperties appProperties;

    @Bean("firstJob")
    public Job firstJob(final JobRepository jobRepository,
                        final @Qualifier("firstStep") TaskletStep firstStep,
                        final @Qualifier("secondStep") Step secondStep,
                        final @Qualifier("thirdStep") Step thirdStep) {
        return new JobBuilder("first_job", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(firstStep)
                .next(secondStep)
                .next(thirdStep)
                .build();
    }

    @Bean("firstStep")
    public TaskletStep firstStep(final JobRepository jobRepository,
                                 final PlatformTransactionManager platformTransactionManager) {
        return new StepBuilder("first_step", jobRepository)
                .tasklet(new Tasklet() {
                    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) {
                        log.info("Hello World from first step");
                        return RepeatStatus.FINISHED;
                    }
                }, platformTransactionManager)
                .build();
    }

    @Bean("secondStep")
    public Step secondStep(final JobRepository jobRepository,
                           final PlatformTransactionManager platformTransactionManager) {
        return new StepBuilder("second_step", jobRepository)
                .<String, String>chunk(1, platformTransactionManager)

                .reader(new ItemReader<String>() {
                    final Iterator<String> iterator = appProperties.getNames().iterator();

                    @Override
                    public String read() {
                        if (iterator.hasNext()) {
                            final String name = iterator.next();
                            log.info("- Read name: {}", name);
                            return name;
                        }
                        return null;
                    }
                })

                .processor(new ItemProcessor<String, String>() {
                    @Override
                    public String process(final String name) {
                        final String transformedName = name.toUpperCase();
                        log.info("- Transformed from: {} to: {} in FIRST PROCESSOR", name, transformedName);
                        return transformedName;
                    }
                })

//                .processor(new ItemProcessor<String, String>() {
//                    @Override
//                    public String process(final String name) {
//                        final String transformedName = name + LocalDateTime.now();
//                        log.info("- Transformed from: {} to: {} in SECOND PROCESSOR", name, transformedName);
//                        return transformedName;
//                    }
//                })

//                .processor(compositeProcessor())

                .writer(new ItemWriter<String>() {
                    @Override
                    public void write(final Chunk<? extends String> chunk) {
                        chunk.getItems()
                                .forEach(s -> log.info("- Item to write: {}", s));
                    }
                })

                .allowStartIfComplete(true)
                .build();
    }

    @Bean("thirdStep")
    public Step thirdStep(final JobRepository jobRepository,
                          final PlatformTransactionManager platformTransactionManager,
                          final @Qualifier("namesReader") ItemReader<String> namesReader,
                          final @Qualifier("namesProcessor") ItemProcessor<String, User> namesProcessor,
                          final @Qualifier("namesWriter") ItemWriter<User> namesWriter) {
        return new StepBuilder("third_step", jobRepository)
                .<String, User>chunk(3, platformTransactionManager)

                .reader(namesReader)

                .processor(namesProcessor)

                .writer(namesWriter)

                .allowStartIfComplete(true)
                .build();
    }

    @Bean("namesReader")
    public ItemReader<String> namesReader() {
        final List<String> names = appProperties.getNames();
        final Iterator<String> iterator = names.iterator();
        return new ItemReader<String>() {
            @Override
            public String read() {
                if (iterator.hasNext()) {
                    final String name = iterator.next();
                    log.info("- - Read name: {}", name);
                    return name;
                }
                return null;
            }
        };
    }

    @Bean("namesProcessor")
    public ItemProcessor<String, User> namesProcessor() {
        return new ItemProcessor<String, User>() {
            @Override
            public User process(final String name) {
                final String transformedName = name.toUpperCase();
                final User user = new User(UUID.randomUUID(), transformedName);
                log.info("- - Transformed from: {} to: {}", name, user);
                return user;
            }
        };
    }

    @Bean("namesWriter")
    private static ItemWriter<User> namesWriter() {
        return new ItemWriter<User>() {
            @Override
            public void write(final Chunk<? extends User> chunk) {
                chunk.getItems()
                        .forEach(u -> log.info("- - Item to write: {}", u));
            }
        };
    }

    private static ItemProcessor<String, String> getProcessor(final String processorOrder,
                                                              final Function<String, String> transformationFunction) {
        return name -> {
            final String transformedName = transformationFunction.apply(name);
            log.info("- - - Transformed from: {} to: {} in {} PROCESSOR", name, transformedName, processorOrder);
            return transformedName;
        };
    }

    @Bean
    public CompositeItemProcessor<String, String> compositeProcessor() {
        final CompositeItemProcessor<String, String> compositeProcessor = new CompositeItemProcessor<>();
        final ItemProcessor<String, String> firstProcessor = getProcessor("FIRST", String::toUpperCase);
        final ItemProcessor<String, String> secondProcessor = getProcessor("SECOND", name -> name + LocalDateTime.now());

        compositeProcessor.setDelegates(List.of(firstProcessor, secondProcessor));
//        compositeProcessor.setDelegates(List.of(secondProcessor, firstProcessor));

        return compositeProcessor;
    }
}
