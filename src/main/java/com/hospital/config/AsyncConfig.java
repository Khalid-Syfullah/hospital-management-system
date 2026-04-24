package com.hospital.config;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.core.task.support.TaskExecutorAdapter;

@Configuration
public class AsyncConfig {

    @Bean
    AsyncTaskExecutor applicationTaskExecutor() {
        ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();
        return new TaskExecutorAdapter(executor);
    }
}
