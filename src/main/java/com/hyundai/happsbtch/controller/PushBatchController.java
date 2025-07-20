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
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/batch")
@RequiredArgsConstructor
public class PushBatchController {

    private final JobLauncher jobLauncher;
    private final Job processPushMessageJob;

    /**
     * 푸시 메시지 배치 작업 수동 실행
     */
    @PostMapping("/push-message")
    public ResponseEntity<Map<String, Object>> runPushMessageJob() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            log.info("푸시 메시지 배치 작업 수동 실행 시작 - {}", LocalDateTime.now());
            
            JobParameters jobParameters = new JobParametersBuilder()
                    .addLong("time", System.currentTimeMillis())
                    .addString("jobType", "MANUAL")
                    .toJobParameters();

            jobLauncher.run(processPushMessageJob, jobParameters);
            
            response.put("success", true);
            response.put("message", "푸시 메시지 배치 작업이 성공적으로 실행되었습니다.");
            response.put("timestamp", LocalDateTime.now());
            
            log.info("푸시 메시지 배치 작업 수동 실행 완료");
            
        } catch (Exception e) {
            log.error("푸시 메시지 배치 작업 수동 실행 중 오류 발생", e);
            
            response.put("success", false);
            response.put("message", "푸시 메시지 배치 작업 실행 중 오류가 발생했습니다: " + e.getMessage());
            response.put("timestamp", LocalDateTime.now());
        }
        
        return ResponseEntity.ok(response);
    }
} 