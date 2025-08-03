package com.hyundai.happsbtch.schedule;

import com.hyundai.happsbtch.service.SchedulerLockService;
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
    private final SchedulerLockService schedulerLockService;
    
    private static final String SCHEDULER_NAME = "push-stby-insert-batch";

    @Scheduled(fixedDelay = 10000)
    public void runPushStbyInsertJob() {
        // 스케줄러 락 획득 시도
        if (!schedulerLockService.acquireLock(SCHEDULER_NAME)) {
            log.info("[배치] 발송대기 적재 배치 스킵 - 이미 실행 중");
            return;
        }
        
        try {
            log.info("[배치] 발송대기 적재 배치 시작: {}", LocalDateTime.now());
            JobParameters params = new JobParametersBuilder()
                    .addLong("time", System.currentTimeMillis())
                    .toJobParameters();
            jobLauncher.run(pushStbyInsertJob, params);
            log.info("[배치] 발송대기 적재 배치 완료: {}", LocalDateTime.now());
        } catch (Exception e) {
            log.error("[배치] 발송대기 적재 배치 오류", e);
        } finally {
            // 스케줄러 락 해제
            schedulerLockService.releaseLock(SCHEDULER_NAME);
        }
    }
}