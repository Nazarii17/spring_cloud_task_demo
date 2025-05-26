package com.ntj.api_batch_job.reader;

import com.ntj.api_batch_job.dto.UserRecord;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.annotation.OnReadError;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemStream;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class HttpUserItemReader implements ItemReader<UserRecord>, ItemStream {

    private final RestTemplate restTemplate;

    @Value("${api.user.get-users-path}")
    private String apiUrl;

    private List<UserRecord> allUserRecords;
    private Iterator<UserRecord> userRecordIterator;
    private int currentItemCount = 0;
    private static final String CURRENT_ITEM_COUNT_KEY = "currentItemCount";

    @Override
    public void open(ExecutionContext executionContext) throws ItemStreamException {
        log.info("HttpUserItemReader: Opening stream. Attempting to fetch data from: {}", apiUrl);
        try {
            final ResponseEntity<List<UserRecord>> response = restTemplate
                    .exchange(apiUrl, HttpMethod.GET, null, new ParameterizedTypeReference<>() {
                    });

            this.allUserRecords = Objects.requireNonNullElseGet(response.getBody(), ArrayList::new);
            allUserRecords.sort(Comparator.comparing(UserRecord::createdAt).reversed());

            this.userRecordIterator = this.allUserRecords.iterator();
            log.info("Successfully fetched {} user records from {}.", allUserRecords.size(), apiUrl);

            if (executionContext.containsKey(CURRENT_ITEM_COUNT_KEY)) {
                currentItemCount = executionContext.getInt(CURRENT_ITEM_COUNT_KEY);
                log.info("HttpUserItemReader: Restoring state. Starting from item index: {}", currentItemCount);
                for (int i = 0; i < currentItemCount; i++) {
                    if (userRecordIterator.hasNext()) {
                        userRecordIterator.next();
                    } else {
                        log.warn("HttpUserItemReader: Saved item count ({}) exceeds available records ({}). Resetting.",
                                currentItemCount, allUserRecords.size());
                        currentItemCount = 0;
                        userRecordIterator = allUserRecords.iterator();
                        break;
                    }
                }
            } else {
                log.info("HttpUserItemReader: No previous state found. Starting from the beginning.");
                currentItemCount = 0;
            }
        } catch (Exception e) {
            log.error("HttpUserItemReader: Failed to open stream or fetch data from {}: {}", apiUrl, e.getMessage(), e);
            throw new ItemStreamException("Failed to open HttpUserItemReader stream", e);
        }
    }

    @Override
    public UserRecord read() {
        if (userRecordIterator.hasNext()) {
            UserRecord userRecord = userRecordIterator.next();
            currentItemCount++;
            log.info("- - - Reading user record {}: {}", currentItemCount, userRecord);
            return userRecord;
        } else {
            log.info("Finished reading all user records.");
            return null;
        }
    }

    @Override
    public void update(ExecutionContext executionContext) throws ItemStreamException {
        executionContext.putInt(CURRENT_ITEM_COUNT_KEY, currentItemCount);
        log.info("HttpUserItemReader: Updating state. Saved current item count: {}", currentItemCount);
    }

    @Override
    public void close() throws ItemStreamException {
        log.info("HttpUserItemReader: Closing stream. Total items read: {}", currentItemCount);
        this.allUserRecords = null;
        this.userRecordIterator = null;
        this.currentItemCount = 0;

    }
}
