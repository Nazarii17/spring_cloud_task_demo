package com.ntj.springcloudbatchcommon.model;

import com.ntj.springcloudbatchcommon.enums.ReadStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public interface UserData {

    UUID getId();

    String getName();

    ReadStatus getReadStatus();

    int getIteration();

    LocalDateTime getCreatedAt();

    LocalDateTime getUpdatedAt();

}
