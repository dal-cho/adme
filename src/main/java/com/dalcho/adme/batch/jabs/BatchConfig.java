package com.dalcho.adme.batch.jabs;

import com.dalcho.adme.batch.tasklets.BatchTasklet;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class BatchConfig {
    private final JobBuilderFactory jobBuilderFactory; // job 빌더 생성용
    private final StepBuilderFactory stepBuilderFactory; // Step 빌더 생성용
    private final BatchTasklet batchTasklet;

    // JobBuilderFactory를 통해서 Job을 생성
    @Bean
    public Job VideoJob() {
        return jobBuilderFactory.get("VideoJob")
                .start(Step())  // Step 설정
                .build();
    }

    // StepBuilderFactory를 통해서 tutorialStep을 생성
    @Bean
    @JobScope
    public Step Step() {
        return stepBuilderFactory.get("CleanUpStep")
                .tasklet(batchTasklet)
                .build();
    }
}
