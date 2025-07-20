package com.hyundai.happsbtch.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PushMessageDto {
    private Long messageId;
    private String title;
    private String body;
    private String platform; // "AOS" or "IOS"
    private String deviceToken;
    private String userId;
    private Map<String, Object> data;
    private LocalDateTime scheduledTime;
    private String status; // "PENDING", "SENT", "FAILED"
    private Integer retryCount;
} 