package com.hyundai.happsbtch.batch.job;

import com.hyundai.happsbtch.batch.processor.PushSendProcessor;
import com.hyundai.happsbtch.batch.processor.PushSendResult;
import com.hyundai.happsbtch.batch.reader.PushSendStbyReader;
import com.hyundai.happsbtch.batch.writer.PushSendResultWriter;
import com.hyundai.happsbtch.entity.PushSendStbyEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class PushSendBatchJob {
    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final PushSendStbyReader reader;
    private final PushSendProcessor processor;
    private final PushSendResultWriter writer;

    @Value("${batch.push.chunk-size:100}")
    private int chunkSize;

    @Bean
    public Job pushSendJob() {
        return new JobBuilder("pushSendJob", jobRepository)
                .start(pushSendStep())
                .build();
    }

    @Bean
    public Step pushSendStep() {
        return new StepBuilder("pushSendStep", jobRepository)
                .<PushSendStbyEntity, PushSendResult>chunk(chunkSize, transactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }
} 