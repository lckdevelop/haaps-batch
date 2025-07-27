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
public class PushStbyInsertBatchJob {
    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final PushStbyInsertReader reader;
    private final PushStbyInsertProcessor processor;
    private final PushStbyInsertWriter writer;

    @Value("${batch.push.chunk-size:100}")
    private int chunkSize;

    @Bean
    public Job pushStbyInsertJob() {
        return new JobBuilder("pushStbyInsertJob", jobRepository)
                .start(pushStbyInsertStep())
                .build();
    }

    @Bean
    public Step pushStbyInsertStep() {
        return new StepBuilder("pushStbyInsertStep", jobRepository)
                .<PushStbyInsertDto, PushSendStbyEntity>chunk(chunkSize, transactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }
} 