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
    private final Job pushStbyInsertJob;  // Batch 1: 데이터 적재
    private final Job pushSendJob;        // Batch 2: 실제 발송

    /**
     * 데이터 적재 배치 작업 수동 실행 (Batch 1)
     * 마스터+타겟 → 대기 테이블 적재
     */
    @PostMapping("/push-stby-insert")
    public ResponseEntity<Map<String, Object>> runPushStbyInsertJob() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            log.info("데이터 적재 배치 작업 수동 실행 시작 - {}", LocalDateTime.now());
            
            JobParameters jobParameters = new JobParametersBuilder()
                    .addLong("time", System.currentTimeMillis())
                    .addString("jobType", "MANUAL_STBY_INSERT")
                    .toJobParameters();

            jobLauncher.run(pushStbyInsertJob, jobParameters);
            
            response.put("success", true);
            response.put("message", "데이터 적재 배치 작업이 성공적으로 실행되었습니다.");
            response.put("timestamp", LocalDateTime.now());
            
            log.info("데이터 적재 배치 작업 수동 실행 완료");
            
        } catch (Exception e) {
            log.error("데이터 적재 배치 작업 수동 실행 중 오류 발생", e);
            
            response.put("success", false);
            response.put("message", "데이터 적재 배치 작업 실행 중 오류가 발생했습니다: " + e.getMessage());
            response.put("timestamp", LocalDateTime.now());
        }
        
        return ResponseEntity.ok(response);
    }

    /**
     * 실제 발송 배치 작업 수동 실행 (Batch 2)
     * 대기 테이블 → FCM/APNS 발송 → 결과 저장
     */
    @PostMapping("/push-send")
    public ResponseEntity<Map<String, Object>> runPushSendJob() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            log.info("실제 발송 배치 작업 수동 실행 시작 - {}", LocalDateTime.now());
            
            JobParameters jobParameters = new JobParametersBuilder()
                    .addLong("time", System.currentTimeMillis())
                    .addString("jobType", "MANUAL_PUSH_SEND")
                    .toJobParameters();

            jobLauncher.run(pushSendJob, jobParameters);
            
            response.put("success", true);
            response.put("message", "실제 발송 배치 작업이 성공적으로 실행되었습니다.");
            response.put("timestamp", LocalDateTime.now());
            
            log.info("실제 발송 배치 작업 수동 실행 완료");
            
        } catch (Exception e) {
            log.error("실제 발송 배치 작업 수동 실행 중 오류 발생", e);
            
            response.put("success", false);
            response.put("message", "실제 발송 배치 작업 실행 중 오류가 발생했습니다: " + e.getMessage());
            response.put("timestamp", LocalDateTime.now());
        }
        
        return ResponseEntity.ok(response);
    }
} 