package com.hyundai.happsbtch.batch;

import com.hyundai.happsbtch.batch.processor.PushStbyInsertProcessor;
import com.hyundai.happsbtch.batch.reader.PushStbyInsertReader;
import com.hyundai.happsbtch.batch.writer.PushStbyInsertWriter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class PushStbyInsertBatchConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final PushStbyInsertReader pushStbyInsertReader;
    private final PushStbyInsertProcessor pushStbyInsertProcessor;
    private final PushStbyInsertWriter pushStbyInsertWriter;
    
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
                .<PushStbyInsertReader.PushStbyInsertDto, com.hyundai.happsbtch.entity.PushSendStbyEntity>chunk(chunkSize)
                .reader(pushStbyInsertReader)
                .processor(pushStbyInsertProcessor)
                .writer(pushStbyInsertWriter)
                .build();
    }
} 