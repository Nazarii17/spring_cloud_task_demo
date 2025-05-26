package com.ntj.file_batch_job.config.restart;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class StartupRunner implements CommandLineRunner {

    private final JobLauncher jobLauncher;
    private final Job importUserJob;

    @Value("${spring.batch.job.enabled}")
    private boolean springBatchJobEnabled;

    @Override
    public void run(String... args) throws Exception {
        if(!springBatchJobEnabled){
            log.info("< - - - Start Retryable Batch Job - - - >");
            runJobWithId(1);
        }
    }

    public void runJobWithId(final Integer runId) throws Exception {
        final JobParameters jobParameters = new JobParametersBuilder()
                .addJobParameter("custom.id", runId, Integer.class)
                .toJobParameters();

        jobLauncher.run(importUserJob, jobParameters);
    }
}

