package com.hyundai.happsbtch.batch.job;

import com.hyundai.happsbtch.batch.processor.PushSendProcessor;
import com.hyundai.happsbtch.batch.reader.PushSendStbyReader;
import com.hyundai.happsbtch.batch.writer.PushSendResultWriter;
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
public class PushSendBatchConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final PushSendStbyReader pushSendStbyReader;
    private final PushSendProcessor pushSendProcessor;
    private final PushSendResultWriter pushSendResultWriter;
    
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
                .<com.hyundai.happsbtch.entity.PushSendStbyEntity, com.hyundai.happsbtch.batch.processor.PushSendResult>chunk(chunkSize)
                .reader(pushSendStbyReader)
                .processor(pushSendProcessor)
                .writer(pushSendResultWriter)
                .build();
    }
} 