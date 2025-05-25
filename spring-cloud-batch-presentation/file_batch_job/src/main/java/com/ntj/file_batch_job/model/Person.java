package com.ntj.file_batch_job.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Person {

    private String firstName;
    private String lastName;
    private String email;
    private RegistrationState registrationState;

    public enum RegistrationState {
        IN_PROGRESS,
        COMPLETED
    }
}
