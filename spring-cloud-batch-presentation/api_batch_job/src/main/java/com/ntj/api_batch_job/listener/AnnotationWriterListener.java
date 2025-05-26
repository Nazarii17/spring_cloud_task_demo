package com.ntj.api_batch_job.listener;

import com.ntj.api_batch_job.entity.ApiUser;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.AfterWrite;
import org.springframework.batch.core.scope.context.StepSynchronizationManager;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AnnotationWriterListener {

    @PersistenceContext
    private EntityManager entityManager;

    @AfterWrite
    public void afterWrite() {
        final StepExecution stepExecution = StepSynchronizationManager.getContext().getStepExecution();
        if (stepExecution == null) {
            log.warn("No StepExecution available in AfterWrite");
            return;
        }

        final ExecutionContext jobContext = stepExecution.getJobExecution().getExecutionContext();

        if (jobContext.containsKey("I_JUST_WANT_TO_ASK_ENTITY") &&
                !jobContext.containsKey("I_JUST_WANT_TO_ASK_ENTITY_MERGED")) {

            final ApiUser extraEntity = (ApiUser) jobContext.get("I_JUST_WANT_TO_ASK_ENTITY");

            entityManager.merge(extraEntity);
            log.info("Merged ApiUser: {}", extraEntity);

            jobContext.put("I_JUST_WANT_TO_ASK_ENTITY_MERGED", true);
            jobContext.put("addedExtraUser", true);
        }
    }
}
