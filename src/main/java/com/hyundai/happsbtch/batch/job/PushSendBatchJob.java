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
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Slf4j
@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
public class PushSendBatchJob {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final PlatformTransactionManager transactionManager;
    private final PushSendStbyReader reader;
    private final PushSendProcessor processor;
    private final PushSendResultWriter writer;

    @Value("${batch.push.chunk-size:100}")
    private int chunkSize;

    @Bean
    public Job pushSendJob() {
        return jobBuilderFactory.get("pushSendJob")
                .start(pushSendStep())
                .build();
    }

    @Bean
    public Step pushSendStep() {
        return stepBuilderFactory.get("pushSendStep")
                .<PushSendStbyEntity, PushSendResult>chunk(chunkSize)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }
} 