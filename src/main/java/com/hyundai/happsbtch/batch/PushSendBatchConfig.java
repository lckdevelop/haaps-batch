package com.hyundai.happsbtch.batch;

import com.hyundai.happsbtch.batch.processor.PushSendProcessor;
import com.hyundai.happsbtch.batch.reader.PushSendStbyReader;
import com.hyundai.happsbtch.batch.writer.PushSendResultWriter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class PushSendBatchConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final PushSendStbyReader pushSendStbyReader;
    private final PushSendProcessor pushSendProcessor;
    private final PushSendResultWriter pushSendResultWriter;

    @Bean
    public Job pushSendJob() {
        return new JobBuilder("pushSendJob", jobRepository)
                .start(pushSendStep())
                .build();
    }

    @Bean
    public Step pushSendStep() {
        return new StepBuilder("pushSendStep", jobRepository)
                .<com.hyundai.happsbtch.entity.PushSendStbyEntity, com.hyundai.happsbtch.batch.processor.PushSendResult>chunk(100, transactionManager)
                .reader(pushSendStbyReader)
                .processor(pushSendProcessor)
                .writer(pushSendResultWriter)
                .build();
    }
} 