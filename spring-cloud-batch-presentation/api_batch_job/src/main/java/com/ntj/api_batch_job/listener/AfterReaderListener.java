package com.ntj.api_batch_job.listener;

import com.ntj.api_batch_job.dto.UserRecord;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.AfterRead;
import org.springframework.batch.core.scope.context.StepSynchronizationManager;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

import static com.ntj.api_batch_job.enums.ReadStatus.NEW;

@Component
@Slf4j
public class AfterReaderListener {

    @AfterRead
    public void afterRead() {
        final StepExecution stepExecution = StepSynchronizationManager.getContext().getStepExecution();
        final ExecutionContext jobContext = stepExecution.getJobExecution().getExecutionContext();

        if (!jobContext.containsKey("I_JUST_WANT_TO_ASK_RECORD")) {
            final UserRecord userRecord =
                    new UserRecord(UUID.randomUUID(), "I_JUST_WANT_TO_ASK_RECORD", NEW, 0, LocalDateTime.now(), LocalDateTime.now());

            jobContext.put("I_JUST_WANT_TO_ASK_RECORD", userRecord);
            log.info("Added UserRecord to job execution context: {}", userRecord);
        }
    }
}
