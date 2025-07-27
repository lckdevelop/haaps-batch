package com.hyundai.happsbtch.schedule;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class PushStbyInsertBatchScheduler {
    private final JobLauncher jobLauncher;
    private final Job pushStbyInsertJob;

    @Scheduled(fixedDelay = 10000)
    public void runPushStbyInsertJob() {
        try {
            log.info("[배치] 발송대기 적재 배치 시작: {}", LocalDateTime.now());
            JobParameters params = new JobParametersBuilder()
                    .addLong("time", System.currentTimeMillis())
                    .toJobParameters();
            jobLauncher.run(pushStbyInsertJob, params);
        } catch (Exception e) {
            log.error("[배치] 발송대기 적재 배치 오류", e);
        }
    }
}