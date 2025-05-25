package com.example.demo.task;

import org.springframework.boot.CommandLineRunner;
import org.springframework.cloud.deployer.spi.core.AppDefinition;
import org.springframework.cloud.deployer.spi.core.AppDeploymentRequest;
import org.springframework.cloud.deployer.spi.local.LocalDeployerProperties;
import org.springframework.cloud.deployer.spi.local.LocalTaskLauncher;
import org.springframework.cloud.deployer.spi.task.TaskLauncher;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.Files;
import java.util.Collections;

@Component
public class TaskExecutorRunner implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        final LocalDeployerProperties props = new LocalDeployerProperties();
//        props.setWorkingxDirectoriesRoot("/Users/nazarii.tkachuk/dev/task-logs");

        final TaskLauncher launcher = new LocalTaskLauncher(props);

        final File jarFile = extractJarFromResources();
        final AppDefinition definition = new AppDefinition("first-task", Collections.emptyMap());
        final AppDeploymentRequest request = new AppDeploymentRequest(definition, new FileSystemResource(jarFile));

        final String taskId = launcher.launch(request);
        System.out.println("Launched task with ID: " + taskId);
    }

    private File extractJarFromResources() throws IOException {
        final InputStream jarStream = getClass().getClassLoader().getResourceAsStream("tasks/first_task-1.0-SNAPSHOT.jar");
        if (jarStream == null) {
            throw new FileNotFoundException("Resource not found: " + "tasks/first_task-1.0-SNAPSHOT.jar");
        }

        final File tempJar = Files.createTempFile("task-", ".jar").toFile();
        try (OutputStream out = new FileOutputStream(tempJar)) {
            jarStream.transferTo(out);
        }

        return tempJar;
    }
}

