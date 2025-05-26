package com.ntj.springcloudbatchcommon.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.ntj.springcloudbatchcommon.model.UserData;
import com.ntj.springcloudbatchcommon.enums.ReadStatus;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record UserRecordV1(UUID id,
                           String name,
                           ReadStatus readStatus,
                           int iteration,
                           @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
                         LocalDateTime createdAt,
                           @JsonInclude(JsonInclude.Include.NON_NULL)
                         @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
                         LocalDateTime updatedAt) implements UserData {

    public static UserRecordV1 convert(final UserData user) {
        return UserRecordV1.builder()
                .id(user.getId())
                .name(user.getName())
                .readStatus(user.getReadStatus())
                .iteration(user.getIteration())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }

    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public ReadStatus getReadStatus() {
        return readStatus;
    }

    @Override
    public int getIteration() {
        return iteration;
    }

    @Override
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    @Override
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
