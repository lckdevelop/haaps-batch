package com.hyundai.happsbtch.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class SchedulerLockService {

    private final RedisTemplate<String, String> redisTemplate;
    
    private static final String LOCK_PREFIX = "scheduler:lock:";
    private static final long DEFAULT_LOCK_DURATION_SECONDS = 300; // 5분
    
    /**
     * 스케줄러 락을 획득합니다.
     * @param schedulerName 스케줄러 이름
     * @return 락 획득 성공 여부
     */
    public boolean acquireLock(String schedulerName) {
        String lockKey = LOCK_PREFIX + schedulerName;
        
        try {
            Boolean result = redisTemplate.opsForValue()
                    .setIfAbsent(lockKey, "locked", DEFAULT_LOCK_DURATION_SECONDS, TimeUnit.SECONDS);
            
            boolean acquired = Boolean.TRUE.equals(result);
            if (acquired) {
                log.info("[스케줄러 락] {} 스케줄러 락 획득 성공", schedulerName);
            } else {
                log.info("[스케줄러 락] {} 스케줄러 락 획득 실패 (이미 실행 중)", schedulerName);
            }
            
            return acquired;
        } catch (Exception e) {
            log.error("[스케줄러 락] {} 스케줄러 락 획득 중 오류 발생", schedulerName, e);
            return false;
        }
    }
    
    /**
     * 스케줄러 락을 해제합니다.
     * @param schedulerName 스케줄러 이름
     */
    public void releaseLock(String schedulerName) {
        String lockKey = LOCK_PREFIX + schedulerName;
        
        try {
            Boolean result = redisTemplate.delete(lockKey);
            if (Boolean.TRUE.equals(result)) {
                log.info("[스케줄러 락] {} 스케줄러 락 해제 성공", schedulerName);
            } else {
                log.warn("[스케줄러 락] {} 스케줄러 락 해제 실패 (락이 존재하지 않음)", schedulerName);
            }
        } catch (Exception e) {
            log.error("[스케줄러 락] {} 스케줄러 락 해제 중 오류 발생", schedulerName, e);
        }
    }
    
    /**
     * 스케줄러 락 상태를 확인합니다.
     * @param schedulerName 스케줄러 이름
     * @return 락이 존재하는지 여부
     */
    public boolean isLocked(String schedulerName) {
        String lockKey = LOCK_PREFIX + schedulerName;
        
        try {
            String value = redisTemplate.opsForValue().get(lockKey);
            return value != null;
        } catch (Exception e) {
            log.error("[스케줄러 락] {} 스케줄러 락 상태 확인 중 오류 발생", schedulerName, e);
            return false;
        }
    }
} 