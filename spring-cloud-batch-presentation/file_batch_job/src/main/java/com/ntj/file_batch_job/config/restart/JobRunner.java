package com.ntj.file_batch_job.config.restart;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JobRunner {

    private final JobLauncher jobLauncher;
    private final Job importUserJob;

    public void runJobWithId(final Integer runId) throws Exception {
        final JobParameters jobParameters = new JobParametersBuilder()
                .addJobParameter("custom.id", runId, Integer.class)
                .toJobParameters();

        jobLauncher.run(importUserJob, jobParameters);
    }
}

