package com.ntj.hello_spring_batch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.batch.JobLauncherApplicationRunner;


@SpringBootApplication
public class HelloSpringBatchApplication {

    public static void main(String[] args) {
        SpringApplication.run(HelloSpringBatchApplication.class, args);
    }

}
