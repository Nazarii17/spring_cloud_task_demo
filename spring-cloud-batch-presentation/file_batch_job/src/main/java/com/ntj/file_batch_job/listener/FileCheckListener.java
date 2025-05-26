package com.ntj.file_batch_job.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionException;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.annotation.AfterJob;
import org.springframework.batch.core.annotation.BeforeJob;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.IOException;

@Slf4j
@Component
public class FileCheckListener {

    private final String inputFilePath;
    private final ResourceLoader resourceLoader;

    public FileCheckListener(@Value("${file.input}") String inputFilePath,
                             ResourceLoader resourceLoader) {
        this.inputFilePath = inputFilePath;
        this.resourceLoader = resourceLoader;
    }

    @BeforeJob()
    public void beforeJob(JobExecution jobExecution) throws JobExecutionException {
        log.info("< - - - - Job '{}' starting. Initiating pre-job file check. - - - - >",
                jobExecution.getJobInstance().getJobName());
        log.debug("Configured input file path: {}", inputFilePath);

        try {
            final Resource resource = resourceLoader.getResource(inputFilePath);

            if (!resource.exists()) {
                final String errorMessage = "Input file NOT FOUND: " + inputFilePath + ". Aborting job.";
                log.error(errorMessage);
                throw new JobExecutionException(errorMessage);
            }

            if (!resource.isFile()) {
                final String errorMessage = "Input path '{}' is not a file. Aborting job.";
                log.error(errorMessage, inputFilePath);
                throw new JobExecutionException(errorMessage);
            }

            log.info("Input file found successfully: '{}'", resource.getFile().getAbsolutePath());

        } catch (FileNotFoundException e) {
            log.error("Input file specified in '{}' cannot be accessed or does not exist. Details: {}. Aborting job."
                    , inputFilePath, e.getMessage(), e);
            throw new JobExecutionException("Failed to access input file: " + inputFilePath, e);
        } catch (IOException e) {
            log.error("An I/O error occurred while checking input file '{}'. Details: {}. Aborting job.",
                    inputFilePath, e.getMessage(), e);
            throw new JobExecutionException("I/O error during file check for: " + inputFilePath, e);
        } catch (JobExecutionException e) {
            throw e;
        } catch (Exception e) {
            log.error("An unexpected error occurred during pre-job file check for '{}'. Details: {}. Aborting job.",
                    inputFilePath, e.getMessage(), e);
            throw new JobExecutionException("Unexpected error during file check for: " + inputFilePath, e);
        }
    }

    @AfterJob
    public void afterJob(JobExecution jobExecution) {
        log.info("< - - - - Job '{}' completed with status: {} - - - - >",
                jobExecution.getJobInstance().getJobName(), jobExecution.getStatus());
    }
}