package com.hyundai.happsbtch.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/api/batch")
@RequiredArgsConstructor
public class PushBatchController {

    private final JobLauncher jobLauncher;
    private final Job pushStbyInsertJob;
    private final Job pushSendJob;

    /**
     * 발송대기 적재 배치 수동 실행
     */
    @PostMapping("/push-stby-insert")
    public ResponseEntity<String> runPushStbyInsertJob() {
        try {
            log.info("[API] 발송대기 적재 배치 수동 실행 시작: {}", LocalDateTime.now());
            
            JobParameters params = new JobParametersBuilder()
                    .addLong("time", System.currentTimeMillis())
                    .toJobParameters();
            
            jobLauncher.run(pushStbyInsertJob, params);
            
            log.info("[API] 발송대기 적재 배치 수동 실행 완료: {}", LocalDateTime.now());
            return ResponseEntity.ok("발송대기 적재 배치가 성공적으로 실행되었습니다.");
            
        } catch (Exception e) {
            log.error("[API] 발송대기 적재 배치 수동 실행 오류", e);
            return ResponseEntity.internalServerError()
                    .body("발송대기 적재 배치 실행 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    /**
     * 푸시 발송 배치 수동 실행
     */
    @PostMapping("/push-send")
    public ResponseEntity<String> runPushSendJob() {
        try {
            log.info("[API] 푸시 발송 배치 수동 실행 시작: {}", LocalDateTime.now());
            
            JobParameters params = new JobParametersBuilder()
                    .addLong("time", System.currentTimeMillis())
                    .toJobParameters();
            
            jobLauncher.run(pushSendJob, params);
            
            log.info("[API] 푸시 발송 배치 수동 실행 완료: {}", LocalDateTime.now());
            return ResponseEntity.ok("푸시 발송 배치가 성공적으로 실행되었습니다.");
            
        } catch (Exception e) {
            log.error("[API] 푸시 발송 배치 수동 실행 오류", e);
            return ResponseEntity.internalServerError()
                    .body("푸시 발송 배치 실행 중 오류가 발생했습니다: " + e.getMessage());
        }
    }
} 