package com.ntj.file_batch_job.listener.order;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.annotation.BeforeJob;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Order(Ordered.LOWEST_PRECEDENCE)
@Slf4j
@Component
public class CustomFirstInOrderExecutionListener1 {

    @Value("${spring.batch.job.enabled}")
    private boolean springBatchJobEnabled;

    @BeforeJob()
    public void beforeJob() {
        if(springBatchJobEnabled) {
            log.info("< = = = = = 1 FROM 'CustomFirstInOrderExecutionListener1' FIRST IN ORDER = = = = = >");
        }

    }
}
