package com.ntj.api_batch_job.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Slf4j
@RequiredArgsConstructor
@Component
public class RefreshUsersListener implements StepExecutionListener {

    @Value("${api.user.complete-path}")
    private String apiUrl;
    private final RestTemplate restTemplate;

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        log.info("Executing afterStep for step: {}", stepExecution.getStepName());

        if (stepExecution.getStatus() == BatchStatus.COMPLETED) {
            try {
                log.info("Attempting to call external API to signal completion: {}", apiUrl);
                restTemplate.getForObject(apiUrl, String.class);
                log.info("Successfully called external API: {}", apiUrl);
            } catch (Exception e) {
                log.error("Failed to call external API {}: {}", apiUrl, e.getMessage(), e);
            }
        }

        return stepExecution.getExitStatus();
    }
}
