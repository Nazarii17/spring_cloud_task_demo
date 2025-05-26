package com.ntj.api_batch_job.decider;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JobDecider implements JobExecutionDecider {

    @Override
    public FlowExecutionStatus decide(final JobExecution jobExecution,
                                      final StepExecution stepExecution) {
        final ExecutionContext jobContext = jobExecution.getExecutionContext();

        boolean isAddedExtraUser = false;

        if (jobContext.containsKey("addedExtraUser")) {
            Object value = jobContext.get("addedExtraUser");

            if (value instanceof Boolean) {
                isAddedExtraUser = (boolean) value;
                log.info("JobDecider: 'addedExtraUser' found in context: {}", isAddedExtraUser);
            } else {
                log.warn("JobDecider: 'addedExtraUser' found but is not a Boolean. Actual type: {}. Defaulting to false.",
                        value != null ? value.getClass().getName() : "null");
            }
        } else {
            log.warn("JobDecider: 'addedExtraUser' not found in Job Context. Defaulting to false.");
        }

        log.info("In JobDecider: Determining flow based on 'isAddedExtraUser' = {}", isAddedExtraUser);

        String state;
        if (isAddedExtraUser) {
            state = "ADDED";
        } else {
            state = "NOT_ADDED";
        }
        return new FlowExecutionStatus(state);
    }
}
