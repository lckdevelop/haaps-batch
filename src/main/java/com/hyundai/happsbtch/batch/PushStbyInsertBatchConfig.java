package com.hyundai.happsbtch.batch;

import com.hyundai.happsbtch.batch.processor.PushStbyInsertProcessor;
import com.hyundai.happsbtch.batch.reader.PushStbyInsertReader;
import com.hyundai.happsbtch.batch.writer.PushStbyInsertWriter;
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
public class PushStbyInsertBatchConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final PushStbyInsertReader pushStbyInsertReader;
    private final PushStbyInsertProcessor pushStbyInsertProcessor;
    private final PushStbyInsertWriter pushStbyInsertWriter;

    @Bean
    public Job pushStbyInsertJob() {
        return new JobBuilder("pushStbyInsertJob", jobRepository)
                .start(pushStbyInsertStep())
                .build();
    }

    @Bean
    public Step pushStbyInsertStep() {
        return new StepBuilder("pushStbyInsertStep", jobRepository)
                .<PushStbyInsertReader.PushStbyInsertDto, com.hyundai.happsbtch.entity.PushSendStbyEntity>chunk(100, transactionManager)
                .reader(pushStbyInsertReader)
                .processor(pushStbyInsertProcessor)
                .writer(pushStbyInsertWriter)
                .build();
    }
} 