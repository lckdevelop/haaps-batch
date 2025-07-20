package com.hyundai.happsbtch.batch;

import com.hyundai.happsbtch.batch.processor.PushMessageProcessor;
import com.hyundai.happsbtch.batch.reader.PushMessageReader;
import com.hyundai.happsbtch.batch.writer.PushMessageWriter;
import com.hyundai.happsbtch.dto.PushMessageDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class PushBatch {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager platformTransactionManager;
    private final PushMessageReader pushMessageReader;
    private final PushMessageProcessor pushMessageProcessor;
    private final PushMessageWriter pushMessageWriter;

    @Value("${batch.push.chunk-size:100}")
    private int chunkSize;

    @Bean
    public Job processPushMessageJob() {
        log.info("푸시 메시지 배치 작업 설정 - processPushMessageJob");
        
        return new JobBuilder("processPushMessageJob", jobRepository)
                .start(pushMessageStartStep())
                .next(pushMessageProcessStep())
                .next(pushMessageEndStep())
                .build();
    }

    @Bean
    public Step pushMessageStartStep() {
        return new StepBuilder("pushMessageStartStep", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    log.info("=== 푸시 메시지 배치 작업 시작 ===");
                    return RepeatStatus.FINISHED;
                }, platformTransactionManager)
                .build();
    }

    @Bean
    public Step pushMessageProcessStep() {
        return new StepBuilder("pushMessageProcessStep", jobRepository)
                .<PushMessageDto, PushMessageDto>chunk(chunkSize, platformTransactionManager)
                .reader(pushMessageReader)
                .processor(pushMessageProcessor)
                .writer(pushMessageWriter)
                .build();
    }

    @Bean
    public Step pushMessageEndStep() {
        return new StepBuilder("pushMessageEndStep", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    log.info("=== 푸시 메시지 배치 작업 완료 ===");
                    return RepeatStatus.FINISHED;
                }, platformTransactionManager)
                .build();
    }
}
