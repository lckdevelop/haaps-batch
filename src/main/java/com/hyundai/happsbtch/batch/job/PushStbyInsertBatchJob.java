package com.hyundai.happsbtch.batch.job;

import com.hyundai.happsbtch.batch.processor.PushStbyInsertProcessor;
import com.hyundai.happsbtch.batch.reader.PushStbyInsertReader;
import com.hyundai.happsbtch.batch.reader.PushStbyInsertReader.PushStbyInsertDto;
import com.hyundai.happsbtch.batch.writer.PushStbyInsertWriter;
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
public class PushStbyInsertBatchJob {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final PlatformTransactionManager transactionManager;
    private final PushStbyInsertReader reader;
    private final PushStbyInsertProcessor processor;
    private final PushStbyInsertWriter writer;

    @Value("${batch.push.chunk-size:100}")
    private int chunkSize;

    @Bean
    public Job pushStbyInsertJob() {
        return jobBuilderFactory.get("pushStbyInsertJob")
                .start(pushStbyInsertStep())
                .build();
    }

    @Bean
    public Step pushStbyInsertStep() {
        return stepBuilderFactory.get("pushStbyInsertStep")
                .<PushStbyInsertDto, PushSendStbyEntity>chunk(chunkSize)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }
} 