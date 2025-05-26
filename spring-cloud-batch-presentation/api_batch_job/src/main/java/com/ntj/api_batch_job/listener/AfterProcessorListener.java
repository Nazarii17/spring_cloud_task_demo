package com.ntj.api_batch_job.listener;

import com.ntj.api_batch_job.dto.UserRecord;
import com.ntj.api_batch_job.entity.ApiUser;
import com.ntj.api_batch_job.enums.ApiUserStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.AfterProcess;
import org.springframework.batch.core.scope.context.StepSynchronizationManager;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AfterProcessorListener {

    @AfterProcess
    public void afterProcess() {
        final StepExecution stepExecution = StepSynchronizationManager.getContext().getStepExecution();
        final ExecutionContext jobContext = stepExecution.getJobExecution().getExecutionContext();

        if (jobContext.containsKey("I_JUST_WANT_TO_ASK_RECORD") && !jobContext.containsKey("I_JUST_WANT_TO_ASK_ENTITY")) {
            final UserRecord apiUser = (UserRecord) jobContext.get("I_JUST_WANT_TO_ASK_RECORD");

            if (apiUser != null) {
                final ApiUser entity = ApiUser.builder()
                        .name(apiUser.name())
                        .readStatus(ApiUserStatus.NEW)
                        .iteration(apiUser.iteration())
                        .name(apiUser.name())
                        .build();

                jobContext.put("I_JUST_WANT_TO_ASK_ENTITY", entity);
                log.info("Converted UserRecord to ApiUser and saved in context: {}", entity);
            }
        }
    }
}
