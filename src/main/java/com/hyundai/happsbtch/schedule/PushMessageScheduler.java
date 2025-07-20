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
public class PushMessageScheduler {

    private final JobLauncher jobLauncher;
    private final Job processPushMessageJob;

    /**
     * 5초마다 푸시 메시지 배치 작업 실행
     * cron 표현식: 초 분 시 일 월 요일
     */
    @Scheduled(cron = "*/5 * * * * *")
    public void runPushMessageJob() {
        try {
            log.info("푸시 메시지 배치 작업 스케줄러 시작 - {}", LocalDateTime.now());
            
            JobParameters jobParameters = new JobParametersBuilder()
                    .addLong("time", System.currentTimeMillis())
                    .toJobParameters();

            jobLauncher.run(processPushMessageJob, jobParameters);
            
            log.info("푸시 메시지 배치 작업 스케줄러 완료 - {}", LocalDateTime.now());
            
        } catch (Exception e) {
            log.error("푸시 메시지 배치 작업 실행 중 오류 발생", e);
        }
    }
} 