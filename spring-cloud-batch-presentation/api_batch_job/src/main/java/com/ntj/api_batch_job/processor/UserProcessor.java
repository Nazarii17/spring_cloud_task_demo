package com.ntj.api_batch_job.processor;


import com.ntj.api_batch_job.dto.UserRecord;
import com.ntj.api_batch_job.entity.ApiUser;
import com.ntj.api_batch_job.enums.ApiUserStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserProcessor implements ItemProcessor<UserRecord, ApiUser> {

    @Override
    public ApiUser process(UserRecord userRecord) {
        log.info("Processing UserRecord with ID: {} for transformation.", userRecord.id());

        return ApiUser.builder()
                .id(userRecord.id())
                .name(userRecord.name())
                .iteration(userRecord.iteration())
                .readStatus(ApiUserStatus.valueOf(userRecord.readStatus().name()))
                .build();
    }
}
