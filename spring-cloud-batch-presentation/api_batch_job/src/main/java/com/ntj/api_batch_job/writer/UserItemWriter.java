package com.ntj.api_batch_job.writer;

import com.ntj.api_batch_job.entity.ApiUser;
import com.ntj.api_batch_job.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;


@Slf4j
@Component
@RequiredArgsConstructor
public class UserItemWriter implements ItemWriter<ApiUser> {

    private final UserRepository userRepository;

    @Override
    public void write(Chunk<? extends ApiUser> chunk) {
        log.info("Attempting to write {} users.", chunk.size());

        try {
            List<ApiUser> mergedUsers = chunk.getItems().stream()
                    .map(incomingUser -> {
                        UUID id = incomingUser.getId();
                        if (id == null) {
                            return ApiUser.builder()
                                    .name(incomingUser.getName())
                                    .readStatus(incomingUser.getReadStatus())
                                    .iteration(incomingUser.getIteration())
                                    .build();
                        }

                        return userRepository.findById(id)
                                .map(existing -> {
                                    existing.setName(incomingUser.getName());
                                    existing.setIteration(incomingUser.getIteration());
                                    existing.setReadStatus(incomingUser.getReadStatus());
                                    return existing;
                                })
                                .orElseGet(() -> {
                                    log.warn("= = = User with ID {} not found in DB. Creating new record without ID.", id);
                                    return ApiUser.builder()
                                            .name(incomingUser.getName())
                                            .readStatus(incomingUser.getReadStatus())
                                            .iteration(incomingUser.getIteration())
                                            .build();
                                });
                    })
                    .toList();

            userRepository.saveAll(mergedUsers);
            log.info("Successfully wrote {} users.", mergedUsers.size());
        } catch (Exception e) {
            log.error("Error writing chunk of users: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to write user chunk.", e);
        }
    }
}

