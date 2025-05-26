package com.ntj.api_batch_job.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import com.ntj.api_batch_job.enums.ReadStatus;
import lombok.Builder;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record UserRecord(UUID id,
                         String name,
                         ReadStatus readStatus,
                         int iteration,
                         @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
                         LocalDateTime createdAt,
                         @JsonInclude(JsonInclude.Include.NON_NULL)
                         @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
                         LocalDateTime updatedAt) implements Serializable {
}
